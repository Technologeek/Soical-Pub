package com.socialpub.rahul.ui.home.navigation

import android.content.Intent
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.socialpub.rahul.base.BaseNavigator
import com.socialpub.rahul.base.NavFlow
import com.socialpub.rahul.ui.edit.favourites.FavPostActivity
import com.socialpub.rahul.ui.edit.followers.FollowersActivity
import com.socialpub.rahul.ui.preview.notifications.NotificationsActivity
import com.socialpub.rahul.ui.edit.profile.EditProfileActivity
import com.socialpub.rahul.ui.home.HomeActivity
import com.socialpub.rahul.ui.home.members.post.PostFragment
import com.socialpub.rahul.ui.home.members.search.SearchFragment
import com.socialpub.rahul.ui.home.members.user.ProfileFragment
import com.socialpub.rahul.ui.onboarding.OnboardingActivity
import com.socialpub.rahul.ui.preview.location.MapActivity
import com.socialpub.rahul.ui.preview.post.PreviewPostActivity
import com.socialpub.rahul.ui.preview.profile.ProfilePreviewActivity
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
        activity.startActivity(Intent(activity, FollowersActivity::class.java))
    }

    fun openNotifications() {
        activity.startActivity(Intent(activity, NotificationsActivity::class.java))
    }

    fun openEditProfile() {
        activity.startActivity(Intent(activity, EditProfileActivity::class.java))
    }

    fun openSettings() {
        activity.startActivity(Intent(activity, SettingsActivity::class.java))
    }

    fun openProfilePreview(showFollow: Boolean, PreviewUserId: String) {
        activity.startActivity(Intent(activity, ProfilePreviewActivity::class.java).also {
            it.putExtra("showFollow", showFollow)
            it.putExtra("userId", PreviewUserId)
        })
    }

    fun openMapLocation(postId: String) {
        activity.startActivity(Intent(activity, MapActivity::class.java).also {
            it.putExtra("postId", postId)
        })
    }

    fun openPostPreview(enableDelete: Boolean, PreviewPostId: String, PreviewUserId: String) {
        activity.startActivity(Intent(activity, PreviewPostActivity::class.java).also {
            it.putExtra("postId", PreviewPostId)
            it.putExtra("enableDelete", enableDelete)
            it.putExtra("globalUserId", PreviewUserId)
        })
    }

    fun getFragment(screen: String): Fragment = when (screen) {
        NavFlow.Home.FEEDS -> PostFragment.newInstance()
        NavFlow.Home.SEARCH -> SearchFragment.newInstance()
        NavFlow.Home.PROFILE -> ProfileFragment.newInstance()
        else -> PostFragment.newInstance()
    }
}
