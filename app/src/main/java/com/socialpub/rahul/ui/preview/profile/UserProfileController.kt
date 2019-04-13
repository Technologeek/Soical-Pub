package com.socialpub.rahul.ui.preview.profile

import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.data.remote.firebase.sources.post.PostSource
import com.socialpub.rahul.data.remote.firebase.sources.user.UserSource
import com.socialpub.rahul.di.Injector
import timber.log.Timber
import java.lang.Exception

class UserProfileController(
    private val view: UserProfileContract.View
) : UserProfileContract.Controller {

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

        var following: List<String>? = null

        userSource.getUser(userPrefs.userId).addOnSuccessListener { doc ->
            val user = doc.toObject(User::class.java)
            if (user != null) {
                following = user.following
            } else {
                error(Exception("user is null"))
            }

        }.addOnFailureListener {
            error(it)
        }

        userSource.getUser(userId)
            .addOnSuccessListener { doc ->
                val user = doc.toObject(User::class.java)
                if (user != null) {
                    previewUser = user

                    following?.forEach { userFollowing ->
                        if (user.uid == userFollowing) {
                            view.disableFollowing()
                        }
                    }
                    view.updateUserPreview(user)
                    getPreviewUserPost(user)
                    view.hideLoading()
                } else {
                    error(Exception("error in null"))
                }

            }.addOnFailureListener {
                error(it)
            }
    }

    private fun error(it: Exception) {
        view.dissmissDialog()
        view.hideLoading()
        view.onError("Unable to show profile...")
        Timber.e(it.localizedMessage)
    }

    private fun getPreviewUserPost(user: User) {
        val postList = arrayListOf<Post>()
        postSource.getAllPublishedPost(user.uid).addOnSuccessListener { querySnap ->
            querySnap.forEach { docSnap ->
                val post = docSnap.toObject(Post::class.java)
                post?.let { postList.add(it) }

            }
            view.showAllUser(postList)
            view.hideLoading()
        }.addOnFailureListener {
            error(it)
        }
    }


}