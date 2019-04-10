package com.socialpub.rahul

import android.app.Application
import com.cloudinary.android.MediaManager
import com.socialpub.rahul.di.Injector
import timber.log.Timber

class SocialPubApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()
        initCloudinary()
        initTimber()
    }

    private fun initTimber()  = Timber.plant(Timber.DebugTree())

    private fun initDI() = Injector.init(this)

    private fun initCloudinary() = MediaManager.init(this)
}