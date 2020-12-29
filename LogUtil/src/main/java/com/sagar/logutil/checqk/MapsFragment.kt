package com.sagar.logutil.checqk

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.sagar.logutil.R
import com.sagar.logutil.databinding.FragmentMapsBinding
import java.io.IOException
import java.util.*

class MapsFragment : Fragment() {


    private lateinit var mGoogleMap: GoogleMap

    private var circleRadius = 0
    private var isMoving = false
//    private var mapFragment: SupportMapFragment? = null

    private lateinit var binding: FragmentMapsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_maps,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
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
/*
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
*/

        mGoogleMap = googleMap
        mGoogleMap.setOnCameraIdleListener {
            val latLng = mGoogleMap.cameraPosition.target
            getAddress(latLng.latitude, latLng.longitude)
        }

        MapsInitializer.initialize(this.requireActivity())
        moveMapCamera()

    }


    private fun moveMapCamera() {
        val center = CameraUpdateFactory.newLatLng(LatLng(19.087016062268965, 72.87757895886898))
        val zoom = CameraUpdateFactory.zoomTo(13f)
        mGoogleMap.moveCamera(center)
        mGoogleMap.animateCamera(zoom)
    }

    fun getAddress(latitude: Double, longitude: Double): Address? {
        val addresses: List<Address>
        val geoCoder = Geocoder(this.context, Locale.getDefault())
        try {
            addresses = geoCoder.getFromLocation(
                latitude,
                longitude,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return if (addresses.isNotEmpty()) {
                addresses[0]
            } else {
                null
            }
        } catch (e: IOException) {

        }
        return null
    }


}