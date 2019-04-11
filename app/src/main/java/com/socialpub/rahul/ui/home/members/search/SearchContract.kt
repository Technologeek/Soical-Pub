package com.socialpub.rahul.ui.home.members.search

import com.socialpub.rahul.base.BaseContract
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.model.User

interface SearchContract {

    interface View : BaseContract.View {
        fun attachActions()
        fun updateSearchList(userList: List<User>)
        fun updatePostList(postList: List<Post>)
        fun updateFollowingList(userList: List<User>)
        fun listScrollToTop()
    }

    interface Controller : BaseContract.Controller {
        fun searchType(searchType: Int)
        fun getResult(query: String)
        fun getUserFollowers()

    }

}