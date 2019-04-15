package com.socialpub.rahul.ui.home

import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseActivity
import com.socialpub.rahul.base.NavFlow
import com.socialpub.rahul.ui.home.navigation.NavController
import com.socialpub.rahul.ui.home.navigation.Navigator
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(), NavController {


    override val contentLayout: Int
        get() = R.layout.activity_home

    lateinit var navigator: Navigator

    override fun setup() {
        navigator = Navigator(R.id.screen_home, this)

        navigator.addFragment(
            navigator.getFragment(NavFlow.Home.FEEDS),
            NavFlow.Home.FEEDS
        )

        bottom_nave_home.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> navigator.goto(NavFlow.Home.FEEDS)
                R.id.action_search -> navigator.goto(NavFlow.Home.SEARCH)
                R.id.action_profile -> navigator.goto(NavFlow.Home.PROFILE)
                else -> navigator.goto(NavFlow.Home.FEEDS)
            }
            return@setOnNavigationItemSelectedListener true
        }

    }

    override fun signoutUser() = navigator.restartApp()

    override fun goto(screen: String) {
        navigator.goto(screen)
    }
    
    override fun goBack() = navigator.goBack()

}
