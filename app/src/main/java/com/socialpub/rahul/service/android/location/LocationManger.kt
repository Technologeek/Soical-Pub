package com.socialpub.rahul.service.android.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task

class LocationManger private constructor(
    private val context: Context
) {

    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun getLastLocation(): Task<Location>? = try {
        fusedLocationProviderClient.lastLocation
    } catch (e: SecurityException) {
        null
    }

    fun getAddressFromLocation(latLng: LatLng): List<Address> {
        with(latLng) {
            val geocoder = Geocoder(context)
            return try {
                geocoder.getFromLocation(latitude, longitude, 1)
            } catch (e: NullPointerException) {
                emptyList<Address>()
            }
        }
    }

    fun makeMarker(latLng: LatLng) =
        MarkerOptions().position(latLng)
            .title("Tag here?")

    fun makeCustomMarker(latLng: LatLng) =
        MarkerOptions().position(latLng)
            .title("Tag here?")

    companion object {

        @Volatile
        private var instance: LocationManger? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: LocationManger(context).also { instance = it }
        }

    }

}