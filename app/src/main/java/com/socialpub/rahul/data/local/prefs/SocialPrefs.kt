package com.socialpub.rahul.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import com.socialpub.rahul.utils.AppConst


class SocialPrefs private constructor(context: Context) {

    inner class UserPref() : AppPrefs.User {

        override var isUserLoggedIn: Boolean
            get() = prefs.getBoolean(AppPrefs.User.Key.IS_USER_LOGGEDIN, false)
            set(value) = prefs.edit().putBoolean(AppPrefs.User.Key.IS_USER_LOGGEDIN, value).apply()

        override var displayName: String
            get() = prefs.getString(AppPrefs.User.Key.USER_NAME, "USER NAME")!!
            set(value) = prefs.edit().putString(AppPrefs.User.Key.USER_NAME, value).apply()

        override var avatarUrl: String
            get() = prefs.getString(AppPrefs.User.Key.USER_PHOTO, "USER PHOTO")!!
            set(value) = prefs.edit().putString(AppPrefs.User.Key.USER_PHOTO, value).apply()

        override var email: String
            get() = prefs.getString(AppPrefs.User.Key.USER_EMAIL, "USER@EMAIL")!!
            set(value) = prefs.edit().putString(AppPrefs.User.Key.USER_EMAIL, value).apply()

        override var userId: String
            get() = prefs.getString(AppPrefs.User.Key.USER_ID, "")!!
            set(value) = prefs.edit().putString(AppPrefs.User.Key.USER_ID, value).apply()

        override var filterType: Int
            get() = prefs.getInt(AppPrefs.User.Key.FILTER_TYPE, AppConst.POST_FILTER_LATEST)
            set(value) = prefs.edit().putInt(AppPrefs.User.Key.FILTER_TYPE, value).apply()

        override var followers: Long
            get() = prefs.getLong(AppPrefs.User.Key.FOLLOWERS, 0)
            set(value) = prefs.edit().putLong(AppPrefs.User.Key.FOLLOWERS, value).apply()

        override var following: Long
            get() = prefs.getLong(AppPrefs.User.Key.FOLLOWING, 0)
            set(value) = prefs.edit().putLong(AppPrefs.User.Key.FOLLOWING, value).apply()

    }

    companion object {

        @Volatile
        private var instance: SocialPrefs? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: SocialPrefs(context).also { instance = it }
        }

    }

    private val prefs: SharedPreferences = context.getSharedPreferences("social_pub_pref", Context.MODE_PRIVATE)


}