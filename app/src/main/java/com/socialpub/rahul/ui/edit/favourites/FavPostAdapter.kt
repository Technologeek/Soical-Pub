package com.socialpub.rahul.ui.edit.favourites

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
import kotlinx.android.synthetic.main.item_like_post.view.*
import java.text.DateFormat

class FavPostAdapter private constructor(
    diffCallback: DiffUtil.ItemCallback<Post>,
    private val listener: FavPostActionListener
) :
    ListAdapter<Post, FavPostAdapter.PostViewHolder>(diffCallback) {


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

        fun newInstance(listener: FavPostActionListener) = FavPostAdapter(DIFF_CALLBACK, listener)
    }

    fun getPostAt(position: Int) = getItem(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_like_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        with(holder) {

            val post = getItem(position)

            text_username.text = post.username
            text_post_location.text = post.location

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

            container_like_post.setOnClickListener {
                checkbox_post.isChecked = !checkbox_post.isChecked
            }

            checkbox_post.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked)
                    listener.onMutiSelectAddPost(adapterPosition)
                else
                    listener.onMutiSelectRemovePost(adapterPosition)
            }

        }
    }


    class PostViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {
        val text_username = view.text_publisher_user_name
        val text_post_location = view.text_published_post_location
        val text_post_date = view.text_published_post_date
        val image_post_preview = view.image_published_post_preview
        val image_post_publisher_avatar = view.image_publisher_post_avatar
        val checkbox_post = view.checkbox_post
        val container_like_post = view.container_like_post
    }
}

interface FavPostActionListener {
    fun onMutiSelectAddPost(position: Int)
    fun onMutiSelectRemovePost(position: Int)
}