package com.socialpub.rahul.ui.preview.post

import com.google.firebase.firestore.ListenerRegistration
import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.model.Comment
import com.socialpub.rahul.data.model.Notif
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.remote.firebase.sources.notifications.NotificationSource
import com.socialpub.rahul.data.remote.firebase.sources.post.PostSource
import com.socialpub.rahul.data.remote.firebase.sources.user.UserSource
import com.socialpub.rahul.di.Injector
import com.socialpub.rahul.utils.AppConst
import timber.log.Timber

class PreviewPostController(
    private val view: PreviewPostContract.View
) : PreviewPostContract.Controller {

    private lateinit var userPrefs: AppPrefs.User
    private lateinit var userSource: UserSource
    private lateinit var postSource: PostSource
    private lateinit var notifSource: NotificationSource

    override fun onStart() {
        userPrefs = Injector.userPrefs()
        userSource = Injector.userSource()
        postSource = Injector.postSource()
        notifSource = Injector.notificationSource()

        view.attachActions()
    }


    var previewPost: Post? = null
    override fun getUserPost(postId: String?) {
        if (postId.isNullOrBlank()) {
            view.dissmissDialog()
            view.onError("Unable to show preview...")
            return
        }

        view.showLoading("loading..")

        postSource.getUserPost(postId, userPrefs.userId)
            .addOnSuccessListener { doc ->
                val post = doc.toObject(Post::class.java)

                view.hideLoading()
                if (post != null) {
                    previewPost = post
                    startObServingComments(post)
                    view.updatePostPreview(post)
                } else {
                    view.dissmissDialog()
                    view.onError("Unable to show preview...")
                }
            }
    }


    override fun getGlobalUserPost(postId: String?, globalUserId: String?) {
        if (!globalUserId.isNullOrBlank() && !postId.isNullOrBlank()) {

            view.showLoading("Loading...")
            postSource.getUserPost(postId, globalUserId)
                .addOnSuccessListener { doc ->
                    val post = doc.toObject(Post::class.java)

                    view.hideLoading()
                    if (post != null) {
                        previewPost = post
                        startObServingComments(post)
                        view.updatePostPreview(post)
                    } else {
                        view.dissmissDialog()
                        view.onError("Unable to show preview...")
                    }
                }

        } else {
            view.dissmissDialog()
            view.onError("Unable to show preview...")
        }

    }


    override fun deletePost() {

        previewPost?.run {
            view.showLoading("Deleting...")
            postSource.deleteUserPost(postId, userPrefs.userId)
                .addOnSuccessListener {
                    deleteGlobalPost(postId)
                }.addOnFailureListener {
                    view.hideLoading()
                    view.onError("Post deleting failed...")
                    Timber.e(it.localizedMessage)
                }
        }

    }

    private fun deleteGlobalPost(postId: String) {
        postSource.deleteGlobalPost(postId).addOnSuccessListener {
            view.hideLoading()
            view.dissmissDialog()
            view.onError("Post deleted...")
        }.addOnFailureListener {
            view.hideLoading()
            Timber.e(it.localizedMessage)
        }
    }

    private var postUpdateListener: ListenerRegistration? = null
    private fun startObServingComments(post: Post) {
        postUpdateListener =
            postSource.observePost(post).addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    view.hideLoading()
                    view.onError("Something went wrong...")
                    view.dissmissDialog()
                    Timber.e(firebaseFirestoreException.localizedMessage)
                    return@addSnapshotListener
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    val newPost = documentSnapshot.toObject(Post::class.java)
                    if (newPost != null) {
                        previewPost = newPost
                        view.updatePostPreview(newPost)
                    }
                }
            }
    }

    override fun stopObservingComments() {
        postUpdateListener?.remove()
    }

    override fun submitComment(text: String) {
        if (previewPost != null) {
            view.showLoading("Submitting...")

            val newComment = Comment(
                uid = userPrefs.userId,
                userAvatar = userPrefs.avatarUrl,
                text = text
            )

            val newCommentList: ArrayList<Comment> = ArrayList(previewPost!!.comments)
            newCommentList.add(newComment)

            val updatedPost = previewPost!!.copy(
                comments = newCommentList,
                commentCount = newCommentList.size.toLong()
            )

            Timber.e("Size : ${newCommentList.size}")

            postSource.commentOnUserPost(updatedPost).addOnSuccessListener {

                updateGlobalCommnets(updatedPost)

            }.addOnFailureListener {
                view.hideLoading()
                view.onError("Could'nt Comment.Something went wrong...")
                view.dissmissDialog()
            }

        } else {
            view.hideLoading()
            view.onError("Something went wrong...")
            view.dissmissDialog()
        }

    }


    override fun submitGlobalComment(text: String) {

        if (previewPost != null) {
            view.showLoading("Submitting...")

            val newComment = Comment(
                uid = userPrefs.userId,
                userAvatar = userPrefs.avatarUrl,
                text = text
            )

            val newCommentList: ArrayList<Comment> = ArrayList(previewPost!!.comments)
            newCommentList.add(newComment)

            val updatedPost = previewPost!!.copy(
                comments = newCommentList,
                commentCount = newCommentList.size.toLong()
            )

            Timber.e("Size : ${newCommentList.size}")

            postSource.commentOnUserPost(updatedPost).addOnSuccessListener {

                updateGlobalCommnets(updatedPost)

            }.addOnFailureListener {
                view.hideLoading()
                view.onError("Could'nt Comment.Something went wrong...")
                view.dissmissDialog()
            }

        } else {
            view.hideLoading()
            view.onError("Something went wrong...")
            view.dissmissDialog()
        }


    }

    private fun updateGlobalCommnets(updatedPost: Post) {
        postSource.commentOnGlobalPost(updatedPost).addOnSuccessListener {
            notifyGlobalUser(updatedPost)
        }.addOnFailureListener {
            view.hideLoading()
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
            action = AppConst.NOTIF_ACTION_COMMENT
        )
        notifSource.notifyUser(globalPost.uid, notif).addOnSuccessListener {
            Timber.e("notified user success")
            view.hideLoading()
        }.addOnFailureListener {
            Timber.e("notified user failed : $it")
        }
    }


}