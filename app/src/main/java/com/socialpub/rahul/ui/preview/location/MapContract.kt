package com.socialpub.rahul.ui.preview.location

import android.content.Intent
import com.socialpub.rahul.base.BaseContract
import com.socialpub.rahul.data.model.Post

interface MapContract {

    interface View : BaseContract.View {
        fun attachActions()
        fun showPost(post: Post)
    }

    interface Controller : BaseContract.Controller {
        fun getPost(postId: String)
    }
}

internal const val REQUEST_LOCATION_PERMISSION = 1000
internal const val DEFAULT_ZOOM = 15f