package com.example.easyteamup.Activities.ViewEventActivities.DisplayEventHelpers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.easyteamup.Activities.CreateEventActivities.CreateEventActivity;
import com.example.easyteamup.Activities.UserHomeActivities.ViewProfileActivity;
import com.example.easyteamup.Activities.ViewEventActivities.EventDispatcherActivity;
import com.example.easyteamup.R;

public class NoEventsFragment extends Fragment {

    private String uid;

    private TextView noEvents;
    //no invited, public, rsvpd, hosted events
    private Button browsePublicCenter;
    private Button browsePublicLeft;
    private Button viewInvitationsRight;
    private Button createEvent;
    private Button userHome;

    private ProgressBar loading;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_no_events, container, false);
        Bundle bundleArgs = getArguments();
        uid = bundleArgs.getString("uid");
        String none = bundleArgs.getString("none");

        noEvents = (TextView) v.findViewById(R.id.no_events);
        browsePublicCenter = (Button) v.findViewById(R.id.browse_public_events);
        browsePublicLeft = (Button) v.findViewById(R.id.browse_public_events_left);
        viewInvitationsRight = (Button) v.findViewById(R.id.browse_invited_events_right);
        createEvent = (Button) v.findViewById(R.id.create_event);
        userHome = (Button) v.findViewById(R.id.user_home);
        loading = (ProgressBar) v.findViewById(R.id.loading);
        loading.setVisibility(View.INVISIBLE);

        if(none.equals("invited")) { //shows browse public events button
            noEvents.setText(R.string.no_invitations);
            browsePublicCenter.setVisibility(View.VISIBLE);
            browsePublicCenter.setEnabled(true);

            createEvent.setVisibility(View.INVISIBLE);
            browsePublicLeft.setVisibility(View.INVISIBLE);
            viewInvitationsRight.setVisibility(View.INVISIBLE);
            createEvent.setEnabled(false);
            browsePublicLeft.setEnabled(false);
            viewInvitationsRight.setEnabled(false);
        } else if(none.equals("public") || none.equals("attending")) { //shows invitations and public events button
            if(none.equals("public"))
                noEvents.setText(R.string.no_public_events);
            else
                noEvents.setText(R.string.no_rsvps);
            browsePublicLeft.setVisibility(View.VISIBLE);
            viewInvitationsRight.setVisibility(View.VISIBLE);
            browsePublicLeft.setEnabled(true);
            viewInvitationsRight.setEnabled(true);

            createEvent.setVisibility(View.INVISIBLE);
            browsePublicCenter.setVisibility(View.INVISIBLE);
            createEvent.setEnabled(false);
            browsePublicCenter.setEnabled(false);
        } else if (none.equals("hosting")) { //shows create event option
            noEvents.setText(R.string.no_hosting);
            createEvent.setVisibility(View.VISIBLE);
            createEvent.setEnabled(true);

            browsePublicCenter.setVisibility(View.INVISIBLE);
            browsePublicLeft.setVisibility(View.INVISIBLE);
            viewInvitationsRight.setVisibility(View.INVISIBLE);
            browsePublicCenter.setEnabled(false);
            browsePublicLeft.setEnabled(false);
            viewInvitationsRight.setEnabled(false);
        }
        else if(none.equals("past")){
            noEvents.setText(R.string.no_past_events);

            browsePublicLeft.setVisibility(View.VISIBLE);
            viewInvitationsRight.setVisibility(View.VISIBLE);
            browsePublicLeft.setEnabled(true);
            viewInvitationsRight.setEnabled(true);

            createEvent.setVisibility(View.INVISIBLE);
            browsePublicCenter.setVisibility(View.INVISIBLE);
            createEvent.setEnabled(false);
            browsePublicCenter.setEnabled(false);
        }

        browsePublicCenter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                Intent viewPublicEventsList = new Intent(getActivity(), EventDispatcherActivity.class);
                viewPublicEventsList.putExtra("uid", uid);
                viewPublicEventsList.putExtra("event_type", "public");
                startActivity(viewPublicEventsList);
            }
        });

        browsePublicLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                Intent viewPublicEventsList = new Intent(getActivity(), EventDispatcherActivity.class);
                viewPublicEventsList.putExtra("uid", uid);
                viewPublicEventsList.putExtra("event_type", "public");
                startActivity(viewPublicEventsList);
            }
        });

        viewInvitationsRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                Intent viewPublicEventsList = new Intent(getActivity(), EventDispatcherActivity.class);
                viewPublicEventsList.putExtra("uid", uid);
                viewPublicEventsList.putExtra("event_type", "invited");
                startActivity(viewPublicEventsList);
            }
        });

        createEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                Intent createEvent = new Intent(getActivity(), CreateEventActivity.class);
                createEvent.putExtra("uid", uid);
                startActivity(createEvent);
            }
        });

        userHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                Intent userProfile = new Intent(getActivity(), ViewProfileActivity.class);
                userProfile.putExtra("uid", uid);
                startActivity(userProfile);
            }
        });

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}