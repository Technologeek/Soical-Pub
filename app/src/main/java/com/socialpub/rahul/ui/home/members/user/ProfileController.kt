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
                name = userPrefs.displayName,
                username = userPrefs.displayName,
                email = userPrefs.email,
                avatar = userPrefs.avatarUrl,
                uid = userPrefs.userId
            )
        )
        getUserProfile(userPrefs.userId)
        startObservingPublishedPost()
        startObservingLikedPost()
    }

    override fun getUserProfile(uId: String) {
        userSource.getUser(uId).addOnSuccessListener {
            val profile = it.toObject(User::class.java)
            requireNotNull(profile)

            with(userPrefs) {
                profile.avatar?.apply { userPrefs.avatarUrl = this }
                followers = profile.followers.size.toLong()
                following = profile.following.size.toLong()
                displayName = profile.name
            }

            view.updateProfileInfo(profile)
        }
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
                        view.updatePublishList(updatePostList)
                    }
                }
            }

    }

    override fun stopObservingPublishedPost() {
        publishPostListener?.remove()
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


}