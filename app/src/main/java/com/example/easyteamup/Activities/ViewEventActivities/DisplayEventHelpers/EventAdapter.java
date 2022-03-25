package com.example.easyteamup.Activities.ViewEventActivities.DisplayEventHelpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.easyteamup.Backend.Event;
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.Backend.User;
import com.example.easyteamup.R;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;

public class EventAdapter extends ArrayAdapter<Event> {
    private Context mContext;
    private FirebaseOperations fops;
    private ArrayList<Event> eventsList;

    public EventAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        mContext = context;
        eventsList = events;
        fops = new FirebaseOperations(mContext);
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

        TextView eventStatus = (TextView) listItem.findViewById(R.id.event_invitation);
        //modify
        String status = "pending";
        String formattedInvite = mContext.getString(R.string.invitation_status, status);
        eventStatus.setText(formattedInvite);
        if(status.equals("accepted")){
            eventStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        }
        else if(status.equals("rejected"))
            eventStatus.setTextColor(ContextCompat.getColor(mContext, R.color.coral));

        TextView dueTime = (TextView) listItem.findViewById(R.id.event_due);
        Date time = new Date(currentEvent.getDueDate().getSeconds()*1000);
        String formattedTime = mContext.getString(R.string.due_time, time.toString());
        dueTime.setText(formattedTime);

        return listItem;
    }
}
