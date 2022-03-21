package com.example.easyteamup.Activities.ViewEventActivities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.example.easyteamup.Activities.CreateEventActivities.CreateEventActivity;
import com.example.easyteamup.Activities.SnackBarActivity.SnackBarFragment;
import com.example.easyteamup.Activities.SnackBarActivity.SnackBarInterface;
import com.example.easyteamup.Activities.UserHomeActivities.ViewEventAnalyticsActivity;
import com.example.easyteamup.Activities.UserHomeActivities.ViewProfileActivity;
import com.example.easyteamup.Backend.Event;
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.R;
import com.firebase.geofire.GeoLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ViewListEventsActivity extends AppCompatActivity implements ViewEventsActivity, SnackBarInterface {

    private Intent getIntent;
    private FirebaseOperations fops;
    private FragmentManager fragmentManager;

    private GeoLocation geolocation = new GeoLocation(34.0224, 118.2851); //defaults to USC
    private int radius = 10; //defaults to 10 mile radius

    // feature to add
    private String sortBy = "proximity"; //defaults to events listed in order of closeness to geolocation

    private String uid; //uid
    private String event_type;

    private EventAdapter adapter;
    private ListView listEvents;
    private FragmentContainerView noEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_events);
        getSupportActionBar().hide();
        fops = new FirebaseOperations(this);

        fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.snackbar_container, SnackBarFragment.class, bundle)
                .addToBackStack("snackbar")
                .commit();

        getIntent = getIntent();
        uid = getIntent.getStringExtra("uid");
        event_type = getIntent.getStringExtra("event_type");

        listEvents = (ListView) findViewById(R.id.events_list);
        noEvents = (FragmentContainerView) findViewById(R.id.no_events_container);

        getEvents();
    }

    @Override
    public void getEvents() {
        if(event_type.equals("invited")) { //displays events user is invited to, regardless of status
            fops.getInvitedEvents(uid, listObject -> {
                try{
                    listEvents.setVisibility(View.VISIBLE);
                    noEvents.setVisibility(View.INVISIBLE);

                    Map<String, String> mapEvents = (Map<String, String>) listObject;
                    ArrayList<String> events = new ArrayList<>();
                    ArrayList<String> events_status = new ArrayList<>();

                    Iterator<Map.Entry<String, String>> it = mapEvents.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, String> event = it.next();
                        String eid = event.getKey();
                        String status = event.getValue();
                        events.add(eid);
                        events_status.add(status);
                    }
                    adapter = new EventAdapter(this, events, events_status);
                    listEvents.setAdapter(adapter);
                }
                catch(NullPointerException npe) {
                    listEvents.setVisibility(View.INVISIBLE);
                    noEvents.setVisibility(View.VISIBLE);

                    Bundle bundle = new Bundle();
                    bundle.putString("uid", uid);
                    bundle.putBoolean("map", false);
                    bundle.putString("none", event_type);
                    fragmentManager.beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.no_events_container, NoEventsFragment.class, bundle)
                            .addToBackStack("none_invited_list")
                            .commit();
                }
            });
        } else if (event_type.equals("public")) { //displays public events
            fops.getPublicEvents(geolocation, radius, listObject ->{
                try {
                    listEvents.setVisibility(View.VISIBLE);
                    noEvents.setVisibility(View.INVISIBLE);

                    ArrayList<String> events = (ArrayList<String>) listObject;
                    ArrayList<String> events_status = new ArrayList<>();

                    for (int i = 0; i < events.size(); i++) {
                        String eid = events.get(i);
                        events.add(eid);
                        events_status.add("pending");
                    }

                    adapter = new EventAdapter(this, events, events_status);
                    listEvents.setAdapter(adapter);

                    showPublicEventsDialog();
                }
                catch(NullPointerException npe) {
                    listEvents.setVisibility(View.INVISIBLE);
                    noEvents.setVisibility(View.VISIBLE);

                    Bundle bundle = new Bundle();
                    bundle.putString("uid", uid);
                    bundle.putBoolean("map", false);
                    bundle.putString("none", event_type);
                    fragmentManager.beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.no_events_container, NoEventsFragment.class, bundle)
                            .addToBackStack("none_public_list")
                            .commit();
                }
            });
        } else if (event_type.equals("attending")) { //displays events user is registered to attend
            fops.getRSVPedEvents(uid, listObject -> {
                try {
                    listEvents.setVisibility(View.VISIBLE);
                    noEvents.setVisibility(View.INVISIBLE);

                    ArrayList<String> events = (ArrayList<String>) listObject;
                    ArrayList<String> events_status = new ArrayList<>();
                    for (int i = 0; i < events.size(); i++) {
                        String eid = events.get(i);
                        events.add(eid);
                        events_status.add("attending");
                    }

                    adapter = new EventAdapter(this, events, events_status);
                    listEvents.setAdapter(adapter);
                }
                catch(NullPointerException npe) {
                    listEvents.setVisibility(View.INVISIBLE);
                    noEvents.setVisibility(View.VISIBLE);

                    Bundle bundle = new Bundle();
                    bundle.putString("uid", uid);
                    bundle.putBoolean("map", false);
                    bundle.putString("none", event_type);
                    fragmentManager.beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.no_events_container, NoEventsFragment.class, bundle)
                            .addToBackStack("none_attending_list")
                            .commit();
                }
            });
        } else if (event_type.equals("hosting")) { //displays events user is hosting
            fops.getHostedEvents(uid, listObject -> {
                try {
                    listEvents.setVisibility(View.VISIBLE);
                    noEvents.setVisibility(View.INVISIBLE);

                    ArrayList<String> events = (ArrayList<String>) listObject;
                    ArrayList<String> events_status = new ArrayList<>();
                    for (int i = 0; i < events.size(); i++) {
                        String eid = events.get(i);
                        events.add(eid);
                        events_status.add("hosting");
                    }

                    adapter = new EventAdapter(this, events, events_status);
                    listEvents.setAdapter(adapter);
                }
                catch (NullPointerException npe) {
                    listEvents.setVisibility(View.INVISIBLE);
                    noEvents.setVisibility(View.VISIBLE);

                    Bundle bundle = new Bundle();
                    bundle.putString("uid", uid);
                    bundle.putBoolean("map", false);
                    bundle.putString("none", event_type);
                    fragmentManager.beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.no_events_container, NoEventsFragment.class, bundle)
                            .addToBackStack("none_hosting_list")
                            .commit();
                }
            });
        }
    }

    public void showPublicEventsDialog() {
        
    }

    public void viewPublicMapEvents(){
        Intent viewPublicEventsMap = new Intent(ViewListEventsActivity.this, ViewMapEventsActivity.class);
        viewPublicEventsMap.putExtra("uid", uid);
        viewPublicEventsMap.putExtra("event_type", "public");
        startActivity(viewPublicEventsMap);
    }

    public void viewPublicListEvents(){
        Intent viewPublicEventsList = new Intent(ViewListEventsActivity.this, ViewListEventsActivity.class);
        viewPublicEventsList.putExtra("uid", uid);
        viewPublicEventsList.putExtra("event_type", "public");
        startActivity(viewPublicEventsList);
    }

    public void createEvent(){
        Intent createEvent = new Intent(ViewListEventsActivity.this, CreateEventActivity.class);
        createEvent.putExtra("uid", uid);
        startActivity(createEvent);
    }

    public void viewUserProfile(){
        Intent userProfile = new Intent(ViewListEventsActivity.this, ViewProfileActivity.class);
        userProfile.putExtra("uid", uid);
        startActivity(userProfile);
    }

    public void viewUserHistory(){
        Intent viewEventHistory = new Intent(ViewListEventsActivity.this, ViewEventAnalyticsActivity.class);
        viewEventHistory.putExtra("uid", uid);
        startActivity(viewEventHistory);
    }
}