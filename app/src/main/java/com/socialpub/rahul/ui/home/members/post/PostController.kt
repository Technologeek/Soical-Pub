package com.socialpub.rahul.ui.home.members.post

import android.content.Intent
import com.cloudinary.android.callback.ErrorInfo
import com.esafirm.imagepicker.features.ImagePicker
import com.google.firebase.firestore.ListenerRegistration
import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.remote.firebase.sources.post.PostSource
import com.socialpub.rahul.di.Injector
import com.socialpub.rahul.utils.helper.CloudinaryUploadHelper
import timber.log.Timber


class PostController(
    private val view: PostContract.View
) : PostContract.Controller {

    private lateinit var userPrefs: AppPrefs.User
    private lateinit var postSource: PostSource

    override fun onStart() {
        view.attachActions()
        userPrefs = Injector.userPrefs()
        postSource = Injector.postSource()
    }

    private var globalfeedsListener: ListenerRegistration? = null

    override fun startObservingGlobalFeeds() {
        globalfeedsListener = postSource.observeGlobalFeeds()
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

    override fun handleImagePickerRequest(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PostContract.Controller.Const.IMAGE_PICKER_REQUEST) {
            try {
                val image = ImagePicker.getFirstImageOrNull(data)
                val path = image.path

                view.onError("todo : showDilaog which show inputs for other post details")
                view.onImagePickerSuccess(path)
            } catch (e: Exception) {
                view.onError(e.localizedMessage)
            }
        }
    }


    override fun uploadPost(post: Post) {
        view.onError("todo :add feed dialog before upload image to cloudinary")
        uploadImageClouinary(post)
    }

    //uploaded image to cloundiary to get a public key which will become postId and imageUrl
    private fun uploadImageClouinary(post: Post) =
        Injector.postSource().uploadImageCloundary(post.imagePath)
            .callback(object : CloudinaryUploadHelper() {
                override fun onStart(requestId: String?) {
                    super.onStart(requestId)
                    view.showLoading("Uploading...")
                    //test
                }


                override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                    super.onSuccess(requestId, resultData)
                    view.hideLoading()
                    val publicKey = requireNotNull(resultData?.get("public_id") as String?)
                    val imageUrl = requireNotNull(resultData?.get("secure_url") as String?)
                    val newPost = post.copy(
                        postId = publicKey,
                        userAvatar = userPrefs.avatarUrl,
                        uid = userPrefs.userId,
                        username = userPrefs.displayName,
                        imageUrl = imageUrl
                    )

                    uploadUserPost(newPost)
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    super.onError(requestId, error)
                    view.hideLoading()
                    Timber.e(error?.description)
                    view.onError("Something went wrong while uploading post..")
                }

            }).dispatch()

    //push post to userId
    private fun uploadUserPost(post: Post) {
        postSource.createPost(post)
            .addOnSuccessListener {
            uploadGlobalPost(post)
        }.addOnFailureListener {
            view.hideLoading()
            view.onError(it.localizedMessage)
        }
    }

    //push post to global feeds
    private fun uploadGlobalPost(post: Post) {
        postSource.addPostGlobally(post)
            .addOnCompleteListener {
            view.hideLoading()
        }.addOnFailureListener {
            view.hideLoading()
            view.onError(it.localizedMessage)
        }
    }

}