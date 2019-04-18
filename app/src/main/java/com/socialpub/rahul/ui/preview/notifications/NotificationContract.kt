package com.socialpub.rahul.ui.preview.notifications

import com.socialpub.rahul.base.BaseContract
import com.socialpub.rahul.data.model.Notif

interface NotificationContract {

    interface View : BaseContract.View {
        fun attachActions()
        fun showAllNotifications(list: List<Notif>)
    }

    interface Controller : BaseContract.Controller {
        fun getAllNotifications(userId: String)
    }
}