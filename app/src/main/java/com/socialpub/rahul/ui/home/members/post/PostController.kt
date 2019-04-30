package com.socialpub.rahul.ui.home.members.post

import com.google.firebase.firestore.ListenerRegistration
import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.model.Like
import com.socialpub.rahul.data.model.Notif
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.remote.firebase.sources.notifications.NotificationSource
import com.socialpub.rahul.data.remote.firebase.sources.post.PostSource
import com.socialpub.rahul.di.Injector
import com.socialpub.rahul.utils.AppConst
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
            if (post.uid != userPrefs.userId) {
                view.showLoading("Liking...")
                postSource.getGlobalPost(post.postId)
                    .addOnSuccessListener {

                        val globalPost = it.toObject(Post::class.java)

                        globalPost?.let {

                            likedBy.forEach { like ->
                                if (like.uid == userPrefs.userId) {
                                    view.onError("You have already liked the post")
                                    view.hideLoading()
                                    return@addOnSuccessListener
                                }
                            }

                            val likers = it.likedBy.toMutableList()

                            likers.add(
                                Like(
                                    uid = userPrefs.userId,
                                    username = userPrefs.displayName,
                                    userAvatar = userPrefs.avatarUrl,
                                    userEmail = userPrefs.email
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
            } else {
                view.onError("You can't like your own post!")
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
        postSource.createPost(globalPost)
            .addOnSuccessListener {
                view.hideLoading()
                notifyGlobalUser(globalPost, AppConst.NOTIF_ACTION_LIKE)
            }.addOnFailureListener {
                firebaseError(it)
            }

    }


    //============= ADD FAV =================//

    override fun addFav(post: Post?) {
        view.showLoading()
        post?.run {
            postSource.saveFavPost(post, userPrefs.userId)
                .addOnSuccessListener {
                    view.hideLoading()
                    view.onError("Post saved!")
                }.addOnFailureListener {
                    view.hideLoading()
                    view.onError("Oh Snap..couldn't save")
                    Timber.e(it.localizedMessage)
                }
        }
    }

    //============= Report =================//

    override fun reportPost(post: Post?) {
        view.showLoading()
        post?.run {
            postSource.getUserPost(post.postId, post.uid)
                .addOnSuccessListener {
                    val userPost = it.toObject(Post::class.java)
                    if (userPost != null) {
                        val updatedPost = userPost.copy(
                            reported = userPost.reported + 1
                        )
                        postSource.createPost(updatedPost)
                            .addOnSuccessListener {
                                notifyGlobalUser(updatedPost, AppConst.NOTIF_ACTION_REPORT)
                                view.hideLoading()
                            }.addOnFailureListener {
                                firebaseError(it)
                            }
                    } else {
                        firebaseError(NullPointerException("Empty user post"))
                    }
                }.addOnFailureListener {
                    firebaseError(it)
                }

        }
    }

    private fun firebaseError(it: Exception) {
        view.hideLoading()
        view.onError("Something went wrong...")
        Timber.e(it)
    }

    //============= Notification =================//
    private fun notifyGlobalUser(globalPost: Post, action: Int) {

        if (userPrefs.userId != globalPost.uid ){

            val notif = Notif(
                actionOnPostId = globalPost.postId,
                actionByuid = userPrefs.userId,
                actionByUsername = userPrefs.displayName,
                actionByUserAvatar = userPrefs.avatarUrl,
                action = action
            )
            notificationSource.notifyUser(globalPost.uid, notif).addOnSuccessListener {
                Timber.e("notified user success")
            }.addOnFailureListener {
                Timber.e("notified user failed : $it")
            }
        }


    }


}