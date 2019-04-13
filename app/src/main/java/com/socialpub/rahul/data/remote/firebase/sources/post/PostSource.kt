package com.socialpub.rahul.data.remote.firebase.sources.post

import com.google.firebase.firestore.Query
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

    fun observeGlobalFeedsByTimeStamp() = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_POST)
        .orderBy("timestamp", Query.Direction.DESCENDING)

    fun getAllPost() = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_POST)
        .get()

    fun observeGlobalFeedsByLikes() = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_POST)
        .orderBy("likeCount", Query.Direction.DESCENDING)

    fun observeGlobalFeedsByComments() = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_POST)
        .orderBy("commentCount", Query.Direction.DESCENDING)

    fun uploadImageCloundary(filePath: String) = cloudinaryManager.uploadImage(filePath)

    fun createPost(userPost: Post) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_USERS)
        .document(userPost.uid)
        .collection(FirebaseApi.FireStore.Collection.PUBLISHED_POSTS)
        .document(userPost.postId)
        .set(userPost, SetOptions.merge())

    fun likePost(userPost: Post) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_USERS)
        .document(userPost.uid)
        .collection(FirebaseApi.FireStore.Collection.LIKED_POSTS)
        .document(userPost.postId)
        .set(userPost, SetOptions.merge())

    fun likeGlobalPost(userPost: Post) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_POST)
        .document(userPost.postId)
        .set(userPost, SetOptions.merge())


    fun observePost(post: Post) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_USERS)
        .document(post.uid)
        .collection(FirebaseApi.FireStore.Collection.PUBLISHED_POSTS)
        .document(post.postId)


    fun commentOnUserPost(userPost: Post) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_USERS)
        .document(userPost.uid)
        .collection(FirebaseApi.FireStore.Collection.PUBLISHED_POSTS)
        .document(userPost.postId)
        .set(userPost)

    fun commentOnGlobalPost(userPost: Post) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_POST)
        .document(userPost.postId)
        .set(userPost, SetOptions.merge())

    fun addPostGlobally(globalPost: Post) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_POST)
        .document(globalPost.postId)
        .set(globalPost, SetOptions.merge())

    fun getGlobalPost(postId: String) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_POST)
        .document(postId)
        .get()

    fun getUserPost(postId: String, uid: String) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_USERS)
        .document(uid)
        .collection(FirebaseApi.FireStore.Collection.PUBLISHED_POSTS)
        .document(postId)
        .get()

    fun deleteUserPost(postId: String, uid: String) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_USERS)
        .document(uid)
        .collection(FirebaseApi.FireStore.Collection.PUBLISHED_POSTS)
        .document(postId)
        .delete()

    fun deleteGlobalPost(postId: String) = firebaseStore
        .collection(FirebaseApi.FireStore.Collection.ALL_POST)
        .document(postId)
        .delete()

}