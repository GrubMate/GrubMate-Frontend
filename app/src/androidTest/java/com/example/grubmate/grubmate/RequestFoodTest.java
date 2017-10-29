package com.example.grubmate.grubmate;


import android.support.test.uiautomator.UiDevice;

import com.example.grubmate.grubmate.fragments.FeedFragment;
import com.example.grubmate.grubmate.fragments.NotificationCenterFragment;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;


/**
 * Created by PETER on 10/28/17.
 */

public class RequestFoodTest {
    UiDevice mDevice;
    @Rule
    public FragmentTestRule<NotificationCenterFragment> mFragmentTestRule = new FragmentTestRule<>(NotificationCenterFragment.class);

    @Test
    public void basicInfo() {

        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);

        // Then use Espresso to test the Fragment


    }


}