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
import andyanderson.appointments.models.Division;

/**
 * Controller class to manage the modify customer form
 * @author Andy Anderson
 */
public class CustomerModify implements Initializable {
    @FXML
    private TextField idField;
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
     * Method to modify an existing customer record based on form input
     * @param event event
     */
    public void modifyCustomer(ActionEvent event) {
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
            CustomerMain.getSelectedCustomer().setName(name);
            CustomerMain.getSelectedCustomer().setPhone(phone);
            CustomerMain.getSelectedCustomer().setAddress(streetAddress + ", " + city);
            CustomerMain.getSelectedCustomer().setPostalCode(postalCode);
            CustomerMain.getSelectedCustomer().setDivision(division);
            Database.updateCustomer(CustomerMain.getSelectedCustomer());
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
     * Method to populate the form's selection boxes with valid options, and autofill the forms with the customer's information
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Division customerDivision = Database.getDivision(CustomerMain.getSelectedCustomer().getDivision().getDivisionID());
        idField.setText(String.valueOf(CustomerMain.getSelectedCustomer().getCustomerID()));
        nameField.setText(CustomerMain.getSelectedCustomer().getName());
        phoneField.setText(CustomerMain.getSelectedCustomer().getPhone());
        postalCodeField.setText(CustomerMain.getSelectedCustomer().getPostalCode());
        String[] address = CustomerMain.getSelectedCustomer().getAddress().split(", ");
        switch (address.length) { // tries to format correctly the address break when some people have apt., township, etc.
            case 1:
                streetAddressField.setText(address[0]);
                break;
            case 2:
                streetAddressField.setText(address[0]);
                cityField.setText(address[1]);
                break;
            case 3:
                if (customerDivision.getCountry().equals("UK")) { // UK townships would be split in city, not apt. first
                    streetAddressField.setText(address[0]);
                    cityField.setText(address[1] + ", " + address[2]);
                } else {
                    streetAddressField.setText(address[0] + ", " + address[1]);
                    cityField.setText(address[2]);
                }
                break;
            default: // Maybe executed if UK and apt. or longer?
                String streetAddressText = "";
                for (int i = 2; i < address.length; i++) {
                    streetAddressText += address[i - 2];
                }
                streetAddressField.setText(streetAddressText);
                cityField.setText(address[address.length - 2] + ", " + address[address.length - 1]);
        }

        ObservableList<Division> divisions = Database.getDivisions(customerDivision.getCountry());
        ObservableList<String> divisionNames = FXCollections.observableArrayList();
        for (Division division : divisions) {
            divisionNames.add(division.getName());
        }
        divisionBox.setItems(divisionNames);
        divisionBox.getSelectionModel().select(customerDivision.getName());

        ObservableList<Country> countries = Database.getCountries();
        ObservableList<String> countryNames = FXCollections.observableArrayList();
        for (Country country : countries) {
            countryNames.add(country.getName());
        }
        countryBox.setItems(countryNames);
        countryBox.getSelectionModel().select(customerDivision.getCountry().getName());
    }
}