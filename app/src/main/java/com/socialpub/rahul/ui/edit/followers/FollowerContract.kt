package com.socialpub.rahul.ui.edit.followers

import com.socialpub.rahul.base.BaseContract
import com.socialpub.rahul.data.model.User

interface FollowerContract {

    interface View : BaseContract.View {
        fun attachActions()
        fun updateFollowerList(followerList: List<User>)
        fun updateFollowingList(followerList: List<User>)
        fun listScrollToTop()
    }

    interface Controller : BaseContract.Controller {
        fun getUser(userId: String)
        fun startObservingFollowers()
        fun stopObservingFollowers()
        fun unfollowUser(user: User)
        fun followUser(user: User)
    }

}