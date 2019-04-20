package com.socialpub.rahul.ui.preview.post.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.socialpub.rahul.R
import com.socialpub.rahul.data.model.Like
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.ui.home.members.search.adapter.UserProfileListener
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.item_search_user.view.*

class LikedeByAdapter private constructor(
    diffCallback: DiffUtil.ItemCallback<Like>,
    private val listener: UserProfileListener
) :
    ListAdapter<Like, LikedeByAdapter.UserViewHolder>(diffCallback) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Like>() {

            override fun areItemsTheSame(oldItem: Like, newItem: Like): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: Like, newItem: Like): Boolean {
                return (oldItem.uid == newItem.uid &&
                        oldItem.username == newItem.username &&
                        oldItem.userEmail == newItem.userEmail &&
                        oldItem.userAvatar == newItem.userAvatar)
            }
        }

        fun newInstance(listener: UserProfileListener) =
            LikedeByAdapter(
                DIFF_CALLBACK,
                listener
            )
    }

    fun getProfileAt(position: Int) = getItem(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_user, parent, false)
        return UserViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        with(holder) {

            val userProfile: Like = getItem(position)

            text_userEmail.text = userProfile.userEmail
            text_userName.text = userProfile.username

            if (userProfile.userAvatar.isNotEmpty()) {
                Picasso.get()
                    .load(userProfile.userAvatar)
                    .transform(CropCircleTransformation())
                    .into(image_userAvatar)
            }
        }
    }

    class UserViewHolder(
        view: View,
        private val listener: UserProfileListener
    ) : RecyclerView.ViewHolder(view) {
        val text_userName = view.text_search_user_name
        val text_userEmail = view.text_search_user_email
        val image_userAvatar = view.image_user_search_avatar
        private val container_profile = view.container_profile.also {
            it.setOnClickListener {
                listener.onClickUserProfile(adapterPosition)
            }
        }
    }
}