package com.example.easyteamup.Frontend;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;

import android.view.View;
import android.widget.ListView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.easyteamup.Activities.UserHomeActivities.StartActivity;
import com.example.easyteamup.R;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class AttendEventTests {

    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(StartActivity.class);
    @Rule
    public TestName testName = new TestName();

    @Before
    public void init() {
        ActivityScenario scenario = rule.getScenario();

        //give it 5 seconds to load
        try{
            Thread.sleep(5000);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        // if it is not displayed, that means we need to login
        try {
            onView(withId(R.id.invitations_text)).check(matches(isDisplayed()));
        }
        catch(NoMatchingViewException nmve){
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
        }
    }


    @Test
    public void viewInvitedEventsList() {
        onView(withId(R.id.invited_events_button)).perform(scrollTo(), click());
        //give time for async submission
        try{
            Thread.sleep(5000);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        final int[] new_event_amt = new int[1];
        onView(withId(R.id.events_list)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;
                new_event_amt[0] = listView.getCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {
            }
        }));

        for(int i = 0; i < new_event_amt[0]; i++){
            onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i)
                    .check(matches(hasDescendant(
                            allOf(withId(R.id.event_name), withText("Invited Event")))));
        }
    }

    @Test
    public void viewPastEventsList() {
        onView(withId(R.id.event_history_button)).perform(scrollTo(), click());
        //give time for async submission
        try{
            Thread.sleep(5000);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        final int[] new_event_amt = new int[1];
        onView(withId(R.id.events_list)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;
                new_event_amt[0] = listView.getCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {
            }
        }));
    }

    @Test
    public void viewEventsMap() {}

    @Test
    public void searchPublicEvents() {}

    @Test
    public void rsvpCancelPublicEvent() {}

    @Test
    public void rsvpCancelInvitedEvent() {}

    @Test
    public void addAvailableTimesTest() {}

}
