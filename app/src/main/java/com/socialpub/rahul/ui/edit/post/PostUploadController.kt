package com.socialpub.rahul.ui.edit.post

import android.content.Intent
import com.cloudinary.android.callback.ErrorInfo
import com.esafirm.imagepicker.features.ImagePicker
import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.remote.firebase.sources.post.PostSource
import com.socialpub.rahul.data.remote.firebase.sources.user.UserSource
import com.socialpub.rahul.di.Injector
import com.socialpub.rahul.ui.home.members.post.PostContract
import com.socialpub.rahul.utils.helper.CloudinaryUploadHelper
import timber.log.Timber

class PostUploadController(
    private val view: PostUploadContract.View
) : PostUploadContract.Controller {

    private lateinit var userPrefs: AppPrefs.User
    private lateinit var userSource: UserSource
    private lateinit var postSource: PostSource

    override fun onStart() {
        userPrefs = Injector.userPrefs()
        userSource = Injector.userSource()
        postSource = Injector.postSource()
        view.attachActions()
    }


    var postContentImagePath: String? = null
    override fun handleImagePickerRequest(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PostContract.Controller.Const.IMAGE_PICKER_REQUEST) {
            try {
                val image = ImagePicker.getFirstImageOrNull(data)
                val path = image.path
                view.showSelectedImage(path)
                postContentImagePath = path
            } catch (e: Exception) {
                view.onError(e.localizedMessage)
            }
        }
    }

    //============= Create Post =================//

    override fun uploadPost(post: Post) {
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
                        username = userPrefs.displayName,
                        userAvatar = userPrefs.avatarUrl,
                        uid = userPrefs.userId,
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
                view.onPostSubmittedSuccess()
            }.addOnFailureListener {
                view.hideLoading()
                view.onError(it.localizedMessage)
            }
    }


}