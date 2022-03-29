package com.example.easyteamup.Activities.UserHomeActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.easyteamup.Activities.CreateEventActivities.CreateEventActivity;
import com.example.easyteamup.Activities.CreateEventActivities.LeaveCreateEventDialogFragment;
import com.example.easyteamup.Activities.ViewEventActivities.EventDispatcherActivity;
import com.example.easyteamup.R;

public class LeaveUpdateProfileDialogFragment extends DialogFragment {

    private String uid;
    private String where;

    private Button cancel;
    private Button home;
    private TextView text;

    public LeaveUpdateProfileDialogFragment(){}

    public static LeaveUpdateProfileDialogFragment newInstance(String uid, String where) {
        LeaveUpdateProfileDialogFragment frag = new LeaveUpdateProfileDialogFragment();

        Bundle args = new Bundle();
        args.putString("uid", uid);
        args.putString("where", where);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_leave_update_profile_dialog, container);
        uid = getArguments().getString("uid");
        where = getArguments().getString("where");

        text = (TextView) v.findViewById(R.id.dialog_message);
        cancel = (Button) v.findViewById(R.id.cancel);
        home = (Button) v.findViewById(R.id.home);

        if(where.equals("fail")){
            text.setText("Oh no! There was an issue saving your updated profile. Please check all fields are filled in correctly.");
            cancel.setText("Keep modifying my profile");
            home.setText("Go back home");
        }


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("WHERE", where);
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
                else if(where.equals("hosting")){
                    Intent viewHostedEventsMap = new Intent(getActivity(), EventDispatcherActivity.class);
                    viewHostedEventsMap.putExtra("uid", uid);
                    viewHostedEventsMap.putExtra("event_type", "hosting");
                    startActivity(viewHostedEventsMap);
                }
                else {
                    Intent viewProfile = new Intent(getActivity(), ViewProfileActivity.class);
                    viewProfile.putExtra("uid", uid);
                    startActivity(viewProfile);
                }
            }
        });

        return v;
    }
}

