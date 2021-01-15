/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kmcgh15
 */
public class MainScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    Parent main;
    Stage stage;

    @FXML
    private void MainAppointmentButton(ActionEvent event) throws IOException {

        Parent main = FXMLLoader.load(getClass().getResource("/View_Controller/Appointments.fxml"));
        Scene scene = new Scene(main);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void MainCustomersButton(ActionEvent event) throws IOException {

        Parent main = FXMLLoader.load(getClass().getResource("/View_Controller/Customers.fxml"));
        Scene scene = new Scene(main);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void MainReportsButton(ActionEvent event) throws IOException {
        Parent main = FXMLLoader.load(getClass().getResource("/View_Controller/Reports.fxml"));
        Scene scene = new Scene(main);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    private void MainExitButton(ActionEvent event) {
        System.exit(0);
    }

}
