package com.example.easyteamup.Backend;

import static org.junit.Assert.*;

import org.junit.Test;

public class EventTest {

    @Test
    public void setEventId() {
        Event event = new Event();
        String id = "test";
        event.setEventId("test");
        assertEquals(event.getEventId(), id);
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
    }

    @Test
    public void setLatitude() {
    }

    @Test
    public void getLongitude() {
    }

    @Test
    public void setLongitude() {
    }

    @Test
    public void getEventLength() {
    }

    @Test
    public void setEventLength() {
    }

    @Test
    public void getDueDate() {
    }

    @Test
    public void setDueDate() {
    }

    @Test
    public void getFinalTime() {
    }

    @Test
    public void setFinalTime() {
    }

    @Test
    public void getIsPublic() {
    }

    @Test
    public void setIsPublic() {
    }

    @Test
    public void getIsRsvped() {
    }

    @Test
    public void setIsRsvped() {
    }

    @Test
    public void getInvitationStatus() {
    }

    @Test
    public void setInvitationStatus() {
    }

    @Test
    public void getDescription() {
    }

    @Test
    public void setDescription() {
    }
}