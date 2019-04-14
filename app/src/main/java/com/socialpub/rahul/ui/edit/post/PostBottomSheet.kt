package com.socialpub.rahul.ui.edit.post

import android.content.Intent
import android.location.Location
import android.view.View
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseBottomSheet
import com.socialpub.rahul.di.Injector
import com.socialpub.rahul.service.android.location.LocationManger
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.bottom_sheet_upload_post.*
import timber.log.Timber
import java.lang.Exception


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


//        image_profile.run {
//
//            Picasso.get()
//                .load(profileUrl)
//                .placeholder(R.mipmap.ic_launcher_round)
//                .fit()
//                .centerInside()
//                .into(this)
//
//            setOnClickListener {
//                ImagePicker.create(this@PostBottomSheet)
//                    .returnMode(ReturnMode.ALL)
//                    .includeVideo(false)
//                    .single()
//                    .showCamera(true)
//                    .start(PostContract.Controller.Const.IMAGE_PICKER_REQUEST)
//            }
//        }

        btn_upload_post_close.setOnClickListener { this.dismiss() }
        // btn_update_profile.setOnClickListener { controller.updateUserProfile(edit_new_username.text.toString()) }
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
        fun newInstance() = PostBottomSheet()
    }

}