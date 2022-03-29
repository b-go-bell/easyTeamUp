package com.example.easyteamup.Activities.EventActivities.DatePickerActivities.DoubleDatePickerActivities;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.easyteamup.R;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateTimePickerDialogFragment extends DialogFragment {

    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;

    private Button submitRange, cancel;

    private SelectedEventAvailableTimesViewModel model;
    private ArrayList<Date> ranges;

    public DateTimePickerDialogFragment() {
        // Required empty public constructor
    }

    public static DateTimePickerDialogFragment newInstance() {
        DateTimePickerDialogFragment fragment = new DateTimePickerDialogFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_date_time_picker_dialog, container, false);
        model = new ViewModelProvider(requireActivity()).get(SelectedEventAvailableTimesViewModel.class);
        ranges = new ArrayList<>();

        startDatePicker = (DatePicker) v.findViewById(R.id.start_date_picker);
        endDatePicker = (DatePicker) v.findViewById(R.id.end_date_picker);
        startTimePicker = (TimePicker) v.findViewById(R.id.start_time_picker);
        endTimePicker = (TimePicker) v.findViewById(R.id.end_time_picker);

        // TO DO -> take host availability and and set their first time availlable listed and last time available
        // and set them as the ranges
        // USE FOPS getHostAvailability() to set a limit
        // will need to bundle info if using
        //come back to this later
//        startDatePicker.setMinDate();
//        startDatePicker.setMaxDate();
//        endDatePicker.setMinDate();
//        endDatePicker.setMaxDate();

        cancel = (Button) v.findViewById(R.id.cancel);
        submitRange = (Button) v.findViewById(R.id.submit_range);

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                (getDialog()).cancel();
            }
        });

        submitRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int startYear = startDatePicker.getYear();
                int startMonth = startDatePicker.getMonth();
                int startDay = startDatePicker.getDayOfMonth();
                int startHour = startTimePicker.getHour();
                int startMinute = startTimePicker.getMinute();

                int endYear = endDatePicker.getYear();
                int endMonth = endDatePicker.getMonth();
                int endDay = endDatePicker.getDayOfMonth();
                int endHour = endTimePicker.getHour();
                int endMinute = endTimePicker.getMinute();

                Calendar startCal = Calendar.getInstance();
                startCal.set(startYear, startMonth, startDay, startHour, startMinute);

                Calendar endCal = Calendar.getInstance();
                endCal.set(endYear, endMonth, endDay, endHour, endMinute);

                Log.d("START TIME", String.valueOf(startCal.getTime()));
                Log.d("END TIME", String.valueOf(endCal.getTime()));

                ranges.add(startCal.getTime());
                ranges.add(endCal.getTime());

                model.updateAvailableTimes(ranges);

                (getDialog()).cancel();
            }
        });
        return v;
    }

}