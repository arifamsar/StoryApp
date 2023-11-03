package com.arfsar.storyapp.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.arfsar.storyapp.R
import com.arfsar.storyapp.helper.EspressoIdlingResource
import com.arfsar.storyapp.ui.addstory.AddStoryActivity
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        Intents.release()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun uploadStory() {
        onView(withId(R.id.fab_addStory)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_addStory)).perform(click())

        Intents.intended(IntentMatchers.hasComponent(AddStoryActivity::class.java.name))

        onView(withId(R.id.cameraButton)).check(matches(isDisplayed()))
        onView(withId(R.id.galleryButton)).check(matches(isDisplayed()))
        onView(withId(R.id.uploadButton)).check(matches(isDisplayed()))
        onView(withId(R.id.checkboxLocation)).check(matches(isDisplayed()))

        // camera must take action manually
        onView(withId(R.id.cameraButton)).perform(click())
        onView(withId(R.id.previewImageView)).check(matches(isDisplayed()))

        val descriptionText = "This is a description text testing"
        onView(withId(R.id.editTextDescription)).perform(typeText(descriptionText), closeSoftKeyboard())

        onView(withId(R.id.uploadButton)).perform(click())

        onView(withText("Success")).inRoot(isDialog()).check(matches(isDisplayed()))
        onView(withText("OK")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())

        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
        onView(withId(R.id.rv_listStory)).check(matches(isDisplayed()))
    }

}