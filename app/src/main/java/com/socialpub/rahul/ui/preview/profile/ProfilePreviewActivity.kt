package com.socialpub.rahul.ui.preview.profile

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseActivity
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.ui.home.members.search.adapter.SearchPostAdapter
import com.socialpub.rahul.ui.home.members.search.adapter.SearchPostListener
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.bottom_sheet_preview_proifle.*

class ProfilePreviewActivity : BaseActivity(), UserProfileContract.View {

    override val contentLayout: Int
        get() = R.layout.bottom_sheet_preview_proifle

    lateinit var controller: UserProfileController
    lateinit var navigator: Navigator

    override fun setup() {
        navigator = Navigator(0, this)
        controller = UserProfileController(this)
        controller.onStart()
    }

    private lateinit var searchPostAdapter: SearchPostAdapter

    override fun attachActions() {

        val showFollow = intent?.getBooleanExtra("showFollow", true) ?: true

        if (showFollow) {
            btn_preview_follow.visibility = View.VISIBLE
        } else {
            btn_preview_follow.visibility = View.GONE
        }


        val userId = intent?.getStringExtra("userId")

        isFollowing(false)

        controller.getUserProfile(userId)

        btn_preview_profile_close.setOnClickListener {
            dissmissDialog()
        }

        searchPostAdapter = SearchPostAdapter.newInstance(
            object : SearchPostListener {
                override fun onPostClicked(position: Int) {
                    val post = searchPostAdapter.getPostAt(position)
                    navigator.openPostPreview(false, post.postId, post.uid)
                }
            }
        )

        list_preview_profile_published_post.run {
            layoutManager = LinearLayoutManager(this@ProfilePreviewActivity)
            adapter = searchPostAdapter
        }
    }

    override fun isFollowing(following: Boolean) {
        val userId = intent?.getStringExtra("userId")

        if (following) {
            btn_preview_follow.text = "unfollow"
        } else {
            btn_preview_follow.text = "follow"
        }

        btn_preview_follow.setOnClickListener {
            controller.updatefollowing(following, userId)
        }
    }

    override fun updateUserPreview(user: User) {
        with(user) {
            Picasso.get()
                .load(avatar)
                .placeholder(R.mipmap.ic_launcher_round)
                .transform(CropCircleTransformation())
                .into(image_preview_profile_avatar)

            text_preview_profile_user_email.text = username
            text_preview_profile_user_name.text = email
        }
    }

    override fun showAllUser(list: List<Post>) {
        searchPostAdapter.submitList(list)
    }

    override fun onError(message: String) = toast(message)


    override fun showLoading(message: String) {
        showProgress(message)
    }

    override fun hideLoading() {
        hideProgress()
    }

    override fun dissmissDialog() {
        finish()
    }
}
