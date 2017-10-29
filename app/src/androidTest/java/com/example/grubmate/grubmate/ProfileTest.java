package com.example.grubmate.grubmate;

import android.net.Uri;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.view.View;

import com.example.grubmate.grubmate.dataClass.Subscription;
import com.example.grubmate.grubmate.fragments.FeedFragment;
import com.example.grubmate.grubmate.fragments.PostFragment;
import com.example.grubmate.grubmate.fragments.ProfileFragment;
import com.example.grubmate.grubmate.fragments.SubscriptionFragment;

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

public class ProfileTest{
    @Rule
    public FragmentTestRule<ProfileFragment> mFragmentTestRule = new FragmentTestRule<>(ProfileFragment.class);

    @Test
    public void basicInfo() {

        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);

        // Then use Espresso to test the Fragment
        onView(withId(R.id.tv_profile_name)).check(matches(withText("Jie Ji")));
        onView(withId(R.id.iv_profile_avatar)).check(matches(isDisplayed()));

    }
    @Test
    public void ratingDisplay() {

        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);

        onView(withId(R.id.rb_profile_rating)).check(matches(isDisplayed()));
    }

    @Test
    public void pastOrder() {

        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);

        // Then use Espresso to test the Fragment
        onView(withId(R.id.rv_feed)).check(matches(isDisplayed()));
    }


}
