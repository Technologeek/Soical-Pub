package com.socialpub.rahul.data.remote.firebase.config

object FirebaseApi {

    object FireStore {

        object Collection {
            const val ALL_USERS = "all_users"
            const val PUBLISHED_POSTS = "published_posts"
            const val NOTIFICATIONS = "notifications"
            const val FAV_POSTS = "fav_posts"
            const val ALL_POST = "all_posts"
            const val PROFILE = "profile"
        }

        object Documents {
            const val LIKE = "liked"
            const val POST = "post"
            const val NOTIF = "notification"
        }

    }

    object Realtime {
        const val GLOBAL_POST = "global_post"
    }
}