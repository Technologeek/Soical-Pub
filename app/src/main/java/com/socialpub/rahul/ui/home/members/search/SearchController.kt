package com.socialpub.rahul.ui.home.members.search

import com.google.firebase.firestore.ListenerRegistration
import com.google.gson.Gson
import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.data.remote.firebase.sources.user.UserSource
import com.socialpub.rahul.di.Injector
import timber.log.Timber

class SearchController(private val view: SearchContract.View) : SearchContract.Controller {


    private lateinit var userPrefs: AppPrefs.User
    private lateinit var userSource: UserSource

    override fun onStart() {
        view.attachActions()
        userPrefs = Injector.userPrefs()
        userSource = Injector.userSource()
        getUserFollowers()
    }

    private fun getUserFollowers() {

        view.showLoading("loading your followers...")

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

        followingIds.forEach { followingId ->
            userSource.getUser(followingId).addOnSuccessListener { documentSnapshot ->
                val followingProfile = documentSnapshot.toObject(User::class.java)
                followingProfile?.run {
                    followerList.add(this)
                    view.updateList(followerList)
                    view.hideLoading()
                }
            }
        }
    }

    private var userProfileListener: ListenerRegistration? = null

    override fun searchType(searchType: Int) {


    }

    override fun getResult(query: String) {

    }


}