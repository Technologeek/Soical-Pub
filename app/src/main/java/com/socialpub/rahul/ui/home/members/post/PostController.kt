package com.socialpub.rahul.ui.home.members.post

import android.content.Intent
import com.cloudinary.android.callback.ErrorInfo
import com.esafirm.imagepicker.features.ImagePicker
import com.google.firebase.firestore.ListenerRegistration
import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.model.Like
import com.socialpub.rahul.data.model.Notif
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.remote.firebase.sources.notifications.NotificationSource
import com.socialpub.rahul.data.remote.firebase.sources.post.PostSource
import com.socialpub.rahul.di.Injector
import com.socialpub.rahul.utils.AppConst
import com.socialpub.rahul.utils.helper.CloudinaryUploadHelper
import timber.log.Timber


class PostController(
    private val view: PostContract.View
) : PostContract.Controller {

    private lateinit var userPrefs: AppPrefs.User
    private lateinit var postSource: PostSource
    private lateinit var notificationSource: NotificationSource

    override fun onStart() {
        view.attachActions()
        userPrefs = Injector.userPrefs()
        postSource = Injector.postSource()
        notificationSource = Injector.notificationSource()
    }

    private var globalfeedsListener: ListenerRegistration? = null

    override fun startObservingGlobalFeeds() {

        val filterObservable = when (userPrefs.filterType) {
            AppConst.POST_FILTER_LIKES -> postSource.observeGlobalFeedsByLikes()
            AppConst.POST_FILTER_COMMENTS -> postSource.observeGlobalFeedsByComments()
            else -> postSource.observeGlobalFeedsByTimeStamp()
        }

        globalfeedsListener = filterObservable
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

                    view.updatePost(updatePostList)
                }
            }
    }


    override fun stopObservingGlobalFeeds() {
        globalfeedsListener?.remove()
    }


    //============= Filter Post =================//

    override fun filterLatest() {
        stopObservingGlobalFeeds()
        userPrefs.filterType = AppConst.POST_FILTER_LATEST
        startObservingGlobalFeeds()
        view.listScrollToTop()
    }

    override fun filterLiked() {
        stopObservingGlobalFeeds()
        userPrefs.filterType = AppConst.POST_FILTER_LIKES
        startObservingGlobalFeeds()
        view.listScrollToTop()
    }

    override fun filterCommented() {
        stopObservingGlobalFeeds()
        userPrefs.filterType = AppConst.POST_FILTER_COMMENTS
        startObservingGlobalFeeds()
        view.listScrollToTop()
    }


    //============= LIKE =================//

    override fun addLike(post: Post?) {

        post?.run {
            view.showLoading("Liking...")

            postSource.getGlobalPost(post.postId)
                .addOnSuccessListener {

                    val globalPost = it.toObject(Post::class.java)

                    globalPost?.let {
                        val likers = it.likedBy.toMutableList()
                        likers.add(
                            Like(
                                uid = userPrefs.userId,
                                username = userPrefs.displayName,
                                userAvatar = userPrefs.avatarUrl
                            )
                        )
                        val newPost = globalPost.copy(
                            likedBy = likers,
                            likeCount = likers.size.toLong()
                        )
                        updateGlobalLike(newPost)
                    }

                }.addOnFailureListener {
                    view.hideLoading()
                    view.onError("Unable to like post")
                    Timber.e(it.localizedMessage)
                }
        }
    }

    private fun updateGlobalLike(globalPost: Post) {
        postSource.likeGlobalPost(globalPost)
            .addOnSuccessListener {
                updateUserLike(globalPost)
            }.addOnFailureListener {
                view.hideLoading()
                view.onError("Oh Snap..couldn't like")
                Timber.e(it.localizedMessage)
            }
    }

    private fun updateUserLike(globalPost: Post) {
        postSource.likeUserPost(globalPost, userPrefs.userId)
            .addOnSuccessListener {
                view.hideLoading()
                view.onError("Post liked!")
                notifyGlobalUser(globalPost)
            }.addOnFailureListener {
                view.hideLoading()
                view.onError("Oh Snap..couldn't like")
                Timber.e(it.localizedMessage)
            }
    }


    //============= Notification =================//
    private fun notifyGlobalUser(globalPost: Post) {
        val notif = Notif(
            actionOnPostId = globalPost.postId,
            actionByuid = userPrefs.userId,
            actionByUsername = userPrefs.displayName,
            actionByUserAvatar = userPrefs.avatarUrl,
            action = AppConst.NOTIF_ACTION_LIKE
        )
        notificationSource.notifyUser(globalPost.uid, notif).addOnSuccessListener {
            Timber.e("notified user success")
        }.addOnFailureListener {
            Timber.e("notified user failed : $it")
        }
    }


}