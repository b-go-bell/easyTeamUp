package com.example.easyteamup.Activities.ViewEventActivities.DisplayEventHelpers;

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

import com.example.easyteamup.Activities.ViewEventActivities.EventDetailsActivities.Host.HostEventDetailsDialogFragment;
import com.example.easyteamup.Activities.ViewEventActivities.EventDetailsActivities.NonHost.EventDetailsDialogFragment;
import com.example.easyteamup.Backend.Event;
import com.example.easyteamup.R;

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

        if(currentEvent.getFinalTime() == null){
            TextView dueTime = (TextView) listItem.findViewById(R.id.event_due);
            Date time = currentEvent.getDueDate().toDate();
            String formattedTime = mContext.getString(R.string.due_time, time.toString());
            dueTime.setText(formattedTime);
        }
        else {
            TextView dueTime = (TextView) listItem.findViewById(R.id.event_due);
            Date time = currentEvent.getFinalTime().toDate();
            String formattedTime = ("Happening on ").concat(time.toString());
            dueTime.setText(formattedTime);
        }


        //showing status of the event for the logged in user
        eventStatus = (TextView) listItem.findViewById(R.id.event_invitation);

        if(uid.equals(currentEvent.getHost())){
            showHosted(currentEvent);
        }
        else if(eventType.equals("past")){
            String status;
            if(uid.equals(currentEvent.getHost())){
                status = "Hosted";
                eventStatus.setTextColor(ContextCompat.getColor(mContext, R.color.orange));
                eventStatus.setText(status);
            }
            else {
                status = "Attended";
                formatGeneral(status);
            }
        }
        else if(eventType.equals("invited")){

            Log.d("INVITED", currentEvent.getName());
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
        else {
            //rsvpd
            Log.d("ATTENDING", currentEvent.getName());
            String status = "Attending";
            formatGeneral(status);
        }

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uid.equals(currentEvent.getHost())){
                    HostEventDetailsDialogFragment hostDetails = HostEventDetailsDialogFragment.newInstance(uid, currentEvent);
                    hostDetails.show(fragmentManager, "fragment_event_details_dialog");
                }
                else if(eventType.equals("past")){}
                else {
                    EventDetailsDialogFragment viewEventDetails = EventDetailsDialogFragment.newInstance(uid, eventType, currentEvent);
                    viewEventDetails.show(fragmentManager, "fragment_event_details_dialog");
                }
            }
        });

        return listItem;
    }

    private void showHosted(Event currEvent) {
        String status;
        if(!currEvent.getIsPublic()){
            status = "Hosting private event";
            eventStatus.setTextColor(ContextCompat.getColor(mContext, R.color.orange));
        }
        else {
            status = "Hosting public event";
            eventStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        }
        eventStatus.setText(status);
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
