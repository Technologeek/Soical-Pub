package com.socialpub.rahul.ui.home.members.post.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.socialpub.rahul.R
import com.socialpub.rahul.data.model.Post
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.item_post.view.*
import timber.log.Timber
import java.text.DateFormat

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
                        oldItem.userAvatar == newItem.userAvatar &&
                        oldItem.imageUrl == newItem.imageUrl &&
                        oldItem.caption == newItem.caption &&
                        oldItem.location == newItem.location &&
                        oldItem.comments == newItem.comments &&
                        oldItem.likedBy == newItem.likedBy &&
                        oldItem.likeCount == newItem.likeCount &&
                        oldItem.commentCount == newItem.commentCount &&
                        oldItem.timestamp == newItem.timestamp &&
                        oldItem.imageUrl == newItem.imageUrl)
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

            Timber.d(Gson().toJson(post))

            text_username.text = post.username
            text_post_location.text = post.location.name
            text_post_caption.text = post.caption
            btn_like.text = "${post.likeCount} Likes"
            btn_comments.text = "${post.commentCount} Comments"

            val date = DateFormat.getInstance().format(post.timestamp)
            text_post_date.text = date

            if (!post.userAvatar.isEmpty()) {
                Picasso.get()
                    .load(post.userAvatar)
                    .transform(CropCircleTransformation())
                    .into(image_post_publisher_avatar)
            }
            if (!post.imageUrl.isEmpty()) {
                Picasso.get()
                    .load(post.imageUrl)
                    .placeholder(R.drawable.ic_empty_image)
                    .fit()
                    .centerInside()
                    .into(image_post_preview)
            }

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
        val image_post_preview = view.image_post_preview.also {
            it.setOnLongClickListener {
                listener.onLongPressPost(adapterPosition)
                return@setOnLongClickListener true
            }
        }
        val image_post_publisher_avatar = view.image_post_publisher_avatar.also {
            it.setOnClickListener {
                listener.onProfileClicked(adapterPosition)
            }
        }
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
    fun onlikeClicked(position: Int)
    fun onCommentClicked(position: Int)
    fun onProfileClicked(position: Int)
    fun onLongPressPost(position: Int)
}