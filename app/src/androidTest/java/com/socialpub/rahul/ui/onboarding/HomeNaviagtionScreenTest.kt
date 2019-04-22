package com.socialpub.rahul.ui.onboarding


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.socialpub.rahul.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class HomeNaviagtionScreenTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(OnboardingActivity::class.java)

    @Test
    fun appScreenTest() {
        val appCompatButton = onView(
            allOf(
                withId(R.id.btn_google_signin), withText("SignIn with google"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.screen_onboarding),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.action_home), withContentDescription("Home"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_nave_home),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val button = onView(
            allOf(
                withId(R.id.fab_upload),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.toolbar_home),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val bottomNavigationItemView2 = onView(
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
        bottomNavigationItemView2.perform(click())

        val chip = onView(
            allOf(
                withId(R.id.chip_sort_name),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(androidx.appcompat.widget.LinearLayoutCompat::class.java),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        chip.check(matches(isDisplayed()))

        val chip2 = onView(
            allOf(
                withId(R.id.chip_sort_name),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(androidx.appcompat.widget.LinearLayoutCompat::class.java),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        chip2.check(matches(isDisplayed()))

        val bottomNavigationItemView3 = onView(
            allOf(
                withId(R.id.action_profile), withContentDescription("Profile"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_nave_home),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView3.perform(click())

        val imageView = onView(
            allOf(
                withId(R.id.image_profile_avatar),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(androidx.appcompat.widget.LinearLayoutCompat::class.java),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))
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
