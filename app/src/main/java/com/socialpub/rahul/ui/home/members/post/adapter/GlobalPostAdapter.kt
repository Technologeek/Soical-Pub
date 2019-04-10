package com.socialpub.rahul.ui.home.members.post.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.socialpub.rahul.R
import com.socialpub.rahul.data.model.Post
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.item_post.view.*

class GlobalPostAdapter private constructor(
    diffCallback: DiffUtil.ItemCallback<Post>,
    private val listener: PostClickListener
) :
    ListAdapter<Post, GlobalPostAdapter.PostViewHolder>(diffCallback) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem.postId == newItem.postId

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return (oldItem.postId == newItem.postId &&
                        oldItem.uid == newItem.uid &&
                        oldItem.username == newItem.username &&
                        oldItem.imageUrl == newItem.imageUrl &&
                        oldItem.caption == newItem.caption &&
                        oldItem.likeCount == newItem.likeCount &&
                        oldItem.commentCount == newItem.commentCount)
            }
        }

        fun newInstance(listener: PostClickListener) = GlobalPostAdapter(DIFF_CALLBACK, listener)
    }

    fun getPostAt(position: Int) = getItem(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        with(holder) {
            val post = getItem(position)
            text_username.text = post.username
            text_post_location.text = post.location
            text_post_date.text = post.date.toString()
            text_post_caption.text = post.caption
            btn_like.text = "${post.likeCount} Likes"
            btn_comments.text = "${post.commentCount} Comments"

            Picasso.get()
                .load(post.userProfilePic)
                .transform(CropCircleTransformation())
                .into(image_post_publisher_avatar)

            Picasso.get()
                .load(post.imageUrl)
                .placeholder(R.drawable.ic_empty_image)
                .fit()
                .centerInside()
                .into(image_post_preview)
        }
    }


    class PostViewHolder(
        view: View,
        private val listener: PostClickListener
    ) : RecyclerView.ViewHolder(view) {
        val text_username = view.text_user_name
        val text_post_location = view.text_post_location
        val text_post_date = view.text_post_date
        val text_post_caption = view.text_post_caption
        val image_post_preview = view.image_post_preview
        val image_post_publisher_avatar = view.image_post_publisher_avatar
        val btn_like = view.btn_like.also {
            it.setOnClickListener {
                listener.onlikeClicked(adapterPosition)
            }
        }

        val btn_comments = view.btn_comments.also {
            it.setOnClickListener {
                listener.onCommentClicked(adapterPosition)
            }
        }

    }
}

interface PostClickListener {
    fun onlikeClicked(adapterPosition: Int)
    fun onCommentClicked(position: Int)
}