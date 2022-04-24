package com.example.easyteamup.Activities.EventActivities.DateTimePickerActivities;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.easyteamup.R;

import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.ViewContainer;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.TimeZone;

class DayViewContainer extends ViewContainer {
    CalendarDay day = null;
    Boolean due = null;
    private FragmentManager fragmentManager;

    private DueDateViewModel dueModel;


    public DayViewContainer(View view) {
        super(view);
        final TextView calendar_day_text = view.findViewById(R.id.calendarDayText);

        dueModel = new ViewModelProvider((unwrap(view.getContext()))).get(DueDateViewModel.class);


        dueModel.getDueTime().observe((unwrap(view.getContext())), item -> {
            ZonedDateTime setUTCDueTime = item;
            if (setUTCDueTime != null && day != null) {
                ZonedDateTime setDueTime = setUTCDueTime.withZoneSameInstant(TimeZone.getDefault().toZoneId());
                if(day.getDate().getMonthValue() == setDueTime.getMonthValue() &&
                        day.getDate().getDayOfMonth() == setDueTime.getDayOfMonth()){
                    calendar_day_text.setTextColor(ContextCompat.getColor((unwrap(view.getContext())), R.color.green));
                }
                else if (day.getOwner() != DayOwner.THIS_MONTH ||
                        ((day.getDate().getDayOfMonth() <= LocalDate.now().getDayOfMonth()) && (day.getDate().getMonthValue() == LocalDate.now().getMonthValue()))) {
                    calendar_day_text.setTextColor(ContextCompat.getColor((unwrap(view.getContext())), R.color.medium_gray));
                }
                else{
                    calendar_day_text.setTextColor(ContextCompat.getColor((unwrap(view.getContext())), R.color.gray));
                }
            }
        });
                fragmentManager = (unwrap(view.getContext())).getSupportFragmentManager();

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(due){
                    if(day.getOwner() == DayOwner.THIS_MONTH){
                        if(day.getDate().getMonthValue() != LocalDate.now().getMonthValue() ||
                                (day.getDate().getDayOfMonth() > LocalDate.now().getDayOfMonth())){
                            DialogFragment dueTimePicker = TimePickerDialogFragment.newInstance(day, due);
                            dueTimePicker.show(fragmentManager, "dialog");
                        }
                    }
                }
            }
        });
    }

    private static FragmentActivity unwrap(Context context) {
        while (!(context instanceof FragmentActivity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        return (FragmentActivity) context;
    }
}
