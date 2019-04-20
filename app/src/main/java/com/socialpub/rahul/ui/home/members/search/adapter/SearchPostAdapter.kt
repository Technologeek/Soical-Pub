package com.socialpub.rahul.ui.home.members.search.adapter

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

class SearchPostAdapter private constructor(
    diffCallback: DiffUtil.ItemCallback<Post>,
    private val listener: SearchPostListener,
    private val showPostActions: Boolean,
    private val showPostProfile: Boolean
) :
    ListAdapter<Post, SearchPostAdapter.PostViewHolder>(diffCallback) {

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

        fun newInstance(
            listener: SearchPostListener,
            showPostActions: Boolean = true,
            showPostProfile: Boolean = false
        ) = SearchPostAdapter(DIFF_CALLBACK, listener, showPostActions, showPostProfile)
    }

    fun getPostAt(position: Int) = getItem(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        with(holder) {


            if (!showPostActions) {
                container_post_actions.visibility = View.GONE
            }

            if (!showPostProfile) {
                container_profile.visibility = View.GONE
            }

            btn_like.setOnClickListener {
                listener.onPostLikeClicked(adapterPosition)
            }
            btn_comments.setOnClickListener {
                listener.onPostCommentClicked(adapterPosition)
            }

            val post = getItem(position)

            Timber.d(Gson().toJson(post))

            if (!post.imageUrl.isEmpty()) {
                Picasso.get()
                    .load(post.imageUrl)
                    .placeholder(R.drawable.ic_empty_image)
                    .fit()
                    .centerInside()
                    .into(image_post_preview)
            }

            image_post_preview.setOnLongClickListener {
                listener.onPostLongClicked(adapterPosition)
                return@setOnLongClickListener true
            }

            image_post_preview.setOnClickListener {
                listener.onPostClicked(adapterPosition)
            }

            text_post_caption.text = post.caption
            text_user_name.text = post.username
            text_post_location.text = post.location.name

            val date = DateFormat.getInstance().format(post.timestamp)
            text_post_date.text = date

            if (!post.userAvatar.isEmpty()) {
                Picasso.get()
                    .load(post.userAvatar)
                    .transform(CropCircleTransformation())
                    .into(image_post_publisher_avatar)
            }

        }
    }


    class PostViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        val container_profile = view.container_profile
        val btn_comments = view.btn_comments
        val btn_like = view.btn_like
        val image_post_preview = view.image_post_preview
        val text_post_caption = view.text_post_caption
        val container_post_actions = view.container_post_actions
        val text_user_name = view.text_user_name
        val text_post_location = view.text_post_location
        val text_post_date = view.text_post_date
        val image_post_publisher_avatar = view.image_post_publisher_avatar
    }
}

interface SearchPostListener {
    fun onPostLongClicked(position: Int)
    fun onPostClicked(position: Int)
    fun onPostLikeClicked(position: Int)
    fun onPostCommentClicked(position: Int)
}