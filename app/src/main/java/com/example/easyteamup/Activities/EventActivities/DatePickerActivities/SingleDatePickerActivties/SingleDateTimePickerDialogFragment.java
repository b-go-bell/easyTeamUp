package com.example.easyteamup.Activities.EventActivities.DatePickerActivities.SingleDatePickerActivties;

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

import java.util.Calendar;
import java.util.Date;

public class SingleDateTimePickerDialogFragment extends DialogFragment {

    private DatePicker dueDatePicker;
    private TimePicker dueTimePicker;

    private Button submitRange, cancel;

    private SingleSelectedEventAvailableTimesViewModel model;
    private Date dueDate;

    public SingleDateTimePickerDialogFragment() {
        // Required empty public constructor
    }

    public static SingleDateTimePickerDialogFragment newInstance() {
        SingleDateTimePickerDialogFragment fragment = new SingleDateTimePickerDialogFragment();
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
        View v = inflater.inflate(R.layout.fragment_single_date_time_picker_dialog, container, false);
        model = new ViewModelProvider(requireActivity()).get(SingleSelectedEventAvailableTimesViewModel.class);

        dueDatePicker = (DatePicker) v.findViewById(R.id.date_picker);
        dueTimePicker = (TimePicker) v.findViewById(R.id.start_time_picker);

        Log.d("DUEDATE", String.valueOf(dueDatePicker));

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
                int dueYear = dueDatePicker.getYear();
                int dueMonth = dueDatePicker.getMonth();
                int dueDay = dueDatePicker.getDayOfMonth();
                int dueHour = dueTimePicker.getHour();
                int dueMinute = dueTimePicker.getMinute();


                Calendar dueCal = Calendar.getInstance();
                dueCal.set(dueYear, dueMonth, dueDay, dueHour, dueMinute);

                dueDate = dueCal.getTime();

                model.updateDueTime(dueDate);

                (getDialog()).cancel();
            }
        });
        return v;
    }

}
