package com.example.easyteamup.Activities.ViewEventActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.example.easyteamup.Activities.CreateEventActivities.CreateEventActivity;
import com.example.easyteamup.Activities.SnackBarActivity.SnackBarFragment;
import com.example.easyteamup.Activities.SnackBarActivity.SnackBarInterface;
import com.example.easyteamup.Activities.UserHomeActivities.ViewEventAnalyticsActivity;
import com.example.easyteamup.Activities.UserHomeActivities.ViewProfileActivity;
import com.example.easyteamup.Activities.ViewEventActivities.DisplayEventHelpers.NoEventsFragment;
import com.example.easyteamup.Backend.Event;
import com.example.easyteamup.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.easyteamup.Activities.ViewEventActivities.DisplayEventHelpers.EventAdapter;
import com.example.easyteamup.Backend.FirebaseOperations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ViewInvitedEventsActivity extends AppCompatActivity implements SnackBarInterface {

    private Intent getIntent;
    private FirebaseOperations fops;
    private FragmentManager fragmentManager;

    private String uid;
    private boolean map;

    private ListView listEvents;
    private FragmentContainerView noEvents;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invited_events);
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
        map = false;

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
        fops.getInvitedEvents(uid, listObject -> {
            try{
                ArrayList<String> eventIds = new ArrayList<>();
                ArrayList<String> eventStatuses = new ArrayList<>();

                Map<String, String> mapEvents = (Map<String, String>) listObject;
                if(mapEvents.isEmpty()){
                    throw new NullPointerException();
                }
                Iterator<Map.Entry<String, String>> it = mapEvents.entrySet().iterator();

                while (it.hasNext()) {
                    Map.Entry<String, String> event = it.next();
                    String eid = event.getKey();
                    String status = event.getValue();
                    eventIds.add(eid);
                    eventStatuses.add(status);
                }

                fops.getEventsByEventId(eventIds, eventList -> {
                    try {
                        ArrayList<Event> events = (ArrayList<Event>) eventList;
                        eventAdapter = new EventAdapter(this, events, eventStatuses);

                        listEvents.setAdapter(eventAdapter);

                        listEvents.setVisibility(View.VISIBLE);
                        noEvents.setVisibility(View.INVISIBLE);
                    }
                    catch(NullPointerException npe){
                        showNoEvents();
                    }
                });
            }
            catch(NullPointerException npe) {
                showNoEvents();
            }
        });
    }

    private void showNoEvents() {
        listEvents.setVisibility(View.INVISIBLE);
        noEvents.setVisibility(View.VISIBLE);

        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        bundle.putString("none", "invited");
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.no_events_container, NoEventsFragment.class, bundle)
                .addToBackStack("none_list")
                .commit();
    }

    public void viewPublicEvents(){
        Intent viewPublicEvents = new Intent(ViewInvitedEventsActivity.this, EventDispatcherActivity.class);
        viewPublicEvents.putExtra("uid", uid);
        viewPublicEvents.putExtra("event_type", "public");
        startActivity(viewPublicEvents);
    }

    public void viewInvitations(){
        Intent viewInvitations = new Intent(ViewInvitedEventsActivity.this, EventDispatcherActivity.class);
        viewInvitations.putExtra("uid", uid);
        viewInvitations.putExtra("event_type", "invited");
        startActivity(viewInvitations);
    }

    public void createEvent(){
        Intent createEvent = new Intent(ViewInvitedEventsActivity.this, CreateEventActivity.class);
        createEvent.putExtra("uid", uid);
        startActivity(createEvent);
    }

    public void viewUserProfile(){
        Intent viewUserProfile = new Intent(ViewInvitedEventsActivity.this, ViewProfileActivity.class);
        viewUserProfile.putExtra("uid", uid);
        startActivity(viewUserProfile);
    }

    public void viewUserHistory(){
        Intent viewEventHistory = new Intent(ViewInvitedEventsActivity.this, ViewEventAnalyticsActivity.class);
        viewEventHistory.putExtra("uid", uid);
        startActivity(viewEventHistory);
    }
}