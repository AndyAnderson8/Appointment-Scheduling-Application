package andyanderson.appointments.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

import andyanderson.appointments.Database;
import andyanderson.appointments.Utils;
import andyanderson.appointments.models.User;

/**
 * Controller class to manage the application login
 * @author Andy Anderson
 */
public class Login implements Initializable {
    private static User activeUser;
    @FXML
    private Text titleLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button submitButton;
    @FXML
    private Text locationLabel;

    /**
     * Method to get the user who is logged in as the active user
     * @return active user
     */
    public static User getActiveUser() {
        return activeUser;
    }

    /**
     * Method to set the user who is logged in as the active user
     * @param user active user
     */
    public static void setActiveUser(User user) {
        activeUser = user;
    }

    /**
     * Method to check if a login is valid and continue to main menu if so
     * @param event event
     */
    public void submitLogin(ActionEvent event) {
        String username = usernameField.getText().toLowerCase();
        String password = passwordField.getText();
        for (User user : Database.getUsers()) {
            if (user.getUsername().equals(username)) {
                setActiveUser(user);
                break;
            }
        }
        if (username.length() == 0 || password.length() == 0 || username.contains(" ") || password.contains(" ")) {
            if (Locale.getDefault().getLanguage().equals("fr")) {
                Utils.errorMessage("Le nom d'utilisateur et le mot de passe ne doivent pas être vides ou contenir des espaces");
            } else {
                Utils.errorMessage("Username and password must not be empty or contain spaces");
            }
        } else if (getActiveUser() != null && getActiveUser().getPassword().equals(password)) {
            reportLoginAttempt(username, true);
            Utils.loadScreen(event, "Menu.fxml");
        } else {
            reportLoginAttempt(username, false);
            if (Locale.getDefault().getLanguage().equals("fr")) {
                Utils.errorMessage("Authentification invalide");
            } else {
                Utils.errorMessage("Invalid login credentials");
            }
        }
    }

    /**
     * Method to translate the menu text if necessary based on user's region
     * @param username user's username
     * @param successful whether the login attempt was successful
     */
    public void reportLoginAttempt(String username, boolean successful) {
        try {
            FileWriter fw = new FileWriter ("./login_activity.txt",true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(username + " | " + Utils.getDateTime("UTC") + " | " + (successful ? "Successful" : "Unsuccessful") + "\n");
            bw.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Method to translate the menu text if necessary based on user's region
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Locale.getDefault().getLanguage().equals("fr")) {
            titleLabel.setText("Se connecter");
            usernameLabel.setText("Nom d'utilisateur");
            passwordLabel.setText("Mot de passe");
            submitButton.setText("Soumettre");
            locationLabel.setText("Connecté: " + ZoneId.systemDefault().getId());
        } else {
            locationLabel.setText("Connected: " + ZoneId.systemDefault().getId());
        }
    }
}