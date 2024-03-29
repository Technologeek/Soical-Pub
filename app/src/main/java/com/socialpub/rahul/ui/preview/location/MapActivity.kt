package com.socialpub.rahul.ui.preview.location

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.socialpub.rahul.R
import com.socialpub.rahul.base.BaseActivity
import com.socialpub.rahul.data.model.Post
import com.socialpub.rahul.di.Injector
import com.socialpub.rahul.service.android.location.LocationManger
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_map.*
import timber.log.Timber

class MapActivity : BaseActivity(), MapContract.View {


    override val contentLayout: Int
        get() = R.layout.activity_map


    lateinit var controller: MapController

    override fun setup() {
        initToolbar()
        controller = MapController(this)
        controller.onStart()

        val postId = intent.getStringExtra("postId")
        controller.getPost(postId)
    }


    lateinit var locationManager: LocationManger
    override fun attachActions() {
        initMap()
    }

    lateinit var preiewPost: Post
    override fun showPost(post: Post) {
        with(post) {
            preiewPost = post

            text_post_caption.text = caption

            Picasso.get()
                .load(userAvatar)
                .placeholder(R.mipmap.ic_launcher)
                .transform(CropCircleTransformation())
                .into(image_post_avatar)

            Picasso.get()
                .load(imageUrl)
                .transform(CropCircleTransformation())
                .placeholder(R.drawable.ic_empty_image)
                .into(image_post_preview)


            updateMapCamera(LatLng(location.lat, location.long))

        }
    }

    private var googleMap: GoogleMap? = null
    private fun initMap() {

        locationManager = Injector.locationManager()

        val mapFragment = SupportMapFragment()

        supportFragmentManager
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

        val displayLocation = try {
            address.get(0).getAddressLine(0)
        } catch (e: IndexOutOfBoundsException) {
            Timber.e(e)
            "Location"
        }

        text_post_location.text = displayLocation

        val opiton = MarkerOptions()
            .position(latLng)
            .title(preiewPost.username)
            .snippet(preiewPost.caption)

        googleMap?.addMarker(opiton)

    }

    override fun onDestroy() {
        super.onDestroy()
        val mapFragement = supportFragmentManager.findFragmentByTag("post_map")
        if (mapFragement != null)
            supportFragmentManager
                .beginTransaction()
                .remove(mapFragement)
                .commitNowAllowingStateLoss()
    }

    override fun onError(message: String) = toast(message)

    override fun showLoading(message: String) {
        showProgress(message)
    }

    override fun hideLoading() {
        hideProgress()
    }

    private fun initToolbar() = setSupportActionBar(toolbar_map).let {
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
