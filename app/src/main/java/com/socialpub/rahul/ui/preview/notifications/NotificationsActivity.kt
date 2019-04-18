package com.socialpub.rahul.ui.preview.notifications

import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseActivity
import com.socialpub.rahul.data.model.Notif
import kotlinx.android.synthetic.main.activity_fav_post.*

class NotificationsActivity : BaseActivity(),NotificationContract.View {


    override val contentLayout: Int
        get() = R.layout.activity_notification


    override fun setup() {


    }

    override fun showLoading(message: String) = showProgress(message)

    override fun hideLoading() = hideProgress()

    override fun onError(message: String) {
        snack(root_fav, message)
    }


}
