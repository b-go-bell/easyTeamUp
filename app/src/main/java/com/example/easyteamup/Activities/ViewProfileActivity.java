package com.example.easyteamup.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.easyteamup.Activities.UserAuthActivities.SignUpActivityFolder.FirstSignUpFragment;
import com.example.easyteamup.FirebaseOperations;
import com.example.easyteamup.R;
import com.example.easyteamup.User;

public class ViewProfileActivity extends AppCompatActivity implements SnackBarInterface {

    private Intent getIntent;
    private User user;
    private FirebaseOperations fops;
    FragmentManager fragmentManager;

    private TextView welcome;
    private Button private_events_map;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        getSupportActionBar().hide();
        fops = new FirebaseOperations();

        private_events_map = (Button) findViewById(R.id.private_events_map_button);


        getIntent = getIntent();
        String uid = getIntent.getStringExtra("uid");
        Log.d("uid in viewProfile: ", uid);
        fops.getUserByUid(uid, userObject -> {
            user = (User) userObject;
        });

        Log.d("user: ", String.valueOf(user));

        String formatted_welcome  = getString(R.string.welcome, user.getFirstName());

        welcome.setText(formatted_welcome);

        fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.snackbar_container, SnackBarFragment.class, bundle)
                .addToBackStack("snackbar")
                .commit();
    }

    public void viewPublicMapEvents(){}
    public void viewPublicListEvents(){}
    public void createEvent(){}
    public void viewUserProfile(){}
    public void viewUserHistory(){}
}