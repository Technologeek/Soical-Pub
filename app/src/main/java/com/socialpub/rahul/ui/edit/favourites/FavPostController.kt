package com.socialpub.rahul.ui.edit.favourites

import com.google.firebase.firestore.ListenerRegistration
import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.remote.firebase.sources.post.PostSource
import com.socialpub.rahul.data.remote.firebase.sources.user.UserSource
import com.socialpub.rahul.di.Injector
import timber.log.Timber

class FavPostController(private val view: FavContract.View) : FavContract.Controller {

    private lateinit var userPrefs: AppPrefs.User
    private lateinit var userSource: UserSource
    private lateinit var postSource: PostSource

    override fun onStart() {
        view.attachActions()
        userPrefs = Injector.userPrefs()
        userSource = Injector.userSource()
        postSource = Injector.postSource()

        startObservingLikedPost()
    }

    private var likedPostListener: ListenerRegistration? = null
    override fun startObservingLikedPost() {
        likedPostListener = userSource.observeUserLikedPost(userPrefs.userId)
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
                        Timber.e(post.toString())
                        updatePostList.add(post)
                        view.updateLikedList(updatePostList)
                    }
                }
            }
    }

    override fun stopObservingLikedPost() {
        likedPostListener?.remove()
    }

    override fun deleteFavPost(postList: List<Post>) {
        if (postList.isEmpty()) {
            return
        }

        var completed = 0
        view.showLoading("Deleting...")
        postList.forEach {
            postSource.deleteLikedPost(it.postId, userPrefs.userId)
                .addOnSuccessListener {
                    completed++
                    if (completed <= postList.size) {
                        view.hideLoading()
                    }
                }.addOnFailureListener {
                    view.hideLoading()
                    view.onError("Woops..failed to delete")
                    Timber.e(it.localizedMessage)
                }
        }

    }


}