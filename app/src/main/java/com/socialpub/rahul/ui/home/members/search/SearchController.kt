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

        val userNameProfiles = arrayListOf<User>()
        val userEmailProfiles = arrayListOf<User>()

        userSource.getAllProfiles().addOnSuccessListener { querySnapshot ->
            querySnapshot?.apply {
                forEach { queryDocument ->
                    if (queryDocument.get(type).toString().contains(query, true)) {
                        val userProfile = queryDocument.toObject(User::class.java)
                        if (type.equals("username")){
                            userNameProfiles.add(userProfile)
                            view.updateSearchList(userNameProfiles)
                        }

                        if (type.equals("email") && userProfile.isVisibletoEmailSearch) {
                            userEmailProfiles.add(userProfile)
                            view.updateSearchList(userEmailProfiles)
                        }

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