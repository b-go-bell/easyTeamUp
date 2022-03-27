package com.example.easyteamup.Backend;

import android.content.Context;
import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FirebaseOperations {
    FirebaseAuth auth;
    FirebaseFirestore db;
    StorageReference storage;
    FirebaseUser authenticatedUser;
    RequestQueue requestQueue;

    public FirebaseOperations(Context context) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        authenticatedUser = auth.getCurrentUser();
        requestQueue = Volley.newRequestQueue(context);
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
     * Gets the download link to a user's profile pho!to
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
     * @param hostAvailability The host's availability
     * @return stringCallback is a callback with a String representing the automatically generated
     *              eventID for the object
     */
    public void createEvent(Event event,  Map<String, List<Map<String, String>>> hostAvailability, ObjectCallback stringCallback) {
        event.setHost(authenticatedUser.getUid());
        db.collection("events").add(event).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                String eventId = task1.getResult().getId();
                db.collection("users")
                        .document(event.getHost())
                        .update("hostedEvents", FieldValue.arrayUnion(eventId))
                        .addOnSuccessListener(documentReference -> {
                            RSVPforEvent(event.getHost(), eventId, hostAvailability, bool -> {
                                if (bool) stringCallback.result(eventId);
                                else stringCallback.result(null);
                            });
                        })
                        .addOnFailureListener(e -> {
                            //REMOVE HOSTED EVENT [TBD]
                            stringCallback.result(null);
                        });

            }
            else {
                stringCallback.result(null);
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
     * @param uids A List of uids corresponding to the User that
     *                 will be returned
     * @return listObject contains a List of user corresponding to
     *         the specified ids, or an empty ArrayList if none exists.
     */
    public void getUsersByUid(List<String> uids, ObjectCallback listObject) {
        try {
            db.collection("users").whereIn(FieldPath.documentId(), uids).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<User> users = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User u = document.toObject(User.class);
                        u.setUid(document.getId());
                        users.add(u);
                    }
                    listObject.result(users);
                } else {
                    listObject.result(null);
                }
            });
        }
        catch (RuntimeException re) {
        listObject.result(null);
        }
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
     * and the value is the invitation status.
     * @throws NullPointerException when there are no invited events or
     * an error with Firestore
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
     * @throws NullPointerException when there are no RSVPed events or
     * an error with Firestore
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
     * @throws NullPointerException when there are no past events or
     * an error with Firestore
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
     * Returns events that the user is set to be hosting (but haven't
     * yet passed).
     * @param uid
     * @return listObject is a List<String> containing eventIds that the
     * user has hosted.
     * @throws NullPointerException when there are no hosted events or
     * an error with Firestore
     */
    public void getHostedEvents(String uid, ObjectCallback listObject) {
        db.collection("users")
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map <String, Object> userData = task.getResult().getData();
                        listObject.result(userData.get("hostedEvents"));
                    }
                    else {
                        listObject.result(null);
                    }
                });
    }

    /**
     * @param eventIds A List of eventIds corresponding to the Event that
     *                 will be returned
     * @return listObject contains a List of events corresponding to
     *         the specified ids, or an empty ArrayList if none exists.
     */
    public void getEventsByEventId(List<String> eventIds, ObjectCallback listObject) {
        try {
            db.collection("events")
                    .whereIn(FieldPath.documentId(), eventIds)
                    .get()
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            List<Event> events = new ArrayList<>();
                            List<Task<?>> tasks = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task1.getResult()) {
                                Event e = document.toObject(Event.class);
                                e.setEventId(document.getId());

                                Task<DocumentSnapshot> checkRsvp = document
                                        .getReference()
                                        .collection("rsvpedUsers")
                                        .document(authenticatedUser.getUid())
                                        .get();
                                Task<DocumentSnapshot> checkInvitation = document
                                        .getReference()
                                        .collection("invitedUsers")
                                        .document(authenticatedUser.getUid())
                                        .get();

                                Task<Void> allTask = Tasks.whenAll(checkRsvp, checkInvitation);
                                tasks.add(allTask);
                                allTask.addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        e.setIsRsvped(checkRsvp.getResult().exists());

                                        DocumentSnapshot invitation = checkInvitation.getResult();
                                        if (invitation.exists()) {
                                            e.setInvitationStatus(invitation.getString("status"));
                                        }
                                        events.add(e);
                                    }
                                });
                            }//end for

                            Tasks.whenAll(tasks).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    listObject.result(events);
                                }
                                else {
                                    listObject.result(null);
                                }
                            });

                        }
                        else {
                            listObject.result(null);
                        }
                    });
        }
        catch (RuntimeException re) {
            listObject.result(null);
        }

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
     * Get the host's available times for an event
     * @param eventId The event
     * @param mapObject contains a Map<String, List<Map<String, String>>> (can
     *                  be shorted to (Map<String, Object>) when casting, that
     *                  denotes a host's availability for the event corresponding
     *                  to eventId.
     */
    public void getHostAvailability(String eventId, ObjectCallback mapObject) {
        db.collection("events")
                .document(eventId)
                .get()
                .addOnSuccessListener(result1 -> {
                    String host = result1.getString("host");
                    db.collection("events")
                            .document(eventId)
                            .collection("userAvailabilities")
                            .document(host)
                            .get()
                            .addOnSuccessListener(result2 -> {
                                mapObject.result(result2.getData());
                            })
                            .addOnFailureListener(error -> {
                                mapObject.result(null);
                            });
                })
                .addOnFailureListener(error -> {
                    mapObject.result(null);
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
                })
                .addOnFailureListener(e -> {
                    bc.isTrue(false);
                });

    }

    public void removeRSVPFromEvent(String uid, String eventID, BooleanCallback bc) {

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
                    bc.isTrue(false);
                    throw new ArrayIndexOutOfBoundsException("User invitation does not exits");
                });
    }

    /**
     * Takes a private event, and makes it public. All invitations and RSVPs
     * will remain unchanged. NOTE: Does not check if the event is already
     * public.
     * @param eventId
     * @throws IllegalStateException when the event is already public
     * @param bc returns true is conversion was successful, false otherwise
     */
    public void convertPrivateToPublic(String eventId, BooleanCallback bc){
        getEventsByEventId(new ArrayList<String>() {{ add(eventId); }},
                res -> {
                    db.collection("events")
                            .document(eventId)
                            .update("isPublic", true)
                            .addOnCompleteListener(task -> {
                                bc.isTrue(task.isSuccessful());
                            });
                });
    }

    /**
     * Converts an event from public to private. NOTE: Does not check if the function
     * is already private.
     * @param eventId The event being converted
     * @param keepRSVPedUsers If true, all user rsvps, availabilities, and invitations
     *                        will be deleted.
     * @param bc will contain true if the conversion was a success
     */
    public void convertPublicToPrivate(String eventId, boolean keepRSVPedUsers, BooleanCallback bc) {
        getEventsByEventId(new ArrayList<String>() {{add(eventId);}},
                res -> {
            db.collection("events")
                    .document(eventId)
                    .update("isPublic", false)
                    .addOnCompleteListener(task -> {
                       if (task.isSuccessful()){
                            if (!keepRSVPedUsers) {
                                StringRequest request = new StringRequest(Request.Method.DELETE,
                                        "https://easy-team-up.uc.r.appspot.com/deleteInvitationsAndRsvps",
                                        response -> {
                                            bc.isTrue(true);
                                        },
                                        error -> {
                                            bc.isTrue(false);
                                        })
                                {
                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        Map<String, String> headers = new HashMap<>();
                                        headers.put("eventId", eventId);
                                        return headers;
                                    }
                                };
                                requestQueue.add(request);
                            }
                            else {
                                bc.isTrue(true);
                            }
                       }
                       else {
                           bc.isTrue(false);
                       }
                    });
        });
    }

    /**
     * Returns all public events within a certain radius of the User's current
     * location.
     * @param currentLocation The user's current location
     * @param radius (In miles) Specifies how far you want to look from
     *               the user's current location for public
     * @return listObject contains a List<String> containing all found
     * eventIds
     */
    public void getPublicEvents(GeoLocation currentLocation, double radius, ObjectCallback listObject){

        //create query parameters to get all events within radius
        final double radiusInM = radius * 1609.34;
        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(currentLocation, radiusInM);
        List<Task<QuerySnapshot>> queries = new ArrayList<>();
        for (GeoQueryBounds b : bounds) {
            Query q = db.collection("events")
                    .whereEqualTo("isPublic", true)
                    .orderBy("geohash")
                    .startAt(b.startHash)
                    .endAt(b.endHash);
            queries.add(q.get());
        }

        //run queries, remove false negatives, and get list of DocumentSnapshots
        Tasks.whenAllComplete(queries).addOnCompleteListener(t -> {
            List<DocumentSnapshot> events = new ArrayList<>();
            for (Task<QuerySnapshot> query: queries) {
                QuerySnapshot result = query.getResult();
                for (DocumentSnapshot event: result.getDocuments()) {
                    double lat = event.getDouble("latitude");
                    double lng = event.getDouble("longitude");

                    // We have to filter out a few false positives due to GeoHash
                    // accuracy, but most will match
                    GeoLocation docLocation = new GeoLocation(lat, lng);
                    double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, currentLocation);
                    if (distanceInM <= radiusInM) {
                        events.add(event);
                    }
                }
            }

            //sort by distance
            events.sort((documentSnapshot1, documentSnapshot2) -> {
                double diff1 = GeoFireUtils.getDistanceBetween(currentLocation,
                        new GeoLocation(documentSnapshot1.getDouble("latitude"),
                                documentSnapshot1.getDouble("longitude")));

                double diff2 = GeoFireUtils.getDistanceBetween(currentLocation,
                        new GeoLocation(documentSnapshot2.getDouble("latitude"),
                                documentSnapshot2.getDouble("longitude")));

                return Double.compare(diff1, diff2);
            });

            //convert to list of eventIds
            List<String> eventIds = new ArrayList<>();
            for (DocumentSnapshot ds : events) {
                eventIds.add(ds.getId());
            }
            listObject.result(eventIds);

        });
    }


    /**
     * Deletes an Event, including all user rsvps, invitations,
     * and user availabilities. THIS COMMAND IS FINAL AND THERE
     * IS NO GOING BACK!!! NOTE: This command can only be executed
     * when the currently logged in user is the host of the event.
     * @param eventId The event being deleted.
     * @param bc
     */
    public void deleteEvent(String eventId, BooleanCallback bc) {
        //check to make sure the host is deleting the event
        DocumentReference eventRef = db.collection("events").document(eventId);
        eventRef.get().addOnSuccessListener(result -> {
            if (result.getString("host").equals(authenticatedUser.getUid())) {
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE,
                        "https://easy-team-up.uc.r.appspot.com/deleteInvitationsAndRsvps",
                        null,
                        response -> {

                            Task<Void> deleteEvent = eventRef.delete();
                            Task<Void> removeFromHost = db.collection("users")
                                    .document(authenticatedUser.getUid())
                                    .update("hostedEvents", FieldValue.arrayRemove(eventId));

                            Tasks.whenAll(deleteEvent, removeFromHost)
                                    .addOnSuccessListener(unused -> {
                                        bc.isTrue(true);
                                    })
                                    .addOnFailureListener(error -> {
                                        bc.isTrue(false);
                                    });
                        },
                        error -> {
                            bc.isTrue(false);
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                headers.put("eventId", eventId);
                                return headers;
                            }
                };
                requestQueue.add(request);
            } else {
                bc.isTrue(false);
            }
        });

    }



    /**
     * Checks if an email address has already been registered with a user
     * @param email The email address being checked
     * @returns bc will contain True if the email address is already in
     * use, false otherwise
     */
    public void checkIfEmailInUse(String email, BooleanCallback bc){

        JsonObjectRequest request = new JsonObjectRequest("https://easy-team-up.uc.r.appspot.com/checkEmailInUse",
                response -> {
                    try {
                        bc.isTrue(response.getBoolean("inUse"));
                    } catch (JSONException e) {
                        bc.isTrue(null);
                    }
                },
                error -> {
                    bc.isTrue(null);
                })
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("email", email);
                        return headers;
                    }
                };

        requestQueue.add(request);
    }

    /**
     * Checks if a user has RSVPed for an event
     * @param uid
     * @param eventId
     * @returns bc contains true if the user has rsvped for the event, false otherwise
     */
    public void checkIfUserRSVPedForEvent(String uid, String eventId, BooleanCallback bc){
        db.collection("events")
                .document(eventId)
                .collection("rsvpedUsers")
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        bc.isTrue(task.getResult().exists());
                    }
                    else bc.isTrue(null);
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
