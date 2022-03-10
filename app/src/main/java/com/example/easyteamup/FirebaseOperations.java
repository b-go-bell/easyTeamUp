package com.example.easyteamup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            else{
                listObject.result(null);
            }
        });
    }

    /**
     * Gets a list of eventIds corresponding to events that the user has previously
     * attended.
     * @param uid
     * @return listObject is a List<String> containing eventIds for all events that the users
     * has RSVPed for and confirmed that they will attend.
     */
    public void getPastEvents(String uid, ObjectCallback listObject) {
        db.collection("users").document(uid).collection("pastEvents").get().addOnCompleteListener(task-> {
            if (task.isSuccessful()){
                List<String> invitedEvents = new ArrayList<String>();
                for (QueryDocumentSnapshot document: task.getResult()){
                    invitedEvents.add(document.getId());
                }
                listObject.result(null);
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
     * @return eventIdString is a callback with a String representing the automatically generated
     *              eventID for the object
     */
    public void createEvent(Event event, ObjectCallback eventIdString) {
        String eventId = null;
        db.collection("events").add(event).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                eventIdString.result(task.getResult().getId());
            }
            else {
                eventIdString.result(null);
            }
        });
    }

    /**
     * NEEDS TO BE TESTED STILL
     * Invite a user to an event (action initiated by event host). If the user
     * has already RSVPed for an event, the invitation status will be set to
     * "attending". Otherwise, the invitation will be set to "pending".
     * @param uid Corresponds to the user being invited
     * @param eid Corresponds to the event the user is being invited too.
     *            Note: This event should be hosted by the currently
     *            logged-in user, as only the host can make invitations.
     * @return BooleanCallback returns true if the user has been successfully
     *            invited, false otherwise. NOTE: this function makes two separate
     *            database calls. If one succeeds but the other doesn't, this function
     *            currently doesn't handle it, which leads to contradicting data. If
     *            this function ends up frequently returning false, lmk so I can
     *            do debugging and get a sense of whats going on, and also clean
     *            up the data.
     */
    public void inviteUserToEvent(String uid, String eid, BooleanCallback bc){

        //first, check to see if the user has RSVPed to an event already
        db.collection("events")
                .document(eid)
                .collection("RSVPedUsers")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    //set invitation status, based on whether a user has RSVPed or not
                    String status = documentSnapshot.exists() ? "attending" : "pending";

                    Task<Void> updateUser = db.collection("users")
                            .document(uid)
                            .update("invitedEvents." + eid, status);
                    Task<Void> updateEvent = db.collection("events")
                            .document(eid)
                            .collection("invitedUsers")
                            .document(uid)
                            .set(new HashMap<String, Object>(){{ put("status", status); }});

                    while(!updateEvent.isComplete() || !updateUser.isComplete());
                    bc.isTrue(updateUser.isSuccessful() && updateEvent.isSuccessful());
                })
                .addOnFailureListener(listener -> {
                    bc.isTrue(false);
                });

    }


}
