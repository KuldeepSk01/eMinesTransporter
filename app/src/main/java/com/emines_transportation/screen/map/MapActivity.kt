package com.emines_transportation.screen.map

import androidx.databinding.ViewDataBinding
import com.emines_transportation.R
import com.emines_transportation.base.BaseActivity
import com.emines_transportation.databinding.ActivityMapBinding

class MapActivity : BaseActivity() {
    private lateinit var mapBinding: ActivityMapBinding

    /*, OnMapReadyCallback {
    private lateinit var mapBinding: ActivityMapBinding
    private lateinit var mGoogleMap: GoogleMap
    override val layoutId: Int
        get() = R.layout.activity_map

    override fun onCreateInit(binding: ViewDataBinding?) {
        mapBinding = binding as ActivityMapBinding
        if (MyPermissions.isLocationEnable) {
            launchActivity(MainActivity::class.java)
            finish()
        } else {
            Dexter.withContext(this)
                .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        MyPermissions.isLocationEnable = true
                        Log.d("LocationPermission", "onPermissionGranted: Granted..")
                        launchActivity(MainActivity::class.java)
                        finish()
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Log.d("LocationPermission", "onPermissionGranted: Denied..")
                        MyPermissions.isLocationEnable = false
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                    ) {
                        MyPermissions.isLocationEnable = false
                        Log.d("LocationPermission", "onPermissionGranted: RationalShould")
                    }

                }).check()
        }

        val supportMapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this@MapActivity)
    }

    override fun onMapReady(p0: GoogleMap) {
        mGoogleMap = p0


    }
}*/
    override val layoutId: Int
        get() = R.layout.activity_map

    override fun onCreateInit(binding: ViewDataBinding?) {
        mapBinding = binding as ActivityMapBinding
    }

}