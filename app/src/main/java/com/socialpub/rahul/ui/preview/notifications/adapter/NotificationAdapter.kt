package com.socialpub.rahul.ui.preview.notifications.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.socialpub.rahul.R
import com.socialpub.rahul.data.model.Notif
import com.socialpub.rahul.utils.AppConst
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.item_notif.view.*
import timber.log.Timber

class NotificationAdapter private constructor(
    diffCallback: DiffUtil.ItemCallback<Notif>,
    private val listener: NotificationListener
) :
    ListAdapter<Notif, NotificationAdapter.NotifViewHolder>(diffCallback) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Notif>() {
            override fun areItemsTheSame(oldItem: Notif, newItem: Notif): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: Notif, newItem: Notif): Boolean {
                return (oldItem.actionOnPostId == newItem.actionOnPostId &&
                        oldItem.actionByuid == newItem.actionByuid &&
                        oldItem.actionByUsername == newItem.actionByUsername &&
                        oldItem.action == newItem.action &&
                        oldItem.actionByUserAvatar == newItem.actionByUserAvatar &&
                        oldItem.timestamp == newItem.timestamp)
            }
        }

        fun newInstance(listener: NotificationListener) = NotificationAdapter(DIFF_CALLBACK, listener)
    }

    fun getNotifAt(position: Int) = getItem(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notif, parent, false)
        return NotifViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotifViewHolder, position: Int) {
        with(holder) {

            val notif = getItem(position)

            if (notif.actionByUserAvatar.isNotEmpty()) {
                Picasso.get()
                    .load(notif.actionByUserAvatar)
                    .transform(CropCircleTransformation())
                    .into(image_actor_avtar)
            }

            val action = when (notif.action) {
                AppConst.NOTIF_ACTION_COMMENT -> "commented on your post"
                AppConst.NOTIF_ACTION_LIKE -> "liked your post"
                AppConst.NOTIF_ACTION_FOLLOWED -> "followed you"
                AppConst.NOTIF_ACTION_REPORT -> "REPORTED your post"
                else -> "Error"
            }

            text_actor_username.text = "${notif.actionByUsername} ${action}"

            container_notif.setOnClickListener {
                when (notif.action) {
                    AppConst.NOTIF_ACTION_COMMENT -> listener.onNotifClickedOpenPost(adapterPosition)
                    AppConst.NOTIF_ACTION_LIKE -> listener.onNotifClickedOpenPost(adapterPosition)
                    AppConst.NOTIF_ACTION_FOLLOWED -> listener.onNotifClickedOpenProfile(adapterPosition)
                    AppConst.NOTIF_ACTION_REPORT -> listener.onNotifClickedOpenPost(adapterPosition)
                    else -> Timber.e("Illegal value of action")
                }
            }

        }
    }


    class NotifViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {
        val text_actor_username = view.text_actor_name
        val image_actor_avtar = view.image_actor_avatar
        val container_notif = view.container_notif
    }
}

interface NotificationListener {
    fun onNotifClickedOpenPost(position: Int)
    fun onNotifClickedOpenProfile(position: Int)
}