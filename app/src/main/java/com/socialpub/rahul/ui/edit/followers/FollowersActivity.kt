package com.socialpub.rahul.ui.edit.followers

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseActivity
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.di.Injector
import com.socialpub.rahul.ui.edit.followers.navigator.Navigator
import com.socialpub.rahul.ui.home.members.search.adapter.SearchUserAdapter
import com.socialpub.rahul.ui.home.members.search.adapter.UserProfileListener
import kotlinx.android.synthetic.main.activity_followers.*

class FollowersActivity : BaseActivity(), FollowerContract.View {

    override val contentLayout: Int
        get() = R.layout.activity_followers

    lateinit var controller: FollowerController
    lateinit var followerAdapter: SearchUserAdapter
    lateinit var followingAdapter: SearchUserAdapter
    lateinit var navigator: Navigator

    override fun setup() {
        initToolbar()

        navigator = Navigator(0, this)
        controller = FollowerController(this)
        controller.onStart()

    }

    override fun attachActions() {

        btn_following.setOnClickListener {
            container_following.visibility = View.VISIBLE
            container_followedBy.visibility = View.GONE
        }

        btn_followers.setOnClickListener {
            container_following.visibility = View.GONE
            container_followedBy.visibility = View.VISIBLE
        }

        list_followers.run {
            container_followedBy.visibility = View.GONE
            layoutManager = LinearLayoutManager(this@FollowersActivity)
            adapter = SearchUserAdapter.newInstance(
                object : UserProfileListener {
                    override fun onClickUserProfile(position: Int) {
                        val profile = followerAdapter.getProfileAt(position)
                        val userPrefs = Injector.userPrefs()
                        if (profile.uid != userPrefs.userId) {
                            navigator.openProfilePreview(true, profile.uid)
                        }
                    }
                }
            ).also { followerAdapter = it }
        }

        list_following.run {
            container_following.visibility = View.VISIBLE
            layoutManager = LinearLayoutManager(this@FollowersActivity)
            adapter = SearchUserAdapter.newInstance(
                object : UserProfileListener {
                    override fun onClickUserProfile(position: Int) {
                        val profile = followingAdapter.getProfileAt(position)
                        val userPrefs = Injector.userPrefs()
                        if (profile.uid != userPrefs.userId) {
                            navigator.openProfilePreview(true, profile.uid)
                        }
                    }
                }
            ).also { followingAdapter = it }
        }

    }

    override fun updateFollowerList(followerList: List<User>) {
        text_follower_count.text = "You are followed by (${followerList.size}) :"
        followerAdapter.submitList(followerList) {
            if (followerList.isEmpty()) {
                followingAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun updateFollowingList(followerList: List<User>) {
        text_following_count.text = "You are following (${followerList.size}) :"
        followingAdapter.submitList(followerList) {
            if (followerList.isEmpty()) {
                followingAdapter.notifyDataSetChanged()
            }
        }
    }


    override fun listScrollToTop() {

    }

    override fun showLoading(message: String) = showProgress(message)

    override fun hideLoading() = hideProgress()

    override fun onError(message: String) {
        snack(root_followers, message)
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.stopObservingFollowers()
    }


    private fun initToolbar() = setSupportActionBar(toolbar_followers).let {
        with(requireNotNull(supportActionBar)) {
            setDefaultDisplayHomeAsUpEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}
