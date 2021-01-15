/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.User;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.DBConnection;
import static util.Query.getResult;
import static util.Query.makeQuery;



/**
 * FXML Controller class
 *
 * @author kmcgh15
 */
public class LoginController implements Initializable {

    ResourceBundle rb;
    @FXML
    private Button LoginButton;
    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Label LoginTitleLabel;
    
    private boolean alertCheck(int userId) throws SQLException{
        ZonedDateTime zdt = ZonedDateTime.now();
        Instant utcStamp = Instant.now();
        long minutes = 15;
        Instant instantFifteen = utcStamp.plus(minutes, ChronoUnit.MINUTES);
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15));
        makeQuery("SELECT count(*) FROM appointment WHERE userId = "+ userId +" and start between CURRENT_TIMESTAMP and '"+instantFifteen+"'");
        ResultSet alertSet = getResult();
        alertSet.first();
        int alertInt = alertSet.getInt("count(*)");
        if (alertInt == 0){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ResourceBundle rbt = ResourceBundle.getBundle("loginRb/loginRb", Locale.getDefault());
        LoginTitleLabel.setText(rbt.getString("loginTitle"));
        userNameTextField.setText(rbt.getString("userName"));
        passwordTextField.setText(rbt.getString("password"));
        LoginButton.setText(rbt.getString("loginButton"));
    }  
   

    @FXML
    private void UserNameHandler(ActionEvent event) {
    }

    @FXML
    private void PasswordHandler(ActionEvent event) {
    }

    @FXML
    private void LoginButtonHandler(ActionEvent event) throws IOException, SQLException {
        ResourceBundle rbt = ResourceBundle.getBundle("loginRb/loginRb", Locale.getDefault());
        String usernameInput = userNameTextField.getText();
        String passwordInput = passwordTextField.getText();
        int userID = getUserID(usernameInput);
        Parent root;
        Stage stage;
        User user = new User();

        if (isValidPassword(userID, passwordInput)) {
            User.setUserID(userID);
            User.setUsername(usernameInput);
            loginLog(user.getUsername());
            if (alertCheck(userID)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("IMMINENT APPOINTMENT");
                    alert.setHeaderText("You have an appointment booked within 15 minutes");
                    alert.setContentText("Check schedule a prepare for imminent appointment");
                    Optional<ButtonType> result = alert.showAndWait();
                
            }
            root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            stage = (Stage) LoginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText(rbt.getString("alertHeader"));
            alert.setContentText(rbt.getString("alertContent"));
            Optional<ButtonType> result = alert.showAndWait();
        }
    }
    
    private boolean isValidPassword(int userID, String password) throws SQLException {
        Statement statement = DBConnection.conn.createStatement();
        String sqlStatement = "SELECT password FROM user WHERE userId ='" + userID + "'";;
        ResultSet result = statement.executeQuery(sqlStatement);
        while (result.next()) {
            if (result.getString("password").equals(password)) {
                return true;
            }
        }
        return false;
    }
    
    private int getUserID(String username) throws SQLException {
        int userID = -1;
        Statement statement = DBConnection.conn.createStatement();
        String sqlStatement = "SELECT userID FROM user WHERE userName ='" + username + "'";
        ResultSet result = statement.executeQuery(sqlStatement);
        while (result.next()) {
            userID = result.getInt("userId");
        }
        return userID;
    }
    public static java.sql.Timestamp getTimeStamp() {
        ZoneId zoneid = ZoneId.of("UTC");
        LocalDateTime localDateTime = LocalDateTime.now(zoneid);
        java.sql.Timestamp timeStamp = Timestamp.valueOf(localDateTime);
        return timeStamp;
    }
        
    public void loginLog(String user) {
        try {
            String fileName = "loginLog";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.append(getTimeStamp() + " " + user + " " + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }    
}
