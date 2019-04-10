package com.socialpub.rahul.ui.home.members.search

import com.socialpub.rahul.base.BaseContract

interface SearchContract {

    interface View : BaseContract.View {
        fun attachActions()
        fun listScrollToTop()
    }

    interface Controller : BaseContract.Controller {
        fun startObservingGlobalFeeds()
        fun stopObservingGlobalFeeds()
    }

}