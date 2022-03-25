package com.example.easyteamup.Activities.ViewEventActivities.FilterEvents;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.easyteamup.Activities.CreateEventActivities.CreateEventActivity;
import com.example.easyteamup.Activities.UserHomeActivities.ViewProfileActivity;
import com.example.easyteamup.Activities.ViewEventActivities.ViewPublicEventsActivity;
import com.example.easyteamup.R;
import com.google.android.gms.location.FusedLocationProviderClient;


public class PublicEventsDialogFragment extends DialogFragment {

    private FusedLocationProviderClient client;

    private String uid;
    private String locName;
    private double lat;
    private double lon;
    private double radius = 0;
    private boolean map = false;

    private ImageButton close;
    private TextView locationText, invalidText;
    private Button mapLocation, submit;
    private SwitchCompat mapOrList;
    private EditText radiusText;

    public PublicEventsDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static PublicEventsDialogFragment newInstance(String uid, String locName, double lat, double lon) {
        PublicEventsDialogFragment frag = new PublicEventsDialogFragment();

        Bundle args = new Bundle();
        args.putString("uid", uid);
        args.putString("locName", locName);
        args.putDouble("lat", lat);
        args.putDouble("lon", lon);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_public_events_dialog, container);

        uid = getArguments().getString("uid");
        locName = getArguments().getString("locName");
        lat = getArguments().getDouble("lat");
        lon = getArguments().getDouble("lon");

        close = (ImageButton) v.findViewById(R.id.close);
        locationText = (TextView)  v.findViewById(R.id.where_search);
        invalidText = (TextView) v.findViewById(R.id.error);
        mapLocation = (Button) v.findViewById(R.id.other_loc);
        radiusText = (EditText) v.findViewById(R.id.distance_input);
        mapOrList = (SwitchCompat)  v.findViewById(R.id.map_toggle);
        submit = (Button) v.findViewById(R.id.submit);

        if(locName == null){
            locationText.setText(R.string.no_location);
        } else {
            String picked  = getString(R.string.selected_location, locName);
            locationText.setText(picked);
        }

        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCancel(getDialog());
            }
        });


        mapLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent chooseLocation = new Intent(getActivity(), ChooseLocationActivity.class);
                chooseLocation.putExtra("uid", getArguments().getString("uid"));
                startActivity(chooseLocation);
            }
        });

        mapOrList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //map
                    map = true;
                } else {
                    //list
                    map = false;
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                invalidText.setText("");
                String rad = radiusText.getText().toString();
                int rad_len = rad.trim().length();
                if(rad_len == 0 || locName == null){
                    invalidText.setText(R.string.selection_error);
                }
                else {
                    radius = Double.parseDouble(rad);

                    //send off to view public events activity
                    Intent viewPublicEvents = new Intent(getActivity(), ViewPublicEventsActivity.class);
                    viewPublicEvents.putExtra("uid", uid);
                    viewPublicEvents.putExtra("locName", locName);
                    viewPublicEvents.putExtra("lat", lat);
                    viewPublicEvents.putExtra("lon", lon);
                    viewPublicEvents.putExtra("radius", radius);
                    viewPublicEvents.putExtra("map", map);
                    startActivity(viewPublicEvents);
                }
            }
        });

        return v;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        //go back to the user home
        Intent goHome = new Intent(getActivity(), ViewProfileActivity.class);
        goHome.putExtra("uid", uid);
        startActivity(goHome);
    }
}