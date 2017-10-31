package com.example.grubmate.grubmate;

import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.view.View;

import com.example.grubmate.grubmate.fragments.FeedFragment;
import com.example.grubmate.grubmate.fragments.ProfileFragment;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by PETER on 10/28/17.
 */

public class PostAsRequesterTest {
    UiDevice mDevice;
    @Rule
    public FragmentTestRule<FeedFragment> mFragmentTestRule = new FragmentTestRule<>(FeedFragment.class);

    @Test
    public void basicInfo() {

        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.rv_feed)).check(matches(isDisplayed()));
        // Then use Espresso to test the Fragment


    }
    @Test
    public void requestButton() throws UiObjectNotFoundException {

        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);

        // Then use Espresso to test the Fragment
        onView(withId(R.id.rv_feed)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.b_feed_item_request)));
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject google = mDevice.findObject(new UiSelector().text("Search"));
        google.setText("USC");
        UiObject address = mDevice.findObject(new UiSelector().text("University of Southern California"));
        address.click();
        mDevice.pressHome();

    }

    @Test
    public void posterProfile() {

        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);

        // Then use Espresso to test the Fragment
        onView(withId(R.id.rv_feed)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.tv_feed_item_poster)));


    }

    public static class MyViewAction {

        public static ViewAction clickChildViewWithId(final int id) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return "Click on a child view with specified id.";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    View v = view.findViewById(id);
                    v.performClick();
                }
            };
        }

    }
}
