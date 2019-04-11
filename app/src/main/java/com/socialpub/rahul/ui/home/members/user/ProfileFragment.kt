package com.socialpub.rahul.ui.home.members.user


import android.view.View
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseFragment

class ProfileFragment : BaseFragment() {

    override val contentLayout: Int
        get() = R.layout.fragment_proifle

    override fun setup(view: View) {

    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
