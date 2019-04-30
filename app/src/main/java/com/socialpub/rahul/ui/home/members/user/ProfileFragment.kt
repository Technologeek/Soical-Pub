package com.socialpub.rahul.ui.home.members.user


import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseFragment
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.ui.home.members.search.adapter.SearchPostAdapter
import com.socialpub.rahul.ui.home.members.search.adapter.SearchPostListener
import com.socialpub.rahul.ui.home.navigation.NavController
import com.socialpub.rahul.utils.AppConst
import com.squareup.picasso.Picasso
import io.reactivex.Completable
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_proifle.*
import java.util.concurrent.TimeUnit

class ProfileFragment : BaseFragment(), ProfileContract.View {


    override val contentLayout: Int
        get() = R.layout.fragment_proifle


    lateinit var controller: ProfileController
    lateinit var navigator: NavController
    override fun setup(view: View) {
        navigator = attachedContext as NavController
        controller = ProfileController(this)
        controller.onStart()
    }

    lateinit var publishedPostAdapter: SearchPostAdapter

    override fun attachActions() {

        btn_profile_user_post.setOnClickListener {
            listScrollToTop()
            list_profile_published_post.visibility = View.VISIBLE
        }

        publishedPostAdapter = SearchPostAdapter.newInstance(
            showPostActions = false,
            showPostProfile = false,
            listener = object : SearchPostListener {
                override fun onPostLongClicked(position: Int) {
                    val post = publishedPostAdapter.getPostAt(position)
                    showOptionsDialog(post)
                }

                override fun onPostLikeClicked(position: Int) {

                }

                override fun onPostCommentClicked(position: Int) {

                }

                override fun onPostClicked(position: Int) {
                    val post = publishedPostAdapter.getPostAt(position)
                    navigator.openPostPreview(true, post.postId, "")
                }
            }
        )

        list_profile_published_post.run {
            layoutManager = LinearLayoutManager(attachedContext)
            adapter = publishedPostAdapter
        }

    }

    override fun updateProfileInfo(profile: User) {

        text_followers.text = "${profile.followedBy.size}\nfollowers"
        text_following.text = "${profile.following.size}\nfollowing"

        text_profile_user_name.text = profile.username
        text_profile_user_email.text = profile.email
        Picasso.get()
            .load(profile.avatar)
            .placeholder(R.mipmap.ic_launcher)
            .transform(CropCircleTransformation())
            .into(image_profile_avatar)
    }


    override fun updatePublishList(postList: List<Post>) {
        publishedPostAdapter.submitList(postList) {
            if (postList.isEmpty()) {
                publishedPostAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun listScrollToTop() {
        Completable.timer(300, TimeUnit.MILLISECONDS)
            .subscribe {
                list_profile_published_post.smoothScrollToPosition(0)
            }
    }

    override fun showLoading(message: String) = showProgress(message)

    override fun hideLoading() = hideProgress()

    override fun onError(message: String) {
        toast(message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        controller.stopObservingPublishedPost()
        controller.stopProfileObserving()
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private fun showOptionsDialog(post: Post) {
        val items = arrayOf<String>("View location", "Delete")
        val builder = AlertDialog.Builder(attachedContext)
        builder.setTitle("Post Options")
        builder.setItems(items, DialogInterface.OnClickListener { dialog, itemPos ->
            when (itemPos) {
                AppConst.USER_PROFILE_VIEW_MAP -> navigator.openPostLocationOnMap(post)
                AppConst.USER_PROFILE_DELETE -> actionDelePostConfirm(post.postId)
                else -> {
                }
            }
        })
        val alert = builder.create()
        if (isVisible) alert.show()
    }


    private fun actionDelePostConfirm(postId: String) {
        val builder = AlertDialog.Builder(attachedContext)

        with(builder)
        {
            setTitle("Delete Post")
            setMessage("are you sure you want to delete your post?")
            setPositiveButton("Yes", DialogInterface.OnClickListener(function = { dialog, which ->
                controller.deletePublishedPost(postId)
            }))
            setNegativeButton("No", DialogInterface.OnClickListener(function = { dialog, which ->
                dialog.dismiss()
            }))
            if (isVisible) show()
        }
    }

}
