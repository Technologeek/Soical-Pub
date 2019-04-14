package com.socialpub.rahul.ui.edit.post

import android.content.Intent
import com.socialpub.rahul.base.BaseContract

interface PostUploadContract {

    interface View : BaseContract.View {
        fun attachActions()
        fun showSelectedImage(path: String)
        fun dissmissDialog()
    }

    interface Controller : BaseContract.Controller {
        fun handleImagePickerRequest(requestCode: Int, resultCode: Int, data: Intent?)
        fun updateUserProfile(username: String)
    }
}

internal const val REQUEST_LOCATION_PERMISSION = 1000
internal const val DEFAULT_ZOOM = 15f