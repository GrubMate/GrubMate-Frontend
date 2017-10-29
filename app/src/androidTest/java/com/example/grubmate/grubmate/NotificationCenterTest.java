package com.example.grubmate.grubmate;

import com.example.grubmate.grubmate.fragments.NotificationCenterFragment;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


/**
 * Created by tianhangliu on 10/28/17.
 */

public class NotificationCenterTest {
    @Rule
    public FragmentTestRule<NotificationCenterFragment> mFragmentTestRule = new FragmentTestRule<>(NotificationCenterFragment.class);

    @Test
    public void fragment_can_be_instantiated() {

        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);

        // Then use Espresso to test the Fragment
        onView(withId(R.id.tv_notification_title)).check(matches(isDisplayed()));
    }
}
