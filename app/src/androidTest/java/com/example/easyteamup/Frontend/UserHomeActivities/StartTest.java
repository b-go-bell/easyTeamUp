package com.example.easyteamup.Frontend.UserHomeActivities;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.easyteamup.Activities.UserHomeActivities.LoginLogoutActivities.LoginActivity;
import com.example.easyteamup.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.example.easyteamup.Activities.UserHomeActivities.StartActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StartTest {
    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(StartActivity.class);

    @Test
    public void testGoToLogin() {
        ActivityScenario loginScenario = rule.getScenario();
        onView(withId(R.id.login_button)).perform(click());
        //checks that has transitioned to login activity where you can click login
        onView(withId(R.id.submit_button)).check(matches(isDisplayed()));
    }
}
