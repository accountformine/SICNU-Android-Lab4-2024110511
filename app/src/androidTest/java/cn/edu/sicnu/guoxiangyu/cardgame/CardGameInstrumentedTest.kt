package cn.edu.sicnu.guoxiangyu.cardgame

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CardGameInstrumentedTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun navigateToCardGameAndReset() {
        onView(withId(R.id.btnPlayGame)).check(matches(isDisplayed())).perform(click())
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.reset)).check(matches(isDisplayed())).perform(click())
        onView(withId(R.id.btnExit)).check(matches(isDisplayed())).perform(click())
        onView(withId(R.id.tvHighScore)).check(matches(isDisplayed()))
    }
}
