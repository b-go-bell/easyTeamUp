package com.example.easyteamup.Activities.EventActivities.ViewEventActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.easyteamup.Activities.EventActivities.ViewEventActivities.FilterPublicEventsActivities.PublicEventsDialogActivity;
import com.example.easyteamup.Activities.EventActivities.ViewEventActivities.ListEventActivities.ViewHostedEventsActivity;
import com.example.easyteamup.Activities.EventActivities.ViewEventActivities.ListEventActivities.ViewInvitedEventsActivity;
import com.example.easyteamup.Activities.EventActivities.ViewEventActivities.ListEventActivities.ViewRSVPdEventsActivity;
import com.example.easyteamup.R;

public class EventDispatcherActivity extends AppCompatActivity {

    private Intent getIntent;

    private String uid; //uid
    private String event_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_dispatcher);
        getSupportActionBar().hide();


        getIntent = getIntent();
        uid = getIntent.getStringExtra("uid");
        event_type = getIntent.getStringExtra("event_type");

        dispatch();
    }

    private void dispatch(){
        if(event_type.equals("invited")) {
            Intent viewInvitedEvents = new Intent(EventDispatcherActivity.this, ViewInvitedEventsActivity.class);
            viewInvitedEvents.putExtra("uid", uid);
            startActivity(viewInvitedEvents);
        }
        else if (event_type.equals("public")) {
            showPublicEventsDialog();
        }
        else if (event_type.equals("attending")) {
            Intent viewRSVPdEvents = new Intent(EventDispatcherActivity.this, ViewRSVPdEventsActivity.class);
            viewRSVPdEvents.putExtra("uid", uid);
            startActivity(viewRSVPdEvents);
        }
        else if (event_type.equals("hosting")) {
            Intent viewHostedEvents = new Intent(EventDispatcherActivity.this, ViewHostedEventsActivity.class);
            viewHostedEvents.putExtra("uid", uid);
            startActivity(viewHostedEvents);
        }
    }

    private void showPublicEventsDialog() {
        Intent publicEventsDialog = new Intent(EventDispatcherActivity.this, PublicEventsDialogActivity.class);
        publicEventsDialog.putExtra("uid", uid);
        publicEventsDialog.putExtra("locName", (String)null);
        publicEventsDialog.putExtra("lat", 0);
        publicEventsDialog.putExtra("lon", 0);

        startActivity(publicEventsDialog);
    }
}