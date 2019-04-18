package com.socialpub.rahul.ui.edit.favourites

import com.socialpub.rahul.base.BaseContract
import com.socialpub.rahul.data.model.Post

interface FavContract {

    interface View : BaseContract.View {
        fun attachActions()
        fun updateLikedList(postList: List<Post>)
        fun listScrollToTop()
        fun onDeletedComplted()
    }

    interface Controller : BaseContract.Controller {
        fun startObservingLikedPost()
        fun stopObservingLikedPost()
        fun deleteFavPost(postList: List<Post>)
    }

}