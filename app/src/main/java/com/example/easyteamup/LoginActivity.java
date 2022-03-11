package com.example.easyteamup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameText, passwordText;
    private Button submitButton, signupButton;
    private ProgressBar loadingBar;
    private TextView invalid;

    FirebaseOperations fops = new FirebaseOperations();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        usernameText = (EditText) findViewById(R.id.username_text);
        passwordText = (EditText) findViewById(R.id.password_text);
        submitButton = (Button) findViewById(R.id.submit_button);
        signupButton = (Button) findViewById(R.id.signup_button);
        invalid = (TextView) findViewById(R.id.bad_login);

        loadingBar = (ProgressBar) findViewById(R.id.loading);
        loadingBar.setVisibility(View.INVISIBLE);
        invalid.setVisibility(View.INVISIBLE);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);

                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();
                fops.loginUser(username, password, bool -> {
                    if (bool){
                        invalid.setVisibility(View.INVISIBLE);
                        Intent viewProfile = new Intent(LoginActivity.this, ViewProfileActivity.class);
                        startActivity(viewProfile);
                    }
                    else {
                        loadingBar.setVisibility(View.INVISIBLE);
                        invalid.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);

                Intent signup = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signup);
            }
        });
    }
}