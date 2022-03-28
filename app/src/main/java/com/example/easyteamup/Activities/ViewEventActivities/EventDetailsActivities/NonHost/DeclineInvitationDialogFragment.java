package com.example.easyteamup.Activities.ViewEventActivities.EventDetailsActivities.NonHost;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.easyteamup.Activities.ViewEventActivities.ListEventActivities.ViewInvitedEventsActivity;
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.R;

public class DeclineInvitationDialogFragment extends DialogFragment {
    private FirebaseOperations fops;

    private String uid;
    private String eid;
    private String eventName;
    private boolean invite;

    private TextView type;
    private Button goBack;
    private Button decline;

    public DeclineInvitationDialogFragment() {
    }

    public static DeclineInvitationDialogFragment newInstance(String uid, String eid, String eventName, boolean invite) {
        DeclineInvitationDialogFragment frag = new DeclineInvitationDialogFragment();
        Bundle args = new Bundle();

        args.putString("uid", uid);
        args.putString("eid", eid);
        args.putString("eventName", eventName);
        args.putBoolean("inv", invite);

        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_decline_invitation_dialog, container);
        uid = getArguments().getString("uid");
        eid = getArguments().getString("eid");
        eventName = getArguments().getString("eventName");
        invite = getArguments().getBoolean("inv");
        fops = new FirebaseOperations(this.getContext());

        goBack = (Button) v.findViewById(R.id.cancel);
        decline = (Button) v.findViewById(R.id.reject);
        type = (TextView) v.findViewById(R.id.event_title);

        if(!invite){
            type.setText(getString(R.string.reject_rsvp_dialog, eventName));
        }

        goBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                (getDialog()).cancel();
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(invite){
                    fops.rejectEventInvitation(uid, eid, bc -> {
                        if (bc) {
                            specialCancel();
                        } else {
                            type.setText(("There was an issue rejecting your invitation. Please try again."));
                        }
                    });
                }
                else {
                    fops.removeRSVPFromEvent(uid, eid, bc -> {
                        if(bc){
                            specialCancel();
                        }
                        else {
                            type.setText(("There was an issue removing your RSVP. Please try again."));
                        }
                    });
                }
            }
        });

        return v;
    }

    private void specialCancel() {
        Intent fetchUpdatedInvitations = new Intent(getActivity(), ViewInvitedEventsActivity.class);
        fetchUpdatedInvitations.putExtra("uid", uid);
        startActivity(fetchUpdatedInvitations);
    }
}
