package andyanderson.appointments.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

import andyanderson.appointments.Database;
import andyanderson.appointments.Utils;
import andyanderson.appointments.models.Appointment;

/**
 * Controller class to manage a menu of different views and tools provided by the application
 * @author Andy Anderson
 */
public class Menu implements Initializable {
    @FXML
    private Label alertLabel;

    /**
     * Method to load the main appointment view
     * @param event event
     */
    public void loadAppointments(ActionEvent event) {
        Utils.loadScreen(event, "AppointmentMain.fxml");
    }

    /**
     * Method to load the main customer view
     * @param event event
     */
    public void loadCustomers(ActionEvent event) {
        Utils.loadScreen(event, "CustomerMain.fxml");
    }

    /**
     * Method to load the report view
     * @param event event
     */
    public void loadReport(ActionEvent event) {
        Utils.loadScreen(event, "Report.fxml");
    }

    /**
     * Method to view the sign in attempt log in Notepad
     */
    public void viewLog() {
        try {
            Runtime.getRuntime().exec("notepad " + "./login_activity.txt");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Method to load the login screen and prepare for the next user
     * @param event event
     */
    public void logout(ActionEvent event) {
        Login.setActiveUser(null);
        Utils.loadScreen(event, "Login.fxml");
    }

    /**
     * Method to load an alert at the top of the screen if any appointments for the user are occurring in the next 15 minutes
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (Appointment appt : Database.getAppointments(Login.getActiveUser())) { // All appt. for active user
            String currentDateTimeEST = Utils.getDateTime("EST"); // "YYYY-MM-DD"
            String apptDateTimeEST = Utils.convertDateTime(appt.getStartDateTime(), "UTC", "EST");
            String dateTimeCutOff = Utils.addDateTimeMinutes(Utils.getDateTime("EST"), 15); // 15 min after current time
            if (currentDateTimeEST.split(" ")[0].equals(apptDateTimeEST.split(" ")[0]) && dateTimeCutOff.compareTo(apptDateTimeEST) > 0) { // appt is same day and within 15 min
                String apptLocalTime = Utils.convertDateTime(apptDateTimeEST, "EST", "local");
                String apptReadableTimeFormat = Utils.formatDateTime(apptLocalTime, "yyyy-MM-dd HH:mm:ss", "h:mm a - MMMM d, yyyy");
                alertLabel.setText("ALERT: Upcoming Appt: " + apptReadableTimeFormat + " (ID: " + appt.getAppointmentID() + ")");
                alertLabel.setStyle("-fx-background-color: Red;");
                break;
            }
        }
    }
}