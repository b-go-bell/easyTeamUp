package com.example.easyteamup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;


public class FirebaseOperations {
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser authenticatedUser;

    public FirebaseOperations() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    /**
     *
     * @param email
     * @param password
     * @return BooleanCallback contains a bool indicating whether the login
     *      was successful or not.
     */
    public void loginUser(String email, String password, BooleanCallback bc) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            bc.isTrue(task.isSuccessful());
        });
    }

    /**
     *
     * @param user an already initialized User object with any desired profile attributes
     *             already set
     * @param password
     * @return BooleanCallback contains a bool indicating whether the login
     *      *      was successful or not.
     */
    public void registerUser(User user, String password, BooleanCallback bc) {
        auth.createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                authenticatedUser = auth.getCurrentUser();
                user.setUid(authenticatedUser.getUid()); //userId has been generated, save to object


                //create Map from User
                ObjectMapper UserToMap = new ObjectMapper();
                Map <String, Object> mappedUser =   UserToMap.convertValue(user, Map.class);
                mappedUser.remove("uid");


                db.collection("users").document(authenticatedUser.getUid()).set(mappedUser);
                db.collection("users").document(authenticatedUser.getUid()).collection("pastEvents");
                bc.isTrue(true);
            }
            else bc.isTrue(false);
        });
    }


    /**
     * @param uid A userid corresponding to the User that will be returned
     * @return ObjectCallback contains a User corresponding to uid, or
     * null if none exists.
     */
    public void getUserByUid(String uid, ObjectCallback userObject){
        db.collection("users").document(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                User res = task.getResult().toObject(User.class);
                res.setUid(uid);
                userObject.result(res);
            }
            else {
                userObject.result(null);
            }
        });
    }

    /**
     *
     * @param user a user object with all desired attributes already set
     * @param uid the uid that will correspond to the user
     * @return BooleanCallback will be true if operation is succesful
     */
    public void setUser(User user, String uid, BooleanCallback bc) {
        db.collection("users").document(uid).set(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                bc.isTrue(true);
            }
            else {
                bc.isTrue(true);
            }
        });
    }

    /**
     * Get a list of a users' invitations and as well as their RSVP status.
     * Options are {attending, pending, rejected}
     * @param uid Represents the user who's invitations we are checking
     * @return mapObject is a Map <String, String> where the key is the eventId
     * and and the value is the invitation status.
     */
    public void getInvitedEvents(String uid, ObjectCallback mapObject) {
        db.collection("users").document(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Map<String, Object> userInfo = task.getResult().getData();
                mapObject.result(userInfo.get("invitedEvents"));
            }
            else{
                mapObject.result(null);
            }
        });
    }

    /**
     * Get a list of eventIds corresponding to events that a User has RSVPed for
     * @param uid Represents the user who's invitations we are checking
     * @return listObject is a List<String> containing eventsIds for all events that
     * the user has RSVPed for and confirmed that they will attend.
     */
    public void getRSVPedEvents(String uid, ObjectCallback listObject) {
        db.collection("users").document(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Map<String, Object> userInfo = task.getResult().getData();
                listObject.result(userInfo.get("rsvpedEvents"));
            }
        });
    }

    /**
     * @param eventId A userid corresponding to the Event that will be returned
     * @return ObjectCallback contains a Event corresponding to eventId, or
     * null if none exists.
     */
    public void getEventByEventid(String eventId, ObjectCallback eventObject) {
        db.collection("events").document(eventId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Event res = task.getResult().toObject(Event.class);
                res.setEventId(eventId);
                eventObject.result(res);
            }
            else {
                eventObject.result(null);
            }
        });
    }

    /**
     *
     * @param event An already initialized event object, with all desired details set.
     *              <span class = "strong">Note: Leave EventId as null.</span> It will be automatically
     *              generated by Firebase.
     * @return eventIdString is a callback with a String
     * representing the automatically generated eventID for the object
     */
    public void createEvent(Event event, ObjectCallback eventIdString) {
        String eventId = null;
        db.collection("events").add(event).addOnSuccessListener(documentReference -> {
            eventIdString.result(documentReference.getId());
        });
    }



}
