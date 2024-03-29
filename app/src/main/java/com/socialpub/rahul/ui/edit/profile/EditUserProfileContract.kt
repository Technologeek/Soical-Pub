package com.socialpub.rahul.ui.edit.profile

import android.content.Intent
import com.socialpub.rahul.base.BaseContract

interface EditUserProfileContract {

    interface View : BaseContract.View {
        fun attachActions(profileUrl:String)
        fun showSelectedImage(path: String)
        fun dissmissDialog()
    }

    interface Controller:BaseContract.Controller{
        fun handleImagePickerRequest(requestCode: Int, resultCode: Int, data: Intent?)
        fun updateUserProfile(username:String)
    }
}