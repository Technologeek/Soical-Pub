package com.socialpub.rahul.ui.home.members.post

import android.content.Intent
import com.socialpub.rahul.base.BaseContract
import com.socialpub.rahul.data.model.Post

interface PostContract {

    interface View : BaseContract.View {
        fun attachActions()
        fun updatePost(globalPost: List<Post>)
        fun listScrollToTop()
    }

    interface Controller : BaseContract.Controller {

        object Const {
            const val IMAGE_PICKER_REQUEST = 1001
        }

        fun startObservingGlobalFeeds()
        fun stopObservingGlobalFeeds()
        fun filterLatest()
        fun filterCommented()
        fun filterLiked()
        fun addLike(post: Post?)
        fun addFav(post: Post?)
        fun reportPost(post: Post?)
    }

}