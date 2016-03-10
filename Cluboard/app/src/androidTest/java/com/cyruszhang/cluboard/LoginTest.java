package com.cyruszhang.cluboard;

/**
 * Created by Yimeng on 3/9/2016.
 */
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import android.support.test.runner.AndroidJUnit4;

import com.cyruszhang.cluboard.activity.Login;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;


@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Rule
    public IntentsTestRule<Login> mActivityRule = new IntentsTestRule<>(Login.class);

    @Test
    public void logintest(){

        onView(withId(R.id.Users_password)).perform(typeText("123456"));
        onView(withId(R.id.Users_username)).perform(typeText("xinyuan"));
        closeSoftKeyboard();

        onView(withId(R.id.login_button)).perform(click());

        intended(toPackage("com.cyruszhang.cluboard.activity.Login"));


    }


}
