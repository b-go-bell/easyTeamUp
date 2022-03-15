package com.example.easyteamup;

import android.net.Uri;

import com.example.easyteamup.User;

public interface SignUpInterface {
    public void onFirstContinue(String email, String psd);
    public void onSecondContinue(boolean back, String firstName, String lastName, long phone);
    public void onThirdContinue(boolean back, String major, int year);
    public void onFourthContinue(boolean back, String bio);
    public void onFifthSubmit(boolean back, Uri image);
}
