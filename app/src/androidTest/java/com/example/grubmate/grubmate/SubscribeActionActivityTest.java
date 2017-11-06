package com.example.grubmate.grubmate;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiObjectNotFoundException;

import org.junit.*;

import static org.junit.Assert.*;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.action.ViewActions.*;

import static org.hamcrest.Matchers.*;

/**
 * Created by jieji on 28/10/2017.
 */
public class SubscribeActionActivityTest {
    @Rule
    public ActivityTestRule<SubscribeActionActivity> mActivityRule =
            new ActivityTestRule(SubscribeActionActivity.class);

    @Test
    public void subscribeNormally() throws UiObjectNotFoundException
    {


        onView(withId(R.id.et_subscribe_item_name)).perform(typeText("Test Name"));
        onView(withId(R.id.spinner_category))
                .perform(scrollTo())
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Asian"))).perform(click());

        onView(withId(R.id.et_subscribe_item_tags)).perform(typeText("pipitag"));

        //onView(withId(R.id.spinner_time))
        //        .perform(scrollTo())
        //        .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Lunch"))).perform(click());

        ViewActions.closeSoftKeyboard();

        onView(withId(R.id.fab_subscribe)).perform(click());
    }

    @Test
    public void subscribeWithoutName() throws UiObjectNotFoundException
    {


        //onView(withId(R.id.et_subscribe_item_name)).perform(typeText("Test Name"));
        onView(withId(R.id.spinner_category))
                .perform(scrollTo())
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Asian"))).perform(click());

        onView(withId(R.id.et_subscribe_item_tags)).perform(typeText("pipitag"));

        //onView(withId(R.id.spinner_time))
        //        .perform(scrollTo())
        //        .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Lunch"))).perform(click());

        ViewActions.closeSoftKeyboard();

        onView(withId(R.id.fab_subscribe)).perform(click());
    }
    @Test
    public void subscribeWithoutCategory() throws UiObjectNotFoundException
    {


        onView(withId(R.id.et_subscribe_item_name)).perform(typeText("Test Name"));
//        onView(withId(R.id.spinner_category))
//                .perform(scrollTo())
//                .perform(click());
//        onData(allOf(is(instanceOf(String.class)), is("Asian"))).perform(click());

        onView(withId(R.id.et_subscribe_item_tags)).perform(typeText("pipitag"));

        //onView(withId(R.id.spinner_time))
        //        .perform(scrollTo())
        //        .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Lunch"))).perform(click());

        ViewActions.closeSoftKeyboard();

        onView(withId(R.id.fab_subscribe)).perform(click());
    }
    @Test
    public void subscribeWithoutTags() throws UiObjectNotFoundException
    {
        onView(withId(R.id.et_subscribe_item_name)).perform(typeText("Test Name"));
        onView(withId(R.id.spinner_category))
                .perform(scrollTo())
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Asian"))).perform(click());

        //onView(withId(R.id.et_subscribe_item_tags)).perform(typeText("poison, poop"));

        //onView(withId(R.id.spinner_time))
        //        .perform(scrollTo())
        //        .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Lunch"))).perform(click());

        ViewActions.closeSoftKeyboard();

        onView(withId(R.id.fab_subscribe)).perform(click());
    }

    @Test
    public void subscribeWithoutTimeperiod() throws UiObjectNotFoundException
    {


        onView(withId(R.id.et_subscribe_item_name)).perform(typeText("Test Name"));
        onView(withId(R.id.spinner_category))
                .perform(scrollTo())
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Asian"))).perform(click());

        onView(withId(R.id.et_subscribe_item_tags)).perform(typeText("pipitag"));

//        onView(withId(R.id.spinner_time))
//                .perform(scrollTo())
//                .perform(click());
//        onData(allOf(is(instanceOf(String.class)), is("Lunch"))).perform(click());

        ViewActions.closeSoftKeyboard();

        onView(withId(R.id.fab_subscribe)).perform(click());
    }

}