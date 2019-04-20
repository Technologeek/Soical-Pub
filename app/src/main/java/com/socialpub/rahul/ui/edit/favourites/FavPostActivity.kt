package com.socialpub.rahul.ui.edit.favourites

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseActivity
import com.socialpub.rahul.data.model.Post
import kotlinx.android.synthetic.main.activity_fav_post.*

class FavPostActivity : BaseActivity(), FavContract.View {

    override val contentLayout: Int
        get() = R.layout.activity_fav_post


    lateinit var controller: FavPostController
    lateinit var favPostAdapter: FavPostAdapter

    override fun setup() {

        initToolbar()
        controller = FavPostController(this)
        controller.onStart()

    }

    private val deletePostList: ArrayList<Post> = arrayListOf<Post>()

    override fun attachActions() {

        updateText()

        favPostAdapter = FavPostAdapter.newInstance(object : FavPostActionListener {
            override fun onMutiSelectAddPost(position: Int) {
                deletePostList.add(favPostAdapter.getPostAt(position))
                updateText()
            }

            override fun onMutiSelectRemovePost(position: Int) {
                deletePostList.remove(favPostAdapter.getPostAt(position))
                updateText()
            }
        })

        list_fav_post.run {
            layoutManager = LinearLayoutManager(this@FavPostActivity)
            adapter = favPostAdapter
        }

        btn_multi_delete.setOnClickListener {
            actionDelePostConfirm()
        }

    }

    private fun actionDelePostConfirm() {
        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle("Delete Selected?")
            setMessage("are you sure you want to delete selected post?")
            setPositiveButton("Yes", DialogInterface.OnClickListener(function = { dialog, which ->
                controller.deleteFavPost(deletePostList)
            }))
            setNegativeButton("No", DialogInterface.OnClickListener(function = { dialog, which ->
                dialog.dismiss()
            }))
            if (!isFinishing) show()
        }
    }

    override fun onDeletedComplted() {
        deletePostList.clear()
        updateText()
    }

    private fun updateText() {
        btn_multi_delete.text = "Delete (${deletePostList.size} Post)"
    }

    override fun updateLikedList(postList: List<Post>) {
        favPostAdapter.submitList(postList) {
            if (postList.isEmpty()) {
                favPostAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun listScrollToTop() {
        list_fav_post.smoothScrollToPosition(0)
    }

    override fun showLoading(message: String) = showProgress(message)

    override fun hideLoading() = hideProgress()

    override fun onError(message: String) {
        snack(root_fav, message)
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.stopObservingLikedPost()
    }


    private fun initToolbar() = setSupportActionBar(toolbar_fav).let {
        with(requireNotNull(supportActionBar)) {
            setDefaultDisplayHomeAsUpEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
