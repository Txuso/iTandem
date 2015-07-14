package Classes;

import java.util.HashMap;

/**
 * Created by Txuso on 27/5/15.
 * Tandem Place class
 */
public class TandemPlaceClass {

    String title;
    String creatorId;
    String description;
    String when;
    Double latitude;
    Double longitude;
    HashMap<String, Boolean> participants;

    public TandemPlaceClass(String creatorId, String title, String description, String when, Double latitude, Double longitude, HashMap<String, Boolean> participants){

        this.creatorId = creatorId;
        this.title = title;
        this.description = description;
        this.when = when;
        this.latitude = latitude;
        this.longitude = longitude;
        this.participants = participants;
    }

    public TandemPlaceClass(){
        this.creatorId = "";
        this.title = "";
        this.description = "";
        this.when = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.participants = new HashMap<String, Boolean>();
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public HashMap<String, Boolean> getTandemUsers() {
        return participants;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getWhen() {
        return when;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setTandemUsers(HashMap<String, Boolean> participants) {
        this.participants = participants;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    
}
