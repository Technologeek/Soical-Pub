package com.socialpub.rahul.ui.home.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.socialpub.rahul.base.BaseNavigator
import com.socialpub.rahul.base.NavFlow
import com.socialpub.rahul.ui.home.HomeActivity
import com.socialpub.rahul.ui.home.members.post.PostFragment
import com.socialpub.rahul.ui.home.members.search.SearchFragment
import com.socialpub.rahul.ui.home.members.user.ProfileFragment

class Navigator(
    @IdRes private val activeScreen: Int,
    private val activity: HomeActivity
) : BaseNavigator(activeScreen, activity) {

    fun goto(screen: String) = replaceFragment(getFragment(screen), screen)

    //no need to write back mechanism for this module
    fun goBack() = activity.onBackPressed()

    fun getFragment(screen: String): Fragment = when (screen) {
        NavFlow.Home.FEEDS -> PostFragment.newInstance()
        NavFlow.Home.SEARCH -> SearchFragment.newInstance()
        NavFlow.Home.PROFILE -> ProfileFragment.newInstance()
        else -> PostFragment.newInstance()
    }
}
