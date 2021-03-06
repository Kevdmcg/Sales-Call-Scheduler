/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointment;
import Model.Customer;
import Model.User;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import util.DBConnection;
import static util.DBConnection.conn;
import static util.Query.getResult;
import static util.Query.makeQuery;

/**
 * FXML Controller class
 *
 * @author kmcgh15
 */
public class ReportsController implements Initializable {

    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    ObservableList<String> companyList = FXCollections.observableArrayList();
    ObservableList<Integer> monthlyCount = FXCollections.observableArrayList();
    private final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ZoneId localTimeZoneId = ZoneId.systemDefault();
    private final ZoneId utcTimeZoneId = ZoneId.of("UTC");

    Parent root;
    Stage stage;

    @FXML
    private MenuButton TypeMenuButton;
    @FXML
    private Label JanCountLabel;
    @FXML
    private Label DecCountLabel;
    @FXML
    private Label NovCountLabel;
    @FXML
    private Label OctCountLabel;
    @FXML
    private Label SepCountLabel;
    @FXML
    private Label AugCountLabel;
    @FXML
    private Label JulCountLabel;
    @FXML
    private Label JunCountLabel;
    @FXML
    private Label MayCountLabel;
    @FXML
    private Label AprCountLabel;
    @FXML
    private Label MarCountLabel;
    @FXML
    private Label FebCountLabel;
    @FXML
    private TableView<Appointment> UserScheduleTable;
    @FXML
    private Button BackButton;
    @FXML
    private TableColumn<Appointment, String> ScheduleUserColumn;
    @FXML
    private TableColumn<Appointment, String> ScheduleStartColumn;
    @FXML
    private TableColumn<Appointment, String> ScheduleEndColumn;
    @FXML
    private TableColumn<Appointment, String> ScheduleClientColumn;
    @FXML
    private MenuItem SalesMenuItem;
    @FXML
    private MenuItem BillingMenuItem;
    @FXML
    private MenuItem AdminMenuItem;
    @FXML
    private ChoiceBox<String> TableThreeCustomerChoiceBox;
    @FXML
    private Label TableThreeCountLabel;

    /**
     * Initializes the controller class.
     *
     */
    // This listener updates the label for the third report once a customer has been selected by calling the getCustomerAppointmentCount() to change the label text
    private void tableThreeListener() {
        TableThreeCustomerChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue ov, String value, String new_value) {
                if (new_value != null) {
                    String customerName = TableThreeCustomerChoiceBox.getValue();
                    try {
                        TableThreeCountLabel.setText(getCustomerAppointmentCount(customerName));
                    } catch (SQLException ex) {
                        Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
        });
    }

    // This retrieves the counts/by/type for appointments per month from the database
    private String getMonthlyTypeCount(int month, String type) throws SQLException {
        if (month < 10) {
            makeQuery("SELECT count(*) from appointment where start >= '2021-0" + month + "-01' and start < '2021-0" + month + "-31' and TYPE='" + type + "';");
            ResultSet count = getResult();
            count.first();
            String counter = Integer.toString(count.getInt("count(*)"));
            return counter;
        } else {
            makeQuery("SELECT count(*) from appointment where start >= '2021-" + month + "-01' and start < '2021-" + month + "-31' and TYPE='" + type + "';");
            ResultSet count = getResult();
            count.first();
            String counter = Integer.toString(count.getInt("count(*)"));
            return counter;

        }
    }
    
    // This counts the number of appointments for a given customer for table #3 
    private String getCustomerAppointmentCount(String customerName) throws SQLException {
        makeQuery("SELECT customerId from customer where customerName = '" + customerName + "'");
        ResultSet custId = getResult();
        custId.first();
        int queryInt = custId.getInt("customerId");

        makeQuery("SELECT COUNT(*) FROM appointment WHERE customerId = " + queryInt);
        ResultSet countSet = getResult();
        countSet.first();
        String returnCount = Integer.toString(countSet.getInt("count(*)"));
        return returnCount;
    }
    
    // gets the valid customers to fill choiceBox
    private void fillCustomerChoiceBox() throws SQLException {
        PreparedStatement fillCustomer;
        fillCustomer = conn.prepareStatement("SELECT customerName FROM customer");
        ResultSet customers = fillCustomer.executeQuery();

        while (customers.next()) {
            Customer cust = new Customer();
            cust.setCustomerName(customers.getString("customerName"));
            companyList.add(cust.getCustomerName());
            TableThreeCustomerChoiceBox.setItems(companyList);

        }
    }
    
    // updates the tableView when new information needs displayed
    private boolean updateScheduleTable() throws SQLException {
        appointmentList.clear();
        String sqlStatement = ("SELECT userName, start, end, customerName FROM user, appointment, customer "
                + "WHERE appointment.userId = user.userId and appointment.customerId = customer.customerId order by userName, start");
        PreparedStatement ps;
        ps = conn.prepareStatement(sqlStatement);
        Statement stmt = DBConnection.conn.createStatement();
        ResultSet result = stmt.executeQuery(sqlStatement);
        result.beforeFirst();
        while (result.next()) {
            Appointment appointment = new Appointment();
            Customer customer = new Customer(result.getString("customerName"));
            User user = new User(result.getString("userName"));
            appointment.setCustomerName(result.getString("customerName"));
            appointment.setUserName(result.getString("userName"));
            String startUTC = result.getString("start").substring(0, 19);
            String endUTC = result.getString("end").substring(0, 19);

            LocalDateTime utcStartDT = LocalDateTime.parse(startUTC, dateTimeFormat);
            LocalDateTime utcEndDT = LocalDateTime.parse(endUTC, dateTimeFormat);

            ZonedDateTime localZoneStart = utcStartDT.atZone(utcTimeZoneId).withZoneSameInstant(localTimeZoneId);
            ZonedDateTime localZoneEnd = utcEndDT.atZone(utcTimeZoneId).withZoneSameInstant(localTimeZoneId);

            String localStartDT = localZoneStart.format(dateTimeFormat);
            String localEndDT = localZoneEnd.format(dateTimeFormat);
            appointment.setStart(localStartDT);
            appointment.setEnd(localEndDT);
            appointmentList.addAll(appointment);
        }
        UserScheduleTable.setItems(appointmentList);
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PropertyValueFactory<Appointment, String> userNameFactory = new PropertyValueFactory<>("UserName");
        PropertyValueFactory<Appointment, String> custDateFactory = new PropertyValueFactory<>("Start");
        PropertyValueFactory<Appointment, String> custStartFactory = new PropertyValueFactory<>("End");
        PropertyValueFactory<Appointment, String> custEndFactory = new PropertyValueFactory<>("CustomerName");

        ScheduleUserColumn.setCellValueFactory(userNameFactory);
        ScheduleStartColumn.setCellValueFactory(custDateFactory);
        ScheduleEndColumn.setCellValueFactory(custStartFactory);
        ScheduleClientColumn.setCellValueFactory(custEndFactory);

        try {
            updateScheduleTable();
        } catch (SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fillCustomerChoiceBox();
        } catch (SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableThreeListener();
    }

    @FXML
    private void TypeMenuButtonHandler(ActionEvent event) {
    }

    @FXML
    private void BackButtonHandler(ActionEvent event) throws IOException {

        root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        stage = (Stage) BackButton.getScene().getWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void SalesMenuItemHandler(ActionEvent event) throws SQLException {
        String type = "Sales";
        JanCountLabel.setText(getMonthlyTypeCount(1, type));
        FebCountLabel.setText(getMonthlyTypeCount(2, type));
        MarCountLabel.setText(getMonthlyTypeCount(3, type));
        AprCountLabel.setText(getMonthlyTypeCount(4, type));
        MayCountLabel.setText(getMonthlyTypeCount(5, type));
        JunCountLabel.setText(getMonthlyTypeCount(6, type));
        JulCountLabel.setText(getMonthlyTypeCount(7, type));
        AugCountLabel.setText(getMonthlyTypeCount(8, type));
        SepCountLabel.setText(getMonthlyTypeCount(9, type));
        OctCountLabel.setText(getMonthlyTypeCount(10, type));
        NovCountLabel.setText(getMonthlyTypeCount(11, type));
        DecCountLabel.setText(getMonthlyTypeCount(12, type));
        TypeMenuButton.setText(type);
    }

    @FXML
    private void BillingMenuItemHandler(ActionEvent event) throws SQLException {
        String type = "Billing";
        JanCountLabel.setText(getMonthlyTypeCount(1, type));
        FebCountLabel.setText(getMonthlyTypeCount(2, type));
        MarCountLabel.setText(getMonthlyTypeCount(3, type));
        AprCountLabel.setText(getMonthlyTypeCount(4, type));
        MayCountLabel.setText(getMonthlyTypeCount(5, type));
        JunCountLabel.setText(getMonthlyTypeCount(6, type));
        JulCountLabel.setText(getMonthlyTypeCount(7, type));
        AugCountLabel.setText(getMonthlyTypeCount(8, type));
        SepCountLabel.setText(getMonthlyTypeCount(9, type));
        OctCountLabel.setText(getMonthlyTypeCount(10, type));
        NovCountLabel.setText(getMonthlyTypeCount(11, type));
        DecCountLabel.setText(getMonthlyTypeCount(12, type));
        TypeMenuButton.setText(type);
    }

    @FXML
    private void AdminMenuItemHandler(ActionEvent event) throws SQLException {
        String type = "Admin";
        JanCountLabel.setText(getMonthlyTypeCount(1, type));
        FebCountLabel.setText(getMonthlyTypeCount(2, type));
        MarCountLabel.setText(getMonthlyTypeCount(3, type));
        AprCountLabel.setText(getMonthlyTypeCount(4, type));
        MayCountLabel.setText(getMonthlyTypeCount(5, type));
        JunCountLabel.setText(getMonthlyTypeCount(6, type));
        JulCountLabel.setText(getMonthlyTypeCount(7, type));
        AugCountLabel.setText(getMonthlyTypeCount(8, type));
        SepCountLabel.setText(getMonthlyTypeCount(9, type));
        OctCountLabel.setText(getMonthlyTypeCount(10, type));
        NovCountLabel.setText(getMonthlyTypeCount(11, type));
        DecCountLabel.setText(getMonthlyTypeCount(12, type));
        TypeMenuButton.setText(type);
    }

}
