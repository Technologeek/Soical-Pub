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
    private val listener: SearchPostListener
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

        fun newInstance(listener: SearchPostListener) = SearchPostAdapter(DIFF_CALLBACK, listener)
    }

    fun getPostAt(position: Int) = getItem(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        with(holder) {

            container_profile.visibility = View.GONE
            btn_like.visibility = View.GONE
            btn_comments.visibility = View.GONE

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

            image_post_preview.setOnClickListener {
                listener.onPostClicked(adapterPosition)
            }

            text_post_caption.text = post.caption

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
    }
}

interface SearchPostListener {
    fun onPostClicked(position: Int)
}