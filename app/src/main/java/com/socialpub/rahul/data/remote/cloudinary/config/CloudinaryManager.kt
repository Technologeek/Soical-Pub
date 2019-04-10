package com.socialpub.rahul.data.remote.cloudinary.config

import com.cloudinary.android.MediaManager

class CloudinaryManager private constructor() {

    companion object {

        @Volatile
        private var instance: CloudinaryManager? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: CloudinaryManager().also { instance = it }
        }

    }

    fun uploadImage(filePath: String) = MediaManager.get()
        .upload(filePath)
        .option("connect_timeout", 10000)
        .option("read_timeout", 10000)


}