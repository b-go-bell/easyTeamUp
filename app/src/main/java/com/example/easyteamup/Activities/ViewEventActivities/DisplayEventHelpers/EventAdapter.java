package com.example.easyteamup.Activities.ViewEventActivities.DisplayEventHelpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.easyteamup.R;

import java.util.ArrayList;

public class EventAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> eventsList = new ArrayList<>();
    private ArrayList<String> eventsStatus = new ArrayList<>();

    public EventAdapter(Context context, ArrayList<String> events, ArrayList<String> statuses) {
        super(context, 0, events);
        mContext = context;
        eventsList = events;
        eventsStatus = statuses;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_event,parent,false);

        String eid = eventsList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.event_name);
        name.setText(eid);

        return listItem;
    }
}
