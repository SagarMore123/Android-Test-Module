package com.astrika.checqk.view.dashboard.viewmodels

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
import com.astrika.checqk.master_controller.source.MasterRepository
import com.astrika.checqk.model.DashboardDrawerDTO
import com.astrika.checqk.network.network_utils.IDataSourceCallback
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.GenericBaseObservable
import com.astrika.checqk.view.login.UserLoginActivity
import com.astrika.checqk.view.login.remote.UserRepository
import com.sagar.logutil.R
import com.sagar.logutil.databinding.AlreadyLoginPopupLayoutBinding
import java.util.*
import kotlin.collections.ArrayList

class DashboardViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var masterRepository: MasterRepository,
    private var userRepository: UserRepository
) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>()

    var dashboardDrawerArrayList = ArrayList<DashboardDrawerDTO>()
    var dashboardDrawerListMutableLiveData = MutableLiveData<ArrayList<DashboardDrawerDTO>>()
    var keyList = listOf(Constants.ACCESS_TOKEN,Constants.IS_LOG_OUT)

    fun fetchDrawerMenu() {
        masterRepository.fetchDashboardDrawerMasterDataLocal(object :
            IDataSourceCallback<List<DashboardDrawerDTO>> {

            override fun onDataFound(data: List<DashboardDrawerDTO>) {
                dashboardDrawerArrayList.clear()
                dashboardDrawerArrayList = data as ArrayList<DashboardDrawerDTO>
                for (item in dashboardDrawerArrayList) {
                    if (item.name == Constants.MENU_MANAGEMENT) {
                        item.applicationModules = setAdditionalMenuData()
                        break
                    }
                }
                val dashboardDrawerDTO = DashboardDrawerDTO()
                dashboardDrawerDTO.name = "Logout"
                dashboardDrawerArrayList.add(dashboardDrawerArrayList.size, dashboardDrawerDTO)
                dashboardDrawerListMutableLiveData.value = dashboardDrawerArrayList
//                Log.e("data", data.toString())
            }

            override fun onError(error: String) {
                getmSnackbar().value = error
            }
        })
    }

    fun showLogoutDialog() {
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
            activity.resources.getString(R.string.do_you_want_to_logout)
        alreadyLoginPopUpLayoutBinding.yesBtn.setOnClickListener(View.OnClickListener {
            logoutUser()
        })
        alreadyLoginPopUpLayoutBinding.noBtn.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        dialog.show()
        Objects.requireNonNull(dialog.window)?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }


    private fun logoutUser() {
        showProgressBar.value = true
        userRepository.logoutUser(object : IDataSourceCallback<String> {

            override fun onDataFound(data: String) {
                showProgressBar.value = false
                Constants.getSharedPreferences(activity).edit()?.
                putString(Constants.IS_LOG_OUT,Constants.encrypt(true.toString()))?.apply()
                Constants.clearAllPrefsExceptKey(activity,keyList)
                Constants.changeActivityAndFinish<UserLoginActivity>(activity)
            }

            override fun onError(error: String) {
                showProgressBar.value = false
                getmSnackbar().value = error
            }
        })
    }

    fun setAdditionalMenuData(): ArrayList<DashboardDrawerDTO> {
        val applicationModules = ArrayList<DashboardDrawerDTO>()
        val dashboardDrawerDTOMenuCategory = DashboardDrawerDTO()
        dashboardDrawerDTOMenuCategory.name = Constants.MENU_CATEGORY
        val dashboardDrawerDTOMenuSection = DashboardDrawerDTO()
        dashboardDrawerDTOMenuSection.name = Constants.MENU_SECTION
        val dashboardDrawerDTOMenuDishes = DashboardDrawerDTO()
        dashboardDrawerDTOMenuDishes.name = Constants.MENU_DISHES
        applicationModules.add(dashboardDrawerDTOMenuCategory)
        applicationModules.add(dashboardDrawerDTOMenuSection)
        applicationModules.add(dashboardDrawerDTOMenuDishes)
        return applicationModules
    }

    fun drawerAppModuleItemSelection(mainContainerPosition: Int, position: Int) {

        if (!dashboardDrawerArrayList.isNullOrEmpty()
            && dashboardDrawerArrayList[mainContainerPosition] != null
            && !dashboardDrawerArrayList[mainContainerPosition].applicationModules.isNullOrEmpty()
        ) {

            for ((i, module) in dashboardDrawerArrayList.withIndex() ?: arrayListOf()) {

                if (i == mainContainerPosition) {
                    for ((j, appModuleItem) in module.applicationModules?.withIndex()
                        ?: arrayListOf()) {

                        appModuleItem.selected = j == position
                    }
                } else {
                    for ((j, appModuleItem) in module.applicationModules?.withIndex()
                        ?: arrayListOf()) {
                        appModuleItem.selected = false
                    }
                }

            }
        }

        dashboardDrawerListMutableLiveData.value = dashboardDrawerArrayList
    }
}