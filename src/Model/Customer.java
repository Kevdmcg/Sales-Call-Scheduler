/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Date;
import java.util.Calendar;

/**
 *
 * @author kmcgh15
 */
// design customer objects to interact with customer table in database
//  table columns:  customerId (int) (auto) (primary key) , customerName (varchar), addressId (int) (foreign key to addresses) , active (tinyint) ,
// (creatDate (datetime) , createdBy (varchar) , lastUpdate (timestamp) , lasteUpdateBy (varchar)
// must require name, address, phone number (?)(from address table)
public class Customer {

    private int customerId; //Auto incremented in database
    private String customerName;
    private int active;
    private Date createDate;
    private String createdBy;
    private String address;
    private String address2;
    private String city;
    private String postalCode;
    private String phone;
    private String country;
    private Date lastUpdate;
    private String lastUpdateBy;
    private String activeString;
    private boolean activeBool;

    //constructor
    public Customer() {

    }

    public Customer(int customerId, String customerName, int active, String address, String address2, String city, String postalCode, String phone, String country, Date lastUpdate, String lastUpdateBy) {
        setCustomerId(customerId);
        setCustomerName(customerName);
        setCustomerActive(active);
        setCustomerAddress(address);
        setCustomerAddress2(address2);
        setCustomerCity(city);
        setCustomerPostalCode(postalCode);
        setCustomerPhone(phone);
        setCustomerCountry(country);
        setCustomerLastUpdate(lastUpdate);
        setCustomerLastUpdateBy(lastUpdateBy);

    }

    public Customer(int customerId, String customerName, int addressId, int active, String createDate, String lastUpdate, String lastUpdateby, Calendar createDateCalendar, Calendar lastUpdateCalendar) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Customer(int customerId, String customerName) {
        setCustomerId(this.customerId);
        setCustomerName(customerName);
    }

    public Customer(String string) {
        setCustomerName(customerName);
    }

    //getters
    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getCustomerActive() {
        return active;
    }

    public String getCustomerAddress() {
        return address;
    }

    public String getCustomerAddress2() {
        return address2;
    }

    public String getCustomerCity() {
        return city;
    }

    public String getCustomerPostalCode() {
        return postalCode;
    }

    public String getCustomerPhone() {
        return phone;
    }

    public String getCustomerCountry() {
        return country;
    }

    public Date getCustomerLastUpdate() {
        return lastUpdate;
    }

    public String getCustomerLastUpdateBy() {
        return lastUpdateBy;
    }

    public String getActiveString() {
        return activeString;
    }

    //setters
    public void setCustomerId(int customerId) {

        this.customerId = customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerActive(int active) {
        this.active = active;
    }

    public void setCustomerAddress(String address) {
        this.address = address;
    }

    public void setCustomerAddress2(String address2) {
        this.address2 = address2;
    }

    public void setCustomerCity(String city) {
        this.city = city;
    }

    public void setCustomerPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCustomerPhone(String phone) {
        this.phone = phone;
    }

    public void setCustomerCountry(String country) {
        this.country = country;
    }

    public void setCustomerLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setCustomerLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public void setActiveString(String activeString) {

        if (this.active != 1) {
            this.activeString = "N";
        } else {
            this.activeString = "Y";
        }

    }

}
