package com.example.easyteamup.Backend;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserTest {

    String uid = "TESTUID";
    String email = "TEST@TEST.COM";
    long phone = 123;
    String firstName = "TESTFIRST";
    String lastName = "TESTLAST";
    int graduationYear = 2022;
    String major = "TESTMAJOR";
    String bio = "TESTBIO";
    String photoUrl = "www.testurl.com";
    ArrayList<String> rsvplist = new ArrayList<>();
    List<String> RSVPedEvents = rsvplist;
    ArrayList<String> hostedlist = new ArrayList<>();
    List<String> hostedEvents = hostedlist;
    HashMap<String, String> invitedmap = new HashMap<>();
    Map<String, String> invitedEvents = invitedmap;
    HashMap<String, Object> settingsmap = new HashMap<>();
    Map<String, Object> settings = settingsmap;
    User testUser = new User(uid, email, phone, firstName, lastName, graduationYear, major, bio,
            photoUrl,RSVPedEvents, hostedEvents, invitedEvents, settings);

    @Test
    public void tostringTest(){
        String uid = "TESTUID";
        String email = "TEST@TEST.COM";
        long phone = 123;
        String firstName = "TESTFIRST";
        String lastName = "TESTLAST";
        int graduationYear = 2022;
        String major = "TESTMAJOR";
        String bio = "TESTBIO";
        String photoUrl = "www.testurl.com";
        ArrayList<String> rsvplist = new ArrayList<>();
        List<String> RSVPedEvents = rsvplist;
        ArrayList<String> hostedlist = new ArrayList<>();
        List<String> hostedEvents = hostedlist;
        HashMap<String, String> invitedmap = new HashMap<>();
        Map<String, String> invitedEvents = invitedmap;
        HashMap<String, Object> settingsmap = new HashMap<>();
        Map<String, Object> settings = settingsmap;
        User user1 = new User(uid, email, phone, firstName, lastName, graduationYear, major, bio,
                photoUrl,RSVPedEvents, hostedEvents, invitedEvents, settings);
        User user2 = new User(uid, email, phone, firstName, lastName, graduationYear, major, bio,
                photoUrl,RSVPedEvents, hostedEvents, invitedEvents, settings);
        assertEquals(user1.toString(), user2.toString());
    }

    @Test
    public void getUid() {
        assertEquals(testUser.getUid(), uid);
    }

    @Test
    public void setUid() {
        User u = new User();
        String s = "INITIALIZED";
        u.setUid(s);
        assertEquals(s, u.getUid());
    }

    @Test
    public void exceptionTest(){
        String err = "UID is immutable. DO NOT CHANGE!";
        try{
            testUser.setUid("BOGUS");
        } catch (RuntimeException e){
            assertEquals(err, e.getMessage());
        }
    }

    @Test
    public void getEmail() {
        assertEquals(testUser.getEmail(), email);
    }

    @Test
    public void setEmail() {
        String otherEmail = "altEmail";
        testUser.setEmail(otherEmail);
        assertEquals(testUser.getEmail(), otherEmail);
        testUser.setEmail(email);
        assertEquals(testUser.getEmail(), email);
    }

    @Test
    public void getPhone() {
        assertEquals(testUser.getPhone(), phone);
    }

    @Test
    public void setPhone() {
        long otherPhone = 456;
        testUser.setPhone(otherPhone);
        assertEquals(testUser.getPhone(), otherPhone);
        testUser.setPhone(phone);
        assertEquals(testUser.getPhone(), phone);
    }

    @Test
    public void getFirstName() {
        assertEquals(testUser.getFirstName(), firstName);
    }

    @Test
    public void setFirstName() {
        String otherFirstName = "OTHERFIRSTNAME";
        testUser.setFirstName(otherFirstName);
        assertEquals(testUser.getFirstName(), otherFirstName);
        testUser.setFirstName(firstName);
        assertEquals(testUser.getFirstName(), firstName);
    }

    @Test
    public void getLastName() {
        assertEquals(testUser.getLastName(), lastName);
    }

    @Test
    public void setLastName() {
        String otherLastName = "OTHERLASTNAME";
        testUser.setLastName(otherLastName);
        assertEquals(testUser.getLastName(), otherLastName);
        testUser.setLastName(lastName);
        assertEquals(testUser.getLastName(), lastName);
    }

    @Test
    public void getGraduationYear() {
        assertEquals(testUser.getGraduationYear(), graduationYear);
    }

    @Test
    public void setGraduationYear() {
        int otherGradYear = 2023;
        testUser.setGraduationYear(otherGradYear);
        assertEquals(testUser.getGraduationYear(), otherGradYear);
        testUser.setGraduationYear(graduationYear);
        assertEquals(testUser.getGraduationYear(), graduationYear);
    }

    @Test
    public void getMajor() {
        assertEquals(testUser.getMajor(), major);
    }

    @Test
    public void setMajor() {
        String otherMajor = "OTHERMAJOR";
        testUser.setMajor(otherMajor);
        assertEquals(testUser.getMajor(), otherMajor);
        testUser.setMajor(major);
        assertEquals(testUser.getMajor(), major);
    }

    @Test
    public void getBio() {
        assertEquals(testUser.getBio(), bio);
    }

    @Test
    public void setBio() {
    }

    @Test
    public void getPhotoUrl() {
        assertEquals(testUser.getPhotoUrl(), photoUrl);
    }

    @Test
    public void setPhotoUrl() {
    }

    @Test
    public void getRSVPedEvents() {
        assertEquals(testUser.getRSVPedEvents(), RSVPedEvents);
    }

    @Test
    public void setRSVPedEvents() {
    }

    @Test
    public void getHostedEvents() {
        assertEquals(testUser.getHostedEvents(), hostedEvents);
    }

    @Test
    public void setHostedEvents() {
    }

    @Test
    public void getInvitedEvents() {
        assertEquals(testUser.getInvitedEvents(), invitedEvents);
    }

    @Test
    public void setInvitedEvents() {
    }

    @Test
    public void getSettings() {
        assertEquals(testUser.getSettings(), settings);
    }

    @Test
    public void setSettings() {
    }
}