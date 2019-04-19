package com.socialpub.rahul.ui.settings

import com.google.firebase.firestore.ListenerRegistration
import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.data.remote.firebase.sources.post.PostSource
import com.socialpub.rahul.data.remote.firebase.sources.user.UserSource
import com.socialpub.rahul.di.Injector
import timber.log.Timber

class SettingsController(private val view: SettingsContract.View) : SettingsContract.Controller {

    private lateinit var userPrefs: AppPrefs.User
    private lateinit var userSource: UserSource
    private lateinit var postSource: PostSource

    override fun onStart() {
        userPrefs = Injector.userPrefs()
        userSource = Injector.userSource()
        postSource = Injector.postSource()
        view.attachActions()
        startObservingSettings()
    }

    private var userProfileListener: ListenerRegistration? = null
    private fun startObservingSettings() {

        view.showLoading()

        userProfileListener = userSource.observeUserProfile(userPrefs.userId)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->

                if (firebaseFirestoreException != null) {
                    view.onError("Something went wrong...")
                    Timber.e(firebaseFirestoreException)
                    return@addSnapshotListener
                }

                if (documentSnapshot != null) {
                    val userProfile = documentSnapshot.toObject(User::class.java)
                    userProfile?.run {
                        view.hideLoading()
                        view.updateUserSettings(isVisibletoLocationSearch, isVisibletoEmailSearch)
                    }
                }
            }
    }

    override fun updateUserSettings(isLocationVisible: Boolean, isEmailVisible: Boolean) {
        view.showLoading()

        userSource.getUser(userPrefs.userId)
            .addOnSuccessListener {
                val userProfile = it.toObject(User::class.java)
                if (userProfile != null) {
                    val updatedProfile = userProfile.copy(
                        isVisibletoEmailSearch = isEmailVisible,
                        isVisibletoLocationSearch = isLocationVisible
                    )
                    userSource.createUser(updatedProfile).addOnSuccessListener {
                        view.hideLoading()
                        view.onError("Settings updated...")
                    }.addOnFailureListener {
                        view.hideLoading()
                        view.onError("Somthing went wrong...")
                        Timber.e(it)
                    }
                }
            }
    }


    override fun stopObservingSettings() {
        userProfileListener?.remove()
    }

}