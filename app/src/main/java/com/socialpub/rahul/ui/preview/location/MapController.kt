package com.socialpub.rahul.ui.preview.location

import android.content.Intent
import com.cloudinary.android.callback.ErrorInfo
import com.esafirm.imagepicker.features.ImagePicker
import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.remote.firebase.sources.post.PostSource
import com.socialpub.rahul.data.remote.firebase.sources.user.UserSource
import com.socialpub.rahul.di.Injector
import com.socialpub.rahul.ui.home.members.post.PostContract
import com.socialpub.rahul.utils.helper.CloudinaryUploadHelper
import timber.log.Timber

class MapController(
    private val view: MapContract.View
) : MapContract.Controller {

    private lateinit var userPrefs: AppPrefs.User
    private lateinit var userSource: UserSource
    private lateinit var postSource: PostSource

    override fun onStart() {
        userPrefs = Injector.userPrefs()
        userSource = Injector.userSource()
        postSource = Injector.postSource()
        view.attachActions()
    }

    override fun getPost(postId: String) {
        view.showLoading()
        postSource
            .getGlobalPost(postId)
            .addOnSuccessListener {
                val post = it.toObject(Post::class.java)
                if (post != null) {
                    view.showPost(post)
                    view.hideLoading()
                }
            }.addOnFailureListener {
                view.hideLoading()
                view.onError("Something went wrong...")
                Timber.e(it)
            }
    }


}