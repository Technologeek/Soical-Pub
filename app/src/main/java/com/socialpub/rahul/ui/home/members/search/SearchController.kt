package com.socialpub.rahul.ui.home.members.search

import com.google.firebase.firestore.ListenerRegistration
import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.remote.firebase.sources.user.UserSource
import com.socialpub.rahul.di.Injector
import timber.log.Timber

class SearchController(private val view: SearchContract.View) : SearchContract.Controller {


    private lateinit var userPrefs: AppPrefs.User
    private lateinit var userSource: UserSource

    override fun onStart() {
        view.attachActions()
        userPrefs = Injector.userPrefs()
        userSource = Injector.userSource()
    }


    private var userProfileListener: ListenerRegistration? = null

    override fun startObservingGlobalFeeds() {
//        userProfileListener = userSource.getUser()
//            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
//
//                if (firebaseFirestoreException != null) {
//                    view.onError("Post not available...try again later")
//                    Timber.e(firebaseFirestoreException)
//                    return@addSnapshotListener
//                }
//
//                if (querySnapshot != null) {
//
//                    val updatePostList = arrayListOf<Post>()
//                    querySnapshot.forEach { queryDocument ->
//                        val post = queryDocument.toObject(Post::class.java)
//                        updatePostList.add(post)
//                    }
//
//                    view.updatePost(updatePostList)
//                }
//            }
    }

    override fun stopObservingGlobalFeeds() {

    }


}