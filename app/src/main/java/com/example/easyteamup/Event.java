package com.example.easyteamup;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.Timestamp;

public class Event {
    private String eventId;
    private String host;
    private String geohash;
    private Double latitude;
    private Double longitude;

    private Timestamp dueDate;
    private Timestamp finalTime;
    private boolean isPublic;

    public Event() {
        this.eventId = null;
        this.host = null;
        this.geohash = null;
        this.latitude = null;
        this.longitude = null;
        this.dueDate = null;
        this.finalTime = null;
        this.isPublic = false;
    }

    public Event(String eventId, String host, String geohash, Double latitude, Double longitude, Timestamp dueDate, Timestamp finalTime, boolean isPublic) {
        this.eventId = eventId;
        this.host = host;
        this.geohash = geohash;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dueDate = dueDate;
        this.finalTime = finalTime;
        this.isPublic = isPublic;
    }

    public void setEventId(String eventId) {
        this.eventId = (this.eventId == null) ? eventId : throw_changeEventIdException();
    }

    public String throw_changeEventIdException() {
        throw new RuntimeException("eventId is immutable. DO NOT CHANGE!");
    }

    public String getEventId() {
        return eventId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public void generateGeohash() {
        geohash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(latitude, longitude));
    }

    public void generateGeohash(Double latitude, Double longitude) {
        geohash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(latitude, longitude));
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public Timestamp getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(Timestamp finalTime) {
        this.finalTime = finalTime;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    /*
    For debugging - will probably want to delete
     */
    @Override
    public String toString() {
        return "Event{" +
                "eventId='" + eventId + '\'' +
                ", host='" + host + '\'' +
                ", geohash=" + geohash +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", dueDate=" + dueDate +
                ", finalTime=" + finalTime +
                ", isPublic=" + isPublic +
                '}';
    }
}
