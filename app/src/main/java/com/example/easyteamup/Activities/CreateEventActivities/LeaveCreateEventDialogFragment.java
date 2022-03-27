package com.example.easyteamup.Activities.CreateEventActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import com.example.easyteamup.Activities.UserHomeActivities.ViewEventAnalyticsActivity;
import com.example.easyteamup.Activities.UserHomeActivities.ViewProfileActivity;
import com.example.easyteamup.Activities.ViewEventActivities.EventDetailsActivities.EventDetailsDialogFragment;
import com.example.easyteamup.Activities.ViewEventActivities.EventDispatcherActivity;
import com.example.easyteamup.Backend.Event;
import com.example.easyteamup.R;

public class LeaveCreateEventDialogFragment extends DialogFragment {

    private String uid;
    private String where;

    private Button cancel;
    private Button home;

    public LeaveCreateEventDialogFragment(){}

    public static LeaveCreateEventDialogFragment newInstance(String uid, String where) {
        LeaveCreateEventDialogFragment frag = new LeaveCreateEventDialogFragment();

        Bundle args = new Bundle();
        args.putString("uid", uid);
        args.putString("where", where);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_leave_create_event_dialog, container);
        uid = getArguments().getString("uid");
        where = getArguments().getString("uid");

        cancel = (Button) v.findViewById(R.id.cancel);
        home = (Button) v.findViewById(R.id.home);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(where.equals("home")){
                    Intent viewProfile = new Intent(getActivity(), ViewProfileActivity.class);
                    viewProfile.putExtra("uid", uid);
                    startActivity(viewProfile);
                }
                else if(where.equals("create")){
                    Intent createEvent = new Intent(getActivity(), CreateEventActivity.class);
                    createEvent.putExtra("uid", uid);
                    startActivity(createEvent);
                }
                else if(where.equals("public")){
                    Intent viewPublicEvents = new Intent(getActivity(), EventDispatcherActivity.class);
                    viewPublicEvents.putExtra("uid", uid);
                    viewPublicEvents.putExtra("event_type", "public");
                    startActivity(viewPublicEvents);
                }
                else if(where.equals("invite")){
                    Intent viewInvitations = new Intent(getActivity(), EventDispatcherActivity.class);
                    viewInvitations.putExtra("uid", uid);
                    viewInvitations.putExtra("event_type", "invited");
                    startActivity(viewInvitations);
                }
                else if(where.equals("history")){
                    Intent viewEventHistory = new Intent(getActivity(), ViewEventAnalyticsActivity.class);
                    viewEventHistory.putExtra("uid", uid);
                    startActivity(viewEventHistory);
                }
            }
        });

        return v;
    }
}
