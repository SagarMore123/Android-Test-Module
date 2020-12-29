package com.astrika.checqk.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.net.Uri
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.checqk.master_controller.source.MasterRepository
import com.astrika.checqk.model.*
import com.astrika.checqk.network.network_utils.IDataSourceCallback
import com.astrika.checqk.source.DashboardRepository
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.ErrorCheckUtils
import com.astrika.checqk.utils.GenericBaseObservable
import com.astrika.checqk.utils.Utils
import com.astrika.checqk.utils.location.AddressWithLatLangDTO
import com.example.opposfeapp.utils.SingleLiveEvent

class AddStaffUserDialogViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var masterRepository: MasterRepository,
    private var dashboardRepository: DashboardRepository
) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>()
    private var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)

    val onAccessInfoClickEvent = SingleLiveEvent<Void>()
    val onKnownLanguagesClickEvent = SingleLiveEvent<Void>()
    val onDesignationClickEvent = SingleLiveEvent<Void>()

    var staffUserId = 0L
    var displayCommunicationInfoDTO = DisplayCommunicationInfoDTO()
    var accessRoleId = 0L
    var accessRoleValue = MutableLiveData<String>("")
    var firstName = MutableLiveData<String>("")
    var emailAddress = MutableLiveData<String>("")
    var countryCode = MutableLiveData<String>("")
    var mobileNumber = MutableLiveData<String>("")
    var cancelDialog = MutableLiveData<Boolean>(false)
    var profileImagePath = ""
    private var profileImageBase64: String? = ""
    var fileName: String? = ""
    var firstNameErrorMsg = MutableLiveData<String>("")
    var emailAddressErrorMsg = MutableLiveData<String>("")
    var mobileNumberErrorMsg = MutableLiveData<String>("")
    var accessRoleErrorMsg = MutableLiveData<String>("")
    var knownLanguagesErrorMsg = MutableLiveData<String>("")
    var designationErrorMsg = MutableLiveData<String>("")
    var totalExperienceErrorMsg = MutableLiveData<String>("")

    var imageDTO = ImageDTO()

    var aboutUserValue = MutableLiveData<String>("")
    var knowLanguagesIdList = ArrayList<Long>()
    var knowLanguagesValue = MutableLiveData<String>("")
    var designationId = 0L
    var designationValue = MutableLiveData<String>("")
    var totalExperienceValue = MutableLiveData<String>("")
    var withUsSinceValue = MutableLiveData<String>("")

    private val onCountryClick = SingleLiveEvent<Void>()
    private val onStateClick = SingleLiveEvent<Void>()
    private val onCityClick = SingleLiveEvent<Void>()
    private val onAreaClick = SingleLiveEvent<Void>()
    private var outLetId = 0L
    private var accessRolesList = arrayListOf<Long>()
    private var knownLanguagesList = arrayListOf<Long>()

    //    private val onSocialMediaIconClick = SingleLiveEvent<Void>()
    private val onChangeLocationClick = SingleLiveEvent<Void>()
//    var staffUserDTO = UserDTO()
    lateinit var error: String

    var fromTime = MutableLiveData<String>("")
    var toTime = MutableLiveData<String>("")
    var errorMsg = MutableLiveData<String>("")

    var onAddTimeSlotClick = MutableLiveData<Boolean>(false)
    var navToNext = MutableLiveData<Boolean>(false)

    var daysArrayList = ArrayList<DayDTO>()
    var daysListMutableLiveData = MutableLiveData<List<DayDTO>>()

    var resetTimingsArrayList = ArrayList<TimingDTO>()
    var timingArrayList = ArrayList<DayDTO>()
    var timingListMutableLiveData = MutableLiveData<List<DayDTO>>()
    var updatedList = ArrayList<DayDTO>()


    init {
        outLetId = Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L
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

        onResetClick()
    }

    fun getMastersData(){


    }

    fun populateData(subAdminDTO: SubAdminDTO?) {
        if (subAdminDTO != null) {
            emailAddress.value = subAdminDTO.emailAddress
//            firstName.value = subAdminDTO.userFirstName
            mobileNumber.value = subAdminDTO.mobileNo

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
                        accessRoleValue.value = it.roleName
                    }
                }
            }

            if (subAdminDTO.profileImage != null) {
                profileImagePath = subAdminDTO.profileImage?.path!!
            }

        }
    }

    fun onAccessInfoClick() {
        onAccessInfoClickEvent.call()
        accessRoleId = 0L
        accessRoleErrorMsg.value = ""
    }

    fun onKnownLanguagesClick(){
        onKnownLanguagesClickEvent.call()
        knownLanguagesErrorMsg.value = ""
    }

    fun onDesignationClick(){
        onDesignationClickEvent.call()
        designationId = 0L
        designationErrorMsg.value = ""
    }

    fun getmOnAccessInfoClick(): SingleLiveEvent<Void> {
        return onAccessInfoClickEvent
    }

    fun onCountryClick() {
        onCountryClick.call()
        displayCommunicationInfoDTO.stateId = 0L
        displayCommunicationInfoDTO.stateName.value = ""
    }

    fun getmOnCountryClick(): SingleLiveEvent<Void> {
        return onCountryClick
    }

    fun onStateClick() {
        if (displayCommunicationInfoDTO.countryId == 0L) {
            displayCommunicationInfoDTO.countryErrorMsg.value = "Please select country"
        } else {
            onStateClick.call()
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

    private fun initiateDays() {

        daysArrayList.clear()
        daysListMutableLiveData.value = arrayListOf()
        for (item in DaysEnum.values()) {

            val dayDTO = DayDTO()
            dayDTO.dayId = item.id.toLong()
            dayDTO.dayName = item.value
            dayDTO.isMarkAsClosedVisibility = false
            daysArrayList.add(dayDTO)
            timingArrayList.add(dayDTO)
        }

        daysListMutableLiveData.value = daysArrayList
    }

    private fun initiateTimings() {

        timingArrayList.clear()
        for (item in DaysEnum.values()) {

            val dayDTO = DayDTO()
            dayDTO.dayId = item.id.toLong()
            dayDTO.dayName = item.value
            dayDTO.isMarkAsClosedVisibility = false
            timingArrayList.add(dayDTO)
        }

    }


    private fun resetDays() {

        fromTime.value = ""
        toTime.value = ""
        onAddTimeSlotClick.value = false

        for (item in daysArrayList) {
            item.dayIsCheckedOrClosed = false
        }
        daysListMutableLiveData.value = daysArrayList
    }

    private fun populateTimings() {

        /* showProgressBar.value = true
         userRepository.fetchTimings(outLetId, object : IDataSourceCallback<OutletTimingDTO> {

             override fun onDataFound(data: OutletTimingDTO) {
                 showProgressBar.value = false
                 resetTimingsArrayList = data.timings
                 onResetClick()

             }

             override fun onDataNotFound() {
                 showProgressBar.value = false
             }

             override fun onError(error: String) {
                 showProgressBar.value = false
                 getmSnackbar().value = error
             }
         })*/
    }

    fun onResetClick() {
        initiateDays()
        initiateTimings()
        for (times in resetTimingsArrayList) {

            for (item in timingArrayList) {

                if (item.dayId == times.weekDay) {
                    item.timings.add(times)
                }
            }
        }


        updatedList.clear()
        for (item in timingArrayList) {

            if (item.timings.isNotEmpty()) {
                item.dayIsCheckedOrClosed = false
                updatedList.add(item)
            }
        }

        timingListMutableLiveData.value = updatedList

    }

    fun onAddTimeSlotClick() {
        onAddTimeSlotClick.value = true
    }

    fun onAddTime() {

        if (!fromTime.value.isNullOrBlank() && !toTime.value.isNullOrBlank()) {

            var isDayPresent = false
            for (item in daysArrayList) {
                if (item.dayIsCheckedOrClosed) {
                    isDayPresent = true
                    for (item2 in timingArrayList) {
                        if (item.dayId == item2.dayId && !item2.dayIsCheckedOrClosed) {
                            val timingDTO = TimingDTO()
                            timingDTO.opensAt = fromTime.value ?: ""
                            timingDTO.closesAt = toTime.value ?: ""
//                            timingDTO.staffId = staffUserId
                            timingDTO.weekDay = item2.dayId
//                            item2.timings.add(timingDTO)
                            item2.timings = arrayListOf(timingDTO)
                            break
                        }
                    }
                }
            }


            updatedList.clear()
            for (item in timingArrayList) {

                if (item.timings.isNotEmpty()) {
                    item.dayIsCheckedOrClosed = item.dayIsCheckedOrClosed
                    updatedList.add(item)
                }
            }

            timingListMutableLiveData.value = updatedList
            if (!isDayPresent) {
                errorMsg.value = "Please select day, where the time should be added"
            } else {
                resetDays()
                errorMsg.value = ""
            }


        } else if (fromTime.value.isNullOrBlank()) {
            errorMsg.value = "Please enter From time"

        } else if (toTime.value.isNullOrBlank()) {
            errorMsg.value = "Please enter To time"

        }
    }

    fun onDayItemClick(position: Int, dayDTO: DayDTO) {
        daysArrayList[position].dayIsCheckedOrClosed =
            !daysArrayList[position].dayIsCheckedOrClosed
        daysListMutableLiveData.value = daysArrayList
    }

    // Timings section
    fun onMarkAsClosedItemClick(position: Int, dayDTO: DayDTO) {
        updatedList[position] = dayDTO
        timingListMutableLiveData.value = updatedList

        for ((i, item) in timingArrayList.withIndex()) {
            if (item.dayId == dayDTO.dayId) {
                timingArrayList[i] = dayDTO
                daysArrayList[i].dayEnable = !daysArrayList[i].dayEnable
                if (!daysArrayList[i].dayEnable && daysArrayList[i].dayIsCheckedOrClosed) {
                    daysArrayList[i].dayIsCheckedOrClosed = false
                }
            }
        }

        daysListMutableLiveData.value = daysArrayList

    }

    fun onTimingRemoveItemClick(
        position: Int,
        mainContainerPosition: Int,
        timingDTO: TimingDTO
    ) {
        updatedList[mainContainerPosition].timings.remove(updatedList[mainContainerPosition].timings[position])
        if (updatedList[mainContainerPosition].timings.isEmpty()) {
            updatedList.removeAt(mainContainerPosition)
        }

        timingListMutableLiveData.value = updatedList
    }

    fun getStaffUserDetails(staffUserId : Long){
        this.staffUserId = staffUserId
        showProgressBar.value = true
        dashboardRepository.getStaffUserDetails(staffUserId, object : IDataSourceCallback<UserDTO> {
            override fun onDataFound(data: UserDTO) {
                showStaffUserData(data)
                showProgressBar.value = false
            }

            override fun onError(error: String) {
                showProgressBar.value = false
                getmSnackbar().value = error
            }
        })
    }

    fun showStaffUserData(staffUserDTO: UserDTO){

        firstName.value = staffUserDTO.fullName
        emailAddress.value = staffUserDTO.emailAddress
        countryCode.value = staffUserDTO.mobileCCode
        mobileNumber.value = staffUserDTO.mobileNo
        accessRoleId = staffUserDTO.accessRoles?.get(0) ?: 0
        aboutUserValue.value =  staffUserDTO.bio
        knowLanguagesIdList = staffUserDTO.knownLanguages?: arrayListOf()
        designationId = staffUserDTO.designationId?:0
        totalExperienceValue.value = staffUserDTO.totalExperience?:""
        withUsSinceValue.value = staffUserDTO.workingSince?:""

        displayCommunicationInfoDTO.addressLine1.value = staffUserDTO.outletAddress?.addressLine1
        displayCommunicationInfoDTO.addressLine2.value = staffUserDTO.outletAddress?.addressLine2

        displayCommunicationInfoDTO.countryId = staffUserDTO.outletAddress?.countryId?:0
        displayCommunicationInfoDTO.countryName.value = staffUserDTO.outletAddress?.countryName
        displayCommunicationInfoDTO.stateId = staffUserDTO.outletAddress?.stateId?:0
        displayCommunicationInfoDTO.stateName.value = staffUserDTO.outletAddress?.stateName
        displayCommunicationInfoDTO.cityId = staffUserDTO.outletAddress?.cityId?:0
        displayCommunicationInfoDTO.cityName.value = staffUserDTO.outletAddress?.cityName
        displayCommunicationInfoDTO.areaId = staffUserDTO.outletAddress?.areaId?:0
        displayCommunicationInfoDTO.areaName.value = staffUserDTO.outletAddress?.areaName
        displayCommunicationInfoDTO.pincodeNo.value = staffUserDTO.outletAddress?.pincode.toString()
        displayCommunicationInfoDTO.landmark.value = staffUserDTO.outletAddress?.landmark

        if (staffUserDTO?.profileImage != null){
            imageDTO = staffUserDTO?.profileImage?:ImageDTO()
        }

        masterRepository.fetchGroupRolesStaffMasterDataById(staffUserDTO.accessRoles?.get(0)?:0,
            object : IDataSourceCallback<GroupRolesStaffDTO>{
                override fun onDataFound(data: GroupRolesStaffDTO) {
                    accessRoleValue.value = data.name
                }

                override fun onError(error: String) {
                    getmSnackbar().value = "Access role not found"
                }
            })

        masterRepository.fetchKnownLanguagesMasterDataById(staffUserDTO.knownLanguages?.get(0)!!,
            object : IDataSourceCallback<KnownLanguagesDTO>{
                override fun onDataFound(data: KnownLanguagesDTO) {
                    knowLanguagesValue.value = data.languageName
                }

                override fun onError(error: String) {
                    getmSnackbar().value = "Known languages not found"
                }
            })

        masterRepository.fetchDesignationMasterDataById(staffUserDTO.designationId!!,
            object : IDataSourceCallback<DesignationDTO>{
                override fun onDataFound(data: DesignationDTO) {
                    designationValue.value = data.designationName
                }

                override fun onError(error: String) {
                    getmSnackbar().value = "Designation not found"
                }
            })

        resetTimingsArrayList.clear()
        for(time in staffUserDTO.staffTimingDTO!!.timings){
            var timingDTO = TimingDTO()
            timingDTO.timingId = time.timingId
            timingDTO.weekDay = time.weekDay
            timingDTO.opensAt = time.opensAt
            timingDTO.closesAt = time.closesAt

            resetTimingsArrayList.add(
                timingDTO
            )
        }
//                resetTimingsArrayList = data.timings
        onResetClick()
    }


    fun onCancel() {
        cancelDialog.value = true
    }

    fun onSaveClick() {
        validations()
        /*if (validations()) {
            //service call for saving the data
            showProgressBar.value = true
            userRepository.saveOutletSubAdmin(subAdminDTO, object : IDataSourceCallback<String> {

                override fun onDataFound(data: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = data
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })
        }*/
    }

    private fun validations(): Boolean {
        var isValid: Boolean = true

        if(!validateFirstName()){
            isValid = false
        }
        /*else if(!validateEmail()){
            isValid = false
        }else if(!validateCountryCode()){
            isValid = false
        }else if(!validateMobileNumber()){
            isValid = false
        }else if(!validateImage()){
            isValid = false
        }*/
        else if(!validateAccessRole()){
            isValid = false
        }else if(knowLanguagesIdList.isEmpty()){
            knownLanguagesErrorMsg.value = "Please select known Languages"
        }else if(designationId == 0L){
            designationErrorMsg.value = "Please select designation"
        }else if(totalExperienceValue.value!!.isEmpty()){
            totalExperienceErrorMsg.value = "Enter total experience"
        }
        /*else if (ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.addressLine1)) {
            isValid = false
            displayCommunicationInfoDTO.addressLine1ErrorMsg.value = "Please enter address line 1"
        }else if (ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.addressLine2)) {
            isValid = false
        }else if (ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.countryName)
            || displayCommunicationInfoDTO.countryId == 0L) {
            isValid = false
            displayCommunicationInfoDTO.countryErrorMsg.value = "Please select country"
        }else if (ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.stateName)
            || displayCommunicationInfoDTO.stateId == 0L) {
            isValid = false
            displayCommunicationInfoDTO.stateErrorMsg.value = "Please select state"
        }else if (ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.cityName)
            || displayCommunicationInfoDTO.cityId == 0L) {
            isValid = false
            displayCommunicationInfoDTO.cityErrorMsg.value = "Please select city"
        }else if (ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.areaName)
            || displayCommunicationInfoDTO.areaId == 0L) {
            isValid = false
            displayCommunicationInfoDTO.areaErrorMsg.value = "Please select area"
        }else if (ErrorCheckUtils.checkValidPincode(displayCommunicationInfoDTO.pincodeNo.value).isNotEmpty()) {
            isValid = false
            displayCommunicationInfoDTO.pincodeErrorMsg.value =
                ErrorCheckUtils.checkValidPincode(displayCommunicationInfoDTO.pincodeNo.value)
        }else if (!ErrorCheckUtils.validateWithCommaNDot(displayCommunicationInfoDTO.landmark)) {
            isValid = false

        } */
        else{

           if (profileImageBase64!!.isNotEmpty()){
               imageDTO.base64 = profileImageBase64
               imageDTO.documentName = fileName
           }

            accessRolesList = arrayListOf()
            /*var role = RolesDTO()
            role.roleId = accessRoleId
            role.name = accessRoleValue.value
            role.roleName = accessRoleValue.value*/
            accessRolesList.add(accessRoleId)

            val addressMasterDTO = AddressMasterDTO()

            addressMasterDTO.addressLine1 = displayCommunicationInfoDTO.addressLine1.value.toString().trim()
            addressMasterDTO.addressLine2 = displayCommunicationInfoDTO.addressLine2.value.toString().trim()
            addressMasterDTO.countryId = displayCommunicationInfoDTO.countryId
            addressMasterDTO.countryName = displayCommunicationInfoDTO.countryName.value.toString().trim()
            addressMasterDTO.stateId = displayCommunicationInfoDTO.stateId
            addressMasterDTO.stateName = displayCommunicationInfoDTO.stateName.value.toString().trim()
            addressMasterDTO.cityId = displayCommunicationInfoDTO.cityId
            addressMasterDTO.cityName = displayCommunicationInfoDTO.cityName.value.toString().trim()
            addressMasterDTO.areaId = displayCommunicationInfoDTO.areaId
            addressMasterDTO.areaName = displayCommunicationInfoDTO.areaName.value.toString().trim()

            if(displayCommunicationInfoDTO.pincodeNo.value!!.isNotEmpty())
                addressMasterDTO.pincode = displayCommunicationInfoDTO.pincodeNo.value?.toLong() ?: 0

            addressMasterDTO.landmark = displayCommunicationInfoDTO.landmark.value.toString().trim()

//            staffUserDTO.outletAddress = addressMasterDTO

            var timingList = ArrayList<StaffTimingDTO>()
            for( day in timingListMutableLiveData.value!!){
                for(time in day.timings){
                    val timing = StaffTimingDTO()
                    timing.timingId = time.timingId
                    timing.staffId = staffUserId
                    timing.weekDay = time.weekDay
                    timing.opensAt = time.opensAt
                    timing.closesAt = time.closesAt
                    timingList.add(timing)
                }
            }
            val staffTimingDTO = StaffTimingRequestDTO(
                timingList
            )

            val name = firstName.value!!.split(" ")
            val fname = name.get(0)
            val lname = if(name.size > 1)  name[1] else ""

            val staffUserDTO = UserDTO(
                userId = staffUserId,
                loginId = emailAddress.value,
                emailAddress = emailAddress.value,
                fullName = firstName.value,
                userLastName = fname,
                userMiddleName = lname,
                mobileNo = mobileNumber.value,
                userFirstName = firstName.value,
                language = null,
                timeZone = null,
                currency = null,
                profileImage = imageDTO,
                companyId = null,
                managingOrganizationId = null,
                reportingManagerId = null,
                bio = aboutUserValue.value,
                knownLanguages = knowLanguagesIdList,
                designationId = designationId,
                totalExperience = totalExperienceValue.value,
                workingSince = withUsSinceValue.value,
                accessRoles = accessRolesList,
                outletId = outLetId,
                productId = null,
                outletAddress = addressMasterDTO,
                mobileCCode = countryCode.value,
                lastLoginDateTime = null,
                createdOn = null,
                staffTimingDTO = staffTimingDTO
            )
            saveStaffData(staffUserDTO)
        }


        return isValid
    }

    private fun saveStaffData(staffUserDTO: UserDTO){
        //service call for saving the data
          showProgressBar.value = true
        dashboardRepository.saveStaffUser(staffUserDTO, object : IDataSourceCallback<String> {
              override fun onDataFound(data: String) {
                  showProgressBar.value = false
                  getmSnackbar().value = data
                  cancelDialog.value = true
              }

              override fun onError(error: String) {
                  showProgressBar.value = false
                  getmSnackbar().value = error
              }
          })
    }



    private fun validateCountryCode(): Boolean {
        if (countryCode.value.isNullOrBlank()) {
            mobileNumberErrorMsg.value = "Please enter valid country code"
            getmSnackbar().value = "Please enter valid country code"
            return false
        } else {
            mobileNumberErrorMsg.value = ""
            return true
        }
    }


    private fun validateFirstName(): Boolean {
        error = ErrorCheckUtils.checkValidFirstName(firstName.value)
        if (error.isNotEmpty()) {
            firstNameErrorMsg.value = error
            getmSnackbar().value = error
            return false
        } else {
            firstNameErrorMsg.value = ""
        }
        return true
    }

    private fun validateEmail(): Boolean {
        error = ErrorCheckUtils.checkValidEmail(emailAddress.value)
        if (error.isNotEmpty()) {
            emailAddressErrorMsg.value = error
            getmSnackbar().value = error
            return false
        } else {
            emailAddressErrorMsg.value = ""
        }
        return true
    }

    private fun validateMobileNumber(): Boolean {
        error = ErrorCheckUtils.checkValidMobile(mobileNumber.value)
        if (error.isNotEmpty()) {
            mobileNumberErrorMsg.value = error
            getmSnackbar().value = error
            return false
        } else {
            mobileNumberErrorMsg.value = ""
        }
        return true
    }

    private fun validateAccessRole(): Boolean {
        return if (accessRoleId != 0L) {
            accessRoleErrorMsg.value = ""
            getmSnackbar().value = error
            true
        } else {
            accessRoleErrorMsg.value = "Please select an access role"
            false
        }

    }


    private fun validateImage(): Boolean {
        if (profileImageBase64.isNullOrBlank()) {
            getmSnackbar().value = "Please upload an image"
            getmSnackbar().value = error
            return false
        } else {
            return true
        }

    }

}