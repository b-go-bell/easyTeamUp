package com.example.easyteamup.Activities.ViewEventActivities.EventDetailsActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.easyteamup.Activities.DatePickerActivities.DateTimePickerDialogFragment;
import com.example.easyteamup.Activities.DatePickerActivities.SelectedEventAvailableTimesViewModel;
import com.example.easyteamup.Activities.ViewEventActivities.ListEventActivities.ViewInvitedEventsActivity;
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.R;

import java.util.ArrayList;
import java.util.Date;

public class AcceptEventDialogFragment extends DialogFragment {

    private String uid;
    private String eid;
    private String eventName;
    private boolean invite;

    private FirebaseOperations fops;
    private FragmentManager fragmentManager;
    private SelectedEventAvailableTimesViewModel model;

    private Button goBack;
    private Button pickTime;
    private Button submit;

    private TextView timesSubmitted, error;

    private ArrayList<Date> availDates;

    public AcceptEventDialogFragment() {
        // Required empty public constructor
    }

    public static AcceptEventDialogFragment newInstance(String uid, String eid, String eventName, boolean invite) {
        AcceptEventDialogFragment fragment = new AcceptEventDialogFragment();
        Bundle args = new Bundle();
        args.putString("uid", uid);
        args.putString("eid", eid);
        args.putString("eventName", eventName);
        args.putBoolean("inv", invite);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_accept_event_dialog, container, false);
        model = new ViewModelProvider(requireActivity()).get(SelectedEventAvailableTimesViewModel.class);

        uid = getArguments().getString("uid");
        eid = getArguments().getString("eid");
        eventName = getArguments().getString("eventName");
        invite = getArguments().getBoolean("inv");
        fops = new FirebaseOperations(this.getContext());
        fragmentManager = getChildFragmentManager();


        goBack = (Button) v.findViewById(R.id.cancel);
        pickTime = (Button) v.findViewById(R.id.times);
        submit = (Button) v.findViewById(R.id.submit);
        timesSubmitted = (TextView) v.findViewById(R.id.times_submitted);
        error = (TextView) v.findViewById(R.id.error);

        goBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCancel(getDialog());
                getDialog().cancel();
            }
        });

        pickTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment dateTimePicker = DateTimePickerDialogFragment.newInstance();
                dateTimePicker.show(fragmentManager, "dateTimePicker");
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(availDates == null || availDates.size() == 0){
                    error.setText("Please pick at least one available time.");
                }
                else {
                    //PASS OFF THE INFO TO GET SCHEDULED
                    //FOPS STUFF

                    Intent fetchUpdatedInvitations = new Intent(getActivity(), ViewInvitedEventsActivity.class);
                    fetchUpdatedInvitations.putExtra("uid", uid);
                    startActivity(fetchUpdatedInvitations);
                }
            }
        });

        model.getAvailableTimes().observe(this, item -> {
            Log.d("ITEM?", String.valueOf(item));
            availDates = item;

            if(item.size() == 0){
                timesSubmitted.setText(R.string.no_times);
            }
            else {
                String dates = "";
                for(int i = 0; i < item.size(); i+=2){
                    dates = dates.concat(String.valueOf(item.get(i))).concat(" - ").concat(String.valueOf(item.get(i+1))).concat("\n");
                }
                Log.d("DATES", dates);
                timesSubmitted.setText(dates);
            }
        });
        return v;
    }

    @Override
    public void onCancel(DialogInterface dlg) {
        model.deleteTimes();
        super.onCancel(dlg);
    }
}