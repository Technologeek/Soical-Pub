package com.socialpub.rahul.ui.settings

import com.socialpub.rahul.base.BaseContract
import com.socialpub.rahul.data.model.User

interface SettingsContract {

    interface View : BaseContract.View {
        fun attachActions()
        fun updateUserSettings(isLocationVisible: Boolean, isEmailVisible: Boolean)
    }

    interface Controller : BaseContract.Controller {
        fun updateUserSettings(isLocationVisible: Boolean, isEmailVisible: Boolean)
        fun stopObservingSettings()
    }

}