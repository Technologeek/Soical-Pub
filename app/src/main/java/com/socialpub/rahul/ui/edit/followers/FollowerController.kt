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
                    followedByList.clear()
                    followinglist.clear()
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

    private var followedByList = arrayListOf<User>()
    private fun getFollowedByUser(userID: String) {

        userSource.getUser(userID).addOnSuccessListener {
            val user = it.toObject(User::class.java)
            user?.apply {
                followedByList.add(this)
            } ?: kotlin.run { Timber.e("user is null") }
            view.updateFollowerList(followedByList)
        }
    }

    private var followinglist = arrayListOf<User>()
    private fun getFollowingUser(userID: String) {
        userSource.getUser(userID).addOnSuccessListener {
            val user = it.toObject(User::class.java)
            user?.apply {
                followinglist.add(this)
            } ?: kotlin.run { Timber.e("user is null") }
            view.updateFollowingList(followinglist)
        }
    }

    override fun stopObservingFollowers() {
        userProfileListener?.remove()
    }

}