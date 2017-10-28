package com.example.grubmate.grubmate;

import android.content.ClipData;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import android.support.test.espresso.contrib.RecyclerViewActions;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by PETER on 10/28/17.
 */

public class SearchActionTest {

    UiDevice mDevice;
         @Rule
         public ActivityTestRule<SearchActivity> mActivityRule =
                 new ActivityTestRule(SearchActivity.class);


        @Test
        public void normalSearch() throws UiObjectNotFoundException {

            onView(withId(R.id.et_search_item_name)).perform(typeText("pi pi name"));
            onView(withId(R.id.search_spinner_category))
                    .perform(click());

            onData(allOf(is(instanceOf(String.class)), is("Asian"))).perform(click());

            onView(withId(R.id.search_spinner_time))
                    .perform(click());

            onData(allOf(is(instanceOf(String.class)), is("Lunch"))).perform(click());

            onView(withId(R.id.b_search_button)).perform(click());

            closeSoftKeyboard();

        //    SystemClock.sleep(2000);
            onView(withId(R.id.rv_feed)).perform(
                    RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id. b_feed_item_request)));

            mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

            UiObject google = mDevice.findObject(new UiSelector().text("Search"));
            google.setText("USC");
            UiObject address = mDevice.findObject(new UiSelector().text("University of Southern California"));
            address.click();
            mDevice.pressHome();
    }

    @Test
    public void searchWithoutName() throws UiObjectNotFoundException {

        onView(withId(R.id.search_spinner_category))
                .perform(click());

        onData(allOf(is(instanceOf(String.class)), is("Asian"))).perform(click());

        onView(withId(R.id.search_spinner_time))
                .perform(click());

        onData(allOf(is(instanceOf(String.class)), is("Lunch"))).perform(click());

        onView(withId(R.id.b_search_button)).perform(click());

        closeSoftKeyboard();

     //   SystemClock.sleep(2000);
        onView(withId(R.id.rv_feed)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id. b_feed_item_request)));

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        UiObject google = mDevice.findObject(new UiSelector().text("Search"));
        google.setText("USC");
        UiObject address = mDevice.findObject(new UiSelector().text("University of Southern California"));
        address.click();
        mDevice.pressHome();
    }

    @Test
    public void searchWithoutCategory() throws UiObjectNotFoundException {

        onView(withId(R.id.et_search_item_name)).perform(typeText("pi pi name"));


        onView(withId(R.id.search_spinner_time))
                .perform(click());

        onData(allOf(is(instanceOf(String.class)), is("Lunch"))).perform(click());

        onView(withId(R.id.b_search_button)).perform(click());

        closeSoftKeyboard();

     //   SystemClock.sleep(2000);
        onView(withId(R.id.rv_feed)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id. b_feed_item_request)));

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        UiObject google = mDevice.findObject(new UiSelector().text("Search"));
        google.setText("USC");
        UiObject address = mDevice.findObject(new UiSelector().text("University of Southern California"));
        address.click();
        mDevice.pressHome();
    }

    @Test
    public void searchWithoutTime() throws UiObjectNotFoundException {

        onView(withId(R.id.et_search_item_name)).perform(typeText("pi pi name"));
        onView(withId(R.id.search_spinner_category))
                .perform(click());

        onData(allOf(is(instanceOf(String.class)), is("Asian"))).perform(click());


        onView(withId(R.id.b_search_button)).perform(click());

        closeSoftKeyboard();

     //   SystemClock.sleep(2000);
        onView(withId(R.id.rv_feed)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id. b_feed_item_request)));

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        UiObject google = mDevice.findObject(new UiSelector().text("Search"));
        google.setText("USC");
        UiObject address = mDevice.findObject(new UiSelector().text("University of Southern California"));
        address.click();
        mDevice.pressHome();

    }

    @Test
    public void searchWithoutAnything() throws UiObjectNotFoundException {

        onView(withId(R.id.b_search_button)).perform(click());

        closeSoftKeyboard();

      //  onView(nthChildOf(withId(R.id.fragment_search), 0)).perform(clickChildViewWithId(R.id. bt_deliver));
     //   SystemClock.sleep(2000);
        onView(withId(R.id.rv_feed)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id. b_feed_item_request)));

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        UiObject google = mDevice.findObject(new UiSelector().text("Search"));
        google.setText("USC");
        UiObject address = mDevice.findObject(new UiSelector().text("University of Southern California"));
        address.click();
        mDevice.pressHome();
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
//    public static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
//        return new TypeSafeMatcher<View>() {
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("with "+childPosition+" child view of type parentMatcher");
//            }
//
//            @Override
//            public boolean matchesSafely(View view) {
//                if (!(view.getParent() instanceof ViewGroup)) {
//                    return parentMatcher.matches(view.getParent());
//                }
//
//                ViewGroup group = (ViewGroup) view.getParent();
//                return parentMatcher.matches(view.getParent()) && group.getChildAt(childPosition).equals(view);
//            }
//        };
//    }

}
