//TODO: Use the models for the search query with API
package app.sandoval.com.flightpuntos.models;


public class Airline {

    private int airlineID;
    private String airlineName;

    public Airline(String airlineName) {
        this.airlineName = airlineName;
    }

    public int getAirlineID() {
        return airlineID;
    }

    public void setAirlineID(int airlineID) {
        this.airlineID = airlineID;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }
}
