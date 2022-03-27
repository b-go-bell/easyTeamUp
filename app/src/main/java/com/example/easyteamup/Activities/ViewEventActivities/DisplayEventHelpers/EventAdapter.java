package com.example.easyteamup.Activities.ViewEventActivities.DisplayEventHelpers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.easyteamup.Activities.ViewEventActivities.EventDetailsActivities.EventDetailsDialogFragment;
import com.example.easyteamup.Activities.ViewEventActivities.FilterEvents.PublicEventsDialogFragment;
import com.example.easyteamup.Backend.Event;
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.Backend.User;
import com.example.easyteamup.R;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;

public class EventAdapter extends ArrayAdapter<Event> {
    private Context mContext;
    private FragmentManager fragmentManager;

    private String uid;
    private String eventType;
    private ArrayList<Event> eventsList;
    private ArrayList<String> eventsStatuses;

    private TextView eventStatus;

    public EventAdapter(Context context, String id, String eType, ArrayList<Event> events) {
        super(context, 0, events);
        mContext = context;
        fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();
        uid = id;
        eventType = eType;
        eventsList = events;
    }

    public EventAdapter(Context context, String id, String eType, ArrayList<Event> events, ArrayList<String> statuses) {
        super(context, 0, events);
        mContext = context;
        fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();
        uid = id;
        eventsList = events;
        eventType = eType;
        eventsStatuses = statuses;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_event,parent,false);

        Event currentEvent = eventsList.get(position);

        TextView eventName = (TextView) listItem.findViewById(R.id.event_name);
        eventName.setText(currentEvent.getName());

        TextView eventHostLoc = (TextView) listItem.findViewById(R.id.event_location);
        String formattedHostLoc = mContext.getString(R.string.event_location, currentEvent.getAddress());
        eventHostLoc.setText(formattedHostLoc);

        TextView dueTime = (TextView) listItem.findViewById(R.id.event_due);
        Date time = new Date(currentEvent.getDueDate().getSeconds()*1000);
        String formattedTime = mContext.getString(R.string.due_time, time.toString());
        dueTime.setText(formattedTime);

        //showing status of the event for the logged in user
        eventStatus = (TextView) listItem.findViewById(R.id.event_invitation);
        if(eventType.equals("invited")){
            String status = currentEvent.getInvitationStatus();
            formatInvite(status);
        }
        else if(eventType.equals("public")){
            eventStatus.setVisibility(View.VISIBLE);
            if(currentEvent.getInvitationStatus() != null){
                String status = currentEvent.getInvitationStatus();
                formatInvite(status);
            }
            else{
                if(currentEvent.getIsRsvped()){
                    String status = "Attending";
                    formatGeneral(status);
                }
                else {
                    eventStatus.setVisibility(View.GONE);
                }
            }
        }
        else if(eventType.equals("hosted")){
            String status;
            if(!currentEvent.getIsPublic()){
                status = "Private";
                eventStatus.setTextColor(ContextCompat.getColor(mContext, R.color.coral));
            }
            else {
                status = "Public";
                eventStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green));
            }
            eventStatus.setText(status);
        }
        else{
            //rsvpd
            Log.d("ATTENDING", currentEvent.getName());
            String status = "Attending";
            formatGeneral(status);
        }

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventType.equals("hosted")){
                    /*
                        FILL IN
                     */
                }
                else {
                    EventDetailsDialogFragment viewEventDetails = EventDetailsDialogFragment.newInstance(uid, eventType, currentEvent);
                    viewEventDetails.show(fragmentManager, "fragment_event_details_dialog");
                }
            }
        });

        return listItem;
    }

    private void formatGeneral(String status){
        eventStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        eventStatus.setText(status);
    }

    private void formatInvite(String status){
        String formattedInvite = "";
        if(status.equals("attending")){
            formattedInvite = mContext.getString(R.string.invitation_status, "accepted");
            eventStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        }
        else if(status.equals("rejected")) {
            formattedInvite = mContext.getString(R.string.invitation_status, status);
            eventStatus.setTextColor(ContextCompat.getColor(mContext, R.color.coral));
        }
        else if(status.equals("pending")){
            formattedInvite = mContext.getString(R.string.invitation_status, "awaiting response");
            eventStatus.setTextColor(ContextCompat.getColor(mContext, R.color.orange));
        }
        eventStatus.setText(formattedInvite);
    }

}
