package com.socialpub.rahul.ui.edit.followers

import com.google.firebase.firestore.ListenerRegistration
import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.data.remote.firebase.sources.post.PostSource
import com.socialpub.rahul.data.remote.firebase.sources.user.UserSource
import com.socialpub.rahul.di.Injector
import timber.log.Timber

class FollowerController(private val view: FollowerContract.View) : FollowerContract.Controller {

    private lateinit var userPrefs: AppPrefs.User
    private lateinit var userSource: UserSource
    private lateinit var postSource: PostSource

    override fun onStart() {
        view.attachActions()
        userPrefs = Injector.userPrefs()
        userSource = Injector.userSource()
        postSource = Injector.postSource()

        startObservingFollowers()
    }

    override fun getUser(userId: String) {

    }


    private var userProfileListener: ListenerRegistration? = null
    override fun startObservingFollowers() {
        userProfileListener = userSource.observeUserProfile(userPrefs.userId)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->

                if (firebaseFirestoreException != null) {
                    view.onError("Something went wrong...")
                    Timber.e(firebaseFirestoreException)
                    return@addSnapshotListener
                }

                if (documentSnapshot != null) {

                    view.showLoading()

                    val userProfile = documentSnapshot.toObject(User::class.java)
                    userProfile?.run {
                        followedBy.forEach { userID ->
                            getFollowedByUser(userID)
                        }
                        following.forEach { userID ->
                            getFollowingUser(userID)
                        }
                    }


                }
            }
    }

    private fun getFollowedByUser(userID: String) {
        val followedBy = arrayListOf<User>()
        userSource.getUser(userID).addOnSuccessListener {
            val user = it.toObject(User::class.java)
            user?.apply {
                followedBy.add(this)
            } ?: kotlin.run { Timber.e("user is null") }
            view.updateFollowerList(followedBy)
            view.hideLoading()
        }
    }

    private fun getFollowingUser(userID: String) {
        val following = arrayListOf<User>()
        userSource.getUser(userID).addOnSuccessListener {
            val user = it.toObject(User::class.java)
            user?.apply {
                following.add(this)
            } ?: kotlin.run { Timber.e("user is null") }
            view.updateFollowingList(following)
            view.hideLoading()
        }
    }

    override fun stopObservingFollowers() {
        userProfileListener?.remove()
    }

    override fun unfollowUser(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun followUser(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}