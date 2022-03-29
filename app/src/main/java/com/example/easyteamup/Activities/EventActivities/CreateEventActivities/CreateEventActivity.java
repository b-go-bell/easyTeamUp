package com.example.easyteamup.Activities.EventActivities.CreateEventActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.easyteamup.Activities.EventActivities.DatePickerActivities.DoubleDatePickerActivities.DateTimePickerDialogFragment;
import com.example.easyteamup.Activities.EventActivities.DatePickerActivities.SingleDatePickerActivties.SingleDateTimePickerDialogFragment;
import com.example.easyteamup.Activities.EventActivities.DatePickerActivities.SingleDatePickerActivties.SingleSelectedEventAvailableTimesViewModel;
import com.example.easyteamup.Activities.SnackBarActivities.SnackBarFragment;
import com.example.easyteamup.Activities.SnackBarActivities.SnackBarInterface;
import com.example.easyteamup.Activities.EventActivities.DatePickerActivities.DoubleDatePickerActivities.SelectedEventAvailableTimesViewModel;
import com.example.easyteamup.Activities.EventActivities.ViewEventActivities.EventDispatcherActivity;
import com.example.easyteamup.Backend.Event;
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.Backend.TimeUtil;
import com.example.easyteamup.R;
import com.google.firebase.Timestamp;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity implements SnackBarInterface {

    private Intent getIntent;
    private String uid;
    private FirebaseOperations fops;
    private FragmentManager fragmentManager;
    private SelectedEventAvailableTimesViewModel availModel;
    private SingleSelectedEventAvailableTimesViewModel dueModel;

    private EditText eventName, eventAddress, eventDescription, inviteEmail, eventLength;
    private SwitchCompat publicPrivate;
    private Button inviteUser, addTime, addDueTime, createEvent, cancelEvent;
    private TextView submittedTimes, dueTime, error, inviteError, eventInvitees;

    private boolean isPublic = false;
    private ArrayList<String> invitedUids = new ArrayList<String>();
    private ArrayList<Date> availDates = new ArrayList<Date>();
    private Date dueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        getSupportActionBar().hide();
        availModel = new ViewModelProvider(this).get(SelectedEventAvailableTimesViewModel.class);
        dueModel = new ViewModelProvider(this).get(SingleSelectedEventAvailableTimesViewModel.class);
        fops = new FirebaseOperations(this);

        fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.snackbar_container, SnackBarFragment.class, bundle)
                .commit();

        getIntent = getIntent();
        uid = getIntent.getStringExtra("uid");

        //setting up views
        eventName = (EditText) findViewById(R.id.event_title);
        eventAddress = (EditText) findViewById(R.id.event_address);
        eventDescription = (EditText) findViewById(R.id.event_description);
        inviteEmail = (EditText) findViewById(R.id.user_email_address);
        publicPrivate = (SwitchCompat) findViewById(R.id.privacy_toggle);
        inviteUser = (Button) findViewById(R.id.invite_user);
        addTime = (Button) findViewById(R.id.times);
        addDueTime = (Button) findViewById(R.id.due_time);
        createEvent = (Button) findViewById(R.id.submit_event);
        cancelEvent = (Button) findViewById(R.id.cancel_event);
        submittedTimes = (TextView) findViewById(R.id.times_submitted);
        dueTime = (TextView) findViewById(R.id.due_submitted);
        error = (TextView) findViewById(R.id.error);
        inviteError = (TextView) findViewById(R.id.invite_error);
        eventInvitees = (TextView) findViewById(R.id.event_invitees);
        eventLength = (EditText) findViewById(R.id.event_length);

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
                    }
                });
            }
        });

        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dateTimePicker = DateTimePickerDialogFragment.newInstance();
                dateTimePicker.show(fragmentManager, "dateTimePicker");
            }
        });

        addDueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dueDateTimePicker = SingleDateTimePickerDialogFragment.newInstance();
                dueDateTimePicker.show(fragmentManager, "singleDateTimePicker");
            }
        });

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = eventName.getText().toString();

                Double eventMinutes = null;
                try {
                    eventMinutes = Double.valueOf(eventLength.getText().toString());
                }
                catch(NumberFormatException nfe){
                    error.setText("Please enter a valid event length time.");
                }

                Double lat = null;
                Double lon = null;
                String address = eventAddress.getText().toString();
                Geocoder geocoder = new Geocoder(getApplicationContext());

                try {
                    List<Address> addresses = geocoder.getFromLocationName(address.trim().toLowerCase(), 1);
                    if (addresses.size() > 0) {
                        lat = addresses.get(0).getLatitude();
                        lon = addresses.get(0).getLongitude();
                    }
                } catch (IOException ioe) {
                    error.setText("Please enter a valid address.");
                }

                if(dueDate == null){
                    error.setText("Please pick an event due time.");
                }
                else if(availDates == null || availDates.size() == 0){
                    error.setText("Please pick at least one available time.");
                }
                else if(lat == null || lon == null){
                    error.setText("Please enter a valid address.");
                }
                else if(name == null || name.equals("")){
                    error.setText("Please enter a name for your event.");
                }
                else if(eventMinutes == null || eventMinutes <= 0){
                    error.setText("Please enter a valid event length time.");
                }
                else {
                    Event e = new Event();
                    e.setName(name);
                    e.setAddress(address);
                    e.setLatitude(lat);
                    e.setLongitude(lon);

                    String description = eventDescription.getText().toString();
                    e.setDescription(description);
                    e.setIsPublic(isPublic);
                    Timestamp ts = new Timestamp(dueDate);
                    e.setDueDate(ts);
                    e.setEventLength(eventMinutes);

                    fops.createEvent(e, TimeUtil.getAvailability(availDates), eventId -> {
                        if(eventId != null){
                            for(int i = 0; i < invitedUids.size(); i++){
                                fops.inviteUserToEvent(invitedUids.get(i), (String)eventId, bool -> {
                                    if(bool) {
                                    }
                                    else {
                                        LeaveCreateEventDialogFragment leave = LeaveCreateEventDialogFragment.newInstance(uid, "fail");
                                        leave.show(fragmentManager, "fragment_leave_create_event");
                                    }
                                });
                            }
                            Intent viewHosted = new Intent(CreateEventActivity.this, EventDispatcherActivity.class);
                            viewHosted.putExtra("uid", uid);
                            viewHosted.putExtra("event_type", "hosting");
                            startActivity(viewHosted);
                        }
                        else {
                            LeaveCreateEventDialogFragment leave = LeaveCreateEventDialogFragment.newInstance(uid, "fail");
                            leave.show(fragmentManager, "fragment_leave_create_event");
                        }
                    });
                }
            }
        });

        cancelEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LeaveCreateEventDialogFragment leave = LeaveCreateEventDialogFragment.newInstance(uid, "home");
                leave.show(fragmentManager, "fragment_leave_create_event");
            }
        });

        availModel.getAvailableTimes().observe(this, item -> {
            availDates = item;
            if(item.size() == 0){
                submittedTimes.setText(R.string.no_times);
            }
            else {
                String dates = "";
                for(int i = 0; i < item.size(); i+=2){
                    dates = dates.concat(String.valueOf(item.get(i))).concat(" - ").concat(String.valueOf(item.get(i+1))).concat("\n");
                }
                Log.d("DATES", dates);
                submittedTimes.setText(dates);
            }
        });

        dueModel.getDueTime().observe(this, item -> {
            Log.d("ITEM?", String.valueOf(item));
            dueDate = item;

            if(dueDate == null){
                dueTime.setText(R.string.no_due_time);
            }
            else {
                String date = dueDate.toString();
                dueTime.setText(date);
            }
        });

    }


    public void viewPublicEvents(){
        LeaveCreateEventDialogFragment leave = LeaveCreateEventDialogFragment.newInstance(uid, "public");
        leave.show(fragmentManager, "fragment_leave_create_event");
    }

    public void viewInvitations(){
        LeaveCreateEventDialogFragment leave = LeaveCreateEventDialogFragment.newInstance(uid, "invite");
        leave.show(fragmentManager, "fragment_leave_create_event");
    }

    public void createEvent(){
        LeaveCreateEventDialogFragment leave = LeaveCreateEventDialogFragment.newInstance(uid, "create");
        leave.show(fragmentManager, "fragment_leave_create_event");
    }

    public void viewUserProfile(){
        LeaveCreateEventDialogFragment leave = LeaveCreateEventDialogFragment.newInstance(uid, "home");
        leave.show(fragmentManager, "fragment_leave_create_event");
    }

    public void viewUserHistory(){
        LeaveCreateEventDialogFragment leave = LeaveCreateEventDialogFragment.newInstance(uid, "history");
        leave.show(fragmentManager, "fragment_leave_create_event");
    }
}