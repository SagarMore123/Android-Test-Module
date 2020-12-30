package com.sagar.logutil.checqk

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.sagar.logutil.R
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.location.AddressWithLatLangDTO
import com.sagar.logutil.checqk.utils.location.LocationActivity
import com.sagar.logutil.databinding.ActivityMapsBinding


class MapsActivity : LocationActivity() {

    private lateinit var binding: ActivityMapsBinding

    private lateinit var mGoogleMap: GoogleMap
    private var latLng: LatLng = LatLng(19.070963984413137, 72.87751358002424)
    private lateinit var locationActivity: LocationActivity
    private var resultCode = 0

    var addressWithLatLangDTO = AddressWithLatLangDTO()
    private var lat = 0.0
    private var lon = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        locationActivity = this
        setChildActivity(locationActivity)

        binding.fab.setOnClickListener {
            latLng = LatLng(latitude, longitude)
            moveMapCamera()
        }

        resultCode = intent.getIntExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, 0)
        lat = if (!intent.getStringExtra(Constants.LOCATION_LATITUDE_KEY)
                .isNullOrBlank() && intent.getStringExtra(Constants.LOCATION_LATITUDE_KEY) != "null"
        ) {
            intent.getStringExtra(Constants.LOCATION_LATITUDE_KEY)?.toDouble()
                ?: 0.0
        } else {
            19.070963984413137
        }


        lon = if (!intent.getStringExtra(Constants.LOCATION_LONGITUDE_KEY)
                .isNullOrBlank() && intent.getStringExtra(Constants.LOCATION_LONGITUDE_KEY) != "null"
        ) {
            intent.getStringExtra(Constants.LOCATION_LONGITUDE_KEY)?.toDouble()
                ?: 00.0

        } else {
            72.87751358002424
        }


        binding.confirmButton.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(Constants.SELECTED_DROPDOWN_ITEM, addressWithLatLangDTO)
            setResult(resultCode, resultIntent)
            finish()
        }


    }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

/*
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
*/


        mGoogleMap = googleMap
/*

        mGoogleMap.uiSettings.isZoomControlsEnabled = true
        mGoogleMap.uiSettings.isZoomGesturesEnabled = true
        mGoogleMap.uiSettings.isCompassEnabled = true
*/

        latLng = LatLng(lat, lon)

//        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL;
//        googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID;
//        googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE;
//        googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN;

        mGoogleMap.setOnCameraIdleListener {
            latLng = mGoogleMap.cameraPosition.target
            latitude = latLng.latitude
            longitude = latLng.longitude

//            getAddress(latLng.latitude, latLng.longitude)
            addressWithLatLangDTO = address
            binding.locTxt.setText(address.address ?: "")
        }

        MapsInitializer.initialize(this)
        moveMapCamera()

    }

    private fun moveMapCamera() {
        val center = CameraUpdateFactory.newLatLng(latLng)
        val zoom = CameraUpdateFactory.zoomTo(18f)
        mGoogleMap.moveCamera(center)
        mGoogleMap.animateCamera(zoom)
    }


}