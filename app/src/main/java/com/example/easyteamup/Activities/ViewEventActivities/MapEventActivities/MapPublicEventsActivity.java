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
import com.example.easyteamup.Activities.ViewEventActivities.EventDetailsActivities.NonHost.EventDetailsDialogFragment;
import com.example.easyteamup.Activities.ViewEventActivities.EventDispatcherActivity;
import com.example.easyteamup.Activities.ViewEventActivities.ListEventActivities.ViewPublicEventsActivity;
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

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MapPublicEventsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, SnackBarInterface {

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";
    private static final int DEFAULT_ZOOM = 10;

    private Intent getIntent;
    private FirebaseOperations fops;
    private FragmentManager fragmentManager;

    private String uid;
    private String locationName;
    private double latitude;
    private double longitude;
    private double radius;

    private TextView topDisplay;
    private Button listButton;
    private SupportMapFragment mapFragment;
    private FragmentContainerView noEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_public_events);
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

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        noEvents = (FragmentContainerView) findViewById(R.id.no_events_container);
        topDisplay = (TextView) findViewById(R.id.display_text);
        listButton = (Button) findViewById(R.id.list_button);

        mapFragment.getMapAsync(this);

        String formatted = getString(R.string.viewing_public, locationName);
        topDisplay.setText(formatted);

        listButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewEventsInList();
            }
        });
    }

    private void viewEventsInList(){
        Intent viewPublicListEvents = new Intent(MapPublicEventsActivity.this, ViewPublicEventsActivity.class);
        viewPublicListEvents.putExtra("uid", uid);
        viewPublicListEvents.putExtra("locName", locationName);
        viewPublicListEvents.putExtra("lat", latitude);
        viewPublicListEvents.putExtra("lon", longitude);
        viewPublicListEvents.putExtra("radius", radius);

        startActivity(viewPublicListEvents);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        if(marker.getSnippet() == null){
            Event e = (Event) marker.getTag();
            Log.d("EVENT", String.valueOf(e));
            EventDetailsDialogFragment viewEventDetails = EventDetailsDialogFragment.newInstance(uid, "public", e);
            viewEventDetails.show(fragmentManager, "fragment_event_details_dialog");
        }
        else {
            marker.setSnippet(null);
        }
        return false;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapFragment.getView().setVisibility(View.INVISIBLE);
        GeoLocation geoloc = new GeoLocation(latitude, longitude);
        fops.getPublicEvents(geoloc, radius, eventIdList -> {
            try {
                ArrayList<String> eventIds = (ArrayList<String>) eventIdList;
                if(eventIds.size() == 0){
                    throw new NullPointerException();
                }
                mapFragment.getView().setVisibility(View.VISIBLE);
                noEvents.setVisibility(View.INVISIBLE);

                mMap = googleMap;
                mMap.setOnMarkerClickListener(this);
                mMap.getUiSettings().setZoomControlsEnabled(true);

                //setting up icon for searching around where
                LatLng searchCenter = new LatLng(latitude, longitude);
                BitmapDescriptor searchCenterIcon;
                if(locationName.equals("My Current Location")){
                    int height = 90;
                    int width = 90;
                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.current_loc_bmp);
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                    searchCenterIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);;
                }
                else {
                    int height = 144;
                    int width = 90;
                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gray_marker);
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                    searchCenterIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                }
                //setting marker
                String markerTitle = "Searching for events around ";
                markerTitle = markerTitle.concat(locationName);
                Marker search_marker = mMap.addMarker(new MarkerOptions().position(searchCenter)
                        .title(markerTitle)
                        .icon(searchCenterIcon));;
                search_marker.setSnippet("search");

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(searchCenter, DEFAULT_ZOOM));

                fops.getEventsByEventId(eventIds, eventList -> {
                    try {
                        ArrayList<Event> events = (ArrayList<Event>) eventList;
                        for(int i = 0; i < events.size(); i++){
                            //for each event, display on the map
                            Event currentEvent = events.get(i);
                            if(currentEvent.getLatitude() == null || currentEvent.getLongitude() == null){
                                continue;
                            }
                            Log.d("EVENT", String.valueOf(currentEvent));
                            LatLng eventLoc = new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude());
                            String inviteStatus = currentEvent.getInvitationStatus();
                            boolean isRsvpd = currentEvent.getIsRsvped();
                            String eventName = currentEvent.getName();

                            BitmapDescriptor eventIcon;
                            int height = 144;
                            int width = 90;
                            if(inviteStatus != null){
                                //person was invited to public event; make color = response status
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
                            }
                            else {
                                if(isRsvpd){
                                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.green_marker);
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                                    eventIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                                }
                                else{
                                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.blue_marker);
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                                    eventIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                                }
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
        bundle.putString("none", "public");
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.no_events_container, NoEventsFragment.class, bundle)
                .addToBackStack("none_list")
                .commit();
    }

    public void viewPublicEvents(){
        Intent viewPublicEvents = new Intent(MapPublicEventsActivity.this, EventDispatcherActivity.class);
        viewPublicEvents.putExtra("uid", uid);
        viewPublicEvents.putExtra("event_type", "public");
        startActivity(viewPublicEvents);
    }

    public void viewInvitations(){
        Intent viewInvitations = new Intent(MapPublicEventsActivity.this, EventDispatcherActivity.class);
        viewInvitations.putExtra("uid", uid);
        viewInvitations.putExtra("event_type", "invited");
        startActivity(viewInvitations);
    }

    public void createEvent(){
        Intent createEvent = new Intent(MapPublicEventsActivity.this, CreateEventActivity.class);
        createEvent.putExtra("uid", uid);
        startActivity(createEvent);
    }

    public void viewUserProfile(){
        Intent viewUserProfile = new Intent(MapPublicEventsActivity.this, ViewProfileActivity.class);
        viewUserProfile.putExtra("uid", uid);
        startActivity(viewUserProfile);
    }

    public void viewUserHistory(){
        Intent viewEventHistory = new Intent(MapPublicEventsActivity.this, ViewEventAnalyticsActivity.class);
        viewEventHistory.putExtra("uid", uid);
        startActivity(viewEventHistory);
    }
}