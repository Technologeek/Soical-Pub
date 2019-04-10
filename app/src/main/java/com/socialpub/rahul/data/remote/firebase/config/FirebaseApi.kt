package com.socialpub.rahul.data.remote.firebase.config

object FirebaseApi {

    object FireStore {

        object Collection {
            const val ALL_USERS = "all_users"
            const val USER_POSTS = "user_posts"
            const val ALL_POST = "all_posts"
            const val PROFILE = "profile"
        }

        object Documents {
            const val USER = "user"
            const val ALL_POST = "all_post"
        }

    }

    object Realtime {
        const val GLOBAL_POST = "global_post"
    }
}