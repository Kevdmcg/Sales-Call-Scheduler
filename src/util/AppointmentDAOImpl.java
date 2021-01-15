/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import Model.City;
import Model.Country;
import Model.Customer;
import Model.User;
import Model.address;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import static util.TimeFiles.stringToCalendar;

/**
 *
 * @author kmcgh15
 */
public class AppointmentDAOImpl {

    static boolean act;

    //  seperate methods for pull from each table maybe
    // address, appointment, city, country, customer, user
    // seperate methods for SELECT, UPDATE, INSERT, DELETE
    // address table
    // SELECT
    public static address getAddress(String addressId) throws SQLException, Exception {
        // type is name or phone, value is the name or the phone #
        DBConnection.makeConnection();
        String sqlStatement = "select * FROM address WHERE addressId  = '" + addressId + "'";
        //  String sqlStatement="select FROM address";
        Query.makeQuery(sqlStatement);
        address addressResult;
        ResultSet result = Query.getResult();
        while (result.next()) {
            int addressid = result.getInt("addressId");
            String address = result.getString("address");
            String address2 = result.getString("address2");
            int cityId = result.getInt("cityId");
            String postalCode = result.getString("postalCode");
            String phone = result.getString("phone");
            String createDate = result.getString("createDate");
            String lastUpdate = result.getString("lastUpdate");
            String lastUpdateby = result.getString("lastUpdateBy");
            Calendar createDateCalendar = stringToCalendar(createDate);
            Calendar lastUpdateCalendar = stringToCalendar(lastUpdate);
            //   s(int addressId, String address, String address2, int cityId, String postalCode, String phone, Calendar createDate, String createdBy, Calendar lastUpdate, String lastUpdateBy)
            addressResult = new address(addressid, addressId, address, address2, cityId, postalCode, phone, lastUpdate, lastUpdateby, createDateCalendar, lastUpdateCalendar);
            return addressResult;
        }
        DBConnection.closeConnections();
        return null;
    }

    //  UPDATE
    public static address updateAddress(String addressId) {

        return null;
    }

    // INSERT
    public static void createAddress(String addressId) {

    }

    // DELETE
    public static void deleteAddress(String addressId) {

    }

    // appointment table
    // SELECT
    /* public static Appointment getAppointment(String appointmentId) throws SQLException, Exception{
        // type is name or phone, value is the name or the phone #
         DBConnection.makeConnection();
        String sqlStatement="select * FROM appointment WHERE appointmentId  = '" + appointmentId+ "'";
         //  String sqlStatement="select FROM appointment";
        Query.makeQuery(sqlStatement);
           Appointment appointmentResult;
           ResultSet result=Query.getResult();
           while(result.next()){
                int appointmentid=result.getInt("appointmentId");
                int customerId=result.getInt("customerId");
                int userId=result.getInt("userId");
                String title=result.getString("title");
                String description=result.getString("description");
                String location=result.getString("location");
                String contact=result.getString("contact");
                String type=result.getString("type");
                String url=result.getString("url");
                String start=result.getString("start");
                String end=result.getString("end");
                String postalCode=result.getString("postalCode");
                String phone=result.getString("phone");
                String createDate=result.getString("createDate");
                String lastUpdate=result.getString("lastUpdate");
                String lastUpdateby=result.getString("lastUpdateBy");
                Calendar createDateCalendar=stringToCalendar(createDate);
                Calendar lastUpdateCalendar=stringToCalendar(lastUpdate);
             //   s(int addressId, String address, String address2, int cityId, String postalCode, String phone, Calendar createDate, String createdBy, Calendar lastUpdate, String lastUpdateBy)
             //   appointmentResult= new Appointment(appointmentid, customerId, userId, title, description, location, contact, type, url , start, end, postalCode,phone, createDate, lastUpdate, lastUpdateby, createDateCalendar, lastUpdateCalendar );
                return appointmentResult;
           }
             DBConnection.closeConnections();
        return null;
    }
     */
    //  UPDATE
    public static void updateAppointment(String appointmentId) {

    }

    // INSERT
    public static void createAppointment(String appointmentId) {

    }

    // DELETE
    public static void deleteAppointment(String appointmentId) {

    }

    // city table
    // SELECT
    public static City getCity(String cityId) throws SQLException, Exception {
        // type is name or phone, value is the name or the phone #
        DBConnection.makeConnection();
        String sqlStatement = "select * FROM city WHERE cityId  = '" + cityId + "'";
        //  String sqlStatement="select FROM appointment";
        Query.makeQuery(sqlStatement);
        City cityResult;
        ResultSet result = Query.getResult();
        while (result.next()) {
            int cityid = result.getInt("cityId");
            int countryId = result.getInt("countryId");
            String cityName = result.getString("city");
            String createDate = result.getString("createDate");
            String lastUpdate = result.getString("lastUpdate");
            String lastUpdateby = result.getString("lastUpdateBy");
            Calendar createDateCalendar = stringToCalendar(createDate);
            Calendar lastUpdateCalendar = stringToCalendar(lastUpdate);
            //   s(int addressId, String address, String address2, int cityId, String postalCode, String phone, Calendar createDate, String createdBy, Calendar lastUpdate, String lastUpdateBy)
            cityResult = new City(cityid, countryId, cityName, createDate, lastUpdate, lastUpdateby, createDateCalendar, lastUpdateCalendar);
            return cityResult;
        }
        DBConnection.closeConnections();
        return null;
    }

    //  UPDATE
    public static void updateCity(String cityId) {

    }

    // INSERT
    public static void createCity(String cityId) {

    }

    // DELETE
    public static void deleteCity(String cityId) {

    }

    // country table
    // SELECT
    public static Country getCountry(String countryId) throws SQLException, Exception {
        // type is name or phone, value is the name or the phone #
        DBConnection.makeConnection();
        String sqlStatement = "select * FROM country WHERE countryId  = '" + countryId + "'";
        //  String sqlStatement="select FROM appointment";
        Query.makeQuery(sqlStatement);
        Country countryResult;
        ResultSet result = Query.getResult();
        while (result.next()) {
            int countryid = result.getInt("countryId");
            String countryName = result.getString("country");
            String createDate = result.getString("createDate");
            String lastUpdate = result.getString("lastUpdate");
            String lastUpdateby = result.getString("lastUpdateBy");
            Calendar createDateCalendar = stringToCalendar(createDate);
            Calendar lastUpdateCalendar = stringToCalendar(lastUpdate);
            //   s(int addressId, String address, String address2, int cityId, String postalCode, String phone, Calendar createDate, String createdBy, Calendar lastUpdate, String lastUpdateBy)
            countryResult = new Country(countryId, countryName, createDate, lastUpdate, lastUpdateby, createDateCalendar, lastUpdateCalendar);
            return countryResult;
        }
        DBConnection.closeConnections();
        return null;
    }

    //  UPDATE
    public static void updateCountry(String countryId) {

    }

    // INSERT
    public static void createCountry(String countryId) {

    }

    // DELETE
    public static void deleteCountry(String countryId) {

    }

    // customer table
    // SELECT
    public static Customer getCustomer(String customerId) throws SQLException, Exception {
        // type is name or phone, value is the name or the phone #
        DBConnection.makeConnection();
        String sqlStatement = "select * FROM customer WHERE customerId  = '" + customerId + "'";
        //  String sqlStatement="select FROM appointment";
        Query.makeQuery(sqlStatement);
        Customer customerResult;
        ResultSet result = Query.getResult();
        while (result.next()) {
            int customerid = result.getInt("customerId");
            String customerName = result.getString("customerName");
            int addressId = result.getInt("addressId");
            int active = result.getInt("active");
            if (active == 1) {
                act = true;
            }
            String createDate = result.getString("createDate");
            String lastUpdate = result.getString("lastUpdate");
            String lastUpdateby = result.getString("lastUpdateBy");
            Calendar createDateCalendar = stringToCalendar(createDate);
            Calendar lastUpdateCalendar = stringToCalendar(lastUpdate);
            //   s(int addressId, String address, String address2, int cityId, String postalCode, String phone, Calendar createDate, String createdBy, Calendar lastUpdate, String lastUpdateBy)
            customerResult = new Customer(customerid, customerName, addressId, active, createDate, lastUpdate, lastUpdateby, createDateCalendar, lastUpdateCalendar);
            return customerResult;
        }
        DBConnection.closeConnections();
        return null;
    }

    //  UPDATE
    public static void updateCustomer(String customerId) {

    }

    // INSERT
    public static void createCustomer(String customerId) {

    }

    // DELETE
    public static void deleteCustomer(String customerId) {

    }

    // user table
    // SELECT
    public static User getUser(String userId) throws SQLException, Exception {
        // type is name or phone, value is the name or the phone #
        DBConnection.makeConnection();
        String sqlStatement = "select * FROM user WHERE userName  = '" + userId + "'";
        //  String sqlStatement="select FROM address";
        Query.makeQuery(sqlStatement);
        User userResult;
        ResultSet result = Query.getResult();
        while (result.next()) {
            int userid = result.getInt("userid");
            String userNameG = result.getString("userName");
            String password = result.getString("password");
            int active = result.getInt("active");
            if (active == 1) {
                act = true;
            }
            String createDate = result.getString("createDate");
            String createdBy = result.getString("createdBy");
            String lastUpdate = result.getString("lastUpdate");
            String lastUpdateby = result.getString("lastUpdateBy");
            Calendar createDateCalendar = stringToCalendar(createDate);
            Calendar lastUpdateCalendar = stringToCalendar(lastUpdate);
            //   s(int addressId, String address, String address2, int cityId, String postalCode, String phone, Calendar createDate, String createdBy, Calendar lastUpdate, String lastUpdateBy)
            userResult = new User(userid, userId, password);
            return userResult;
        }
        DBConnection.closeConnections();
        return null;
    }

    //  UPDATE
    public static void updateUser(String userId) {

    }

    // INSERT
    public static void createUser(String userId) {

    }

    // DELETE
    public static void deleteUser(String userId) {

    }

}
