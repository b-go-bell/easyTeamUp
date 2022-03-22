package com.example.easyteamup.Activities.ViewEventActivities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.easyteamup.Activities.UserAuthActivities.SignUpActivityFolder.SignUpInterface;
import com.example.easyteamup.Activities.UserHomeActivities.ViewProfileActivity;
import com.example.easyteamup.R;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;


public class PublicEventsDialogFragment extends DialogFragment {

    private PublicEventsDialogInterface mCallback;
    private FusedLocationProviderClient client;

    private double lat;
    private double lon;
    private double radius = 0;

    private ImageButton close;
    private Button currentLocation, mapLocation, submit;
    private EditText radiusText;

    public PublicEventsDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static PublicEventsDialogFragment newInstance() {
        PublicEventsDialogFragment frag = new PublicEventsDialogFragment();
        return frag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (PublicEventsDialogInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement SignUpInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_public_events_dialog, container);

        close = (ImageButton) v.findViewById(R.id.close);
        currentLocation = (Button) v.findViewById(R.id.current_loc);
        mapLocation = (Button) v.findViewById(R.id.other_loc);
        submit = (Button) v.findViewById(R.id.submit);
        radiusText = (EditText) v.findViewById(R.id.distance_input);


        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallback.onSubmit(null, 0);
                onCancel(getDialog());
            }
        });

        currentLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        mapLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent chooseLocation = new Intent(getActivity(), ChooseLocationActivity.class);
                startActivity(chooseLocation);
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
        mCallback.onSubmit(null, 0);
        super.onCancel(dialog);
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }
}