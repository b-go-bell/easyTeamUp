package com.example.easyteamup.Activities.ViewEventActivities.MapEventActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.example.easyteamup.Activities.CreateEventActivities.CreateEventActivity;
import com.example.easyteamup.Activities.SnackBarActivity.SnackBarFragment;
import com.example.easyteamup.Activities.SnackBarActivity.SnackBarInterface;
import com.example.easyteamup.Activities.UserHomeActivities.ViewEventAnalyticsActivity;
import com.example.easyteamup.Activities.UserHomeActivities.ViewProfileActivity;
import com.example.easyteamup.Activities.ViewEventActivities.DisplayEventHelpers.NoEventsFragment;
import com.example.easyteamup.Activities.ViewEventActivities.EventDispatcherActivity;
import com.example.easyteamup.Activities.ViewEventActivities.ListEventActivities.ViewHostedEventsActivity;
import com.example.easyteamup.Activities.ViewEventActivities.ListEventActivities.ViewRSVPdEventsActivity;
import com.example.easyteamup.Backend.Event;
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MapRSVPdEventsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, SnackBarInterface {

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
        setContentView(R.layout.activity_map_rsvpd_events);
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

    private void viewEventsInList() {
        Intent viewPublicListEvents = new Intent(MapRSVPdEventsActivity.this, ViewRSVPdEventsActivity.class);
        viewPublicListEvents.putExtra("uid", uid);
        startActivity(viewPublicListEvents);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapFragment.getView().setVisibility(View.INVISIBLE);
        fops.getRSVPedEvents(uid, eventIdList -> {
            try {
                ArrayList<String> eventIds = (ArrayList<String>) eventIdList;
                if (eventIds.size() == 0) {
                    throw new NullPointerException();
                }
                mapFragment.getView().setVisibility(View.VISIBLE);
                noEvents.setVisibility(View.INVISIBLE);

                mMap = googleMap;
                mMap.setOnMarkerClickListener(this);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                //change this to be their current location if time
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(34.0205, -118.2856), 7));

                fops.getEventsByEventId(eventIds, eventList -> {
                    try {
                        ArrayList<Event> events = (ArrayList<Event>) eventList;
                        for (int i = 0; i < events.size(); i++) {
                            //for each event, display on the map
                            Event currentEvent = events.get(i);
                            LatLng eventLoc = new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude());
                            String eventName = currentEvent.getName();

                            BitmapDescriptor eventIcon;
                            int height = 144;
                            int width = 90;
                            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.green_marker);
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                            eventIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);

                            //setting marker
                            MarkerOptions event_marker = new MarkerOptions().position(eventLoc)
                                    .title(eventName)
                                    .icon(eventIcon);
                            mMap.addMarker(event_marker);
                        }
                    } catch (NullPointerException npe) {
                        showNoEvents();
                    }
                });
            } catch (NullPointerException npe) {
                showNoEvents();
            }
        });
    }

    private void showNoEvents() {
        mapFragment.getView().setVisibility(View.INVISIBLE);
        noEvents.setVisibility(View.VISIBLE);

        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        bundle.putString("none", "attending");
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.no_events_container, NoEventsFragment.class, bundle)
                .addToBackStack("none_list")
                .commit();
    }

    public void viewPublicEvents(){
        Intent viewPublicEvents = new Intent(MapRSVPdEventsActivity.this, EventDispatcherActivity.class);
        viewPublicEvents.putExtra("uid", uid);
        viewPublicEvents.putExtra("event_type", "public");
        startActivity(viewPublicEvents);
    }

    public void viewInvitations(){
        Intent viewInvitations = new Intent(MapRSVPdEventsActivity.this, EventDispatcherActivity.class);
        viewInvitations.putExtra("uid", uid);
        viewInvitations.putExtra("event_type", "invited");
        startActivity(viewInvitations);
    }

    public void createEvent(){
        Intent createEvent = new Intent(MapRSVPdEventsActivity.this, CreateEventActivity.class);
        createEvent.putExtra("uid", uid);
        startActivity(createEvent);
    }

    public void viewUserProfile(){
        Intent viewUserProfile = new Intent(MapRSVPdEventsActivity.this, ViewProfileActivity.class);
        viewUserProfile.putExtra("uid", uid);
        startActivity(viewUserProfile);
    }

    public void viewUserHistory(){
        Intent viewEventHistory = new Intent(MapRSVPdEventsActivity.this, ViewEventAnalyticsActivity.class);
        viewEventHistory.putExtra("uid", uid);
        startActivity(viewEventHistory);
    }
}