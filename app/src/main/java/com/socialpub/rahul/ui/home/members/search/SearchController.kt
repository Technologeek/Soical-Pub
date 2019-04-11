package com.socialpub.rahul.ui.home.members.search

import com.google.gson.Gson
import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.data.remote.firebase.sources.post.PostSource
import com.socialpub.rahul.data.remote.firebase.sources.user.UserSource
import com.socialpub.rahul.di.Injector
import com.socialpub.rahul.utils.AppConst
import timber.log.Timber

class SearchController(private val view: SearchContract.View) : SearchContract.Controller {


    private lateinit var userPrefs: AppPrefs.User
    private lateinit var userSource: UserSource
    private lateinit var postSource: PostSource

    override fun onStart() {
        view.attachActions()
        userPrefs = Injector.userPrefs()
        userSource = Injector.userSource()
        postSource = Injector.postSource()
        getUserFollowers()
    }

    override fun getUserFollowers() {

        userSource.getUser(userPrefs.userId).addOnSuccessListener {
            val userProfile = it.toObject(User::class.java)
            userProfile?.apply {
                getFollowingList(following)
            }
        }.addOnFailureListener {
            view.onError("Something went wrong...")
            Timber.e(it.localizedMessage)
        }
    }

    private fun getFollowingList(followingIds: List<String>) {
        Timber.d(Gson().toJson(followingIds))
        val followerList = arrayListOf<User>()

        if (followingIds.isNotEmpty()) {
            followingIds.forEach { followingId ->
                userSource.getUser(followingId).addOnSuccessListener { documentSnapshot ->
                    val followingProfile = documentSnapshot.toObject(User::class.java)
                    followingProfile?.run {
                        followerList.add(this)
                        view.updateFollowingList(followerList)
                    }
                }
            }
        } else {
            view.onError("You are not following anyone!")
        }

    }

    private var searchType: Int = AppConst.SEARCH_FILTER_NONE

    override fun searchType(searchType: Int) {
        this.searchType = searchType
    }

    override fun getResult(query: String) {
        when (searchType) {
            AppConst.SEARCH_FILTER_NAME -> getSearchResult("username", query)
            AppConst.SEARCH_FILTER_EMAIL -> getSearchResult("email", query)
            AppConst.SEARCH_FILTER_LOCATION -> getSearchPostResults(query)
            else -> {
                view.onError("Please select the filter")
            }
        }
    }

    private fun getSearchPostResults(query: String) {

        val searchPosLists = arrayListOf<Post>()

        postSource.getAllPost().addOnSuccessListener { querySnapshot ->
            querySnapshot.forEach { queryDocument ->
                if (queryDocument.get("location").toString().contains(query, true)) {
                    val searchPost = queryDocument.toObject(Post::class.java)
                    searchPosLists.add(searchPost)
                    Timber.e(searchPost.toString())
                    view.updatePostList(searchPosLists)
                }
            }
        }.addOnFailureListener {
            Timber.e(it.localizedMessage)
            view.onError("Something went wrong...")
        }
    }

    private fun getSearchResult(type: String, query: String) {

        val userProfiles = arrayListOf<User>()

        userSource.getAllProfiles().addOnSuccessListener { querySnapshot ->
            querySnapshot?.apply {
                forEach { queryDocument ->
                    if (queryDocument.get(type).toString().contains(query, true)) {
                        val userProfile = queryDocument.toObject(User::class.java)
                        userProfiles.add(userProfile)
                        view.updateSearchList(userProfiles)
                    }
                }
            } ?: kotlin.run {
                view.onError("No results...")
                Timber.e("empty")
            }
        }.addOnFailureListener {
            view.onError(it.localizedMessage)
        }
    }


}