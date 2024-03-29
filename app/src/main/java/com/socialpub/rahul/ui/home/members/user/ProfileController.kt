package com.socialpub.rahul.ui.home.members.user

import com.google.firebase.firestore.ListenerRegistration
import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.data.remote.firebase.sources.post.PostSource
import com.socialpub.rahul.data.remote.firebase.sources.user.UserSource
import com.socialpub.rahul.di.Injector
import timber.log.Timber

class ProfileController(private val view: ProfileContract.View) : ProfileContract.Controller {

    private lateinit var userPrefs: AppPrefs.User
    private lateinit var userSource: UserSource
    private lateinit var postSource: PostSource

    override fun onStart() {
        view.attachActions()
        userPrefs = Injector.userPrefs()
        userSource = Injector.userSource()
        postSource = Injector.postSource()
        view.updateProfileInfo(
            User(
                username = userPrefs.displayName,
                email = userPrefs.email,
                avatar = userPrefs.avatarUrl,
                uid = userPrefs.userId
            )
        )
        startProfileObserving(userPrefs.userId)
        startObservingPublishedPost()
    }

    private var profileListener: ListenerRegistration? = null
    override fun startProfileObserving(uId: String) {
        profileListener =
            userSource.observeUserProfile(uId).addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    view.onError("Profile info not available...try again later")
                    Timber.e(firebaseFirestoreException)
                    return@addSnapshotListener
                }

                if (documentSnapshot != null) {
                    with(userPrefs) {
                        val profile = documentSnapshot.toObject(User::class.java)
                        if (profile != null) {
                            profile.avatar?.apply { avatarUrl = this }
                            following = profile.following.size.toLong()
                            displayName = profile.username
                            view.updateProfileInfo(profile)
                        }
                    }

                }
            }
    }

    override fun stopProfileObserving() {
        profileListener?.remove()
    }

    private var publishPostListener: ListenerRegistration? = null
    override fun startObservingPublishedPost() {
        publishPostListener = userSource.observeUserPublishedPost(userPrefs.userId)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                if (firebaseFirestoreException != null) {
                    view.onError("Post not available...try again later")
                    Timber.e(firebaseFirestoreException)
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    val updatePostList = arrayListOf<Post>()
                    querySnapshot.forEach { queryDocument ->
                        val post = queryDocument.toObject(Post::class.java)
                        updatePostList.add(post)
                    }
                    view.updatePublishList(updatePostList)
                }
            }

    }

    override fun stopObservingPublishedPost() {
        publishPostListener?.remove()
    }

    override fun deletePublishedPost(postID: String) {
        view.showLoading("Deleting...")
        postSource.deleteUserPost(postID, userPrefs.userId)
            .addOnSuccessListener {
                deleteGlobalPost(postID)
            }.addOnFailureListener {
                view.hideLoading()
                view.onError("Post deleting failed...")
                Timber.e(it.localizedMessage)
            }
    }

    private fun deleteGlobalPost(postId: String) {
        postSource.deleteGlobalPost(postId).addOnSuccessListener {
            view.hideLoading()
            view.onError("Post deleted...")
        }.addOnFailureListener {
            view.hideLoading()
            Timber.e(it.localizedMessage)
        }
    }
}