package com.socialpub.rahul.ui.preview.profile

import android.Manifest
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseActivity
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.di.Injector
import com.socialpub.rahul.ui.edit.post.REQUEST_LOCATION_PERMISSION
import com.socialpub.rahul.ui.home.members.search.adapter.SearchPostAdapter
import com.socialpub.rahul.ui.home.members.search.adapter.SearchPostListener
import com.socialpub.rahul.utils.AppConst
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.bottom_sheet_preview_proifle.*
import pub.devrel.easypermissions.EasyPermissions

class ProfilePreviewActivity : BaseActivity(), UserProfileContract.View {

    override val contentLayout: Int
        get() = R.layout.bottom_sheet_preview_proifle

    lateinit var controller: UserProfileController
    lateinit var navigator: Navigator

    override fun setup() {
        initToolbar()
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

        if (userId == Injector.userPrefs().userId) {
            dissmissDialog()
        }

        isFollowing(false)
        controller.getUserProfile(userId)

        searchPostAdapter = SearchPostAdapter.newInstance(
            object : SearchPostListener {
                override fun onPostClicked(position: Int) {
                    toast("Long press for option menu")
                }

                override fun onPostLongClicked(position: Int) {
                    val post = searchPostAdapter.getPostAt(position)
                    showOptionsDialog(post)
                }

                override fun onPostLikeClicked(position: Int) {
                    val post = searchPostAdapter.getPostAt(position)
                    controller.addLike(post)
                }

                override fun onPostCommentClicked(position: Int) {
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

        val userId = intent?.extras?.getString("userId")

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

            val fromSearch = intent?.extras?.getBoolean("fromSearchResults") ?: false

            if (fromSearch && !isVisibletoLocationSearch) {
                toast("User is not Visible to Public Search")
                finish()
            }

            Picasso.get()
                .load(avatar)
                .placeholder(R.mipmap.ic_launcher)
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

    private fun initToolbar() = setSupportActionBar(toolbar_profile).let {
        with(requireNotNull(supportActionBar)) {
            setDefaultDisplayHomeAsUpEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showOptionsDialog(post: Post) {
        val items = arrayOf<String>("Add to favourite", "View location", "Report")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Post Options")
        builder.setItems(items, DialogInterface.OnClickListener { dialog, itemPos ->
            when (itemPos) {
                AppConst.PROFILE_PREVIEW_ADD_FAV -> controller.addFav(post)
                AppConst.PROFILE_PREVIEW_VIEW_MAP -> {

                    val perms =
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

                    if (EasyPermissions.hasPermissions(this@ProfilePreviewActivity, *perms)) {
                        navigator.openMapLocation(post.postId)
                    } else {
                        EasyPermissions.requestPermissions(
                            this,
                            "Please provide location permission for tagging pictures",
                            REQUEST_LOCATION_PERMISSION,
                            *perms
                        )
                    }

                }


                AppConst.PROFILE_PREVIEW_REPORT -> controller.reportPost(post)
                else -> {
                }
            }
        })
        val alert = builder.create()
        if (!isFinishing) alert.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}
