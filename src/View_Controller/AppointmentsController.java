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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
public class AppointmentsController implements Initializable {

    @FXML
    private Button AddAppointmentButton;
    @FXML
    private Button DeleteSelectedButton;
    @FXML
    private Button EditSelectedButton;
    @FXML
    private Button BackButton;
    @FXML
    private TextField MeetingTitleTextField;
    @FXML
    private TextField MeetingDescriptionTextField;
    @FXML
    private ChoiceBox<String> MeetingTypeChoiceBox;
    @FXML
    private DatePicker MeetingDateCalendar;
    @FXML
    private ChoiceBox<LocalTime> StartTimeChoiceBox;
    @FXML
    private TextField UrlLabel;
    @FXML
    private Label CreatedByLabel;
    @FXML
    private Label LastUpdatedLabel;
    @FXML
    private Button SaveAppointmentButton;
    @FXML
    private Button ClearFormButton;
    @FXML
    private RadioButton MonthlyViewRadio;
    @FXML
    private RadioButton WeeklyViewRadio;
    @FXML
    private TableView<Appointment> AppointmentsTableView;
    @FXML
    private TableColumn<Appointment, String> CustomerNameColumn;
    @FXML
    private TableColumn<Appointment, String> StartTimeColumn;
    @FXML
    private TableColumn<Appointment, String> EndTimeColumn;
    @FXML
    private TableColumn<Appointment, String> CreatedByColumn;
    @FXML
    private TableColumn<Appointment, String> LocationColumn;
    @FXML
    private TableColumn<Appointment, String> TypeColumn;
    @FXML
    private TableColumn<Appointment, String> DateColumn;
    @FXML
    private TextField contactTextBox;
    @FXML
    private RadioButton ViewAllRadio;
    @FXML
    private ChoiceBox<String> CompanyNameChoiceBox;
    @FXML
    private Label BranchLocationLabel;
    @FXML
    private Button CancelInputButton;
    @FXML
    private ChoiceBox<String> DurationChoiceBox;
    @FXML
    private Label EndTimeLabel;
    
        
    private final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ZoneId localZoneID = ZoneId.systemDefault();
    private final ZoneId utcZoneID = ZoneId.of("UTC");
    private boolean isWeekly;
    private int isWeeklyInt;
    private boolean isMonthly;
    private int isMonthlyInt;
    private LocalTime endTime;
    private int durationInt;
    LocalTime startTime;
    private boolean isNew;
    
    
    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    ObservableList<String> companyList = FXCollections.observableArrayList();
    ObservableList<String> typeList = FXCollections.observableArrayList();
    ObservableList<LocalTime> startList = FXCollections.observableArrayList();
    ObservableList<String> durationList = FXCollections.observableArrayList();
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    
    private void branchListener() {
        CompanyNameChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
        
            public void changed(ObservableValue ov, String value, String new_value) {
                if (new_value != null) {
                    String customerName = CompanyNameChoiceBox.getValue();
                    makeQuery("SELECT city.city FROM city, address, customer WHERE city.cityId = address.cityId AND"
                            + " customer.addressId = address.addressId AND customerName = '" + customerName + "'");
                    ResultSet branch = getResult();
                    try {
                        while (branch.next()){
                            try {
                                BranchLocationLabel.setText(branch.getString("city"));
                            } catch (SQLException ex) {
                                Logger.getLogger(AppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(AppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
    });
    }
    
    private void startListener() {
        StartTimeChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LocalTime>() {
            @Override
            public void changed(ObservableValue ov, LocalTime value, LocalTime new_value){
                  if (StartTimeChoiceBox != null && new_value != null) {
                      LocalTime startTime = new_value;
                      //System.out.println("startTime = " + startTime);
                    }
                }
        });
    }

    private void durationListener() {
    DurationChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue ov, String value, String new_value){
                if ( new_value != null && StartTimeChoiceBox != null ) {
                    LocalTime closeTime = LocalTime.of(17, 00, 00);
                    String durationTime = new_value;
                    int durationInt = Integer.parseInt(durationTime.substring(0, 2));
                    LocalTime startTime = StartTimeChoiceBox.getValue();
                    LocalTime endTime = startTime.plusMinutes(durationInt);
                    int closeValue = endTime.compareTo(closeTime);
                    if (closeValue > 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setHeaderText("Appointment Schedule Error");
                        alert.setContentText("This Meeting is scheduled to end after closing time.  Adjust start time or duration.");
                        Optional<ButtonType> result = alert.showAndWait();
                        DurationChoiceBox.setValue(null);
                    }
                  
                  String endLabel = endTime.format(timeDTF);
                  
                  EndTimeLabel.setText(endLabel);
                }
            }
        });
    }
    
    @FXML
    private void appointmentMouseClick(MouseEvent event) throws SQLException, Exception {
        System.out.println();
        System.out.println("++++ appointmentMouseClick routine++++");
       // System.out.println("tableMouseClick activated");
        
        Appointment appointment = AppointmentsTableView.getSelectionModel().getSelectedItem();
        int idLookup = appointment.getAppointmentId();
        //LocalTime formStart = appointment.getStart();
        
        makeQuery("SELECT * FROM appointment, customer WHERE appointment.appointmentId = " + idLookup + " AND customer.customerId = appointment.customerId ");
        ResultSet clickSet = getResult();
        
        
        ResultSetMetaData clickmeta = clickSet.getMetaData();
        int columnsNumber = clickmeta.getColumnCount();
        
        while (clickSet.next()) {
            
            CompanyNameChoiceBox.setValue(clickSet.getString("customerName"));
            MeetingTitleTextField.setText(clickSet.getString("title"));            
            MeetingDescriptionTextField.setText(clickSet.getString("description"));
            MeetingTypeChoiceBox.setValue(clickSet.getString("type"));
            contactTextBox.setText(clickSet.getString("contact"));
            BranchLocationLabel.setText(clickSet.getString("location"));
            LastUpdatedLabel.setText(clickSet.getString("lastUpdate"));
            UrlLabel.setText(clickSet.getString("url"));
            CreatedByLabel.setText(clickSet.getString("createdBy"));
            
            String startUTC = clickSet.getString("start").substring(0, 19);
            String endUTC = clickSet.getString("end").substring(0, 19);
                
            LocalDateTime utcStartDT = LocalDateTime.parse(startUTC, datetimeDTF);
            LocalDateTime utcEndDT = LocalDateTime.parse(endUTC, datetimeDTF);
                
            ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
            ZonedDateTime localZoneEnd = utcEndDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
            
            String localStartDT = localZoneStart.format(datetimeDTF);
            String localEndDT = localZoneEnd.format(datetimeDTF);
            LocalTime timeChoiceBoxStart = LocalTime.parse(localStartDT.substring(11,16));
            String timeChoiceBoxEnd = localEndDT.substring(11);
            StartTimeChoiceBox.setValue(timeChoiceBoxStart);
            EndTimeLabel.setText(timeChoiceBoxEnd);
            
            
            Duration duration = Duration.between(utcStartDT, utcEndDT);
            Duration d = Duration.between(LocalTime.parse(appointment.getStart()), LocalTime.parse(appointment.getEnd()));
            long milli = d.toMinutes();
            String durationFill = milli + " Minutes";
            DurationChoiceBox.setValue(durationFill);
        }  
        
    } //FINISHED
    
    
    
    private void fillStartList() {
        LocalTime startTimeListOpen = LocalTime.of(8, 0, 0);
        LocalTime startTimeListClose = LocalTime.of(17, 0, 0);
        LocalTime startTimeListAdd = startTimeListOpen;
        startList.add(startTimeListOpen);
        int compare = startTimeListAdd.compareTo(startTimeListClose);
        while (compare < 0) {            
            startTimeListAdd = startTimeListAdd.plusMinutes(30);
            startList.add(startTimeListAdd);
            compare = startTimeListAdd.compareTo(startTimeListClose);
        }        
        StartTimeChoiceBox.setItems(startList);         
    }
    
    private void fillCustomerList() throws SQLException {       
        PreparedStatement fillCustomer;
        fillCustomer =conn.prepareStatement("SELECT customerName FROM customer");
        ResultSet customers = fillCustomer.executeQuery();        
        while (customers.next()) {
            Customer cust = new Customer();
            cust.setCustomerName(customers.getString("customerName"));
            companyList.add(cust.getCustomerName());
            CompanyNameChoiceBox.setItems(companyList);

        }
    }

    
    
    public void updateAppointmentsTableView() throws SQLException {
            appointmentList.clear();
            
            String sqlStatement = ("SELECT appointment.appointmentId, appointment.customerId, appointment.userId, appointment.title, appointment.description, "
                    + "appointment.location, appointment.contact, appointment.type, appointment.url, appointment.start, appointment.end, user.userName, "
                    + "appointment.createdBy, customer.customerId, customer.customerName "
                    + "FROM appointment, customer, user "
                    + "WHERE appointment.customerId = customer.customerId AND user.userId = appointment.userId"
                    + " ORDER BY `start`");
            PreparedStatement ps;
            ps =conn.prepareStatement(sqlStatement);
            Statement stmt = DBConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);
            ResultSetMetaData rsmd = result.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            result.beforeFirst();
            while (result.next()) {
                Appointment appointment = new Appointment();
                Customer customer = new Customer(result.getString("customerName"));
                appointment.setAppointmentId(result.getInt("appointmentId"));
                appointment.setCustomerId(result.getInt("customerId"));
                appointment.setUserName(result.getString("userName"));
                
                appointment.setCustomerName(result.getString("customerName"));
                String startUTC = result.getString("start").substring(0, 19);
                String endUTC = result.getString("end").substring(0, 19);
                
                LocalDateTime utcStartDT = LocalDateTime.parse(startUTC, datetimeDTF);
                LocalDateTime utcEndDT = LocalDateTime.parse(endUTC, datetimeDTF);
                
                ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
                ZonedDateTime localZoneEnd = utcEndDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
                
                String localStartDT = localZoneStart.format(datetimeDTF);
                String localEndDT = localZoneEnd.format(datetimeDTF);
                appointment.setStart(localStartDT.substring(11));
                appointment.setEnd(localEndDT.substring(11));
                appointment.setAppointmentDate(localStartDT.substring(0,10));
               
                String localDate = localZoneStart.format(date);
               
                LocalDate updateCalendar = LocalDate.parse(localDate);
                MeetingDateCalendar.setValue(updateCalendar);
                appointment.setUserId(result.getInt("userId"));
                
                appointment.setTitle(result.getString("title"));                
                appointment.setDescription(result.getString("description"));                
                appointment.setContact(result.getString("contact"));
                appointment.setCreatedBy(result.getString("createdBy"));
                appointment.setLocation(result.getString("location"));                
                appointment.setType(result.getString("type"));                
                appointment.setUrl(result.getString("url"));
                appointment.setDateTime(result.getString("start"));
                appointmentList.addAll(appointment);
            }
        
        
        
        AppointmentsTableView.setItems(appointmentList);
        if (isWeekly) {
                filterAppointmentsByWeek(appointmentList);
            } else if(isMonthly) {
                filterAppointmentsByMonth(appointmentList);
            }
    } 
    
    private boolean validateForm() {
        String customerValid = CompanyNameChoiceBox.getValue();
        String titleValid = MeetingTitleTextField.getText();
        String descriptionValid = MeetingDescriptionTextField.getText();
        String typeValid = MeetingTypeChoiceBox.getValue();
        String contactValid = contactTextBox.getText();
        LocalDate datePickerValid = MeetingDateCalendar.getValue();
        LocalTime startValid = StartTimeChoiceBox.getValue();
        String durationValid = DurationChoiceBox.getValue();
        String urlValid = UrlLabel.getText();
        
        if (customerValid == null || customerValid.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Correct Missing Information");
            alert.setContentText(" Must Give Customer Name");
            Optional<ButtonType> result = alert.showAndWait();
            return false;
        }
        if (titleValid == null || titleValid.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Correct Missing Information");
            alert.setContentText(" Must Give Meeting Title");
            Optional<ButtonType> result = alert.showAndWait();
            return false;
        }
        if (descriptionValid == null || descriptionValid.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Correct Missing Information");
            alert.setContentText(" Must Give Event Description");
            Optional<ButtonType> result = alert.showAndWait();
            return false;
        }
        if (typeValid == null || typeValid.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Correct Missing Information");
            alert.setContentText(" Must Choose Meeting Type");
            Optional<ButtonType> result = alert.showAndWait();

            return false;
        }
        if (contactValid == null || contactValid.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Correct Missing Information");
            alert.setContentText(" Must Give Meeting Contact");
            Optional<ButtonType> result = alert.showAndWait();

            return false;
        }
        if (datePickerValid == null ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Correct Missing Information");
            alert.setContentText(" Must Choose a Date");
            Optional<ButtonType> result = alert.showAndWait();

            return false;
        }
        if (startValid == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Correct Missing Information");
            alert.setContentText(" Must Choose a Start Time");
            Optional<ButtonType> result = alert.showAndWait();

            return false;
        }
        if (durationValid == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Correct Missing Information");
            alert.setContentText(" Must Select Meeting Duration");
            Optional<ButtonType> result = alert.showAndWait();

            return false;
        }
        if (urlValid == null || urlValid.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Correct Missing Information");
            alert.setContentText(" Must Give Customer Web Address");
            Optional<ButtonType> result = alert.showAndWait();

            return false;
        }
        
        return true;
    }
    
    private boolean checkConflict(Timestamp start, Timestamp end, int userId) throws SQLException {
        makeQuery("SELECT count(*) FROM appointment WHERE userID= '"+userId+"' and start between '"+start+"' and '"+end+"'");
        System.out.println("Query Made");
        ResultSet conflict = getResult();
        ResultSetMetaData rsmd = conflict.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (conflict.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
            if (i > 1) System.out.print(",  ");
            String columnValue = conflict.getString(i);
            System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }   
        }
           
        conflict.first();
        int conflictInt = conflict.getInt("count(*)");
        if (conflictInt > 0){
           return false;
        } else{
         return true;  
        }    
    }
    
    private int queryCustomerId(String customerName) throws SQLException {
        String customerIdString = CompanyNameChoiceBox.getValue();
        makeQuery("SELECT customerId FROM customer WHERE customerName='" + customerIdString + "'");
        ResultSet custId = getResult();
        custId.beforeFirst();
        custId.next();
        int queryCustomerId = custId.getInt("customerId");
        return queryCustomerId;
    }
    
    private boolean saveToDatabase() throws SQLException, Exception {
        String localStartDTString = MeetingDateCalendar.getValue() + " " + StartTimeChoiceBox.getSelectionModel().getSelectedItem() + ":00";
        String localEndDTString = MeetingDateCalendar.getValue() +" " + EndTimeLabel.getText();
        String customerName = CompanyNameChoiceBox.getValue();
        LocalDate datePickerValue = MeetingDateCalendar.getValue();
        LocalDateTime localStartTime = LocalDateTime.parse(localStartDTString, datetimeDTF);
        LocalDateTime localEndTime = LocalDateTime.parse(localEndDTString, datetimeDTF);
        ZonedDateTime startUTC = localStartTime.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = localEndTime.atZone(localZoneID).withZoneSameInstant(ZoneId.of("UTC"));
        Timestamp sqlStartTS = Timestamp.valueOf(startUTC.toLocalDateTime());
        Timestamp sqlEndTS = Timestamp.valueOf(endUTC.toLocalDateTime());
        if (checkConflict(sqlStartTS, sqlEndTS, User.getUserID())){
            if (isNew) { 
                String insertAppointment = "INSERT INTO appointment(customerId, userId, title, description, location, contact, type, url, start, end,"
                        + " createdBy, lastUpdateBy, createDate) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)"; //13
                PreparedStatement psAppointment;
                psAppointment =conn.prepareStatement(insertAppointment);
                psAppointment.setInt(1, queryCustomerId(customerName));
                psAppointment.setInt(2, User.getUserID());
                psAppointment.setString(3, MeetingTitleTextField.getText());
                psAppointment.setString(4, MeetingDescriptionTextField.getText());
                psAppointment.setString(5, BranchLocationLabel.getText());
                psAppointment.setString(6, contactTextBox.getText());
                psAppointment.setString(7, MeetingTypeChoiceBox.getValue());
                psAppointment.setString(8, UrlLabel.getText());
                psAppointment.setTimestamp(9, sqlStartTS);
                psAppointment.setTimestamp(10, sqlEndTS);
                psAppointment.setString(11, CreatedByLabel.getText());
                psAppointment.setString(12, User.getUsername());
                int rs = psAppointment.executeUpdate();
                return true;

            } else {
                System.out.println();
                System.out.println("++++ Save Updated Subroutine++++");
                System.out.println();
                Appointment appt = AppointmentsTableView.getSelectionModel().getSelectedItem();
                System.out.println("idlookup : " + appt.getAppointmentId());
                int idlookup = appt.getAppointmentId();
                String updateAppointment = "UPDATE appointment SET customerId = ?, userId = ?, title = ?, description = ?, location = ?,"
                        + " contact = ?, type = ?, url = ?, start = ?, end = ?, lastUpdateBy = ? WHERE appointmentId = ?";
                PreparedStatement psAppointment;
                psAppointment =conn.prepareStatement(updateAppointment);
                psAppointment.setInt(1, queryCustomerId(customerName));
                psAppointment.setInt(2, User.getUserID());
                psAppointment.setString(3, MeetingTitleTextField.getText());
                psAppointment.setString(4, MeetingDescriptionTextField.getText());
                psAppointment.setString(5, BranchLocationLabel.getText());
                psAppointment.setString(6, contactTextBox.getText());
                psAppointment.setString(7, MeetingTypeChoiceBox.getValue());
                psAppointment.setString(8, UrlLabel.getText());
                psAppointment.setTimestamp(9, sqlStartTS);
                psAppointment.setTimestamp(10, sqlEndTS);
                psAppointment.setString(11, User.getUsername());
                psAppointment.setInt(12, idlookup);
                int rs = psAppointment.executeUpdate();
                return true;


            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Appointment Conflict");
            alert.setContentText("This time conflicts with schedule for this user");
            DurationChoiceBox.setValue(null);
            StartTimeChoiceBox.setValue(null);
            Optional<ButtonType> result = alert.showAndWait();
            return false;
        }
           
    }
        
    public void filterAppointmentsByWeek(ObservableList appointmentsWeek) {
        LocalDate now = LocalDate.now();
        LocalDate nowPlus1Week = now.plusWeeks(1);
        FilteredList<Appointment> filteredData = new FilteredList<>(appointmentsWeek);
        filteredData.setPredicate(row -> {
            String truncateDate = row.getDateTime();
            LocalDate rowDate = LocalDate.parse(truncateDate.substring(0,19), datetimeDTF);
            return rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(nowPlus1Week);
        });
        AppointmentsTableView.setItems(filteredData);
    }  
    
    public void filterAppointmentsByMonth(ObservableList appointmentsOL) throws SQLException {
        LocalDate now = LocalDate.now();
        LocalDate nowPlus1Month = now.plusMonths(1);
        FilteredList<Appointment> filteredData = new FilteredList<>(appointmentsOL);
        filteredData.setPredicate(row -> {
            String truncateDate = row.getDateTime();
            LocalDate rowDate = LocalDate.parse(truncateDate.substring(0,19), datetimeDTF);
            return rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(nowPlus1Month);
        });

        AppointmentsTableView.setItems(filteredData);
    }
    
    private void disableAppointmentForm() {
        CompanyNameChoiceBox.setDisable(true);
        MeetingTitleTextField.setDisable(true);
        MeetingDescriptionTextField.setDisable(true);
        MeetingTypeChoiceBox.setDisable(true);
        contactTextBox.setDisable(true);
        BranchLocationLabel.setDisable(true);
        BranchLocationLabel.setText("Branch Office");
        MeetingDateCalendar.setDisable(true);
        StartTimeChoiceBox.setDisable(true);
        DurationChoiceBox.setDisable(true);
        CreatedByLabel.setDisable(true);
        CreatedByLabel.setText("Created By");
        LastUpdatedLabel.setDisable(true);
        LastUpdatedLabel.setText("Last Update By");
        UrlLabel.setDisable(true);
        EndTimeLabel.setDisable(true);
        SaveAppointmentButton.setDisable(true);
        ClearFormButton.setDisable(true);
        CancelInputButton.setDisable(true);
    }
    
    private void enableAppointmentForm() {
        CompanyNameChoiceBox.setDisable(false);
        MeetingTitleTextField.setDisable(false);
        MeetingDescriptionTextField.setDisable(false);
        MeetingTypeChoiceBox.setDisable(false);
        contactTextBox.setDisable(false);
        BranchLocationLabel.setDisable(false);
        MeetingDateCalendar.setDisable(false);
        StartTimeChoiceBox.setDisable(false);
        DurationChoiceBox.setDisable(false);
        CreatedByLabel.setDisable(false);
        LastUpdatedLabel.setDisable(false);
        UrlLabel.setDisable(false);
        EndTimeLabel.setDisable(false);
        SaveAppointmentButton.setDisable(false);
        ClearFormButton.setDisable(false);
        CancelInputButton.setDisable(false);
    }
    
    private void wipeAppointmentForm() {
        CompanyNameChoiceBox.setValue(null);
        MeetingTitleTextField.setText("Meeting Title");
        MeetingDescriptionTextField.setText("Description");
        MeetingTypeChoiceBox.setValue(null);
        contactTextBox.setText("");
        BranchLocationLabel.setDisable(true);
        BranchLocationLabel.setText("Branch Office");
        MeetingDateCalendar.setValue(null);
        StartTimeChoiceBox.setValue(null);
        DurationChoiceBox.setValue(null);
        CreatedByLabel.setText("Created By");
        LastUpdatedLabel.setText("Last Update By");
        UrlLabel.setText("URL");
        EndTimeLabel.setText("End Time");
    }
    
    private void disableLeftBar() {
        ViewAllRadio.setDisable(true);
        MonthlyViewRadio.setDisable(true);
        WeeklyViewRadio.setDisable(true);
        AddAppointmentButton.setDisable(true);
        DeleteSelectedButton.setDisable(true);
        EditSelectedButton.setDisable(true);
        BackButton.setDisable(true);
        
    }
    
    private void enableLeftBar(){
        ViewAllRadio.setDisable(false);
        MonthlyViewRadio.setDisable(false);
        WeeklyViewRadio.setDisable(false);
        AddAppointmentButton.setDisable(false);
        DeleteSelectedButton.setDisable(false);
        EditSelectedButton.setDisable(false);
        BackButton.setDisable(false);
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        PropertyValueFactory<Appointment, String> custNameFactory = new PropertyValueFactory<>("CustomerName");
        PropertyValueFactory<Appointment, String> custDateFactory = new PropertyValueFactory<>("AppointmentDate");
        PropertyValueFactory<Appointment, String> custStartFactory = new PropertyValueFactory<>("Start");
        PropertyValueFactory<Appointment, String> custEndFactory = new PropertyValueFactory<>("End");
        PropertyValueFactory<Appointment, String> custUserIdFactory = new PropertyValueFactory<>("userName");
        PropertyValueFactory<Appointment, String> custLocationFactory = new PropertyValueFactory<>("Location");
        PropertyValueFactory<Appointment, String> custTypeFactory = new PropertyValueFactory<>("Type");
        
        CustomerNameColumn.setCellValueFactory(custNameFactory);
        DateColumn.setCellValueFactory(custDateFactory);
        StartTimeColumn.setCellValueFactory(custStartFactory);
        EndTimeColumn.setCellValueFactory(custEndFactory);
        CreatedByColumn.setCellValueFactory(custUserIdFactory);
        LocationColumn.setCellValueFactory(custLocationFactory);
        TypeColumn.setCellValueFactory(custTypeFactory);
        ViewAllRadio.setSelected(true);
        MonthlyViewRadio.setSelected(false);
        WeeklyViewRadio.setSelected(false);
        
        startListener();
        durationListener();
        branchListener();
        
        fillStartList();
        try {
            fillCustomerList();
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        typeList.addAll("Sales", "Admin", "Billing");
        MeetingTypeChoiceBox.setItems(typeList);
        
        durationList.addAll("30 Minutes", "60 Minutes", "90 Minutes");
        DurationChoiceBox.setItems(durationList);
        MeetingDateCalendar.setDayCellFactory(picker -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.getDayOfWeek() == DayOfWeek.SUNDAY);
                    
                }
                
            };
            
        });
        try {
            updateAppointmentsTableView();
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        disableAppointmentForm();
    }
    
    Parent root;
    Stage stage;


    @FXML
    private void AddAppointmentHandler(ActionEvent event) {
        isNew = true;
        wipeAppointmentForm();
        enableAppointmentForm();
        disableLeftBar();
        AppointmentsTableView.setDisable(true);
        
        CreatedByLabel.setText(User.getUsername());        
    }

    @FXML
    private void DeleteSelectedHandler(ActionEvent event) throws SQLException {
        ObservableList<Appointment> appointmentClear = FXCollections.observableArrayList();
        Appointment appt = AppointmentsTableView.getSelectionModel().getSelectedItem();
        com.mysql.jdbc.PreparedStatement delPull;
        int idLookup = appt.getAppointmentId();
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm Delete");
        alert.setContentText("Are you sure you want to Delete this appointment?");
        Optional<ButtonType> result = alert.showAndWait();    
        if (result.get() == ButtonType.OK) {
              try {
            delPull =(com.mysql.jdbc.PreparedStatement) conn.prepareStatement("DELETE appointment.* from appointment where appointmentId = ?");
            delPull.setInt(1, idLookup);
            int executeUpdate = delPull.executeUpdate();
            } catch (SQLException e) {
            }
        wipeAppointmentForm();
        updateAppointmentsTableView();
        }
    }

    @FXML
    private void EditSelectedHandler(ActionEvent event) {
        isNew = false;
        enableAppointmentForm();
        AppointmentsTableView.setDisable(true);
        disableLeftBar();
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
    private void MeetingDateCalendarHandler(ActionEvent event) {
    }


    @FXML
    private void ClearFormButtonHandler(ActionEvent event) {
        System.out.println();
        System.out.println("++++ ClearFormButtonHandler routine++++");
        disableAppointmentForm();
        wipeAppointmentForm();
        enableLeftBar();
        AppointmentsTableView.setDisable(false);
        System.out.println("++++ ClearFormButtonHandler routine++++");
        System.out.println();
        
    }

    @FXML
    private void MonthlyViewRadioHandler(ActionEvent event) throws SQLException {
        isWeekly = false;
        isMonthly = true;
        isWeeklyInt = 0;
        isMonthlyInt = 1;
        WeeklyViewRadio.setSelected(false);
        ViewAllRadio.setSelected(false);
        
        updateAppointmentsTableView();
    }

    @FXML
    private void WeeklyViewRadioHandler(ActionEvent event) throws SQLException {
        isWeekly = true;
        isMonthly = false;
        isWeeklyInt = 1;
        isMonthlyInt = 0;
        MonthlyViewRadio.setSelected(false);
        ViewAllRadio.setSelected(false);
        
        updateAppointmentsTableView();
    }

    @FXML
    private void ViewAllRadioHandler(ActionEvent event) throws SQLException {
      MonthlyViewRadio.setSelected(false);
      WeeklyViewRadio.setSelected(false);
      isMonthlyInt = 0;
      isWeeklyInt = 0;
      isMonthly = false;
      isWeekly = false;
      
      updateAppointmentsTableView();
      
      
      
    }

    @FXML
    private void CancelInputButtonHandler(ActionEvent event) {
        System.out.println();
        System.out.println("++++ CancelInputButtonHandler routine++++");
        AppointmentsTableView.setDisable(false);
        disableAppointmentForm();
        wipeAppointmentForm();
        enableLeftBar();
        System.out.println("++++ CancelInputButtonHandler routine++++");
        System.out.println();
    }

    @FXML
    private void SaveAppointmentButtonHandler(ActionEvent event) throws Exception {
        System.out.println();
        System.out.println("++++ SaveAppointmentButtonHandler routine++++");
        if (validateForm()) {
            if (saveToDatabase()){
                enableLeftBar();
                updateAppointmentsTableView();
                AppointmentsTableView.setDisable(false);
                disableAppointmentForm();
                wipeAppointmentForm();
                System.out.println("++++ SaveAppointmentButtonHandler routine++++");
                System.out.println();
                
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Entry Did Not Save");
                alert.setContentText("Context Error, inputs did not save");
                Optional<ButtonType> result = alert.showAndWait();
                
            }
            
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Validation Error");
            alert.setContentText("Invalid entry not validated for save");
            Optional<ButtonType> result = alert.showAndWait();
            
        }
        
        
    }

    

    
}
