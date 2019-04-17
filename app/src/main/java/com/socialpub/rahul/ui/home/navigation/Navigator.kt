package com.socialpub.rahul.ui.home.navigation

import android.content.Intent
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.socialpub.rahul.base.BaseNavigator
import com.socialpub.rahul.base.NavFlow
import com.socialpub.rahul.ui.edit.favourites.FavPostActivity
import com.socialpub.rahul.ui.edit.post.PostActivity
import com.socialpub.rahul.ui.edit.profile.EditProfileActivity
import com.socialpub.rahul.ui.home.HomeActivity
import com.socialpub.rahul.ui.home.members.post.PostFragment
import com.socialpub.rahul.ui.home.members.search.SearchFragment
import com.socialpub.rahul.ui.home.members.user.ProfileFragment
import com.socialpub.rahul.ui.navigation.FollowerActivity
import com.socialpub.rahul.ui.onboarding.OnboardingActivity
import com.socialpub.rahul.ui.settings.SettingsActivity

class Navigator(
    @IdRes private val activeScreen: Int,
    private val activity: HomeActivity
) : BaseNavigator(activeScreen, activity) {

    fun goto(screen: String) = replaceFragment(getFragment(screen), screen)

    //no need to write back mechanism for this module
    fun goBack() = activity.onBackPressed()

    fun restartApp() {
        activity.startActivity(Intent(activity, OnboardingActivity::class.java))
        activity.finish()
    }

    fun openManageFavourite() {
        activity.startActivity(Intent(activity, FavPostActivity::class.java))
    }

    fun openManageFollowers() {
        activity.startActivity(Intent(activity, FollowerActivity::class.java))
    }

    fun openManagePost() {
        activity.startActivity(Intent(activity, PostActivity::class.java))
    }

    fun openEditProfile() {
        activity.startActivity(Intent(activity, EditProfileActivity::class.java))
    }

    fun openSettings() {
        activity.startActivity(Intent(activity, SettingsActivity::class.java))
    }



    fun getFragment(screen: String): Fragment = when (screen) {
        NavFlow.Home.FEEDS -> PostFragment.newInstance()
        NavFlow.Home.SEARCH -> SearchFragment.newInstance()
        NavFlow.Home.PROFILE -> ProfileFragment.newInstance()
        else -> PostFragment.newInstance()
    }
}
