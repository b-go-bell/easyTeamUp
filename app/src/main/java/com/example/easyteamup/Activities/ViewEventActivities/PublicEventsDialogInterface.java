package com.example.easyteamup.Activities.ViewEventActivities;

import com.firebase.geofire.GeoLocation;

public interface PublicEventsDialogInterface {
    void onSubmit(GeoLocation location, double radius);
}
