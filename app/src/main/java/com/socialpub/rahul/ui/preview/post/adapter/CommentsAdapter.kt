package com.socialpub.rahul.ui.preview.post.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.socialpub.rahul.R
import com.socialpub.rahul.data.model.Comment
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.item_comments.view.*
import timber.log.Timber

class CommentsAdapter private constructor(
    diffCallback: DiffUtil.ItemCallback<Comment>,
    private val listener: CommentProfileListener
) :
    ListAdapter<Comment, CommentsAdapter.CommentViewHolder>(diffCallback) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Comment>() {

            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean = oldItem.uid == newItem.uid

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return (oldItem.uid == newItem.uid &&
                        oldItem.userAvatar == newItem.userAvatar
                        && oldItem.text == newItem.text)
            }
        }

        fun newInstance(listener: CommentProfileListener) =
            CommentsAdapter(
                DIFF_CALLBACK,
                listener
            )
    }

    fun getProfileAt(position: Int) = getItem(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comments, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        with(holder) {

            val comment: Comment = getItem(position)

            Timber.e(Gson().toJson(comment))

            text_CommentMessage.text = comment.text
            text_comment_user_name.text = comment.userName
            if (!comment.userAvatar.isNullOrBlank()) {
                Picasso.get()
                    .load(comment.userAvatar)
                    .transform(CropCircleTransformation())
                    .into(image_CommentAvatar)
            }

            container_Comment.setOnClickListener {
                listener.onClickCommentProfile(position)
            }
        }
    }


    class CommentViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {
        val text_CommentMessage = view.text_comment_message
        val text_comment_user_name = view.text_comment_user_name
        val image_CommentAvatar = view.image_comment_avatar
        val container_Comment = view.container_comments
    }
}

interface CommentProfileListener {
    fun onClickCommentProfile(position: Int)
}