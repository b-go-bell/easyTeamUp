package com.example.easyteamup.Activities.ViewEventActivities.FilterEvents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.example.easyteamup.R;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class ChooseLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";
    private FusedLocationProviderClient mFusedLocationProviderClient;
    AutocompleteSupportFragment autocompleteFragment;

    // A default location (USC) and default zoom to use
    private final LatLng mDefaultLocation = new LatLng(34.0224, -118.28521);
    private static final int DEFAULT_ZOOM = 10;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private Intent getIntent;
    private ImageButton currentLoc;
    private Button pickLocation;
    private String uid;

    private FragmentManager fragmentManager;
    private String locationNameSend;
    private double locationLatitudeSend;
    private double locationLongitudeSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        getSupportActionBar().hide();
        fragmentManager = getSupportFragmentManager();

        getIntent = getIntent();
        uid = getIntent.getStringExtra("uid");


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // Set up the views
        currentLoc = (ImageButton) findViewById(R.id.my_location_button);
        pickLocation = (Button) findViewById(R.id.pick_location);

        // Initialize the clients
        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                fragmentManager.findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME));


        currentLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickCurrentPlace();
            }
        });

        pickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent publicEventsDialog = new Intent(ChooseLocationActivity.this, PublicEventsDialogActivity.class);
                publicEventsDialog.putExtra("uid", uid);
                publicEventsDialog.putExtra("locName", locationNameSend);
                publicEventsDialog.putExtra("lat", locationLatitudeSend);
                publicEventsDialog.putExtra("lon", locationLongitudeSend);

                startActivity(publicEventsDialog);
            }
        });

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.d("PLACE: ", String.valueOf(place));
                LatLng search_loc = place.getLatLng();

                int height = 120;
                int width = 77;
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.green_marker);
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                BitmapDescriptor search_icon = BitmapDescriptorFactory.fromBitmap(smallMarker);;

                MarkerOptions search = new MarkerOptions().position(search_loc)
                                                          .title(place.getName())
                                                          .icon(search_icon);
                mMap.addMarker(search);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(search_loc, DEFAULT_ZOOM));

                //initialize locationToSend to default
                locationNameSend = place.getName();
                locationLatitudeSend = search_loc.latitude;
                locationLongitudeSend = search_loc.longitude;
                String formatted_button = getString(R.string.button_loc, place.getName());
                pickLocation.setText(formatted_button);
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }


        });

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Log.d("MARKER CLICK", marker.getTitle());
        String formatted_button = getString(R.string.button_loc, marker.getTitle());
        pickLocation.setText(formatted_button);
        return false;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        // Add a marker at default USC and move camera
        //setting up marker icon
        int height = 120;
        int width = 77;
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.green_marker);
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        BitmapDescriptor uscIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);;

        mMap.addMarker(new MarkerOptions().position(mDefaultLocation).title(getString(R.string.usc)).icon(uscIcon));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 8));

        locationNameSend = getString(R.string.usc);
        locationLatitudeSend = mDefaultLocation.latitude;
        locationLongitudeSend = mDefaultLocation.longitude;
        String formatted_button = getString(R.string.button_loc, getString(R.string.usc));
        pickLocation.setText(formatted_button);


        // Enable the zoom controls for the map
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Prompt the user for permission.
        getLocationPermission();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                LatLng click_loc = new LatLng(point.latitude, point.longitude);
                //setting up marker icon
                int height = 120;
                int width = 77;
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.green_marker);
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                BitmapDescriptor clickIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);;

                MarkerOptions click_marker = new MarkerOptions().position(click_loc).title("Clicked Location").icon(clickIcon);
                mMap.addMarker(click_marker);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(click_loc, DEFAULT_ZOOM));

                locationNameSend = "Clicked Location";
                locationLatitudeSend = click_loc.latitude;
                locationLongitudeSend = click_loc.longitude;
                String formatted_button = getString(R.string.button_loc, "Clicked Location");
                pickLocation.setText(formatted_button);
            }
        });
    }

    /**
     * Get the current location of the device, and position the map's camera
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                try {
                    Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                    Log.d("FUSED", "HAPPENED");

                    locationResult.addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Set the map's camera position to the current location of the device.
                                LatLng current_loc = new LatLng(location.getLatitude(), location.getLongitude());

                                //setting up marker icon
                                int height = 80;
                                int width = 80;
                                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.current_loc_bmp);
                                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                                BitmapDescriptor currentLocIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);;

                                //setting marker
                                MarkerOptions current_marker = new MarkerOptions().position(current_loc)
                                                                                .title("My Current Location")
                                                                                .icon(currentLocIcon);

                                mMap.addMarker(current_marker);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current_loc, DEFAULT_ZOOM));

                                locationNameSend = "My Current Location";
                                locationLatitudeSend = current_loc.latitude;
                                locationLongitudeSend = current_loc.longitude;
                                String formatted_button = getString(R.string.button_loc, "My Current Location");
                                pickLocation.setText(formatted_button);

                            } else {
                                Log.d(TAG, "Current location is null. Using defaults.");
                                mMap.moveCamera(CameraUpdateFactory
                                        .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            }
                        }
                    });
                }
                catch(SecurityException se){}
            }
        } catch (Exception e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Show the current place on the map - provided the user
     * has granted location permission.
     */
    private void pickCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            Log.d("PERMISSION", "GRANTED");
            getDeviceLocation();
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        mLocationPermissionGranted = false;
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
}