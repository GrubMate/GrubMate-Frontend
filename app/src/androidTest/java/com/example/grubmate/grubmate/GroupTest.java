package com.example.grubmate.grubmate;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import com.example.grubmate.grubmate.fragments.GroupFragment;
import com.example.grubmate.grubmate.fragments.ProfileFragment;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by PETER on 10/29/17.
 */

public class GroupTest {

    UiDevice mDevice;
    @Rule
    public FragmentTestRule<GroupFragment> mFragmentTestRule = new FragmentTestRule<>(GroupFragment.class);

    @Test
    public void basicInfo() {

        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);

        // Then use Espresso to test the Fragment
        onView(withId(R.id.rv_Group_list)).check(matches(isDisplayed()));

        onView(withId(R.id.fab_group_add)).check(matches(isDisplayed()));

    }

    @Test
    public void addButtonTest() {

        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);

        // Then use Espresso to test the Fragment
       onView(withId(R.id.fab_group_add)).perform(click());

    }

    @Test
    public void editButtonTest() throws UiObjectNotFoundException {

        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);

        // Then use Espresso to test the Fragment
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject edit = mDevice.findObject(new UiSelector().text("EDIT"));
        edit.click();

    }
}
