package com.socialpub.rahul.ui.settings

import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity(), SettingsContract.View {


    override val contentLayout: Int
        get() = R.layout.activity_settings


    lateinit var controller: SettingsController
    private var isLocatonVisible = false
    private var isEmailVisible = false

    override fun setup() {

        initToolbar()

        controller = SettingsController(this)
        controller.onStart()
    }

    override fun attachActions() {
        switch_location_search.setOnClickListener {
            controller.updateUserSettings(!isLocatonVisible, isEmailVisible)
        }

        switch_email_search.setOnClickListener {
            controller.updateUserSettings(isLocatonVisible, !isEmailVisible)
        }

    }

    override fun updateUserSettings(isLocationVisible: Boolean, isEmailVisible: Boolean) {
        this.isLocatonVisible = isLocationVisible
        this.isEmailVisible = isEmailVisible
        switch_location_search.isChecked = isLocationVisible
        switch_email_search.isChecked = isEmailVisible
    }

    private fun initToolbar() = setSupportActionBar(toolbar_settings).let {
        with(requireNotNull(supportActionBar)) {
            setDefaultDisplayHomeAsUpEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun showLoading(message: String) = showProgress(message)

    override fun hideLoading() = hideProgress()

    override fun onError(message: String) {
        snack(root_settings, message)
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.stopObservingSettings()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
