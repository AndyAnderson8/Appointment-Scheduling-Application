package andyanderson.appointments.controllers;

import javafx.beans.property.ReadOnlyStringWrapper;
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
import andyanderson.appointments.models.Customer;

/**
 * Controller class to manage the main view of customer records
 * @author Andy Anderson
 */
public class CustomerMain implements Initializable {
    private static Customer selectedCustomer;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> idColumn;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TableColumn<Customer, String> addressColumn;
    @FXML
    private TableColumn<Customer, String> divisionColumn;
    @FXML
    private TableColumn<Customer, String> postalCodeColumn;
    @FXML
    private TableColumn<Customer, String> countryColumn;

    /**
     * Method to get the selected customer from the customer table
     * @return selected customer
     */
    public static Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    /**
     * Method to set the selected customer from the customer table
     * @param customer selected customer
     */
    public static void setSelectedCustomer(Customer customer) {
        selectedCustomer = customer;
    }

    /**
     * Method to load the add customer view
     * @param event event
     */
    public void addCustomer(ActionEvent event) {
        Utils.loadScreen(event, "CustomerAdd.fxml");
    }

    /**
     * Method to load the modify customer view
     * @param event event
     */
    public void modifyCustomer(ActionEvent event) {
        setSelectedCustomer(customerTable.getSelectionModel().getSelectedItem());
        if (getSelectedCustomer() == null) { // no item selected
            Utils.errorMessage("No customer selected");
        } else {
            Utils.loadScreen(event, "CustomerModify.fxml");
        }
    }

    /**
     * Method to delete the selected customer from the database after getting confirmation from the user
     */
    public void deleteCustomer() {
        setSelectedCustomer(customerTable.getSelectionModel().getSelectedItem());
        if (getSelectedCustomer() == null) { // no item selected
            Utils.errorMessage("No customer selected");
        } else if (Database.getAppointments(getSelectedCustomer()).size() > 0) {
            Utils.errorMessage("This customer still has appointments scheduled. You must delete any customer appointments before deleting the customer.");
        } else if (Utils.confirmationMessage("Are you sure you want to delete this record?")) {
            String customerInfo = "(ID: " + getSelectedCustomer().getCustomerID() + ", Name: " + getSelectedCustomer().getName() + ")";
            Database.deleteCustomer(getSelectedCustomer());
            customerTable.setItems(Database.getCustomers());
            Utils.informationMessage("Record was successfully deleted\n" + customerInfo);
        }
    }

    /**
     * Method to load the main appointment view
     * @param event event
     */
    public void loadAppointments(ActionEvent event) {
        Utils.loadScreen(event, "AppointmentMain.fxml");
    }

    /**
     * Method to load the menu view
     * @param event event
     */
    public void loadMenu(ActionEvent event) {
        Utils.loadScreen(event, "Menu.fxml");
    }

    /**
     * Method to populate the customer table with customer records
     * LAMBDA: Uses lambda expressions to populate the division name and country name, which are not directly stored
     * by the customer object as an attribute.
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerTable.setItems(Database.getCustomers());
        idColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        divisionColumn.setCellValueFactory(cellData -> { // Lambda
            return new ReadOnlyStringWrapper(cellData.getValue().getDivision().getName());
        });
        countryColumn.setCellValueFactory(cellData -> { // Lambda
            return new ReadOnlyStringWrapper(cellData.getValue().getDivision().getCountry().getName());
        });
    }
}