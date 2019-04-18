package com.socialpub.rahul.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import com.socialpub.rahul.utils.AppConst
import com.socialpub.rahul.utils.dialogs.ProgressDialog

/***
 * Skeleton class for Activities , used to reduce boilerplate
 */
abstract class BaseActivity : AppCompatActivity() {

    protected abstract val contentLayout: Int

    protected abstract fun setup()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentLayout)
        setup()
    }

    protected fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    protected fun snack(layout: View, message: String) {
        Snackbar.make(layout, message, Snackbar.LENGTH_SHORT).show()
    }

    protected fun showProgress(message: String) {
        val progressDialog = supportFragmentManager.findFragmentByTag(AppConst.TAG_PROGRESS_DIALOG)
        if (progressDialog == null && !isFinishing) {
            val dialog = ProgressDialog.newInstance(message)
            dialog.show(requireNotNull(supportFragmentManager), AppConst.TAG_PROGRESS_DIALOG)
        }
    }

    protected fun hideProgress() {
        val progressDialog = supportFragmentManager.findFragmentByTag(AppConst.TAG_PROGRESS_DIALOG)
        if (progressDialog != null && !isFinishing) {
            (progressDialog as DialogFragment).dismiss()
        }
    }


}