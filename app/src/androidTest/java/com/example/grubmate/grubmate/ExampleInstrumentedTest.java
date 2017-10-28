package com.example.grubmate.grubmate;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import com.example.grubmate.grubmate.activities.PostsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.Espresso.onView;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    UiDevice mDevice;
    @Rule
    public ActivityTestRule<PostActionActivity> mActivityRule =
            new ActivityTestRule(PostActionActivity.class);


    @Test
    public void postAction() throws UiObjectNotFoundException {

        onView(withId(R.id.et_post_item_name)).perform(typeText("pi pi zhu"));
        onView(withId(R.id.et_post_item_tags)).perform(typeText("pi pi tag"));
        onView(withId(R.id.cb_post_home)).check(matches(isNotChecked())).perform(scrollTo(), click());
//        onData(allOf(is(instanceOf(String.class)), is("Asian")))
//                .perform(click());
//        onView(withId(R.id.spinner_category))
//                .check(matches(withSpinnerText(containsString("Asian"))));
        onView(withId(R.id.et_post_item_allergy))
                .perform(scrollTo())
                .perform(click())
          .perform(typeText("pi pi allergy"));
        onView(withId(R.id.et_post_item_description)).perform(typeText("pi pi description"));
        onView(withId(R.id.et_post_item_quantity)).perform(typeText("1"));
        onView(withId(R.id.spinner_category))
                .perform(scrollTo())
                .perform(click());

        onData(allOf(is(instanceOf(String.class)), is("Asian"))).perform(click());


        onView(withId(R.id.spinner_time)) .perform(scrollTo())
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Lunch"))).perform(click());

        onView(withId(R.id.et_location)) .perform(scrollTo())
                .perform(click());

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        UiObject google = mDevice.findObject(new UiSelector().text("Search"));
        google.setText("USC");
        UiObject address = mDevice.findObject(new UiSelector().text("University of Southern California"));
        address.click();

        onView(withId(R.id.b_post_action_photo))
                .perform(click());

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject checkBox = mDevice.findObject(new UiSelector().index(0));
        checkBox.click();
        UiObject apply = mDevice.findObject(new UiSelector().text("Apply"));
        apply.click();
      // onView(withId(R.id.fab_post)).perform(click());
    }


}



