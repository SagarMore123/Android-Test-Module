package com.sagar.logutil.checqk.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.sagar.logutil.checqk.master_controller.source.MasterRepository
import com.sagar.logutil.checqk.master_controller.source.daos.SystemValueMasterDao
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.source.DashboardRepository
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.GenericBaseObservable

class ReservedTableViewModel : GenericBaseObservable {

    var activity: Activity
    var application: Application
    var showProgressBar = MutableLiveData<Boolean>()
    private var dashboardRepository: DashboardRepository
    private var masterRepository: MasterRepository
    var tableLiveList = MutableLiveData<ArrayList<BookingDTO>>()
    var outLetId = 0L
    var tableManagementListLiveData = MutableLiveData<ArrayList<TableManagementDTO>>()
    var vacantTableArrayList = ArrayList<SystemValueMasterDTO>()
    var vacantTableArrayListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()
    var occupiedTableArrayList = ArrayList<SystemValueMasterDTO>()
    var occupiedTableArrayListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()
    var reservedTableArrayList = ArrayList<SystemValueMasterDTO>()
    var reservedTableArrayListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()

    constructor(
        activity: Activity,
        application: Application,
        owner: LifecycleOwner?,
        view: View?,
        masterRepository: MasterRepository,
        dashboardRepository: DashboardRepository
    ) : super(application, owner, view) {
        this.activity = activity
        this.application = application
        this.dashboardRepository = dashboardRepository
        this.masterRepository = masterRepository
        val sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)
        outLetId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L
        populateMasters()
        getTableManagementList()
    }

    private fun populateMasters() {
        //fetch vacant green seat images from svm
        masterRepository.fetchSystemMasterValueByNameLocal(
            SystemValueMasterDao.TABLE_CONFIG_VACANT,
            object : IDataSourceCallback<List<SystemValueMasterDTO>> {

                override fun onDataFound(data: List<SystemValueMasterDTO>) {
                    vacantTableArrayList.clear()
                    vacantTableArrayList = data as ArrayList<SystemValueMasterDTO>
                    vacantTableArrayListMutableLiveData.value = vacantTableArrayList
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                }
            })

        //fetch occupied blue seat images from svm
        masterRepository.fetchSystemMasterValueByNameLocal(
            SystemValueMasterDao.TABLE_CONFIG_OCCUPIED,
            object : IDataSourceCallback<List<SystemValueMasterDTO>> {

                override fun onDataFound(data: List<SystemValueMasterDTO>) {
                    occupiedTableArrayList.clear()
                    occupiedTableArrayList = data as ArrayList<SystemValueMasterDTO>
                    occupiedTableArrayListMutableLiveData.value = occupiedTableArrayList
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                }
            })

        //fetch reserved red seat images from svm
        masterRepository.fetchSystemMasterValueByNameLocal(
            SystemValueMasterDao.TABLE_CONFIG_RESERVED,
            object : IDataSourceCallback<List<SystemValueMasterDTO>> {

                override fun onDataFound(data: List<SystemValueMasterDTO>) {
                    reservedTableArrayList.clear()
                    reservedTableArrayList = data as ArrayList<SystemValueMasterDTO>
                    reservedTableArrayListMutableLiveData.value = reservedTableArrayList
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                }
            })
    }

    private fun getTableManagementList() {
        dashboardRepository.getReservedTableListingByOutletId(outLetId,
            object : IDataSourceCallback<ArrayList<TableManagementDTO>> {

                override fun onDataFound(data: ArrayList<TableManagementDTO>) {
                    showProgressBar.value = false
                    Log.e("data",data.toString())
                    tableManagementListLiveData.value = data
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })
    }

    fun fetchReservedTableListing() {
        val commonListingDTO = CommonListingDTO()
        val commonSortDTO = CommonSortDTO()
        commonSortDTO.sortField = Constants.BOOKING_ID_COLUMN
        commonSortDTO.sortOrder = Constants.SORT_ORDER_DESC
        commonListingDTO.defaultSort = commonSortDTO
        commonListingDTO.length = Constants.PAGINATION_PAGE_DATA_COUNT
        commonListingDTO.status = true
        commonListingDTO.statusCode = 1
        commonListingDTO.requestType = -1
        showProgressBar.value = true
        dashboardRepository.getReservedTableListing(commonListingDTO,
            object : IDataSourceCallback<ArrayList<BookingDTO>> {

                override fun onDataFound(data: ArrayList<BookingDTO>) {
                    tableLiveList.value = data
                    showProgressBar.value = false
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                    showProgressBar.value = false
                }
            })

    }

    fun assignTableServiceCall(bookingId: Long) {
        showProgressBar.value = true
        dashboardRepository.assignTable(bookingId, Constants.CONFIRMED_BOOKING_STATUS,
            object : IDataSourceCallback<String> {

                override fun onDataFound(data: String) {
                    fetchReservedTableListing()
                    showProgressBar.value = false
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                    showProgressBar.value = false
                }
            })

    }

}