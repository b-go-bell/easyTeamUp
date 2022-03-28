package com.example.easyteamup.Activities.ViewEventActivities.EventDetailsActivities.Host;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.example.easyteamup.Activities.ViewEventActivities.ListEventActivities.ViewHostedEventsActivity;
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.Backend.User;
import com.example.easyteamup.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ViewRSVPsDialogFragment extends DialogFragment {
    private FirebaseOperations fops;
    private String eid;
    private String what;

    private TextView rsvps;
    private Button goBack;

    public ViewRSVPsDialogFragment() {
    }

    public static ViewRSVPsDialogFragment newInstance(String eid, String what) {
        ViewRSVPsDialogFragment frag = new ViewRSVPsDialogFragment();
        Bundle args = new Bundle();

        args.putString("eid", eid);
        args.putString("what", what);

        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_rsvps_dialog, container);
        eid = getArguments().getString("eid");
        what = getArguments().getString("what");
        fops = new FirebaseOperations(this.getContext());
        goBack = (Button) v.findViewById(R.id.cancel);
        rsvps = (TextView) v.findViewById(R.id.event_rsvps);

        goBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                (getDialog()).cancel();
            }
        });

        if(what.equals("invite")){
            fops.getInvitedUsers(eid, mapObject -> {
                HashMap<String, String> invites = (HashMap) mapObject;
                Iterator it = invites.entrySet().iterator();

                if(invites.isEmpty()){
                    rsvps.setText("Looks like you haven't invited anyone to your event!");
                }
                else {
                    while(it.hasNext()){
                        Map.Entry mapElement = (Map.Entry)it.next();
                        String uid = (String) mapElement.getKey();

                        fops.getUserByUid(uid, userObject -> {
                            User usr = (User) userObject;
                            String inviteStatus = (String) mapElement.getValue();
                            String attending = (String) rsvps.getText();
                            if(attending == null){
                                attending = "";
                            }
                            else{
                                attending = attending.concat("\n\n");
                            }
                            attending = attending.concat(usr.getEmail()).concat(": ").concat(usr.getFirstName()).concat(" ").concat(usr.getLastName().concat("\n").concat(inviteStatus));
                            rsvps.setText(attending);
                        });

                    }
                }
            });
        }
        else {
            fops.getRSVPedUsers(eid, listObject -> {
                ArrayList<String> ids = (ArrayList<String>) listObject;
                Log.d("IDS", String.valueOf(ids));

                if(ids == null || ids.isEmpty()){
                    rsvps.setText("Looks like no one has registered for your event yet!");
                }
                else {
                    for(int i = 0; i < ids.size(); i++){
                        fops.getUserByUid(ids.get(i), userObject -> {
                            User usr = (User) userObject;
                            String attending = (String) rsvps.getText();
                            if(attending == null){
                                attending = "";
                            }
                            else{
                                attending = attending.concat("\n");
                            }
                            attending = attending.concat(usr.getEmail()).concat(": ").concat(usr.getFirstName()).concat(" ").concat(usr.getLastName());
                            Log.d("RSVP TEXT", attending);
                            rsvps.setText(attending);
                        });
                    }
                }
            });
        }




        return v;
    }
}
