package com.sagar.logutil.checqk.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.source.DashboardRepository
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.GenericBaseObservable

class AddStaffSafetyReadingsViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var dashboardRepository: DashboardRepository
) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>()
    private var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)

    private var outLetId = 0L
    var staffSafetyReadingsList = MutableLiveData<ArrayList<StaffSafetyReadingDTO>>()

    var cancelDialog = MutableLiveData<Boolean>(false)
    var checkedOnDate = MutableLiveData<String>("")

    var staffUsersId = ""

    init {
        outLetId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L

    }

    fun getStaffUserSafetyReadings() {

//        staffSafetyReadingsList.value = arrayListOf(null,null,null,null)

        var staffUsersId: String = ""
        for (staffUser in staffSafetyReadingsList.value ?: arrayListOf()) {
            if (staffUsersId.isEmpty()) {
                staffUsersId = staffUser.staffUserId.toString()
            } else {
                staffUsersId += "," + staffUser.staffUserId.toString()
            }
        }

        val commonListingDTO = CommonListingDTO()

        val commonSortDTO = CommonSortDTO()
        commonSortDTO.sortField = Constants.STAFF_USER_SAFETY_READING_COLUMN
        commonSortDTO.sortOrder = Constants.SORT_ORDER
        commonListingDTO.defaultSort = commonSortDTO

        commonListingDTO.length = Constants.PAGINATION_PAGE_DATA_COUNT
        commonListingDTO.start = 0
        commonListingDTO.status = true

        val commonSearchList = ArrayList<CommonSearchDTO>()
        val commonSearchDTO1 = CommonSearchDTO()
        commonSearchDTO1.search = outLetId.toString()
        commonSearchDTO1.searchCol = Constants.OUTLET_ID_COLUMN
        commonSearchList.add(commonSearchDTO1)

        val commonSearchDTO2 = CommonSearchDTO()
        commonSearchDTO2.search = staffUsersId
        commonSearchDTO2.searchCol = Constants.STAFF_USER_ID_COLUMN
        commonSearchList.add(commonSearchDTO2)

        val commonSearchDTO3 = CommonSearchDTO()
        commonSearchDTO3.search = System.currentTimeMillis().toString()
        commonSearchDTO3.searchCol = Constants.CHECKED_ON_DATE_COLUMN
        commonSearchList.add(commonSearchDTO3)
        commonListingDTO.search = commonSearchList

        showProgressBar.value = true
        dashboardRepository.getStaffSafetyReadings(
            commonListingDTO,
            object : IDataSourceCallback<ArrayList<StaffSafetyReadingDTO>> {
                override fun onDataFound(data: ArrayList<StaffSafetyReadingDTO>) {
//                staffSafetyReadingsList.value = data
//                checkedOnDate.value = data[0].checkedon
                    showStaffUsersSafetyReadingData(data)
                    showProgressBar.value = false
                }

                override fun onDataNotFound() {
                    showProgressBar.value = false
//                getmSnackbar().value = "No staff users found"
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })

    }

    fun showStaffUsersSafetyReadingData(staffSafetyReadingList: ArrayList<StaffSafetyReadingDTO>) {

        for (staffUserDetails in staffSafetyReadingsList.value ?: arrayListOf()) {
            for (staffSafetyReading in staffSafetyReadingList) {
                if (staffUserDetails.staffUserId == staffSafetyReading.staffUserId) {
                    staffUserDetails.staffSecurityMeasuresId = staffSafetyReading.staffSecurityMeasuresId
                    staffUserDetails.maskFlag = staffSafetyReading.maskFlag
                    staffUserDetails.tempReading = staffSafetyReading.tempReading
                    staffUserDetails.checkedOn = staffSafetyReading.checkedOn
                }
            }
        }
        staffSafetyReadingsList.value = staffSafetyReadingsList.value
        checkedOnDate.value = (staffSafetyReadingsList.value ?: arrayListOf())[0].checkedOn ?: ""
    }

    fun onCancel() {
        cancelDialog.value = true
    }

    fun onSaveClick() {
        if (staffSafetyReadingsList.value?.size ?: 0 > 0)
            saveStaffSafetyReading()
    }

    fun saveStaffSafetyReading() {
        val staffSafetyReadingRequestDTO = StaffSafetyReadingRequestDTO(
            staffSafetyReadingsList.value ?: arrayListOf()
        )
        dashboardRepository.saveStaffSafetyReadings(
            staffSafetyReadingRequestDTO,
            object : IDataSourceCallback<String> {
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
}