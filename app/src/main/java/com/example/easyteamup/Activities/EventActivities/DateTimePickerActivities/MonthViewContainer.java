package com.example.easyteamup.Activities.EventActivities.DateTimePickerActivities;

import android.view.View;
import android.widget.TextView;

import com.example.easyteamup.R;
import com.kizitonwose.calendarview.ui.ViewContainer;

class MonthViewContainer extends ViewContainer {

    public MonthViewContainer(View view) {
        super(view);
        TextView calendar_month_text = view.findViewById(R.id.headerTextView);
    }
}
