package com.socialpub.rahul.ui.preview.post

import com.socialpub.rahul.base.BaseContract
import com.socialpub.rahul.data.model.Post

interface PreviewPostContract {

    interface View : BaseContract.View {
        fun attachActions()
        fun dissmissDialog()
        fun updatePostPreview(post: Post)
    }

    interface Controller : BaseContract.Controller{
        fun getUserPost(postId:String?)
        fun submitComment(text: String)
        fun stopObservingComments()
        fun deletePost()
    }
}