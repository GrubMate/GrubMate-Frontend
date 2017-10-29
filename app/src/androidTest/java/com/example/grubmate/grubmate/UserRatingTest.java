package com.example.grubmate.grubmate;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.grubmate.grubmate.fragments.NotificationCenterFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToHolder;

/**
 * Created by tianhangliu on 10/28/17.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserRatingTest {
    @Rule
    public FragmentTestRule<NotificationCenterFragment> mFragmentTestRule = new FragmentTestRule<>(NotificationCenterFragment.class);

    @Before
    public void setUp() {
        mFragmentTestRule.launchActivity(null);
    }

    @Test
    public void has_rating_box() {
        onView(ViewMatchers.withId(R.id.rv_notification_list));
    }

}
