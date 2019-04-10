package com.socialpub.rahul.ui.onboarding

import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseActivity
import com.socialpub.rahul.base.NavFlow
import com.socialpub.rahul.ui.onboarding.navigation.NavController
import com.socialpub.rahul.ui.onboarding.navigation.Navigator

/**
 * Onboarding module is responsible for registering using and taking all the other
 * neccessary information about the user and upload it to our backend service. Refer
 * Onboarding test class for more info and flow.
 */
class OnboardingActivity : BaseActivity(), NavController {

    override val contentLayout: Int = R.layout.activity_onboarding
    lateinit var navigator: Navigator

    override fun setup() {

        navigator = Navigator(R.id.screen_onboarding, this)

        navigator.addFragment(
            navigator.getFragment(NavFlow.ONBOARD.SPLASH),
            NavFlow.ONBOARD.SPLASH
        )

    }

    override fun goto(screen: String) {
        navigator.goto(screen)
    }

    override fun goBack() = navigator.goBack()

    override fun gotoHome() = navigator.gotoHome()

}
