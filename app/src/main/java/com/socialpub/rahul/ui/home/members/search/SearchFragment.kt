package com.socialpub.rahul.ui.home.members.search

import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseFragment
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.di.Injector
import com.socialpub.rahul.ui.home.members.post.adapter.GlobalPostAdapter
import com.socialpub.rahul.ui.home.members.post.adapter.PostClickListener
import com.socialpub.rahul.ui.home.members.search.adapter.SearchPostAdapter
import com.socialpub.rahul.ui.home.members.search.adapter.SearchPostListener
import com.socialpub.rahul.ui.home.members.search.adapter.SearchUserAdapter
import com.socialpub.rahul.ui.home.members.search.adapter.UserProfileListener
import com.socialpub.rahul.ui.home.navigation.NavController
import com.socialpub.rahul.utils.AppConst
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : BaseFragment(), SearchContract.View {


    override val contentLayout: Int
        get() = R.layout.fragment_search


    lateinit var controller: SearchController
    lateinit var navigator: NavController

    override fun setup(view: View) {
        navigator = attachedContext as NavController

        controller = SearchController(this)
        controller.onStart()
    }

    private lateinit var searchProfileAdapter: SearchUserAdapter
    private lateinit var searchPostAdapter: SearchPostAdapter

    override fun attachActions() {

        searchProfileAdapter = SearchUserAdapter.newInstance(
            object : UserProfileListener {
                override fun onClickUserProfile(position: Int) {
                    val profile = searchProfileAdapter.getProfileAt(position)
                    val userPrefs = Injector.userPrefs()
                    if (profile.uid != userPrefs.userId) {
                        navigator.openProfilePreview(true, profile.uid)
                    }
                }
            }
        )

        searchPostAdapter = SearchPostAdapter.newInstance(
            showPostActions = false,
            showPostProfile = true,
            listener = object : SearchPostListener {
                override fun onPostLongClicked(position: Int) {
                    onClickedPost(position)
                }

                override fun onPostClicked(position: Int) {
                    onClickedPost(position)
                }

                override fun onPostLikeClicked(position: Int) {
                    onClickedPost(position)
                }

                override fun onPostCommentClicked(position: Int) {
                    onClickedPost(position)
                }

            }
        )

        container_search.visibility = View.GONE
        container_search_post.visibility = View.GONE

        list_sort_users.run {
            layoutManager = LinearLayoutManager(attachedContext)
            adapter = searchProfileAdapter
        }

        list_search_post.run {
            layoutManager = LinearLayoutManager(attachedContext)
            adapter = searchPostAdapter
        }

        edit_search_user_name.run {

            setOnFocusChangeListener { v, hasFocus ->
                onCheckUpdate(v, hasFocus)
            }

            addTextChangedListener {
                val input = it.toString()
                controller.getResult(input)
            }
        }

        chip_sort_email.setOnCheckedChangeListener { buttonView, isChecked ->
            onCheckUpdate(buttonView, isChecked)
        }

        chip_sort_name.setOnCheckedChangeListener { buttonView, isChecked ->
            onCheckUpdate(buttonView, isChecked)
        }

        chip_sort_location.setOnCheckedChangeListener { buttonView, isChecked ->
            onCheckUpdate(buttonView, isChecked)
        }

    }

    private fun onClickedPost(position: Int) {
        val post = searchPostAdapter.getPostAt(position)
        navigator.openProfilePreview(true, post.uid)
    }

    private fun onCheckUpdate(view: View, isChecked: Boolean) {

        when (view.id) {
            R.id.chip_sort_email -> {
                controller.searchType(AppConst.SEARCH_FILTER_EMAIL)
                chip_sort_name.isChecked = false
                chip_sort_location.isChecked = false
                edit_search_user_name.hint = "Search user by email"
            }
            R.id.chip_sort_name -> {
                controller.searchType(AppConst.SEARCH_FILTER_NAME)
                chip_sort_email.isChecked = false
                chip_sort_location.isChecked = false
                edit_search_user_name.hint = "Search user by name"
            }
            R.id.chip_sort_location -> {
                controller.searchType(AppConst.SEARCH_FILTER_LOCATION)
                chip_sort_email.isChecked = false
                chip_sort_name.isChecked = false
                edit_search_user_name.hint = "Search user by Post location"
            }
            else -> {
            }
        }


        if (!chip_sort_email.isChecked
            && !chip_sort_name.isChecked
            && !chip_sort_location.isChecked
        ) {
            container_search.visibility = View.GONE
            container_search_post.visibility = View.GONE
            edit_search_user_name.hint = "Search user"
            controller.searchType(AppConst.SEARCH_FILTER_NONE)
        } else {
            if (chip_sort_location.isChecked) {
                container_search_post.visibility = View.VISIBLE
                container_search.visibility = View.GONE

            } else {
                container_search_post.visibility = View.GONE
                container_search.visibility = View.VISIBLE
            }

        }

    }

    override fun updatePostList(postList: List<Post>) {
        searchPostAdapter.submitList(postList)
    }

    override fun updateSearchList(userList: List<User>) {
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
