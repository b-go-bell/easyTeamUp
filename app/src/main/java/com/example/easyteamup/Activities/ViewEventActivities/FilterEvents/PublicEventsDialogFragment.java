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

import com.example.easyteamup.R;
import com.google.android.gms.location.FusedLocationProviderClient;


public class PublicEventsDialogFragment extends DialogFragment {

    private FusedLocationProviderClient client;

    private double latitude;
    private double longitude;
    private double radius = 0;

    private ImageButton close;
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
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_public_events_dialog, container);

        close = (ImageButton) v.findViewById(R.id.close);
        mapLocation = (Button) v.findViewById(R.id.other_loc);
        radiusText = (EditText) v.findViewById(R.id.distance_input);
        mapOrList = (SwitchCompat)  v.findViewById(R.id.map_toggle);
        submit = (Button) v.findViewById(R.id.submit);


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
                } else {
                    //list
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        return v;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}