package andyanderson.appointments.models;

/**
 * Class to create User instances and to read and write from them, as well as serves as an abstraction for database records
 * @author Andy Anderson
 */
public class User {
    private int userID;
    private String username;
    private String password;

    /**
     * Constructor method for a User
     * @param userID user's id number
     * @param username user's username
     * @param password user's password
     */
    public User(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    /**
     * Method to get a user's ID
     * @return user's id
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Method to get a user's username
     * @return user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Method to set a user's ID
     * @param username user's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Method to get a user's password
     * @return user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Method to set a user's password
     * @param password user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}