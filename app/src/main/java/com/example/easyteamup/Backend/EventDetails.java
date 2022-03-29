package com.example.easyteamup.Backend;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.Timestamp;

public class EventDetails {
    private String name;
    private String address;
    private String geohash;
    private String description;
    private String category;
    private Double latitude;
    private Double longitude;
    private Double eventLength;
    private Timestamp dueDate;

    public EventDetails() {
        this.name = null;
        this.address = null;
        this.description = null;
        this.category = null;
        this.geohash = null;
        this.latitude = null;
        this.longitude = null;
        this.eventLength = null;
        this.dueDate = null;
    }

    public EventDetails(Event event) {
        this.name = event.getName();
        this.address = event.getAddress();
        this.latitude = event.getLatitude();
        this.longitude = event.getLongitude();
        this.category = event.getCategory();
        this.eventLength = event.getEventLength();
        this.description = event.getDescription();
        this.dueDate = event.getDueDate();
        generateGeohash();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        if (this.latitude != null && this.longitude != null) {
            generateGeohash();
        }
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
        if (this.latitude != null && this.longitude != null) {
            generateGeohash();
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public Double getEventLength() {
        return eventLength;
    }

    public void setEventLength(Double eventLength) {
        this.eventLength = eventLength;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
