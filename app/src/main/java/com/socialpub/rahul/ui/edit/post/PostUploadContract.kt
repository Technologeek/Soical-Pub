package com.socialpub.rahul.ui.edit.post

import android.content.Intent
import com.socialpub.rahul.base.BaseContract
import com.socialpub.rahul.data.model.Post

interface PostUploadContract {

    interface View : BaseContract.View {
        fun attachActions()
        fun showSelectedImage(path: String)
        fun dissmissDialog()
        fun onPostSubmittedSuccess()
    }

    interface Controller : BaseContract.Controller {
        fun handleImagePickerRequest(requestCode: Int, resultCode: Int, data: Intent?)
        fun uploadPost(post: Post)
    }
}

internal const val REQUEST_LOCATION_PERMISSION = 1000
internal const val DEFAULT_ZOOM = 15f