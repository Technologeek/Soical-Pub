package com.socialpub.rahul.ui.home.members.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.socialpub.rahul.R
import com.socialpub.rahul.data.model.User
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.item_search_user.view.*
import timber.log.Timber

class SearchUserAdapter private constructor(
    diffCallback: DiffUtil.ItemCallback<User>,
    private val listener: UserProfileListener
) :
    ListAdapter<User, SearchUserAdapter.UserViewHolder>(diffCallback) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem.uid == newItem.uid

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return (oldItem.uid == newItem.uid &&
                        oldItem.email == newItem.email)
            }
        }

        fun newInstance(listener: UserProfileListener) =
            SearchUserAdapter(
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

            val userProfile: User = getItem(position)

            Timber.e(Gson().toJson(userProfile))

            text_userEmail.text = userProfile.email
            text_userName.text = userProfile.username

            if (userProfile.avatar?.isEmpty() != true) {
                Picasso.get()
                    .load(userProfile.avatar)
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

interface UserProfileListener {
    fun onClickUserProfile(position: Int)
}