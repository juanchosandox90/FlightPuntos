//TODO: Use the models for the search query with API
package app.sandoval.com.flightpuntos.models;

import java.util.Date;


public class Itinerary {

    private int itineraryID;
    private Date timeStamp;

    public int getItineraryID() {
        return itineraryID;
    }

    public void setItineraryID(int itineraryID) {
        this.itineraryID = itineraryID;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
