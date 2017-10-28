package com.example.grubmate.grubmate;

import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiObjectNotFoundException;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by PETER on 10/28/17.
 */

public class SearchActivityTest {
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
    }

    @Test
    public void searchWithoutCategory() throws UiObjectNotFoundException {

        onView(withId(R.id.et_search_item_name)).perform(typeText("pi pi name"));


        onView(withId(R.id.search_spinner_time))
                .perform(click());

        onData(allOf(is(instanceOf(String.class)), is("Lunch"))).perform(click());

        onView(withId(R.id.b_search_button)).perform(click());

        closeSoftKeyboard();
    }

    @Test
    public void searchWithoutTime() throws UiObjectNotFoundException {

        onView(withId(R.id.et_search_item_name)).perform(typeText("pi pi name"));
        onView(withId(R.id.search_spinner_category))
                .perform(click());

        onData(allOf(is(instanceOf(String.class)), is("Asian"))).perform(click());
        

        onView(withId(R.id.b_search_button)).perform(click());

        closeSoftKeyboard();
    }
}
