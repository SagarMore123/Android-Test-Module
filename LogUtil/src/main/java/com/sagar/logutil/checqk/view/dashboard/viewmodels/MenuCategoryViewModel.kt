package com.astrika.checqk.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.checqk.model.CategoryCatalogueDTO
import com.astrika.checqk.model.GetCatalogueCategoriesResponseDTO
import com.astrika.checqk.network.network_utils.IDataSourceCallback
import com.astrika.checqk.source.DashboardRepository
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.GenericBaseObservable
import com.sagar.logutil.R
import com.sagar.logutil.databinding.AddMenuCategoryDialoagLayoutBinding

class MenuCategoryViewModel : GenericBaseObservable {

    var activity: Activity
    var application: Application
    var showProgressBar = MutableLiveData<Boolean>()
    var menuCategoryNotAddedVisibility = MutableLiveData<Boolean>(false)
//    var noInactiveMenuCategoryVisibility = MutableLiveData<Boolean>(false)
    var inactiveMenuCategoryRecyclerVisibility = MutableLiveData<Boolean>(false)
    var noInactiveMenuCategoryTextVisibility = MutableLiveData<Boolean>(false)
    var menuCategoryMutableArrayList = MutableLiveData<ArrayList<CategoryCatalogueDTO>>()
    var inactiveMenuCategoryMutableArrayList = MutableLiveData<ArrayList<CategoryCatalogueDTO>>()
    var menuCategoryList = ArrayList<CategoryCatalogueDTO>()
    private var dashboardRepository: DashboardRepository
    private var sharedPreferences: SharedPreferences
    var outLetId = 0L
    var index = 0


    constructor(
        activity: Activity,
        application: Application,
        owner: LifecycleOwner?,
        view: View?,
        dashboardRepository: DashboardRepository
    ) : super(application, owner, view) {
        this.activity = activity
        this.application = application
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

    fun getAllMenuCategories(){
        showProgressBar.value = true
        dashboardRepository.getAllMenuCategories(outLetId, object : IDataSourceCallback<GetCatalogueCategoriesResponseDTO>{
            override fun onDataFound(data: GetCatalogueCategoriesResponseDTO) {
                menuCategoryList = data.activeCatalogueCategoryDTOs?: arrayListOf()
                menuCategoryMutableArrayList.value = data.activeCatalogueCategoryDTOs?: arrayListOf()
                inactiveMenuCategoryMutableArrayList.value = data.inActiveCatalogueCategoryDTOs?: arrayListOf()
                menuCategoryNotAddedVisibility.value = false
                showProgressBar.value = false
            }

            override fun onDataNotFound() {
                menuCategoryList = arrayListOf()
                menuCategoryMutableArrayList.value = arrayListOf()
                inactiveMenuCategoryMutableArrayList.value = arrayListOf()
                menuCategoryNotAddedVisibility.value = true
                showProgressBar.value = false
            }

            override fun onError(error: String) {
                getmSnackbar().value = error
                showProgressBar.value = false
            }
        })
    }

    fun saveMenuCategory(menuCategory : CategoryCatalogueDTO){

        showProgressBar.value = true
        dashboardRepository.saveMenuCategories(menuCategory, object : IDataSourceCallback<Long>{
            override fun onDataFound(data: Long) {

//                menuCategoryList = menuCategoryMutableArrayList.value!!
                /*if(!menuCategory.catalogueCategoryId!!.equals(0L)){
                    menuCategoryList.set(index, menuCategory)
                }else{
                    menuCategory.catalogueCategoryId = data
                    menuCategoryList.add(menuCategory)
                }
                menuCategoryMutableArrayList.value = menuCategoryList*/

                getmSnackbar().value = "Menu category save successfully"
                showProgressBar.value = false

                getAllMenuCategories()
            }

            override fun onError(error: String) {
                getmSnackbar().value = error
                showProgressBar.value = false
            }
        })
    }

    fun saveMenuCategoriesSequence(menuCategoryList :  ArrayList<CategoryCatalogueDTO>){
//        showProgressBar.value = true
        dashboardRepository.saveMenuCategoriesSequence(menuCategoryList,
            object : IDataSourceCallback<String>{
                override fun onDataFound(data: String) {
                    getmSnackbar().value = data
//                    showProgressBar.value = false
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
//                    showProgressBar.value = false
                }
            })
    }

    fun changeMenuCategoryStatus(menuCategory : CategoryCatalogueDTO, status : Boolean){
        showProgressBar.value = true
        dashboardRepository.changeMenuCategoryStatus(menuCategory.catalogueCategoryId!!, status,
            object : IDataSourceCallback<String>{
                override fun onDataFound(data: String) {
//                    menuCategoryList = menuCategoryMutableArrayList.value!!
                    /*menuCategoryList.removeAt(index)
                    menuCategoryMutableArrayList.value = menuCategoryList*/
                    getmSnackbar().value = data
                    showProgressBar.value = false
                    getAllMenuCategories()
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                    showProgressBar.value = false
                }
            })
    }

    fun onAddClick(){
        showAddMenuCategoryDialog(null)
    }

    fun onShowInactiveClick(){
        if(inactiveMenuCategoryMutableArrayList.value!!.size > 0){
            inactiveMenuCategoryRecyclerVisibility.value = !inactiveMenuCategoryRecyclerVisibility.value!!
        }else{
            noInactiveMenuCategoryTextVisibility.value = !noInactiveMenuCategoryTextVisibility.value!!
        }
    }

    fun showAddMenuCategoryDialog(menuCategory : CategoryCatalogueDTO?) {

        val inflater = activity.layoutInflater
//        val alertLayout: View = inflater.inflate(R.layout.add_menu_category_dialoag_layout, null)
        var binding: AddMenuCategoryDialoagLayoutBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.add_menu_category_dialoag_layout,
            null,
            false
        )
        val alert = AlertDialog.Builder(activity)

        alert.setView(binding.root)
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false)
        val dialog = alert.create()

        if (menuCategory != null && menuCategory.catalogueCategoryName!!.isNotEmpty()){
            binding.menuNameEditText.setText(menuCategory.catalogueCategoryName!!)
        }

        binding.closeDialogImageView.setOnClickListener { dialog.dismiss() }

        binding.saveButton.setOnClickListener {
            if(binding.menuNameEditText.text.isEmpty()){
                binding.menuCategoryErrorTextView.visibility = View.VISIBLE
            }else{
                binding.menuCategoryErrorTextView.visibility = View.GONE
                if (menuCategory != null){
                    menuCategory.catalogueCategoryName = binding.menuNameEditText.text.toString()
                    saveMenuCategory(menuCategory)
                }else{
                    val menuCategory1 = CategoryCatalogueDTO(
                        0,
                        binding.menuNameEditText.text.toString(),
                        binding.menuNameEditText.text.toString(),
                        null,
                        true,
                        outLetId
                    )
                    saveMenuCategory(menuCategory1)
                }
                dialog.dismiss()
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    fun confirmationDialogForDelete(context : Context, menuCategory : CategoryCatalogueDTO){
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Confirmation")
        //set message for alert dialog
        var message = ""
        if(menuCategory.active!!){
            message = "Are you sure, you want to delete this menu category?"
        }else{
            message = "Are you sure, you want to restore this menu category?"
        }
        builder.setMessage(message)
//        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            dialogInterface.dismiss()
            changeMenuCategoryStatus(menuCategory, !menuCategory.active!!)
        }

        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }


}