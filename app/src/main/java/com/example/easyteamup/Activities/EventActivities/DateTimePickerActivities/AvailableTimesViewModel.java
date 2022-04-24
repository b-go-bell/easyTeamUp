package com.example.easyteamup.Activities.EventActivities.DateTimePickerActivities;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class AvailableTimesViewModel extends ViewModel {
    private final MutableLiveData<HashMap<LocalDate, ArrayList<ZonedDateTime>>> availableTimes = new MutableLiveData<HashMap<LocalDate, ArrayList<ZonedDateTime>>>();

    public void addAvailableTime(LocalDate key, ArrayList<ZonedDateTime> value) {
        HashMap<LocalDate, ArrayList<ZonedDateTime>> currMap = availableTimes.getValue();

        if(currMap != null && currMap.containsKey(key)){
            currMap.replace(key, value);
        }
        else {
            if(currMap == null){
                currMap = new HashMap<>();
                currMap.put(key, value);
            }
            else {
                currMap.put(key, value);
            }
        }

        availableTimes.setValue(currMap);
    }

    public void deleteAvailableTimes() {
        availableTimes.setValue(null);
    }

    public MutableLiveData<HashMap<LocalDate, ArrayList<ZonedDateTime>>> getAvailableTimes() {
        return availableTimes;
    }
}
