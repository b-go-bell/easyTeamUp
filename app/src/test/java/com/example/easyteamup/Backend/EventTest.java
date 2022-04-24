package com.example.easyteamup.Backend;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.firebase.Timestamp;
import java.sql.Time;
import java.util.Date;

public class EventTest {

    @Test
    public void setEventId() {
        Event event = new Event();
        String id = "test";
        event.setEventId("test");
        assertEquals(event.getEventId(), id);
    }

    @Test
    public void throwTest(){
        Event event = new Event();
        String id = "test";
        event.setEventId("test");
        try{
            event.setEventId("new");
        }
        catch(RuntimeException e){
            assertEquals("eventId is immutable. DO NOT CHANGE!", e.getMessage());
        }
    }

    @Test
    public void getEventId() {
        Event event = new Event();
        String id = "test";
        event.setEventId("test");
        assertEquals(event.getEventId(), id);
    }

    @Test
    public void getName() {
        Event event = new Event();
        String name = "test";
        event.setName("test");
        assertEquals(event.getName(), name);
    }

    @Test
    public void setName() {
        Event event = new Event();
        String name = "test";
        event.setName("test");
        assertEquals(event.getName(), name);
    }

    @Test
    public void getHost() {
        Event event = new Event();
        String host = "test";
        event.setHost("test");
        assertEquals(event.getHost(), host);
    }

    @Test
    public void setHost() {
        Event event = new Event();
        String host = "test";
        event.setHost("test");
        assertEquals(event.getHost(), host);
    }

    @Test
    public void getAddress() {
        Event event = new Event();
        String address = "test";
        event.setAddress("test");
        assertEquals(event.getAddress(), address);
    }

    @Test
    public void setAddress() {
        Event event = new Event();
        String address = "test";
        event.setAddress("test");
        assertEquals(event.getAddress(), address);
    }

    @Test
    public void getCategory() {
        Event event = new Event();
        String cat = "test";
        event.setCategory("test");
        assertEquals(event.getCategory(), cat);
    }

    @Test
    public void setCategory() {
        Event event = new Event();
        String cat = "test";
        event.setCategory("test");
        assertEquals(event.getCategory(), cat);
    }

    @Test
    public void getLatitude() {
        setLatitude();
    }

    @Test
    public void setLatitude() {
        Event event = new Event();
        Double lat = 6.902;
        event.setLatitude(6.902);
        assertEquals(event.getLatitude(), lat);
    }

    @Test
    public void getLongitude() {
        Event event = new Event();
        Double lon = 6.902;
        event.setLongitude(6.902);
        assertEquals(event.getLongitude(), lon);
    }

    @Test
    public void setLongitude() {
        getLongitude();
    }

    @Test
    public void getEventLength() {
        setEventLength();
    }

    @Test
    public void setEventLength() {
        Event event = new Event();
        Double t = 60.0;
        event.setEventLength(t);
        assertEquals(event.getEventLength(), t);
    }

    @Test
    public void getDueDate() {
        Event event = new Event();
        Date date = new Date(System.currentTimeMillis());
        Timestamp time = new Timestamp(date);
        event.setDueDate(time);
        assertEquals(event.getDueDate(), time);
    }

    @Test
    public void setDueDate() {
        getDueDate();
    }

    @Test
    public void getFinalTime() {
        setFinalTime();
    }

    @Test
    public void setFinalTime() {
        Event event = new Event();
        Date date = new Date(System.currentTimeMillis());
        Timestamp time = new Timestamp(date);
        event.setFinalTime(time);
        assertEquals(event.getFinalTime(), time);
    }

    @Test
    public void getIsPublic() {
        Event event = new Event();
        event.setIsPublic(true);
        assertTrue(event.getIsPublic());
        event.setIsPublic(false);
        assertFalse(event.getIsPublic());
    }

    @Test
    public void setIsPublic() {
        getIsPublic();
    }

    @Test
    public void getIsRsvped() {
        Event event = new Event();
        event.setIsRsvped(true);
        assertTrue(event.getIsRsvped());
        event.setIsRsvped(false);
        assertFalse(event.getIsRsvped());
    }

    @Test
    public void setIsRsvped() {
        getIsRsvped();
    }

    @Test
    public void getInvitationStatus() {
        Event event = new Event();
        String s = "test string";
        event.setInvitationStatus(s);
        assertEquals(event.getInvitationStatus(), s);
    }

    @Test
    public void setInvitationStatus() {
        getInvitationStatus();
    }

    @Test
    public void getDescription() {
        Event event = new Event();
        String s = "test string";
        event.setDescription(s);
        assertEquals(event.getDescription(), s);
    }

    @Test
    public void setDescription() {
        getDescription();
    }

    @Test
    public void toStringTest(){
        String eventId = "TESTID";
        String name = "TESTNAME";
        String host = "TESTHOST";
        String address = "TESTADDRESS";
        String geohash = "TESTGEOHASH";
        String description = "TESTDESC";
        String category = "TESTCAT";
        Double latitude = 119.112;
        Double longitude = 20.21;
        Double eventLength = 60.0;
        Date date1 = new Date(System.currentTimeMillis());
        Timestamp dueDate = new Timestamp(date1);
        Date date2 = new Date(System.currentTimeMillis());
        Timestamp finalTime = new Timestamp(date2);
        boolean isPublic = true;
        boolean rsvped = false;
        String invitationStatus = "STATUSTEST";
        Event event1 = new Event(eventId, name, host, address, geohash, description,
                category, latitude, longitude, eventLength,dueDate, finalTime, isPublic,
                rsvped, invitationStatus);
        Event event2 = new Event(eventId, name, host, address, geohash, description,
                category, latitude, longitude, eventLength,dueDate, finalTime, isPublic,
                rsvped, invitationStatus);
        assertEquals(event1.toString(), event2.toString());
    }
}