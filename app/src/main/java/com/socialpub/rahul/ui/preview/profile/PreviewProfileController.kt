package com.socialpub.rahul.ui.preview.profile

import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.data.remote.firebase.sources.post.PostSource
import com.socialpub.rahul.data.remote.firebase.sources.user.UserSource
import com.socialpub.rahul.di.Injector

class PreviewProfileController(
    private val view: PreviewProfileContract.View
) : PreviewProfileContract.Controller {

    private lateinit var userPrefs: AppPrefs.User
    private lateinit var userSource: UserSource
    private lateinit var postSource: PostSource

    override fun onStart() {
        userPrefs = Injector.userPrefs()
        userSource = Injector.userSource()
        postSource = Injector.postSource()
        view.attachActions()
    }


    var previewUser: User? = null
    override fun getUserProfile(userId: String?) {

        if (userId.isNullOrBlank()) {
            view.dissmissDialog()
            view.onError("Unable to show preview...")
            return
        }

        view.showLoading("loading..")

        userSource.getUser(userId)
            .addOnSuccessListener { doc ->
                val user = doc.toObject(User::class.java)

                view.hideLoading()
                if (user != null) {
                    previewUser = user
                    view.updateUserPreview(user)
                } else {
                    view.dissmissDialog()
                    view.onError("Unable to show preview...")
                }
            }
    }



}