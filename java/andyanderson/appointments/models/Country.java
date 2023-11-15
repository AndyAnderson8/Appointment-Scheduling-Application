package andyanderson.appointments.models;

/**
 * Class to create Country instances and to read and write from them, as well as serves as an abstraction for database records
 * @author Andy Anderson
 */
public class Country {
    private int countryID;
    private String name;

    /**
     * Constructor method for a Country
     * @param countryID country's id number
     * @param name country's name
     */
    public Country(int countryID, String name) {
        this.countryID = countryID;
        this.name = name;
    }

    /**
     * Method to get a country's ID
     * @return country's ID
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * Method to get a country's name
     * @return country's name
     */
    public String getName() {
        return name;
    }

    /**
     * Method to set a country's name
     * @param name country's name
     */
    public void setName(String name) {
        this.name = name;
    }
}