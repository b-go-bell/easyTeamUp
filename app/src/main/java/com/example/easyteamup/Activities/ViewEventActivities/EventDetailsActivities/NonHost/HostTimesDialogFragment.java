package com.example.easyteamup.Activities.ViewEventActivities.EventDetailsActivities.NonHost;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.easyteamup.Activities.ViewEventActivities.EventDetailsActivities.Host.ViewRSVPsDialogFragment;
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.Backend.User;
import com.example.easyteamup.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HostTimesDialogFragment extends DialogFragment {
    private FirebaseOperations fops;
    private String eid;

    private TextView hostTimes;
    private Button goBack;

    public HostTimesDialogFragment() {
    }

    public static HostTimesDialogFragment newInstance(String eid) {
        HostTimesDialogFragment frag = new HostTimesDialogFragment();
        Bundle args = new Bundle();
        args.putString("eid", eid);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_rsvps_dialog, container);
        eid = getArguments().getString("eid");
        fops = new FirebaseOperations(this.getContext());

        goBack = (Button) v.findViewById(R.id.cancel);
        hostTimes = (TextView) v.findViewById(R.id.event_rsvps);

        goBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                (getDialog()).cancel();
            }
        });

        fops.getHostAvailability(eid, mapObject -> {
            HashMap<String, List<Map<String, String>>> times = (HashMap) mapObject;
            Iterator it = times.entrySet().iterator();

            if(times.isEmpty()){
                hostTimes.setText("Looks like the host set no available times!");
            }
            else {
                try {
                    while (it.hasNext()) {
                        Map.Entry mapElement = (Map.Entry)it.next();
                        ArrayList<Map<String, String>> lst = (ArrayList<Map<String, String>>) mapElement.getValue();

                        for(int i = 0; i < lst.size(); i++){
                            HashMap<String, String> dayTimes = (HashMap) lst.get(i);
                            Log.d("DAY TIMES", String.valueOf(dayTimes));
                            Iterator itr = dayTimes.entrySet().iterator();
                            while(itr.hasNext()){
                                Map.Entry timeElementStart = (Map.Entry)itr.next();
                                Map.Entry timeElementEnd = (Map.Entry)itr.next();
                                String prnt = (String) hostTimes.getText();
                                if(prnt == null || prnt.equals("")){
                                    prnt = "";
                                }
                                else{
                                    prnt = prnt.concat("\n\n");
                                }
                                prnt = prnt.concat((String)mapElement.getKey()).concat(": ")
                                        .concat((String)timeElementStart.getValue()).concat(" - ")
                                        .concat((String)timeElementEnd.getValue());
                                hostTimes.setText(prnt);
                            }
                        }
                    }
                }
                catch(Exception e) {
                    hostTimes.setText("There was an issue getting the host times...");
                }
            }
        });


        return v;
    }
}
