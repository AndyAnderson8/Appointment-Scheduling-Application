package andyanderson.appointments.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class to create Appointment instances and to read and write from them, as well as serves as an abstraction for database records
 * @author Andy Anderson
 */
public class Appointment {
    private static final String[] businessHours = {"08:00:00", "22:00:00"}; // in EST
    private static final String[] types = {"Planning Session", "De-Briefing", "Consultation", "Other"};
    private static int idCounter = 0;
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private String startDateTime;
    private String endDateTime;
    private Contact contact;
    private Customer customer;
    private User user;

    /**
     * Constructor method for an Appointment
     * @param appointmentID appointment's id number
     * @param title appointment's title
     * @param description appointment's description
     * @param location location appointment is held at
     * @param type type of appointment
     * @param startDateTime appointment starting datetime in UTC
     * @param endDateTime appointment ending datetime in UTC
     * @param contact appointment's contact
     * @param customer appointment's customer
     * @param user appointment's user
     */
    public Appointment(int appointmentID, String title, String description, String location, String type, String startDateTime, String endDateTime, Contact contact, Customer customer, User user) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.contact = contact;
        this.customer = customer;
        this.user = user;
        idCounter = Math.max(idCounter, appointmentID);
    }

    /**
     * Method to get all possible appointment types
     * @return appointment types
     */
    public static ObservableList<String> getTypes() {
        ObservableList<String> typeList = FXCollections.observableArrayList(); // has to be an observable list to set items like it
        typeList.addAll(types);
        return typeList;
    }

    /**
     * Method to get the business hours appointments are allowed to be scheduled during
     * @return business hours
     */
    public static String[] getBusinessHours() {
        return businessHours;
    }

    /**
     * Method to get the next available ID to create a new Appointment instance
     * @return next available appointment ID
     */
    public static int getNextAppointmentID() {
        return idCounter + 1;
    }

    /**
     * Method to get an appointment's ID
     * @return appointment's ID
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * Method to get an appointment's title
     * @return appointment's ID
     */
    public String getTitle() {
        return title;
    }

    /**
     * Method to set an appointment's title
     * @param title appointment's title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Method to get an appointment's description
     * @return appointment's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method to set an appointment's description
     * @param description appointment's description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Method to get an appointment's location
     * @return appointment's location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Method to set an appointment's location
     * @param location appointment's location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Method to get an appointment's type
     * @return appointment's type
     */
    public String getType() {
        return type;
    }

    /**
     * Method to set an appointment's type
     * @param type appointment's type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Method to get an appointment's start datetime in UTC
     * @return appointment's start datetime
     */
    public String getStartDateTime() {
        return startDateTime;
    }

    /**
     * Method to set an appointment's start datetime in UTC
     * @param startDateTime appointment's start datetime
     */
    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * Method to get an appointment's end datetime in UTC
     * @return appointment's end datetime
     */
    public String getEndDateTime() {
        return endDateTime;
    }

    /**
     * Method to set an appointment's end datetime in UTC
     * @param endDateTime appointment's end datetime
     */
    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * Method to get an appointment's contact
     * @return appointment's contact
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Method to set an appointment's contact
     * @param contact appointment's contact
     */
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /**
     * Method to get an appointment's customer
     * @return appointment's customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Method to set an appointment's customer
     * @param customer appointment's customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Method to get an appointment's user
     * @return appointment's user
     */
    public User getUser() {
        return user;
    }

    /**
     * Method to set an appointment's user
     * @param user appointment's user
     */
    public void setUser(User user) {
        this.user = user;
    }
}