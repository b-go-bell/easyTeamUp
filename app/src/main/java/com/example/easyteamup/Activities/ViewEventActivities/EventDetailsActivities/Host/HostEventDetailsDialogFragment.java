package com.example.easyteamup.Activities.ViewEventActivities.EventDetailsActivities.Host;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.easyteamup.Activities.ViewEventActivities.EventDetailsActivities.NonHost.AcceptEventDialogFragment;
import com.example.easyteamup.Activities.ViewEventActivities.EventDetailsActivities.NonHost.DeclineInvitationDialogFragment;
import com.example.easyteamup.Activities.ViewEventActivities.EventDetailsActivities.NonHost.EventDetailsDialogFragment;
import com.example.easyteamup.Backend.Event;
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.R;

import java.util.Date;

public class HostEventDetailsDialogFragment extends DialogFragment  {
    private FirebaseOperations fops;
    private FragmentManager fragmentManager;

    private String uid;
    private Event event;

    private ImageButton close;
    private TextView eventTitle, eventAddress, eventPublicity, eventDue, eventDescrip;
    private ConstraintLayout descripLayout;
    private Button deleteEvent, updateEvent;

    public HostEventDetailsDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static HostEventDetailsDialogFragment newInstance(String uid, Event event) {
        HostEventDetailsDialogFragment frag = new HostEventDetailsDialogFragment();

        Bundle args = new Bundle();
        args.putString("uid", uid);
        args.putParcelable("event", event);

        frag.setArguments(args);

        return frag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_host_event_details_dialog, container);
        fops = new FirebaseOperations(this.getContext());
        fragmentManager = ((FragmentActivity)getContext()).getSupportFragmentManager();

        uid = getArguments().getString("uid");
        event = getArguments().getParcelable("event");

        close = (ImageButton) v.findViewById(R.id.close);
        eventTitle = (TextView) v.findViewById(R.id.event_title);
        eventAddress = (TextView) v.findViewById(R.id.event_address);
        eventPublicity = (TextView) v.findViewById(R.id.event_publicity);
        eventDue = (TextView) v.findViewById(R.id.event_due);
        eventDescrip = (TextView) v.findViewById(R.id.event_descrip);

        descripLayout = (ConstraintLayout) v.findViewById(R.id.descrip_layout);
        deleteEvent = (Button) v.findViewById(R.id.delete_event);
        updateEvent = (Button) v.findViewById(R.id.update_event);


        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                (getDialog()).cancel();
            }
        });

        deleteEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DeleteEventDialogFragment deleteEvent = DeleteEventDialogFragment.newInstance(uid, event.getEventId(), event.getName());
                deleteEvent.show(getChildFragmentManager(), "fragment_delete_event");
            }
        });

        updateEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UpdateEventDialogFragment updateEvent = UpdateEventDialogFragment.newInstance(uid, event.getEventId(), event);
                updateEvent.show(getChildFragmentManager(), "fragment_delete_event");
            }
        });

        eventTitle.setText(event.getName());
        eventAddress.setText(event.getAddress());
        String status;
        if(!event.getIsPublic()){
            status = "Hosting private event";
            eventPublicity.setTextColor(ContextCompat.getColor(getContext(), R.color.orange));
        }
        else {
            status = "Hosting public event";
            eventPublicity.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
        }
        eventPublicity.setText(status);


        //TO DO
        if(event.getDescription() == null){
            descripLayout.setVisibility(View.GONE);
        }
        else {
            descripLayout.setVisibility(View.VISIBLE);
            eventDescrip.setText(event.getDescription());
        }

        Date time = event.getDueDate().toDate();
        String formattedTime = this.getString(R.string.due_time, time.toString());
        eventDue.setText(formattedTime);

        return v;
    }
}
