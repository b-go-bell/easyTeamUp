package com.example.easyteamup.Activities.EventActivities.ViewEventActivities.ListEventActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.easyteamup.Activities.EventActivities.CreateEventActivities.CreateEventActivity;
import com.example.easyteamup.Activities.EventActivities.ViewEventActivities.DisplayEventHelpers.EventAdapter;
import com.example.easyteamup.Activities.EventActivities.ViewEventActivities.EventDispatcherActivity;
import com.example.easyteamup.Activities.SnackBarActivities.SnackBarFragment;
import com.example.easyteamup.Activities.SnackBarActivities.SnackBarInterface;
import com.example.easyteamup.Activities.UserHomeActivities.ViewProfileActivity;
import com.example.easyteamup.Activities.EventActivities.ViewEventActivities.DisplayEventHelpers.NoEventsFragment;
import com.example.easyteamup.Activities.EventActivities.ViewEventActivities.MapEventActivities.MapHostedEventsActivity;
import com.example.easyteamup.Backend.Event;
import com.example.easyteamup.R;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.easyteamup.Backend.FirebaseOperations;

import java.util.ArrayList;

public class ViewHostedEventsActivity extends AppCompatActivity implements SnackBarInterface {

    private Intent getIntent;
    private FirebaseOperations fops;
    private FragmentManager fragmentManager;

    private String uid;

    private ListView listEvents;
    private Button mapButton;
    private FragmentContainerView noEvents;
    private EventAdapter eventAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hosted_events);
        getSupportActionBar().hide();

        fops = new FirebaseOperations(this);
        fragmentManager = getSupportFragmentManager();
        getIntent = getIntent();

        Bundle bundle = new Bundle();
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.snackbar_container, SnackBarFragment.class, bundle)
                .addToBackStack("snackbar")
                .commit();

        listEvents = (ListView) findViewById(R.id.events_list);
        noEvents = (FragmentContainerView) findViewById(R.id.no_events_container);
        mapButton = (Button) findViewById(R.id.map_button);

        uid = getIntent.getStringExtra("uid");

        mapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewEventsOnMap();
            }
        });

        viewEventsInList();
    }

    private void viewEventsOnMap() {
        Intent viewHostedMapEvents = new Intent(ViewHostedEventsActivity.this, MapHostedEventsActivity.class);
        viewHostedMapEvents.putExtra("uid", uid);
        startActivity(viewHostedMapEvents);
    }

    private void viewEventsInList() {
        fops.getHostedEvents(uid, listObject -> {
            try {
                ArrayList<String> eventIds = (ArrayList<String>) listObject;
                if(eventIds.size() == 0){
                    Log.d("EVENT IDS 0", "npe here");
                    throw new NullPointerException();
                }

                fops.getEventsByEventId(eventIds, eventList -> {
                    try {
                        ArrayList<Event> events = (ArrayList<Event>) eventList;
                        Log.d("EVENTS", String.valueOf(events));
                        eventAdapter = new EventAdapter(this, uid, "hosted", events);

                        listEvents.setAdapter(eventAdapter);

                        listEvents.setVisibility(View.VISIBLE);
                        noEvents.setVisibility(View.INVISIBLE);
                    }
                    catch(NullPointerException npe){
                        Log.d("GET EVENTS", "npe here");
                        showNoEvents();
                    }
                });
            }
            catch (NullPointerException npe) {
                Log.d("GET HOSTED", "npe here");
                showNoEvents();
            }
        });
    }

    private void showNoEvents() {
        listEvents.setVisibility(View.INVISIBLE);
        noEvents.setVisibility(View.VISIBLE);

        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        bundle.putString("none", "hosting");
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.no_events_container, NoEventsFragment.class, bundle)
                .addToBackStack("none_list")
                .commit();
    }

    public void viewPublicEvents(){
        Intent viewPublicEvents = new Intent(ViewHostedEventsActivity.this, EventDispatcherActivity.class);
        viewPublicEvents.putExtra("uid", uid);
        viewPublicEvents.putExtra("event_type", "public");
        startActivity(viewPublicEvents);
    }

    public void viewInvitations(){
        Intent viewInvitations = new Intent(ViewHostedEventsActivity.this, EventDispatcherActivity.class);
        viewInvitations.putExtra("uid", uid);
        viewInvitations.putExtra("event_type", "invited");
        startActivity(viewInvitations);
    }

    public void createEvent(){
        Intent createEvent = new Intent(ViewHostedEventsActivity.this, CreateEventActivity.class);
        createEvent.putExtra("uid", uid);
        startActivity(createEvent);
    }

    public void viewUserProfile(){
        Intent viewUserProfile = new Intent(ViewHostedEventsActivity.this, ViewProfileActivity.class);
        viewUserProfile.putExtra("uid", uid);
        startActivity(viewUserProfile);
    }

    public void viewUserHistory(){
        Intent viewEventHistory = new Intent(ViewHostedEventsActivity.this, ViewEventAnalyticsActivity.class);
        viewEventHistory.putExtra("uid", uid);
        startActivity(viewEventHistory);
    }
}