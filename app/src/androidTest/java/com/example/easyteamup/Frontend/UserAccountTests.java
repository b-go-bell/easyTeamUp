package com.example.easyteamup.Frontend;

import java.util.Random;
import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.easyteamup.Activities.UserHomeActivities.LoginLogoutActivities.LoginActivity;
import com.example.easyteamup.Activities.UserHomeActivities.StartActivity;
import com.example.easyteamup.Activities.UserHomeActivities.ViewProfileActivity;
import com.example.easyteamup.R;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserAccountTests {
    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(StartActivity.class);

    @Before
    public void init() {
        ActivityScenario scenario = rule.getScenario();
        try{
            Thread.sleep(1000);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        try {
            onView(withId(R.id.invitations_text)).check(matches(isDisplayed()));
            onView(withId(R.id.logout_button)).perform(scrollTo(), click());
            onView(withId(R.id.logout_button)).perform( click());
        }
        catch(NoMatchingViewException e){

        }
        //sleep for 10 seconds to let async logout happen
        try{
            Thread.sleep(10000);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void badLogin() {
        //invalid email, invalid password
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.username_text)).perform(typeText("abc"));
        onView(withId(R.id.password_text)).perform(typeText("abc"), closeSoftKeyboard());
        onView(withId(R.id.submit_button)).perform(click());
        //wait for async transition
        try{
            Thread.sleep(8000);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        onView(withId(R.id.bad_login)).check(matches(isDisplayed()));

        //valid but no account email, invalid password
        onView(withId(R.id.username_text)).perform(clearText(), typeText("bridget.go.bell@gmail.com"));
        onView(withId(R.id.password_text)).perform(clearText(), typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.submit_button)).perform(click());
        //wait for async transition
        try{
            Thread.sleep(8000);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        onView(withId(R.id.bad_login)).check(matches(isDisplayed()));

        //valid email, invalid password
        onView(withId(R.id.username_text)).perform(clearText(), typeText("bridget.test@gmail.com"));
        onView(withId(R.id.password_text)).perform(clearText(), typeText("wrongPassword"), closeSoftKeyboard());
        onView(withId(R.id.submit_button)).perform(click());
        //wait for async transition
        try{
            Thread.sleep(8000);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        onView(withId(R.id.bad_login)).check(matches(isDisplayed()));
    }


    @Test
    public void loginLogout() {
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.username_text)).perform(typeText("bridget.test@gmail.com"));
        onView(withId(R.id.password_text)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.submit_button)).perform(click());

        //wait for async transition for 10 seconds
        try{
            Thread.sleep(10000);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        onView(withId(R.id.invitations_text)).check(matches(isDisplayed()));
    }

    @Test
    public void signup() {
        onView(withId(R.id.signup_button)).perform(click());
        Random rand = new Random();
        Integer num = rand.nextInt();

        String newUser = ("user").concat(num.toString());
        String newUserEmail = newUser.concat("@gmail.com");
        String psd = "password";

        onView(withId(R.id.username_text)).perform(typeText(newUserEmail));
        onView(withId(R.id.password_text)).perform(typeText(psd), closeSoftKeyboard());
        onView(withId(R.id.continue_button)).perform(click());

        //wait for async transition of registering user email for 10 seconds
        try{
            Thread.sleep(5000);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        //filling out all user info; does not include info on non-necessary stuff

        onView(withId(R.id.first_name_text)).perform(typeText(newUser));
        onView(withId(R.id.last_name_text)).perform(typeText("Last"));
        onView(withId(R.id.phone_text)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.continue_button)).perform(click());

        //wait for async transition of registering user email for 10 seconds
        try{
            Thread.sleep(5000);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        onView(withId(R.id.major_text)).perform(typeText(""));
        onView(withId(R.id.year_text)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.continue_button)).perform(click());

        //wait for async transition of registering user email for 10 seconds
        try{
            Thread.sleep(5000);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        onView(withId(R.id.bio_text)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.continue_button)).perform(click());

        //wait for async transition of registering user email for 10 seconds
        try{
            Thread.sleep(5000);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        onView(withId(R.id.submit_button)).perform(click());

        //wait for async transition for 15 seconds since transition activity
        try{
            Thread.sleep(10000);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        onView(withId(R.id.invitations_text)).check(matches(isDisplayed()));
    }

    @Test
    public void updateProfile() {
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.username_text)).perform(typeText("bridget.test@gmail.com"));
        onView(withId(R.id.password_text)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.submit_button)).perform(click());

        //wait for async transition for 10 seconds
        try{
            Thread.sleep(10000);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        //go to update profile
        onView(withId(R.id.update_profile_button)).perform(scrollTo(), click());

        Random rand = new Random();
        Integer num = rand.nextInt(1000000000);
        String newUser = ("user").concat(num.toString());

        onView(withId(R.id.first)).perform(scrollTo(), clearText(), typeText(newUser), closeSoftKeyboard());
        onView(withId(R.id.last)).perform(scrollTo(), clearText(), typeText(("Last").concat(num.toString())), closeSoftKeyboard());
        onView(withId(R.id.phone)).perform(scrollTo(), clearText(), typeText("1234567890"), closeSoftKeyboard());
        onView(withId(R.id.major)).perform(scrollTo(), clearText(), typeText(("Major").concat(num.toString())), closeSoftKeyboard());
        onView(withId(R.id.year)).perform(scrollTo(), clearText(), typeText("2024"), closeSoftKeyboard());
        onView(withId(R.id.bio)).perform(scrollTo(), clearText(), typeText(("New bio ").concat(num.toString())), closeSoftKeyboard());

        onView(withId(R.id.submit_event)).perform(scrollTo(), click());

        //wait for async transition for 15 seconds since transition activity
        try{
            Thread.sleep(10000);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        //go to update profile again to check that the data was updated
        onView(withId(R.id.update_profile_button)).perform(scrollTo(), click());

        //checking all things match with update
        onView(withId(R.id.first)).check(matches(withText(newUser)));
        onView(withId(R.id.last)).check(matches(withText(("Last").concat(num.toString()))));
        onView(withId(R.id.phone)).check(matches(withText("1234567890")));
        onView(withId(R.id.major)).check(matches(withText(("Major").concat(num.toString()))));
        onView(withId(R.id.year)).check(matches(withText("2024")));
        onView(withId(R.id.bio)).check(matches(withText(("New bio ").concat(num.toString()))));
    }
}
