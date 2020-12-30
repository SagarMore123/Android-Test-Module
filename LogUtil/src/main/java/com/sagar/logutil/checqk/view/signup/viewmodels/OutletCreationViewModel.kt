package com.sagar.logutil.checqk.view.signup.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.net.Uri
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.opposfeapp.utils.SingleLiveEvent
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.ErrorCheckUtils
import com.sagar.logutil.checqk.utils.GenericBaseObservable
import com.sagar.logutil.checqk.utils.Utils
import com.sagar.logutil.checqk.view.login.UserLoginActivity
import com.sagar.logutil.checqk.view.login.remote.UserRepository

class OutletCreationViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var userRepository: UserRepository
) : GenericBaseObservable(application, owner, view) {

    lateinit var error: String
    var showProgressBar = MutableLiveData<Boolean>(false)
    var outletLogoErrorMsg = MutableLiveData<String>("")
    var outletLogoVisible = MutableLiveData<Boolean>(false)
    var outletOwnerLayoutVisible = MutableLiveData<Boolean>(true)
    var uri: Uri? = null
    var restaurantMasterDTO = RestaurantMasterDTO()
    var displayRestaurantMasterDTO = DisplayRestaurantMasterDTO()
    private var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)

    var continuePressed = MutableLiveData<Boolean>(false)
    private val onOutletCreationContinueClick = SingleLiveEvent<Void>()

    var outLetId = 0L


    init {

        outLetId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L
        if (outLetId != null && outLetId != 0L) {
            populateData()
        }

    }

    private fun populateData() {

        userRepository.fetchRestaurantDetails(outLetId,
            object : IDataSourceCallback<RestaurantMasterDTO> {

                override fun onDataFound(data: RestaurantMasterDTO) {
                    showProgressBar.value = false
                    restaurantMasterDTO = data
                    restaurantMasterDTO.restaurantProfileImage = null

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

    fun onContinueClick() {
        onOutletCreationContinueClick.call()
    }

    fun getmOnContinueClick(): SingleLiveEvent<Void> {
        return onOutletCreationContinueClick
    }


    fun onLoginClick() {
        Constants.changeActivity<UserLoginActivity>(activity)
    }

    fun outletCreationValidations(): Boolean {

        var isValid = true

        if (uri != null) {
            val imageDTO = ImageDTO()
            imageDTO.documentName = "LOGO_IMG_" + System.currentTimeMillis() + ".jpg"
            imageDTO.base64 = Utils.getBitmapFromUri(application, uri)
                ?.let { Utils.convertBitmapToBase64(it) }
            restaurantMasterDTO.restaurantProfileImage = imageDTO
        } else {
            isValid = false
            outletLogoErrorMsg.value = "Please upload logo"
        }

        if (ErrorCheckUtils.validateWithCommaNDot(displayRestaurantMasterDTO.outletName)) {
            isValid = false
            displayRestaurantMasterDTO.outletNameErrorMsg.value = "Please enter Outlet Name"
        } else {
            restaurantMasterDTO.outletName =
                displayRestaurantMasterDTO.outletName.value.toString().trim()
        }

        return isValid
    }


    fun outletCreationAdditionalDataValidations(): Boolean {

        var isValid = true


        if (ErrorCheckUtils.validateWithCommaNDot(displayRestaurantMasterDTO.outletMobileCode)
            && ErrorCheckUtils.validateWithCommaNDot(displayRestaurantMasterDTO.outletMobileNo)
        ) {
            isValid = false
            displayRestaurantMasterDTO.outletMobileNoErrorMsg.value =
                "Please enter valid mobile number"
        } else if (ErrorCheckUtils.validateWithCommaNDot(displayRestaurantMasterDTO.outletMobileCode)) {
            isValid = false
            displayRestaurantMasterDTO.outletMobileNoErrorMsg.value = "Please enter country code"
        } else if (ErrorCheckUtils.checkValidMobile(displayRestaurantMasterDTO.outletMobileNo.value)
                .isNotEmpty()
        ) {
            isValid = false
            displayRestaurantMasterDTO.outletMobileNoErrorMsg.value =
                ErrorCheckUtils.checkValidMobile(displayRestaurantMasterDTO.outletMobileNo.value)
        } else {
            restaurantMasterDTO.communicationInfoDTO?.phoneNos = arrayListOf()
            restaurantMasterDTO.communicationInfoDTO?.phoneNos?.add(
                displayRestaurantMasterDTO.outletMobileCode.value.toString()
                    .trim() + displayRestaurantMasterDTO.outletMobileNo.value.toString().trim()
            )
        }


        if (ErrorCheckUtils.checkValidEmail(displayRestaurantMasterDTO.outletEmail.value)
                .isNotEmpty()
        ) {
            isValid = false
            displayRestaurantMasterDTO.outletEmailErrorMsg.value =
                ErrorCheckUtils.checkValidEmail(displayRestaurantMasterDTO.outletEmail.value)
        } else {
            restaurantMasterDTO.communicationInfoDTO?.emailIds = arrayListOf()
            restaurantMasterDTO.communicationInfoDTO?.emailIds?.add(
                displayRestaurantMasterDTO.outletEmail.value.toString().trim()
            )
        }

        // Is Owner / User
        if (displayRestaurantMasterDTO.isOutletOwner.value == true) {

            val userDTO = UserDetailsDTO()
            if (ErrorCheckUtils.validateWithCommaNDot(displayRestaurantMasterDTO.userMobileCode)
                && ErrorCheckUtils.validateWithCommaNDot(displayRestaurantMasterDTO.userMobileNo)
            ) {
                isValid = false
                displayRestaurantMasterDTO.userMobileNoErrorMsg.value =
                    "Please enter valid mobile number"
            } else if (ErrorCheckUtils.validateWithCommaNDot(displayRestaurantMasterDTO.userMobileCode)) {
                isValid = false
                displayRestaurantMasterDTO.userMobileNoErrorMsg.value = "Please enter country code"
            } else if (ErrorCheckUtils.checkValidMobile(displayRestaurantMasterDTO.userMobileNo.value)
                    .isNotEmpty()
            ) {
                isValid = false
                displayRestaurantMasterDTO.userMobileNoErrorMsg.value =
                    ErrorCheckUtils.checkValidMobile(displayRestaurantMasterDTO.userMobileNo.value)
            } else {
                userDTO.mobileCode =
                    displayRestaurantMasterDTO.userMobileCode.value.toString().trim()
                userDTO.mobileNo = displayRestaurantMasterDTO.userMobileNo.value.toString().trim()


            }


            if (ErrorCheckUtils.checkValidEmail(displayRestaurantMasterDTO.userEmail.value)
                    .isNotEmpty()
            ) {
                isValid = false
                displayRestaurantMasterDTO.userEmailErrorMsg.value =
                    ErrorCheckUtils.checkValidEmail(displayRestaurantMasterDTO.userEmail.value)
            } else {
                userDTO.emailAddress =
                    displayRestaurantMasterDTO.userEmail.value.toString().trim()
            }

            if (ErrorCheckUtils.validateWithCommaNDot(displayRestaurantMasterDTO.userFirstName)) {
                isValid = false
                displayRestaurantMasterDTO.userFirstNameErrorMsg.value =
                    "Please enter First Name"
            } else {
                userDTO.userFirstName = displayRestaurantMasterDTO.userFirstName.value
            }

            if (ErrorCheckUtils.validateWithCommaNDot(displayRestaurantMasterDTO.userLastName)) {
                isValid = false
                displayRestaurantMasterDTO.userLastNameErrorMsg.value =
                    "Please enter Last Name"
            } else {
                userDTO.userLastName = displayRestaurantMasterDTO.userLastName.value
                userDTO.fullName =
                    displayRestaurantMasterDTO.userFirstName.value + " " + displayRestaurantMasterDTO.userLastName.value
            }

            restaurantMasterDTO.userDetailsDto = userDTO
        } else {
            restaurantMasterDTO.userDetailsDto = null
        }

        restaurantMasterDTO.isOutletOpen = displayRestaurantMasterDTO.isOutletOpen.value

        return isValid
    }

    fun onSaveClick() {


        restaurantMasterDTO.communicationInfoDTO?.outletId = restaurantMasterDTO.outletId

        restaurantMasterDTO.status =
            RestaurantInfoStatusEnum.AWAITING_APPROVAL.name // Setting status for the creation of restaurant/outlet

        showProgressBar.value = true
        userRepository.saveRestaurantDetailsByVisitor(restaurantMasterDTO,
            object : IDataSourceCallback<CommonResponseDTO> {
                override fun onDataFound(data: CommonResponseDTO) {
                    showProgressBar.value = false
                    if (data.outletId != null) {
                        restaurantMasterDTO.outletId = data.outletId

                        sharedPreferences.edit().putString(
                            Constants.OUTLET_ID,
                            Constants.encrypt(data.outletId.toString())
                        ).apply()

                    }
                    getmSnackbar().value = data.success?.message ?: ""
                    continuePressed.value = true
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })

    }


}