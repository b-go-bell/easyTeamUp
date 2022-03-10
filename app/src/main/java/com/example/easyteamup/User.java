package com.example.easyteamup;

import java.util.List;
import java.util.Map;

public class User {
    private String uid;
    private String email;
    private long phone;
    private String firstName;
    private String lastName;
    private int graduationYear;
    private String major;
    private String bio;
    private String photoUrl;
    private List<String> RSVPedEvents;
    private List<String> hostedEvents;
    private Map<String, String> invitedEvents;
    private Map<String, Object> settings;

    public User(String uid, String email, int phone, String firstName,
                String lastName, int graduationYear, String major, String bio,
                String photoUrl, List<String> RSVPedEvents, List<String> hostedEvents,
                Map<String, String> invitedEvents, Map<String, Object> settings) {
        this.uid = uid;
        this.email = email;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.graduationYear = graduationYear;
        this.major = major;
        this.bio = bio;
        this.photoUrl = photoUrl;
        this.RSVPedEvents = RSVPedEvents;
        this.hostedEvents = hostedEvents;
        this.invitedEvents = invitedEvents;
        this.settings = settings;
    }

    public User() {
        this.uid = null;
        this.email = null;
        this.phone = 0;
        this.firstName = null;
        this.lastName = null;
        this.graduationYear = 0;
        this.major = null;
        this.bio = null;
        this.photoUrl = null;
        this.RSVPedEvents = null;
        this.hostedEvents = null;
        this.invitedEvents = null;
        this.settings = null;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = (this.uid == null) ? uid : throw_changeUidException();
    }

    public String throw_changeUidException() {
        throw new RuntimeException("UID is immutable. DO NOT CHANGE!");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<String> getRSVPedEvents() {
        return RSVPedEvents;
    }

    public void setRSVPedEvents(List<String> RSVPedEvents) {
        this.RSVPedEvents = RSVPedEvents;
    }

    public List<String> getHostedEvents() {
        return hostedEvents;
    }

    public void setHostedEvents(List<String> hostedEvents) {
        this.hostedEvents = hostedEvents;
    }

    public Map<String, String> getInvitedEvents() {
        return invitedEvents;
    }

    public void setInvitedEvents(Map<String, String> invitedEvents) {
        this.invitedEvents = invitedEvents;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }

    /*
    For debugging purposes - will probably want to delete
     */
    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", phone=" + phone +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", graduationYear=" + graduationYear +
                ", major='" + major + '\'' +
                ", bio='" + bio + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", RSVPedEvents=" + RSVPedEvents +
                ", hostedEvents=" + hostedEvents +
                ", invitedEvents=" + invitedEvents +
                ", settings=" + settings +
                '}';
    }
}
