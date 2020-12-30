package com.sagar.logutil.checqk.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.sagar.logutil.R
import com.sagar.logutil.checqk.model.CommonListingDTO
import com.sagar.logutil.checqk.model.CommonSortDTO
import com.sagar.logutil.checqk.model.SubAdminDTO
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.GenericBaseObservable
import com.sagar.logutil.checqk.view.login.remote.UserRepository
import com.sagar.logutil.databinding.AlreadyLoginPopupLayoutBinding

class SubAdminViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var userRepository: UserRepository
) : GenericBaseObservable(application, owner, view) {
    var showProgressBar = MutableLiveData<Boolean>()
    var subAdminDTOMutableList = MutableLiveData<ArrayList<SubAdminDTO>>()
    var subAdminActive = MutableLiveData<Boolean>(true)
    var subAdminInActive = MutableLiveData<Boolean>(false)
    var isActive = true

    fun fetchSubAdminListing() {
        val commonListingDTO = CommonListingDTO()
        val commonSortList = ArrayList<CommonSortDTO>()
        val commonSortDTO = CommonSortDTO()
        commonSortDTO.sortField = Constants.SUB_ADMIN_SORT_FIELD
        commonSortDTO.sortOrder = Constants.SORT_ORDER
        commonSortList.add(commonSortDTO)
//        commonListingDTO.sort = commonSortList
        commonListingDTO.sort = arrayListOf()
        commonListingDTO.defaultSort = commonSortDTO
        commonListingDTO.length = Constants.PAGINATION_PAGE_DATA_COUNT
        commonListingDTO.status = isActive
        showProgressBar.value = true
        userRepository.outletSubAdminListing(
            commonListingDTO,
            object : IDataSourceCallback<List<SubAdminDTO>> {

                override fun onDataFound(data: List<SubAdminDTO>) {
                    showProgressBar.value = false
                    for(item in data){
                        item.isActive = isActive
                    }
                    subAdminDTOMutableList.value = data as ArrayList<SubAdminDTO>

                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })
    }

    fun changeSubAdminStatus(userId: Long?,isRestore:Boolean) {
        showProgressBar.value = true
        if (userId != null) {
            userRepository.changeSubAdminStatus(userId, isRestore, object : IDataSourceCallback<String> {

                override fun onDataFound(data: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = data
                    fetchSubAdminListing()
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })
        }

    }


    fun onActiveSubAdmin(){
        subAdminActive.value = true
        subAdminInActive.value = false
        isActive = true
        fetchSubAdminListing()
    }

    fun onInActiveSubAdmin(){
        subAdminActive.value = false
        subAdminInActive.value = true
        isActive = false
        fetchSubAdminListing()
    }

    fun showConfirmationDialog(userId: Long?) {

        val alreadyLoginPopUpLayoutBinding: AlreadyLoginPopupLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(activity), R.layout.already_login_popup_layout, null, false
            )
        val view: View = alreadyLoginPopUpLayoutBinding.root
        val alert =
            AlertDialog.Builder(activity)
        // this is set the view from XML inside AlertDialog
        alert.setView(view)
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false)
        val dialog = alert.create()
        alreadyLoginPopUpLayoutBinding.titleDescription.text =
            getmContext().resources.getString(R.string.do_you_want_to_delete_the_subadmin)
        alreadyLoginPopUpLayoutBinding.yesBtn.setOnClickListener(View.OnClickListener {
            //service call for discarding the category and images
            changeSubAdminStatus(userId,false)
            dialog.dismiss()
        })
        alreadyLoginPopUpLayoutBinding.noBtn.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        dialog.show()
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

}