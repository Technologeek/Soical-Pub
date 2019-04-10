package com.socialpub.rahul.utils.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.socialpub.rahul.R
import com.socialpub.rahul.utils.AppConst
import kotlinx.android.synthetic.main.dialog_progressbar.*


class ProgressDialog : DialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_progressbar, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val msg = arguments?.getString("message")
        message.text = msg ?: AppConst.DIALOG_PLEASE_WAIT
        isCancelable = false
    }

    companion object {
        fun newInstance(message: String) = ProgressDialog().also {
            it.arguments = Bundle().apply {
                putString("message", message)
            }
        }
    }

}