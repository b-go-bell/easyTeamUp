package com.example.easyteamup.Backend;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

public class Event implements Parcelable {
    private String eventId;
    private String name;
    private String host;
    private String address;
    private String geohash;
    private String description;
    private Double latitude;
    private Double longitude;
    private Timestamp dueDate;
    private Timestamp finalTime;
    private boolean isPublic;
    private boolean rsvped;
    private String invitationStatus;

    public Event() {
        this.eventId = null;
        this.name = null;
        this.host = null;
        this.address = null;
        this.geohash = null;
        this.description = null;
        this.latitude = null;
        this.longitude = null;
        this.dueDate = null;
        this.finalTime = null;
        this.isPublic = false;
        this.rsvped = false;
        this.invitationStatus = null;
    }

    public Event(String eventId, String name, String host, String address,
                 String geohash, String description, Double latitude, Double longitude,
                 Timestamp dueDate, Timestamp finalTime, boolean isPublic, boolean rsvped,
                 String invitationStatus) {
        this.eventId = eventId;
        this.name = name;
        this.host = host;
        this.address = address;
        this.geohash = geohash;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dueDate = dueDate;
        this.finalTime = finalTime;
        this.isPublic = isPublic;
        this.rsvped = rsvped;
        this.invitationStatus = invitationStatus;
    }


    /* Begin Parcelable Implementation */

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(eventId);
        out.writeString(name);
        out.writeString(host);
        out.writeString(address);
        out.writeString(geohash);
        out.writeString(description);
        out.writeDouble(latitude);
        out.writeDouble(longitude);
        out.writeParcelable(dueDate, 0);
        out.writeParcelable(finalTime, 0);
        out.writeByte((byte) (isPublic ? 1 : 0));
        out.writeByte((byte) (rsvped ? 1 : 0));
        out.writeString(invitationStatus);

    }

    private Event(Parcel in) {
        eventId = in.readString();
        name = in.readString();
        host = in.readString();
        address = in.readString();
        geohash = in.readString();
        description = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        dueDate = in.readParcelable(Timestamp.class.getClassLoader());
        finalTime = in.readParcelable(Timestamp.class.getClassLoader());
        isPublic = in.readByte() != 0;
        rsvped = in.readByte() != 0;
        invitationStatus = in.readString();
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
    /* End Parcelable Implementation */


    public void setEventId(String eventId) {
        this.eventId = (this.eventId == null) ? eventId : throw_changeEventIdException();
    }

    public String throw_changeEventIdException() {
        throw new RuntimeException("eventId is immutable. DO NOT CHANGE!");
    }

    @Exclude
    public String getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean getIsRsvped() {
        return rsvped;
    }

    public void setIsRsvped(boolean rsvped) {
        this.rsvped = rsvped;
    }

    public String getInvitationStatus() {
        return invitationStatus;
    }

    public void setInvitationStatus(String invitationStatus) {
        this.invitationStatus = invitationStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId='" + eventId + '\'' +
                ", name='" + name + '\'' +
                ", host='" + host + '\'' +
                ", address='" + address + '\'' +
                ", geohash='" + geohash + '\'' +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", dueDate=" + dueDate +
                ", finalTime=" + finalTime +
                ", isPublic=" + isPublic +
                ", rsvped=" + rsvped +
                ", invitationStatus='" + invitationStatus + '\'' +
                '}';
    }
}
