package com.example.easyteamup.Activities.EventActivities.ViewEventActivities.MapEventActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.example.easyteamup.Activities.EventActivities.CreateEventActivities.CreateEventActivity;
import com.example.easyteamup.Activities.EventActivities.ViewEventActivities.EventDispatcherActivity;
import com.example.easyteamup.Activities.EventActivities.ViewEventActivities.ListEventActivities.ViewEventAnalyticsActivity;
import com.example.easyteamup.Activities.EventActivities.ViewEventActivities.ListEventActivities.ViewHostedEventsActivity;
import com.example.easyteamup.Activities.SnackBarActivities.SnackBarFragment;
import com.example.easyteamup.Activities.SnackBarActivities.SnackBarInterface;
import com.example.easyteamup.Activities.UserHomeActivities.ViewProfileActivity;
import com.example.easyteamup.Activities.EventActivities.ViewEventActivities.DisplayEventHelpers.NoEventsFragment;
import com.example.easyteamup.Activities.EventActivities.ViewEventActivities.EventDetailsActivities.Host.HostEventDetailsDialogFragment;
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

public class MapHostedEventsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, SnackBarInterface {

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";
    private static final int DEFAULT_ZOOM = 10;

    private Intent getIntent;
    private FirebaseOperations fops;
    private FragmentManager fragmentManager;

    private String uid;
    private boolean none;

    private Button listButton;
    private SupportMapFragment mapFragment;
    private FragmentContainerView noEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_hosted_events);
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
        Intent viewPublicListEvents = new Intent(MapHostedEventsActivity.this, ViewHostedEventsActivity.class);
        viewPublicListEvents.putExtra("uid", uid);
        startActivity(viewPublicListEvents);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Event e = (Event) marker.getTag();
        HostEventDetailsDialogFragment hostDetails = HostEventDetailsDialogFragment.newInstance(uid, e);
        hostDetails.show(fragmentManager, "fragment_event_details_dialog");
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapFragment.getView().setVisibility(View.INVISIBLE);
        fops.getHostedEvents(uid, eventIdList -> {
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

                //displaying events
                fops.getEventsByEventId(eventIds, eventList -> {
                    try {
                        ArrayList<Event> events = (ArrayList<Event>) eventList;
                        for(int i = 0; i < events.size(); i++) {
                            //for each event, display on the map
                            Event currentEvent = events.get(i);
                            if(currentEvent.getLatitude() == null || currentEvent.getLongitude() == null){
                                continue;
                            }
                            LatLng eventLoc = new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude());

                            String eventName = currentEvent.getName();

                            BitmapDescriptor eventIcon;
                            int height = 144;
                            int width = 90;

                            if(!currentEvent.getIsPublic()){
                                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.coral_marker);
                                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                                eventIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                            }
                            else{
                                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.green_marker);
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
        bundle.putString("none", "hosting");
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.no_events_container, NoEventsFragment.class, bundle)
                .addToBackStack("none_list")
                .commit();
    }

    public void viewPublicEvents(){
        Intent viewPublicEvents = new Intent(MapHostedEventsActivity.this, EventDispatcherActivity.class);
        viewPublicEvents.putExtra("uid", uid);
        viewPublicEvents.putExtra("event_type", "public");
        startActivity(viewPublicEvents);
    }

    public void viewInvitations(){
        Intent viewInvitations = new Intent(MapHostedEventsActivity.this, EventDispatcherActivity.class);
        viewInvitations.putExtra("uid", uid);
        viewInvitations.putExtra("event_type", "invited");
        startActivity(viewInvitations);
    }

    public void createEvent(){
        Intent createEvent = new Intent(MapHostedEventsActivity.this, CreateEventActivity.class);
        createEvent.putExtra("uid", uid);
        startActivity(createEvent);
    }

    public void viewUserProfile(){
        Intent viewUserProfile = new Intent(MapHostedEventsActivity.this, ViewProfileActivity.class);
        viewUserProfile.putExtra("uid", uid);
        startActivity(viewUserProfile);
    }

    public void viewUserHistory(){
        Intent viewEventHistory = new Intent(MapHostedEventsActivity .this, ViewEventAnalyticsActivity.class);
        viewEventHistory.putExtra("uid", uid);
        startActivity(viewEventHistory);
    }
}