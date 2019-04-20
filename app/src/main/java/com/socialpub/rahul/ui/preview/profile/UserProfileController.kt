package com.socialpub.rahul.ui.preview.profile

import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.data.remote.firebase.sources.post.PostSource
import com.socialpub.rahul.data.remote.firebase.sources.user.UserSource
import com.socialpub.rahul.di.Injector
import timber.log.Timber

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


    var localUser: User? = null
    override fun getUserProfile(userId: String?) {
        if (userId.isNullOrBlank()) {
            view.dissmissDialog()
            view.onError("Unable to show preview...")
            return
        }

        val globalUserID = userId
        view.showLoading("loading..")
        userSource.getUser(userPrefs.userId).addOnSuccessListener { doc ->
            val user = doc.toObject(User::class.java)
            if (user != null) {
                localUser = user
                user.following.forEach { followerid ->
                    if (followerid == globalUserID) {
                        view.isFollowing(true)
                    }
                }
                getUserData(globalUserID)
            } else {
                error(Exception("user is null"))
            }

        }.addOnFailureListener {
            error(it)
        }

    }

    var previewUser: User? = null
    private fun getUserData(globalUserID: String) {

        userSource.getUser(globalUserID).addOnSuccessListener { doc ->
            val globalUser = doc.toObject(User::class.java)
            if (globalUser != null) {
                previewUser = globalUser
                view.updateUserPreview(globalUser)
                getPreviewUserPost(globalUser)
            } else {
                error(Exception("error in null"))
            }

        }.addOnFailureListener {
            error(it)
        }
    }


    override fun followGlobalUser(userId: String?) {
        userId?.let { previewUserId ->

            view.showLoading("following...")

            //update local user
            val updatedFollowers = localUser!!.following.toMutableList()
            updatedFollowers.add(previewUserId)
            val updatedUser = localUser!!.copy(
                following = updatedFollowers
            )
            localUser = updatedUser
            userSource.createUser(updatedUser).addOnSuccessListener {

                //update global user
                val updatedGlobalFollowedby = previewUser!!.followedBy.toMutableList()
                updatedGlobalFollowedby.add(userPrefs.userId)
                val updatedGlobalUser = previewUser!!.copy(
                    followedBy = updatedGlobalFollowedby
                )
                userSource.createUser(updatedGlobalUser).addOnSuccessListener {
                    view.hideLoading()
                    //refresh
                    view.attachActions()
                }.addOnFailureListener {
                    view.hideLoading()
                    Timber.e(it)
                }

            }.addOnFailureListener {
                view.hideLoading()
                Timber.e(it)
            }


        }

    }

    override fun updatefollowing(following: Boolean, userId: String?) {
        if (following) {
            unfollowGlobalUser(userId)
        } else {
            followGlobalUser(userId)
        }
    }

    override fun unfollowGlobalUser(userId: String?) {

        userId?.let { previewUserId ->

            view.showLoading("Un-following...")

            //update local user
            val updatedFollowers = localUser!!.following.toMutableList()
            updatedFollowers.remove(previewUserId)
            val updatedUser = localUser!!.copy(
                following = updatedFollowers
            )
            localUser = updatedUser
            userSource.createUser(updatedUser).addOnSuccessListener {

                //update global user
                val updatedGlobalFollowedby = previewUser!!.followedBy.toMutableList()
                updatedGlobalFollowedby.remove(userPrefs.userId)
                val updatedGlobalUser = previewUser!!.copy(
                    followedBy = updatedGlobalFollowedby
                )
                userSource.createUser(updatedGlobalUser).addOnSuccessListener {
                    view.hideLoading()
                    //refresh
                    view.attachActions()
                }.addOnFailureListener {
                    view.hideLoading()
                    Timber.e(it)
                }

            }.addOnFailureListener {
                view.hideLoading()
                Timber.e(it)
            }
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