package com.socialpub.rahul.data.remote.firebase.sources.user

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.data.remote.firebase.config.FirebaseApi
import com.socialpub.rahul.data.remote.firebase.config.FirebaseManager

class UserSource private constructor(private val firebaseManager: FirebaseManager) {

    companion object {

        @Volatile
        private var instance: UserSource? = null

        fun getInstance(firebaseManager: FirebaseManager) = instance ?: synchronized(this) {
            instance ?: UserSource(firebaseManager).also { instance = it }
        }

    }

    private val firebaseStore by lazy { firebaseManager.firestore }

    fun createUser(user: User): Task<Void> = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_USERS)
        .document(user.uid)
        .set(user, SetOptions.merge())

    fun getUser(uId: String) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_USERS)
        .document(uId)
        .get()

    fun getAllProfiles() = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_USERS)
        .orderBy("username")
        .get()

    fun observeUserPublishedPost(uid: String) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_USERS)
        .document(uid)
        .collection(FirebaseApi.FireStore.Collection.PUBLISHED_POSTS)
        .orderBy("timestamp", Query.Direction.DESCENDING)


    fun observeUserLikedPost(uid: String) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_USERS)
        .document(uid)
        .collection(FirebaseApi.FireStore.Collection.LIKED_POSTS)
        .orderBy("timestamp", Query.Direction.DESCENDING)

    fun observeUserProfile(uid: String) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_USERS)
        .document(uid)



}