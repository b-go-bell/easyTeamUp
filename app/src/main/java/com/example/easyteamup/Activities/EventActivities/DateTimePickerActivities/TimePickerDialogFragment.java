package com.example.easyteamup.Activities.EventActivities.DateTimePickerActivities;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.easyteamup.R;
import com.kizitonwose.calendarview.model.CalendarDay;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

public class TimePickerDialogFragment extends DialogFragment {

    private CalendarDay day;
    private Boolean due;

    private DueDateViewModel dueModel;
    private AvailableTimesViewModel availableModel;

    private TextView date, select;
    private Button returnToCal;

    private Button am1200, am1215, am1230, am1245, am100, am115, am130, am145, am200, am215, am230, am245, am300, am315, am330, am345,
            am400, am415, am430, am445, am500, am515, am530, am545, am600, am615, am630, am645, am700, am715, am730, am745,
            am800, am815, am830, am845, am900, am915, am930, am945, am1000, am1015, am1030, am1045, am1100, am1115, am1130, am1145,
            pm1200, pm1215, pm1230, pm1245, pm100, pm115, pm130, pm145, pm200, pm215, pm230, pm245, pm300, pm315, pm330, pm345,
            pm400, pm415, pm430, pm445, pm500, pm515, pm530, pm545, pm600, pm615, pm630, pm645, pm700, pm715, pm730, pm745,
            pm800, pm815, pm830, pm845, pm900, pm915, pm930, pm945, pm1000, pm1015, pm1030, pm1045, pm1100, pm1115, pm1130, pm1145;

    private Set<Button> timesButtons = new HashSet<>();

    public TimePickerDialogFragment() {
        // Required empty public constructor
    }

    public static TimePickerDialogFragment newInstance(CalendarDay day, Boolean due) {
        TimePickerDialogFragment fragment = new TimePickerDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable("day", day);
        args.putBoolean("due", due);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_times_picker, container, false);

        dueModel = new ViewModelProvider(requireActivity()).get(DueDateViewModel.class);
        availableModel = new ViewModelProvider(requireActivity()).get(AvailableTimesViewModel.class);

        day = (CalendarDay) getArguments().getSerializable("day");
        due = getArguments().getBoolean("due");

        date = (TextView) v.findViewById(R.id.date);
        select = (TextView) v.findViewById(R.id.select);

        if(!due){
            String str = "Select all available 15 minute blocks for ";
            str = str.concat(String.valueOf(day.getDate()));
            date.setVisibility(View.GONE);
            select.setText(str);
        }
        else {
            date.setText(String.valueOf(day.getDate()));
        }

        am1200 = (Button) v.findViewById(R.id.am1200);
        am1215 = (Button) v.findViewById(R.id.am1215);
        am1230 = (Button) v.findViewById(R.id.am1230);
        am1245 = (Button) v.findViewById(R.id.am1245);
        am100 = (Button) v.findViewById(R.id.am100);
        am115 = (Button) v.findViewById(R.id.am115);
        am130 = (Button) v.findViewById(R.id.am130);
        am145 = (Button) v.findViewById(R.id.am145);
        am200 = (Button) v.findViewById(R.id.am200);
        am215 = (Button) v.findViewById(R.id.am215);
        am230 = (Button) v.findViewById(R.id.am230);
        am245 = (Button) v.findViewById(R.id.am245);
        am300 = (Button) v.findViewById(R.id.am300);
        am315 = (Button) v.findViewById(R.id.am315);
        am330 = (Button) v.findViewById(R.id.am330);
        am345 = (Button) v.findViewById(R.id.am345);
        am400 = (Button) v.findViewById(R.id.am400);
        am415 = (Button) v.findViewById(R.id.am415);
        am430 = (Button) v.findViewById(R.id.am430);
        am445 = (Button) v.findViewById(R.id.am445);
        am500 = (Button) v.findViewById(R.id.am500);
        am515 = (Button) v.findViewById(R.id.am515);
        am530 = (Button) v.findViewById(R.id.am530);
        am545 = (Button) v.findViewById(R.id.am545);
        am600 = (Button) v.findViewById(R.id.am600);
        am615 = (Button) v.findViewById(R.id.am615);
        am630 = (Button) v.findViewById(R.id.am630);
        am645 = (Button) v.findViewById(R.id.am645);
        am700 = (Button) v.findViewById(R.id.am700);
        am715 = (Button) v.findViewById(R.id.am715);
        am730 = (Button) v.findViewById(R.id.am730);
        am745 = (Button) v.findViewById(R.id.am745);
        am800 = (Button) v.findViewById(R.id.am800);
        am815 = (Button) v.findViewById(R.id.am815);
        am830 = (Button) v.findViewById(R.id.am830);
        am845 = (Button) v.findViewById(R.id.am845);
        am900 = (Button) v.findViewById(R.id.am900);
        am915 = (Button) v.findViewById(R.id.am915);
        am930 = (Button) v.findViewById(R.id.am930);
        am945 = (Button) v.findViewById(R.id.am945);
        am1000 = (Button) v.findViewById(R.id.am1000);
        am1015 = (Button) v.findViewById(R.id.am1015);
        am1030 = (Button) v.findViewById(R.id.am1030);
        am1045 = (Button) v.findViewById(R.id.am1045);
        am1100 = (Button) v.findViewById(R.id.am1100);
        am1115 = (Button) v.findViewById(R.id.am1115);
        am1130 = (Button) v.findViewById(R.id.am1130);
        am1145 = (Button) v.findViewById(R.id.am1145);

        pm1200 = (Button) v.findViewById(R.id.pm1200);
        pm1215 = (Button) v.findViewById(R.id.pm1215);
        pm1230 = (Button) v.findViewById(R.id.pm1230);
        pm1245 = (Button) v.findViewById(R.id.pm1245);
        pm100 = (Button) v.findViewById(R.id.pm100);
        pm115 = (Button) v.findViewById(R.id.pm115);
        pm130 = (Button) v.findViewById(R.id.pm130);
        pm145 = (Button) v.findViewById(R.id.pm145);
        pm200 = (Button) v.findViewById(R.id.pm200);
        pm215 = (Button) v.findViewById(R.id.pm215);
        pm230 = (Button) v.findViewById(R.id.pm230);
        pm245 = (Button) v.findViewById(R.id.pm245);
        pm300 = (Button) v.findViewById(R.id.pm300);
        pm315 = (Button) v.findViewById(R.id.pm315);
        pm330 = (Button) v.findViewById(R.id.pm330);
        pm345 = (Button) v.findViewById(R.id.pm345);
        pm400 = (Button) v.findViewById(R.id.pm400);
        pm415 = (Button) v.findViewById(R.id.pm415);
        pm430 = (Button) v.findViewById(R.id.pm430);
        pm445 = (Button) v.findViewById(R.id.pm445);
        pm500 = (Button) v.findViewById(R.id.pm500);
        pm515 = (Button) v.findViewById(R.id.pm515);
        pm530 = (Button) v.findViewById(R.id.pm530);
        pm545 = (Button) v.findViewById(R.id.pm545);
        pm600 = (Button) v.findViewById(R.id.pm600);
        pm615 = (Button) v.findViewById(R.id.pm615);
        pm630 = (Button) v.findViewById(R.id.pm630);
        pm645 = (Button) v.findViewById(R.id.pm645);
        pm700 = (Button) v.findViewById(R.id.pm700);
        pm715 = (Button) v.findViewById(R.id.pm715);
        pm730 = (Button) v.findViewById(R.id.pm730);
        pm745 = (Button) v.findViewById(R.id.pm745);
        pm800 = (Button) v.findViewById(R.id.pm800);
        pm815 = (Button) v.findViewById(R.id.pm815);
        pm830 = (Button) v.findViewById(R.id.pm830);
        pm845 = (Button) v.findViewById(R.id.pm845);
        pm900 = (Button) v.findViewById(R.id.pm900);
        pm915 = (Button) v.findViewById(R.id.pm915);
        pm930 = (Button) v.findViewById(R.id.pm930);
        pm945 = (Button) v.findViewById(R.id.pm945);
        pm1000 = (Button) v.findViewById(R.id.pm1000);
        pm1015 = (Button) v.findViewById(R.id.pm1015);
        pm1030 = (Button) v.findViewById(R.id.pm1030);
        pm1045 = (Button) v.findViewById(R.id.pm1045);
        pm1100 = (Button) v.findViewById(R.id.pm1100);
        pm1115 = (Button) v.findViewById(R.id.pm1115);
        pm1130 = (Button) v.findViewById(R.id.pm1130);
        pm1145 = (Button) v.findViewById(R.id.pm1145);


        //set up the highlighted buttons properly
        if(due){
            dueModel.getDueTime().observe(this, item -> {
                ZonedDateTime setUTCDueTime = item;
                if(setUTCDueTime != null){
                    setButton(v, setUTCDueTime);
                }
            });
        }
        else {
            //mark available times that have been selected
            availableModel.getAvailableTimes().observe(this, item -> {
                HashMap<LocalDate, ArrayList<ZonedDateTime>> availTimes = item;
                if(availTimes != null){
                    availTimes.forEach((key, value) -> {
                        for(ZonedDateTime availUTCTime : value){
                            setButton(v, availUTCTime);
                            addButtonToAvailable(v, availUTCTime);
                        }
                    });
                }
            });
        }

        am1200.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am1200);
                else
                    availClick(am1200);
            }
        });

        am1215.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am1215);
                else
                    availClick(am1215);
            }
        });

        am1230.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am1230);
                else
                    availClick(am1230);
            }
        });

        am1245.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am1245);
                else
                    availClick(am1245);
            }
        });

        am100.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am100);
                else
                    availClick(am100);
            }
        });

        am115.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am115);
                else
                    availClick(am115);
            }
        });

        am130.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am130);
                else
                    availClick(am130);
            }
        });

        am145.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am145);
                else
                    availClick(am145);
            }
        });

        am200.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am200);
                else
                    availClick(am200);
            }
        });

        am215.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am215);
                else
                    availClick(am215);
            }
        });

        am230.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am230);
                else
                    availClick(am230);
            }
        });

        am245.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am245);
                else
                    availClick(am245);
            }
        });

        am300.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am300);
                else
                    availClick(am300);
            }
        });

        am315.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am315);
                else
                    availClick(am315);
            }
        });

        am330.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am330);
                else
                    availClick(am330);
            }
        });

        am345.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am345);
                else
                    availClick(am345);
            }
        });

        am400.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am400);
                else
                    availClick(am400);
            }
        });

        am415.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am415);
                else
                    availClick(am415);
            }
        });

        am430.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am430);
                else
                    availClick(am430);
            }
        });

        am445.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am445);
                else
                    availClick(am445);
            }
        });

        am500.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am500);
                else
                    availClick(am500);
            }
        });

        am515.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am515);
                else
                    availClick(am515);
            }
        });

        am530.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am530);
                else
                    availClick(am530);
            }
        });

        am545.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am545);
                else
                    availClick(am545);
            }
        });

        am600.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am600);
                else
                    availClick(am600);
            }
        });

        am615.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am615);
                else
                    availClick(am615);
            }
        });

        am630.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am630);
                else
                    availClick(am630);
            }
        });

        am645.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am645);
                else
                    availClick(am645);
            }
        });

        am700.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am700);
                else
                    availClick(am700);
            }
        });

        am715.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am715);
                else
                    availClick(am715);
            }
        });

        am730.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am730);
                else
                    availClick(am730);
            }
        });

        am745.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am745);
                else
                    availClick(am745);
            }
        });

        am800.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am800);
                else
                    availClick(am800);
            }
        });

        am815.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am815);
                else
                    availClick(am815);
            }
        });

        am830.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am830);
                else
                    availClick(am830);
            }
        });

        am845.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am845);
                else
                    availClick(am845);
            }
        });

        am900.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am900);
                else
                    availClick(am900);
            }
        });

        am915.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am915);
                else
                    availClick(am915);
            }
        });

        am930.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am930);
                else
                    availClick(am930);
            }
        });

        am945.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am945);
                else
                    availClick(am945);
            }
        });

        am1000.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am1000);
                else
                    availClick(am1000);
            }
        });

        am1015.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am1015);
                else
                    availClick(am1015);
            }
        });

        am1030.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am1030);
                else
                    availClick(am1030);
            }
        });

        am1045.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am1045);
                else
                    availClick(am1045);
            }
        });

        am1100.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am1100);
                else
                    availClick(am1100);
            }
        });

        am1115.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am1115);
                else
                    availClick(am1115);
            }
        });

        am1130.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am1130);
                else
                    availClick(am1130);
            }
        });

        am1145.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(am1145);
                else
                    availClick(am1145);
            }
        });

        pm1200.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm1200);
                else
                    availClick(pm1200);
            }
        });

        pm1215.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm1215);
                else
                    availClick(pm1215);
            }
        });

        pm1230.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm1230);
                else
                    availClick(pm1230);
            }
        });

        pm1245.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm1245);
                else
                    availClick(pm1245);
            }
        });

        pm100.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm100);
                else
                    availClick(pm100);
            }
        });

        pm115.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm115);
                else
                    availClick(pm115);
            }
        });

        pm130.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm130);
                else
                    availClick(pm1230);
            }
        });

        pm145.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm145);
                else
                    availClick(pm145);
            }
        });

        pm200.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm200);
                else
                    availClick(pm200);
            }
        });

        pm215.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm215);
                else
                    availClick(pm215);
            }
        });

        pm230.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm230);
                else
                    availClick(pm230);
            }
        });

        pm245.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm245);
                else
                    availClick(pm245);
            }
        });

        pm300.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm300);
                else
                    availClick(pm300);
            }
        });

        pm315.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm315);
                else
                    availClick(pm315);
            }
        });

        pm330.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm330);
                else
                    availClick(pm330);
            }
        });

        pm345.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm345);
                else
                    availClick(pm345);
            }
        });

        pm400.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm400);
                else
                    availClick(pm400);
            }
        });

        pm415.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm415);
                else
                    availClick(pm415);
            }
        });

        pm430.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm430);
                else
                    availClick(pm430);
            }
        });

        pm445.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm445);
                else
                    availClick(pm445);
            }
        });

        pm500.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm500);
                else
                    availClick(pm500);
            }
        });

        pm515.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm515);
                else
                    availClick(pm515);
            }
        });

        pm530.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm530);
                else
                    availClick(pm530);
            }
        });

        pm545.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm545);
                else
                    availClick(pm545);
            }
        });

        pm600.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm600);
                else
                    availClick(pm600);
            }
        });

        pm615.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm615);
                else
                    availClick(pm615);
            }
        });

        pm630.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm630);
                else
                    availClick(pm630);
            }
        });

        pm645.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm645);
                else
                    availClick(pm645);
            }
        });

        pm700.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm700);
                else
                    availClick(pm700);
            }
        });

        pm715.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm715);
                else
                    availClick(pm715);
            }
        });

        pm730.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm730);
                else
                    availClick(pm730);
            }
        });

        pm745.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm745);
                else
                    availClick(pm745);
            }
        });

        pm800.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm800);
                else
                    availClick(pm800);
            }
        });

        pm815.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm815);
                else
                    availClick(pm815);
            }
        });

        pm830.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm830);
                else
                    availClick(pm830);
            }
        });

        pm845.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm845);
                else
                    availClick(pm845);
            }
        });

        pm900.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm900);
                else
                    availClick(pm900);
            }
        });

        pm915.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm915);
                else
                    availClick(pm915);
            }
        });

        pm930.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm930);
                else
                    availClick(pm930);
            }
        });

        pm945.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm945);
                else
                    availClick(pm945);
            }
        });

        pm1000.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm1000);
                else
                    availClick(pm1000);
            }
        });

        pm1015.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm1015);
                else
                    availClick(pm1015);
            }
        });

        pm1030.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm1030);
                else
                    availClick(pm1030);
            }
        });

        pm1045.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm1045);
                else
                    availClick(pm1045);
            }
        });

        pm1100.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm1100);
                else
                    availClick(pm1100);
            }
        });

        pm1115.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm1115);
                else
                    availClick(pm1115);
            }
        });

        pm1130.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm1130);
                else
                    availClick(pm1130);
            }
        });

        pm1145.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due)
                    dueClick(pm1145);
                else
                    availClick(pm1145);
            }
        });



        returnToCal = (Button) v.findViewById(R.id.return_times);
        returnToCal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!due){
                    LocalDate key = day.getDate();
                    ArrayList<ZonedDateTime> value = new ArrayList<>();

                    for(Button b : timesButtons ){
                        LocalDate ld = day.getDate();

                        Pair<Integer, Integer> hrSec = getButtonTime(b);
                        LocalTime lt = LocalTime.of(hrSec.first, hrSec.second, 0, 0);

                        ZonedDateTime zdt = ZonedDateTime.of(ld, lt, TimeZone.getDefault().toZoneId());
                        ZonedDateTime availableZdtUtc = zdt.withZoneSameInstant(ZoneId.of("UTC"));

                        value.add(availableZdtUtc);
                    }
                    availableModel.addAvailableTime(key, value);

                    availableModel.getAvailableTimes().observe((unwrap(v.getContext())), item -> {
                        HashMap<LocalDate, ArrayList<ZonedDateTime>> availTimes = item;
                    });

                }
                (getDialog()).cancel();
            }
        });

        return v;
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if(window == null) return;
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int w = displaymetrics.widthPixels;
        int h = displaymetrics.heightPixels;

        WindowManager.LayoutParams params = window.getAttributes();
        params.width = w;
        params.height = h-100;
        window.setAttributes(params);
    }

    private void dueClick(Button b) {
        LocalDate ld = day.getDate();

        Pair<Integer, Integer> hrSec = getButtonTime(b);
        LocalTime lt = LocalTime.of(hrSec.first, hrSec.second, 0, 0);

        ZonedDateTime zdt = ZonedDateTime.of(ld, lt, TimeZone.getDefault().toZoneId());
        ZonedDateTime zdtUTC = zdt.withZoneSameInstant(ZoneId.of("UTC"));

        dueModel.setDueTime(zdtUTC);
        (getDialog()).cancel();
    }

    private Pair<Integer, Integer> getButtonTime(Button b){
        String buttonTime = b.getText().toString();
        String[] colonSplits = buttonTime.split(":");
        String[] spaceSplits = colonSplits[1].split(" ");

        int hr = Integer.parseInt(colonSplits[0]);
        if(spaceSplits[1].equals("AM") && hr == 12){
            hr -= 12;
        }
        else if(spaceSplits[1].equals("PM") && hr != 12){
            Log.d("PM", "here");
            hr += 12;
        }
        int mins = Integer.parseInt(spaceSplits[0]);

        return new Pair<>(hr, mins);
    }


    private void availClick(Button b) {
        if(!timesButtons.contains(b)){ //not in set of clicked times
            timesButtons.add(b);
            b.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue));
        }
        else { //in set of clicked times
            timesButtons.remove(b);
            b.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.beige));
        }
    }

    private void addButtonToAvailable(View v, ZonedDateTime utcTime) {
        ZonedDateTime setDueTime = utcTime.withZoneSameInstant(TimeZone.getDefault().toZoneId());
        LocalDate thisDate = day.getDate();
        if (setDueTime.getYear() == thisDate.getYear() &&
                setDueTime.getMonthValue() == thisDate.getMonthValue() &&
                setDueTime.getDayOfMonth() == thisDate.getDayOfMonth()) { //the day of the due date picked

            String mins = String.valueOf(setDueTime.getMinute());
            if (mins.equals("0")) {
                mins = mins.concat("0");
            }
            int hrs = setDueTime.getHour();

            String mornAft = "am";
            if (hrs == 0) {
                hrs = 12;
            } else if (hrs >= 12) {
                mornAft = "pm";
                if (hrs > 12) {
                    hrs -= 12;
                }
            }
            String buttonID = mornAft.concat(String.valueOf(hrs)).concat(String.valueOf(mins));
            Button b = v.findViewById(getResources().getIdentifier(buttonID, "id", getActivity().getPackageName()));

            timesButtons.add(b);
        }
    }

    //colors the correct local time button with the associated UTC Time
    private void setButton(View v, ZonedDateTime utcTime){
        ZonedDateTime setDueTime = utcTime.withZoneSameInstant(TimeZone.getDefault().toZoneId());
        LocalDate thisDate = day.getDate();
        if(setDueTime.getYear() == thisDate.getYear() &&
                setDueTime.getMonthValue() == thisDate.getMonthValue() &&
                setDueTime.getDayOfMonth() == thisDate.getDayOfMonth()){ //the day of the due date picked

            String mins = String.valueOf(setDueTime.getMinute());
            if(mins.equals("0")){
                mins = mins.concat("0");
            }
            int hrs = setDueTime.getHour();

            String mornAft = "am";
            if(hrs == 0){
                hrs = 12;
            }
            else if(hrs >= 12){
                mornAft = "pm";
                if(hrs > 12){
                    hrs -= 12;
                }
            }
            String buttonID = mornAft.concat(String.valueOf(hrs)).concat(String.valueOf(mins));
            Button b = v.findViewById(getResources().getIdentifier(buttonID, "id", getActivity().getPackageName()));

            if(due){
                b.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
            }
            else {
                b.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue));
            }
        }
    }

    private static FragmentActivity unwrap(Context context) {
        while (!(context instanceof FragmentActivity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        return (FragmentActivity) context;
    }
}
