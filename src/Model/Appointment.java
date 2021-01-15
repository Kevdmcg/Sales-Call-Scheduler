/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

// Needs to update the appointments table in database
import java.sql.Timestamp;
import java.util.Calendar;

// table fields are: appointmentId (INT)(auto) (primary) , customerId (INT) , userId (INT), title (varchar) , description (txt), location (txt), contact (txt) , type (txt)
// , url (varchar), start (datetime), end (datetime), createdDate (varchar), createdBy (varchar)
// lastUpdate (timestamp) , lastUpdatedBy (varchar)
// Min. required:  type of appointment , customerId, and time
/**
 *
 * @author kmcgh15
 */
public class Appointment {

    private int appointmentId;
    private int customerId;
    private int userId;
    public String customerName;
    private String appointmentDate;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    private String createdBy;
    private String lastUpdateBy;
    private String start;
    private String end;
    private Calendar createDate;
    private Timestamp lastUpdate;
    private String dateTime;
    private String userName;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Appointment() {
    }

    public Appointment(String userName, String start, String end, String customerName) {
        this.userName = userName;
        this.start = start;
        this.end = end;
        this.customerName = customerName;
    }

    public Appointment(int appointmentId, int customerId, int userId, String customerName, String appointmentDate, String title, String description, String location, String contact, String type, String url, String createdBy, String lastUpdateBy, String start, String end, Calendar createDate, Timestamp lastUpdate) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.userId = userId;
        this.customerName = customerName;
        this.appointmentDate = appointmentDate;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    /**
     *
     * @param appointmentId
     * @param customerId
     * @param customerName
     * @param start
     * @param end
     * @param appointmentDate
     * @param userId
     * @param title
     * @param description
     * @param contact
     * @param createdBy
     * @param location
     * @param type
     * @param url
     */
    /*public Appointment(int appointmentId, int customerId, String customerName, String start, String end, String appointmentDate, int userId, String title,
            String description, String contact, String createdBy, String location, String type, String url ) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.start = start;
        this.end = end;
        this.appointmentDate = appointmentDate;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.location = location;
        this.type = type;
        this.url = url;
    }
     */
    public Appointment(int appointmentId, int customerId, String customerName, String start, String end, String appointmentDate, int userId, String title,
            String description, String contact, String createdBy, String location, String type, String url, String dateTime) {

        setAppointmentId(appointmentId);
        setCustomerId(customerId);
        setCustomerName(customerName);
        setStart(start);
        setEnd(end);
        setAppointmentDate(appointmentDate);
        setUserId(userId);
        setTitle(title);
        setDescription(description);
        setCreatedBy(createdBy);
        setLocation(location);
        setType(type);
        setUrl(url);
        setDateTime(dateTime);
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
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
