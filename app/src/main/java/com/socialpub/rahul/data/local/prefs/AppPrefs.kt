package com.socialpub.rahul.data.local.prefs


interface AppPrefs {


    interface User {

        object Key {
            const val IS_USER_LOGGEDIN = "IS_USER_LOGGEDIN"
            const val USER_NAME: String = "USER_NAME"
            const val USER_PHOTO: String = "USER_PHOTO"
            const val USER_EMAIL: String = "USER_EMAIL"
            const val USER_ID: String = "USER_UNIQUE_ID"
        }

        var isUserLoggedIn: Boolean
        var userId: String
        var displayName: String
        var avatarUrl: String
        var email: String
    }


}