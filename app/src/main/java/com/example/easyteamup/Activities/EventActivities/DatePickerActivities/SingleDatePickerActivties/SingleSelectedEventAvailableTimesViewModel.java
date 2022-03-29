package com.example.easyteamup.Activities.EventActivities.DatePickerActivities.SingleDatePickerActivties;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

public class SingleSelectedEventAvailableTimesViewModel extends ViewModel {
    private MutableLiveData<Date> dueTime = new MutableLiveData<Date>();

    public void updateDueTime(Date newDate) {
        dueTime.setValue(newDate);
    }

    public void deleteDueTime() {
        dueTime.setValue(null);
    }

    public MutableLiveData<Date> getDueTime() {
        return dueTime;
    }
}


