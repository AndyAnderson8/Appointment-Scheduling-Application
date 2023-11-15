package andyanderson.appointments.models;

/**
 * Class to create Contact instances and to read and write from them, as well as serves as an abstraction for database records
 * @author Andy Anderson
 */
public class Contact {
    private int contactID;
    private String name;
    private String email;

    /**
     * Constructor method for a Contact
     * @param contactID contact's id number
     * @param name contact's name
     * @param email contact's email
     */
    public Contact(int contactID, String name, String email) {
        this.contactID = contactID;
        this.name = name;
        this.email = email;
    }

    /**
     * Method to get a contact's id
     * @return contact id
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Method to get a contact's name
     * @return contact name
     */
    public String getName() {
        return name;
    }

    /**
     * Method to set a contact's name
     * @param name contact's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method to get a contact's email
     * @return contact's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Method to set a contact's email
     * @param email contact's email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}