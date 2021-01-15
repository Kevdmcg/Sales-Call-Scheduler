/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

//  Needed to fill city table of db

import java.sql.Timestamp;
import java.util.Calendar;

// columns:  cityId (int) (auto),city (varchar) , countryId (int) (foreign key) , createDate (datetime) , createdBy (varchar) , lastUpdate (timestamp)
// lastUpdatedBy (varchar)

/**
 *
 * @author kmcgh15
 */
public class City {
    
    private int cityId;
    private String city;
    private int countryId;
    private Calendar createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;

    public City() {
    }

    public City(int cityId, String city, int countryId, Calendar createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
        this.cityId = cityId;
        this.city = city;
        this.countryId = countryId;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public City(int cityid, int countryId, String cityName, String createDate, String lastUpdate, String lastUpdateby, java.util.Calendar createDateCalendar, java.util.Calendar lastUpdateCalendar) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public Calendar getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Calendar createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
    
    
            
    
}
