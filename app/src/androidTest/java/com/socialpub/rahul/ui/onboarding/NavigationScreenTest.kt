package com.socialpub.rahul.ui.onboarding


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
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
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class NavigationScreenTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(OnboardingActivity::class.java)

    @Test
    fun navigationScreenTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        onView(withId(R.id.drawer_container_home))
            .perform(DrawerActions.open());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(1500)

        val imageView = onView(
            allOf(
                withId(R.id.image_side_nav_home_useravatar),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navigation_header_container),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        onView(withId(R.id.drawer_container_home))
            .perform(DrawerActions.open());
        Thread.sleep(1500)

        val navigationMenuItemView = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.side_drawer_home),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(1000)

        val textView = onView(
            allOf(
                withText("Notifications"),
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
        textView.check(matches(withText("Notifications")))

        val materialButton = onView(
            allOf(
                withId(R.id.btn_close),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.appcompat.widget.LinearLayoutCompat")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        onView(withId(R.id.drawer_container_home))
            .perform(DrawerActions.open());
        Thread.sleep(1500)

        val navigationMenuItemView2 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.side_drawer_home),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )

        navigationMenuItemView2.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(1000)

        val button = onView(
            allOf(
                withId(R.id.btn_multi_delete),
                childAtPosition(
                    allOf(
                        withId(R.id.container_multiselect),
                        childAtPosition(
                            IsInstanceOf.instanceOf(androidx.appcompat.widget.LinearLayoutCompat::class.java),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val appCompatImageButton = onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar_fav),
                        childAtPosition(
                            withClassName(`is`("androidx.appcompat.widget.LinearLayoutCompat")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        onView(withId(R.id.drawer_container_home))
            .perform(DrawerActions.open());
        Thread.sleep(1500)

        val navigationMenuItemView3 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.side_drawer_home),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView3.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(1000)

        val button2 = onView(
            allOf(
                withId(R.id.btn_following),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(androidx.appcompat.widget.LinearLayoutCompat::class.java),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )

        Thread.sleep(1000)

        button2.check(matches(isDisplayed()))

        val button3 = onView(
            allOf(
                withId(R.id.btn_followers),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(androidx.appcompat.widget.LinearLayoutCompat::class.java),
                        1
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        button3.check(matches(isDisplayed()))

        val appCompatImageButton2 = onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar_followers),
                        childAtPosition(
                            withClassName(`is`("androidx.appcompat.widget.LinearLayoutCompat")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        onView(withId(R.id.drawer_container_home))
            .perform(DrawerActions.open());
        Thread.sleep(1500)

        val navigationMenuItemView4 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.side_drawer_home),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView4.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(1000)

        val switch_ = onView(
            allOf(
                withId(R.id.switch_location_search),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(androidx.appcompat.widget.LinearLayoutCompat::class.java),
                        1
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        switch_.check(matches(isDisplayed()))

        val switch_2 = onView(
            allOf(
                withId(R.id.switch_email_search),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(androidx.appcompat.widget.LinearLayoutCompat::class.java),
                        2
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        switch_2.check(matches(isDisplayed()))

        val appCompatImageButton3 = onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar_settings),
                        childAtPosition(
                            withClassName(`is`("androidx.appcompat.widget.LinearLayoutCompat")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton3.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        onView(withId(R.id.drawer_container_home))
            .perform(DrawerActions.open());
        Thread.sleep(1500)

        val navigationMenuItemView5 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.side_drawer_home),
                            0
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView5.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(1000)

        val button4 = onView(
            allOf(
                withId(R.id.btn_update_profile),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        button4.check(matches(isDisplayed()))

        val appCompatImageButton4 = onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar_profile),
                        childAtPosition(
                            withClassName(`is`("androidx.appcompat.widget.LinearLayoutCompat")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton4.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        onView(withId(R.id.drawer_container_home))
            .perform(DrawerActions.open());
        Thread.sleep(1500)

        val navigationMenuItemView6 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.side_drawer_home),
                            0
                        )
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView6.perform(click())

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
