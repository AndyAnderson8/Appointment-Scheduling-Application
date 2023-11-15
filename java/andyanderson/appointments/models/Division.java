package andyanderson.appointments.models;

/**
 * Class to create Division instances and to read and write from them, as well as serves as an abstraction for database records
 * @author Andy Anderson
 */
public class Division {
    private int divisionID;
    private String name;
    private Country country;

    /**
     * Constructor method for a Division
     * @param divisionID division's id number
     * @param name division's name
     * @param country the country the division is a part of
     */
    public Division(int divisionID, String name, Country country) {
        this.divisionID = divisionID;
        this.name = name;
        this.country = country;
    }

    /**
     * Method to get a division's ID
     * @return division's ID
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * Method to get a division's name
     * @return division's name
     */
    public String getName() {
        return name;
    }

    /**
     * Method to set a division's name
     * @param name division's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method to get a division's country
     * @return division's country
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Method to get a division's country
     * @param country division's country
     */
    public void setCountry(Country country) {
        this.country = country;
    }
}