package com.example.easyteamup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class SignUpActivity extends AppCompatActivity implements SignUpInterface {

    private User user;
    private String password;
    private ProgressBar loadingBar;
    private Button loginButton;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        loginButton = (Button) findViewById(R.id.login_button);
        loadingBar = (ProgressBar) findViewById(R.id.loading);
        loadingBar.setVisibility(View.INVISIBLE);

        user = new User();
        fragmentManager = getSupportFragmentManager();

            Bundle bundle = new Bundle();
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container, FirstSignUpFragment.class, bundle)
                    .addToBackStack("first fragment")
                    .commit();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent login = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });
    }

    public void onFirstContinue(String email, String psd) {
        loadingBar.setVisibility(View.VISIBLE);
        user.setEmail(email);
        password = psd;

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SecondSignUpFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("second fragment")
                .commit();
        loadingBar.setVisibility(View.INVISIBLE);
    }

    public void onSecondContinue(boolean back, String firstName, String lastName, String phone){
        loadingBar.setVisibility(View.VISIBLE);

        user.setFirstName(firstName);
        user.setLastName(lastName);
        // user.setPhone(phone);

        if (back){
            fragmentManager.popBackStack();
        }
        else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ThirdSignUpFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("third fragment")
                    .commit();
        }
        loadingBar.setVisibility(View.INVISIBLE);
    }

    public void onThirdContinue(boolean back, String major, String year){

    }

    public void onFourthContinue(boolean back, String bio){

    }

    public void onFifthSubmit(boolean back, String image){

    }
}