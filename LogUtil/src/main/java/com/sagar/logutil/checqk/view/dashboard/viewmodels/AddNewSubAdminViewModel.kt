package com.astrika.checqk.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.checqk.model.AddressMasterDTO
import com.astrika.checqk.model.DisplayCommunicationInfoDTO
import com.astrika.checqk.model.ImageDTO
import com.astrika.checqk.model.SubAdminDTO
import com.astrika.checqk.network.network_utils.IDataSourceCallback
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.ErrorCheckUtils
import com.astrika.checqk.utils.GenericBaseObservable
import com.astrika.checqk.utils.Utils
import com.astrika.checqk.utils.location.AddressWithLatLangDTO
import com.astrika.checqk.view.login.remote.UserRepository
import com.example.opposfeapp.utils.SingleLiveEvent

class AddNewSubAdminViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var userRepository: UserRepository
) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>()
    private var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)
    private val onAccessInfoClickEvent = SingleLiveEvent<Void>()
    var displayCommunicationInfoDTO = DisplayCommunicationInfoDTO()
    var accessInfoId = 0L
    var accessInfoValue = MutableLiveData<String>("")
    var fullName = MutableLiveData<String>("")
    var emailAddress = MutableLiveData<String>("")
    var mobileCode = MutableLiveData<String>("")
    var mobileNumber = MutableLiveData<String>("")
    var cancelDialog = MutableLiveData<Boolean>(false)
    var profileImagePath = ""
    var profileImageId = 0L
    private var profileImageBase64: String? = ""
    var fileName: String? = ""
    var fullNameErrorMsg = MutableLiveData<String>("")
    var emailAddressErrorMsg = MutableLiveData<String>("")
    var mobileNumberErrorMsg = MutableLiveData<String>("")
    var accessRoleErrorMsg = MutableLiveData<String>("")
    var countryCodeErrorMsg = MutableLiveData<String>("")
    var userId:Long ?= 0

    private val onCountryClick = SingleLiveEvent<Void>()
    private val onStateClick = SingleLiveEvent<Void>()
    private val onCityClick = SingleLiveEvent<Void>()
    private val onAreaClick = SingleLiveEvent<Void>()
    private var outLetId = 0L
    private var accessRolesList = arrayListOf<Long>()

    //    private val onSocialMediaIconClick = SingleLiveEvent<Void>()
    private val onChangeLocationClick = SingleLiveEvent<Void>()
    var subAdminDTO = SubAdminDTO()
    lateinit var error: String

    init {
        outLetId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L
//           populateCommunicationInfo()

        var addressWithLatLangDTO = Utils.getLocationAddressDTO(
            Constants.decrypt(
                sharedPreferences.getString(
                    Constants.LOCATION_ADDRESS_KEY,
                    ""
                )
            )
        )

        displayCommunicationInfoDTO.addressLine1.value = addressWithLatLangDTO?.address ?: ""
        displayCommunicationInfoDTO.pincodeNo.value = addressWithLatLangDTO?.pincode ?: ""
        if (addressWithLatLangDTO?.latitude.toString() == "null") {
            displayCommunicationInfoDTO.latitude.value = ""
        } else {
            displayCommunicationInfoDTO.latitude.value =
                addressWithLatLangDTO?.latitude.toString() ?: ""
        }
        if (addressWithLatLangDTO?.longitude.toString() == "null") {
            displayCommunicationInfoDTO.longitude.value = ""
        } else {
            displayCommunicationInfoDTO.longitude.value =
                addressWithLatLangDTO?.longitude.toString() ?: ""
        }
    }

    fun populateData(subAdminDTO: SubAdminDTO?) {
        if (subAdminDTO != null) {
            emailAddress.value = subAdminDTO.emailAddress
            fullName.value = subAdminDTO.fullName
            mobileCode.value = subAdminDTO.mobileCCode
            mobileNumber.value = subAdminDTO.mobileNo
            if(userId != null){
                userId = subAdminDTO.userId
            }
            displayCommunicationInfoDTO.addressLine1.value =
                subAdminDTO.outletAddress?.addressLine1 ?: ""
            displayCommunicationInfoDTO.addressLine2.value =
                subAdminDTO.outletAddress?.addressLine2 ?: ""

            displayCommunicationInfoDTO.countryId =
                subAdminDTO.outletAddress?.countryId ?: 0
            displayCommunicationInfoDTO.countryName.value =
                subAdminDTO.outletAddress?.countryName ?: ""

            displayCommunicationInfoDTO.stateId =
                subAdminDTO.outletAddress?.stateId ?: 0
            displayCommunicationInfoDTO.stateName.value =
                subAdminDTO.outletAddress?.stateName ?: ""

            displayCommunicationInfoDTO.cityId =
                subAdminDTO.outletAddress?.cityId ?: 0
            displayCommunicationInfoDTO.cityName.value =
                subAdminDTO.outletAddress?.cityName ?: ""

            displayCommunicationInfoDTO.areaId =
                subAdminDTO.outletAddress?.areaId ?: 0
            displayCommunicationInfoDTO.areaName.value =
                subAdminDTO.outletAddress?.areaName ?: ""

            displayCommunicationInfoDTO.pincodeNo.value =
                subAdminDTO.outletAddress?.pincode.toString() ?: ""
            displayCommunicationInfoDTO.landmark.value =
                subAdminDTO.outletAddress?.landmark ?: ""
            displayCommunicationInfoDTO.latitude.value =
                subAdminDTO.outletAddress?.latitude ?: ""
            displayCommunicationInfoDTO.longitude.value =
                subAdminDTO.outletAddress?.longitude ?: ""

            if (subAdminDTO.roles != null) {
                if (subAdminDTO.roles?.size!! > 0) {
                    for (it in subAdminDTO.roles!!) {
                        accessInfoValue.value = it.roleName
                    }
                }
            }

            if (subAdminDTO.profileImage != null) {
                if(subAdminDTO.profileImage?.id != null){
                    profileImageId = subAdminDTO.profileImage?.id!!
                }
                if(subAdminDTO.profileImage?.path != null){
                    profileImagePath = subAdminDTO.profileImage?.path!!
                }
            }

        }
    }


    fun onAccessInfoClick() {
        onAccessInfoClickEvent.call()
        accessInfoId = 0L
        accessRoleErrorMsg.value = ""
    }

    fun getmOnAccessInfoClick(): SingleLiveEvent<Void> {
        return onAccessInfoClickEvent
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

    fun convertUriToBase64(resultUri: Uri?) {
        fileName = "PROFILE_IMG" + System.currentTimeMillis() + ".jpg"
        profileImageBase64 = Utils.getBitmapFromUri(application, resultUri)
            ?.let { Utils.convertBitmapToBase64(it) }
    }

    fun onCancel() {
        cancelDialog.value = true
    }

    fun onSave() {
        if (validations()) {
            //service call for saving the data
            showProgressBar.value = true
            userRepository.saveOutletSubAdmin(subAdminDTO, object : IDataSourceCallback<String> {

                override fun onDataFound(data: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = data
                    Handler(Looper.getMainLooper()).postDelayed({
                        cancelDialog.value = true
                    }, 1000)
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })
        }
    }

    private fun validations(): Boolean {
        var isValid: Boolean = true

        if (userId != 0L) {
            subAdminDTO.userId = userId
        }

        if (outLetId != 0L) {
            subAdminDTO.outletId = outLetId
        } else {
            isValid = false
        }

        if (validateFirstName()) {
            subAdminDTO.fullName = fullName.value
        } else {
            isValid = false
        }

        if (validateEmail()) {
            subAdminDTO.emailAddress = emailAddress.value
        } else {
            isValid = false
        }

        if (validateCountryCode()) {
            subAdminDTO.mobileCCode = mobileCode.value
        } else {
            isValid = false
        }

        if (validateMobileNumber()) {
            subAdminDTO.mobileNo = mobileNumber.value
        } else {
            isValid = false
        }

        if(profileImageId != 0L){
            //image already present
            subAdminDTO.profileImage?.id = profileImageId
            subAdminDTO.profileImage?.path = profileImagePath
        }else{
            //image not present
            if (validateImage()) {
                val imageDTO = ImageDTO()
                imageDTO.base64 = profileImageBase64
                imageDTO.documentName = fileName
                subAdminDTO.profileImage?.base64 = profileImageBase64
                subAdminDTO.profileImage = imageDTO
            } else {
                isValid = false
            }
        }

        if (validateAccessRole()) {
            accessRolesList = arrayListOf()
            accessRolesList.add(accessInfoId)
            subAdminDTO.accessRoles = accessRolesList
        } else {
            isValid = false
        }

        val addressMasterDTO = AddressMasterDTO()

        // Address Line 1
        if (ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.addressLine1)) {
            isValid = false
            displayCommunicationInfoDTO.addressLine1ErrorMsg.value = "Please enter address line 1"
        } else {
            addressMasterDTO.addressLine1 =
                displayCommunicationInfoDTO.addressLine1.value.toString().trim()
        }

        // Address Line 2
        if (!ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.addressLine2)) {
            addressMasterDTO.addressLine2 =
                displayCommunicationInfoDTO.addressLine2.value.toString().trim()
        }

        // Country
        if (ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.countryName)
            || displayCommunicationInfoDTO.countryId == 0L
        ) {
            isValid = false
            displayCommunicationInfoDTO.countryErrorMsg.value = "Please select country"
        } else {
            addressMasterDTO.countryId = displayCommunicationInfoDTO.countryId
            addressMasterDTO.countryName =
                displayCommunicationInfoDTO.countryName.value.toString().trim()

        }

        // State
        if (ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.stateName)
            || displayCommunicationInfoDTO.stateId == 0L
        ) {
            isValid = false
            displayCommunicationInfoDTO.stateErrorMsg.value = "Please select state"
        } else {
            addressMasterDTO.stateId = displayCommunicationInfoDTO.stateId
            addressMasterDTO.stateName =
                displayCommunicationInfoDTO.stateName.value.toString().trim()
        }

        // City
        if (ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.cityName)
            || displayCommunicationInfoDTO.cityId == 0L
        ) {
            isValid = false
            displayCommunicationInfoDTO.cityErrorMsg.value = "Please select city"
        } else {
            addressMasterDTO.cityId = displayCommunicationInfoDTO.cityId
            addressMasterDTO.cityName =
                displayCommunicationInfoDTO.cityName.value.toString().trim()
        }

        // Area
        if (ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.areaName)
            || displayCommunicationInfoDTO.areaId == 0L
        ) {
            isValid = false
            displayCommunicationInfoDTO.areaErrorMsg.value = "Please select area"
        } else {
            addressMasterDTO.areaId = displayCommunicationInfoDTO.areaId
            addressMasterDTO.areaName =
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
            addressMasterDTO.pincode =
                displayCommunicationInfoDTO.pincodeNo.value?.toLong() ?: 0
        }

        // Landmark
        if (!ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.landmark)) {
            addressMasterDTO.landmark =
                displayCommunicationInfoDTO.landmark.value.toString().trim()
        }

        // Latitude
        if (ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.latitude)) {
            isValid = false
            displayCommunicationInfoDTO.latitudeErrorMsg.value = "Please enter Latitude"
        } else {
            addressMasterDTO.latitude =
                displayCommunicationInfoDTO.latitude.value.toString().trim()
        }

        // Longitude
        if (ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.longitude)) {
            isValid = false
            displayCommunicationInfoDTO.longitudeErrorMsg.value = "Please enter Longitude"
        } else {
            addressMasterDTO.longitude =
                displayCommunicationInfoDTO.longitude.value.toString().trim()
        }

        subAdminDTO.outletAddress = addressMasterDTO

        return isValid
    }


    private fun validateFirstName(): Boolean {
        error = ErrorCheckUtils.checkValidFirstName(fullName.value)
        if (error.isNotEmpty()) {
            fullNameErrorMsg.value = error
            return false
        } else {
            fullNameErrorMsg.value = ""
        }
        return true
    }

    private fun validateEmail(): Boolean {
        error = ErrorCheckUtils.checkValidEmail(emailAddress.value)
        if (error.isNotEmpty()) {
            emailAddressErrorMsg.value = error
            return false
        } else {
            emailAddressErrorMsg.value = ""
        }
        return true
    }

    private fun validateCountryCode(): Boolean {
        var isValid = true
        if (mobileCode.value != null && !mobileCode.value.isNullOrBlank()) {
            if (mobileCode.value!!.length <= 4) {
                isValid = true
                countryCodeErrorMsg.value = ""
            }
        } else {
            countryCodeErrorMsg.value = "Invalid Code"
            isValid = false
        }
        return isValid
    }

    private fun validateMobileNumber(): Boolean {
        error = ErrorCheckUtils.checkValidMobile(mobileNumber.value)
        if (error.isNotEmpty()) {
            mobileNumberErrorMsg.value = error
            return false
        } else {
            mobileNumberErrorMsg.value = ""
        }
        return true
    }

    private fun validateAccessRole(): Boolean {
        return if (accessInfoId != 0L) {
            accessRoleErrorMsg.value = ""
            true
        } else {
            accessRoleErrorMsg.value = "Please select an access role"
            false
        }

    }


    private fun validateImage(): Boolean {
        return if (profileImageBase64.isNullOrBlank()) {
            getmSnackbar().value = "Please upload an image"
            false
        } else {
            true
        }

    }

}