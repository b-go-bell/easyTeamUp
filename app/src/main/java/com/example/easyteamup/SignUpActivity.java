package com.example.easyteamup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignUpActivity extends AppCompatActivity implements SignUpInterface {

    private User user;
    private String password;
    private ProgressBar loadingBar;
    private Button loginButton;
    FragmentManager fragmentManager;
    StorageReference storageReference;


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

    public void onSecondContinue(boolean back, String firstName, String lastName, long phone){
        loadingBar.setVisibility(View.VISIBLE);
        if (back) {
            fragmentManager.popBackStack();
        }
        else {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(phone);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ThirdSignUpFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("third fragment")
                    .commit();
        }
        loadingBar.setVisibility(View.INVISIBLE);
    }

    public void onThirdContinue(boolean back, String major, int year){
        loadingBar.setVisibility(View.VISIBLE);

        user.setMajor(major);
        user.setGraduationYear(year);

        if (back){
            fragmentManager.popBackStack();
        }
        else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FourthSignUpFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("fourth fragment")
                    .commit();
        }
        loadingBar.setVisibility(View.INVISIBLE);
    }

    public void onFourthContinue(boolean back, String bio){
        loadingBar.setVisibility(View.VISIBLE);

        user.setBio(bio);

        if (back){
            fragmentManager.popBackStack();
        }
        else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FifthSignUpFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("fifth fragment")
                    .commit();
        }
        loadingBar.setVisibility(View.INVISIBLE);
    }

    public void onFifthSubmit(boolean back, Uri image){
        loadingBar.setVisibility(View.VISIBLE);

        if(back){
            fragmentManager.popBackStack();
            loadingBar.setVisibility(View.INVISIBLE);
        }
        else {
            /*
                SET IMAGE IN FIREBASE STORAGE
             */
            Intent viewProfile = new Intent(SignUpActivity.this, ViewProfileActivity.class);
            startActivity(viewProfile);
        }
    }
}