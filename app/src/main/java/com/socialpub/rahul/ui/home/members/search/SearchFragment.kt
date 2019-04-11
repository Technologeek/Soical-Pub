package com.socialpub.rahul.ui.home.members.search


import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseFragment
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.ui.home.members.search.adapter.SearchUserAdapter
import com.socialpub.rahul.ui.home.members.search.adapter.UserProfileListener
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment(), SearchContract.View {


    override val contentLayout: Int
        get() = R.layout.fragment_search


    lateinit var controller: SearchController

    override fun setup(view: View) {
        controller = SearchController(this)
        controller.onStart()
    }

    private lateinit var searchProfileAdapter: SearchUserAdapter

    override fun attachActions() {

        searchProfileAdapter = SearchUserAdapter.newInstance(
            object : UserProfileListener {
                override fun onClickUserProfile(position: Int) {

                }
            }
        )

        list_sort_users.run {
            layoutManager = LinearLayoutManager(attachedContext)
            adapter = searchProfileAdapter
        }


        edit_search_user_name.addTextChangedListener {
            val input = it.toString()
        }

        chip_sort_email
        chip_sort_name
        chip_sort_location

    }


    override fun updateList(userList: List<User>) {
        searchProfileAdapter.submitList(userList)
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
