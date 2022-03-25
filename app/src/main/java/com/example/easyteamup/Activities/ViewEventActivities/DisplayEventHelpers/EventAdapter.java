package com.example.easyteamup.Activities.ViewEventActivities.DisplayEventHelpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.easyteamup.Backend.Event;
import com.example.easyteamup.R;

import java.util.ArrayList;

public class EventAdapter extends ArrayAdapter<Event> {
    private Context mContext;
    private ArrayList<String> eventsList = new ArrayList<>();
    private ArrayList<String> eventsStatus = new ArrayList<>();

    public EventAdapter(Context context, ArrayList<Event> events) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;

        return listItem;
    }
}
