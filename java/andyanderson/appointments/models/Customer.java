package andyanderson.appointments.models;

/**
 * Class to create Customer instances and to read and write from them, as well as serves as an abstraction for database records
 * @author Andy Anderson
 */
public class Customer {
    private static int idCounter = 0;
    private int customerID;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private Division division;

    /**
     * Constructor method for a Customer
     * @param customerID customer's id number
     * @param name customer's name
     * @param address customer's address (includes street address and city)
     * @param postalCode customer's postal code
     * @param phone customer's phone number
     * @param division customer's residing division
     */
    public Customer(int customerID, String name, String address, String postalCode, String phone, Division division) {
        this.customerID = customerID;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.division = division;
        idCounter = Math.max(idCounter, customerID);
    }

    /**
     * Method to get the next available ID to create a new Customer instance
     * @return next available customer ID
     */
    public static int getNextCustomerID() {
        return idCounter + 1;
    }

    /**
     * Method to get a customer's ID
     * @return customer's ID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Method to get a customer's name
     * @return customer's name
     */
    public String getName() {
        return name;
    }

    /**
     * Method to set a customer's name
     * @param name customer's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method to get a customer's address
     * @return customer's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Method to set a customer's address
     * @param address customer's address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Method to get a customer's postal code
     * @return customer's postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Method to set a customer's postal code
     * @param postalCode customer's postal code
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Method to get a customer's phone
     * @return customer's phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Method to set a customer's phone number
     * @param phone customer's phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Method to get a customer's division
     * @return customer's division
     */
    public Division getDivision() {
        return division;
    }

    /**
     * Method to set a customer's division
     * @param division customer's division
     */
    public void setDivision(Division division) {
        this.division = division;
    }
}