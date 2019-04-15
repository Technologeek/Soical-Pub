package com.socialpub.rahul.ui.edit.post

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseBottomSheet
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.di.Injector
import com.socialpub.rahul.service.android.location.LocationManger
import com.socialpub.rahul.ui.home.members.post.PostContract
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.bottom_sheet_upload_post.*
import timber.log.Timber


class PostBottomSheet : BaseBottomSheet(), PostUploadContract.View {

    override val contentLayout: Int
        get() = R.layout.bottom_sheet_upload_post

    lateinit var controller: PostUploadController

    override fun setup(view: View) {
        controller = PostUploadController(this)
        controller.onStart()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        controller.handleImagePickerRequest(requestCode, resultCode, data)
    }


    lateinit var locationManager: LocationManger

    override fun attachActions() {

        initMap()

        image_post_content.run {

            setOnClickListener {
                ImagePicker.create(this@PostBottomSheet)
                    .returnMode(ReturnMode.ALL)
                    .includeVideo(false)
                    .single()
                    .showCamera(true)
                    .start(PostContract.Controller.Const.IMAGE_PICKER_REQUEST)
            }
        }

        btn_upload_post_close.setOnClickListener { this.dismiss() }


        edit_post_caption.setOnEditorActionListener { view, actionId, event ->
            return@setOnEditorActionListener when {
                event?.action == KeyEvent.KEYCODE_ENTER ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        actionId == EditorInfo.IME_ACTION_GO ||
                        actionId == EditorInfo.IME_ACTION_NEXT -> {
                    scroll.fullScroll(View.FOCUS_DOWN)
                    val imm = attachedContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm?.hideSoftInputFromWindow(edit_post_caption.windowToken, 0)
                    true
                }
                else -> false
            }
        }

        btn_post_submit.isEnabled = false
    }

    override fun onPostSubmittedSuccess() {
        toast("Post submitted...")
        dissmissDialog()
    }


    private var googleMap: GoogleMap? = null
    private fun initMap() {
        val mapFragment = SupportMapFragment()

        childFragmentManager
            .beginTransaction()
            .add(R.id.container_map, mapFragment, "post_map")
            .commitNowAllowingStateLoss()

        mapFragment.getMapAsync {
            googleMap = it
            try {
                googleMap?.isMyLocationEnabled = true
            } catch (e: SecurityException) {
                toast(e.localizedMessage)
            }

            googleMap?.setOnMapClickListener { latlag ->
                googleMap?.clear()
                updateMapCamera(latlag)
            }
        }

        locationManager = Injector.locationManager()

        locationManager.getLastLocation()
            ?.addOnSuccessListener {
                updateMapCamera(LatLng(it.latitude, it.longitude))
            }?.addOnFailureListener {
                onError("Error getting location...Cant tag your post!")
                Timber.e(it.localizedMessage)
            }

        external_scroll_blocker.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    scroll.requestDisallowInterceptTouchEvent(true)
                    false
                }
                MotionEvent.ACTION_UP -> {
                    scroll.requestDisallowInterceptTouchEvent(false);
                    true
                }
                else -> true
            }
        }
    }

    private fun updateMapCamera(latLng: LatLng) {
        googleMap?.moveCamera(
            CameraUpdateFactory
                .newLatLngZoom(
                    latLng,
                    DEFAULT_ZOOM
                )
        )

        val address = locationManager.getAddressFromLocation(latLng)
        text_location_address.text = address.get(0).getAddressLine(0)

        googleMap?.addMarker(locationManager.makeMarker(latLng))

    }


    override fun onDestroyView() {
        super.onDestroyView()
        val mapFragement = childFragmentManager.findFragmentByTag("post_map")
        if (mapFragement != null)
            childFragmentManager
                .beginTransaction()
                .remove(mapFragement)
                .commitNowAllowingStateLoss()
    }

    override fun onError(message: String) = toast(message)

    override fun showSelectedImage(path: String) {
        Picasso.get()
            .load("file://$path")
            .placeholder(R.mipmap.ic_launcher_round)
            .fit()
            .centerInside()
            .into(image_post_content)


        btn_post_submit.run {
            isEnabled = true
            setOnClickListener {

                if (!edit_post_caption.text.toString().isBlank()) {
                    controller.uploadPost(
                        Post(
                            imagePath = path,
                            caption = edit_post_caption.text.toString(),
                            location = text_location_address.text.toString(),
                            likeCount = 0,
                            commentCount = 0
                        )
                    )

                } else {
                    onError("Please enter a caption that describes your post the best!")
                }
            }
        }
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
        fun newInstance() = PostBottomSheet()
    }

}