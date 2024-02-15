package com.emines_transportation.permissions

import android.content.Context
import android.util.Log
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

object MyPermissions {

    var isLocationEnable: Boolean = false
    var isReadStorageEnable: Boolean = false
    var isCameraAccessEnable: Boolean = false
    fun getLocationPermission(context: Context) {
        Dexter.withContext(context).withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    isLocationEnable = true
                    Log.d("LocationPermission", "onPermissionGranted: Granted..")
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Log.d("LocationPermission", "onPermissionGranted: Denied..")
                    isLocationEnable = false
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    isLocationEnable = false
                    Log.d("LocationPermission", "onPermissionGranted: RationalShould")
                }

            }).check()


    }

    fun getReadStoragePermission(context: Context) {
        Dexter.withContext(context)
            .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    isReadStorageEnable = true
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    isReadStorageEnable = false
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    isReadStorageEnable = false
                }
            }).check()


    }

    fun getCameraAccessPermission(context: Context) {
        Dexter.withContext(context).withPermission(android.Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    isCameraAccessEnable = true
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    isCameraAccessEnable = false
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    isCameraAccessEnable = false
                }
            }).check()


    }

    //private var currentLocation: Location? = null
    ////lateinit var locationManager: LocationManager
    //locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    //------------------------------------------------------//
    // val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)


}