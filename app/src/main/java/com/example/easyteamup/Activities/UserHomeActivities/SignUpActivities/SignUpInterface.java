package com.example.easyteamup.Activities.UserHomeActivities.SignUpActivities;

import android.net.Uri;

public interface SignUpInterface {
    public void onFirstContinue(String email, String psd);
    public void onSecondContinue(boolean back, String firstName, String lastName, long phone);
    public void onThirdContinue(boolean back, String major, int year);
    public void onFourthContinue(boolean back, String bio);
    public void onFifthSubmit(boolean back, Uri image);
}
