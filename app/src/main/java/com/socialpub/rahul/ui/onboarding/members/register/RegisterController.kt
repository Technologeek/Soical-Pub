package com.socialpub.rahul.ui.onboarding.members.register

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.di.Injector
import timber.log.Timber

class RegisterController(
    private val view: RegisterContract.View
) : RegisterContract.Controller {


    override fun onStart() {
        val googleClinet = view.initGoogleClient()
        view.attachActions(requireNotNull(googleClinet))
    }

    override fun handleActivityResult(requestCode: Int, data: Intent?) = when (requestCode) {
        RegisterContract.Controller.Const.GOOGLE_LOGIN_REQ_CODE -> {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                Timber.d("Google sign in successfull")
                val account = task.getResult(ApiException::class.java)
                account?.let { handleGoogleAccount(it) }
                view.showLoading()
            } catch (e: ApiException) {
                Timber.e(e, "Google sign in failed ${e.statusCode} ${e.message}")
                view.onError("Failed. try again")
            }
        }
        else -> {
            Timber.e("GOOGLE_LOGIN_REQ_CODE not called back")
            view.onError("Failed. try again")
        }
    }

    private fun handleGoogleAccount(acct: GoogleSignInAccount) {
        Timber.d("handleGoogleAccount: ${acct.id}")
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        val auth = Injector.firebaseManager().auth
        auth.signInWithCredential(credential)
            .addOnSuccessListener { updateUI(requireNotNull(auth.currentUser)) }
            .addOnFailureListener { Timber.e(it) }
    }

    private fun updateUI(firebaseUser: FirebaseUser) {
        val userPrefs = Injector.userPrefs()
        with(userPrefs) {

            isUserLoggedIn = true

            val newUser = User(
                username = firebaseUser.displayName.toString(),
                email = firebaseUser.email.toString(),
                avatar = firebaseUser.photoUrl.toString(),
                uid = firebaseUser.uid
            )

            displayName = firebaseUser.displayName.toString()
            email = firebaseUser.email.toString()
            avatarUrl = firebaseUser.photoUrl.toString()
            userId = firebaseUser.uid
            getFirebaseUser(newUser)
        }
    }

    private fun getFirebaseUser(newUser: User) {
        val userSource = Injector.userSource()
        userSource.getUser(newUser.uid)
            .addOnSuccessListener {
                val user: User? = it.toObject(User::class.java)
                user?.apply {
                    view.hideLoading()
                    view.onboardedSuccessfully()
                } ?: kotlin.run {
                    view.showLoading("Creating profile...")
                    createFirebaseUser(newUser)
                }
            }
    }

    private fun createFirebaseUser(newUser: User) {

        val userSource = Injector.userSource()

        userSource.createUser(newUser).addOnSuccessListener {
            view.hideLoading()
            view.onboardedSuccessfully()
        }.addOnFailureListener {
            view.onError("UserStored failed:${it.localizedMessage}")
            Timber.e(it)
        }

    }


}