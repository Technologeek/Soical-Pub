package com.socialpub.rahul.ui.home.members.post


import android.Manifest
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseFragment
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.di.Injector
import com.socialpub.rahul.ui.edit.post.PostBottomSheet
import com.socialpub.rahul.ui.edit.post.REQUEST_LOCATION_PERMISSION
import com.socialpub.rahul.ui.home.members.post.adapter.GlobalPostAdapter
import com.socialpub.rahul.ui.home.members.post.adapter.PostClickListener
import com.socialpub.rahul.ui.home.navigation.NavController
import com.socialpub.rahul.ui.preview.post.PreviewPostBottomSheet
import io.reactivex.Completable
import kotlinx.android.synthetic.main.fragment_feeds.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import java.util.concurrent.TimeUnit


class PostFragment : BaseFragment(), PostContract.View, EasyPermissions.PermissionCallbacks {


    override val contentLayout: Int
        get() = R.layout.fragment_feeds

    lateinit var controller: PostController
    lateinit var navigator: NavController

    override fun setup(view: View) {
        navigator = attachedContext as NavController

        controller = PostController(this)
        controller.onStart()
    }

    override fun onResume() {
        super.onResume()
        controller.startObservingGlobalFeeds()
    }

    override fun onPause() {
        super.onPause()
        controller.stopObservingGlobalFeeds()
    }

    private lateinit var postAdapter: GlobalPostAdapter

    override fun attachActions() {

        fab_upload.setOnClickListener {

            //check permission
            val perms = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

            if (EasyPermissions.hasPermissions(attachedContext, *perms)) {

                val uploadSheet = PostBottomSheet.newInstance()
                uploadSheet.show(childFragmentManager, "UploadPost")

            } else {
                EasyPermissions.requestPermissions(
                    this,
                    "Please provide location permission for tagging pictures",
                    REQUEST_LOCATION_PERMISSION,
                    *perms
                )
            }

        }

        btn_sort.setOnClickListener {
            val popup = PopupMenu(attachedContext, it)
            popup.run {

                menuInflater.inflate(R.menu.menu_sort_popup, popup.menu)

                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.action_latest -> controller.filterLatest()
                        R.id.action_liked -> controller.filterLiked()
                        R.id.action_controversial -> controller.filterCommented()
                        else -> Timber.e("Error")
                    }
                    return@setOnMenuItemClickListener true
                }
                show()
            }

        }


        postAdapter = GlobalPostAdapter.newInstance(object : PostClickListener {
            override fun onProfileClicked(position: Int) {
                val post = postAdapter.getPostAt(position)
                val userPrefs = Injector.userPrefs()
                if (post.uid != userPrefs.userId) {
                    navigator.openProfilePreview(true, post.uid)
                }
            }

            override fun onlikeClicked(position: Int) {
                val post = postAdapter.getPostAt(position)
                controller.addLike(post)
            }

            override fun onCommentClicked(position: Int) {
                val post = postAdapter.getPostAt(position)
                val postPreview = PreviewPostBottomSheet.newInstance(post.postId, false, post.uid)
                postPreview.showNow(childFragmentManager, "Post_Profile_Preview_post")
            }

        })

        list_global_post.run {
            layoutManager = LinearLayoutManager(attachedContext)
            adapter = postAdapter
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        val uploadSheet = PostBottomSheet.newInstance()
        uploadSheet.show(childFragmentManager, "UploadPost")
    }
    
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            onError("Please grant permissions from setting if you want to post...")
            AppSettingsDialog.Builder(this).build().show()
        }

    }


    override fun listScrollToTop() {
        Completable.timer(300, TimeUnit.MILLISECONDS)
            .subscribe {
                list_global_post.smoothScrollToPosition(0)
            }
    }

    override fun updatePost(globalPost: List<Post>) {
        postAdapter.submitList(globalPost)
    }

    override fun showLoading(message: String) = showProgress(message)

    override fun hideLoading() = hideProgress()

    override fun onError(message: String) {
        toast(message)
    }

    companion object {
        fun newInstance() = PostFragment()
    }
}
