/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Customer;
import Model.User;
import com.mysql.jdbc.PreparedStatement;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
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
public class CustomersController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="FXML fxids">
    @FXML
    private TableView<Customer> CustomerTable;
    @FXML
    private TableColumn<Customer, Integer> CustomerIDColumn;
    @FXML
    private TableColumn<Customer, String> NameColumn;
    @FXML
    private TableColumn<Customer, String> AddressColumn;
    @FXML
    private TableColumn<Customer, String> PhoneColumn;
    @FXML
    private TableColumn<Customer, String> LastUpdateColumn;
    @FXML
    private TableColumn<Customer, String> ActiveColumn;
    @FXML
    private TextField CustomerIDTextField;
    @FXML
    private TextField NameTextField;
    @FXML
    private TextField AddressTextField;
    @FXML
    private TextField PhoneTextField;
    @FXML
    private TextField LastUpdateTextField;
    @FXML
    private RadioButton ActiveRadio;
    @FXML
    private RadioButton InactiveRadio;
    @FXML
    private Button ClearCustomerButton;
    private Button DeleteCustomerButton;
    @FXML
    private Button CustomerToMainButton;
    private MenuButton CityMenuButton;

    @FXML
    private TextField PostalTextField;
    @FXML
    private Label CustomerIdLabel;
    private MenuButton CountryMenuButton;
    @FXML
    private Button EditSelectedButton;
    @FXML
    private Button DeleteSelectedButton;
    @FXML
    private Button NewCustomerButton;
    @FXML
    private Button SaveCustomerInput;
    @FXML
    private ChoiceBox<String> CityChoiceBox;
    //private ChoiceBox<String> CountryChoiceBox;
    @FXML
    private Label CountryLabel;

//</editor-fold>
    Parent root;
    Stage stage;

    private boolean isActive;
    private int activeInt;
    private int customerId;

    ObservableList<Customer> customerList = FXCollections.observableArrayList();
    ObservableList<String> cityList = FXCollections.observableArrayList();
    ObservableList<String> countryList = FXCollections.observableArrayList();

    public void updateCustomerTableView() throws SQLException {
        customerList.clear();
        Statement stmt = DBConnection.conn.createStatement();

        String sqlStatement = "SELECT customerId, customerName, phone, address, active, customer.lastUpdateBy FROM customer, address WHERE customer.addressId = address.addressId  ORDER BY customer.customerName";
        ResultSet result = stmt.executeQuery(sqlStatement);
        result.beforeFirst();
        while (result.next()) {
            Customer cust = new Customer();
            cust.setCustomerId(result.getInt("customerId"));
            cust.setCustomerName(result.getString("customerName"));
            cust.setCustomerAddress(result.getString("address"));
            cust.setCustomerPhone(result.getString("phone"));
            cust.setCustomerLastUpdateBy(result.getString("lastUpdateBy"));
            cust.setCustomerActive(result.getInt("active"));
            cust.setActiveString(result.getString("active"));
            customerList.addAll(cust);
        }
        CustomerTable.setItems(customerList);
    } // FINISHED

    private void fillCityChoiceBox() throws SQLException, Exception {
        PreparedStatement selectCity;
        selectCity = (PreparedStatement) conn.prepareStatement("SELECT city FROM city");
        ResultSet cities = selectCity.executeQuery();
        while (cities.next()) {
            Customer cust = new Customer();
            cust.setCustomerCity(cities.getString("city"));
            cityList.add(cust.getCustomerCity());
            CityChoiceBox.setItems(cityList);
        }
        selectCity.close();
        cities.close();
    } // FINISHED

    private int getCityID(String city) throws SQLException, Exception {
        int cityID = -1;
        makeQuery("SELECT cityId FROM city WHERE city.city = '" + city + "'");
        ResultSet rs = getResult();
        while (rs.next()) {
            cityID = rs.getInt("cityId");
        }
        return cityID;
    }

    private int getCountryId(String country) throws SQLException, Exception {
        int countryId = -1;

        makeQuery("SELECT countryId FROM country WHERE country.country = '" + country + "'");
        ResultSet rs = getResult();

        while (rs.next()) {
            countryId = rs.getInt("countryId");
        }
        return countryId;
    }

    private String getCountry(int countryId) throws SQLException {
        String country = "repair";
        makeQuery("SELECT country FROM country WHERE countryId = '" + countryId + "'");
        ResultSet rs = getResult();
        while (rs.next()) {
            country = rs.getString("country");
        }
        return country;
    }

    private void disableCustomerForm() {
        ActiveRadio.setDisable(true);
        InactiveRadio.setDisable(true);
        CustomerIDTextField.setDisable(true);
        NameTextField.setDisable(true);
        AddressTextField.setDisable(true);
        CountryLabel.setDisable(true);
        CityChoiceBox.setDisable(true);
        PostalTextField.setDisable(true);
        PhoneTextField.setDisable(true);
        EditSelectedButton.setDisable(true);
        DeleteSelectedButton.setDisable(true);
        SaveCustomerInput.setDisable(true);
    } // FINISHED

    private void enableCustomerForm() {
        ActiveRadio.setDisable(false);
        InactiveRadio.setDisable(false);
        NameTextField.setDisable(false);
        AddressTextField.setDisable(false);
        CountryLabel.setDisable(false);
        CityChoiceBox.setDisable(false);
        PostalTextField.setDisable(false);
        PhoneTextField.setDisable(false);
        SaveCustomerInput.setDisable(false);

    } // FINISHED

    private void wipeCustomerForm() {

        LastUpdateTextField.setText("");
        PhoneTextField.setText("");
        PostalTextField.setText("");
        AddressTextField.setText("");
        NameTextField.setText("");
        CustomerIDTextField.setText("");
        ActiveRadio.setSelected(false);
        InactiveRadio.setSelected(false);
        CityChoiceBox.valueProperty().set(null);
        //CountryLabel.setText("");

    } // FINISHED 

    private boolean validateBeforeSave() {
        String saveName = NameTextField.getText();
        String saveAddress = AddressTextField.getText();
        String savePostal = PostalTextField.getText();
        String savePhone = PhoneTextField.getText();
        String citySave = CityChoiceBox.getValue();
        String countrySave = CountryLabel.getText();
        boolean saveActive = ActiveRadio.isSelected();
        boolean saveInactive = InactiveRadio.isSelected();

        if (saveName == null || saveName.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Correct Missing Information");
            alert.setContentText(" Must Give Customer Name");
            Optional<ButtonType> result = alert.showAndWait();

            return false;
        }
        if (saveAddress == null || saveAddress.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Correct Missing Information");
            alert.setContentText(" Must Give Customer Address");
            Optional<ButtonType> result = alert.showAndWait();

            return false;
        }
        if (savePostal == null || savePostal.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Correct Missing Information");
            alert.setContentText(" Must Give Customer Postal Code");
            Optional<ButtonType> result = alert.showAndWait();

            return false;
        }
        if (citySave == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Correct Missing Information");
            alert.setContentText(" Must Choose Customer City");
            Optional<ButtonType> result = alert.showAndWait();

            return false;
        }
        if (countrySave == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Correct Missing Information");
            alert.setContentText(" Must Choose Customer Country");
            Optional<ButtonType> result = alert.showAndWait();

            return false;
        }
        if (savePhone == null || savePhone.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Correct Missing Information");
            alert.setContentText(" Must Give Customer Phone Number");
            Optional<ButtonType> result = alert.showAndWait();

            return false;
        }
        if (!saveActive & !saveInactive) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Correct Missing Information");
            alert.setContentText(" Must Choose Active or Inactive");
            Optional<ButtonType> result = alert.showAndWait();

            return false;
        }

        return true;
    } // FINISHED 

    private boolean saveToDatabase() throws SQLException, Exception {
        String idString = CustomerIDTextField.getText();
        String nameSave = NameTextField.getText();
        String addressSave = AddressTextField.getText();
        String citySave = CityChoiceBox.getValue();
        String countrySave = CountryLabel.getText();
        String postalSave = PostalTextField.getText();
        String phoneSave = PhoneTextField.getText();
        String lastUpdateBySave = User.getUsername();
        int cityIdSave = getCityID(CityChoiceBox.getValue());
        int countryIdSave = getCountryId(CountryLabel.getText());
        if (idString == null || idString.length() == 0) {
            String insertAddress = "INSERT INTO address(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy) VALUES(?, 'N/A', ?, ?, ?, CURRENT_TIMESTAMP, ?, ?)";
            PreparedStatement psAddress;
            psAddress = (PreparedStatement) conn.prepareStatement(insertAddress);
            psAddress.setString(1, addressSave);
            psAddress.setInt(2, cityIdSave);
            psAddress.setString(3, postalSave);
            psAddress.setString(4, phoneSave);
            psAddress.setString(5, lastUpdateBySave);
            psAddress.setString(6, lastUpdateBySave);
            int rs = psAddress.executeUpdate();

            String newAddressSelect = "SELECT addressId FROM address WHERE address.address = '" + addressSave + "'";
            PreparedStatement newAddressPs;
            newAddressPs = (PreparedStatement) conn.prepareStatement(newAddressSelect);
            ResultSet newAddressRs = newAddressPs.executeQuery();
            newAddressRs.beforeFirst();
            newAddressRs.next();
            int newAddressId = newAddressRs.getInt("addressId");
            String insertCustomer = "INSERT INTO customer(customerName, addressId, active, createDate, createdBy, lastUpdateBy) VALUES(?, ?, ?, CURRENT_TIMESTAMP, ?, ?)";
            PreparedStatement psCustomer;
            psCustomer = (PreparedStatement) conn.prepareStatement(insertCustomer);
            psCustomer.setString(1, nameSave);
            psCustomer.setInt(2, newAddressId);
            psCustomer.setInt(3, activeInt);
            psCustomer.setString(4, lastUpdateBySave);
            psCustomer.setString(5, lastUpdateBySave);
            int rsC = psCustomer.executeUpdate();
        } else {
            String updateAddress = "UPDATE address, customer, city, country "
                    + "SET address = ?, address2 = 'N/A', address.cityId = ?, postalCode = ?, phone = ?, address.lastUpdate = CURRENT_TIMESTAMP, address.lastUpdateBy = ? "
                    + "WHERE customer.customerId = ? AND customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId";

            PreparedStatement psUpdateAddress = (PreparedStatement) conn.prepareStatement(updateAddress);
            psUpdateAddress.setString(1, addressSave);
            psUpdateAddress.setInt(2, getCityID(CityChoiceBox.getValue()));
            psUpdateAddress.setString(3, postalSave);
            psUpdateAddress.setString(4, phoneSave);
            psUpdateAddress.setString(5, lastUpdateBySave);
            psUpdateAddress.setString(6, idString);
            int result = psUpdateAddress.executeUpdate();

            String updateStatement = "UPDATE customer, address, city , country SET customerName = ?, customer.active = ?, customer.lastUpdate = CURRENT_TIMESTAMP,  customer.lastUpdateBy = ? "
                    + "WHERE customer.customerId = ? AND customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId";
            PreparedStatement psUpdateCustomer = (PreparedStatement) conn.prepareStatement(updateStatement);
            psUpdateCustomer.setString(1, nameSave);
            psUpdateCustomer.setInt(2, activeInt);
            psUpdateCustomer.setString(3, lastUpdateBySave);
            psUpdateCustomer.setString(4, idString);
            int rsUC = psUpdateCustomer.executeUpdate();
        }
        return true;
    }

    private String fillCountryLabel(String city) throws SQLException, Exception {
        if (city != null) {
            String fillCountry = "SELECT countryId FROM city WHERE city.cityId = " + getCityID(city);
            PreparedStatement ps;
            ps = (PreparedStatement) conn.prepareStatement(fillCountry);
            ResultSet rs = ps.executeQuery();
            rs.beforeFirst();
            rs.next();
            String newFillCountry = getCountry(rs.getInt("countryId"));
            CountryLabel.setText(newFillCountry);
            return newFillCountry;
        } else {
            String newFillCountry = "Pending";
            CountryLabel.setText(newFillCountry);
            return newFillCountry;
        }
    }

    private void startListener() {
        CityChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String value, String new_value) {
                if (CityChoiceBox != null && new_value != null) {
                    String kd = new_value;
                    try {
                        CountryLabel.setText(fillCountryLabel(CityChoiceBox.getValue()));
                    } catch (Exception ex) {
                        Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PropertyValueFactory<Customer, String> custNameFactory = new PropertyValueFactory<>("CustomerName");
        PropertyValueFactory<Customer, String> custAddressFactory = new PropertyValueFactory<>("CustomerAddress");
        PropertyValueFactory<Customer, String> custPhoneFactory = new PropertyValueFactory<>("CustomerPhone"); //String value "CustomerPhone" calls getCustomerPhone method
        PropertyValueFactory<Customer, Integer> custCustomerIDFactory = new PropertyValueFactory<>("CustomerID");
        PropertyValueFactory<Customer, String> custLastUpdateBy = new PropertyValueFactory<>("CustomerLastUpdateBy");
        PropertyValueFactory<Customer, String> custActiveFactory = new PropertyValueFactory<>("ActiveString");
        CustomerIDColumn.setCellValueFactory(custCustomerIDFactory);
        NameColumn.setCellValueFactory(custNameFactory);
        AddressColumn.setCellValueFactory(custAddressFactory);
        PhoneColumn.setCellValueFactory(custPhoneFactory);
        LastUpdateColumn.setCellValueFactory(custLastUpdateBy);
        ActiveColumn.setCellValueFactory(custActiveFactory);
        CustomerIDTextField.setText("Auto Generated");
        CustomerIDTextField.setDisable(true);
        LastUpdateTextField.setDisable(true);
        LastUpdateTextField.setText("Auto Generated");

        try {
            updateCustomerTableView();
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        disableCustomerForm();
        try {
            fillCityChoiceBox();
        } catch (Exception ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        startListener();
        wipeCustomerForm();
    }

    @FXML
    private void tableMouseClick(MouseEvent event) throws SQLException, Exception {
        Customer cust = CustomerTable.getSelectionModel().getSelectedItem();
        int idLookup = cust.getCustomerId();

        makeQuery("SELECT * FROM customer, address, city, country WHERE customer.customerId = " + idLookup + " AND customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId");
        ResultSet clickSet = getResult();
        while (clickSet.next()) {
            PostalTextField.setText(clickSet.getString("postalCode"));
            CityChoiceBox.setValue(clickSet.getString("city"));
            CountryLabel.setText(clickSet.getString("country"));
            NameTextField.setText(clickSet.getString("customerName"));
            AddressTextField.setText(clickSet.getString("address"));
            PhoneTextField.setText(clickSet.getString("phone"));
            LastUpdateTextField.setText(clickSet.getString("lastUpdateBy"));
            CustomerIDTextField.setText(clickSet.getString("customerId"));
            if (clickSet.getInt("active") == 0) {
                ActiveRadio.setSelected(false);
                InactiveRadio.setSelected(true);
            } else {
                ActiveRadio.setSelected(true);
                InactiveRadio.setSelected(false);
            }

        }
        EditSelectedButton.setDisable(false);
        DeleteSelectedButton.setDisable(false);
    } //FINISHED

    @FXML
    private void CustomerIDTextFieldHandler(ActionEvent event) {

    }

    @FXML
    private void NameTextFieldHandler(ActionEvent event) {
    }

    @FXML
    private void AddressTextFieldHandler(ActionEvent event) {
    }

    @FXML
    private void PhoneTextFieldHandler(ActionEvent event) {
    }

    @FXML
    private void LastUpdateTextFieldHandler(ActionEvent event) {
    }

    @FXML
    private void ActiveRadioHandler(ActionEvent event) {
        isActive = true;
        activeInt = 1;
        InactiveRadio.setSelected(false);
    } // FINISHED

    @FXML
    private void InactiveRadioHandler(ActionEvent event) {
        isActive = false;
        activeInt = 0;
        ActiveRadio.setSelected(false);
    } // FINISHED

    @FXML
    private void ClearCustomerHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm Clear");
        alert.setContentText("Are you sure you want to Clear Form?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            CustomerTable.setDisable(false);
            disableCustomerForm();
            wipeCustomerForm();
            NewCustomerButton.setDisable(false);
            EditSelectedButton.setDisable(true);
            DeleteSelectedButton.setDisable(true);
        }
    } //FINISHED

    @FXML
    private void DeleteCustomerHandler(ActionEvent event) throws SQLException {
        ObservableList<Customer> custClear = FXCollections.observableArrayList();
        Customer cust = CustomerTable.getSelectionModel().getSelectedItem();
        PreparedStatement delPull;
        int idLookup = cust.getCustomerId();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm Delete");
        alert.setContentText("Are you sure you want to Delete " + cust.getCustomerName() + " ?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            try {
                delPull = (PreparedStatement) conn.prepareStatement("DELETE customer.*, address.* from customer, address WHERE customer.customerId = ? AND customer.addressId = address.addressId");
                delPull.setInt(1, idLookup);
                int executeUpdate = delPull.executeUpdate();
            } catch (SQLException e) {
            }
            CustomerTable.setDisable(false);
            disableCustomerForm();
            updateCustomerTableView();
            wipeCustomerForm();
        }

    } // FINISHED

    @FXML
    private void CustomerToMainHandler(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        stage = (Stage) CustomerToMainButton.getScene().getWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    } // FINISHED

    @FXML
    private void EditCustomerHandler(ActionEvent event) {
        enableCustomerForm();
        CustomerTable.setDisable(true);
        DeleteSelectedButton.setDisable(true);
        NewCustomerButton.setDisable(true);
    } //FINISHED

    @FXML
    private void NewCustomerHandler(ActionEvent event) {
        enableCustomerForm();
        CustomerTable.setDisable(true);
        wipeCustomerForm();
        EditSelectedButton.setDisable(true);
        DeleteSelectedButton.setDisable(true);
        NewCustomerButton.setDisable(true);

    } //FINISHED

    @FXML
    private void SaveInputHandler(ActionEvent event) throws Exception {
        if (validateBeforeSave()) {
            if (saveToDatabase());
            {
                CustomerTable.setDisable(false);
                disableCustomerForm();
                wipeCustomerForm();
                updateCustomerTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Entry Not Saved");
            alert.setContentText("Entry Not Saved");
            Optional<ButtonType> result = alert.showAndWait();
        }
        NewCustomerButton.setDisable(false);
        EditSelectedButton.setDisable(false);
        DeleteSelectedButton.setDisable(false);
    }

}
