/*



 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kevinmcghee;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Model.*;
import View_Controller.*;
import java.sql.Connection;
import java.util.Locale;
import java.util.ResourceBundle;
import util.DBConnection;



/**
 *
 * @author kmcgh15
 */
public class KevinMcGheeMain extends Application {
    // loads Login screen on program start
    @Override
    public void start(Stage stage) throws Exception {
        //ResourceBundle rb = ResourceBundle.getBundle("loginRb/loginRb", Locale.getDefault());
        //System.out.println("rb.getString inside start " + rb.getString("loginTitle"));
        //System.out.println("RB loaded");
        //ResourceBundle rb = ResourceBundle.getBundle("login_lang.login", Locale.getDefault());
        //ResourceBundle rb = ResourceBundle.getBundle("login_lang/login");
        Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/Login.fxml"));
        
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        
        System.out.println("Login Screen Loaded");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ResourceBundle rb = ResourceBundle.getBundle("loginRb/loginRb", Locale.getDefault());
        System.out.println("rb.getString inside main " + rb.getString("loginTitle"));
        System.out.println("RB loaded");
        
        Connection con = DBConnection.makeConnection(); // Connect to database
        System.out.println("Connection Started in Main");
        
        
        launch(args);
        DBConnection.closeConnections();
        System.out.println("Connection Closed");
    }
    
}
