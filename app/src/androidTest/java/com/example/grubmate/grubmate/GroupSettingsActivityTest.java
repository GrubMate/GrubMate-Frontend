package com.example.grubmate.grubmate;

import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;

import com.example.grubmate.grubmate.dataClass.Group;

import static android.support.test.espresso.Espresso.onView;
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


import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jieji on 28/10/2017.
 */
public class GroupSettingsActivityTest {

    UiDevice mDevice;
    @Rule
    public ActivityTestRule<GroupSettingsActivity> mActivityRule =
            new ActivityTestRule(GroupSettingsActivity.class);

    @Test
    public void groupSetting() throws UiObjectNotFoundException
    {
        onView(withId(R.id.))
    }
}