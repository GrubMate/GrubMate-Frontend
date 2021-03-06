package com.example.grubmate.grubmate;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Checks;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;

import com.example.grubmate.grubmate.adapters.NotificationAdapter;
import com.example.grubmate.grubmate.dataClass.Notification;
import com.example.grubmate.grubmate.fragments.NotificationCenterFragment;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToHolder;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasValue;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

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
        onView(ViewMatchers.withId(R.id.rv_notification_list))
                .perform(RecyclerViewActions.scrollToPosition(3));
        onView(ViewMatchers.withId(R.id.rv_notification_list)).check(matches(Utils.atPosition(3, hasDescendant(withId(R.id.rb_notification_rating)))));
        onView(ViewMatchers.withId(R.id.rv_notification_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, Utils.clickChildViewWithId(R.id.rb_notification_rating)));
    }

    @Test
    public void has_review_box() {
        onView(ViewMatchers.withId(R.id.rv_notification_list))
                .perform(RecyclerViewActions.scrollToPosition(3));
        onView(ViewMatchers.withId(R.id.rv_notification_list)).check(matches(Utils.atPosition(3, hasDescendant(withId(R.id.tv_notification_rating_review)))));
    }
    @Test
    public void has_submit_button() {
        onView(ViewMatchers.withId(R.id.rv_notification_list))
                .perform(RecyclerViewActions.scrollToPosition(3));
        onView(ViewMatchers.withId(R.id.rv_notification_list)).check(matches(Utils.atPosition(3, hasDescendant(withId(R.id.b_notification_submit)))));
        onView(ViewMatchers.withId(R.id.rv_notification_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, Utils.clickChildViewWithId(R.id.b_notification_submit)));
    }


    }

