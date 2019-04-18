package com.socialpub.rahul.ui.onboarding.members.register


import android.content.Intent
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseFragment
import com.socialpub.rahul.ui.onboarding.navigation.NavController
import kotlinx.android.synthetic.main.fragment_register.*
import timber.log.Timber

/**
 * Social login fragment uses firebase Auth services to onboard user.
 * Refer Onboarding test class for more info and flow.
 */
class RegisterFragment : BaseFragment(), RegisterContract.View {

    override val contentLayout: Int = R.layout.fragment_register

    lateinit var controller: RegisterController
    lateinit var navigator: NavController

    override fun setup(view: View) {
        navigator = attachedContext as NavController
        controller = RegisterController(this)
        controller.onStart()
    }

    override fun attachActions(googleClient: GoogleSignInClient) {
        btn_google_signin.setOnClickListener {
            startActivityForResult(googleClient.signInIntent, RegisterContract.Controller.Const.GOOGLE_LOGIN_REQ_CODE)
        }
    }

    override fun initGoogleClient() = GoogleSignIn.getClient(
        attachedContext, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
    )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        controller.handleActivityResult(requestCode, data)
    }

    override fun onError(message: String) {
        toast(message)
    }

    override fun onboardedSuccessfully() {
        navigator.gotoHome()
        initGoogleClient().signOut().addOnSuccessListener {
            Timber.e("SignedOut")
        }
    }

    override fun showLoading(message: String) = showProgress(message)
    override fun hideLoading() = hideProgress()

    companion object {
        fun newInstance() = RegisterFragment()
    }

}
