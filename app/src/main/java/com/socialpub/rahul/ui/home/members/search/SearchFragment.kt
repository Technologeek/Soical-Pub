package com.socialpub.rahul.ui.home.members.search


import android.view.View
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseFragment
import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.remote.firebase.sources.post.PostSource
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment(), SearchContract.View {


    override val contentLayout: Int
        get() = R.layout.fragment_search


    lateinit var controller: SearchController

    override fun setup(view: View) {
        controller = SearchController(this)
        controller.onStart()
    }

    override fun attachActions() {
        edit_search_user_name
    }

    override fun listScrollToTop() {

    }


    override fun showLoading(message: String) = showProgress(message)

    override fun hideLoading() = hideProgress()

    override fun onError(message: String) {
        toast(message)
    }

    companion object {
        fun newInstance() = SearchFragment()
    }

}
