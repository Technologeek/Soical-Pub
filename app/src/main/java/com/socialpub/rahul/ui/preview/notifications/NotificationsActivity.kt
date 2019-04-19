package com.socialpub.rahul.ui.preview.notifications

import androidx.recyclerview.widget.LinearLayoutManager
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseActivity
import com.socialpub.rahul.data.model.Notif
import com.socialpub.rahul.ui.preview.notifications.adapter.NotificationAdapter
import com.socialpub.rahul.ui.preview.notifications.adapter.NotificationListener
import com.socialpub.rahul.ui.preview.notifications.navigator.Navigator
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationsActivity : BaseActivity(), NotificationContract.View {

    override val contentLayout: Int
        get() = R.layout.activity_notification

    lateinit var controller: NotificationController
    lateinit var notifAdapter: NotificationAdapter
    lateinit var navigator: Navigator


    override fun setup() {
        navigator = Navigator(0, this)
        controller = NotificationController(this)
        controller.onStart()
    }

    override fun attachActions() {

        btn_close.setOnClickListener {
            finish()
        }

        list_notification.run {
            layoutManager = LinearLayoutManager(this@NotificationsActivity)
            adapter = NotificationAdapter.newInstance(
                object : NotificationListener {
                    override fun onNotifClickedOpenPost(position: Int) {
                        toast("wip $position")
                    }

                    override fun onNotifClickedOpenProfile(position: Int) {
                        val notif = notifAdapter.getNotifAt(position)
                        navigator.openProfilePreview(true, notif.actionByuid)
                    }

                }
            ).also { notifAdapter = it }
        }
    }

    override fun showAllNotifications(list: List<Notif>) {
        notifAdapter.submitList(list){
            if (list.isEmpty()){
                onError("No notifications...")
            }
        }
    }

    override fun showLoading(message: String) = showProgress(message)

    override fun hideLoading() = hideProgress()

    override fun onError(message: String) {
        snack(root_notif, message)
    }

}
