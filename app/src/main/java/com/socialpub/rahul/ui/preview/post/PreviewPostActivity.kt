package com.socialpub.rahul.ui.preview.post

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseActivity
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.ui.home.members.search.adapter.UserProfileListener
import com.socialpub.rahul.ui.preview.post.adapter.CommentProfileListener
import com.socialpub.rahul.ui.preview.post.adapter.CommentsAdapter
import com.socialpub.rahul.ui.preview.post.adapter.LikedeByAdapter
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.bottom_sheet_preview_post.*
import kotlinx.android.synthetic.main.item_post.*
import java.text.DateFormat

class PreviewPostActivity : BaseActivity(), PreviewPostContract.View {

    override val contentLayout: Int
        get() = R.layout.bottom_sheet_preview_post

    lateinit var controller: PreviewPostController
    lateinit var navigator: Navigator

    override fun setup() {
        initToolbar()

        navigator = Navigator(0, this)
        controller = PreviewPostController(this)
        controller.onStart()
    }

    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var likeAdapter: LikedeByAdapter

    override fun attachActions() {

        val postId: String? = intent?.extras?.getString("postId")
        val enableDelete: Boolean = intent?.extras?.getBoolean("enableDelete", false) ?: false

        btn_like.visibility = View.GONE
        btn_comments.visibility = View.GONE
        container_view_liked_by.visibility = View.VISIBLE
        container_view_comments.visibility = View.GONE
        container_edit_comment.visibility = View.GONE

        likeAdapter = LikedeByAdapter.newInstance(
            object : UserProfileListener {
                override fun onClickUserProfile(position: Int) {
                    val profile = likeAdapter.getProfileAt(position)
                    navigator.openProfilePreview(true, profile.uid)
                }
            }
        )

        commentsAdapter = CommentsAdapter.newInstance(
            object : CommentProfileListener {
                override fun onClickCommentProfile(position: Int) {
                    val profile = commentsAdapter.getProfileAt(position)
                    navigator.openProfilePreview(true, profile.uid)
                }
            }
        )


        list_likedby.run {
            layoutManager = LinearLayoutManager(this@PreviewPostActivity)
            adapter = likeAdapter
        }

        list_comments.run {
            layoutManager = LinearLayoutManager(this@PreviewPostActivity)
            adapter = commentsAdapter
        }

        btn_view_like.setOnClickListener {
            container_view_liked_by.visibility = View.VISIBLE
            container_view_comments.visibility = View.GONE
            container_edit_comment.visibility = View.GONE
        }

        btn_view_comments.setOnClickListener {
            container_view_liked_by.visibility = View.GONE
            container_view_comments.visibility = View.VISIBLE
            container_edit_comment.visibility = View.VISIBLE
        }

        if (!enableDelete) {

            //global user
            btn_post_delete.visibility = View.GONE
            val globalUserId = intent?.extras?.getString("globalUserId")
            controller.getGlobalUserPost(postId, globalUserId)


            edit_new_comment.setOnEditorActionListener { view, actionId, event ->
                return@setOnEditorActionListener when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        submitGlobalComment(edit_new_comment.text.toString())
                        edit_new_comment.text?.clear()
                        true
                    }
                    else -> false
                }
            }


        } else {
            //local user
            controller.getUserPost(postId)

            edit_new_comment.setOnEditorActionListener { view, actionId, event ->
                return@setOnEditorActionListener when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        submitComment(edit_new_comment.text.toString())
                        edit_new_comment.text?.clear()
                        true
                    }
                    else -> false
                }
            }

            btn_post_delete.setOnClickListener {
                controller.deletePost()
            }

        }
    }

    private fun submitGlobalComment(text: String) {
        if (text.trim().isNotEmpty()) {
            controller.submitGlobalComment(text)
        } else {
            onError("Enter some comment...")
        }
    }

    private fun submitComment(text: String) {
        if (text.trim().isNotEmpty()) {
            controller.submitComment(text)
        } else {
            onError("Enter some comment...")
        }

    }

    override fun updatePostPreview(post: Post) {
        with(post) {
            Picasso.get()
                .load(userAvatar)
                .placeholder(R.mipmap.ic_launcher_round)
                .transform(CropCircleTransformation())
                .into(image_post_publisher_avatar)

            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_empty_image)
                .into(image_post_preview)

            text_user_name.text = username
            text_post_location.text = location.name
            val date = DateFormat.getInstance().format(timestamp)
            text_post_date.text = date
            text_post_caption.text = caption
            text_comment_count.text = "Comments (${comments.size}) :"
            commentsAdapter.submitList(comments)
            text_like_count.text = "Liked by (${likedBy.size}) :"
            likeAdapter.submitList(likedBy)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.stopObservingComments()
    }

    override fun onError(message: String) = toast(message)


    override fun showLoading(message: String) {
        showProgress(message)
    }

    override fun hideLoading() {
        hideProgress()
    }

    override fun dissmissDialog() {
        finish()
    }

    private fun initToolbar() = setSupportActionBar(toolbar_post).let {
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
