package com.example.grubmate.grubmate;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.view.View;
import android.widget.EditText;

import com.example.grubmate.grubmate.fragments.GroupFragment;
import com.example.grubmate.grubmate.fragments.ProfileFragment;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by PETER on 10/29/17.
 */

public class GroupManageTest {

    UiDevice mDevice;
    @Rule
    public FragmentTestRule<GroupFragment> mFragmentTestRule = new FragmentTestRule<>(GroupFragment.class);

    @Test
    public void editGroup() throws UiObjectNotFoundException {

        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);

        // Then use Espresso to test the Fragment
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject edit = mDevice.findObject(new UiSelector().text("EDIT"));
        edit.click();
        onView(withId(R.id.group_name)).perform(replaceText("pi pi groupName"));
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        for(int i=0;i<5;i++) {
            UiObject friends = mDevice.findObject(new UiSelector().text("friend number "+i));
            friends.click();
        }
        closeSoftKeyboard();

        onView(withId(R.id.groupSettingButton)).perform(click());
    }

    @Test
    public void addGroup() throws UiObjectNotFoundException {

        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);

        // Then use Espresso to test the Fragment

        onView(withId(R.id.fab_group_add)).perform(click());
        onView(withId(R.id.group_name)).perform(typeText("pi pi groupName"));
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        for(int i=0;i<3;i++) {
            UiObject friends = mDevice.findObject(new UiSelector().text("friend number "+i));
            friends.click();
        }
        closeSoftKeyboard();

        onView(withId(R.id.groupSettingButton)).perform(click());
    }
    @Test
    public void addGroupWithoutName() throws UiObjectNotFoundException {

        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);

        // Then use Espresso to test the Fragment

        onView(withId(R.id.fab_group_add)).perform(click());

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        for(int i=0;i<3;i++) {
            UiObject friends = mDevice.findObject(new UiSelector().text("friend number "+i));
            friends.click();
        }
        closeSoftKeyboard();

        onView(withId(R.id.groupSettingButton)).perform(click());
    }
    @Test
    public void addGroupWithoutFriend() throws UiObjectNotFoundException {

        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);

        // Then use Espresso to test the Fragment

        onView(withId(R.id.fab_group_add)).perform(click());
        onView(withId(R.id.group_name)).perform(typeText("pi pi groupName"));

        closeSoftKeyboard();

        onView(withId(R.id.groupSettingButton)).perform(click());
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
