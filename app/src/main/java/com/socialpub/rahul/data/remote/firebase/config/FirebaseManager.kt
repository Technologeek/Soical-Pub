package com.socialpub.rahul.data.remote.firebase.config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseManager private constructor() {

    companion object {

        @Volatile
        private var instance: FirebaseManager? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: FirebaseManager().also { instance = it }
        }

    }

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
        private set

    var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        private set
}