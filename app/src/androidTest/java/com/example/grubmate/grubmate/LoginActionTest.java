package com.example.grubmate.grubmate;

import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.widget.EditText;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by PETER on 10/27/17.
 */

public class LoginActionTest {


    UiDevice mDevice;
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule(LoginActivity.class);


    @Test
    public void LoginAction() throws UiObjectNotFoundException {

       onView(withId(R.id.login_button)).perform(click());
//        onView(withId(R.id.et_post_item_tags)).perform(typeText("pi pi tag"));
//        onView(withId(R.id.cb_post_home)).check(matches(isNotChecked())).perform(scrollTo(), click());


        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
//
        UiObject input = mDevice.findObject(new UiSelector()
                .className(EditText.class));
        input.setText("text");
//        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
//        device.findObject(new UiSelector().resourceId("com.facebook.katana:id/username")).setText("x@y.z");
//        device.findObject(new UiSelector().resourceId("com.facebook.katana:id/login")).clickAndWaitForNewWindow();

//        onView(withId(R.id.fab_post)).perform(click());
    }
}
