package com.socialpub.rahul.ui.onboarding.members.splash


import android.view.View
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseFragment
import com.socialpub.rahul.base.NavFlow
import com.socialpub.rahul.di.Injector
import com.socialpub.rahul.ui.onboarding.navigation.NavController
import io.reactivex.Completable
import java.util.concurrent.TimeUnit

/**
 * Screen fragment is resposnible for selecting
 * for selecting onboarding flow
 * Refer test class for more info and flow.
 */
class SplashFragment : BaseFragment() {

    override val contentLayout: Int = R.layout.fragment_splash

    lateinit var navigator: NavController


    override fun setup(view: View) {
        navigator = attachedContext as NavController

        Completable.timer(2, TimeUnit.SECONDS)
            .subscribe {
                val appPrefs = Injector.userPrefs()
                if (appPrefs.isUserLoggedIn) {
                    navigator.gotoHome()
                } else {
                    navigator.goto(NavFlow.ONBOARD.REGISTER)
                }
            }

    }

    companion object {
        fun newInstance() = SplashFragment()
    }

}
