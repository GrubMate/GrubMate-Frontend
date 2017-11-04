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

import com.example.grubmate.grubmate.fragments.PostFragment;
import com.example.grubmate.grubmate.fragments.ProfileFragment;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by PETER on 10/28/17.
 */

public class EditDeletePostTest {
    UiDevice mDevice;
    @Rule
    public FragmentTestRule<PostFragment> mFragmentTestRule = new FragmentTestRule<>(PostFragment.class);

    @Test
    public void editPost() throws UiObjectNotFoundException {

        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);
        // Then use Espresso to test the Fragment
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject edit = mDevice.findObject(new UiSelector().text("EDIT"));
        edit.click();

        onView(withId(R.id.et_post_item_name)).perform(replaceText("pi pi name"));
        onView(withId(R.id.et_post_item_tags)).perform(typeText("pipitag"));
        onView(withId(R.id.cb_post_home)).check(matches(isChecked())).perform(scrollTo(), click());
//        onData(allOf(is(instanceOf(String.class)), is("Asian")))
//                .perform(click());
//        onView(withId(R.id.spinner_category))
//                .check(matches(withSpinnerText(containsString("Asian"))));
        onView(withId(R.id.et_post_item_allergy))
                .perform(scrollTo())
                .perform(click())
                .perform(typeText("pi pi allergy"));
        onView(withId(R.id.et_post_item_description)).perform(typeText("pi pi description"));
        onView(withId(R.id.et_post_item_quantity)).perform(replaceText("1"));
        onView(withId(R.id.spinner_category))
                .perform(scrollTo())
                .perform(click());

        onData(allOf(is(instanceOf(String.class)), is("Asian"))).perform(click());


        //onView(withId(R.id.spinner_time)) .perform(scrollTo())
         //       .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Lunch"))).perform(click());

        onView(withId(R.id.et_location)) .perform(scrollTo())
                .perform(click());

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        UiObject google = mDevice.findObject(new UiSelector().text("Search"));
        google.setText("USC");
        UiObject address = mDevice.findObject(new UiSelector().text("University of Southern California"));
        address.click();

//        onView(withId(R.id.b_post_action_photo))
//                .perform(click());

//        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
//        UiObject checkBox = mDevice.findObject(new UiSelector().checked(false));
//        checkBox.click();
//        UiObject apply = mDevice.findObject(new UiSelector().text("Apply"));
//        apply.click();
        onView(withId(R.id.fab_post)).perform(click());

    }

    @Test
    public void deletePost() throws UiObjectNotFoundException {

        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);

        // Then use Espresso to test the Fragment
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject delete = mDevice.findObject(new UiSelector().text("DELETE"));
        delete.click();


    }



}
