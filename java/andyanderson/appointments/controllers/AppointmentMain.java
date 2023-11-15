package andyanderson.appointments.controllers;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;

import andyanderson.appointments.Database;
import andyanderson.appointments.Utils;
import andyanderson.appointments.models.Appointment;

/**
 * Controller class to manage the main view of appointment records
 * @author Andy Anderson
 */
public class AppointmentMain implements Initializable {
    private static Appointment selectedAppointment;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, Integer> idColumn;
    @FXML
    private TableColumn<Appointment, String> titleColumn;
    @FXML
    private TableColumn<Appointment, String> descriptionColumn;
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    @FXML
    private TableColumn<Appointment, String> locationColumn;
    @FXML
    private TableColumn<Appointment, String> startTimeColumn;
    @FXML
    private TableColumn<Appointment, String> startDateColumn;
    @FXML
    private TableColumn<Appointment, String> endTimeColumn;
    @FXML
    private TableColumn<Appointment, String> endDateColumn;
    @FXML
    private TableColumn<Appointment, String> customerColumn;
    @FXML
    private TableColumn<Appointment, String> contactColumn;
    @FXML
    private TableColumn<Appointment, String> userColumn;

    /**
     * Method to get the selected appointment from the appointment table
     * @return selected appointment
     */
    public static Appointment getSelectedAppointment() {
        return selectedAppointment;
    }

    /**
     * Method to set the selected appointment from the appointment table
     * @param appointment selected appointment
     */
    public static void setSelectedAppointment(Appointment appointment) {
        selectedAppointment = appointment;
    }

    /**
     * Method to populate the appointment table with all appointments up to one week away
     */
    public void showWeekView() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        for (Appointment appointment : Database.getAppointments()) {
            if (Utils.addDateTimeMinutes(Utils.getDateTime("UTC"), 60 * 24 * 7).compareTo(appointment.getStartDateTime()) > 0 && // one week out
                Utils.getDateTime("UTC").compareTo(appointment.getEndDateTime()) < 0) { // not in the past
                appointments.add(appointment);
            }
        }
        appointmentTable.setItems(appointments);
    }

    /**
     * Method to populate the appointment table with all appointments up to one month away
     */
    public void showMonthView() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        for (Appointment appointment : Database.getAppointments()) {
            if (Utils.addDateTimeMinutes(Utils.getDateTime("UTC"), 60 * 24 * 31).compareTo(appointment.getStartDateTime()) > 0 && // one month out
                Utils.getDateTime("UTC").compareTo(appointment.getEndDateTime()) < 0) { // not in the past
                appointments.add(appointment);
            }
        }
        appointmentTable.setItems(appointments);
    }

    /**
     * Method to populate the appointment table with all appointments
     */
    public void showAllView() {
        appointmentTable.setItems(Database.getAppointments());
    }

    /**
     * Method to load the add appointment view
     * @param event event
     */
    public void addAppointment(ActionEvent event) {
        Utils.loadScreen(event, "AppointmentAdd.fxml");
    }

    /**
     * Method to load the modify appointment view
     * @param event event
     */
    public void modifyAppointment(ActionEvent event) {
        setSelectedAppointment(appointmentTable.getSelectionModel().getSelectedItem());
        if (getSelectedAppointment() == null) { // no item selected
            Utils.errorMessage("No customer selected");
        } else {
            Utils.loadScreen(event, "AppointmentModify.fxml");
        }
    }

    /**
     * Method to delete the selected appointment from the database after getting confirmation from the user
     */
    public void deleteAppointment() {
        setSelectedAppointment(appointmentTable.getSelectionModel().getSelectedItem());
        if (getSelectedAppointment() == null) { // no item selected
            Utils.errorMessage("No customer selected");
        } else if (Utils.confirmationMessage("Are you sure you want to delete this record?")) {
            String apptInfo = "(ID: " + getSelectedAppointment().getAppointmentID() + ", Type: " + getSelectedAppointment().getType() + ")";
            Database.deleteAppointment(getSelectedAppointment());
            appointmentTable.setItems(Database.getAppointments());
            Utils.informationMessage("Record was successfully deleted\n" + apptInfo);
        }
    }

    /**
     * Method to load the main customer view
     * @param event event
     */
    public void loadCustomer(ActionEvent event) {
        Utils.loadScreen(event, "CustomerMain.fxml");
    }

    /**
     * Method to load the menu view
     * @param event event
     */
    public void loadMenu(ActionEvent event) {
        Utils.loadScreen(event, "Menu.fxml");
    }

    /**
     * Method to populate the appointment table with appointment records
     * LAMBDA: Uses lambda expressions to populate start and end dates and times, as well as identifiable strings for
     * the contacts, customers, and users. None of this data is possible to display just by getting a single property
     * from the object since they are not directly stored as an attribute.
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentTable.setItems(Database.getAppointments());
        idColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTimeColumn.setCellValueFactory(cellData -> { // Lambda
            return new ReadOnlyStringWrapper(Utils.formatDateTime(Utils.convertDateTime(cellData.getValue().getStartDateTime(), "UTC", "local"), "yyyy-MM-dd HH:mm:ss", "h:mm a"));
        });
        startDateColumn.setCellValueFactory(cellData -> { // Lambda
            return new ReadOnlyStringWrapper(Utils.formatDateTime(Utils.convertDateTime(cellData.getValue().getStartDateTime(), "UTC", "local"), "yyyy-MM-dd HH:mm:ss", "MMMM d, yyyy"));
        });
        endTimeColumn.setCellValueFactory(cellData -> { // Lambda
            return new ReadOnlyStringWrapper(Utils.formatDateTime(Utils.convertDateTime(cellData.getValue().getEndDateTime(), "UTC", "local"), "yyyy-MM-dd HH:mm:ss", "h:mm a"));
        });
        endDateColumn.setCellValueFactory(cellData -> { // Lambda
            return new ReadOnlyStringWrapper(Utils.formatDateTime(Utils.convertDateTime(cellData.getValue().getEndDateTime(), "UTC", "local"), "yyyy-MM-dd HH:mm:ss", "MMMM d, yyyy"));
        });
        contactColumn.setCellValueFactory(cellData -> { // Lambda
            return new ReadOnlyStringWrapper(cellData.getValue().getContact().getName() + " (ID: " + cellData.getValue().getContact().getContactID() + ")");
        });
        customerColumn.setCellValueFactory(cellData -> { // Lambda
            return new ReadOnlyStringWrapper(cellData.getValue().getCustomer().getName() + " (ID: " + cellData.getValue().getCustomer().getCustomerID() + ")");
        });
        userColumn.setCellValueFactory(cellData -> { // Lambda
            return new ReadOnlyStringWrapper(cellData.getValue().getUser().getUsername() + " (ID: " + cellData.getValue().getUser().getUserID() + ")");
        });
    }
}