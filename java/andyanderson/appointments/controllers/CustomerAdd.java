package andyanderson.appointments.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

import andyanderson.appointments.Database;
import andyanderson.appointments.Utils;
import andyanderson.appointments.models.Country;
import andyanderson.appointments.models.Customer;
import andyanderson.appointments.models.Division;

/**
 * Controller class to manage the add customer form
 * @author Andy Anderson
 */
public class CustomerAdd implements Initializable {
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField streetAddressField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private ComboBox divisionBox;
    @FXML
    private ComboBox countryBox;

    /**
     * Method to add a new customer record based on form input
     * @param event event
     */
    public void addCustomer(ActionEvent event) {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String streetAddress = streetAddressField.getText();
        String city = cityField.getText();
        String postalCode = postalCodeField.getText();
        Division division = Database.getDivision(divisionBox.getValue().toString());

        if (name.equals("") || phone.equals("") || streetAddress.equals("") || city.equals("") || postalCode.equals("") || division == null) {
            Utils.errorMessage("Please ensure are fields are filled before submitting form");
        } else if (phone.length() != 12 || phone.replaceAll("[^0-9]", "").length() != 10 || phone.charAt(3) != '-' || phone.charAt(7) != '-') {
            Utils.errorMessage("Please format phone number in correct format (e.g. 555-123-4567)");
        } else {
            Customer customer = new Customer(Customer.getNextCustomerID(), name, streetAddress + ", " + city, postalCode, phone, division);
            Database.addCustomer(customer);
            Utils.loadScreen(event, "CustomerMain.fxml");
        }
    }

    /**
     * Method to load the main customer view
     * @param event event
     */
    public void loadCustomerMain(ActionEvent event) {
        Utils.loadScreen(event, "CustomerMain.fxml");
    }

    /**
     * Method to populate the selection box with divisions for the country selected
     * @param event event
     */
    public void reloadDivisionOptions(ActionEvent event) {
        Country country = Database.getCountry(countryBox.getValue().toString());
        ObservableList<Division> divisions = Database.getDivisions(country);
        ObservableList<String> divisionNames = FXCollections.observableArrayList();
        for (Division division : divisions) {
            divisionNames.add(division.getName());
        }
        divisionBox.setItems(divisionNames);
        divisionBox.setDisable(false);
    }

    /**
     * Method to populate the form's selection boxes with valid options
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Country> countries = Database.getCountries();
        ObservableList<String> countryNames = FXCollections.observableArrayList();
        for (Country country : countries) {
            countryNames.add(country.getName());
        }
        countryBox.setItems(countryNames);
    }
}