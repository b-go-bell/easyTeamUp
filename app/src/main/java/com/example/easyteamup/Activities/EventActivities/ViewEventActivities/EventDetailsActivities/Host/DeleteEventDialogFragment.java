package com.example.easyteamup.Activities.EventActivities.ViewEventActivities.EventDetailsActivities.Host;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.easyteamup.Activities.EventActivities.ViewEventActivities.ListEventActivities.ViewHostedEventsActivity;
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.R;

public class DeleteEventDialogFragment extends DialogFragment {
    private FirebaseOperations fops;

    private String uid;
    private String eid;
    private String eventName;

    private TextView type;
    private Button goBack;
    private Button decline;

    public DeleteEventDialogFragment() {
    }

    public static DeleteEventDialogFragment newInstance(String uid, String eid, String eventName) {
        DeleteEventDialogFragment frag = new DeleteEventDialogFragment();
        Bundle args = new Bundle();

        args.putString("uid", uid);
        args.putString("eid", eid);
        args.putString("eventName", eventName);

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
        fops = new FirebaseOperations(this.getContext());

        goBack = (Button) v.findViewById(R.id.cancel);
        decline = (Button) v.findViewById(R.id.reject);
        type = (TextView) v.findViewById(R.id.event_title);

        goBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                (getDialog()).cancel();
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fops.deleteEvent(eid, bc -> {
                    if (bc) {
                        specialCancel();
                    } else {
                        type.setText(("There was an issue deleting your event. Please try again."));
                    }
                });
            }
        });

        return v;
    }

    private void specialCancel() {
        Intent fetchUpdatedHostedEvents = new Intent(getActivity(), ViewHostedEventsActivity.class);
        fetchUpdatedHostedEvents.putExtra("uid", uid);
        startActivity(fetchUpdatedHostedEvents);
    }
}
