package com.socialpub.rahul.ui.edit.followers

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseActivity
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.ui.home.members.search.adapter.SearchUserAdapter
import com.socialpub.rahul.ui.home.members.search.adapter.UserProfileListener
import kotlinx.android.synthetic.main.activity_followers.*

class FollowersActivity : BaseActivity(), FollowerContract.View {

    override val contentLayout: Int
        get() = R.layout.activity_followers

    lateinit var controller: FollowerController
    lateinit var followerAdapter: SearchUserAdapter
    lateinit var followingAdapter: SearchUserAdapter

    override fun setup() {
        initToolbar()
        controller = FollowerController(this)
        controller.onStart()
    }

    override fun attachActions() {

        btn_following.setOnClickListener {
            container_followers.visibility = View.GONE
            container_followedBy.visibility = View.VISIBLE
        }

        btn_followers.setOnClickListener {
            container_followers.visibility = View.VISIBLE
            container_followedBy.visibility = View.GONE
        }

        list_followers.run {
            visibility = View.GONE
            layoutManager = LinearLayoutManager(this@FollowersActivity)
            adapter = SearchUserAdapter.newInstance(
                object : UserProfileListener {
                    override fun onClickUserProfile(position: Int) {


                    }
                }
            ).also { followerAdapter = it }
        }

        list_following.run {
            visibility = View.VISIBLE
            layoutManager = LinearLayoutManager(this@FollowersActivity)
            adapter = SearchUserAdapter.newInstance(
                object : UserProfileListener {
                    override fun onClickUserProfile(position: Int) {


                    }
                }
            ).also { followingAdapter = it }
        }

    }

    override fun updateFollowerList(followerList: List<User>) {
        text_follower_count.text = "You are followed by (${followerList.size}) :"
        followerAdapter.submitList(followerList)
    }

    override fun updateFollowingList(followerList: List<User>) {
        text_following_count.text = "You are following (${followerList.size}) :"
        followingAdapter.submitList(followerList)
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
