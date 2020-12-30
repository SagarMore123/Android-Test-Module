package com.sagar.logutil.checqk.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.sagar.logutil.checqk.model.CommonListingDTO
import com.sagar.logutil.checqk.model.CommonSortDTO
import com.sagar.logutil.checqk.model.UserDTO
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.source.DashboardRepository
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.GenericBaseObservable
import com.sagar.logutil.checqk.view.login.remote.UserRepository

class StaffUserViewModel : GenericBaseObservable {

    var activity: Activity
    var application: Application
    var showProgressBar = MutableLiveData<Boolean>()
    private var userRepository: UserRepository
    private var dashboardRepository: DashboardRepository
    private var sharedPreferences: SharedPreferences

    val staffUsersMutableLiveData = MutableLiveData<ArrayList<UserDTO>>(arrayListOf())
    var subAdminActive = MutableLiveData<Boolean>(true)
    var subAdminInActive = MutableLiveData<Boolean>(false)
    var isActive = true

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

    }

  /*  fun getMenuList() {
        val arrayList = arrayListOf<CategoryMenuDTO>(
            CategoryMenuDTO(0,"Menu","",0,0),
            CategoryMenuDTO(0,"Bar","",0,0),
            CategoryMenuDTO(0,"Breakfast","",0,0)
        )
        menuCategoryMutableArrayList.value = arrayList
    }*/

    fun getStaffListing(search : String, status : Boolean){
       /* val searchSortDTO = SearchSortDTO(
            addtionalSearch = null,
            search = arrayListOf(),
            defaultSort = SortField("fullName", "asc"),
            sort = null,
            length = 0,
            start = 0,
            status = status,
            statusCode = null
        )*/
        isActive = status
        val commonListingDTO = CommonListingDTO()
        val commonSortList = ArrayList<CommonSortDTO>()
        val commonSortDTO = CommonSortDTO()
        commonSortDTO.sortField = Constants.STAFF_USER_SORT_COLUMN
        commonSortDTO.sortOrder = Constants.SORT_ORDER
        commonSortList.add(commonSortDTO)
        commonListingDTO.sort = commonSortList
        commonListingDTO.length = Constants.PAGINATION_PAGE_DATA_COUNT
        commonListingDTO.status = status
        showProgressBar.value = true

        dashboardRepository.getStaffListing(commonListingDTO, object : IDataSourceCallback<ArrayList<UserDTO>>{
            override fun onDataFound(data: ArrayList<UserDTO>) {
                staffUsersMutableLiveData.value = data
                showProgressBar.value = false
            }

            override fun onDataNotFound() {
                staffUsersMutableLiveData.value = arrayListOf()
                showProgressBar.value = false
            }

            override fun onError(error: String) {
                staffUsersMutableLiveData.value = arrayListOf()
                getmSnackbar().value = error
                showProgressBar.value = false
            }
        })
    }

    fun onActiveStaffUsersClick(){
        subAdminActive.value = true
        subAdminInActive.value = false
        isActive = true
        getStaffListing("", isActive)
    }

    fun onInActiveStaffUsersClick(){
        subAdminActive.value = false
        subAdminInActive.value = true
        isActive = false
        getStaffListing("", isActive)
    }

    fun changeSubAdminStatus(userId: Long?,status:Boolean) {
        showProgressBar.value = true
        if (userId != null) {
            userRepository.changeSubAdminStatus(userId, status, object : IDataSourceCallback<String> {

                override fun onDataFound(data: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = data
                    getStaffListing("",isActive)
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })
        }

    }
}