package com.example.easyteamup.Activities.ViewEventActivities.FilterEvents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.easyteamup.R;

import android.content.Intent;
import android.os.Bundle;

public class PublicEventsDialogActivity extends AppCompatActivity {

    private Intent getIntent;
    private FragmentManager fragmentManager;

    private String uid;
    private String locName;
    private double lat;
    private double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_events_dialog);
        getSupportActionBar().hide();

        fragmentManager = getSupportFragmentManager();

        getIntent = getIntent();
        uid = getIntent.getStringExtra("uid");
        locName = getIntent.getStringExtra("locName");
        lat = getIntent.getDoubleExtra("lat", 0);
        lon = getIntent.getDoubleExtra("lon", 0);

        PublicEventsDialogFragment publicEventsDialogFragment = PublicEventsDialogFragment.newInstance(uid, locName, lat, lon);
        publicEventsDialogFragment.show(fragmentManager, "fragment_public_events_dialog");
    }
}