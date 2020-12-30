package com.sagar.logutil.checqk.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.source.DashboardRepository
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.GenericBaseObservable

class DishCategoryViewModel : GenericBaseObservable {

    var activity: Activity
    var application: Application
    var showProgressBar = MutableLiveData<Boolean>()
    var menuSectionNotAddedVisibility = MutableLiveData<Boolean>(false)
    var menuCategoryMutableArrayList = MutableLiveData<ArrayList<CategoryCatalogueDTO>>()
    var menuSectionMutableArrayList = MutableLiveData<ArrayList<ProductWithSectionDetails>>()
    var menuSectionList = ArrayList<ProductWithSectionDetails>()
    private var dashboardRepository: DashboardRepository
    private var sharedPreferences: SharedPreferences
    var outLetId = 0L
    var catalogueCategoryId = 0L
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

    fun getAllMenuCategories(){
        dashboardRepository.getAllMenuCategories(outLetId, object : IDataSourceCallback<GetCatalogueCategoriesResponseDTO>{
            override fun onDataFound(data: GetCatalogueCategoriesResponseDTO) {
                menuCategoryMutableArrayList.value = data.activeCatalogueCategoryDTOs
                catalogueCategoryId = data.activeCatalogueCategoryDTOs[0].catalogueCategoryId?:0
                getAllDishesWithSections()
            }

            override fun onDataNotFound() {
                menuCategoryMutableArrayList.value = arrayListOf()
                showProgressBar.value = false
            }

            override fun onError(error: String) {
                menuCategoryMutableArrayList.value = arrayListOf()
                getmSnackbar().value = error
                showProgressBar.value = false
            }
        })
    }

    fun getAllDishesWithSections(){

        showProgressBar.value = true
        dashboardRepository.getAllDishes(catalogueCategoryId, outLetId,"", object : IDataSourceCallback<ArrayList<ProductWithSectionDetails>>{
            override fun onDataFound(data: ArrayList<ProductWithSectionDetails>) {
                menuSectionList = data
                menuSectionMutableArrayList.value = data
                menuSectionNotAddedVisibility.value = false
                showProgressBar.value = false
            }

            override fun onDataNotFound() {
                menuSectionList = arrayListOf()
                menuSectionMutableArrayList.value = arrayListOf()
                menuSectionNotAddedVisibility.value = true
                showProgressBar.value = false
            }

            override fun onError(error: String) {
                getmSnackbar().value = error
                showProgressBar.value = false
            }
        })
    }

    fun saveMenuSection(catalogueSection : CatalogueSectionDTO){

        showProgressBar.value = true
        dashboardRepository.saveMenuSection(catalogueSection, object : IDataSourceCallback<Long>{
            override fun onDataFound(data: Long) {

//                menuSectionList = menuSectionMutableArrayList.value!!
                /*if(!menuSection.catalogueSectionId!!.equals(0L)){
                    menuSectionList.set(index, menuSection)
                }else{
                    menuSection.catalogueSectionId = data
                    menuSectionList.add(menuSection)
                }
                menuSectionMutableArrayList.value = menuCategoryList*/

                getmSnackbar().value = "Menu Section save successfully"
                showProgressBar.value = false

                getAllDishesWithSections()
            }

            override fun onError(error: String) {
                getmSnackbar().value = error
                showProgressBar.value = false
            }
        })
    }

    fun changeDishStatus(productDetailsDTO: ProductDetailsDTO, status : Boolean){
        showProgressBar.value = true
        dashboardRepository.changeDishStatus(productDetailsDTO.productId!!, status,
            object : IDataSourceCallback<String>{
                override fun onDataFound(data: String) {
                    getmSnackbar().value = data
                    showProgressBar.value = false
                    getAllDishesWithSections()
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                    showProgressBar.value = false
                }
            })
    }


    fun getInactiveDishes(dishCatalogueSection: CatalogueSectionDTO){
        showProgressBar.value = true
        dashboardRepository.getInactiveDishes(dishCatalogueSection.catalogueCategoryId!!, dishCatalogueSection.catalogueSectionId!!,
            dishCatalogueSection.outletId!!, object : IDataSourceCallback<ArrayList<ProductDetailsDTO>>{
                override fun onDataFound(data: ArrayList<ProductDetailsDTO>) {

                    showProgressBar.value = false
                }

                override fun onDataNotFound() {

                    showProgressBar.value = false
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                    showProgressBar.value = false
                }
            })
    }

    fun saveDishSequence(productList :  ArrayList<ProductDetailsDTO>){
//        showProgressBar.value = true
        dashboardRepository.saveDishSequence(productList, object : IDataSourceCallback<String>{
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

    fun confirmationDialogForDelete(context : Context, productDetailsDTO: ProductDetailsDTO){
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Confirmation")
        //set message for alert dialog
        var message = ""
        if(productDetailsDTO.active!!){
            message = "Are you sure, you want to delete this dish?"
        }else{
            message = "Are you sure, you want to restore this dish?"
        }
        builder.setMessage(message)
//        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            dialogInterface.dismiss()
            changeDishStatus(productDetailsDTO, !productDetailsDTO.active!!)
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