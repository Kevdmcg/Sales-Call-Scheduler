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
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Duration;
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
    
    private final DateTimeFormatter timeOnlyFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final DateTimeFormatter dateOnlyFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ZoneId localTimeZoneId = ZoneId.systemDefault();
    private final ZoneId utcTimeZoneId = ZoneId.of("UTC");
    
    // LAMBDA EXPRESSION USED BELOW FOR CLARITY AND CONCISE EXECUTION OF addListener
    private void branchListener() {
        CompanyNameChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, value, new_value) -> {
            if (new_value != null) {
                String customerName = CompanyNameChoiceBox.getValue();
                makeQuery("SELECT city.city FROM city, address, customer WHERE city.cityId = address.cityId AND"
                        + " customer.addressId = address.addressId AND customerName = '" + customerName + "'");
                ResultSet branch = getResult();
                try {
                    while (branch.next()) {
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
        });
    }

    private void startListener() {
        StartTimeChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LocalTime>() {
            @Override
            public void changed(ObservableValue ov, LocalTime value, LocalTime new_value) {
                if (StartTimeChoiceBox != null && new_value != null) {
                    LocalTime startTime = new_value;
                }
            }
        });
    }
    //  LAMBDA EXPRESSION USED INSIDE durationListener method in order to present concise and efficient code
    private void durationListener() {
        DurationChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, value, new_value) -> {
            if (new_value != null && StartTimeChoiceBox != null) {
                LocalTime closeTime = LocalTime.of(17, 00, 00);
                String durationTime = new_value;
                System.out.println("inside durationListener durationIntl: "+Integer.parseInt(durationTime.substring(0, 2)));
                int durationInt1 = Integer.parseInt(durationTime.substring(0, 2));
                LocalTime startTime1 = StartTimeChoiceBox.getValue();
                LocalTime endTime1 = startTime1.plusMinutes(durationInt1);
                int closeValue = endTime1.compareTo(closeTime);
                if (closeValue > 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Appointment Schedule Error");
                    alert.setContentText("This Meeting is scheduled to end after closing time.  Adjust start time or duration.");
                    Optional<ButtonType> result = alert.showAndWait();
                    DurationChoiceBox.setValue(null);
                }
                String endLabel = endTime1.format(timeOnlyFormat);
                EndTimeLabel.setText(endLabel);
            }
        });
    }

    @FXML  // This code will pre-fill the appointment form on the right when an item is selected from tableView
    private void appointmentMouseClick(MouseEvent event) throws SQLException, Exception {
        System.out.println("+++++appointmentMouseClick activated");
        Appointment appointment = AppointmentsTableView.getSelectionModel().getSelectedItem();
        int idClickLookup = appointment.getAppointmentId();
        System.out.println("+++++appointmentMouseClick idClickLookup:"+idClickLookup);
        makeQuery("SELECT * FROM appointment, customer WHERE appointment.appointmentId = " + idClickLookup + " AND customer.customerId = appointment.customerId ");
        System.out.println("+++++appointmentMouseClick Query Made");
        ResultSet clickSet = getResult();
        ResultSetMetaData clickmeta = clickSet.getMetaData();
        int columnsNumber = clickmeta.getColumnCount();
        while (clickSet.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = clickSet.getString(i);
                System.out.print(columnValue + " " + clickmeta.getColumnName(i));
        }
        System.out.println("");
        }
        clickSet.beforeFirst();
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

            LocalDateTime utcStartDT = LocalDateTime.parse(startUTC, dateTimeFormat);
            LocalDateTime utcEndDT = LocalDateTime.parse(endUTC, dateTimeFormat);

            ZonedDateTime localZoneStart = utcStartDT.atZone(utcTimeZoneId).withZoneSameInstant(localTimeZoneId);
            ZonedDateTime localZoneEnd = utcEndDT.atZone(utcTimeZoneId).withZoneSameInstant(localTimeZoneId);

            String localStartDT = localZoneStart.format(dateTimeFormat);
            String localEndDT = localZoneEnd.format(dateTimeFormat);
            LocalTime timeChoiceBoxStart = LocalTime.parse(localStartDT.substring(11, 16));
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
    
    // fills the list of available start times
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
    
    // fills the choiceBox with valid customers
    private void fillCustomerList() throws SQLException {
        PreparedStatement fillCustomer;
        fillCustomer = conn.prepareStatement("SELECT customerName FROM customer");
        ResultSet customers = fillCustomer.executeQuery();
        while (customers.next()) {
            Customer cust = new Customer();
            cust.setCustomerName(customers.getString("customerName"));
            companyList.add(cust.getCustomerName());
            CompanyNameChoiceBox.setItems(companyList);

        }
    }
    
    // function call each time the tableView needs updated with fresh information
    public void updateAppointmentsTableView() throws SQLException {
        appointmentList.clear();

        String sqlStatement = ("SELECT appointment.appointmentId, appointment.customerId, appointment.userId, appointment.title, appointment.description, "
                + "appointment.location, appointment.contact, appointment.type, appointment.url, appointment.start, appointment.end, user.userName, "
                + "appointment.createdBy, customer.customerId, customer.customerName "
                + "FROM appointment, customer, user "
                + "WHERE appointment.customerId = customer.customerId AND user.userId = appointment.userId"
                + " ORDER BY `start`");
        PreparedStatement ps;
        ps = conn.prepareStatement(sqlStatement);
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

            LocalDateTime utcStartDT = LocalDateTime.parse(startUTC, dateTimeFormat);
            LocalDateTime utcEndDT = LocalDateTime.parse(endUTC, dateTimeFormat);

            ZonedDateTime localZoneStart = utcStartDT.atZone(utcTimeZoneId).withZoneSameInstant(localTimeZoneId);
            ZonedDateTime localZoneEnd = utcEndDT.atZone(utcTimeZoneId).withZoneSameInstant(localTimeZoneId);

            String localStartDT = localZoneStart.format(dateTimeFormat);
            String localEndDT = localZoneEnd.format(dateTimeFormat);
            appointment.setStart(localStartDT.substring(11));
            appointment.setEnd(localEndDT.substring(11));
            appointment.setAppointmentDate(localStartDT.substring(0, 10));

            String localDate = localZoneStart.format(dateOnlyFormat);

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
        } else if (isMonthly) {
            filterAppointmentsByMonth(appointmentList);
        }
    }
    
    // Checks that no empty fields on the new/edit appointment form
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
        if (datePickerValid == null) {
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
    
    // This checks for scheduling conflicts for editing an appointment
    private boolean checkConflictEdit(Timestamp start, Timestamp end, LocalDate localDate, int userId, int appointmentId) throws SQLException {
        System.out.println("Appointment Conflict EDITED check started");
        System.out.println("start: "+ start);
        System.out.println("end: "+ end);
        System.out.println("localDate "+ localDate);
        System.out.println("userId: "+ userId);
        System.out.println("appointmentId: "+ appointmentId);
        makeQuery("SELECT start, end, appointmentId FROM appointment WHERE userId='"+userId+"' and start between '"+localDate+" 00:00:00.0' and '"+localDate+" 23:59:59.0'");
        System.out.println("SELECT count(*) FROM appointment WHERE userID= '" + userId + "' and start between '" + start + "' and '" + end + "' OR end between '" + start + "' and '" + end + "'");
        //makeQuery("SELECT count(*) FROM appointment WHERE userID= '" + userId + "' and start between '" + start + "' and '" + end + "' OR end between '" + start + "' and '" + end + "'");
        ResultSet conflict = getResult();
        ResultSetMetaData rsmd = conflict.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (conflict.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = conflict.getString(i);
                System.out.print(columnValue + " " + rsmd.getColumnName(i));
        }
        System.out.println("");
        }
        
        conflict.beforeFirst();
        while (conflict.next()){
            Appointment appointment = new Appointment();
            appointment.setStart(conflict.getString("start"));
            appointment.setEnd(conflict.getString("end"));
            appointment.setAppointmentId(conflict.getInt("appointmentId"));
            
            System.out.println("inside RESULTSET sqlStart: "+appointment.getStart());
            System.out.println("inside RESULTSET sqlEnd: "+appointment.getEnd());
            //Timestamp sqlStart= Timestamp.valueOf(appointment.getStart().substring(11,18));
            //Timestamp sqlEnd= Timestamp.valueOf(appointment.getEnd().substring(11,18));
            Timestamp sqlStart= Timestamp.valueOf(appointment.getStart());
            Timestamp sqlEnd= Timestamp.valueOf(appointment.getEnd());
            if (start.before(sqlStart) && end.after(sqlStart) && appointmentId != appointment.getAppointmentId()) {
                return false;
            } else if (start.after(sqlStart) && start.before(sqlEnd) && appointmentId != appointment.getAppointmentId()) {
                return false;
            }
        }
        return true;
        
    }
    
    
    // This code checks for conflicting appointments for new appointments before saving an appointment.  It queries the database for appointments in that time and returns false if it cannot
    // be saved
    private boolean checkConflictNew(Timestamp start, Timestamp end, LocalDate localDate, int userId) throws SQLException {
        System.out.println("Appointment Conflict NEW check started");
        System.out.println("start: "+ start);
        System.out.println("end: "+ end);
        System.out.println("userId: "+ userId);
        System.out.println("localDate "+ localDate);
        makeQuery("SELECT start, end FROM appointment WHERE userId='"+userId+"' and start between '"+localDate+" 00:00:00.0' and '"+localDate+" 23:59:59.0'");
        //System.out.println("SELECT count(*) FROM appointment WHERE userID= '" + userId + "' and start between '" + start + "' and '" + end + "' OR end between '" + start + "' and '" + end + "'");
        //makeQuery("SELECT count(*) FROM appointment WHERE userID= '" + userId + "' and start between '" + start + "' and '" + end + "' OR end between '" + start + "' and '" + end + "'");
        ResultSet conflict = getResult();
        ResultSetMetaData rsmd = conflict.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (conflict.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = conflict.getString(i);
                System.out.print(columnValue + " " + rsmd.getColumnName(i));
        }
        System.out.println("");
        }
        conflict.beforeFirst();
        while (conflict.next()){
            Appointment appointment = new Appointment();
            appointment.setStart(conflict.getString("start"));
            appointment.setEnd(conflict.getString("end"));
            System.out.println("inside RESULTSET sqlStart: "+appointment.getStart());
            System.out.println("inside RESULTSET sqlEnd: "+appointment.getEnd());
            //Timestamp sqlStart= Timestamp.valueOf(appointment.getStart().substring(11,18));
            //Timestamp sqlEnd= Timestamp.valueOf(appointment.getEnd().substring(11,18));
            Timestamp sqlStart= Timestamp.valueOf(appointment.getStart());
            Timestamp sqlEnd= Timestamp.valueOf(appointment.getEnd());
            if (start.before(sqlStart) && end.after(sqlStart)) {
                return false;
            } else if (start.after(sqlStart) && start.before(sqlEnd)) {
                return false;
            }
        }
        return true;
        
    }
    
    // a function for looking up CustomerIds inside appointments
    private int queryCustomerId(String customerName) throws SQLException {
        String customerIdString = CompanyNameChoiceBox.getValue();
        makeQuery("SELECT customerId FROM customer WHERE customerName='" + customerIdString + "'");
        ResultSet custId = getResult();
        custId.beforeFirst();
        custId.next();
        int queryCustomerId = custId.getInt("customerId");
        return queryCustomerId;
    }
    
    // the actual save to database routine.  checks validity, conflicts, then checks if its a new or an edit
    private boolean saveToDatabase() throws SQLException, Exception {
        String localStartDTString = MeetingDateCalendar.getValue() + " " + StartTimeChoiceBox.getSelectionModel().getSelectedItem() + ":00";
            System.out.println("+++saveToDatabase localStartDTString: " +localStartDTString);
        String localEndDTString = MeetingDateCalendar.getValue() + " " + EndTimeLabel.getText();
            System.out.println("+++saveToDatabase localEndDTString: " +localEndDTString);
        String customerName = CompanyNameChoiceBox.getValue();
        LocalDate datePickerValue = MeetingDateCalendar.getValue();
            System.out.println("+++saveToDatabase datePickerValue: " +datePickerValue);
        LocalDateTime localStartTime = LocalDateTime.parse(localStartDTString, dateTimeFormat);
            System.out.println("+++saveToDatabase localStartTime: " +localStartTime);
        LocalDateTime localEndTime = LocalDateTime.parse(localEndDTString, dateTimeFormat);
        System.out.println("+++saveToDatabase localEndTime: " +localEndTime);
        ZonedDateTime startUTC = localStartTime.atZone(localTimeZoneId).withZoneSameInstant(ZoneId.of("UTC"));
        System.out.println("+++saveToDatabase startUTC: " +startUTC);
        ZonedDateTime endUTC = localEndTime.atZone(localTimeZoneId).withZoneSameInstant(ZoneId.of("UTC"));
        System.out.println("+++saveToDatabase endUTC: " +endUTC);
        Timestamp sqlStartTS = Timestamp.valueOf(startUTC.toLocalDateTime());
        System.out.println("+++saveToDatabase sqlStartTS: " +sqlStartTS);
        Timestamp sqlEndTS = Timestamp.valueOf(endUTC.toLocalDateTime());
        System.out.println("+++saveToDatabase sqlEndTS: " +sqlEndTS);
        System.out.println("");
        
        if (isNew && checkConflictNew(sqlStartTS, sqlEndTS, datePickerValue, User.getUserId())) {
                String insertAppointment = "INSERT INTO appointment(customerId, userId, title, description, location, contact, type, url, start, end,"
                        + " createdBy, lastUpdateBy, createDate) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)"; //13
                PreparedStatement psAppointment;
                psAppointment = conn.prepareStatement(insertAppointment);
                psAppointment.setInt(1, queryCustomerId(customerName));
                psAppointment.setInt(2, User.getUserId());
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

            } else if (!isNew) {
                Appointment appt = AppointmentsTableView.getSelectionModel().getSelectedItem();
                int idLookup = appt.getAppointmentId();
                System.out.println("inside !isNew idlookup:"+idLookup);
                if (checkConflictEdit(sqlStartTS, sqlEndTS, datePickerValue, User.getUserId(), idLookup)){


                    String updateAppointment = "UPDATE appointment SET customerId = ?, userId = ?, title = ?, description = ?, location = ?,"
                            + " contact = ?, type = ?, url = ?, start = ?, end = ?, lastUpdateBy = ? WHERE appointmentId = ?";
                    PreparedStatement psAppointment;
                    psAppointment = conn.prepareStatement(updateAppointment);
                    psAppointment.setInt(1, queryCustomerId(customerName));
                    psAppointment.setInt(2, User.getUserId());
                    psAppointment.setString(3, MeetingTitleTextField.getText());
                    psAppointment.setString(4, MeetingDescriptionTextField.getText());
                    psAppointment.setString(5, BranchLocationLabel.getText());
                    psAppointment.setString(6, contactTextBox.getText());
                    psAppointment.setString(7, MeetingTypeChoiceBox.getValue());
                    psAppointment.setString(8, UrlLabel.getText());
                    psAppointment.setTimestamp(9, sqlStartTS);
                    psAppointment.setTimestamp(10, sqlEndTS);
                    psAppointment.setString(11, User.getUsername());
                    psAppointment.setInt(12, idLookup);
                    int rs = psAppointment.executeUpdate();
                    return true;
                }
            } 
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText("Appointment Conflict");
        alert.setContentText("This time conflicts with schedule for this user");
        DurationChoiceBox.setValue(null);
        StartTimeChoiceBox.setValue(null);
        Optional<ButtonType> result = alert.showAndWait();
        return false;
            
    }
    
    // filter function 
    public void filterAppointmentsByWeek(ObservableList appointmentsWeek) {
        LocalDate now = LocalDate.now();
        LocalDate nowPlus1Week = now.plusWeeks(1);
        FilteredList<Appointment> filteredData = new FilteredList<>(appointmentsWeek);
        filteredData.setPredicate(row -> {
            String truncateDate = row.getDateTime();
            LocalDate rowDate = LocalDate.parse(truncateDate.substring(0, 19), dateTimeFormat);
            return rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(nowPlus1Week);
        });
        AppointmentsTableView.setItems(filteredData);
    }
    
    // filter function
    public void filterAppointmentsByMonth(ObservableList appointmentsOL) throws SQLException {
        LocalDate now = LocalDate.now();
        LocalDate nowPlus1Month = now.plusMonths(1);
        FilteredList<Appointment> filteredData = new FilteredList<>(appointmentsOL);
        filteredData.setPredicate(row -> {
            String truncateDate = row.getDateTime();
            LocalDate rowDate = LocalDate.parse(truncateDate.substring(0, 19), dateTimeFormat);
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

    private void enableLeftBar() {
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
                delPull = (com.mysql.jdbc.PreparedStatement) conn.prepareStatement("DELETE appointment.* from appointment where appointmentId = ?");
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
        disableAppointmentForm();
        wipeAppointmentForm();
        enableLeftBar();
        AppointmentsTableView.setDisable(false);
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
        AppointmentsTableView.setDisable(false);
        disableAppointmentForm();
        wipeAppointmentForm();
        enableLeftBar();
    }

    @FXML
    private void SaveAppointmentButtonHandler(ActionEvent event) throws Exception {
        if (validateForm()) {
            if (saveToDatabase()) {
                enableLeftBar();
                updateAppointmentsTableView();
                AppointmentsTableView.setDisable(false);
                disableAppointmentForm();
                wipeAppointmentForm();
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
