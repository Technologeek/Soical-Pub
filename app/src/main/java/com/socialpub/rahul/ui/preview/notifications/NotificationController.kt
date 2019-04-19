package com.socialpub.rahul.ui.preview.notifications

import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.model.Notif
import com.socialpub.rahul.data.remote.firebase.config.FirebaseApi
import com.socialpub.rahul.data.remote.firebase.sources.notifications.NotificationSource
import com.socialpub.rahul.data.remote.firebase.sources.post.PostSource
import com.socialpub.rahul.data.remote.firebase.sources.user.UserSource
import com.socialpub.rahul.di.Injector
import timber.log.Timber

class NotificationController(
    private val view: NotificationContract.View
) : NotificationContract.Controller {

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
        getAllNotifications(userPrefs.userId)
    }


    override fun getAllNotifications(userId: String) {

        view.showLoading()

        notifSource.observeUserNotifications(userPrefs.userId)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    view.onError("notifications not available...try again later")
                    Timber.e(firebaseFirestoreException)
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    val notifList = arrayListOf<Notif>()
                    querySnapshot.forEach { doc ->
                        val noti = doc.toObject(Notif::class.java)
                        notifList.add(noti)
                    }
                    view.showAllNotifications(notifList)
                    view.hideLoading()
                }else{
                    view.onError("No notifications...")
                    view.hideLoading()
                }
            }
    }


}