package com.example.easyteamup.Activities.ViewEventActivities.EventDetailsActivities;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Date;

public class SelectedEventAvailableTimesViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Date>> availableTimes = new MutableLiveData<ArrayList<Date>>();

    public void updateAvailableTimes(ArrayList<Date> newTimes) {
        ArrayList<Date> times = availableTimes.getValue();
        if(times == null){
            times = new ArrayList<>();
        }

        for (int i = 0; i < newTimes.size(); i++) {
            times.add(newTimes.get(i));
        }
        availableTimes.setValue(times);
    }

    public void deleteTimes(){
        availableTimes.setValue(new ArrayList<>());
    }


    public MutableLiveData<ArrayList<Date>> getAvailableTimes() {
        return availableTimes;
    }
}
