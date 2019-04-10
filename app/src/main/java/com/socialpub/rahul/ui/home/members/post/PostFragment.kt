package com.socialpub.rahul.ui.home.members.post


import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseFragment
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.ui.home.members.post.adapter.GlobalPostAdapter
import com.socialpub.rahul.ui.home.members.post.adapter.PostClickListener
import kotlinx.android.synthetic.main.fragment_feeds.*
import timber.log.Timber

class PostFragment : BaseFragment(), PostContract.View {

    override val contentLayout: Int
        get() = R.layout.fragment_feeds

    lateinit var controller: PostController
    override fun setup(view: View) {
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
            ImagePicker.create(this)
                .returnMode(ReturnMode.ALL)
                .includeVideo(false)
                .single()
                .showCamera(true)
                .start(PostContract.Controller.Const.IMAGE_PICKER_REQUEST)
        }


        postAdapter = GlobalPostAdapter.newInstance(object : PostClickListener {
            override fun onlikeClicked(adapterPosition: Int) {

            }

            override fun onCommentClicked(position: Int) {

            }

        })

        list_global_post.run {
            layoutManager = LinearLayoutManager(attachedContext)
            adapter = postAdapter
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        controller.handleImagePickerRequest(requestCode, resultCode, data)
    }

    override fun onImagePickerSuccess(path: String) {
        controller.uploadPost(
            Post(
                imagePath = path,
                caption = "Pokemon pokemon pokemon"
            )
        )
    }

    override fun updatePost(globalPost: List<Post>) {
        toast(globalPost.get(0).toString())

        //postAdapter.submitList(globalPost)
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
