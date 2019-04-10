package com.socialpub.rahul.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.socialpub.rahul.utils.AppConst
import com.socialpub.rahul.utils.dialogs.ProgressDialog

/***
 * Skeleton class for Fragments , used to reduce boilerplate
 */
abstract class BaseFragment : Fragment() {

    protected lateinit var attachedContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        attachedContext = requireContext()
    }

    fun showProgress(message: String) {
        val progressDialog = childFragmentManager.findFragmentByTag(AppConst.TAG_PROGRESS_DIALOG)
        if (progressDialog == null) {
            if (isAdded) {
                val dialog = ProgressDialog.newInstance(message)
                dialog.show(requireNotNull(childFragmentManager), AppConst.TAG_PROGRESS_DIALOG)
            }
        }
    }

    fun hideProgress() {
        val progressDialog = childFragmentManager.findFragmentByTag(AppConst.TAG_PROGRESS_DIALOG)
        if (progressDialog != null) {
            (progressDialog as DialogFragment).dismiss()
        }
    }

    protected abstract val contentLayout: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(
            contentLayout, container, false
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(view)
    }

    protected abstract fun setup(view: View)

    protected fun toast(msg: String) {
        Toast.makeText(attachedContext, msg, Toast.LENGTH_SHORT).show()
    }

}