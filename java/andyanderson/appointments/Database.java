package andyanderson.appointments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import andyanderson.appointments.controllers.Login;
import andyanderson.appointments.models.*;

/**
 * Class to interface with the JDBC to create different object instances representing stored records in the database
 * @author Andy Anderson
 */
public class Database {
    public static Connection connection;

    /**
     * Method to get a user instance from the user's ID
     * @param userID the user's id
     * @return user instance
     */
    public static User getUser(int userID) {
        for (User user : getUsers()) {
            if (user.getUserID() == userID) {
                return user;
            }
        }
        return null;
    }

    /**
     * Method to generate a list with all user instances based on database records
     * @return list of user instances
     */
    public static ObservableList<User> getUsers() {
        try {
            ObservableList<User> users = FXCollections.observableArrayList();
            ResultSet results = jdbcQuery("SELECT * FROM users;");
            while (results.next()) {
                User user = new User(
                    results.getInt("User_ID"),
                    results.getString("User_Name"),
                    results.getString("Password"));
                users.add(user);
            }
            return users;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Method to add a record to the database based on the passed user's attributes
     * @param user user to add to database
     */
    public static void addUser(User user) {
        jdbcUpdate("INSERT INTO users VALUES (" +
            user.getUserID() + ", " +
            "'" + user.getUsername() + "', " +
            "'" + user.getPassword() + "', " +
            "'" + Utils.getDateTime("UTC") + "', " +
            "'" + Login.getActiveUser().getUsername() + "', " +
            "'" + Utils.getDateTime("UTC") + "', " +
            "'" + Login.getActiveUser().getUsername() + ");");
    }

    /**
     * Method to update a record in the database based on the passed user's attributes
     * @param user user to update in database
     */
    public static void updateUser(User user) {
        jdbcUpdate("UPDATE users SET " +
            "User_Name = '" + user.getUsername() + "', " +
            "Password = '" + user.getPassword() + "', " +
            "Last_Update = '" + Utils.getDateTime("UTC") + "', " +
            "Last_Updated_By = '" + Login.getActiveUser().getUsername() + "' " +
            "WHERE User_ID = " + user.getUserID() + ";");
    }

    /**
     * Method to delete a record in the database based on the passed user's attributes
     * @param user user to delete in database
     */
    public static void deleteUser(User user) {
        jdbcUpdate("DELETE FROM users WHERE User_ID = " + user.getUserID() + ";");
    }

    /**
     * Method to get a contact instance from the contact's ID
     * @param contactID the contact's id
     * @return contact instance
     */
    public static Contact getContact(int contactID) {
        for (Contact contact : getContacts()) {
            if (contact.getContactID() == contactID) {
                return contact;
            }
        }
        return null;
    }

    /**
     * Method to generate a list with all contact instances based on database records
     * @return list of contact instances
     */
    public static ObservableList<Contact> getContacts() {
        try {
            ObservableList<Contact> contacts = FXCollections.observableArrayList();
            ResultSet results = jdbcQuery("SELECT * FROM contacts;");
            while (results.next()) {
                Contact contact = new Contact(
                    results.getInt("Contact_ID"),
                    results.getString("Contact_Name"),
                    results.getString("Email"));
                contacts.add(contact);
            }
            return contacts;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Method to add a record to the database based on the passed contact's attributes
     * @param contact contact to add to database
     */
    public static void addContact(Contact contact) {
        jdbcUpdate("INSERT INTO contacts VALUES (" +
            contact.getContactID() + ", " +
            "'" + contact.getName() + "', " +
            "'" + contact.getEmail() + ");");
    }

    /**
     * Method to update a record in the database based on the passed contact's attributes
     * @param contact contact to update in database
     */
    public static void updateContact(Contact contact) {
        jdbcUpdate("UPDATE appointments SET " +
            "Contact_Name = '" + contact.getName() + "', " +
            "Email = '" + contact.getEmail() + "' " +
            "WHERE Contact_ID = " + contact.getContactID() + ";");
    }

    /**
     * Method to delete a record in the database based on the passed contact's attributes
     * @param contact contact to delete in database
     */
    public static void deleteContact(Contact contact) {
        jdbcUpdate("DELETE FROM contacts WHERE Contact_ID = " + contact.getContactID() + ";");
    }

    /**
     * Method to get a division instance from the division's ID
     * @param divisionID the division's id
     * @return division instance
     */
    public static Division getDivision(int divisionID) {
        for (Division division : getDivisions()) {
            if (division.getDivisionID() == divisionID) {
                return division;
            }
        }
        return null;
    }

    /**
     * Method to get a division instance from the division's name
     * @param name the division's name
     * @return division instance
     */
    public static Division getDivision(String name) {
        for (Division division : getDivisions()) {
            if (division.getName().equals(name)) {
                return division;
            }
        }
        return null;
    }

    /**
     * Method to generate a list with all division instances based on database records
     * @return list of division instances
     */
    public static ObservableList<Division> getDivisions() {
        try {
            ObservableList<Division> divisions = FXCollections.observableArrayList();
            ResultSet results = jdbcQuery("SELECT * FROM first_level_divisions;");
            while (results.next()) {
                Division division = new Division(
                    results.getInt("Division_ID"),
                    results.getString("Division"),
                    getCountry(results.getInt("Country_ID")));
                divisions.add(division);
            }
            return divisions;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Method to generate a list with all division instances that are a part of a given country
     * @param country division's country
     * @return list of division instances
     */
    public static ObservableList<Division> getDivisions(Country country) {
        ObservableList<Division> divisions = FXCollections.observableArrayList();
        for (Division division : getDivisions()) {
            if (division.getCountry().getCountryID() == country.getCountryID()) {
                divisions.add(division);
            }
        }
        return divisions;
    }

    /**
     * Method to add a record to the database based on the passed division's attributes
     * @param division division to add to database
     */
    public static void addDivision(Division division) {
        jdbcUpdate("INSERT INTO first_level_divisions VALUES (" +
            division.getDivisionID() + ", " +
            "'" + division.getName() + "', " +
            "'" + Utils.getDateTime("UTC") + "', " +
            "'" + Login.getActiveUser().getUsername() + "', " +
            "'" + Utils.getDateTime("UTC") + "', " +
            "'" + Login.getActiveUser().getUsername() + "', " +
            "'" + division.getCountry().getCountryID() + ");");
    }

    /**
     * Method to update a record in the database based on the passed division's attributes
     * @param division division to update in database
     */
    public static void updateDivision(Division division) {
        jdbcUpdate("UPDATE first_level_divisions SET " +
            "Division = '" + division.getName() + "', " +
            "Last_Update = '" + Utils.getDateTime("UTC") + "', " +
            "Last_Updated_By = '" + Login.getActiveUser().getUsername() + "', " +
            "Country_ID = " + division.getCountry().getCountryID() + " " +
            "WHERE Division_ID = " + division.getDivisionID() + ";");
    }

    /**
     * Method to delete a record in the database based on the passed division's attributes
     * @param division division to delete in database
     */
    public static void deleteDivision(Division division) {
        jdbcUpdate("DELETE FROM first_level_divisions WHERE Division_ID = " + division.getDivisionID() + ";");
    }

    /**
     * Method to get a country instance from the country's ID
     * @param countryID the country's id
     * @return country instance
     */
    public static Country getCountry(int countryID) {
        for (Country country : getCountries()) {
            if (country.getCountryID() == countryID) {
                return country;
            }
        }
        return null;
    }

    /**
     * Method to get a country instance from the country's name
     * @param name the country's name
     * @return country instance
     */
    public static Country getCountry(String name) {
        for (Country country : getCountries()) {
            if (country.getName().equals(name)) {
                return country;
            }
        }
        return null;
    }

    /**
     * Method to generate a list with all country instances based on database records
     * @return list of country instances
     */
    public static ObservableList<Country> getCountries() {
        try {
            ObservableList<Country> countries = FXCollections.observableArrayList();
            ResultSet results = jdbcQuery("SELECT * FROM countries;");
            while (results.next()) {
                Country country = new Country(
                    results.getInt("Country_ID"),
                    results.getString("Country"));
                countries.add(country);
            }
            return countries;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Method to add a record to the database based on the passed country's attributes
     * @param country country to add to database
     */
    public static void addCountry(Country country) {
        jdbcUpdate("INSERT INTO countries VALUES (" +
            country.getCountryID() + ", " +
            "'" + country.getName() + "', " +
            "'" + Utils.getDateTime("UTC") + "', " +
            "'" + Login.getActiveUser().getUsername() + "', " +
            "'" + Utils.getDateTime("UTC") + "', " +
            "'" + Login.getActiveUser().getUsername() + "';");
    }

    /**
     * Method to update a record in the database based on the passed country's attributes
     * @param country country to update in database
     */
    public static void updateCountry(Country country) {
        jdbcUpdate("UPDATE countries SET " +
            "Country = '" + country.getName() + "', " +
            "Last_Update = '" + Utils.getDateTime("UTC") + "', " +
            "Last_Updated_By = '" + Login.getActiveUser().getUsername() + "' " +
            "WHERE Country_ID = " + country.getCountryID() + ";");
    }

    /**
     * Method to delete a record in the database based on the passed country's attributes
     * @param country country to delete in database
     */
    public static void deleteCountry(Country country) {
        jdbcUpdate("DELETE FROM countries WHERE Country_ID = " + country.getCountryID() + ";");
    }

    /**
     * Method to get a customer instance from the customer's ID
     * @param customerID the customer's id
     * @return customer instance
     */
    public static Customer getCustomer(int customerID) {
        for (Customer customer : getCustomers()) {
            if (customer.getCustomerID() == customerID) {
                return customer;
            }
        }
        return null;
    }

    /**
     * Method to generate a list with all customer instances based on database records
     * @return list of customer instances
     */
    public static ObservableList<Customer> getCustomers() {
        try {
            ObservableList<Customer> customers = FXCollections.observableArrayList();
            ResultSet results = jdbcQuery("SELECT * FROM customers;");
            while (results.next()) {
                Customer customer = new Customer(
                    results.getInt("Customer_ID"),
                    results.getString("Customer_Name"),
                    results.getString("Address"),
                    results.getString("Postal_Code"),
                    results.getString("Phone"),
                    getDivision(results.getInt("Division_ID")));
                customers.add(customer);
            }
            return customers;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Method to add a record to the database based on the passed customer's attributes
     * @param customer customer to add to database
     */
    public static void addCustomer(Customer customer) {
        jdbcUpdate("INSERT INTO customers VALUES (" +
            customer.getCustomerID() + ", " +
            "'" + customer.getName() + "', " +
            "'" + customer.getAddress() + "', " +
            "'" + customer.getPostalCode() + "', " +
            "'" + customer.getPhone() + "', " +
            "'" + Utils.getDateTime("UTC") + "', " +
            "'" + Login.getActiveUser().getUsername() + "', " +
            "'" + Utils.getDateTime("UTC") + "', " +
            "'" + Login.getActiveUser().getUsername() + "', " +
            customer.getDivision().getDivisionID() + ");");
    }

    /**
     * Method to update a record in the database based on the passed customer's attributes
     * @param customer customer to update in database
     */
    public static void updateCustomer(Customer customer) {
        jdbcUpdate("UPDATE customers SET " +
            "Customer_Name = '" + customer.getName() + "', " +
            "Address = '" + customer.getAddress() + "', " +
            "Postal_Code = '" + customer.getPostalCode() + "', " +
            "Phone = '" + customer.getPhone() + "', " +
            "Last_Update = '" + Utils.getDateTime("UTC") + "', " +
            "Last_Updated_By = '" + Login.getActiveUser().getUsername() + "', " +
            "Division_ID = " + customer.getDivision().getDivisionID() + " " +
            "WHERE Customer_ID = " + customer.getCustomerID() + ";");
    }

    /**
     * Method to delete a record in the database based on the passed customer's attributes
     * @param customer customer to delete in database
     */
    public static void deleteCustomer(Customer customer) {
        jdbcUpdate("DELETE FROM customers WHERE Customer_ID = " + customer.getCustomerID() + ";");
    }

    /**
     * Method to get an appointment instance from the appointment's ID
     * @param appointmentID the appointment's id
     * @return appointment instance
     */
    public static Appointment getAppointment(int appointmentID) {
        for (Appointment appointment : getAppointments()) {
            if (appointment.getAppointmentID() == appointmentID) {
                return appointment;
            }
        }
        return null;
    }

    /**
     * Method to generate a list with all appointment instances based on database records
     * @return list of appointment instances
     */
    public static ObservableList<Appointment> getAppointments() {
        try {
            ObservableList<Appointment> appointments = FXCollections.observableArrayList();
            ResultSet results = jdbcQuery("SELECT * FROM appointments;");
            while (results.next()) {
                Appointment appointment = new Appointment(
                    results.getInt("Appointment_ID"),
                    results.getString("Title"),
                    results.getString("Description"),
                    results.getString("Location"),
                    results.getString("Type"),
                    results.getString("Start"),
                    results.getString("End"),
                    getContact(results.getInt("Contact_ID")),
                    getCustomer(results.getInt("Customer_ID")),
                    getUser(results.getInt("User_ID")));
                appointments.add(appointment);
            }
            return appointments;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Method to generate a list with all appointment instances that have the given customer
     * @param customer appointment's customer
     * @return list of appointment instances
     */
    public static ObservableList<Appointment> getAppointments(Customer customer) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        for (Appointment appointment : getAppointments()) {
            if (appointment.getCustomer().getCustomerID() == customer.getCustomerID()) {
                appointments.add(appointment);
            }
        }
        return appointments;
    }

    /**
     * Method to generate a list with all appointment instances that have the given contact
     * @param contact appointment's contact
     * @return list of appointment instances
     */
    public static ObservableList<Appointment> getAppointments(Contact contact) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        for (Appointment appointment : getAppointments()) {
            if (appointment.getContact().getContactID() == contact.getContactID()) {
                appointments.add(appointment);
            }
        }
        return appointments;
    }

    /**
     * Method to generate a list with all appointment instances that have the given user
     * @param user appointment's user
     * @return list of appointment instances
     */
    public static ObservableList<Appointment> getAppointments(User user) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        for (Appointment appointment : getAppointments()) {
            if (appointment.getUser().getUserID() == user.getUserID()) {
                appointments.add(appointment);
            }
        }
        return appointments;
    }

    /**
     * Method to add a record to the database based on the passed appointment's attributes
     * @param appointment appointment to add to database
     */
    public static void addAppointment(Appointment appointment) {
        jdbcUpdate("INSERT INTO appointments VALUES (" +
            appointment.getAppointmentID() + ", " +
            "'" + appointment.getTitle() + "', " +
            "'" + appointment.getDescription() + "', " +
            "'" + appointment.getLocation() + "', " +
            "'" + appointment.getType() + "', " +
            "'" + appointment.getStartDateTime() + "', " +
            "'" + appointment.getEndDateTime() + "', " +
            "'" + Utils.getDateTime("UTC") + "', " +
            "'" + Login.getActiveUser().getUsername() + "', " +
            "'" + Utils.getDateTime("UTC") + "', " +
            "'" + Login.getActiveUser().getUsername() + "', " +
            appointment.getCustomer().getCustomerID() + ", " +
            appointment.getUser().getUserID() + ", " +
            appointment.getContact().getContactID() + ");");
    }

    /**
     * Method to update a record in the database based on the passed appointment's attributes
     * @param appointment appointment to update in database
     */
    public static void updateAppointment(Appointment appointment) {
        jdbcUpdate("UPDATE appointments SET " +
            "Title = '" + appointment.getTitle() + "', " +
            "Description = '" + appointment.getDescription() + "', " +
            "Location = '" + appointment.getLocation() + "', " +
            "Type = '" + appointment.getType() + "', " +
            "Start = '" + appointment.getStartDateTime() + "', " +
            "End = '" + appointment.getEndDateTime() + "', " +
            "Last_Update = '" + Utils.getDateTime("UTC") + "', " +
            "Last_Updated_By = '" + Login.getActiveUser().getUsername() + "', " +
            "Customer_ID = " + appointment.getCustomer().getCustomerID() + ", " +
            "User_ID = " + appointment.getUser().getUserID() + ", " +
            "Contact_ID = " + appointment.getContact().getContactID() + " " +
            "WHERE Appointment_ID = " + appointment.getAppointmentID() + ";");
    }

    /**
     * Method to delete a record in the database based on the passed appointment's attributes
     * @param appointment appointment to delete in database
     */
    public static void deleteAppointment(Appointment appointment) {
        jdbcUpdate("DELETE FROM appointments WHERE Appointment_ID = " + appointment.getAppointmentID() + ";");
    }

    /**
     * Method to open a JDBC connection
     */
    public static void openConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/client_schedule?connectionTimeZone=SERVER", "sqlUser", "passw0rd!");
            System.out.println("JDBC Running");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Method to close a JDBC connection
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("JDBC Closed");
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Method to run a SQL query statement in the database
     * @param statement SQL statement
     * @return the results of the query
     */
    public static ResultSet jdbcQuery(String statement) {
        try {
            return connection.createStatement().executeQuery(statement);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Method to run a SQL update statement in the database
     * @param statement SQL statement
     */
    public static void jdbcUpdate(String statement) {
        try {
            connection.createStatement().executeUpdate(statement);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}