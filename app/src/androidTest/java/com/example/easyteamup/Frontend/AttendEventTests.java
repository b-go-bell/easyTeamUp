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
import static org.junit.Assert.fail;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.easyteamup.Activities.UserHomeActivities.StartActivity;
import com.example.easyteamup.R;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
    public void searchPublicEvents() {
        onView(withId(R.id.public_events_button)).perform(scrollTo(), click());
        onView(withId(R.id.other_loc)).perform(click());
        onView(withId(R.id.pick_location)).perform(click());
        onView(withId(R.id.distance_input)).perform(typeText("30"), closeSoftKeyboard());
        onView(withId(R.id.submit)).perform(click());

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
    public void rsvpPublicEvent() {
        onView(withId(R.id.public_events_button)).perform(scrollTo(), click());
        try{
            Thread.sleep(4000);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        onView(withId(R.id.other_loc)).perform(click());
        try{
            Thread.sleep(10000);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        onView(withId(R.id.pick_location)).perform(click());
        onView(withId(R.id.distance_input)).perform(typeText("30"), closeSoftKeyboard());
        onView(withId(R.id.submit)).perform(click());

        try{
            Thread.sleep(8000);
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
            try {
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i)
                        .check(matches(hasDescendant(
                                allOf(withId(R.id.event_name), withText("Skating at Venice Beach")))));
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i).perform(scrollTo(), click());
                onView(withId(R.id.attend_public)).perform(click());
                onView(withId(R.id.times)).perform(click());
                onView(withId(R.id.date_picker)).perform(PickerActions.setDate(2022, 5, 15));
                onView(withId(R.id.start_time_picker)).perform(PickerActions.setTime(8, 00));
                onView(withId(R.id.end_time_picker)).perform(PickerActions.setTime(14, 30));
                onView(withId(R.id.submit_range)).perform(click());
                onView(withId(R.id.submit)).perform(click());
                break;
            }
            catch (AssertionError e){ }
        }

        try{
            Thread.sleep(5000);
        }
        catch(Exception e){}


        final int[] attend_event_amt = new int[1];
        onView(withId(R.id.events_list)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;
                attend_event_amt[0] = listView.getCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {
            }
        }));

        Log.d("NUMBER", String.valueOf(new_event_amt[0]));

        boolean f = true;
        for(int i = 0; i < attend_event_amt[0]; i++) {
            try {
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i)
                        .check(matches(hasDescendant(
                                allOf(withId(R.id.event_name), withText("Skating at Venice Beach")))));
                f = false;
                break;
            } catch (AssertionError e) {
                Log.d("DIFFERENT NAME", "name");
            }
        }
        if (f){
            fail("Event was not effectively RSVPd");
        }
    }

    @Test
    public void rsvpPublicEventCancel() {
        onView(withId(R.id.attending_button)).perform(scrollTo(), click());

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
            try {
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i)
                        .check(matches(hasDescendant(
                                allOf(withId(R.id.event_name), withText("Skating at Venice Beach")))));
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i).perform(scrollTo(), click());
                onView(withId(R.id.cancel)).perform(click());
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

        final int[] attend_event_amt = new int[1];
        onView(withId(R.id.events_list)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;
                attend_event_amt[0] = listView.getCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {
            }
        }));

        for(int i = 0; i < attend_event_amt[0]; i++) {
            onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i)
                    .check(matches(not(hasDescendant(
                            allOf(withId(R.id.event_name), withText("Skating at Venice Beach"))))));
        }
    }

    @Test
    public void rejectInvitedEvent() {
        onView(withId(R.id.invited_events_button)).perform(scrollTo(), click());
        try{
            Thread.sleep(8000);
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
            try {
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i)
                        .check(matches(hasDescendant(
                                allOf(withId(R.id.event_name), withText("Invited Event")))));
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i).perform(scrollTo(), click());
                try {
                    onView(withId(R.id.reject_invite)).perform(click());
                }
                catch(PerformException e){
                    onView(withId(R.id.cancel)).perform(click());
                }
                onView(withId(R.id.reject)).perform(click());
                break;
            }
            catch (AssertionError e){ }
        }

        try{
            Thread.sleep(5000);
        }
        catch(Exception e){}


        final int[] attend_event_amt = new int[1];
        onView(withId(R.id.events_list)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;
                attend_event_amt[0] = listView.getCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {
            }
        }));

        boolean f = true;
        for(int i = 0; i < attend_event_amt[0]; i++) {
            try {
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i)
                        .check(matches(hasDescendant(
                                allOf(withId(R.id.event_name), withText("Invited Event")))));
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i)
                        .check(matches(hasDescendant(
                                allOf(withId(R.id.event_invitation), withText("Invitation rejected")))));
                f = false;
                break;
            } catch (AssertionError e) {}
        }
        if(f){
            fail("User did not successfully reject event.");
        }
    }

    @Test
    public void rsvpInvitedEvent() {
        onView(withId(R.id.invited_events_button)).perform(scrollTo(), click());
        try{
            Thread.sleep(8000);
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
            try {
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i)
                        .check(matches(hasDescendant(
                                allOf(withId(R.id.event_name), withText("Invited Event")))));
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i).perform(scrollTo(), click());
                onView(withId(R.id.accept_invited_reject)).perform(click());
                onView(withId(R.id.times)).perform(click());
                onView(withId(R.id.date_picker)).perform(PickerActions.setDate(2022, 5, 18));
                onView(withId(R.id.start_time_picker)).perform(PickerActions.setTime(8, 00));
                onView(withId(R.id.end_time_picker)).perform(PickerActions.setTime(14, 30));
                onView(withId(R.id.submit_range)).perform(click());
                onView(withId(R.id.submit)).perform(click());
                break;
            }
            catch (AssertionError e){ }
        }

        try{
            Thread.sleep(5000);
        }
        catch(Exception e){}


        final int[] attend_event_amt = new int[1];
        onView(withId(R.id.events_list)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;
                attend_event_amt[0] = listView.getCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {
            }
        }));

        boolean f = true;
        for(int i = 0; i < attend_event_amt[0]; i++) {
            try {
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i)
                        .check(matches(hasDescendant(
                                allOf(withId(R.id.event_name), withText("Invited Event")))));
                f = false;
                break;
            } catch (AssertionError e) {
                Log.d("DIFFERENT NAME", "name");
            }
        }
        if (f){
            fail("Event was not effectively RSVPd");
        }
    }

    @Test
    public void rsvpInvitedEventCancel() {
        onView(withId(R.id.attending_button)).perform(scrollTo(), click());
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
            try {
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i)
                        .check(matches(hasDescendant(
                                allOf(withId(R.id.event_name), withText("Invited Event")))));
                onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i).perform(scrollTo(), click());
                onView(withId(R.id.cancel)).perform(click());
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

        final int[] attend_event_amt = new int[1];
        onView(withId(R.id.events_list)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;
                attend_event_amt[0] = listView.getCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {
            }
        }));

        for(int i = 0; i < attend_event_amt[0]; i++) {
            onData(anything()).inAdapterView(withId(R.id.events_list)).atPosition(i)
                    .check(matches(not(hasDescendant(
                            allOf(withId(R.id.event_name), withText("Skating at Venice Beach"))))));
        }

    }
}
