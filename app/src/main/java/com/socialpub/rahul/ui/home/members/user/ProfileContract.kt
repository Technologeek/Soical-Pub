package com.socialpub.rahul.ui.home.members.user

import com.socialpub.rahul.base.BaseContract
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.model.User

interface ProfileContract {

    interface View : BaseContract.View {
        fun attachActions()
        fun updatePublishList(postList: List<Post>)
        fun updateProfileInfo(profile: User)
        fun listScrollToTop()
    }

    interface Controller : BaseContract.Controller {
        fun startProfileObserving(uId: String)
        fun startObservingPublishedPost()
        fun stopObservingPublishedPost()
        fun stopProfileObserving()
        fun deletePublishedPost(postID:String)
    }

}