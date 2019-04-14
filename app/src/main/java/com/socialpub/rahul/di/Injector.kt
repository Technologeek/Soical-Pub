package com.socialpub.rahul.di

import android.content.Context
import com.socialpub.rahul.data.local.prefs.AppPrefs
import com.socialpub.rahul.data.local.prefs.SocialPrefs
import com.socialpub.rahul.data.remote.cloudinary.config.CloudinaryManager
import com.socialpub.rahul.data.remote.firebase.config.FirebaseManager
import com.socialpub.rahul.data.remote.firebase.sources.post.PostSource
import com.socialpub.rahul.data.remote.firebase.sources.user.UserSource
import com.socialpub.rahul.service.android.location.LocationManger

class Injector {

    companion object {

        @Volatile
        private lateinit var appContext: Context

        fun init(context: Context) {
            appContext = context
        }

        fun userPrefs(): AppPrefs.User = SocialPrefs.getInstance(appContext).UserPref()

        fun firebaseManager() = FirebaseManager.getInstance()

        fun userSource() = UserSource.getInstance(firebaseManager())

        fun cloudinaryManager() = CloudinaryManager.getInstance()

        fun postSource() = PostSource.getInstance(firebaseManager(), cloudinaryManager())

        fun locationManager() = LocationManger.getInstance(appContext)
    }

}
