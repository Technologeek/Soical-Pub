package com.socialpub.rahul.ui.profile.edit

import android.content.Intent
import android.view.View
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseBottomSheet
import com.socialpub.rahul.ui.home.members.post.PostContract
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.bottom_sheet_edit_profile.*

class ProfileEditBottomSheet : BaseBottomSheet(), ProfileEditContract.View {

    override val contentLayout: Int
        get() = R.layout.bottom_sheet_edit_profile

    lateinit var controller: ProfileEditController

    override fun setup(view: View) {
        controller = ProfileEditController(this)
        controller.onStart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        controller.handleImagePickerRequest(requestCode, resultCode, data)
    }

    override fun attachActions(profileUrl: String) {

        image_profile.run {
            Picasso.get()
                .load(profileUrl)
                .placeholder(R.mipmap.ic_launcher_round)
                .transform(CropCircleTransformation())
                .into(this)
            setOnClickListener {
                ImagePicker.create(this@ProfileEditBottomSheet)
                    .returnMode(ReturnMode.ALL)
                    .includeVideo(false)
                    .single()
                    .showCamera(true)
                    .start(PostContract.Controller.Const.IMAGE_PICKER_REQUEST)
            }
        }

        btn_close.setOnClickListener { this.dismiss() }
        btn_update_profile.setOnClickListener { controller.updateUserProfile(edit_new_username.text.toString()) }
    }

    override fun onError(message: String) = toast(message)

    override fun showSelectedImage(path: String) {
        Picasso.get()
            .load("file://$path")
            .placeholder(R.mipmap.ic_launcher_round)
            .transform(CropCircleTransformation())
            .into(image_profile)
    }

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
        fun newInstance() = ProfileEditBottomSheet()
    }

}