package com.example.easyteamup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public void loginUser(String email, String password, BooleanCallback bc) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            bc.isTrue(task.isSuccessful());
        });
    }

    public void registerUser(User user, String password, BooleanCallback bc) {
        auth.createUserWithEmailAndPassword(user.email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                authenticatedUser = auth.getCurrentUser();
                user.setUid(authenticatedUser.getUid()); //userId has been generated, save to object


                //create Map from User
                ObjectMapper UserToMap = new ObjectMapper();
                Map <String, Object> mappedUser = UserToMap.convertValue(user, Map.class);
                mappedUser.remove("uid");


                db.collection("users").document(authenticatedUser.getUid()).set(mappedUser);
                db.collection("users").document(authenticatedUser.getUid()).collection("pastEvents");
                bc.isTrue(true);
            }
            else bc.isTrue(false);
        });
    }

}
