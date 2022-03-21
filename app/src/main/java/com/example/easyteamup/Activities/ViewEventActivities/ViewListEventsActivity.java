package com.example.easyteamup.Activities.ViewEventActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
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
    private int numResults = 50; //defaults to 50 events being displayed
    private String sortBy = "proximity"; //defaults to events listed in order of closeness to geolocation

    private String uid; //uid
    private String event_type;
    private boolean hosting; //either hosting or not hosting event
    private int attending; //1 means attending, 0 means not RSVPd, -1 means either
    private int invited; //1 means public, 0 means private, -1 means either

    List<Event> eventList = new ArrayList<>(); //list of events

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

        getEvents();
    }

    @Override
    public void getEvents() {
        if(event_type.equals("invited")) { //displays events user is invited to, regardless of status
            fops.getInvitedEvents(uid, listObject -> {
                try{
                    Map<String, String> mapEvents = (Map<String, String>) listObject;
                    ArrayList<Pair<String, String>> events = new ArrayList<>();

                    Iterator<Map.Entry<String, String>> it = mapEvents.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, String> event = it.next();
                        String eid = event.getKey();
                        String status = event.getValue();
                        events.add(new Pair(eid, status));
                    }
                    Log.d("location", "invited has items");
                    Bundle bundle = new Bundle();
                    fragmentManager.beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.list_container, EventsListFragment.class, bundle)
                            .addToBackStack("invited_list")
                            .commit();
                }
                catch(NullPointerException npe) {
                    Bundle bundle = new Bundle();
                    bundle.putString("uid", uid);
                    bundle.putBoolean("map", false);
                    bundle.putString("none", event_type);
                    Log.d("location", "NPE for invited");
                    fragmentManager.beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.list_container, NoEventsFragment.class, bundle)
                            .addToBackStack("none_invited_list")
                            .commit();
                }
            });
        } else if (event_type.equals("public")) { //displays public events
            /*
                TO DO
             */
        } else if (event_type.equals("attending")) { //displays events user is registered to attend
            fops.getRSVPedEvents(uid, listObject -> {
                try {
                    List<String> events = (List<String>) listObject;
                    for (int i = 0; i < events.size(); i++) {
                        String eid = events.get(i);
                        Log.d("location", "attending has items");
                        Bundle bundle = new Bundle();
                        fragmentManager.beginTransaction()
                                .setReorderingAllowed(true)
                                .add(R.id.list_container, EventsListFragment.class, bundle)
                                .addToBackStack("attending_list")
                                .commit();
                    }
                }
                catch(NullPointerException npe) {
                    Bundle bundle = new Bundle();
                    bundle.putString("uid", uid);
                    bundle.putBoolean("map", false);
                    bundle.putString("none", event_type);
                    Log.d("location", "NPE for attending");
                    fragmentManager.beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.list_container, NoEventsFragment.class, bundle)
                            .addToBackStack("none_attending_list")
                            .commit();
                }
            });
        } else if (event_type.equals("hosting")) { //displays events user is hosting
            /*
                TO DO
             */
        }
    }

    public void viewPublicMapEvents(){ ;
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