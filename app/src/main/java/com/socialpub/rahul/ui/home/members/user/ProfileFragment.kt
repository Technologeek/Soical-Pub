package com.socialpub.rahul.ui.home.members.user


import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseFragment
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.ui.home.members.search.adapter.SearchPostAdapter
import com.socialpub.rahul.ui.home.members.search.adapter.SearchPostListener
import com.socialpub.rahul.ui.profile.edit.ProfileEditBottomSheet
import com.squareup.picasso.Picasso
import io.reactivex.Completable
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_proifle.*
import java.util.concurrent.TimeUnit

class ProfileFragment : BaseFragment(), ProfileContract.View {


    override val contentLayout: Int
        get() = R.layout.fragment_proifle


    lateinit var controller: ProfileController
    override fun setup(view: View) {
        controller = ProfileController(this)
        controller.onStart()
    }

    lateinit var publishedPostAdapter: SearchPostAdapter
    lateinit var likedPostAdapter: SearchPostAdapter

    override fun attachActions() {

        list_profile_published_post.visibility = View.VISIBLE
        list_profile_liked_post.visibility = View.GONE

        btn_edit_profile.setOnClickListener {
            ProfileEditBottomSheet.newInstance().show(childFragmentManager, "Profile_Bottom_Sheet")
        }

        btn_profile_user_post.setOnClickListener {
            listScrollToTop()
            list_profile_published_post.visibility = View.VISIBLE
            list_profile_liked_post.visibility = View.GONE
        }

        btn_profile_liked_post.setOnClickListener {
            listScrollToTop()
            list_profile_published_post.visibility = View.GONE
            list_profile_liked_post.visibility = View.VISIBLE
        }

        publishedPostAdapter = SearchPostAdapter.newInstance(
            object : SearchPostListener {
                override fun onPostClicked(position: Int) {
                    toast("clicked $position")
                }
            }
        )

        list_profile_published_post.run {
            layoutManager = LinearLayoutManager(attachedContext)
            adapter = publishedPostAdapter
        }


        likedPostAdapter = SearchPostAdapter.newInstance(
            object : SearchPostListener {
                override fun onPostClicked(position: Int) {
                    toast("clicked $position")
                }
            }
        )

        list_profile_liked_post.run {
            layoutManager = LinearLayoutManager(attachedContext)
            adapter = likedPostAdapter
        }

    }

    override fun updateProfileInfo(profile: User) {

        text_profile_user_name.text = profile.username
        text_profile_user_email.text = profile.email
        Picasso.get()
            .load(profile.avatar)
            .placeholder(R.mipmap.ic_launcher_round)
            .transform(CropCircleTransformation())
            .into(image_profile_avatar)
    }

    override fun updateLikedList(postList: List<Post>) {
        //likedPostAdapter.submitList(postList)
    }

    override fun updatePublishList(postList: List<Post>) {
        publishedPostAdapter.submitList(postList)
    }

    override fun listScrollToTop() {
        Completable.timer(300, TimeUnit.MILLISECONDS)
            .subscribe {
                list_profile_liked_post.smoothScrollToPosition(0)
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
        controller.stopObservingLikedPost()
        controller.stopObservingPublishedPost()
        controller.stopProfileObserving()
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
