package com.socialpub.rahul.ui.onboarding.members.register

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.socialpub.rahul.base.BaseContract

interface RegisterContract {

    interface View : BaseContract.View {
        fun initGoogleClient(): GoogleSignInClient?
        fun attachActions(googleClient: GoogleSignInClient)
        fun onboardedSuccessfully()
    }

    interface Controller:BaseContract.Controller{

        object Const {
            const val GOOGLE_LOGIN_REQ_CODE = 1000
        }

        fun handleActivityResult(requestCode: Int, data: Intent?)
    }
}