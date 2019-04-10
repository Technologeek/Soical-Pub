package com.socialpub.rahul.data.remote.firebase.sources.post

import com.google.firebase.firestore.SetOptions
import com.socialpub.rahul.data.model.Post
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

    fun uploadImageCloundary(filePath: String) = cloudinaryManager.uploadImage(filePath)

    fun createPost(userPost: Post) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_USERS)
        .document(userPost.uid)
        .set(userPost, SetOptions.merge())

    fun addPostGlobally(globalPost: Post) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_POST)
        .document(globalPost.postId)
        .set(globalPost, SetOptions.merge())

    fun getPost(postId:String) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_POST)
        .document(postId)
        .get()

}