package com.socialpub.rahul.data.remote.firebase.sources.notifications

import com.google.firebase.firestore.Query
import com.socialpub.rahul.data.remote.firebase.config.FirebaseApi
import com.socialpub.rahul.data.remote.firebase.config.FirebaseManager

class NotificationSource private constructor(private val firebaseManager: FirebaseManager) {

    companion object {

        @Volatile
        private var instance: NotificationSource? = null

        fun getInstance(firebaseManager: FirebaseManager) = instance ?: synchronized(this) {
            instance ?: NotificationSource(firebaseManager).also { instance = it }
        }

    }

    private val firebaseStore by lazy { firebaseManager.firestore }


    fun observeUserNotifications(uid: String) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_USERS)
        .document(uid)
        .collection(FirebaseApi.FireStore.Collection.NOTIFICATIONS)
        .orderBy("timestamp", Query.Direction.DESCENDING)

}