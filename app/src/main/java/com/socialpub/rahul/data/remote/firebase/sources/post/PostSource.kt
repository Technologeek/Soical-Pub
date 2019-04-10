package com.socialpub.rahul.data.remote.firebase.sources.post

import com.google.firebase.firestore.SetOptions
import com.socialpub.rahul.data.model.GlobalPost
import com.socialpub.rahul.data.model.UserPost
import com.socialpub.rahul.data.remote.cloudinary.config.CloudinaryManager
import com.socialpub.rahul.data.remote.firebase.config.FirebaseApi
import com.socialpub.rahul.data.remote.firebase.config.FirebaseManager

class PostSource private constructor(
    private val firebaseManager: FirebaseManager,
    private val cloudinaryManager: CloudinaryManager
) {

    companion object {

        @Volatile
        private var instance: PostSource? = null

        fun getInstance(firebaseManager: FirebaseManager, cloudinaryManager: CloudinaryManager) =
            instance ?: synchronized(this) {
                instance ?: PostSource(firebaseManager, cloudinaryManager).also { instance = it }
            }

    }

    private val firebaseStore by lazy { firebaseManager.firestore }


    fun observeGlobalFeeds() = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_POST)
        .document(FirebaseApi.FireStore.Documents.ALL_POST)


    fun uploadImageCloundary(filePath: String) = cloudinaryManager.uploadImage(filePath)

    fun createPost(userPost: UserPost, uid: String) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_USERS)
        .document(uid)
        .set(userPost, SetOptions.merge())

    fun addPostGlobally(globalPost: GlobalPost) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_POST)
        .document(FirebaseApi.FireStore.Documents.ALL_POST)
        .set(globalPost, SetOptions.merge())

    fun getAllPost(uId: String) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_USERS)
        .document(uId)
        .get()

}