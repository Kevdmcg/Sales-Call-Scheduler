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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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
    private final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    //private final ZoneId localZoneID = ZoneId.of("UTC-8");
    private final ZoneId localZoneID = ZoneId.systemDefault();
    private final ZoneId utcZoneID = ZoneId.of("UTC");
    
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
    
    private void branchListener() {
        System.out.println();
        System.out.println("++++ branchListener routine++++");
        TableThreeCustomerChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
        
            public void changed(ObservableValue ov, String value, String new_value) {
                if (new_value != null) {
                    System.out.println("listener saw change");
                    String customerName = TableThreeCustomerChoiceBox.getValue();
                    System.out.println("customername variable: " +customerName);
                    try {
                        TableThreeCountLabel.setText(getCustomerAppointmentCount(customerName));
                    } catch (SQLException ex) {
                        Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
                
            }
    });
        System.out.println("++++ branchListener routine++++");
        System.out.println();
    }
    
    private boolean fillCustomerAppointmentCountTable() {
        
        //makeQuery();
        
        
        return true;
    }
    
    private String getMonthlyTypeCount(int month, String type) throws SQLException {
        if (month < 10) {
        makeQuery("SELECT count(*) from appointment where start >= '2021-0" + month + "-01' and start < '2021-0" + month + "-31' and TYPE='" + type +"';");
        ResultSet count = getResult();
        ResultSetMetaData rsmd = count.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (count.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
            if (i > 1) System.out.print(",  ");
            String columnValue = count.getString(i);
            System.out.print(columnValue + " " + rsmd.getColumnName(i));
        }
        System.out.println("");
        }
        //count.beforeFirst();
        count.first();
        String counter = Integer.toString(count.getInt("count(*)"));
        System.out.println("resultset: " + count.getInt("count(*)"));
        System.out.println("String counter: " + counter);
        return counter;
        }
        else {
            makeQuery("SELECT count(*) from appointment where start >= '2021-" + month + "-01' and start < '2021-" + month + "-31' and TYPE='" + type +"';");
        ResultSet count = getResult();
        ResultSetMetaData rsmd = count.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (count.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
            if (i > 1) System.out.print(",  ");
            String columnValue = count.getString(i);
            System.out.print(columnValue + " " + rsmd.getColumnName(i));
        }
        System.out.println("");
        }
        //count.beforeFirst();
        count.first();
        String counter = Integer.toString(count.getInt("count(*)"));
        System.out.println("resultset: " + count.getInt("count(*)"));
        System.out.println("String counter: " + counter);
        return counter;
            
        }
    }
    
    private String getCustomerAppointmentCount(String customerName) throws SQLException {
        makeQuery("SELECT customerId from customer where customerName = '" + customerName + "'");
        System.out.println("customer Id query made");
        ResultSet custId = getResult();
        custId.first();
        int queryInt = custId.getInt("customerId");
        System.out.println(customerName +" : " +custId);
        
        makeQuery("SELECT COUNT(*) FROM appointment WHERE customerId = " + queryInt);
        ResultSet countSet = getResult();
        countSet.first();
        String returnCount = Integer.toString(countSet.getInt("count(*)"));
        return returnCount;
    }
    
    private void fillCustomerChoiceBox() throws SQLException {
        System.out.println();
        System.out.println("++++ fillCustomerList routine++++");
        PreparedStatement fillCustomer;
        fillCustomer =(PreparedStatement) conn.prepareStatement("SELECT customerName FROM customer");
        ResultSet customers = fillCustomer.executeQuery();
        
        while (customers.next()) {
            Customer cust = new Customer();
            cust.setCustomerName(customers.getString("customerName"));
            companyList.add(cust.getCustomerName());
            TableThreeCustomerChoiceBox.setItems(companyList);

        }
        System.out.println("++++ fillCustomerList routine++++");
        System.out.println();
       // System.out.println("FILL CUSTOMER LIST DONE");
    }
    
    private boolean updateScheduleTable() throws SQLException {
        System.out.println();
        System.out.println("++++ updateAppointmentsTableView routine++++");
        appointmentList.clear();
        
        String sqlStatement = ("SELECT userName, start, end, customerName FROM user, appointment, customer "
                + "WHERE appointment.userId = user.userId and appointment.customerId = customer.customerId order by userName, start");
            
        /*String sqlStatement = ("SELECT appointment.appointmentId, appointment.customerId, appointment.userId, appointment.title, appointment.description, "
                + "appointment.location, appointment.contact, appointment.type, appointment.url, appointment.start, appointment.end, "
                + "appointment.createdBy, customer.customerId, customer.customerName "
                + "FROM appointment, customer "
                + "WHERE appointment.customerId = customer.customerId "
                + "ORDER BY `start`");
        */
        PreparedStatement ps;
        ps =(PreparedStatement) conn.prepareStatement(sqlStatement);
                    System.out.println("PreparedStament: " + ps);
        Statement stmt = DBConnection.conn.createStatement();
        ResultSet result = stmt.executeQuery(sqlStatement);
        System.out.println("Appointment Table query worked");
        ResultSetMetaData rsmd = result.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (result.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
            if (i > 1) System.out.print(",  ");
            String columnValue = result.getString(i);
            System.out.print(columnValue + " " + rsmd.getColumnName(i));
        }
        System.out.println("");
        }
        result.beforeFirst();
        while (result.next()) {
            Appointment appointment = new Appointment();
            Customer customer = new Customer(result.getString("customerName"));
            User user = new User(result.getString("userName"));
          //  System.out.println(result.getString("customerName"));
            //appointment.setStart(result.getString("start"));
            //appointment.setEnd(result.getString("end"));
            appointment.setCustomerName(result.getString("customerName"));
            appointment.setUserName(result.getString("userName"));
            
            

            String startUTC = result.getString("start").substring(0, 19);
            System.out.println("start in UTC: " + startUTC);
            String endUTC = result.getString("end").substring(0, 19);

            LocalDateTime utcStartDT = LocalDateTime.parse(startUTC, datetimeDTF);
            LocalDateTime utcEndDT = LocalDateTime.parse(endUTC, datetimeDTF);

            ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
            ZonedDateTime localZoneEnd = utcEndDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);

            String localStartDT = localZoneStart.format(datetimeDTF);
            String localEndDT = localZoneEnd.format(datetimeDTF);
            System.out.println("localStartDT = " + localStartDT);
            System.out.println("localStartSubString : " + localStartDT.substring(11));
           // System.out.println(localStartDT);


            appointment.setStart(localStartDT);
            appointment.setEnd(localEndDT);
            //appointment.setAppointmentDate(localStartDT.substring(0,10));
           // System.out.println(appointment.getStart());
           // System.out.println(appointment.getEnd());
           // System.out.println(appointment.getAppointmentDate());

           //String localDate = localZoneStart.format(date);
           //System.out.println("Formatted localDate String: " +localDate);

           //LocalDate updateCalendar = LocalDate.parse(localDate);
           //MeetingDateCalendar.setValue(updateCalendar);

          // MeetingDateCalendar.setDate

            //
            //

            //appointment.setUserId(result.getInt("userId"));

            //appointment.setTitle(result.getString("title"));
            //System.out.println(result.getString("title"));
           // System.out.println(appointment.getTitle());

            //appointment.setDescription(result.getString("description"));

            //appointment.setContact(result.getString("contact"));

            //appointment.setStart(result.getString("start"));

            //appointment.setEnd(result.getString("end"));

            //appointment.setCreatedBy(result.getString("createdBy"));
           // System.out.println(result.getString("createdBy"));

            //appointment.setLocation(result.getString("location"));

            //appointment.setType(result.getString("type"));

            //appointment.setUrl(result.getString("url"));
            //appointment.setDateTime(result.getString("start"));
           // System.out.println("Appointment.getDateTime : " + appointment.getDateTime());



            //appointmentList.add(new appointment(appointmentId, customerId, userId, title, description, location, contact, type, url, localStartDT, localEndDT, customerName, user));


            appointmentList.addAll(appointment);
          //  System.out.println("appointment added to appoint");
                


        
    }
        UserScheduleTable.setItems(appointmentList);
        System.out.println("***Table Set*** Filter Routine Next");
        
       // System.out.println(datetimeDTF);
       // System.out.println("***** End Update Customer Table *****");
        System.out.println("++++ updateAppointmentsTableView routine++++");
        System.out.println(); 
        
        return true;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // FILL TABLE #1 Appointments by type/month
        
        // FILL TABLE TWO Schedule by userId
         System.out.println("Initial Appointments DB Read and populate");
        
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
        
        
            // FILL TABLE 3 Appointments by Customer Appointments counts
            
        try {
            fillCustomerChoiceBox();   
            // TODO
        } catch (SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        branchListener();
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
        
        /*int i=1;
        while (i<13) {
        monthlyCount.add(getMonthlyTypeCount(i));
        i++;
        }*/
        
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
