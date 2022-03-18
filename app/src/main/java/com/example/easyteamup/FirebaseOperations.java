package com.example.easyteamup;

import android.net.Uri;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FirebaseOperations {
    FirebaseAuth auth;
    FirebaseFirestore db;
    StorageReference storage;
    FirebaseUser authenticatedUser;

    public FirebaseOperations() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
    }

    /**
     * Logs in a user using firebase authentication. By default, this user
     * will remain logged in until the user is explicitly signed out.
     * @param email
     * @param password
     * @return BooleanCallback contains a bool indicating whether the login
     *      was successful or not.
     */
    public void loginUser(String email, String password, BooleanCallback bc) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                authenticatedUser = auth.getCurrentUser();
                bc.isTrue(true);
            }
            else {
                bc.isTrue(false);
            }
        });
    }

    /**
     *
     * @param user an already initialized User object with any desired profile attributes
     *             already set
     * @param password
     * @return BooleanCallback contains a bool indicating whether the user was succesfully
     *         able to sign up or not.
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
     * Get the currently logged in User's uid
     * @return the uid of the currently logged in user, and null if no user
     * is logged in
     */
    public String getLoggedInUserId() {
        if (authenticatedUser != null){
            return authenticatedUser.getUid();
        }
        else {
            return null;
        }
    }

    public void signOut() {
        auth.signOut();
    }

    /**
     * Will upload a profile picture to Firebase storage for the user. The
     * URL will be profile_pictures/[uid].jpg. Note: uploading a new photo
     * will replace the old photo.
     * @param uid The user who's profile is being updated
     * @param imageUri The valid uri leading to the image being uploaded
     * @return bc will contain true if the file was succesfully updated, false
     *         otherwise
     */
    public void uploadProfilePhoto(String uid, Uri imageUri, BooleanCallback bc){
        StorageReference fileRef = storage.child("profile_pictures/"+ uid+".jpg");
        fileRef.putFile(imageUri).addOnCompleteListener(task -> {
            bc.isTrue(task.isComplete());
        });
    }

    /**
     * Gets the download link to a user's profile photo
     * @param uid
     * @return uriObject is a Uri containing the download url for the
     * profile photo with corresponding userId, or will throw a
     * StorageException if no photo exists
     */
    public void getProfilePhoto(String uid, ObjectCallback uriObject){
        StorageReference fileRef = storage.child("profile_pictures/"+ uid+".jpg");
        fileRef.getDownloadUrl()
                .addOnSuccessListener(uri ->{
                    uriObject.result(uri);
                })
                .addOnFailureListener(exception ->{
                    uriObject.result(null);
                });
    }

    /**
     * Will create a new event in Firestore, based on the event object passed in. Will also set the currently
     * logged in user as meeting host.
     * @param event An already initialized event object, with all desired details set.
     *              <span class = "strong">Note: Leave EventId and host as null.</span> It will be automatically
     *              generated by Firebase.
     * @return eventIdString is a callback with a String representing the automatically generated
     *              eventID for the object
     */
    public void createEvent(Event event, ObjectCallback eventIdString) {
        event.setHost(authenticatedUser.getUid());
        db.collection("events").add(event).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                String eventId = task1.getResult().getId();
                db.collection("users")
                        .document(event.getHost())
                        .update("hostedEvents", FieldValue.arrayUnion(eventId))
                        .addOnSuccessListener(documentReference -> {
                          eventIdString.result(eventId);
                        })
                        .addOnFailureListener(e -> {
                            //REMOVE HOSTED EVENT [TBD]
                            eventIdString.result(null);
                        });

            }
            else {
                eventIdString.result(null);
            }
        });
    }

    /**
     * Update event details. In general, the workflow for updating an event
     * should be as follows:
     *      - Use getEventByEventId() to get Event Object
     *      - Create an EventDetails object using the Event Constructor:
     *          EventDetails ed = new EventDetails(event);
     *      - Modify that eventDetails object using getters and setters
     *      - Use setEvent() to push those changes to firebase
     * @param eventId corresponds to the event who's details you want to chagne
     * @param eventDetails The newly updated EventDetails object. Note that this
     *                     contains all of the fields that can be updated at will.
     * @return bc returns true if the event was successfully updated
     */
    public void setEvent(String eventId, EventDetails eventDetails, BooleanCallback bc){
        db.collection("events")
                .document(eventId)
                .set(eventDetails, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    bc.isTrue(task.isSuccessful());
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
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){

                        //create Map from User
                        ObjectMapper UserToMap = new ObjectMapper();
                        Map <String, Object> mappedUser =   UserToMap.convertValue(user, Map.class);
                        mappedUser.remove("uid");

                        db.collection("users").document(uid).set(mappedUser).addOnCompleteListener(task -> {
                            bc.isTrue(task.isSuccessful());
                        });
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
                List<String> invitedEvents = new ArrayList<>();
                for (QueryDocumentSnapshot document: task.getResult()){
                    invitedEvents.add(document.getId());
                }
                listObject.result(invitedEvents);
            }
            else {
                listObject.result(null);
            }
        });
    }

    /**
     * @param eventId A eventId corresponding to the Event that will be returned
     * @return ObjectCallback contains a Event corresponding to eventId, or
     * null if none exists.
     */
    public void getEventByEventid(String eventId, ObjectCallback eventObject) {
        db.collection("events").document(eventId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Event event = task.getResult().toObject(Event.class);
                event.setEventId(eventId);
                eventObject.result(event);
            }
            else {
                eventObject.result(null);
            }
        });
    }



    /**
     * Gets a list of uids of all users that have RSVPed for an event.
     * NOTE: Do not use this functions to add or remove RSVPs, instead use
     * the dedicated functions for those purposes. This function should be
     * treated just like a regular getter - use it to GET information about
     * the RSVPed users, but DON'T use it to SET information about them.
     * @param eventId corresponds to the event who's RSVP list will be checked
     * @return listObject is a List<String> of uids of all rsvped guests
     */
    public void getRSVPedUsers(String eventId, ObjectCallback listObject){
        db.collection("events").document(eventId).collection("rsvpedUsers").get().addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               List<String> RSVPedUsers = new ArrayList<>();
               for (QueryDocumentSnapshot document: task.getResult()){
                   RSVPedUsers.add(document.getId());
               }
               listObject.result(RSVPedUsers);
           }
           else {
               listObject.result(null);
           }
        });
    }

    /**
     * Gets a list of all users that have been invited to an event, as well
     * as their invitation status.
     * NOTE: Do not use these functions to add,remove, or modify invitations, instead use
     * the dedicated functions for those purposes. This function should be
     * treated just like a regular getter - use it to GET information about
     * the invited users, but DON'T use it to SET information about them.
     * @param eventId corresponds to the event who's RSVP list will be checked
     * @param mapObject listObject is a Map<String,String> where the key is the
     *                  uid, and the value is the invitation status, which could
     *                  be one of {attending, pending, rejected}
     */
    public void getInvitedUsers(String eventId, ObjectCallback mapObject){
        db.collection("events").document(eventId).collection("invitedUsers").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Map<String,String> invitedUsers = new HashMap<>();
                for (QueryDocumentSnapshot document: task.getResult()){
                    String key = document.getId();
                    String value = (String) document.getData().get("status");
                    invitedUsers.put(key,value);
                }
                mapObject.result(invitedUsers);
            }
            else {
                mapObject.result(null);
            }
        });
    }

    /**
     * Invite a user to an event (action initiated by event host). If the user
     * has already RSVPed for an event, the invitation status will be set to
     * "attending". Otherwise, the invitation will be set to "pending".
     * @param uid Corresponds to the user being invited
     * @param eventId Corresponds to the event the user is being invited too.
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
    public void inviteUserToEvent(String uid, String eventId, BooleanCallback bc){

        //first, check to see if the user has RSVPed to an event already
        db.collection("events")
                .document(eventId)
                .collection("rsvpedUsers")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    //set invitation status, based on whether a user has RSVPed or not
                    String status = documentSnapshot.exists() ? "attending" : "pending";

                    //add invitation to user
                    Task<Void> updateUser = db.collection("users")
                            .document(uid)
                            .update("invitedEvents." + eventId, status);

                    //add invitation to event
                    Task<Void> updateEvent = db.collection("events")
                            .document(eventId)
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

    /**
     * Updates the availability of a user already registered for an event.
     * @param uid
     * @param eventId
     * @param availability The time availability of of a user who is registering.
     *                    Here is what the keys and values look kinda like:
     *                    Map<"Day", List<Map<["startDate"/"endDate"],actual time>>>.
     *                    A null argument here is equivalent to 0 availability.
     * @param bc Will return false if uid is not registered for eventId or for
     *           any other failure, true otherwise
     */
    public void updateUserAvailability(String uid, String eventId, Map<String, List<Map<String, String>>> availability, BooleanCallback bc){
        //ensure user is RSVPed for Event
        DocumentReference eventRef = db.collection("events")
                .document(eventId);

        eventRef.collection("rsvpedUsers")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        db.collection("events")
                                .document(eventId)
                                .collection("userAvailabilities")
                                .document(uid)
                                .set(availability != null ? availability : new HashMap<>())
                                .addOnCompleteListener(task -> {
                                    bc.isTrue(task.isSuccessful());
                                });
                    }
                    else {
                        bc.isTrue(false);
                    }
                });

    }

    /**
     * Allows a user to register for a given event. If the user has been invited
     * to an event, this will automatically update the user's invitation status
     * to "attending". This function should also be used for a user to "accept"
     * an invitation.
     * @param uid
     * @param eventId
     * @param availability The time availability of of a user who is registering.
     *                     Here is what the keys and values look kinda like:
     *                     Map<"Day", List<Map<["startDate"/"endDate"],actual time>>>.
     *                     A null argument here is equivalent to 0 availability.
     * @returns BooleanCallback contains true if the event registration was a success,
     *          false otherwise.
     */
    public void RSVPforEvent(String uid, String eventId, Map<String, List<Map<String, String>>> availability, BooleanCallback bc){
        //add event to users RSVPlist
        Task<Void> updateUserRSVP = db.collection("users")
                .document(uid)
                .update("rsvpedEvents", FieldValue.arrayUnion(eventId));

        //add user to events RSVPlist
        Task<Void> updateEventRSVP = db.collection("events")
                .document(eventId)
                .collection("rsvpedUsers")
                .document(uid)
                .set(new HashMap<String, Object>());

        //add userAvailabilities
        Task<Void> addAvailabilities = db.collection("events")
                .document(eventId)
                .collection("userAvailabilities")
                .document(uid)
                .set(availability != null ? availability : new HashMap<>());

        //check if user has been invited
        db.collection("events")
                .document(eventId)
                .collection("invitedUsers")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        //change user invitation status
                        Task<Void> updateUserInvitation = db.collection("users")
                                .document(uid)
                                .update("invitedEvents." + eventId, "attending");

                        //change event invitation status
                        Task<Void> updateEventInvitation = db.collection("events")
                                .document(eventId)
                                .collection("invitedUsers")
                                .document(uid)
                                .update("status", "attending");

                        while (!updateUserRSVP.isComplete()
                                | !updateEventRSVP.isComplete()
                                | !updateUserInvitation.isComplete()
                                | !updateEventInvitation.isComplete()
                                | !addAvailabilities.isComplete());

                        bc.isTrue(updateUserRSVP.isSuccessful()
                                && updateEventRSVP.isSuccessful()
                                && updateUserInvitation.isSuccessful()
                                && updateEventInvitation.isSuccessful()
                                && addAvailabilities.isSuccessful());
                    }
                    else {
                        while (!updateUserRSVP.isComplete()
                                | !updateEventRSVP.isComplete()
                                | !addAvailabilities.isComplete()) ;
                        bc.isTrue(updateUserRSVP.isSuccessful()
                                && updateEventRSVP.isSuccessful()
                                && addAvailabilities.isSuccessful());
                    }
                });

    }


    /**
     * Serves as a method for a user to reject an invitation they have received
     * to a specific event
     * @param uid
     * @param eventId
     * @throws ArrayIndexOutOfBoundsException if an invitation corresponding to
     *         the provided user and event doesn't exist
     * @return bc returns true if event rejection was succesful
     */
    public void rejectEventInvitation(String uid, String eventId, BooleanCallback bc) {
        db.collection("events")
                .document(eventId)
                .collection("invitedUsers")
                .document(uid)
                .update("status", "rejected")
                .addOnSuccessListener(Void -> {
                    db.collection("users")
                            .document(uid)
                            .update("invitedEvents." + eventId, "rejected")
                            .addOnSuccessListener(Void2 -> {
                                bc.isTrue(true);
                            })
                            .addOnFailureListener(Void2 -> {
                                bc.isTrue(false);
                                throw new ArrayStoreException("Invitation exists for Event but not User");
                            });
                })
                .addOnFailureListener(e -> {
                    System.out.println("ERROR!: " + e);
                    bc.isTrue(false);
                    throw new ArrayIndexOutOfBoundsException("User invitation does not exits");
                });
    }

    //to be deleted - this is just for debugging purposes
    public void loadSampleUsers() {
        User user1 = new User();
        user1.setEmail("example1@gmail.com");
        user1.setFirstName("User1");
        user1.setLastName("UserOne");
        user1.setGraduationYear(2021);
        user1.setMajor("Underwater Basket Weaving");
        user1.setBio("Road work ahead? I sure hope it does!");
        registerUser(user1, "Password", bool -> {
            if (bool) System.out.println("User1 Registered!");
        });

        User user2 = new User();
        user2.setEmail("example2@gmail.com");
        user2.setFirstName("User2");
        user2.setLastName("UserTwo");
        user2.setGraduationYear(2022);
        user2.setMajor("Zombie Studies");
        user2.setBio("A potato flew around the room before you came");
        registerUser(user2, "Password", bool -> {
            if (bool) System.out.println("User2 Registered!");
        });

        User user3 = new User();
        user3.setEmail("example3@gmail.com");
        user3.setFirstName("User3");
        user3.setLastName("UserThree");
        user3.setGraduationYear(2023);
        user3.setMajor("Truck Driving");
        user3.setBio("I do not think that word means what you think it means");
        registerUser(user3, "Password", bool -> {
            if (bool) System.out.println("User3 Registered!");
        });

        User user4 = new User();
        user4.setEmail("example4@gmail.com");
        user4.setFirstName("User4");
        user4.setLastName("UserFour");
        user4.setGraduationYear(2024);
        user4.setMajor("Crushing It");
        user4.setBio("There's a snake in my boot!");
        registerUser(user4, "Password", bool -> {
            if (bool) System.out.println("User4 Registered!");
        });


    }

}
