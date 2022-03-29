package com.example.easyteamup.Activities.UserHomeActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.easyteamup.Activities.UserHomeActivities.LoginLogoutActivities.LoginActivity;
import com.example.easyteamup.Activities.UserHomeActivities.SignUpActivities.SignUpActivity;
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.R;

public class StartActivity extends AppCompatActivity {

    private Button loginButton, signupButton;
    private ProgressBar loadingBar;

    private FirebaseOperations fops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        loginButton = (Button) findViewById(R.id.login_button);
        signupButton = (Button) findViewById(R.id.signup_button);

        loadingBar = (ProgressBar) findViewById(R.id.loading);
        loadingBar.setVisibility(View.INVISIBLE);

        fops = new FirebaseOperations(this);

        if(fops.checkIfUserLoggedIn()){
            Intent viewProfile = new Intent(StartActivity.this, ViewProfileActivity.class);
            String uid = fops.getLoggedInUserId();
            viewProfile.putExtra("uid", uid);
            startActivity(viewProfile);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent login = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent signup = new Intent(StartActivity.this, SignUpActivity.class);
                startActivity(signup);
            }
        });
    }
}