package com.socialpub.rahul.ui.onboarding


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
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
class HomePostLongPressOptionsTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(OnboardingActivity::class.java)

    @Test
    fun homePostLongPressOptionsTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatImageView = onView(
            allOf(
                withId(R.id.image_post_preview),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.FrameLayout")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(longClick())

        val textView = onView(
            allOf(
                withId(android.R.id.text1), withText("Add to favourite"),
                childAtPosition(
                    allOf(
                        withId(R.id.select_dialog_listview),
                        childAtPosition(
                            withId(R.id.contentPanel),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Add to favourite")))

        val textView2 = onView(
            allOf(
                withId(android.R.id.text1), withText("View location"),
                childAtPosition(
                    allOf(
                        withId(R.id.select_dialog_listview),
                        childAtPosition(
                            withId(R.id.contentPanel),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("View location")))

        val textView3 = onView(
            allOf(
                withId(android.R.id.text1), withText("Report"),
                childAtPosition(
                    allOf(
                        withId(R.id.select_dialog_listview),
                        childAtPosition(
                            withId(R.id.contentPanel),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("Report")))
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
