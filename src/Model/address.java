/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author kmcgh15
 */

// adress table columns : addressId (int) , address (varchar) , address2 (varchar)(?) , cityId (int) (foreign key) , postalcode (varchar) ,
// phone (varchar) , createDate , createdBy , lastUpdate, lastUpdateBy
public class address {
    
    private int addressId;

    
    private String address;
    private String address2;
    private int cityId;
    private String postalCode;
    private String phone;
    private String createdBy;
    private String lastUpdateBy;
    private Calendar createDate;
    private Timestamp lastUpdate; 

    public address(int addressId, String address, String address2, int cityId, String postalCode, String phone, String createdBy, String lastUpdateBy, Calendar createDate, Timestamp lastUpdate) {
        this.addressId = addressId;
        this.address = address;
        this.address2 = address2;
        this.cityId = cityId;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    
    

    public address(int addressid, String addressId, String address, String address2, int cityId, String postalCode, String phone, String lastUpdate, String lastUpdateby, java.util.Calendar createDateCalendar, java.util.Calendar lastUpdateCalendar) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdatedBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public Calendar getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Calendar createDate) {
        this.createDate = createDate;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    
    
}
