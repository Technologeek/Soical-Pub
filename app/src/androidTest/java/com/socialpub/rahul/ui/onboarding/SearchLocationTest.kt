package com.socialpub.rahul.ui.onboarding


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.socialpub.rahul.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SearchLocationTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(OnboardingActivity::class.java)

    @Test
    fun searchLocationTest() {

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.action_search), withContentDescription("Search"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_nave_home),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val chip = onView(
            allOf(
                withId(R.id.chip_sort_location), withText("location"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.appcompat.widget.LinearLayoutCompat")),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        chip.perform(click())

        val textInputEditText = onView(
            allOf(
                withId(R.id.edit_search_user_name),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.til_error),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("del"), closeSoftKeyboard())

        Thread.sleep(7000)

        val textView = onView(
            allOf(
                withId(R.id.text_post_location),
                withText("11-A, Gandhi Park, Hauz Rani, Malviya Nagar, New Delhi, Delhi 110017, India"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.container_profile),
                        1
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("11-A, Gandhi Park, Hauz Rani, Malviya Nagar, New Delhi, Delhi 110017, India")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
