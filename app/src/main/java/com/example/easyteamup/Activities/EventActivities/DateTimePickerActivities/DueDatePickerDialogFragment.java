package com.example.easyteamup.Activities.EventActivities.DateTimePickerActivities;

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
import com.kizitonwose.calendarview.ui.ViewContainer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DueDatePickerDialogFragment extends DialogFragment {

    private Button submitRange, cancel;
    private ImageButton prevMonth, nextMonth;
    private CalendarView calendarView;
    private TextView dueDisplay;

    private DueDateViewModel dueModel;


    public DueDatePickerDialogFragment() {
        // Required empty public constructor
    }

    public static DueDatePickerDialogFragment newInstance() {
        DueDatePickerDialogFragment fragment = new DueDatePickerDialogFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_due_picker_dialog, container, false);

        cancel = (Button) v.findViewById(R.id.cancel);
        submitRange = (Button) v.findViewById(R.id.submit_range);
        prevMonth = (ImageButton) v.findViewById(R.id.prev_month);
        nextMonth = (ImageButton) v.findViewById(R.id.next_month);
        calendarView = (CalendarView) v.findViewById(R.id.calendarView);
        dueDisplay = (TextView) v.findViewById(R.id.due);

        final int[] dueDay = new int[1];
        final int[] dueMonth = new int[1];

        dueModel = new ViewModelProvider(requireActivity()).get(DueDateViewModel.class);

        dueModel.getDueTime().observe(this, item -> {
            ZonedDateTime setUTCDueTime = item;
            if(setUTCDueTime != null){
                ZonedDateTime setDueTime = setUTCDueTime.withZoneSameInstant(TimeZone.getDefault().toZoneId());
                prettyPrintDate(v, setDueTime);
                dueDay[0] = setDueTime.getDayOfMonth();
                dueMonth[0] = setDueTime.getMonthValue();
                Log.d("DUE DAY", String.valueOf(dueDay[0]));
            }
            else {
                dueDisplay.setText("No due date currently selected");
                dueDisplay.setTextColor(ContextCompat.getColor(getContext(), R.color.coral));
            }
        });



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
                dayViewContainer.due = true;

                if(calendarDay.getDate().getMonthValue() == (dueMonth[0]) &&
                        calendarDay.getDate().getDayOfMonth() == (dueDay[0])){
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                }
                else if (calendarDay.getOwner() != DayOwner.THIS_MONTH ||
                        ((calendarDay.getDate().getDayOfMonth() <= LocalDate.now().getDayOfMonth()) && (calendarDay.getDate().getMonthValue() == LocalDate.now().getMonthValue()))) {
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.medium_gray));
                }
                else{
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
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
                (getDialog()).cancel();
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

    private void prettyPrintDate(View v, ZonedDateTime setDueTime){
        String datePrettyPrint = (setDueTime.getDayOfWeek().name()).concat(", ");
        datePrettyPrint = datePrettyPrint.concat(setDueTime.getMonth().toString()).concat(" ");
        datePrettyPrint = datePrettyPrint.concat(String.valueOf(setDueTime.getDayOfMonth())).concat(" at ");

        String prettyHours = "0";
        if(setDueTime.getHour() < 10){
            prettyHours = prettyHours.concat(String.valueOf(setDueTime.getHour()));
        }
        else {
            prettyHours = String.valueOf(setDueTime.getHour());
        }

        datePrettyPrint = datePrettyPrint.concat(prettyHours).concat(":");
        String prettyMins;
        if(setDueTime.getMinute() == 0){
            prettyMins = "00";
        }
        else {
            prettyMins = String.valueOf(setDueTime.getMinute());
        }

        datePrettyPrint = datePrettyPrint.concat(prettyMins);
        dueDisplay.setText(String.valueOf(datePrettyPrint));
        dueDisplay.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
    }
}
