package com.socialpub.rahul.utils.helper

import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback

abstract class CloudinaryUploadHelper : UploadCallback {
    override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {

    }

    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
        val progress = (bytes / totalBytes).toDouble()
    }

    override fun onReschedule(requestId: String?, error: ErrorInfo?) {

    }

    override fun onError(requestId: String?, error: ErrorInfo?) {

    }

    override fun onStart(requestId: String?) {

    }
}