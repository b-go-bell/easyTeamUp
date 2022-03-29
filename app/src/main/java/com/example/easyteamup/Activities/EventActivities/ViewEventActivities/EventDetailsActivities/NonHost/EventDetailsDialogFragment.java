package com.example.easyteamup.Activities.EventActivities.ViewEventActivities.EventDetailsActivities.NonHost;

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

import com.example.easyteamup.Backend.Event;
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.Backend.User;
import com.example.easyteamup.R;

import java.util.Date;

public class EventDetailsDialogFragment extends DialogFragment {

    private FirebaseOperations fops;
    private FragmentManager fragmentManager;

    private String uid;
    private String eventType;
    private Event event;


    private ImageButton close;
    private TextView eventTitle, eventHost, eventCategory, eventAddress, eventStatus, eventDue, eventDescrip, eventLength;
    private ConstraintLayout descripLayout;
    private LinearLayout inviteButtons, publicButton, declineButton, acceptButton;
    private Button rejectInvite, acceptInvite, attendPublic, cancelRSVP, acceptInviteFromReject;

    public EventDetailsDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EventDetailsDialogFragment newInstance(String uid, String eventType, Event event) {
        EventDetailsDialogFragment frag = new EventDetailsDialogFragment();

        Bundle args = new Bundle();
        args.putString("uid", uid);
        args.putString("eventType", eventType);
        args.putParcelable("event", event);

        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_details_dialog, container);
        fops = new FirebaseOperations(this.getContext());
        fragmentManager = ((FragmentActivity)getContext()).getSupportFragmentManager();

        uid = getArguments().getString("uid");
        eventType = getArguments().getString("eventType");
        event = getArguments().getParcelable("event");

        close = (ImageButton) v.findViewById(R.id.close);
        eventTitle = (TextView) v.findViewById(R.id.event_title);
        eventHost = (TextView) v.findViewById(R.id.event_host);
        eventCategory = (TextView) v.findViewById(R.id.event_category);
        eventAddress = (TextView) v.findViewById(R.id.event_address);
        eventStatus = (TextView) v.findViewById(R.id.event_status);
        eventDue = (TextView) v.findViewById(R.id.event_due);
        eventDescrip = (TextView) v.findViewById(R.id.event_descrip);
        eventLength = (TextView) v.findViewById(R.id.event_length);

        descripLayout = (ConstraintLayout) v.findViewById(R.id.descrip_layout);
        rejectInvite = (Button) v.findViewById(R.id.reject_invite);
        acceptInvite = (Button) v.findViewById(R.id.accept_invite);
        attendPublic = (Button) v.findViewById(R.id.attend_public);
        cancelRSVP = (Button) v.findViewById(R.id.cancel);
        acceptInviteFromReject = (Button) v.findViewById(R.id.accept_invited_reject);

        inviteButtons = (LinearLayout)  v.findViewById(R.id.invite_buttons);
        publicButton = (LinearLayout)  v.findViewById(R.id.public_button);
        declineButton = (LinearLayout)  v.findViewById(R.id.decline_button);
        acceptButton = (LinearLayout)  v.findViewById(R.id.accept_button);


        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                (getDialog()).cancel();
            }
        });

        rejectInvite.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 DeclineInvitationDialogFragment declineEvent = DeclineInvitationDialogFragment.newInstance(uid, event.getEventId(), event.getName(), true);
                 declineEvent.show(fragmentManager, "fragment_decline_event");
             }
         });

        acceptInvite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AcceptEventDialogFragment rsvpEvent = AcceptEventDialogFragment.newInstance(uid, event.getEventId(), event.getName(), true);
                rsvpEvent.show(getChildFragmentManager(), "fragment_rsvp_event");
            }
        });

        attendPublic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AcceptEventDialogFragment rsvpEvent = AcceptEventDialogFragment.newInstance(uid, event.getEventId(), event.getName(), true);
                rsvpEvent.show(getChildFragmentManager(), "fragment_rsvp_event");
            }
        });

        cancelRSVP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DeclineInvitationDialogFragment declineEvent = DeclineInvitationDialogFragment.newInstance(uid, event.getEventId(), event.getName(), false);
                declineEvent.show(fragmentManager, "fragment_decline_rsvp");
            }
        });

        acceptInviteFromReject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AcceptEventDialogFragment rsvpEvent = AcceptEventDialogFragment.newInstance(uid, event.getEventId(), event.getName(), true);
                rsvpEvent.show(getChildFragmentManager(), "fragment_rsvp_event");
            }
        });


        eventTitle.setText(event.getName());
        eventAddress.setText(event.getAddress());

        if(event.getCategory() == null || (event.getCategory().equals("")))
            eventCategory.setVisibility(View.GONE);
        else
            eventCategory.setText(event.getCategory());

        if(event.getDescription() == null || (event.getDescription().equals(""))){
            descripLayout.setVisibility(View.GONE);
            eventDescrip.setVisibility(View.GONE);
        }
        else {
            descripLayout.setVisibility(View.VISIBLE);
            eventDescrip.setText(event.getDescription());
        }

        if(event.getEventLength() == null){
            eventLength.setVisibility(View.GONE);
        }
        else {
            eventLength.setText(("Event is ").concat(event.getEventLength().toString()).concat(" minutes"));
        }

        if(event.getFinalTime() == null){
            try{
                Date time = event.getDueDate().toDate();
                String formattedTime = getString(R.string.due_time, time.toString());
                eventDue.setText(formattedTime);
            }
            catch(Exception e){
                eventDue.setText("No due date provided.");
            }
        }
        else {
            Date time = event.getFinalTime().toDate();
            String formattedTime = ("Happening on ").concat(time.toString());
            eventDue.setText(formattedTime);
        }

        setHostName();
        setButtons();
        setStatus();

        return v;
    }

    private void setHostName() {
        fops.getUserByUid(event.getHost(), userObject -> {
            User host = (User) userObject;
            String fName = host.getFirstName();
            String lName = host.getLastName();

            String hostName = "Hosted by ".concat(fName).concat(" ").concat(lName);
            eventHost.setText(hostName);
        });
    }

    private void setButtons() {
        if(event.getIsRsvped()){
            inviteButtons.setVisibility(View.GONE);
            publicButton.setVisibility(View.GONE);
            declineButton.setVisibility(View.VISIBLE);
            acceptButton.setVisibility(View.GONE);
        }
        else {
            if(eventType.equals("invited")){
                setInvitedButtons();
            }
            else if(eventType.equals("public")){
                if(event.getInvitationStatus() != null){
                    setInvitedButtons();
                }
                else {
                    inviteButtons.setVisibility(View.GONE);
                    publicButton.setVisibility(View.VISIBLE);
                    declineButton.setVisibility(View.GONE);
                    acceptButton.setVisibility(View.GONE);
                }
            }
        }
    }

    private void setInvitedButtons() {
        if(event.getInvitationStatus().equals("pending")){
            //show two buttons with options
            inviteButtons.setVisibility(View.VISIBLE);
            publicButton.setVisibility(View.GONE);
            declineButton.setVisibility(View.GONE);
            acceptButton.setVisibility(View.GONE);
        }
        else if(event.getInvitationStatus().equals("accepted")){
            inviteButtons.setVisibility(View.GONE);
            publicButton.setVisibility(View.GONE);
            declineButton.setVisibility(View.VISIBLE);
            acceptButton.setVisibility(View.GONE);
        }
        else {
            //rejected
            inviteButtons.setVisibility(View.GONE);
            publicButton.setVisibility(View.GONE);
            declineButton.setVisibility(View.GONE);
            acceptButton.setVisibility(View.VISIBLE);
        }
    }

    private void setStatus() {
        if(eventType.equals("invited")){
            String status = event.getInvitationStatus();
            formatInvite(status);
        }
        else if(eventType.equals("public")){
            eventStatus.setVisibility(View.VISIBLE);
            if(event.getInvitationStatus() != null){
                String status = event.getInvitationStatus();
                formatInvite(status);
            }
            else{
                if(event.getIsRsvped()){
                    String status = "Attending";
                    formatGeneral(status);
                }
                else {
                    eventStatus.setVisibility(View.GONE);
                }
            }
        }
        else{
            //rsvpd
            String status = "Attending";
            formatGeneral(status);
        }
    }

    private void formatInvite(String status){
        String formattedInvite = "";
        if(status.equals("attending")){
            formattedInvite = this.getString(R.string.invitation_status, "accepted");
            eventStatus.setTextColor(ContextCompat.getColor(this.getContext(), R.color.green));
        }
        else if(status.equals("rejected")) {
            formattedInvite = this.getString(R.string.invitation_status, status);
            eventStatus.setTextColor(ContextCompat.getColor(this.getContext(), R.color.coral));
        }
        else if(status.equals("pending")){
            formattedInvite = this.getString(R.string.invitation_status, "awaiting response");
            eventStatus.setTextColor(ContextCompat.getColor(this.getContext(), R.color.orange));
        }
        eventStatus.setText(formattedInvite);
    }

    private void formatGeneral(String status){
        eventStatus.setTextColor(ContextCompat.getColor(this.getContext(), R.color.green));
        eventStatus.setText(status);
    }
}
