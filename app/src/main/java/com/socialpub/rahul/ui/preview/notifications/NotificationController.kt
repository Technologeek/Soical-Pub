package com.socialpub.rahul.ui.preview.notifications

import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.model.Notif
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
    }


    override fun getAllNotifications(userId: String) {

        notifSource.observeUserNotifications(userPrefs.userId)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                if (firebaseFirestoreException != null) {
                    view.onError("notifications not available...try again later")
                    Timber.e(firebaseFirestoreException)
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {

                    val notifList = arrayListOf<Notif>()
                    querySnapshot.forEach { queryDocument ->
                        val notification = queryDocument.toObject(Notif::class.java)
                        Timber.e(notification.toString())
                        notifList.add(notification)
                        view.showAllNotifications(notifList)
                    }
                }
            }

    }


}