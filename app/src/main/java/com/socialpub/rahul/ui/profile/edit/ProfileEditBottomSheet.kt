package com.socialpub.rahul.ui.profile.edit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.socialpub.rahul.R
import com.socialpub.rahul.ui.home.members.post.PostContract
import com.socialpub.rahul.utils.AppConst
import com.socialpub.rahul.utils.dialogs.ProgressDialog
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.bottom_sheet_edit_profile.*

class ProfileEditBottomSheet : BottomSheetDialogFragment(), ProfileEditContract.View {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        setup(view)
    }

    lateinit var controller: ProfileEditController

    private fun setup(view: View) {
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

    fun toast(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

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

}