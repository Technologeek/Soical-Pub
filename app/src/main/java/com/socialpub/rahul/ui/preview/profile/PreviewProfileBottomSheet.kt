package com.socialpub.rahul.ui.preview.profile

import android.os.Bundle
import android.view.View
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseBottomSheet
import com.socialpub.rahul.data.model.User
import com.socialpub.rahul.ui.preview.post.adapter.CommentsAdapter
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.bottom_sheet_preview_proifle.*

class PreviewProfileBottomSheet : BaseBottomSheet(), PreviewProfileContract.View {

    override val contentLayout: Int
        get() = R.layout.bottom_sheet_preview_proifle

    lateinit var controller: PreviewProfileController

    override fun setup(view: View) {
        val userId = arguments?.getString("userId")
        controller = PreviewProfileController(this)
        controller.onStart()
        controller.getUserProfile(userId)

        toast("$userId")
        text_preview_profile_user_email.text = "$userId"
    }

    private lateinit var commentsAdapter: CommentsAdapter

    override fun attachActions() {

        val showFollow = arguments?.getBoolean("showFollow", true) ?: true

        if (showFollow) {
            btn_preview_follow.run {
                visibility = View.VISIBLE
                setOnClickListener {
                    toast("add to follow")
                }
            }
        }

        btn_preview_profile_close.setOnClickListener {
            dissmissDialog()
        }

    }


    override fun updateUserPreview(user: User) {
        with(user) {
            Picasso.get()
                .load(avatar)
                .placeholder(R.mipmap.ic_launcher_round)
                .transform(CropCircleTransformation())
                .into(image_preview_profile_avatar)


            text_preview_profile_user_email.text = username
            text_preview_profile_user_name.text = email
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onError(message: String) = toast(message)


    override fun showLoading(message: String) {
        showProgress(message)
    }

    override fun hideLoading() {
        hideProgress()
    }

    override fun dissmissDialog() {
        this.dismiss()
    }

    companion object {
        fun newInstance(userId: String, showFollow: Boolean) = PreviewProfileBottomSheet().also {
            it.arguments = Bundle().apply {
                putBoolean("showFollow", showFollow)
                putString("userId", userId)
            }
        }
    }

}