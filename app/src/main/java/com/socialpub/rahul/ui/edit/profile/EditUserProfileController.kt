package com.socialpub.rahul.ui.edit.profile

import android.content.Intent
import com.cloudinary.android.callback.ErrorInfo
import com.esafirm.imagepicker.features.ImagePicker
import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.data.remote.firebase.sources.post.PostSource
import com.socialpub.rahul.data.remote.firebase.sources.user.UserSource
import com.socialpub.rahul.di.Injector
import com.socialpub.rahul.ui.home.members.post.PostContract
import com.socialpub.rahul.utils.helper.CloudinaryUploadHelper
import timber.log.Timber

class EditUserProfileController(
    private val view: EditUserProfileContract.View
) : EditUserProfileContract.Controller {

    private lateinit var userPrefs: AppPrefs.User
    private lateinit var userSource: UserSource
    private lateinit var postSource: PostSource

    override fun onStart() {
        userPrefs = Injector.userPrefs()
        userSource = Injector.userSource()
        postSource = Injector.postSource()
        view.attachActions(userPrefs.avatarUrl)
    }

    override fun updateUserProfile(userName: String) {
        if (!userName.isNullOrBlank() || !newProfileAvatarPath.isNullOrBlank()) {
            uploadCloudnary(userName, newProfileAvatarPath)
        } else {
            view.onError("Please enter new details...")
        }
    }

    private fun uploadCloudnary(username: String?, imagePath: String?) {

        if (imagePath != null) {
            postSource.uploadImageCloundary(imagePath)
                .callback(object : CloudinaryUploadHelper() {

                    override fun onStart(requestId: String?) {
                        super.onStart(requestId)
                        view.showLoading("Updating...")
                    }

                    override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                        super.onSuccess(requestId, resultData)
                        val imageUrl = requireNotNull(resultData?.get("secure_url") as String?)
                        updateRemoteUserProfile(username, imageUrl)
                    }

                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        super.onError(requestId, error)
                        view.hideLoading()
                        Timber.e(error?.description)
                        view.onError("Something went wrong while uploading post..")
                    }

                }).dispatch()

        } else {
            updateRemoteUserProfile(username, null)
        }

    }

    private fun updateRemoteUserProfile(username: String?, imageUrl: String?) {
        userSource.createUser(
            User(
                username = username ?: userPrefs.displayName,
                avatar = imageUrl ?: userPrefs.avatarUrl,
                email = userPrefs.email,
                uid = userPrefs.userId
            )
        ).addOnSuccessListener {
            view.hideLoading()
            view.dissmissDialog()
        }.addOnFailureListener {
            view.hideLoading()
            view.onError("Something went wrong...")
            Timber.e(it.localizedMessage)
        }
    }


    var newProfileAvatarPath: String? = null
    override fun handleImagePickerRequest(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PostContract.Controller.Const.IMAGE_PICKER_REQUEST) {
            try {
                val image = ImagePicker.getFirstImageOrNull(data)
                val path = image.path
                view.showSelectedImage(path)
                newProfileAvatarPath = path
            } catch (e: Exception) {
                view.onError(e.localizedMessage)
            }
        }
    }

}