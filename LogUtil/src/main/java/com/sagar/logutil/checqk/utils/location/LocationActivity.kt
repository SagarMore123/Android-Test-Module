package com.sagar.logutil.checqk.utils.location

import android.Manifest
import android.app.Activity
import android.content.IntentSender.SendIntentException
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.location.*
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.Utils
import java.io.IOException
import java.util.*

open class LocationActivity : AppCompatActivity(), ConnectionCallbacks,
    OnConnectionFailedListener, OnRequestPermissionsResultCallback,
    PermissionUtils.PermissionResultCallback,
    LocationListener {
    //    private var mActivity: LocationActivity? = null
    private var mActivity: Activity? = null
    private val mEventId: Long? = null

    // Google client to interact with Google API
    private var mGoogleApiClient: GoogleApiClient? = null
    var latitude = 0.0
    var longitude = 0.0
    var permissionUtils: PermissionUtils? = null
    var isPermissionGranted = false
    var btnClicked = false

    var mSharedPreferences: SharedPreferences? = null

    //this method provides which activity is currently in fore ground
    fun setChildActivity(activity: Activity?) {
        mActivity = activity
        mSharedPreferences =
            mActivity?.applicationContext?.let { Constants.getSharedPreferences(it) }
        initCurrentLocation()

    }

    private fun initCurrentLocation() {
        permissionUtils = PermissionUtils(mActivity ?: Activity())
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissionUtils?.checkPermission(
            permissions,
            "Need GPS permission for getting your location",
            1
        )
        if (checkPlayServices(mActivity)) {
            // Building the GoogleApi client
            buildGoogleApiClient()
        }
        location
        if (mLastLocation != null) {
            latitude = mLastLocation?.latitude ?: 0.0
            longitude = mLastLocation?.longitude ?: 0.0
            startLocationUpdates()
        } else {
            //showToast("Couldn't get the location. Make sure location is enabled on the device");
        }
    }

    override fun onConnected(bundle: Bundle?) {
        location
        if (mLastLocation != null) {
            latitude = mLastLocation?.latitude ?: 0.0
            longitude = mLastLocation?.longitude ?: 0.0
            startLocationUpdates()
        }
    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null) {
            mGoogleApiClient?.connect()
        }
    }

    private var mLocationRequest: LocationRequest? = null
    private val UPDATE_INTERVAL: Long = 1000 /* 15 secs */
    private val FASTEST_INTERVAL: Long = 1000 /* 5 secs */
    protected fun startLocationUpdates() {
        mLocationRequest = LocationRequest()
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest?.interval = UPDATE_INTERVAL
        mLocationRequest?.fastestInterval = FASTEST_INTERVAL
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(applicationContext, "Enable Permissions", Toast.LENGTH_LONG).show()
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient, mLocationRequest, this
        )
    }

    override fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude
        address
        mSharedPreferences?.edit()?.putString(
            Constants.LOCATION_ADDRESS_KEY,
            Constants.encrypt(Utils.setLocationAddressDTO(address))
        )?.apply()

    }

    override fun onConnectionSuspended(i: Int) {}
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        // redirects to utils
        permissionUtils?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    override fun PermissionGranted(request_code: Int) {
//        Log.i("PERMISSION", "GRANTED")
        isPermissionGranted = true
    }

    override fun PartialPermissionGranted(
        request_code: Int,
        granted_permissions: ArrayList<String>?
    ) {
//        Log.i("PERMISSION PARTIALLY", "GRANTED")
    }


    override fun PermissionDenied(request_code: Int) {
//        Log.i("PERMISSION", "DENIED")
    }

    override fun NeverAskAgain(request_code: Int) {
//        Log.i("PERMISSION", "NEVER ASK AGAIN")
    }

    fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private val location: Unit
        private get() {
            if (isPermissionGranted) {
                try {
                    mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient)
                } catch (e: SecurityException) {

                }
            }
        }

    @Synchronized
    fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient?.connect()
        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)
        val result =
            LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build())
        result.setResultCallback { locationSettingsResult ->
            val status = locationSettingsResult.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS ->                         // All location settings are satisfied. The client can initialize location requests here
                    location
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    status.startResolutionForResult(
                        mActivity,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (e: SendIntentException) {
                    // Ignore the error.
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
            }
        }
    }//            Toast.makeText(mActivity, "Fetching Your Location...", Toast.LENGTH_SHORT).show();
//            Toast.makeText(mActivity, "It seems Google is not able to fetch your location! Please enable location manually.", Toast.LENGTH_SHORT).show();
//                Toast.makeText(mActivity, "Fetching Your Location...", Toast.LENGTH_SHORT).show();
//                Toast.makeText(mActivity, "\"It seems Google is not able to fetch your location! Please enable location manually.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(mActivity, "Fetching Your Location...", Toast.LENGTH_SHORT).show();
    /*
                 showToast(address + "");
                 Log.e("Address : ", address);
*/
//                    if (btnClicked) {

    // Write ode here for returning data when button is clicked

    //                        btnClicked = false;
//                    }
    val address: AddressWithLatLangDTO
        get() {
            val addressWithLatLangDTO = AddressWithLatLangDTO()
            val locationAddress = getAddress(latitude, longitude)
            if (locationAddress != null) {
                var address = locationAddress.getAddressLine(0)
                if (address != null) {
                    if (locationAddress.subLocality != null) {
                        address = address.substring(
                            0,
                            address.indexOf(locationAddress.subLocality)
                        ) + locationAddress.subLocality
                        var locality = locationAddress.locality
                        val subLocality = locationAddress.subLocality
                        val country = locationAddress.countryName

                        /*
                                            showToast(address + "");
                                            Log.e("Address : ", address);
                        */
                        //                    if (btnClicked) {

                        // Write ode here for returning data when button is clicked
                        if (subLocality != null && subLocality != "") {
                            locality = "$subLocality, $locality"
                        }
                        addressWithLatLangDTO.latitude = latitude
                        addressWithLatLangDTO.longitude = longitude
                        addressWithLatLangDTO.cityName = locality
                        addressWithLatLangDTO.countryName = country
                        addressWithLatLangDTO.pincode = locationAddress.postalCode
                        addressWithLatLangDTO.address = address

                        //                        btnClicked = false;
                        //                    }
                    } else {
                        //                    Toast.makeText(mActivity, "Fetching Your Location...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //                Toast.makeText(mActivity, "Fetching Your Location...", Toast.LENGTH_SHORT).show();
                    //                Toast.makeText(mActivity, "\"It seems Google is not able to fetch your location! Please enable location manually.", Toast.LENGTH_SHORT).show();
                }
            } else {
                //            Toast.makeText(mActivity, "Fetching Your Location...", Toast.LENGTH_SHORT).show();
                //            Toast.makeText(mActivity, "It seems Google is not able to fetch your location! Please enable location manually.", Toast.LENGTH_SHORT).show();
            }
            return addressWithLatLangDTO
        }

    fun getAddress(latitude: Double, longitude: Double): Address? {
        val addresses: List<Address>
        val geoCoder = Geocoder(mActivity, Locale.getDefault())
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

    private fun checkPlayServices(activity: Activity?): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(activity)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(
                    activity, resultCode,
                    PLAY_SERVICES_REQUEST
                ).show()
            } else {
                Toast.makeText(
                    activity,
                    "This device is not supported.", Toast.LENGTH_LONG
                )
                    .show()
                //finish();
            }
            return false
        }
        return true
    }

    companion object {
        private var mLastLocation: Location? = null

        // list of permissions
        val permissions = ArrayList<String>()
        const val PLAY_SERVICES_REQUEST = 1000
        const val REQUEST_CHECK_SETTINGS = 2000
    }
}