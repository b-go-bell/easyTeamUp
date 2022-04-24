package com.example.easyteamup.Activities.EventActivities.DateTimePickerActivities;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

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
import java.util.TimeZone;

public class AvailableTimesPickerDialogFragment extends DialogFragment {

    private Button submitRange, cancel;
    private ImageButton prevMonth, nextMonth;
    private CalendarView calendarView;
    private TextView timesDisplay;
    private TextView picker;

    private ZonedDateTime dueDate;

    private AvailableTimesViewModel availableModel;

    public AvailableTimesPickerDialogFragment() {
        // Required empty public constructor
    }

    public static AvailableTimesPickerDialogFragment newInstance(ZonedDateTime dueDate) {
        AvailableTimesPickerDialogFragment fragment = new AvailableTimesPickerDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable("dueDate", dueDate);
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
        View v = inflater.inflate(R.layout.fragment_due_picker_dialog, container, false);

        ZonedDateTime dueDateUTC = (ZonedDateTime) getArguments().getSerializable("dueDate");
        dueDate = dueDateUTC.withZoneSameInstant(TimeZone.getDefault().toZoneId());

        cancel = (Button) v.findViewById(R.id.cancel);
        submitRange = (Button) v.findViewById(R.id.submit_range);
        prevMonth = (ImageButton) v.findViewById(R.id.prev_month);
        nextMonth = (ImageButton) v.findViewById(R.id.next_month);
        calendarView = (CalendarView) v.findViewById(R.id.calendarView);
        timesDisplay = (TextView) v.findViewById(R.id.due);
        picker = v.findViewById(R.id.picker_text);

        picker.setText("Pick your available days");
        timesDisplay.setText("No time ranges currently selected");
        submitRange.setText("Submit times");

        availableModel = new ViewModelProvider(requireActivity()).get(AvailableTimesViewModel.class);


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

                if(calendarDay.getDate().getYear() == dueDate.getYear() &&
                        calendarDay.getDate().getMonthValue() == dueDate.getMonthValue() &&
                        calendarDay.getDate().getDayOfMonth() == dueDate.getDayOfMonth()){
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                }
                else if (calendarDay.getOwner() != DayOwner.THIS_MONTH) {
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.medium_gray));
                }
                else if( (calendarDay.getDate().getDayOfMonth() <= LocalDate.now().getDayOfMonth()) &&
                         (calendarDay.getDate().getMonthValue() == LocalDate.now().getMonthValue()) ) {
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.medium_gray));
                }
                else if( (calendarDay.getDate().getDayOfMonth() <= dueDate.getDayOfMonth()) &&
                         (calendarDay.getDate().getMonthValue() == dueDate.getMonthValue()) &&
                         (calendarDay.getDate().getYear() == dueDate.getYear()) ){
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.medium_gray));
                }
                else{
                    availableModel.getAvailableTimes().observe((unwrap(v.getContext())), item -> {
                        HashMap<LocalDate, ArrayList<ZonedDateTime>> utcTimes = item;
                        if(item != null && !item.isEmpty()){

                            utcTimes.forEach((key, value) -> {
                                if(value != null && !value.isEmpty()) {
                                    if (calendarDay.getDate().getYear() == key.getYear() &&
                                            calendarDay.getDate().getMonthValue() == key.getMonthValue() &&
                                            calendarDay.getDate().getDayOfMonth() == key.getDayOfMonth()) {
                                        if((unwrap(getContext())) != null){
                                            textView.setTextColor(ContextCompat.getColor((unwrap(getContext())), R.color.blue));
                                        }
                                    }
                                }
                                else {
                                    if((unwrap(getContext())) != null) {
                                        textView.setTextColor(ContextCompat.getColor((unwrap(getContext())), R.color.gray));
                                    }
                                }
                            });
                        }
                        else {
                            if((unwrap(getContext())) != null) {
                                textView.setTextColor(ContextCompat.getColor((unwrap(getContext())), R.color.gray));
                            }
                        }
                    });
                }
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

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                availableModel.deleteAvailableTimes();
                (getDialog()).cancel();
            }
        });

        availableModel.getAvailableTimes().observe(this, item -> {
            HashMap<LocalDate, ArrayList<ZonedDateTime>> utcTimes = item;
            if(utcTimes != null && !utcTimes.isEmpty()){
                timesDisplay.setVisibility(View.GONE);
            }
        });


        submitRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (getDialog()).cancel();
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
}
