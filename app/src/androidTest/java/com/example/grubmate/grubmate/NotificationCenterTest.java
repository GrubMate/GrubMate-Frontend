package com.example.grubmate.grubmate;

import android.widget.RatingBar;

import com.example.grubmate.grubmate.dataClass.Notification;
import com.example.grubmate.grubmate.fragments.NotificationCenterFragment;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;

import org.hamcrest.core.AllOf;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.typeCompatibleWith;


/**
 * Created by tianhangliu on 10/28/17.
 */

public class NotificationCenterTest {
    @Rule
    public FragmentTestRule<NotificationCenterFragment> mFragmentTestRule = new FragmentTestRule<>(NotificationCenterFragment.class);

    @Before
    public void setUp() {
        mFragmentTestRule.launchActivity(null);
    }

    @Test
    public void fragment_can_be_instantiated() {

        // Launch the activity to make the fragment visible


        // Then use Espresso to test the Fragment
        onView(withId(R.id.tv_notification_title)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_notification_list)).check(matches(isDisplayed()));
    }


    @Test
    public void fragment_can_have_request() {
        onView(
                withId(R.id.rv_notification_list)
        ).check(matches(hasDescendant(withText("Request"))));
        onView(
                withId(R.id.rv_notification_list)
        ).check(matches(hasDescendant(withText("Accept"))));
    }

    @Test
    public void fragment_request_can_be_denied() {
        onView(
                withId(R.id.rv_notification_list)
        ).check(matches(hasDescendant(withText("Deny"))));
    }

    @Test
    public void fragment_can_have_confimation_update() {
        onView(
                withId(R.id.rv_notification_list)
        ).check(matches(hasDescendant(withText("Accepted"))));
    }

    @Test
    public void fragment_can_have_matching_post() {
        onView(
                withId(R.id.rv_notification_list)
        ).check(matches(hasDescendant(withText("Match"))));
        onView(
                withId(R.id.rv_notification_list)
        ).check(matches(hasDescendant(withText("Request"))));
    }

    @Test
    public void fragment_can_have_rating() {
        onView(
                withId(R.id.rv_notification_list)
        ).check(matches(hasDescendant(withText("Rating"))));
        onView(
                withId(R.id.rv_notification_list)
        ).check(matches(hasDescendant(withText("submit"))));

    }
}
