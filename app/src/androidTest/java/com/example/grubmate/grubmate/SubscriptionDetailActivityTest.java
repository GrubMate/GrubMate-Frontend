package com.example.grubmate.grubmate;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import com.example.grubmate.grubmate.fragments.SubscriptionFragment;

import org.junit.*;

import static org.junit.Assert.*;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.action.ViewActions.*;

import static org.hamcrest.Matchers.*;

/**
 * Created by jieji on 29/10/2017.
 */
public class SubscriptionDetailActivityTest {
    @Rule
    public FragmentTestRule<SubscriptionFragment> mFragmentTestRule = new FragmentTestRule<>(SubscriptionFragment.class);


    @Test
    public void unsubscribeNormally() throws UiObjectNotFoundException
    {
        mFragmentTestRule.launchActivity(null);
        UiDevice mDevice;
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        UiObject google = mDevice.findObject(new UiSelector().text("UNSUBSCRIBE"));
        google.click();
    }

}