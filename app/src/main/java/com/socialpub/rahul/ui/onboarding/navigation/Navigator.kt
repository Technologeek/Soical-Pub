package com.socialpub.rahul.ui.onboarding.navigation

import android.content.Intent
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.socialpub.rahul.base.BaseNavigator
import com.socialpub.rahul.base.NavFlow
import com.socialpub.rahul.ui.home.HomeActivity
import com.socialpub.rahul.ui.onboarding.OnboardingActivity
import com.socialpub.rahul.ui.onboarding.members.register.RegisterFragment
import com.socialpub.rahul.ui.onboarding.members.splash.SplashFragment

class Navigator(
    @IdRes private val activeScreen: Int,
    private val activity: OnboardingActivity
) : BaseNavigator(activeScreen, activity) {

    fun goto(screen: String) = replaceFragment(getFragment(screen), screen)

    //no need to write back mechanism for this module
    fun goBack() = activity.onBackPressed()

    fun getFragment(screen: String): Fragment = when (screen) {
        NavFlow.ONBOARD.SPLASH -> SplashFragment.newInstance()
        NavFlow.ONBOARD.REGISTER -> RegisterFragment.newInstance()
        else -> SplashFragment.newInstance()
    }

    fun gotoHome() {
        activity.startActivity(Intent(activity, HomeActivity::class.java))
        activity.finish()
    }
}
