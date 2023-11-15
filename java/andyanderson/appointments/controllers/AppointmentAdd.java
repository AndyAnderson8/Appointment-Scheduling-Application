package andyanderson.appointments.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

import andyanderson.appointments.Database;
import andyanderson.appointments.Utils;
import andyanderson.appointments.models.*;

/**
 * Controller class to manage the add appointment form
 * @author Andy Anderson
 */
public class AppointmentAdd implements Initializable {
    @FXML
    private ComboBox customerBox;
    @FXML
    private ComboBox contactBox;
    @FXML
    private ComboBox userBox;
    @FXML
    private ComboBox startTimeBox;
    @FXML
    private ComboBox endTimeBox;
    @FXML
    private ComboBox typeBox;
    @FXML
    private TextField titleField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField locationField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;

    /**
     * Method to add a new appointment record based on form input
     * @param event event
     */
    public void addAppointment(ActionEvent event) {
        Customer customer = Database.getCustomer(Integer.parseInt(customerBox.getValue().toString().split("ID:")[1].replaceAll("[^0-9]", "")));
        Contact contact = Database.getContact(Integer.parseInt(contactBox.getValue().toString().split("ID:")[1].replaceAll("[^0-9]", "")));
        User user = Database.getUser(Integer.parseInt(userBox.getValue().toString().split("ID:")[1].replaceAll("[^0-9]", "")));
        String startTime = startTimeBox.getValue().toString();
        String endTime = endTimeBox.getValue().toString();
        String type = typeBox.getValue().toString();
        String title = titleField.getText();
        String description = descriptionField.getText();
        String location = locationField.getText();
        String startDate = startDatePicker.getValue().toString();
        String endDate = endDatePicker.getValue().toString();

        if (customer == null || contact == null || user == null || startTime.equals("") || endTime.equals("") || type.equals("") || title.equals("") || description.equals("") || location.equals("") || startDate.equals("") || endDate.equals("")) {
            Utils.errorMessage("Please ensure are fields are filled before submitting form");
        } else {
            String startDateTime = Utils.convertDateTime(Utils.formatDateTime(startTime + " " + startDate, "h:mm a yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"), "local", "EST");
            String endDateTime = Utils.convertDateTime(Utils.formatDateTime(endTime + " " + endDate, "h:mm a yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"), "local", "EST");
            if (endDateTime.compareTo(startDateTime) < 0) {
                Utils.errorMessage("Appointment end set before appointment start");
            } else if (startDateTime.compareTo(Utils.getDateTime("EST")) < 0) {
                Utils.errorMessage("Cannot set an appointment in the past");
            } else if (
                !startDateTime.split(" ")[0].equals(endDateTime.split(" ")[0]) || //starts and ends on different days EST
                startDateTime.split(" ")[1].compareTo(Appointment.getBusinessHours()[0]) < 0 ||  // starts before opening
                endDateTime.split(" ")[1].compareTo(Appointment.getBusinessHours()[1]) > 0) { // ends after closing
                Utils.errorMessage("Appointment starts, ends, and/or runs outside of business hours");
            } else {
                startDateTime = Utils.convertDateTime(startDateTime, "EST", "UTC");
                endDateTime = Utils.convertDateTime(endDateTime, "EST", "UTC");
                Appointment newAppt = new Appointment(Appointment.getNextAppointmentID(), title, description, location, type, startDateTime, endDateTime, contact, customer, Login.getActiveUser());
                boolean conflictingApptFound = false;
                for (Appointment checkAppt : Database.getAppointments()) {
                    if (newAppt.getAppointmentID() != checkAppt.getAppointmentID()) { // Don't check against the same appt
                        String newApptStart = newAppt.getStartDateTime();
                        String checkApptStart = checkAppt.getStartDateTime();
                        String newApptEnd = newAppt.getEndDateTime();
                        String checkApptEnd = checkAppt.getEndDateTime();
                        if ((checkApptStart.compareTo(newApptStart) < 0 && newApptStart.compareTo(checkApptEnd) < 0) || // new appt starts between appt
                            (checkApptStart.compareTo(newApptEnd) < 0 && newApptEnd.compareTo(checkApptEnd) < 0)) { // new appt ends between appt
                            if (checkAppt.getCustomer().getCustomerID() == newAppt.getCustomer().getCustomerID()) {
                                Utils.errorMessage("Customer already has an appointment scheduled during this time");
                                conflictingApptFound = true;
                                break;
                            } else if (checkAppt.getContact().getContactID() == newAppt.getContact().getContactID()) {
                                Utils.errorMessage("Contact already has an appointment scheduled during this time");
                                conflictingApptFound = true;
                                break;
                            }
                        }
                    }
                }
                if (!conflictingApptFound) {
                    Database.addAppointment(newAppt);
                    Utils.loadScreen(event, "AppointmentMain.fxml");
                }
            }
        }
    }

    /**
     * Method to load the main appointment view
     * @param event event
     */
    public void loadAppointmentMain(ActionEvent event) {
        Utils.loadScreen(event, "AppointmentMain.fxml");
    }

    /**
     * Method to populate the form's selection boxes with valid options
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeBox.setItems(Appointment.getTypes());

        ObservableList<String> customers = FXCollections.observableArrayList();
        for (Customer customer : Database.getCustomers()) {
            customers.add(customer.getName() + " (ID: " + customer.getCustomerID() + ")");
        }
        customerBox.setItems(customers);

        ObservableList<String> contacts = FXCollections.observableArrayList();
        for (Contact contact : Database.getContacts()) {
            contacts.add(contact.getName() + " (ID: " + contact.getContactID() + ")");
        }
        contactBox.setItems(contacts);

        ObservableList<String> users = FXCollections.observableArrayList();
        for (User user : Database.getUsers()) {
            users.add(user.getUsername() + " (ID: " + user.getUserID() + ")");
        }
        userBox.setItems(users);

        ObservableList<String> times = FXCollections.observableArrayList();
        String businessHoursStartLocal = Utils.convertDateTime("2023-01-01 " + Appointment.getBusinessHours()[0], "EST", "local"); // 8am ET, date is irrelevant
        String businessHoursEndLocal = Utils.convertDateTime("2023-01-01 " + Appointment.getBusinessHours()[1], "EST", "local"); // 10pm ET, date is irrelevant
        String nextDateTime = businessHoursStartLocal;
        while (nextDateTime.compareTo(businessHoursEndLocal) <= 0) {
            times.add(Utils.formatDateTime(nextDateTime, "yyyy-MM-dd HH:mm:ss", "h:mm a"));
            nextDateTime = Utils.addDateTimeMinutes(nextDateTime, 30); // time selection in 30 min intervals
        }
        startTimeBox.setItems(times);
        endTimeBox.setItems(times);
    }
}