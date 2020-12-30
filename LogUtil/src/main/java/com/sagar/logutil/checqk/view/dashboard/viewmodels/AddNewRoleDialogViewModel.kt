package com.sagar.logutil.checqk.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.sagar.logutil.checqk.model.AccessRoleDTO
import com.sagar.logutil.checqk.model.PermissionDTO
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.source.DashboardRepository
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.GenericBaseObservable

class AddNewRoleDialogViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var dashboardRepository: DashboardRepository
) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>()
    private var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)

    var permissionMutableList = MutableLiveData<ArrayList<PermissionDTO>>()
    var permissionList = ArrayList<PermissionDTO>()

    var cancelDialog = MutableLiveData<Boolean>(false)

    var roleName = MutableLiveData<String>("")
    var roleNameErrorMsg = MutableLiveData<String>("")
    var roleDescription = MutableLiveData<String>("")
    var roleDescriptionLength = MutableLiveData<String>("0")

    private var outLetId = 0L
    var accessRoleId = 0L
    var applicableForEntityId = 0L

    lateinit var error: String

    init {
        applicableForEntityId = Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L
        //applicableForEntityId = outLetId
    }

    fun onCancel() {
        cancelDialog.value = true
    }

    fun getApplicationModules(){
        showProgressBar.value = true
        dashboardRepository.getApplicationModules(object : IDataSourceCallback<ArrayList<PermissionDTO>>{
            override fun onDataFound(data: ArrayList<PermissionDTO>) {
                if(accessRoleId == 0L){
                    manageParentChildData(data)
                    showProgressBar.value = false
                }else{
                    permissionList = data
                    getAccessRoleDetails()
                }
            }

            override fun onError(error: String) {
                showProgressBar.value = false
                getmSnackbar().value = error
            }
        })
    }

    fun manageParentChildData(permissionList : ArrayList<PermissionDTO>){

        val parentList = ArrayList<PermissionDTO>()
        for(permission in permissionList){
            if (permission.parentModuleId!!.equals(0L)){
                parentList.add(permission)
            }
        }
        for(permissionParent in parentList){
            val childList = ArrayList<PermissionDTO>()
            for(permission in permissionList){
                if (permissionParent.moduleId!!.equals(permission.parentModuleId)){
                    childList.add(permission)
                }
            }
            permissionParent.permissions = childList
        }
        permissionMutableList.value = parentList

    }

    fun getAccessRoleDetails(){
        dashboardRepository.getAccessRoleDetails( accessRoleId, object : IDataSourceCallback<AccessRoleDTO>{
            override fun onDataFound(data: AccessRoleDTO) {
                showAccessRoleDetails(data)
                showProgressBar.value = false
            }
            override fun onError(error: String) {
                showProgressBar.value = false
                getmSnackbar().value = error
            }
        })
    }

    fun showAccessRoleDetails(accessRoleDTO: AccessRoleDTO){
        val parentList = ArrayList<PermissionDTO>()
        for(permission in permissionList){
            if (permission.parentModuleId!!.equals(0L)){
                parentList.add(permission)
            }
        }
        for(permission in parentList){
            permission.permissions = ArrayList<PermissionDTO>()
           for(per in accessRoleDTO.permissions!!){
               if (permission.moduleId!!.equals(per.parentModuleId)){
                   permission.permissions!!.add(per)
               }
           }
        }
        permissionMutableList.value = parentList

        roleName.value = accessRoleDTO.name
        roleDescription.value = accessRoleDTO.description


    }

    fun onSaveClick() {
        validate()
    }

    fun validate(){
        if(roleName.value!!.isEmpty()){
            roleNameErrorMsg.value = "Please enter role name"
        }else{
            val permissionList = ArrayList<PermissionDTO>()
           /* for(permission in permissionMutableList.value!!){
                if ((permission.viewModule != null && permission.viewModule!!) ||
                    (permission.viewModule != null && permission.addition!!) ||
                    (permission.edit != null && permission.edit!!) ){
                    permissionList.addAll(permission.permissions!!)
                }else{
                    for (perm in permission.permissions!!){
                        if ((permission.viewModule != null && permission.viewModule!!) ||
                            (permission.viewModule != null && permission.addition!!) ||
                            (permission.edit != null && permission.edit!!)  ){
                            permissionList.add(perm)
                        }
                    }
                }
            }*/

            for(permission in permissionMutableList.value!!){
                for (perm in permission.permissions!!){
                    /*if ((permission.viewModule != null && permission.viewModule!!) ||
                        (permission.viewModule != null && permission.addition!!) ||
                        (permission.edit != null && permission.edit!!)  ){
                    }*/
                    permissionList.add(perm)
                }

            }

            val accessRoleDTO = AccessRoleDTO(
                id = accessRoleId,
                applicableForEntityId = applicableForEntityId,
                assignedToUser = 0,
                description = roleDescription.value,
                name = roleName.value,
                createdBy = null,
                createdOn = null,
                modifiedBy = null,
                permissions = permissionList
            )
            saveAccessRole(accessRoleDTO)
        }
    }
    private fun saveAccessRole(accessRoleDTO: AccessRoleDTO){
        //service call for saving the data
          showProgressBar.value = true
        dashboardRepository.saveAccessRole(accessRoleDTO, object : IDataSourceCallback<String> {
              override fun onDataFound(data: String) {
                  showProgressBar.value = false
//                  getmSnackbar().value = data
                  Constants.showToastMessage(activity, data)
                  cancelDialog.value = true

              }

              override fun onError(error: String) {
                  showProgressBar.value = false
                  getmSnackbar().value = error
              }
          })
    }


}