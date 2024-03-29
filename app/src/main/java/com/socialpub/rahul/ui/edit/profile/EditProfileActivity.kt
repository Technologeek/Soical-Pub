package com.socialpub.rahul.ui.edit.profile

import android.content.Intent
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseActivity
import com.socialpub.rahul.ui.home.members.post.PostContract
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.bottom_sheet_edit_profile.*

class EditProfileActivity : BaseActivity(), EditUserProfileContract.View {

    override val contentLayout: Int
        get() = R.layout.bottom_sheet_edit_profile

    lateinit var userProfileController: EditUserProfileController

    override fun setup() {
        initToolbar()
        userProfileController = EditUserProfileController(this)
        userProfileController.onStart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        userProfileController.handleImagePickerRequest(requestCode, resultCode, data)
    }

    override fun attachActions(profileUrl: String) {

        image_profile.run {
            Picasso.get()
                .load(profileUrl)
                .placeholder(R.mipmap.ic_launcher)
                .transform(CropCircleTransformation())
                .into(this)
            setOnClickListener {
                ImagePicker.create(this@EditProfileActivity)
                    .returnMode(ReturnMode.ALL)
                    .includeVideo(false)
                    .single()
                    .showCamera(true)
                    .start(PostContract.Controller.Const.IMAGE_PICKER_REQUEST)
            }
        }

        btn_update_profile.setOnClickListener { userProfileController.updateUserProfile(edit_new_username.text.toString()) }
    }

    override fun onError(message: String) = toast(message)

    override fun showSelectedImage(path: String) {
        Picasso.get()
            .load("file://$path")
            .placeholder(R.mipmap.ic_launcher)
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
        finish()
    }

    private fun initToolbar() = setSupportActionBar(toolbar_profile).let {
        with(requireNotNull(supportActionBar)) {
            setDefaultDisplayHomeAsUpEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

