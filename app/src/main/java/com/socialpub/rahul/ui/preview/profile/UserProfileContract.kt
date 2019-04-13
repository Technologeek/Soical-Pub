package com.socialpub.rahul.ui.preview.profile

import com.socialpub.rahul.base.BaseContract
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.model.User

interface UserProfileContract {

    interface View : BaseContract.View {
        fun attachActions()
        fun dissmissDialog()
        fun updateUserPreview(user: User)
        fun showAllUser(list: List<Post>)
    }

    interface Controller : BaseContract.Controller {
        fun getUserProfile(userId: String?)
    }
}