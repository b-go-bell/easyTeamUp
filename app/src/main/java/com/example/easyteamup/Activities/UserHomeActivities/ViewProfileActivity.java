package com.example.easyteamup.Activities.UserHomeActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.easyteamup.Activities.CreateEventActivities.CreateEventActivity;
import com.example.easyteamup.Activities.CreateEventActivities.LeaveCreateEventDialogFragment;
import com.example.easyteamup.Activities.SnackBarActivity.SnackBarFragment;
import com.example.easyteamup.Activities.SnackBarActivity.SnackBarInterface;
import com.example.easyteamup.Activities.ViewEventActivities.EventDispatcherActivity;
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.R;
import com.example.easyteamup.Backend.User;

public class ViewProfileActivity extends AppCompatActivity implements SnackBarInterface {

    private Intent getIntent;
    private String uid;
    private FirebaseOperations fops;
    FragmentManager fragmentManager;

    private TextView welcome;
    private Button invited_events,  public_events;
    private Button attending, hosting, create_event;
    private Button event_history, update_profile, logout;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        getSupportActionBar().hide();
        fops = new FirebaseOperations(this);

        welcome = (TextView) findViewById(R.id.welcome);

        invited_events = (Button) findViewById(R.id.invited_events_button);
        public_events = (Button) findViewById(R.id.public_events_button);

        attending = (Button) findViewById(R.id.attending_button);
        hosting = (Button) findViewById(R.id.hosting_button);

        create_event = (Button) findViewById(R.id.create_event_button);
        event_history = (Button) findViewById(R.id.event_history_button);
        update_profile = (Button) findViewById(R.id.update_profile_button);
        logout = (Button) findViewById(R.id.logout_button);

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

        //invitations, default to list
        invited_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewPrivateEventsMap = new Intent(ViewProfileActivity.this, EventDispatcherActivity.class);
                viewPrivateEventsMap.putExtra("uid", uid);
                viewPrivateEventsMap.putExtra("event_type", "invited");
                startActivity(viewPrivateEventsMap);
            }
        });


        //public events, default to list
        public_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewPublicEventsMap = new Intent(ViewProfileActivity.this, EventDispatcherActivity.class);
                viewPublicEventsMap.putExtra("uid", uid);
                viewPublicEventsMap.putExtra("event_type", "public");
                startActivity(viewPublicEventsMap);
            }
        });

        //RSVPd events, default to list
        attending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewAttendingEventsMap = new Intent(ViewProfileActivity.this, EventDispatcherActivity.class);
                viewAttendingEventsMap.putExtra("uid", uid);
                viewAttendingEventsMap.putExtra("event_type", "attending");
                startActivity(viewAttendingEventsMap);
            }
        });

        //hosted events, default to list
        hosting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewHostedEventsMap = new Intent(ViewProfileActivity.this, EventDispatcherActivity.class);
                viewHostedEventsMap.putExtra("uid", uid);
                viewHostedEventsMap.putExtra("event_type", "hosting");
                startActivity(viewHostedEventsMap);
            }
        });


        //create an event
        create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createEvent = new Intent(ViewProfileActivity.this, CreateEventActivity.class);
                createEvent.putExtra("uid", uid);
                startActivity(createEvent);
            }
        });

        //view event analytics
        event_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewEventHistory = new Intent(ViewProfileActivity.this, ViewEventAnalyticsActivity.class);
                viewEventHistory.putExtra("uid", uid);
                startActivity(viewEventHistory);
            }
        });

        //update profile
        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateProfile = new Intent(ViewProfileActivity.this, UpdateProfileActivity.class);
                updateProfile.putExtra("uid", uid);
                startActivity(updateProfile);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutDialogFragment logout = LogoutDialogFragment.newInstance(uid);
                logout.show(fragmentManager, "fragment_logout");
            }
        });
    }

    public void viewPublicEvents(){
        Intent viewPublicEvents = new Intent(ViewProfileActivity.this, EventDispatcherActivity.class);
        viewPublicEvents.putExtra("uid", uid);
        viewPublicEvents.putExtra("event_type", "public");
        startActivity(viewPublicEvents);
    }

    public void viewInvitations(){
        Intent viewInvitations = new Intent(ViewProfileActivity.this, EventDispatcherActivity.class);
        viewInvitations.putExtra("uid", uid);
        viewInvitations.putExtra("event_type", "invited");
        startActivity(viewInvitations);
    }

    public void createEvent(){
        Intent createEvent = new Intent(ViewProfileActivity.this, CreateEventActivity.class);
        createEvent.putExtra("uid", uid);
        startActivity(createEvent);
    }

    public void viewUserProfile(){}

    public void viewUserHistory(){
        Intent viewEventHistory = new Intent(ViewProfileActivity.this, ViewEventAnalyticsActivity.class);
        viewEventHistory.putExtra("uid", uid);
        startActivity(viewEventHistory);
    }
}