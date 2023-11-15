package andyanderson.appointments;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class to manage a variety of helper methods primarily involving alerts and time conversions
 * @author Andy Anderson
 */
public class Utils {
    /**
     * Method to give an information alert to the user
     * @param message alert message
     */
    public static void informationMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Method to give an error alert to the user
     * @param message alert message
     */
    public static void errorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Method to give a confirmation alert to the user and get their selection
     * @param message alert message
     * @return user selection
     */
    public static boolean confirmationMessage(String message) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setContentText(message);
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.orElse(null) == ButtonType.OK) {
            return true;
        }
        return false;
    }

    /**
     * Method to load a new fxml view
     * @param event event
     * @param fxml fxml file to load
     */
    public static void loadScreen(ActionEvent event, String fxml) {
        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(Utils.class.getResource(fxml)));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Error: " + e.getStackTrace() + e.getCause() + e.getMessage());
        }
    }

    /**
     * Method to get the current datetime
     * @param timeZone timezone to return the datetime for
     * @return current datetime
     */
    public static String getDateTime(String timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (timeZone.toLowerCase().equals("local")) {
            sdf.setTimeZone(TimeZone.getDefault());
        } else {
            sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        }
        return sdf.format(new Date());
    }

    /**
     * Method to convert a datetime from one timezone to another
     * @param dateTime the datetime to convert
     * @param originalTimeZone the original timezone
     * @param newTimeZone the new timezone
     * @return datetime in new timezone
     */
    public static String convertDateTime(String dateTime, String originalTimeZone, String newTimeZone) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (originalTimeZone.toLowerCase().equals("local")) {
                sdf.setTimeZone(TimeZone.getDefault());
            } else {
                sdf.setTimeZone(TimeZone.getTimeZone(originalTimeZone));
            }
            Date newDateTime = sdf.parse(dateTime);
            if (newTimeZone.toLowerCase().equals("local")) {
                sdf.setTimeZone(TimeZone.getDefault());
            } else {
                sdf.setTimeZone(TimeZone.getTimeZone(newTimeZone));
            }
            return sdf.format(newDateTime);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Method to format a datetime from one format to another
     * @param dateTime the datetime to convert
     * @param originalFormat datetime format to parse
     * @param newFormat the new format to convert to
     * @return datetime in new format
     */
    public static String formatDateTime(String dateTime, String originalFormat, String newFormat) {
        try {
            SimpleDateFormat sdfOriginal = new SimpleDateFormat(originalFormat);
            Date originalFormatTime = sdfOriginal.parse(dateTime);
            SimpleDateFormat sdfNew = new SimpleDateFormat(newFormat);
            return sdfNew.format(originalFormatTime);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Method to add time in minutes to a datetime
     * @param datetime datetime to add time to
     * @param minutes the number of minutes to add
     * @return the new datetime
     */
    public static String addDateTimeMinutes(String datetime, int minutes) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(datetime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, minutes);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}