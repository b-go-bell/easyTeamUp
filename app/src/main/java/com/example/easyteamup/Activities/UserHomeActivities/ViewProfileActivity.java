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
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.R;
import com.example.easyteamup.Backend.User;

public class ViewProfileActivity extends AppCompatActivity implements SnackBarInterface {

    private Intent getIntent;
    private String uid;
    private FirebaseOperations fops;
    FragmentManager fragmentManager;

    private TextView welcome;
    private ProgressBar loadingBar;
    private Button private_events_map, private_events_list, public_events_map, public_events_list;
    private Button attending_map, attending_list, hosting_map, hosting_list;
    private Button create_event, event_history, update_profile;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        getSupportActionBar().hide();
        fops = new FirebaseOperations(this);

        welcome = (TextView) findViewById(R.id.welcome);
        loadingBar = (ProgressBar)  findViewById(R.id.loading);
        loadingBar.setVisibility(View.INVISIBLE);

        private_events_map = (Button) findViewById(R.id.private_events_map_button);
        private_events_list = (Button) findViewById(R.id.private_events_list_button);
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

        private_events_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent viewPrivateEventsMap = new Intent(ViewProfileActivity.this, ViewMapEventsActivity.class);
                viewPrivateEventsMap.putExtra("uid", uid);
                viewPrivateEventsMap.putExtra("hosting", -1);
                viewPrivateEventsMap.putExtra("attending", -1);
                viewPrivateEventsMap.putExtra("invited", 1);
                startActivity(viewPrivateEventsMap);
            }
        });

        private_events_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent viewPrivateEventsList = new Intent(ViewProfileActivity.this, ViewListEventsActivity.class);
                viewPrivateEventsList.putExtra("uid", uid);
                viewPrivateEventsList.putExtra("hosting", -1);
                viewPrivateEventsList.putExtra("attending", -1);
                viewPrivateEventsList.putExtra("invited", 1);
                startActivity(viewPrivateEventsList);
            }
        });

        public_events_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent viewPublicEventsMap = new Intent(ViewProfileActivity.this, ViewMapEventsActivity.class);
                viewPublicEventsMap.putExtra("uid", uid);
                viewPublicEventsMap.putExtra("hosting", -1);
                viewPublicEventsMap.putExtra("attending", -1);
                viewPublicEventsMap.putExtra("invited", 0);
                startActivity(viewPublicEventsMap);
            }
        });

        public_events_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent viewPublicEventsList = new Intent(ViewProfileActivity.this, ViewListEventsActivity.class);
                viewPublicEventsList.putExtra("uid", uid);
                viewPublicEventsList.putExtra("hosting", false);
                viewPublicEventsList.putExtra("attending", -1);
                viewPublicEventsList.putExtra("invited", 0);
                startActivity(viewPublicEventsList);
            }
        });

        attending_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent viewAttendingEventsMap = new Intent(ViewProfileActivity.this, ViewMapEventsActivity.class);
                viewAttendingEventsMap.putExtra("uid", uid);
                viewAttendingEventsMap.putExtra("hosting", false);
                viewAttendingEventsMap.putExtra("attending", 1);
                viewAttendingEventsMap.putExtra("invited", -1);
                startActivity(viewAttendingEventsMap);
            }
        });

        attending_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent viewAttendingEventsList = new Intent(ViewProfileActivity.this, ViewListEventsActivity.class);
                viewAttendingEventsList.putExtra("uid", uid);
                viewAttendingEventsList.putExtra("hosting", false);
                viewAttendingEventsList.putExtra("attending", 1);
                viewAttendingEventsList.putExtra("invited", -1);
                startActivity(viewAttendingEventsList);
            }
        });

        hosting_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent viewHostedEventsMap = new Intent(ViewProfileActivity.this, ViewMapEventsActivity.class);
                viewHostedEventsMap.putExtra("uid", uid);
                viewHostedEventsMap.putExtra("hosting", true);
                viewHostedEventsMap.putExtra("attending", -1);
                viewHostedEventsMap.putExtra("invited", -1);
                startActivity(viewHostedEventsMap);
            }
        });

        hosting_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent viewHostedEventsList = new Intent(ViewProfileActivity.this, ViewListEventsActivity.class);
                viewHostedEventsList.putExtra("uid", uid);
                viewHostedEventsList.putExtra("hosting", true);
                viewHostedEventsList.putExtra("attending", -1);
                viewHostedEventsList.putExtra("invited", -1);
                startActivity(viewHostedEventsList);

            }
        });

        create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent createEvent = new Intent(ViewProfileActivity.this, CreateEventActivity.class);
                createEvent.putExtra("uid", uid);
                startActivity(createEvent);
            }
        });

        event_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent viewEventHistory = new Intent(ViewProfileActivity.this, ViewEventAnalyticsActivity.class);
                viewEventHistory.putExtra("uid", uid);
                startActivity(viewEventHistory);
            }
        });

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
        viewPublicEventsMap.putExtra("hosting", -1);
        viewPublicEventsMap.putExtra("attending", -1);
        viewPublicEventsMap.putExtra("invited", 0);
        startActivity(viewPublicEventsMap);
    }

    public void viewPublicListEvents(){
        loadingBar.setVisibility(View.VISIBLE);
        Intent viewPublicEventsList = new Intent(ViewProfileActivity.this, ViewListEventsActivity.class);
        viewPublicEventsList.putExtra("uid", uid);
        viewPublicEventsList.putExtra("hosting", false);
        viewPublicEventsList.putExtra("attending", -1);
        viewPublicEventsList.putExtra("invited", 0);
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