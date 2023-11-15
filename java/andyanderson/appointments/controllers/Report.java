package andyanderson.appointments.controllers;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import andyanderson.appointments.Database;
import andyanderson.appointments.Utils;
import andyanderson.appointments.models.Appointment;
import andyanderson.appointments.models.Contact;
import andyanderson.appointments.models.Customer;

/**
 * Controller class to generate and display reports based on database records
 * @author Andy Anderson
 */
public class Report implements Initializable {
    @FXML
    private TableView<Map.Entry<String, Integer>> typeTable;
    @FXML
    private TableColumn<Map.Entry<String, Integer>, String> apptTypeColumn;
    @FXML
    private TableColumn<Map.Entry<String, Integer>, Integer> typeCountColumn;
    @FXML
    private TableView<Map.Entry<String, Integer>> monthTable;
    @FXML
    private TableColumn<Map.Entry<String, Integer>, String> monthColumn;
    @FXML
    private TableColumn<Map.Entry<String, Integer>, Integer> monthCountColumn;
    @FXML
    private TableView<Appointment> scheduleTable;
    @FXML
    private TableColumn<Appointment, Integer> idColumn;
    @FXML
    private TableColumn<Appointment, String> titleColumn;
    @FXML
    private TableColumn<Appointment, String> descriptionColumn;
    @FXML
    private TableColumn<Appointment, String> typeColumn;
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
    private ComboBox contactBox;
    @FXML
    private TableView<Map.Entry<String, Integer>> countryTable;
    @FXML
    private TableColumn<Map.Entry<String, Integer>, String> countryColumn;
    @FXML
    private TableColumn<Map.Entry<String, Integer>, Integer> countryCountColumn;

    /**
     * Method to load the menu view
     * @param event event
     */
    public void loadMenu(ActionEvent event) {
        Utils.loadScreen(event, "Menu.fxml");
    }

    /**
     * Method to generate schedules for a given contact selected in a dropdown box
     * LAMBDA: Uses lambda expressions to populate start and end dates and times. None of this data is possible to
     * display just by getting a single property from the object since they are not directly stored as attributes.
     */
    public void generateSchedule() {
        Contact contact = Database.getContact(Integer.parseInt(contactBox.getValue().toString().split("ID:")[1].replaceAll("[^0-9]", "")));
        scheduleTable.setItems(Database.getAppointments(contact));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
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
        customerColumn.setCellValueFactory(cellData -> { // Lambda
            return new ReadOnlyStringWrapper(cellData.getValue().getCustomer().getName() + " (ID: " + cellData.getValue().getCustomer().getCustomerID() + ")");
        });
    }

    /**
     * Method to populate each of the tables with their respective data
     * LAMBDA: Uses lambda expressions to populate the tables with month, type, and country counts since that data
     * is counted through a hashmap, which then needs to be iterated to for each dataset.
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> contacts = FXCollections.observableArrayList();
        for (Contact contact : Database.getContacts()) {
            contacts.add(contact.getName() + " (ID: " + contact.getContactID() + ")");
        }
        contactBox.setItems(contacts);

        Map<String, Integer> months = new LinkedHashMap<String, Integer>();
        months.put("January", 0);
        months.put("February", 0);
        months.put("March", 0);
        months.put("April", 0);
        months.put("May", 0);
        months.put("June", 0);
        months.put("July", 0);
        months.put("August", 0);
        months.put("September", 0);
        months.put("October", 0);
        months.put("November", 0);
        months.put("December", 0);
        for (Appointment appointment : Database.getAppointments()) {
            String month = Utils.formatDateTime(Utils.convertDateTime(appointment.getStartDateTime(), "UTC", "local"), "yyyy-MM-dd HH:mm:ss", "MMMM");
            months.put(month, months.getOrDefault(month, 0) + 1);
        }
        monthColumn.setCellValueFactory(entry -> new SimpleStringProperty(entry.getValue().getKey())); // Lambda
        monthCountColumn.setCellValueFactory(entry -> new SimpleIntegerProperty(entry.getValue().getValue()).asObject()); // Lambda
        monthTable.getColumns().setAll(monthColumn, monthCountColumn);
        monthTable.getItems().addAll(months.entrySet());

        Map<String, Integer> types = new LinkedHashMap<String, Integer>();
        for (String type : Appointment.getTypes()) {
            types.put(type, 0);
        }
        for (Appointment appointment : Database.getAppointments()) {
            String type = appointment.getType();
            types.put(type, months.getOrDefault(type, 0) + 1);
        }
        apptTypeColumn.setCellValueFactory(entry -> new SimpleStringProperty(entry.getValue().getKey())); // Lambda
        typeCountColumn.setCellValueFactory(entry -> new SimpleIntegerProperty(entry.getValue().getValue()).asObject()); // Lambda
        typeTable.getColumns().setAll(apptTypeColumn, typeCountColumn);
        typeTable.getItems().addAll(types.entrySet());

        Map<String, Integer> countries = new LinkedHashMap<String, Integer>();
        for (Customer customer : Database.getCustomers()) {
            String country = customer.getDivision().getCountry().getName();
            countries.put(country, countries.getOrDefault(country, 0) + 1);
        }
        countryColumn.setCellValueFactory(entry -> new SimpleStringProperty(entry.getValue().getKey())); // Lambda
        countryCountColumn.setCellValueFactory(entry -> new SimpleIntegerProperty(entry.getValue().getValue()).asObject()); // Lambda
        countryTable.getColumns().setAll(countryColumn, countryCountColumn);
        countryTable.getItems().addAll(countries.entrySet());
    }
}