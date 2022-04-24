package com.example.easyteamup.Activities.EventActivities.ViewEventActivities.EventDetailsActivities.Host;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.easyteamup.Activities.EventActivities.CreateEventActivities.LeaveCreateEventDialogFragment;
import com.example.easyteamup.Activities.EventActivities.ViewEventActivities.EventDispatcherActivity;
import com.example.easyteamup.Backend.Event;
import com.example.easyteamup.Backend.EventDetails;
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.R;
import com.google.firebase.Timestamp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpdateEventDialogFragment extends DialogFragment {

    private String uid;
    private String eid;
    private Event e;
    private FirebaseOperations fops;
    private FragmentManager fragmentManager;


    private EditText eventName, eventAddress, eventType, eventDescription, inviteEmail, eventLength;;
    private SwitchCompat publicPrivate;
    private Button inviteUser, addDueTime, updateEvent, cancelEvent;
    private TextView eventInvitees, dueTime, error, inviteError, eventLengthText;

    private boolean isPublic = false;
    private ArrayList<String> invitedUids = new ArrayList<String>();
    private Date dueDate;

    public UpdateEventDialogFragment(){

    }

    public static UpdateEventDialogFragment newInstance(String uid, String eid, Event event) {
        UpdateEventDialogFragment frag = new UpdateEventDialogFragment();
        Bundle args = new Bundle();

        args.putString("uid", uid);
        args.putString("eid", eid);
        args.putParcelable("event", event);

        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_update_event_dialog, container);


        uid = getArguments().getString("uid");
        eid = getArguments().getString("eid");
        e  = getArguments().getParcelable("event");
        fops = new FirebaseOperations(this.getContext());

        //setting up views
        eventName = (EditText) v.findViewById(R.id.event_title);
        eventAddress = (EditText) v.findViewById(R.id.event_address);
        eventType = (EditText) v.findViewById(R.id.event_type);
        eventDescription = (EditText) v.findViewById(R.id.event_description);
        inviteEmail = (EditText) v.findViewById(R.id.user_email_address);
        publicPrivate = (SwitchCompat) v.findViewById(R.id.privacy_toggle);
        inviteUser = (Button) v.findViewById(R.id.invite_user);
        addDueTime = (Button) v.findViewById(R.id.due_time);
        updateEvent = (Button) v.findViewById(R.id.submit_event);
        cancelEvent = (Button) v.findViewById(R.id.cancel_event);
        dueTime = (TextView) v.findViewById(R.id.due_submitted);
        error = (TextView) v.findViewById(R.id.error);
        inviteError = (TextView) v.findViewById(R.id.invite_error);
        eventInvitees = (TextView) v.findViewById(R.id.event_invitees);
        eventLengthText = (TextView) v.findViewById(R.id.event_length_text);
        eventLength = (EditText) v.findViewById(R.id.event_length);

        eventName.setText(e.getName());
        eventAddress.setText(e.getAddress());
        eventDescription.setText(e.getDescription());
        eventLength.setText(String.valueOf(e.getEventLength()));
        if(e.getIsPublic()){
            publicPrivate.setChecked(true);
        }
        else{
            publicPrivate.setChecked(false);
        }
        dueTime.setText(((e.getDueDate()).toDate()).toString());


        publicPrivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //public
                    isPublic = true;
                } else {
                    //private
                    isPublic = false;
                }
            }
        });

        inviteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inviteEmail.getText().toString();
                fops.getUserByEmail(email, id -> {
                    String inviteId = (String) id;
                    if(inviteId == null){
                        inviteError.setVisibility(View.VISIBLE);
                        inviteError.setText("That email is not registered with any EasyTeamUp user.");
                    }
                    else {
                        inviteError.setVisibility(View.GONE);
                        if(!invitedUids.contains(inviteId))
                            invitedUids.add(inviteId);

                        String invitees = (String) eventInvitees.getText();
                        if(invitees.equals("No new users have been invited to your event.")){
                            eventInvitees.setText(email);
                        }
                        else {
                            eventInvitees.setText(invitees.concat("\n").concat(email));
                        }

                        Log.d("UIDS", String.valueOf(invitedUids));
                    }
                });
            }
        });


        addDueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        updateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                Double newLat = null;
                Double newLon = null;
                String address = eventAddress.getText().toString();
                Double eventMinutes = Double.valueOf(eventLength.getText().toString());
                Geocoder geocoder = new Geocoder(getActivity().getBaseContext());

                try {
                    List<Address> addresses = geocoder.getFromLocationName(address.trim().toLowerCase(), 1);
                    if (addresses.size() > 0) {
                        newLat = addresses.get(0).getLatitude();
                        newLon = addresses.get(0).getLongitude();
                    }
                } catch (IOException ioe) {
                    error.setText("Please enter a valid address.");
                    newLat = e.getLatitude();
                    newLon = e.getLongitude();
                    Log.d("COORDS", String.valueOf(e.getLatitude()));
                }

                String name = eventName.getText().toString();

                if(dueDate == null){
                    dueDate = e.getDueDate().toDate();
                }

                if(name == null || name.equals("")){
                    name = e.getName();
                }

                if(eventMinutes == null || eventMinutes <= 0){
                    eventMinutes = e.getEventLength();
                }

                String category = eventType.getText().toString();
                String description = eventDescription.getText().toString();

                EventDetails ed = new EventDetails();

                ed.setName(name);
                ed.setAddress(address);
                ed.setCategory(category);
                ed.setDescription(description);
                Timestamp ts = new Timestamp(dueDate);
                ed.setDueDate(ts);
                ed.setLatitude(newLat);
                ed.setLongitude(newLon);
                ed.setEventLength(eventMinutes);

                fops.setEvent(eid, ed, bc -> {
                    if(bc){
                        Intent viewHosted = new Intent(getActivity(), EventDispatcherActivity.class);
                        viewHosted.putExtra("uid", uid);
                        viewHosted.putExtra("event_type", "hosting");
                        startActivity(viewHosted);
                    }
                    else{
                        LeaveCreateEventDialogFragment leave = LeaveCreateEventDialogFragment.newInstance(uid, "fail");
                        leave.show(fragmentManager, "fragment_leave_create_event");
                    }
                    for(int i = 0; i < invitedUids.size(); i++){
                        fops.inviteUserToEvent(uid, eid, inv -> {
                            if(inv){}
                            else {
                                LeaveCreateEventDialogFragment leave = LeaveCreateEventDialogFragment.newInstance(uid, "fail");
                                leave.show(fragmentManager, "fragment_leave_create_event");
                            }
                        });
                        Intent viewHosted = new Intent(getContext(), EventDispatcherActivity.class);
                        viewHosted.putExtra("uid", uid);
                        viewHosted.putExtra("event_type", "hosting");
                        startActivity(viewHosted);
                    }
                    if(e.getIsPublic() != isPublic){
                        if(e.getIsPublic()){
                            fops.convertPublicToPrivate(eid, false, cv -> {
                                if(cv){}
                                else {
                                    LeaveCreateEventDialogFragment leave = LeaveCreateEventDialogFragment.newInstance(uid, "fail");
                                    leave.show(fragmentManager, "fragment_leave_create_event");
                                }
                            });
                        }
                        else {
                            fops.convertPrivateToPublic(eid, cv -> {
                                if(cv){}
                                else {
                                    LeaveCreateEventDialogFragment leave = LeaveCreateEventDialogFragment.newInstance(uid, "fail");
                                    leave.show(fragmentManager, "fragment_leave_create_event");
                                }
                            });
                        }
                    }
                });

            }
        });

        cancelEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });


        return v;
    }
}
