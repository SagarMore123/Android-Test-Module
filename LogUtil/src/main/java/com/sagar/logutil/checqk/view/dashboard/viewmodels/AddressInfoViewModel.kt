package com.astrika.checqk.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.checqk.master_controller.source.MasterRepository
import com.astrika.checqk.model.AddressMasterDTO
import com.astrika.checqk.model.CommunicationInfoDTO
import com.astrika.checqk.model.DisplayCommunicationInfoDTO
import com.astrika.checqk.model.SocialMediaDTO
import com.astrika.checqk.network.network_utils.IDataSourceCallback
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.ErrorCheckUtils
import com.astrika.checqk.utils.GenericBaseObservable
import com.astrika.checqk.utils.Utils
import com.astrika.checqk.utils.location.AddressWithLatLangDTO
import com.astrika.checqk.view.login.UserLoginActivity
import com.astrika.checqk.view.login.remote.UserRepository
import com.example.opposfeapp.utils.SingleLiveEvent

class AddressInfoViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var masterRepository: MasterRepository,
    private var userRepository: UserRepository
) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>()
    private var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)
    private var outLetId = 0L
    var isNext = MutableLiveData<Boolean>(false)


    var addressInfoLayoutVisible = MutableLiveData<Boolean>(false)
    var mobileLayoutVisible = MutableLiveData<Boolean>(false)
    var emailAddressLayoutVisible = MutableLiveData<Boolean>(false)
    var socialInfoLayoutVisible = MutableLiveData<Boolean>(false)
    var websiteInfoLayoutVisible = MutableLiveData<Boolean>(false)


    var mobileNoArrayList = ArrayList<String>()
    var mobileNoArrayListMutableLiveData = MutableLiveData<ArrayList<String>>()

    var emailAddressArrayList = ArrayList<String>()
    var emailAddressArrayListMutableLiveData = MutableLiveData<ArrayList<String>>()

    var socialMediaArrayList = ArrayList<SocialMediaDTO>()
    var socialMediaListMutableLiveData = MutableLiveData<List<SocialMediaDTO>>()

    var communicationInfoDTO = CommunicationInfoDTO()
    var displayCommunicationInfoDTO = DisplayCommunicationInfoDTO()

    private val onCountryClick = SingleLiveEvent<Void>()
    private val onStateClick = SingleLiveEvent<Void>()
    private val onCityClick = SingleLiveEvent<Void>()
    private val onAreaClick = SingleLiveEvent<Void>()
    private val onSocialMediaIconClick = SingleLiveEvent<Void>()
    private val onChangeLocationClick = SingleLiveEvent<Void>()


    init {
        outLetId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L

        var addressWithLatLangDTO = Utils.getLocationAddressDTO(
            Constants.decrypt(
                sharedPreferences.getString(
                    Constants.LOCATION_ADDRESS_KEY,
                    ""
                )
            )
        )

        if (addressWithLatLangDTO != null) {
            displayCommunicationInfoDTO.addressLine1.value = addressWithLatLangDTO?.address ?: ""
            displayCommunicationInfoDTO.pincodeNo.value = addressWithLatLangDTO?.pincode ?: ""
            displayCommunicationInfoDTO.latitude.value =
                addressWithLatLangDTO?.latitude.toString() ?: ""
            displayCommunicationInfoDTO.longitude.value =
                addressWithLatLangDTO?.longitude.toString() ?: ""
        }

    }

    fun populateCommunicationInfo() {

        showProgressBar.value = true
        userRepository.fetchCommunicationInfo(
            outLetId,
            object : IDataSourceCallback<CommunicationInfoDTO> {

                override fun onDataFound(data: CommunicationInfoDTO) {
                    showProgressBar.value = false
                    communicationInfoDTO = data

                    // address info
                    displayCommunicationInfoDTO.addressLine1.value =
                        communicationInfoDTO.outletAddress?.addressLine1 ?: ""
                    displayCommunicationInfoDTO.addressLine2.value =
                        communicationInfoDTO.outletAddress?.addressLine2 ?: ""

                    displayCommunicationInfoDTO.countryId =
                        communicationInfoDTO.outletAddress?.countryId ?: 0
                    displayCommunicationInfoDTO.countryName.value =
                        communicationInfoDTO.outletAddress?.countryName ?: ""

                    displayCommunicationInfoDTO.stateId =
                        communicationInfoDTO.outletAddress?.stateId ?: 0
                    displayCommunicationInfoDTO.stateName.value =
                        communicationInfoDTO.outletAddress?.stateName ?: ""

                    displayCommunicationInfoDTO.cityId =
                        communicationInfoDTO.outletAddress?.cityId ?: 0
                    displayCommunicationInfoDTO.cityName.value =
                        communicationInfoDTO.outletAddress?.cityName ?: ""

                    displayCommunicationInfoDTO.areaId =
                        communicationInfoDTO.outletAddress?.areaId ?: 0
                    displayCommunicationInfoDTO.areaName.value =
                        communicationInfoDTO.outletAddress?.areaName ?: ""

                    displayCommunicationInfoDTO.pincodeNo.value =
                        communicationInfoDTO.outletAddress?.pincode.toString() ?: ""
                    displayCommunicationInfoDTO.landmark.value =
                        communicationInfoDTO.outletAddress?.landmark ?: ""
                    displayCommunicationInfoDTO.latitude.value =
                        communicationInfoDTO.outletAddress?.latitude ?: ""
                    displayCommunicationInfoDTO.longitude.value =
                        communicationInfoDTO.outletAddress?.longitude ?: ""

                    //Website
                    displayCommunicationInfoDTO.websiteUrl.value = communicationInfoDTO.webUrl ?: ""

                    //Mobiles
                    mobileNoArrayList = communicationInfoDTO.phoneNos ?: arrayListOf()
                    mobileNoArrayListMutableLiveData.value = mobileNoArrayList

                    //Email Addresses
                    emailAddressArrayList = communicationInfoDTO.emailIds ?: arrayListOf()
                    emailAddressArrayListMutableLiveData.value = emailAddressArrayList

                    //Social Media
                    socialMediaArrayList = communicationInfoDTO.socialMediaMappings ?: arrayListOf()
                    socialMediaListMutableLiveData.value = socialMediaArrayList


                }

                override fun onDataNotFound() {
                    showProgressBar.value = false
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })
    }


    fun onAddMobileNoClick() {

        if (!displayCommunicationInfoDTO.countryCode.value.isNullOrBlank() && !displayCommunicationInfoDTO.mobileNo.value.isNullOrBlank()) {

            if (displayCommunicationInfoDTO.mobileNo.value.toString().trim().length < 6) {
                displayCommunicationInfoDTO.mobileErrorMsg.value =
                    "Please enter valid phone or mobile number"
                return
            }
            val mobileNo =
                displayCommunicationInfoDTO.countryCode.value + " " + displayCommunicationInfoDTO.mobileNo.value
            mobileNoArrayList.add(mobileNo)
            mobileNoArrayListMutableLiveData.value = mobileNoArrayList

            displayCommunicationInfoDTO.countryCode.value = ""
            displayCommunicationInfoDTO.mobileNo.value = ""
            displayCommunicationInfoDTO.mobileErrorMsg.value = ""

        } else if (displayCommunicationInfoDTO.countryCode.value.isNullOrBlank()) {
            displayCommunicationInfoDTO.mobileErrorMsg.value = "Please enter country code"
        } else if (displayCommunicationInfoDTO.mobileNo.value.isNullOrBlank()) {
            displayCommunicationInfoDTO.mobileErrorMsg.value = "Please enter phone or mobile number"
        }
    }


    fun onAddEmailAddressClick() {

        if (ErrorCheckUtils.checkValidEmail(displayCommunicationInfoDTO.emailAddress.value)
                .isEmpty()
        ) {

            emailAddressArrayList.add(displayCommunicationInfoDTO.emailAddress.value ?: "")
            emailAddressArrayListMutableLiveData.value = emailAddressArrayList

            displayCommunicationInfoDTO.emailAddress.value = ""
            displayCommunicationInfoDTO.emailAddressErrorMsg.value = ""

        } else {
            displayCommunicationInfoDTO.emailAddressErrorMsg.value =
                ErrorCheckUtils.checkValidEmail(displayCommunicationInfoDTO.emailAddress.value)
        }
    }


    fun onSocialMediaIconClick() {
        onSocialMediaIconClick.call()
    }

    fun getmOnSocialMediaIconClick(): SingleLiveEvent<Void> {
        return onSocialMediaIconClick
    }

    fun onAddSocialMediaClick() {

        if (displayCommunicationInfoDTO.mediumId != 0L && !ErrorCheckUtils.validateWithCommaNDot(
                displayCommunicationInfoDTO.socialMediaLink
            )
        ) {

            val socialMediaDTO = SocialMediaDTO()
            socialMediaDTO.outletId = outLetId
            socialMediaDTO.mediumId = displayCommunicationInfoDTO.mediumId
            socialMediaDTO.mediumIconPath = displayCommunicationInfoDTO.mediumIconPath.get() ?: ""
            socialMediaDTO.url = displayCommunicationInfoDTO.socialMediaLink.value.toString().trim()

            socialMediaArrayList.add(socialMediaDTO)
            socialMediaListMutableLiveData.value = socialMediaArrayList

            displayCommunicationInfoDTO.mediumId = 0L
            displayCommunicationInfoDTO.mediumIconPath.set("")
            displayCommunicationInfoDTO.socialMediaLink.value = ""
            displayCommunicationInfoDTO.socialMediaErrorMsg.value = ""

        } else if (displayCommunicationInfoDTO.mediumId == 0L) {
            displayCommunicationInfoDTO.socialMediaErrorMsg.value = "Please select media icon"
        } else {
            displayCommunicationInfoDTO.socialMediaErrorMsg.value = "Please enter link"
        }
    }


    fun onRemoveStringTagItem(position: Int, adapterType: String) {

        when (adapterType) {

            Constants.MOBILE_NO_STRING_ADAPTER -> {
                mobileNoArrayList.removeAt(position)
                mobileNoArrayListMutableLiveData.value = mobileNoArrayList
            }

            Constants.EMAILS_ADDRESS_STRING_ADAPTER -> {
                emailAddressArrayList.removeAt(position)
                emailAddressArrayListMutableLiveData.value = emailAddressArrayList
            }
        }


    }


    fun onRemoveSocialMediaItem(position: Int) {
        socialMediaArrayList.removeAt(position)
        socialMediaListMutableLiveData.value = socialMediaArrayList
    }


    fun onAddressInfoClick() {
        addressInfoLayoutVisible.value = !addressInfoLayoutVisible.value!!
    }

    fun onMobileInfoClick() {
        mobileLayoutVisible.value = !mobileLayoutVisible.value!!
    }

    fun onEmailAddressClick() {
        emailAddressLayoutVisible.value = !emailAddressLayoutVisible.value!!
    }

    fun onSocialInfoClick() {
        socialInfoLayoutVisible.value = !socialInfoLayoutVisible.value!!
    }

    fun onWebsiteInfoClick() {
        websiteInfoLayoutVisible.value = !websiteInfoLayoutVisible.value!!
    }

    fun onCountryClick() {
        onCountryClick.call()
        displayCommunicationInfoDTO.stateId = 0L
        displayCommunicationInfoDTO.stateName.value = ""
        displayCommunicationInfoDTO.cityId = 0L
        displayCommunicationInfoDTO.cityName.value = ""
        displayCommunicationInfoDTO.areaId = 0L
        displayCommunicationInfoDTO.areaName.value = ""

    }

    fun getmOnCountryClick(): SingleLiveEvent<Void> {
        return onCountryClick
    }

    fun onStateClick() {
        if (displayCommunicationInfoDTO.countryId == 0L) {
            displayCommunicationInfoDTO.countryErrorMsg.value = "Please select country"
        } else {
            onStateClick.call()
            displayCommunicationInfoDTO.cityId = 0L
            displayCommunicationInfoDTO.cityName.value = ""
            displayCommunicationInfoDTO.areaId = 0L
            displayCommunicationInfoDTO.areaName.value = ""

        }
    }

    fun getmOnStateClick(): SingleLiveEvent<Void> {
        return onStateClick
    }


    fun onCityClick() {
        if (displayCommunicationInfoDTO.stateId == 0L) {
            displayCommunicationInfoDTO.stateErrorMsg.value = "Please select state"
        } else {
            onCityClick.call()
            displayCommunicationInfoDTO.areaId = 0L
            displayCommunicationInfoDTO.areaName.value = ""
        }
    }

    fun getmOnCityClick(): SingleLiveEvent<Void> {
        return onCityClick
    }

    fun onAreaClick() {
        if (displayCommunicationInfoDTO.cityId == 0L) {
            displayCommunicationInfoDTO.cityErrorMsg.value = "Please select city"
        } else {
            onAreaClick.call()
        }
    }

    fun getmOnAreaClick(): SingleLiveEvent<Void> {
        return onAreaClick
    }

    fun onChangeLocationClick() {
        onChangeLocationClick.call()
    }

    fun getmOnChangeLocationClick(): SingleLiveEvent<Void> {
        return onChangeLocationClick
    }


    fun validations(): Boolean {
        var isValid = true

        communicationInfoDTO.outletId = outLetId

        val outletAddress = AddressMasterDTO()

        if (communicationInfoDTO.outletAddress != null) {
            outletAddress.addressId = communicationInfoDTO.outletAddress?.addressId ?: 0
            communicationInfoDTO.outletAddressId =
                communicationInfoDTO.outletAddress?.addressId ?: 0
        }

        // Address Line 1
        if (ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.addressLine1)) {
            isValid = false
            displayCommunicationInfoDTO.addressLine1ErrorMsg.value = "Please enter address line 1"
        } else {
            outletAddress.addressLine1 =
                displayCommunicationInfoDTO.addressLine1.value.toString().trim()
        }

        // Address Line 2
        if (!ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.addressLine2)) {
            outletAddress.addressLine2 =
                displayCommunicationInfoDTO.addressLine2.value.toString().trim()
        }

        // Country
        if (ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.countryName)
            || displayCommunicationInfoDTO.countryId == 0L
        ) {
            isValid = false
            displayCommunicationInfoDTO.countryErrorMsg.value = "Please select country"
        } else {
            outletAddress.countryId = displayCommunicationInfoDTO.countryId
            outletAddress.countryName =
                displayCommunicationInfoDTO.countryName.value.toString().trim()

        }

        // State
        if (ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.stateName)
            || displayCommunicationInfoDTO.stateId == 0L
        ) {
            isValid = false
            displayCommunicationInfoDTO.stateErrorMsg.value = "Please select state"
        } else {
            outletAddress.stateId = displayCommunicationInfoDTO.stateId
            outletAddress.stateName =
                displayCommunicationInfoDTO.stateName.value.toString().trim()
        }

        // City
        if (ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.cityName)
            || displayCommunicationInfoDTO.cityId == 0L
        ) {
            isValid = false
            displayCommunicationInfoDTO.cityErrorMsg.value = "Please select city"
        } else {
            outletAddress.cityId = displayCommunicationInfoDTO.cityId
            outletAddress.cityName =
                displayCommunicationInfoDTO.cityName.value.toString().trim()
        }

        // Area
        if (ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.areaName)
            || displayCommunicationInfoDTO.areaId == 0L
        ) {
            isValid = false
            displayCommunicationInfoDTO.areaErrorMsg.value = "Please select area"
        } else {
            outletAddress.areaId = displayCommunicationInfoDTO.areaId
            outletAddress.areaName =
                displayCommunicationInfoDTO.areaName.value.toString().trim()
        }

        // Pincode
        if (ErrorCheckUtils.checkValidPincode(displayCommunicationInfoDTO.pincodeNo.value)
                .isNotEmpty()
        ) {
            isValid = false
            displayCommunicationInfoDTO.pincodeErrorMsg.value =
                ErrorCheckUtils.checkValidPincode(displayCommunicationInfoDTO.pincodeNo.value)
        } else {
            outletAddress.pincode =
                displayCommunicationInfoDTO.pincodeNo.value?.toLong() ?: 0
        }

        // Landmark
        if (!ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.landmark)) {
            outletAddress.landmark =
                displayCommunicationInfoDTO.landmark.value.toString().trim()
        }

        // Latitude
        if (ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.latitude)) {
            isValid = false
            displayCommunicationInfoDTO.latitudeErrorMsg.value = "Please enter Latitude"
        } else {
            outletAddress.latitude =
                displayCommunicationInfoDTO.latitude.value.toString().trim()
        }

        // Longitude
        if (ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.longitude)) {
            isValid = false
            displayCommunicationInfoDTO.longitudeErrorMsg.value = "Please enter Longitude"
        } else {
            outletAddress.longitude =
                displayCommunicationInfoDTO.longitude.value.toString().trim()
        }

        communicationInfoDTO.outletAddress = outletAddress
        communicationInfoDTO.phoneNos = mobileNoArrayList
        communicationInfoDTO.emailIds = emailAddressArrayList
        communicationInfoDTO.socialMediaMappings = socialMediaArrayList

        communicationInfoDTO.webUrl =
            displayCommunicationInfoDTO.websiteUrl.value.toString().trim()


        return isValid
    }

    fun onSaveCommunicationInfoClick() {

        if (validations()) {
            showProgressBar.value = true

            userRepository.saveCommunicationInfo(
                communicationInfoDTO,
                object : IDataSourceCallback<String> {
                    override fun onDataFound(data: String) {
                        showProgressBar.value = false
                        getmSnackbar().value = data
                        isNext.value = true
                    }

                    override fun onError(error: String) {
                        showProgressBar.value = false
                        getmSnackbar().value = error
                    }
                })

        }

    }

    fun onLoginClick() {
        Constants.changeActivity<UserLoginActivity>(activity)
    }

    fun updatedAddressInfo(addressWithLatLangDTO: AddressWithLatLangDTO) {
        if (displayCommunicationInfoDTO.addressLine1.value.isNullOrBlank()) {
            displayCommunicationInfoDTO.addressLine1.value =
                addressWithLatLangDTO.address.toString()
        }
        if (displayCommunicationInfoDTO.pincodeNo.value.isNullOrBlank()) {
            displayCommunicationInfoDTO.pincodeNo.value =
                addressWithLatLangDTO.pincode.toString()
        }

        displayCommunicationInfoDTO.latitude.value =
            addressWithLatLangDTO.latitude.toString()

        displayCommunicationInfoDTO.longitude.value =
            addressWithLatLangDTO.longitude.toString()
    }

}