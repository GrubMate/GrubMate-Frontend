package com.example.grubmate.grubmate;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.core.StringContains.containsString;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.example.grubmate.grubmate.activities.ProfileActivity;
import com.example.grubmate.grubmate.fragments.ProfileFragment;
import com.example.grubmate.grubmate.utilities.PersistantDataManager;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by PETER on 10/28/17.
 */

public class ProfileTest {
    @Rule
    public FragmentTestRule<ProfileFragment> mFragmentRule =
            new FragmentTestRule(ProfileFragment.class);


    @Test
    public void profile() throws UiObjectNotFoundException {

//        Fragment destinationFragment =  ProfileFragment.newInstance(PersistantDataManager.getUserID(),null);
//        FragmentTransaction transaction = mActivityRule.getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fl_main_fragment_container, destinationFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();

//        onView(withId(R.id.rv_feed)).perform(
//                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id. b_feed_item_request)));
//
//        onView(withId(R.id.drawer_layout))
//                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
//                .perform(DrawerActions.open()); // Open Drawer
//
//        // Start the screen of your activity.
//        onView(withId(R.id.nav_view))
//                .perform(NavigationViewActions.navigateTo(R.id.nav_profile));

        onView(withId(R.id.profile_image)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_profile_name)).check(matches(withText(containsString("Jie Ji"))));

//        onView(withId(R.id.et_post_item_tags)).perform(typeText("pi pi tag"));
//        onView(withId(R.id.cb_post_home)).check(matches(isNotChecked())).perform(scrollTo(), click());

    }

    public static class MyViewAction {

        public static ViewAction clickChildViewWithId(final int id) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return "Click on a child view with specified id.";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    View v = view.findViewById(id);
                    v.performClick();
                }
            };
        }

    }
}