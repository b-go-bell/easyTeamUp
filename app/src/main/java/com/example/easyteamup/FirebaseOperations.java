package com.example.easyteamup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


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

}
