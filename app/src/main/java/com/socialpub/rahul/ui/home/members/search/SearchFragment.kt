package com.socialpub.rahul.ui.home.members.search


import android.view.View
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseFragment

class SearchFragment : BaseFragment() {

    override val contentLayout: Int
        get() = R.layout.fragment_search

    override fun setup(view: View) {

    }

    companion object {
        fun newInstance() = SearchFragment()
    }

}
