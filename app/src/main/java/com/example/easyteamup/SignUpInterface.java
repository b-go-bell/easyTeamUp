package com.example.easyteamup;

import com.example.easyteamup.User;

public interface SignUpInterface {
    public void onFirstContinue(String email, String psd);
    public void onSecondContinue(boolean back, String firstName, String lastName, String phone);
    public void onThirdContinue(boolean back, String major, String year);
    public void onFourthContinue(boolean back, String bio);
    public void onFifthSubmit(boolean back, String image);
}
