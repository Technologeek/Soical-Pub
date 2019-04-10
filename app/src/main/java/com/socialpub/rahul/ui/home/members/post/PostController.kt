package com.socialpub.rahul.ui.home.members.post

import android.content.Intent
import com.cloudinary.android.callback.ErrorInfo
import com.esafirm.imagepicker.features.ImagePicker
import com.google.firebase.firestore.ListenerRegistration
import com.google.gson.Gson
import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.model.GlobalPost
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.model.UserPost
import com.socialpub.rahul.data.remote.firebase.sources.post.PostSource
import com.socialpub.rahul.di.Injector
import com.socialpub.rahul.utils.helper.CloudinaryUploadHelper
import timber.log.Timber


class PostController(
    private val view: PostContract.View
) : PostContract.Controller {

    lateinit var userPrefs: AppPrefs.User
    lateinit var postSource: PostSource

    override fun onStart() {
        view.attachActions()
        userPrefs = Injector.userPrefs()
        postSource = Injector.postSource()
    }

    private var globalfeedsListener: ListenerRegistration? = null

    override fun startObservingGlobalFeeds() {
        globalfeedsListener =
            postSource.observeGlobalFeeds().addSnapshotListener { documentSnapshot, firebaseFirestoreException ->

                if (firebaseFirestoreException != null) {
                    view.onError("Post not available...try again later")
                    Timber.e(firebaseFirestoreException)
                    return@addSnapshotListener
                }

                if (documentSnapshot?.exists() == true) {

                    val globalPost = Gson().toJson(documentSnapshot.get("globalPost"))

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
                        userProfilePic = userPrefs.avatarUrl,
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
                    view.onError(error?.description.toString())
                }

            }).dispatch()

    //push post to userId
    private fun uploadUserPost(post: Post) {
        postSource.createPost(UserPost(
            userPost = hashMapOf<String, Post>().apply {
                put(post.postId, post)
            }
        ), post.uid).addOnSuccessListener {
            uploadGlobalPost(post)
        }.addOnFailureListener {
            view.hideLoading()
            view.onError(it.localizedMessage)
        }
    }

    //push post to global feeds
    private fun uploadGlobalPost(post: Post) {
        postSource.addPostGlobally(GlobalPost(
            globalPost = hashMapOf<String, Post>().also {
                it.put(post.postId, post)
            }
        )).addOnCompleteListener {
            view.hideLoading()
        }.addOnFailureListener {
            view.hideLoading()
            view.onError(it.localizedMessage)
        }
    }

}