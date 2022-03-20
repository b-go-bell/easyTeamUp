package com.example.easyteamup.Activities.ViewEventActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.easyteamup.Activities.SnackBarActivity.SnackBarFragment;
import com.example.easyteamup.Activities.SnackBarActivity.SnackBarInterface;
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
    private boolean hosting; //either hosting or not hosting event
    private int attending; //1 means attending, 0 means not RSVPd, -1 means either
    private int invited; //1 means public, 0 means private, -1 means either

    List<Event> eventList = new ArrayList<>(); //list of events

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_events);
        getSupportActionBar().hide();
        fops = new FirebaseOperations();

        fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.snackbar_container, SnackBarFragment.class, bundle)
                .commit();

        getIntent = getIntent();
        uid = getIntent.getStringExtra("uid");
        hosting = getIntent.getBooleanExtra("hosting", false);
        attending = getIntent.getIntExtra("attending", -1);
        invited = getIntent.getIntExtra("invited", -1);
    }

    @Override
    public void getEvents() {
        if(hosting) { //displays hosted events
            // DOES NOT CARE ABOUT 'attending' OR 'publicity' VARS SINCE HOSTS AUTO ATTEND, AUTO ACCESS PRIV/PUB
            /*
                FILL IN
             */
        } else {
            if(attending == 1) {   //displays rsvpd events
                fops.getRSVPedEvents(uid, listObject -> {
                    try {
                        List<String> events = (List<String>) listObject;
                        for(int i = 0; i < events.size(); i++){
                            String eid = events.get(i);
                            Bundle bundle = new Bundle();
                            fragmentManager.beginTransaction()
                                    .setReorderingAllowed(true)
                                    .add(R.id.list_container, EventsListFragment.class, bundle)
                                    .commit();
                        }
                    }
                    catch(NullPointerException npe) {
                        Bundle bundle = new Bundle();
                        fragmentManager.beginTransaction()
                                .setReorderingAllowed(true)
                                .add(R.id.list_container, NoEventsFragment.class, bundle)
                                .commit();
                    }
                });
            }

            if (invited == 1) { //
                fops.getInvitedEvents(uid, listObject -> {
                    try {
                        Map<String, String> mapEvents = (Map<String, String>) listObject;
                        ArrayList<Pair<String, String>>  events = new ArrayList<>();

                        Iterator<Map.Entry<String, String>> it = mapEvents.entrySet().iterator();
                        while(it.hasNext()){
                            Map.Entry<String, String> event = it.next();
                            String eid = event.getKey();
                            String status = event.getValue();
                            events.add(new Pair(eid, status));
                        }
                        Bundle bundle = new Bundle();
                        fragmentManager.beginTransaction()
                                .setReorderingAllowed(true)
                                .add(R.id.list_container, EventsListFragment.class, bundle)
                                .commit();
                    }
                    catch(NullPointerException npe) {
                        Bundle bundle = new Bundle();
                        fragmentManager.beginTransaction()
                                .setReorderingAllowed(true)
                                .add(R.id.list_container, NoEventsFragment.class, bundle)
                                .commit();
                    }
                });
            }
        }
    }

    @Override
    public void viewPublicMapEvents() {

    }

    @Override
    public void viewPublicListEvents() {

    }

    @Override
    public void createEvent() {

    }

    @Override
    public void viewUserProfile() {

    }

    @Override
    public void viewUserHistory() {

    }
}