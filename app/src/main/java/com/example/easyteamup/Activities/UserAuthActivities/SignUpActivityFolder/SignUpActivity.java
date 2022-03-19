package com.example.easyteamup.Activities.UserAuthActivities.SignUpActivityFolder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.easyteamup.Activities.UserAuthActivities.LoginActivity;
import com.example.easyteamup.Activities.UserHomeActivities.ViewProfileActivity;
import com.example.easyteamup.BooleanCallback;
import com.example.easyteamup.FirebaseOperations;
import com.example.easyteamup.R;
import com.example.easyteamup.User;

public class SignUpActivity extends AppCompatActivity implements SignUpInterface {

    private User user;
    private String password, uid;
    private ProgressBar loadingBar;
    private Button loginButton;
    private TextView invalidRegister, invalidDisplayPhone, invalidMajorGrad, invalidBio, invalidPhoto;
    FragmentManager fragmentManager;
    FirebaseOperations fops;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        loginButton = (Button) findViewById(R.id.login_button);
        loadingBar = (ProgressBar) findViewById(R.id.loading);
        loadingBar.setVisibility(View.INVISIBLE);

        invalidRegister = (TextView) findViewById(R.id.bad_signup);
        invalidRegister.setVisibility(View.INVISIBLE);

        invalidDisplayPhone = (TextView) findViewById(R.id.bad_display_phone);
        invalidDisplayPhone.setVisibility(View.INVISIBLE);

        invalidMajorGrad = (TextView) findViewById(R.id.bad_display_school);
        invalidMajorGrad.setVisibility(View.INVISIBLE);

        invalidBio = (TextView) findViewById(R.id.bad_bio);
        invalidBio.setVisibility(View.INVISIBLE);

        invalidPhoto = (TextView) findViewById(R.id.bad_photo);
        invalidPhoto.setVisibility(View.INVISIBLE);

        user = new User();
        fops = new FirebaseOperations();
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
        invalidRegister.setVisibility(View.INVISIBLE);
        loadingBar.setVisibility(View.VISIBLE);

        user.setEmail(email);
        password = psd;

        Log.d("Null check", String.valueOf(user));
        Log.d("Null check", password);

        fops.registerUser(user, password, new BooleanCallback() {
            @Override
            public void isTrue(boolean bool) {
                if (bool) {
                    uid = user.getUid();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, SecondSignUpFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("second fragment")
                            .commit();
                    loadingBar.setVisibility(View.INVISIBLE);
                } else {
                    loadingBar.setVisibility(View.INVISIBLE);
                    invalidRegister.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void onSecondContinue(boolean back, String firstName, String lastName, long phone){
        invalidDisplayPhone.setVisibility(View.INVISIBLE);
        loadingBar.setVisibility(View.VISIBLE);

        if (back) {
            fragmentManager.popBackStack();
        }
        else {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(phone);
            fops.setUser(user, uid, bool -> {
                if(bool){
                    loadingBar.setVisibility(View.INVISIBLE);
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, ThirdSignUpFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("third fragment")
                            .commit();
                }
                else {
                    loadingBar.setVisibility(View.INVISIBLE);
                    invalidDisplayPhone.setVisibility(View.VISIBLE);
                }
            });
        }

    }

    public void onThirdContinue(boolean back, String major, int year){
        invalidMajorGrad.setVisibility(View.INVISIBLE);
        loadingBar.setVisibility(View.VISIBLE);

        user.setMajor(major);
        user.setGraduationYear(year);

        if (back){
            fragmentManager.popBackStack();
        }
        else {
            if(major == null && year == 0){
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FourthSignUpFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("fourth fragment")
                        .commit();
            }
            else {
                fops.setUser(user, uid, bool -> {
                    if(bool){
                        loadingBar.setVisibility(View.INVISIBLE);
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, FourthSignUpFragment.class, null)
                                .setReorderingAllowed(true)
                                .addToBackStack("fourth fragment")
                                .commit();
                    }
                    else {
                        loadingBar.setVisibility(View.INVISIBLE);
                        invalidMajorGrad.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
        loadingBar.setVisibility(View.INVISIBLE);
    }

    public void onFourthContinue(boolean back, String bio){
        invalidBio.setVisibility(View.INVISIBLE);
        loadingBar.setVisibility(View.VISIBLE);

        user.setBio(bio);

        if (back){
            fragmentManager.popBackStack();
        }
        else {
            if(bio == null){
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FifthSignUpFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("fifth fragment")
                        .commit();
            }
            else {
                fops.setUser(user, uid, bool -> {
                    if(bool){
                        loadingBar.setVisibility(View.INVISIBLE);
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, FifthSignUpFragment.class, null)
                                .setReorderingAllowed(true)
                                .addToBackStack("fifth fragment")
                                .commit();
                    }
                    else {
                        loadingBar.setVisibility(View.INVISIBLE);
                        invalidBio.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
        loadingBar.setVisibility(View.INVISIBLE);
    }

    public void onFifthSubmit(boolean back, Uri image){
        invalidPhoto.setVisibility(View.INVISIBLE);
        loadingBar.setVisibility(View.VISIBLE);

        if(back){
            fragmentManager.popBackStack();
            loadingBar.setVisibility(View.INVISIBLE);
        }
        else {
            /*
                SET IMAGE IN FIREBASE STORAGE
             */
            if(image == null){
                Intent viewProfile = new Intent(SignUpActivity.this, ViewProfileActivity.class);
                viewProfile.putExtra("uid", uid);
                startActivity(viewProfile);
            }
            else {
                fops.uploadProfilePhoto(uid, image, bool -> {
                    if(bool){
                        loadingBar.setVisibility(View.INVISIBLE);
                        Intent viewProfile = new Intent(SignUpActivity.this, ViewProfileActivity.class);
                        viewProfile.putExtra("uid", uid);
                        startActivity(viewProfile);
                    }
                    else {
                        loadingBar.setVisibility(View.INVISIBLE);
                        invalidPhoto.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
    }
}