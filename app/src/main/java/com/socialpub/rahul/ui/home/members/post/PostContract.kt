package com.socialpub.rahul.ui.home.members.post

import android.content.Intent
import com.socialpub.rahul.base.BaseContract
import com.socialpub.rahul.data.model.Post

interface PostContract {

    interface View : BaseContract.View {
        fun attachActions()
        fun onImagePickerSuccess(path: String)
        fun updatePost(globalPost: List<Post>)

    }

    interface Controller : BaseContract.Controller {

        object Const {
            const val IMAGE_PICKER_REQUEST = 1001
        }

        fun handleImagePickerRequest(requestCode: Int, resultCode: Int, data: Intent?)
        fun uploadPost(post: Post)
        fun startObservingGlobalFeeds()
        fun stopObservingGlobalFeeds()
    }

}