package com.socialpub.rahul.ui.preview.profile

import android.content.Intent
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.socialpub.rahul.base.BaseNavigator
import com.socialpub.rahul.base.NavFlow
import com.socialpub.rahul.ui.edit.followers.FollowersActivity
import com.socialpub.rahul.ui.home.members.post.PostFragment
import com.socialpub.rahul.ui.home.members.search.SearchFragment
import com.socialpub.rahul.ui.home.members.user.ProfileFragment
import com.socialpub.rahul.ui.preview.location.MapActivity
import com.socialpub.rahul.ui.preview.post.PreviewPostActivity
import com.socialpub.rahul.ui.preview.profile.ProfilePreviewActivity

class Navigator(
    @IdRes private val activeScreen: Int,
    private val activity: ProfilePreviewActivity
) : BaseNavigator(activeScreen, activity) {

    fun goto(screen: String) = replaceFragment(getFragment(screen), screen)

    //no need to write back mechanism for this module
    fun goBack() = activity.onBackPressed()


    fun openPostPreview(enableDelete: Boolean, PreviewPostId: String, PreviewUserId: String) {
        activity.startActivity(Intent(activity, PreviewPostActivity::class.java).also {
            it.putExtra("postId", PreviewPostId)
            it.putExtra("enableDelete", enableDelete)
            it.putExtra("globalUserId", PreviewUserId)
        })
    }

    fun openMapLocation(postId: String) {
        activity.startActivity(Intent(activity, MapActivity::class.java).also {
            it.putExtra("postId", postId)
        })
    }


    fun getFragment(screen: String): Fragment = when (screen) {
        NavFlow.Home.FEEDS -> PostFragment.newInstance()
        NavFlow.Home.SEARCH -> SearchFragment.newInstance()
        NavFlow.Home.PROFILE -> ProfileFragment.newInstance()
        else -> PostFragment.newInstance()
    }
}
