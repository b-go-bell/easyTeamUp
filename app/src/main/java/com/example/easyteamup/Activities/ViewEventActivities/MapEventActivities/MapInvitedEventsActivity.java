package com.example.easyteamup.Activities.ViewEventActivities.MapEventActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.easyteamup.Activities.CreateEventActivities.CreateEventActivity;
import com.example.easyteamup.Activities.SnackBarActivity.SnackBarFragment;
import com.example.easyteamup.Activities.SnackBarActivity.SnackBarInterface;
import com.example.easyteamup.Activities.UserHomeActivities.ViewEventAnalyticsActivity;
import com.example.easyteamup.Activities.UserHomeActivities.ViewProfileActivity;
import com.example.easyteamup.Activities.ViewEventActivities.DisplayEventHelpers.NoEventsFragment;
import com.example.easyteamup.Activities.ViewEventActivities.EventDetailsActivities.EventDetailsDialogFragment;
import com.example.easyteamup.Activities.ViewEventActivities.EventDispatcherActivity;
import com.example.easyteamup.Activities.ViewEventActivities.ListEventActivities.ViewInvitedEventsActivity;
import com.example.easyteamup.Backend.Event;
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.R;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class MapInvitedEventsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, SnackBarInterface {

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";
    private static final int DEFAULT_ZOOM = 10;

    private Intent getIntent;
    private FirebaseOperations fops;
    private FragmentManager fragmentManager;

    private String uid;

    private Button listButton;
    private SupportMapFragment mapFragment;
    private FragmentContainerView noEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_invited_events);
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

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        noEvents = (FragmentContainerView) findViewById(R.id.no_events_container);
        listButton = (Button) findViewById(R.id.list_button);

        mapFragment.getMapAsync(this);

        listButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewEventsInList();
            }
        });
    }

    private void viewEventsInList(){
        Intent viewPublicListEvents = new Intent(MapInvitedEventsActivity.this, ViewInvitedEventsActivity.class);
        viewPublicListEvents.putExtra("uid", uid);
        startActivity(viewPublicListEvents);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Log.d("MARKER CLICK", marker.getTitle());
        Event e = (Event) marker.getTag();
        Log.d("EVENT", String.valueOf(e));
        EventDetailsDialogFragment viewEventDetails = EventDetailsDialogFragment.newInstance(uid, "invited", e);
        viewEventDetails.show(fragmentManager, "fragment_event_details_dialog");
        return false;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapFragment.getView().setVisibility(View.INVISIBLE);
        fops.getInvitedEvents(uid, listObject -> {
            try {
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

                mapFragment.getView().setVisibility(View.VISIBLE);
                noEvents.setVisibility(View.INVISIBLE);

                mMap = googleMap;
                mMap.setOnMarkerClickListener(this);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                //change this to be their current location if time
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(34.0205, -118.2856), 7));

                //displaying the events
                fops.getEventsByEventId(eventIds, eventList -> {
                    try {
                        ArrayList<Event> events = (ArrayList<Event>) eventList;
                        for(int i = 0; i < events.size(); i++){
                            //for each event, display on the map
                            Event currentEvent = events.get(i);
                            LatLng eventLoc = new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude());
                            String inviteStatus = eventStatuses.get(i);
                            String eventName = currentEvent.getName();

                            BitmapDescriptor eventIcon;
                            int height = 144;
                            int width = 90;

                            if(inviteStatus.equals("attending")){
                                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.green_marker);
                                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                                eventIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                            }
                            else if(inviteStatus.equals("rejected")){
                                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.coral_marker);
                                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                                eventIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                            }
                            else {
                                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.orange_marker);
                                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                                eventIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                            }

                            //setting marker
                            Marker event_marker = mMap.addMarker(new MarkerOptions()
                                    .position(eventLoc)
                                    .title(eventName)
                                    .icon(eventIcon));
                            event_marker.setTag(currentEvent);
                        }
                    }
                    catch(NullPointerException npe){
                        showNoEvents();
                    }
                });
            }
            catch (NullPointerException npe){
                showNoEvents();
            }
        });
    }

    private void showNoEvents() {
        mapFragment.getView().setVisibility(View.INVISIBLE);
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
        Intent viewPublicEvents = new Intent(MapInvitedEventsActivity.this, EventDispatcherActivity.class);
        viewPublicEvents.putExtra("uid", uid);
        viewPublicEvents.putExtra("event_type", "public");
        startActivity(viewPublicEvents);
    }

    public void viewInvitations(){}

    public void createEvent(){
        Intent createEvent = new Intent(MapInvitedEventsActivity.this, CreateEventActivity.class);
        createEvent.putExtra("uid", uid);
        startActivity(createEvent);
    }

    public void viewUserProfile(){
        Intent viewUserProfile = new Intent(MapInvitedEventsActivity.this, ViewProfileActivity.class);
        viewUserProfile.putExtra("uid", uid);
        startActivity(viewUserProfile);
    }

    public void viewUserHistory(){
        Intent viewEventHistory = new Intent(MapInvitedEventsActivity.this, ViewEventAnalyticsActivity.class);
        viewEventHistory.putExtra("uid", uid);
        startActivity(viewEventHistory);
    }
}