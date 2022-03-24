package com.example.easyteamup.Activities.ViewEventActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.easyteamup.Activities.SnackBarActivity.SnackBarFragment;
import com.example.easyteamup.Activities.SnackBarActivity.SnackBarInterface;
import com.example.easyteamup.Activities.UserAuthActivities.SignUpActivityFolder.SignUpInterface;
import com.example.easyteamup.Activities.ViewEventActivities.FilterEvents.PublicEventsDialogFragment;
import com.example.easyteamup.R;

public class EventDispatcherActivity extends AppCompatActivity {

    private Intent getIntent;
    private FragmentManager fragmentManager;

    private String uid; //uid
    private String event_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_dispatcher);
        getSupportActionBar().hide();

        fragmentManager = getSupportFragmentManager();

        getIntent = getIntent();
        uid = getIntent.getStringExtra("uid");
        event_type = getIntent.getStringExtra("event_type");

        dispatch();
    }

    private void dispatch(){
        if(event_type.equals("invited")) {
            showSpecialEventsDialog();
        }
        else if (event_type.equals("public")) {
            showPublicEventsDialog();
        }
        else if (event_type.equals("attending")) {
            showSpecialEventsDialog();
        }
        else if (event_type.equals("hosting")) {
            showSpecialEventsDialog();
        }
    }

    private void showPublicEventsDialog() {
        PublicEventsDialogFragment publicEventsDialogFragment = PublicEventsDialogFragment.newInstance(uid, null, 0, 0);
        publicEventsDialogFragment.show(fragmentManager, "fragment_public_events_dialog");
    }

    private void showSpecialEventsDialog() {
        // FILL IN
    }
}