package com.example.easyteamup.Frontend;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.fail;

import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.ScrollToAction;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.easyteamup.Activities.EventActivities.ViewEventActivities.DisplayEventHelpers.EventAdapter;
import com.example.easyteamup.Activities.UserHomeActivities.StartActivity;
import com.example.easyteamup.Backend.Event;
import com.example.easyteamup.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HostEventTests {

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
    public void createEvent() {
        onView(withId(R.id.create_event_button)).perform(scrollTo(), click());

        //trial and error finally adding all relevant details
        onView(withId(R.id.submit_event)).perform(scrollTo(), click());


        //check that error message is displayed,
        // and add due time according to error
        onView(withId(R.id.error)).perform(scrollTo());
        onView(withId(R.id.error)).check(matches(isDisplayed()));
        onView(withId(R.id.due_time)).perform(scrollTo(), click());
        onView(withId(R.id.date_picker)).perform(PickerActions.setDate(2022, 6, 02));
        onView(withId(R.id.start_time_picker)).perform(PickerActions.setTime(8, 00));
        onView(withId(R.id.submit_range)).perform(click());
        onView(withId(R.id.submit_event)).perform(scrollTo(), click());
        onView(withId(R.id.error)).perform(scrollTo());
        onView(withId(R.id.error)).check(matches(isDisplayed()));


        //add times according to error
        onView(withId(R.id.times)).perform(scrollTo(), click());
        onView(withId(R.id.date_picker)).perform(PickerActions.setDate(2022, 6, 03));
        onView(withId(R.id.start_time_picker)).perform(PickerActions.setTime(8, 00));
        onView(withId(R.id.end_time_picker)).perform(PickerActions.setTime(11, 30));
        onView(withId(R.id.submit_range)).perform(click());
        onView(withId(R.id.submit_event)).perform(scrollTo(), click());
        onView(withId(R.id.error)).perform(scrollTo());
        onView(withId(R.id.error)).check(matches(isDisplayed()));

        //add address according to error
        onView(withId(R.id.event_address)).perform(scrollTo(), typeText("University of Southern California"), closeSoftKeyboard());
        onView(withId(R.id.submit_event)).perform(scrollTo(), click());
        onView(withId(R.id.error)).perform(scrollTo());
        onView(withId(R.id.error)).check(matches(isDisplayed()));

        //add event name according to error
        onView(withId(R.id.event_title)).perform(scrollTo(), typeText("Espresso Testing Event"), closeSoftKeyboard());
        onView(withId(R.id.submit_event)).perform(scrollTo(), click());
        onView(withId(R.id.error)).perform(scrollTo());
        onView(withId(R.id.error)).check(matches(isDisplayed()));

        //add event length according to error
        onView(withId(R.id.event_length)).perform(scrollTo(), typeText("60"), closeSoftKeyboard());
        onView(withId(R.id.submit_event)).perform(scrollTo(), click());
        onView(withId(R.id.error)).perform(scrollTo());
        onView(withId(R.id.error)).check(matches(isDisplayed()));

        //add event type according to error
        onView(withId(R.id.event_type)).perform(scrollTo(), typeText("Testing"), closeSoftKeyboard());
        onView(withId(R.id.submit_event)).perform(scrollTo(), click());

        //give time for async submission and confirm goes to host event list
        try{
            Thread.sleep(10000);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        onView(withId(R.id.events_list)).check(matches(isDisplayed()));
    }


    @Test
    public void createEventBadDueTime() {
        onView(withId(R.id.create_event_button)).perform(scrollTo(), click());

        // create event that has due time that has already passed
        onView(withId(R.id.due_time)).perform(scrollTo(), click());
        onView(withId(R.id.date_picker)).perform(PickerActions.setDate(2020, 6, 02));
        onView(withId(R.id.start_time_picker)).perform(PickerActions.setTime(8, 00));
        onView(withId(R.id.submit_range)).perform(click());

        //add times
        onView(withId(R.id.times)).perform(scrollTo(), click());
        onView(withId(R.id.date_picker)).perform(PickerActions.setDate(2022, 6, 03));
        onView(withId(R.id.start_time_picker)).perform(PickerActions.setTime(8, 00));
        onView(withId(R.id.end_time_picker)).perform(PickerActions.setTime(11, 30));
        onView(withId(R.id.submit_range)).perform(click());

        onView(withId(R.id.event_address)).perform(scrollTo(), typeText("University of Southern California"), closeSoftKeyboard());
        onView(withId(R.id.event_title)).perform(scrollTo(), typeText("Bad Due Time Testing Event"), closeSoftKeyboard());
        onView(withId(R.id.event_length)).perform(scrollTo(), typeText("60"), closeSoftKeyboard());
        onView(withId(R.id.event_type)).perform(scrollTo(), typeText("Testing"), closeSoftKeyboard());
        onView(withId(R.id.submit_event)).perform(scrollTo(), click());
        //give time for async submission
        try{
            Thread.sleep(10000);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        //checking if the event appears in hosted events list; if it does, delete it and fail the test
        final int[] events = new int[1];
        onView(withId(R.id.events_list)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;
                events[0] = listView.getCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {}
        }));

        boolean f = false;
        for(int i = 0; i < events[0]; i++){
            try{
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i)
                        .check(matches(hasDescendant(
                                allOf(withId(R.id.event_name), withText("Bad Due Time Testing Event")))));
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i).perform(scrollTo(), click());
                onView(withId(R.id.delete_event)).perform(click());
                onView(withId(R.id.reject)).perform(click());
                f = true;
                break;
            }
            catch (AssertionError e){ }
        }
        if(f){
            fail("Event was created when it should not have been.");
        }
    }

    @Test
    public void createEventBadAvailableTimes() {
        onView(withId(R.id.create_event_button)).perform(scrollTo(), click());

        // create event that has due time that has already passed
        onView(withId(R.id.due_time)).perform(scrollTo(), click());
        onView(withId(R.id.date_picker)).perform(PickerActions.setDate(2022, 6, 02));
        onView(withId(R.id.start_time_picker)).perform(PickerActions.setTime(8, 00));
        onView(withId(R.id.submit_range)).perform(click());

        //add times
        onView(withId(R.id.times)).perform(scrollTo(), click());
        onView(withId(R.id.date_picker)).perform(PickerActions.setDate(2022, 5, 02));
        onView(withId(R.id.start_time_picker)).perform(PickerActions.setTime(8, 00));
        onView(withId(R.id.end_time_picker)).perform(PickerActions.setTime(11, 30));
        onView(withId(R.id.submit_range)).perform(click());

        onView(withId(R.id.event_address)).perform(scrollTo(), typeText("University of Southern California"), closeSoftKeyboard());
        onView(withId(R.id.event_title)).perform(scrollTo(), typeText("Bad Available Time Testing Event"), closeSoftKeyboard());
        onView(withId(R.id.event_length)).perform(scrollTo(), typeText("60"), closeSoftKeyboard());
        onView(withId(R.id.event_type)).perform(scrollTo(), typeText("Testing"), closeSoftKeyboard());
        onView(withId(R.id.submit_event)).perform(scrollTo(), click());
        //give time for async submission
        try{
            Thread.sleep(10000);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        //checking if the event appears in hosted events list; if it does, delete it and fail the test
        final int[] events = new int[1];
        onView(withId(R.id.events_list)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;
                events[0] = listView.getCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {}
        }));

        boolean f = false;
        for(int i = 0; i < events[0]; i++){
            try{
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i)
                        .check(matches(hasDescendant(
                                allOf(withId(R.id.event_name), withText("Bad Available Time Testing Event")))));
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i).perform(scrollTo(), click());
                onView(withId(R.id.delete_event)).perform(click());
                onView(withId(R.id.reject)).perform(click());
                f = true;
                break;
            }
            catch (AssertionError e){ }
        }
        if(f){
            fail("Event was created when it should not have been.");
        }
    }

    @Test
    public void beforeDeleteUpdateEventPublicity() {
        onView(withId(R.id.create_event_button)).perform(scrollTo(), click());

        onView(withId(R.id.event_title)).perform(scrollTo(), typeText("Update Event"), closeSoftKeyboard());
        onView(withId(R.id.event_address)).perform(scrollTo(), typeText("University of Southern California"), closeSoftKeyboard());
        onView(withId(R.id.event_type)).perform(scrollTo(), typeText("Testing"), closeSoftKeyboard());

        // create event that has due time that has already passed
        onView(withId(R.id.due_time)).perform(scrollTo(), click());
        onView(withId(R.id.date_picker)).perform(PickerActions.setDate(2022, 6, 01));
        onView(withId(R.id.start_time_picker)).perform(PickerActions.setTime(8, 00));
        onView(withId(R.id.submit_range)).perform(click());

        //add times
        onView(withId(R.id.times)).perform(scrollTo(), click());
        onView(withId(R.id.date_picker)).perform(PickerActions.setDate(2022, 6, 04));
        onView(withId(R.id.start_time_picker)).perform(PickerActions.setTime(8, 00));
        onView(withId(R.id.end_time_picker)).perform(PickerActions.setTime(11, 30));
        onView(withId(R.id.submit_range)).perform(click());


        onView(withId(R.id.event_length)).perform(scrollTo(), typeText("60"), closeSoftKeyboard());
        onView(withId(R.id.submit_event)).perform(scrollTo(), click());
        //give time for async submission
        try{
            Thread.sleep(8000);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        //checking if the event appears in hosted events list
        final int[] events = new int[1];
        onView(withId(R.id.events_list)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;
                events[0] = listView.getCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {}
        }));

        for(int i = 0; i < events[0]; i++){
            try{
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i)
                        .check(matches(hasDescendant(
                                allOf(withId(R.id.event_name), withText("Update Event")))));
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i).perform(scrollTo(), click());
                onView(withId(R.id.update_event)).perform(click());

                try{
                    Thread.sleep(2000);
                }
                catch(Exception e){
                    e.printStackTrace();
                }

                //update publicity
                onView(withId(R.id.privacy_toggle)).perform(scrollTo(), click());
                try{
                    Thread.sleep(2000);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                onView(withId(R.id.submit_event)).perform(scrollTo(), click());
                break;
            }
            catch (AssertionError e){ }
        }

        try{
            Thread.sleep(4000);
        }
        catch(Exception e){}

        for(int i = 0; i < events[0]; i++){
            try{
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i)
                        .check(matches(hasDescendant(
                                allOf(withId(R.id.event_invitation), withText("Hosting Public Event")))));
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i).perform(scrollTo(), click());
                break;
            }
            catch (AssertionError e){ }
        }
    }

    @Test
    public void beforeDeleteUpdateEventTimes() {
        onView(withId(R.id.hosting_button)).perform(scrollTo(), click());
        //give time for async submission
        try{
            Thread.sleep(5000);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        //checking if the event appears in hosted events list
        final int[] events = new int[1];
        onView(withId(R.id.events_list)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;
                events[0] = listView.getCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {}
        }));

        for(int i = 0; i < events[0]; i++){
            try{
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i)
                        .check(matches(hasDescendant(
                                allOf(withId(R.id.event_name), withText("Update Event")))));
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i).perform(scrollTo(), click());
                onView(withId(R.id.update_event)).perform(click());

                try{
                    Thread.sleep(2000);
                }
                catch(Exception e){
                    e.printStackTrace();
                }

                //update due time
                onView(withId(R.id.submit_event)).perform(scrollTo());
                onView(withId(R.id.due_time)).perform(click());

                //ERROR -> due time button does not function as intended
                try{
                    Thread.sleep(2000);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                onView(withId(R.id.date_picker)).perform(PickerActions.setDate(2022, 5, 25));
                onView(withId(R.id.start_time_picker)).perform(PickerActions.setTime(8, 00));
                onView(withId(R.id.submit_range)).perform(click());
                onView(withId(R.id.submit_event)).perform(scrollTo(), click());
                break;
            }
            catch (AssertionError e){ }
        }

        try{
            Thread.sleep(4000);
        }
        catch(Exception e){}

        for(int i = 0; i < events[0]; i++){
            try{
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i)
                        .check(matches(hasDescendant(
                                allOf(withId(R.id.event_invitation), withText(containsString("May 25"))))));
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i).perform(scrollTo(), click());
                break;
            }
            catch (AssertionError e){ }
        }

    }

    @Test
    public void beforeDeleteUsersInvite() {
        onView(withId(R.id.hosting_button)).perform(scrollTo(), click());
        //give time for async submission
        try{
            Thread.sleep(5000);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        //checking if the event appears in hosted events list
        final int[] events = new int[1];
        onView(withId(R.id.events_list)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;
                events[0] = listView.getCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {}
        }));

        for(int i = 0; i < events[0]; i++){
            try{
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i)
                        .check(matches(hasDescendant(
                                allOf(withId(R.id.event_name), withText("Update Event")))));
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i).perform(scrollTo(), click());
                onView(withId(R.id.update_event)).perform(click());

                try{
                    Thread.sleep(2000);
                }
                catch(Exception e){
                    e.printStackTrace();
                }

                //add invalid user
                onView(withId(R.id.submit_event)).perform(scrollTo());
                onView(withId(R.id.user_email_address)).perform(typeText("baduser"), closeSoftKeyboard());
                onView(withId(R.id.invite_user)).perform(click());
                onView(withId(R.id.invite_error)).perform(scrollTo());
                onView(withId(R.id.invite_error)).check(matches(isDisplayed()));

                //add user
                onView(withId(R.id.user_email_address)).perform(scrollTo(), clearText(), typeText("bridget.test@gmail.com"), closeSoftKeyboard());
                onView(withId(R.id.invite_user)).perform(click());
                onView(withId(R.id.event_invitees)).check(matches(withText(containsString("bridget.test@gmail.com"))));

                return;
            }
            catch (AssertionError e){ }
        }
    }

    @Test
    public void deleteEvent() {
        onView(withId(R.id.hosting_button)).perform(scrollTo(), click());
        //give time for async submission
        try{
            Thread.sleep(5000);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        //checking if the event appears in hosted events list
        final int[] events = new int[1];
        onView(withId(R.id.events_list)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;
                events[0] = listView.getCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {
            }
        }));

        for (int i = 0; i < events[0]; i++) {
            try {
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i)
                        .check(matches(hasDescendant(
                                allOf(withId(R.id.event_name), withText("Update Event")))));
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i).perform(scrollTo(), click());

                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                onView(withId(R.id.delete_event)).perform(click());
                onView(withId(R.id.reject)).perform(click());
                break;
            }
            catch (AssertionError e){ }
        }

        try{
            Thread.sleep(5000);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        //checking if the event appears in hosted events list
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
                .check(matches(not(hasDescendant(
                    allOf(withId(R.id.event_invitation), withText("Update Event"))))));
        }
    }
}
