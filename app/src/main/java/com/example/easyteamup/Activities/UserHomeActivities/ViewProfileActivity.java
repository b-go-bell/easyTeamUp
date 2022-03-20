package com.example.easyteamup.Activities.UserHomeActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.easyteamup.Activities.CreateEventActivities.CreateEventActivity;
import com.example.easyteamup.Activities.SnackBarActivity.SnackBarFragment;
import com.example.easyteamup.Activities.SnackBarActivity.SnackBarInterface;
import com.example.easyteamup.Activities.ViewEventActivities.ViewListEventsActivity;
import com.example.easyteamup.Activities.ViewEventActivities.ViewMapEventsActivity;
import com.example.easyteamup.FirebaseOperations;
import com.example.easyteamup.R;
import com.example.easyteamup.User;

public class ViewProfileActivity extends AppCompatActivity implements SnackBarInterface {

    private Intent getIntent;
    private String uid;
    private FirebaseOperations fops;
    FragmentManager fragmentManager;

    private TextView welcome;
    private ProgressBar loadingBar;
    private Button invited_events_map, invited_events_list, public_events_map, public_events_list;
    private Button attending_map, attending_list, hosting_map, hosting_list;
    private Button create_event, event_history, update_profile;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        getSupportActionBar().hide();
        fops = new FirebaseOperations();

        welcome = (TextView) findViewById(R.id.welcome);
        loadingBar = (ProgressBar)  findViewById(R.id.loading);
        loadingBar.setVisibility(View.INVISIBLE);

        invited_events_map = (Button) findViewById(R.id.private_events_map_button);
        invited_events_list = (Button) findViewById(R.id.private_events_list_button);
        public_events_map = (Button) findViewById(R.id.public_events_map_button);
        public_events_list = (Button) findViewById(R.id.public_events_list_button);

        attending_map = (Button) findViewById(R.id.attending_map_button);
        attending_list = (Button) findViewById(R.id.attending_list_button);
        hosting_map = (Button) findViewById(R.id.hosting_map_button);
        hosting_list = (Button) findViewById(R.id.hosting_list_button);

        create_event = (Button) findViewById(R.id.create_event_button);
        event_history = (Button) findViewById(R.id.event_history_button);
        update_profile = (Button) findViewById(R.id.update_profile_button);

        fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.snackbar_container, SnackBarFragment.class, bundle)
                .commit();

        getIntent = getIntent();
        uid = getIntent.getStringExtra("uid");
        fops.getUserByUid(uid, userObject -> {
            User user = (User) userObject;

            String formatted_welcome  = getString(R.string.welcome, user.getFirstName());
            welcome.setText(formatted_welcome);
        });

        //invitations on the map
        invited_events_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent viewPrivateEventsMap = new Intent(ViewProfileActivity.this, ViewMapEventsActivity.class);
                viewPrivateEventsMap.putExtra("uid", uid);
                viewPrivateEventsMap.putExtra("event_type", "invited");
                startActivity(viewPrivateEventsMap);
            }
        });

        //invitations in a list
        invited_events_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent viewPrivateEventsList = new Intent(ViewProfileActivity.this, ViewListEventsActivity.class);
                viewPrivateEventsList.putExtra("uid", uid);
                viewPrivateEventsList.putExtra("event_type", "invited");
                startActivity(viewPrivateEventsList);
            }
        });

        //public events on the map
        public_events_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent viewPublicEventsMap = new Intent(ViewProfileActivity.this, ViewMapEventsActivity.class);
                viewPublicEventsMap.putExtra("uid", uid);
                viewPublicEventsMap.putExtra("event_type", "public");
                startActivity(viewPublicEventsMap);
            }
        });

        //public events in a list
        public_events_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent viewPublicEventsList = new Intent(ViewProfileActivity.this, ViewListEventsActivity.class);
                viewPublicEventsList.putExtra("uid", uid);
                viewPublicEventsList.putExtra("event_type", "public");
                startActivity(viewPublicEventsList);
            }
        });

        //RSVPd events on the map
        attending_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent viewAttendingEventsMap = new Intent(ViewProfileActivity.this, ViewMapEventsActivity.class);
                viewAttendingEventsMap.putExtra("uid", uid);
                viewAttendingEventsMap.putExtra("event_type", "attending");
                startActivity(viewAttendingEventsMap);
            }
        });

        //RSVPd events in a list
        attending_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent viewAttendingEventsList = new Intent(ViewProfileActivity.this, ViewListEventsActivity.class);
                viewAttendingEventsList.putExtra("uid", uid);
                viewAttendingEventsList.putExtra("event_type", "attending");
                startActivity(viewAttendingEventsList);
            }
        });

        //hosted events on the map
        hosting_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent viewHostedEventsMap = new Intent(ViewProfileActivity.this, ViewMapEventsActivity.class);
                viewHostedEventsMap.putExtra("uid", uid);
                viewHostedEventsMap.putExtra("event_type", "hosting");
                startActivity(viewHostedEventsMap);
            }
        });

        //hosted events in a list
        hosting_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent viewHostedEventsList = new Intent(ViewProfileActivity.this, ViewListEventsActivity.class);
                viewHostedEventsList.putExtra("uid", uid);
                viewHostedEventsList.putExtra("event_type", "hosting");
                startActivity(viewHostedEventsList);
            }
        });

        //create an event
        create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent createEvent = new Intent(ViewProfileActivity.this, CreateEventActivity.class);
                createEvent.putExtra("uid", uid);
                startActivity(createEvent);
            }
        });

        //view event analytics
        event_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent viewEventHistory = new Intent(ViewProfileActivity.this, ViewEventAnalyticsActivity.class);
                viewEventHistory.putExtra("uid", uid);
                startActivity(viewEventHistory);
            }
        });

        //update profile
        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent updateProfile = new Intent(ViewProfileActivity.this, UpdateProfileActivity.class);
                updateProfile.putExtra("uid", uid);
                startActivity(updateProfile);
            }
        });
    }

    public void viewPublicMapEvents(){
        loadingBar.setVisibility(View.VISIBLE);
        Intent viewPublicEventsMap = new Intent(ViewProfileActivity.this, ViewMapEventsActivity.class);
        viewPublicEventsMap.putExtra("uid", uid);
        viewPublicEventsMap.putExtra("event_type", "public");
        startActivity(viewPublicEventsMap);
    }

    public void viewPublicListEvents(){
        loadingBar.setVisibility(View.VISIBLE);
        Intent viewPublicEventsList = new Intent(ViewProfileActivity.this, ViewListEventsActivity.class);
        viewPublicEventsList.putExtra("uid", uid);
        viewPublicEventsList.putExtra("event_type", "public");
        startActivity(viewPublicEventsList);
    }

    public void createEvent(){
        loadingBar.setVisibility(View.VISIBLE);
        Intent createEvent = new Intent(ViewProfileActivity.this, CreateEventActivity.class);
        createEvent.putExtra("uid", uid);
        startActivity(createEvent);
    }

    public void viewUserProfile(){}

    public void viewUserHistory(){
        loadingBar.setVisibility(View.VISIBLE);
        Intent viewEventHistory = new Intent(ViewProfileActivity.this, ViewEventAnalyticsActivity.class);
        viewEventHistory.putExtra("uid", uid);
        startActivity(viewEventHistory);
    }
}