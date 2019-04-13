package com.socialpub.rahul.ui.preview.post

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseBottomSheet
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.ui.preview.post.adapter.CommentProfileListener
import com.socialpub.rahul.ui.preview.post.adapter.CommentsAdapter
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.bottom_sheet_preview_post.*
import kotlinx.android.synthetic.main.item_search_post.*
import java.text.DateFormat

class PreviewPostBottomSheet : BaseBottomSheet(), PreviewPostContract.View {

    override val contentLayout: Int
        get() = R.layout.bottom_sheet_preview_post

    lateinit var controller: PreviewPostController

    override fun setup(view: View) {

        val postId = arguments?.getString("postId")
        controller = PreviewPostController(this)
        controller.onStart()
        controller.getUserPost(postId)
    }

    private lateinit var commentsAdapter: CommentsAdapter

    override fun attachActions() {
        btn_close.setOnClickListener {
            dissmissDialog()
        }

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

        commentsAdapter = CommentsAdapter.newInstance(
            object : CommentProfileListener {
                override fun onClickCommentProfile(position: Int) {
                    
                }
            }
        )

        list_comments.run {
            layoutManager = LinearLayoutManager(attachedContext)
            adapter = commentsAdapter
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
                .into(image_publisher_post_avatar)

            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_empty_image)
                .into(image_published_post_preview)

            text_publisher_user_name.text = username
            text_published_post_location.text = location
            val date = DateFormat.getInstance().format(timestamp)
            text_published_post_date.text = date
            text_published_post_caption.text = caption
            commentsAdapter.submitList(comments)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
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
        this.dismiss()
    }

    companion object {
        fun newInstance(postId: String) = PreviewPostBottomSheet().also {
            it.arguments = Bundle().apply {
                putString("postId", postId)
            }
        }
    }

}