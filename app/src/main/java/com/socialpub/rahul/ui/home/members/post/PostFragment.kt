package com.socialpub.rahul.ui.home.members.post


import android.content.Intent
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseFragment
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.ui.home.members.post.adapter.GlobalPostAdapter
import com.socialpub.rahul.ui.home.members.post.adapter.PostClickListener
import com.socialpub.rahul.ui.home.navigation.NavController
import kotlinx.android.synthetic.main.fragment_feeds.*


class PostFragment : BaseFragment(), PostContract.View {

    override val contentLayout: Int
        get() = com.socialpub.rahul.R.layout.fragment_feeds

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
            ImagePicker.create(this)
                .returnMode(ReturnMode.ALL)
                .includeVideo(false)
                .single()
                .showCamera(true)
                .start(PostContract.Controller.Const.IMAGE_PICKER_REQUEST)
        }

        btn_sort.setOnClickListener {
            val popup = PopupMenu(attachedContext, it)
            popup.run {

                menuInflater.inflate(R.menu.menu_sort_popup, popup.menu)

                setOnMenuItemClickListener {
                    toast("todo click filter ${it.title}")
//                    when (it.itemId) {
//                        R.id.action_latest -> controller
//                        R.id.action_liked -> controller
//                        R.id.action_controversial -> controller
//                        else -> Timber.e("Error")
//                    }
                    return@setOnMenuItemClickListener true
                }
                show()
            }

        }


        postAdapter = GlobalPostAdapter.newInstance(object : PostClickListener {
            override fun onlikeClicked(position: Int) {
                toast("clicked like:$position")
            }

            override fun onCommentClicked(position: Int) {
                toast("clicked comment:$position")
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
