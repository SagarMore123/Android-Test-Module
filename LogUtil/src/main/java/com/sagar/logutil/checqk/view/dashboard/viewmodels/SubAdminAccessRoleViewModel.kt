package com.astrika.checqk.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.checqk.model.AccessRoleDTO
import com.astrika.checqk.model.CommonListingDTO
import com.astrika.checqk.model.CommonSearchDTO
import com.astrika.checqk.model.CommonSortDTO
import com.astrika.checqk.network.network_utils.IDataSourceCallback
import com.astrika.checqk.source.DashboardRepository
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.GenericBaseObservable
import com.astrika.checqk.view.login.remote.UserRepository

class SubAdminAccessRoleViewModel : GenericBaseObservable {

    var activity: Activity
    var application: Application
    var showProgressBar = MutableLiveData<Boolean>()
    private var userRepository: UserRepository
    private var dashboardRepository: DashboardRepository
    private var sharedPreferences: SharedPreferences

    val accessRolesMutableLiveData = MutableLiveData<ArrayList<AccessRoleDTO>>()
    var activeRecords = MutableLiveData<Boolean>(true)
    var inactiveRecords = MutableLiveData<Boolean>(false)
    var isActive = true
    private var outLetId = 0L

    constructor(
        activity: Activity,
        application: Application,
        owner: LifecycleOwner?,
        view: View?,
        userRepository: UserRepository,
        dashboardRepository: DashboardRepository
    ) : super(application, owner, view) {
        this.activity = activity
        this.application = application
        this.userRepository = userRepository
        this.dashboardRepository = dashboardRepository
        sharedPreferences = Constants.getSharedPreferences(application)

        outLetId = Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L

    }

  /*  fun getMenuList() {
        val arrayList = arrayListOf<CategoryMenuDTO>(
            CategoryMenuDTO(0,"Menu","",0,0),
            CategoryMenuDTO(0,"Bar","",0,0),
            CategoryMenuDTO(0,"Breakfast","",0,0)
        )
        menuCategoryMutableArrayList.value = arrayList
    }*/

    fun getSubAdminAccessRoleListing(search : String, status : Boolean){

        isActive = status
        val commonListingDTO = CommonListingDTO()

            val commonSortDTO = CommonSortDTO()
            commonSortDTO.sortField = Constants.GROUP_ROLE_SORT_FIELD
            commonSortDTO.sortOrder = Constants.SORT_ORDER_DESC
        commonListingDTO.defaultSort = commonSortDTO

        val searchList = ArrayList<CommonSearchDTO>()
            val commonSearchDTO = CommonSearchDTO()
            commonSearchDTO.search = outLetId.toString()
            commonSearchDTO.searchCol = Constants.SUB_ADMIN_GROUP_ROLE_COLUMN
            searchList.add(commonSearchDTO)
        commonListingDTO.search = searchList

        commonListingDTO.length = Constants.PAGINATION_PAGE_DATA_COUNT
        commonListingDTO.status = status

        showProgressBar.value = true

        dashboardRepository.getAccessRolesListing(commonListingDTO, object : IDataSourceCallback<ArrayList<AccessRoleDTO>>{
            override fun onDataFound(data: ArrayList<AccessRoleDTO>) {
                accessRolesMutableLiveData.value = data
                showProgressBar.value = false
            }

            override fun onDataNotFound() {
                accessRolesMutableLiveData.value = arrayListOf()
                showProgressBar.value = false
            }

            override fun onError(error: String) {
                accessRolesMutableLiveData.value = arrayListOf()
                getmSnackbar().value = error
                showProgressBar.value = false
            }
        })
    }

    fun onActiveRecordsClick(){
        activeRecords.value = true
        inactiveRecords.value = false
        isActive = true
        getSubAdminAccessRoleListing("", isActive)
    }

    fun onInActiveRecordsClick(){
        activeRecords.value = false
        inactiveRecords.value = true
        isActive = false
        getSubAdminAccessRoleListing("", isActive)
    }

    fun changeAccessRoleStatus(userId: Long?, status:Boolean) {
        showProgressBar.value = true
        if (userId != null) {
            dashboardRepository.changeAccessRoleStatus(userId, status, object : IDataSourceCallback<String> {

                override fun onDataFound(data: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = data
                    getSubAdminAccessRoleListing("",isActive)
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })
        }

    }
}