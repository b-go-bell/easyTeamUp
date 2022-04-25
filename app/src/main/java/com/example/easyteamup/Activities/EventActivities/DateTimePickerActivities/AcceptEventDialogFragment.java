package com.example.easyteamup.Activities.EventActivities.DateTimePickerActivities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.easyteamup.Activities.EventActivities.ViewEventActivities.EventDispatcherActivity;
import com.example.easyteamup.Activities.EventActivities.ViewEventActivities.ListEventActivities.ViewInvitedEventsActivity;
import com.example.easyteamup.Activities.UserHomeActivities.LoginLogoutActivities.FailDialogFragment;
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.Backend.TimeUtil;
import com.example.easyteamup.R;


import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AcceptEventDialogFragment extends DialogFragment {

    private String uid;
    private String eid;
    private String eventName;
    private boolean invite;

    private FirebaseOperations fops;
    private FragmentManager fragmentManager;

    private TextView pick, error;
    private Button goBack, submit;
    private ImageButton prevMonth, nextMonth;
    private CalendarView calendarView;

    private AvailableTimesViewModel availableModel;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_accept_event_dialog, container, false);

        uid = getArguments().getString("uid");
        eid = getArguments().getString("eid");
        eventName = getArguments().getString("eventName");
        invite = getArguments().getBoolean("inv");
        fops = new FirebaseOperations(this.getContext());
        fragmentManager = getChildFragmentManager();

        availableModel = new ViewModelProvider(requireActivity()).get(AvailableTimesViewModel.class);

        error = v.findViewById(R.id.error);
        goBack = (Button) v.findViewById(R.id.cancel);
        submit = (Button) v.findViewById(R.id.submit);
        prevMonth = (ImageButton) v.findViewById(R.id.prev_month);
        nextMonth = (ImageButton) v.findViewById(R.id.next_month);
        calendarView = (CalendarView) v.findViewById(R.id.calendarView);


        calendarView.setDayBinder(new DayBinder<DayViewContainer>(){

            @Override
            public DayViewContainer create(View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(DayViewContainer dayViewContainer, CalendarDay calendarDay) {
                TextView textView = dayViewContainer.getView().findViewById(R.id.calendarDayText);
                textView.setText(Integer.toString(calendarDay.getDate().getDayOfMonth()));

                dayViewContainer.day = calendarDay;
                dayViewContainer.due = false;
                dayViewContainer.host = false;


                fops.getHostAvailability(eid, mapObject -> {
                    HashMap<LocalDate, ArrayList<ZonedDateTime>> hostTimes = TimeUtil.receiveTimes((Map<String, List<Map<String, String>>>) mapObject);
                    dayViewContainer.hostTimes = hostTimes;
                    if(hostTimes != null && !hostTimes.isEmpty()){
                        hostTimes.forEach((key, value) -> {
                            if(!value.isEmpty()){
                                if( (calendarDay.getDate().getYear() == key.getYear()) &&
                                        (calendarDay.getDate().getMonthValue() == key.getMonthValue()) &&
                                        (calendarDay.getDate().getDayOfMonth() == key.getDayOfMonth()) ){
                                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                                }
                            }
                        });
                    }
                });
            }
        });

        calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthViewContainer>(){
            @Override
            public MonthViewContainer create(View view) {
                return new MonthViewContainer(view);
            }

            @Override
            public void bind(MonthViewContainer monthViewContainer, CalendarMonth calendarMonth) {
                TextView textView = monthViewContainer.getView().findViewById(R.id.headerTextView);
                String mth = calendarMonth.getYearMonth().getMonth().toString().toLowerCase();
                String yr = String.valueOf(calendarMonth.getYear());
                textView.setText(mth.concat(" ").concat(yr));
            }
        });

        calendarView.getContext();

        YearMonth currentMonth = YearMonth.now();
        YearMonth lastMonth = currentMonth.plusMonths(12);

        calendarView.setup(currentMonth, lastMonth, DayOfWeek.SUNDAY);
        calendarView.scrollToMonth(currentMonth);

        prevMonth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CalendarMonth month = calendarView.findFirstVisibleMonth();
                if(!((month.getMonth() == currentMonth.getMonthValue()) && (month.getYear() == currentMonth.getYear()))){
                    YearMonth prev = month.getYearMonth().minusMonths(1);
                    calendarView.scrollToMonth(prev);
                }
            }
        });

        nextMonth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CalendarMonth month = calendarView.findFirstVisibleMonth();
                if(!(month.getMonth() == lastMonth.getMonthValue() && month.getYear() == lastMonth.getYear())){
                    YearMonth next = month.getYearMonth().plusMonths(1);
                    calendarView.scrollToMonth(next);
                }
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                availableModel.deleteAvailableTimes();
                onCancel(getDialog());
                getDialog().cancel();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                availableModel.getAvailableTimes().observe(AcceptEventDialogFragment.this, item -> {
                    HashMap<LocalDate, ArrayList<ZonedDateTime>> utcTimes = item;
                    if(utcTimes != null && !utcTimes.isEmpty()){
                        Boolean[] cont = new Boolean[1];
                        cont[0] = false;
                        utcTimes.forEach( (key, value) -> {
                            if(!value.isEmpty()){
                                cont[0] = true;
                            }
                        });

                        if(cont[0]){
                            Map<String, List<Map<String, String>>> availability = TimeUtil.sendTimes(utcTimes);
                            fops.RSVPforEvent(uid, eid, availability, bool -> {
                                if(bool){
                                    Intent viewAttendingEventsMap = new Intent(getActivity(), EventDispatcherActivity.class);
                                    viewAttendingEventsMap.putExtra("uid", uid);
                                    viewAttendingEventsMap.putExtra("event_type", "attending");
                                    startActivity(viewAttendingEventsMap);
                                }
                                else {
                                    FailDialogFragment fail = FailDialogFragment.newInstance(uid, "fail");
                                    fail.show(fragmentManager, "fragment_fail");
                                }
                            });
                            Intent fetchUpdatedInvitations = new Intent(getActivity(), ViewInvitedEventsActivity.class);
                            fetchUpdatedInvitations.putExtra("uid", uid);
                            startActivity(fetchUpdatedInvitations);
                        }
                        else {
                            error.setText("Please pick at least one available time.");
                        }
                    }
                    else {
                        error.setText("Please pick at least one available time.");
                    }
                });
            }
        });

        return v;
    }

    private static FragmentActivity unwrap(Context context) {
        while (!(context instanceof FragmentActivity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        return (FragmentActivity) context;
    }

    @Override
    public void onCancel(DialogInterface dlg) {
        super.onCancel(dlg);
    }
}