package com.example.easyteamup.Activities.ViewEventActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.example.easyteamup.Activities.CreateEventActivities.CreateEventActivity;
import com.example.easyteamup.Activities.SnackBarActivity.SnackBarFragment;
import com.example.easyteamup.Activities.SnackBarActivity.SnackBarInterface;
import com.example.easyteamup.Activities.UserHomeActivities.ViewEventAnalyticsActivity;
import com.example.easyteamup.Activities.UserHomeActivities.ViewProfileActivity;
import com.example.easyteamup.Activities.ViewEventActivities.DisplayEventHelpers.EventAdapter;
import com.example.easyteamup.Activities.ViewEventActivities.DisplayEventHelpers.NoEventsFragment;
import com.example.easyteamup.Backend.Event;
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.R;
import com.firebase.geofire.GeoLocation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewPublicEventsActivity extends AppCompatActivity implements SnackBarInterface {

    private Intent getIntent;
    private FirebaseOperations fops;
    private FragmentManager fragmentManager;

    private String uid;
    private String locationName;
    private double latitude;
    private double longitude;
    private double radius;
    private boolean map;

    private ListView listEvents;
    private FragmentContainerView noEvents;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_public_events);
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


        uid = getIntent.getStringExtra("uid");
        locationName = getIntent.getStringExtra("locName");
        latitude = getIntent.getDoubleExtra("lat", 34.0224);
        longitude = getIntent.getDoubleExtra("lon", -118.2851);
        radius = getIntent.getDoubleExtra("radius", 10);
        map = getIntent.getBooleanExtra("map", false);

        //set up views
        listEvents = (ListView) findViewById(R.id.events_list);
        noEvents = (FragmentContainerView) findViewById(R.id.no_events_container);

        if(map){
            viewEventsOnMap();
        }
        else {
            viewEventsInList();
        }
    }

    private void viewEventsOnMap() {
        //Fill in
    }

    private void viewEventsInList() {
        GeoLocation geoloc = new GeoLocation(latitude, longitude);
        fops.getPublicEvents(geoloc, radius, listObject -> {
            try {
                ArrayList<String> eventIds = (ArrayList<String>) listObject;
                if(eventIds.size() == 0){
                    throw new NullPointerException();
                }

                listEvents.setVisibility(View.VISIBLE);
                noEvents.setVisibility(View.INVISIBLE);
            }
            catch (NullPointerException npe){
                showNoEvents();
            }
        });
    }

    private void showNoEvents() {
        listEvents.setVisibility(View.INVISIBLE);
        noEvents.setVisibility(View.VISIBLE);

        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        bundle.putBoolean("map", false);
        bundle.putString("none", "public");
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.no_events_container, NoEventsFragment.class, bundle)
                .addToBackStack("none_list")
                .commit();
    }

    public void viewPublicEvents(){
        Intent viewPublicEvents = new Intent(ViewPublicEventsActivity.this, EventDispatcherActivity.class);
        viewPublicEvents.putExtra("uid", uid);
        viewPublicEvents.putExtra("event_type", "public");
        startActivity(viewPublicEvents);
    }

    public void viewInvitations(){
        Intent viewInvitations = new Intent(ViewPublicEventsActivity.this, EventDispatcherActivity.class);
        viewInvitations.putExtra("uid", uid);
        viewInvitations.putExtra("event_type", "invited");
        startActivity(viewInvitations);
    }

    public void createEvent(){
        Intent createEvent = new Intent(ViewPublicEventsActivity.this, CreateEventActivity.class);
        createEvent.putExtra("uid", uid);
        startActivity(createEvent);
    }

    public void viewUserProfile(){
        Intent viewUserProfile = new Intent(ViewPublicEventsActivity.this, ViewProfileActivity.class);
        viewUserProfile.putExtra("uid", uid);
        startActivity(viewUserProfile);
    }

    public void viewUserHistory(){
        Intent viewEventHistory = new Intent(ViewPublicEventsActivity.this, ViewEventAnalyticsActivity.class);
        viewEventHistory.putExtra("uid", uid);
        startActivity(viewEventHistory);
    }
}