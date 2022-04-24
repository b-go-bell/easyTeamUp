package com.example.easyteamup.Activities.EventActivities.DateTimePickerActivities;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.ZonedDateTime;


public class DueDateViewModel extends ViewModel {
    private final MutableLiveData<ZonedDateTime> dueTime = new MutableLiveData<ZonedDateTime>();

    public void setDueTime(ZonedDateTime newDate) {
        dueTime.setValue(newDate);
    }

    public void deleteDueTime() {
        dueTime.setValue(null);
    }

    public MutableLiveData<ZonedDateTime> getDueTime() {
        return dueTime;
    }
}
