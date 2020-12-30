package com.sagar.logutil.checqk.view.dashboard.viewmodels

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
import com.sagar.logutil.R
import com.sagar.logutil.checqk.model.CatalogueSectionDTO
import com.sagar.logutil.checqk.model.CategoryCatalogueDTO
import com.sagar.logutil.checqk.model.GetCatalogueCategoriesResponseDTO
import com.sagar.logutil.checqk.model.GetMenuSectionsResponseDTO
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.source.DashboardRepository
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.GenericBaseObservable
import com.sagar.logutil.databinding.AddMenuSectionDialoagLayoutBinding

class MenuSectionViewModel : GenericBaseObservable {

    var activity: Activity
    var application: Application
    var showProgressBar = MutableLiveData<Boolean>()
    var menuSectionNotAddedVisibility = MutableLiveData<Boolean>(false)
//    var noInactiveMenuSectionVisibility = MutableLiveData<Boolean>(false)
    var inactiveMenuSectionRecyclerVisibility = MutableLiveData<Boolean>(false)
    var noInactiveMenuSectionTextVisibility = MutableLiveData<Boolean>(false)
    var menuCategoryMutableArrayList = MutableLiveData<ArrayList<CategoryCatalogueDTO>>()
    var menuSectionMutableArrayList = MutableLiveData<ArrayList<CatalogueSectionDTO>>()
    var inactiveMenuSectionMutableArrayList = MutableLiveData<ArrayList<CatalogueSectionDTO>>()
    var menuSectionList = ArrayList<CatalogueSectionDTO>()
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
        dashboardRepository.getAllMenuCategories( outLetId, object : IDataSourceCallback<GetCatalogueCategoriesResponseDTO>{
            override fun onDataFound(data: GetCatalogueCategoriesResponseDTO) {
                menuCategoryMutableArrayList.value = data.activeCatalogueCategoryDTOs
                catalogueCategoryId = data.activeCatalogueCategoryDTOs[0].catalogueCategoryId?:0
                getAllMenuSections()
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

    fun getAllMenuSections(){

        showProgressBar.value = true
        dashboardRepository.getAllMenuSections(catalogueCategoryId, outLetId,
            object : IDataSourceCallback<GetMenuSectionsResponseDTO>{
            override fun onDataFound(data: GetMenuSectionsResponseDTO) {
                menuSectionList = data.activeCatalogueSectionDTOs
                menuSectionMutableArrayList.value = data.activeCatalogueSectionDTOs
                inactiveMenuSectionMutableArrayList.value = data.inActiveCatalogueSectionDTOs
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
                menuSectionList = arrayListOf()
                menuSectionMutableArrayList.value = arrayListOf()
                menuSectionNotAddedVisibility.value = true
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

                getAllMenuSections()
            }

            override fun onError(error: String) {
                getmSnackbar().value = error
                showProgressBar.value = false
            }
        })
    }

    fun saveMenuSectionsSequence(catalogueSectionList :  ArrayList<CatalogueSectionDTO>){
//        showProgressBar.value = true
        dashboardRepository.saveMenuSectionsSequence(catalogueSectionList,
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

    fun changeMenuSectionStatus(catalogueSection : CatalogueSectionDTO, status : Boolean){
        showProgressBar.value = true
        dashboardRepository.changeMenuSectionStatus(catalogueSection.catalogueSectionId!!, status,
            object : IDataSourceCallback<String>{
                override fun onDataFound(data: String) {
//                    menuSectionList = menuSectionMutableArrayList.value!!
                    /*menuSectionList.removeAt(index)
                    menuSectionMutableArrayList.value = menuSectionList*/
                    getmSnackbar().value = data
                    showProgressBar.value = false
                    getAllMenuSections()
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                    showProgressBar.value = false
                }
            })
    }

    fun onAddClick(){
        showAddMenuSectionDialog(null)
    }

    fun onShowInactiveClick(){
//        inactiveMenuSectionRecyclerVisibility.value = !inactiveMenuSectionRecyclerVisibility.value!!
        if(inactiveMenuSectionMutableArrayList.value!!.size > 0){
            inactiveMenuSectionRecyclerVisibility.value = !inactiveMenuSectionRecyclerVisibility.value!!
        }else{
            noInactiveMenuSectionTextVisibility.value = !noInactiveMenuSectionTextVisibility.value!!
        }
    }

    fun showAddMenuSectionDialog(catalogueSection : CatalogueSectionDTO?) {

        val inflater = activity.layoutInflater
//        val alertLayout: View = inflater.inflate(R.layout.add_menu_category_dialoag_layout, null)
        var binding: AddMenuSectionDialoagLayoutBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.add_menu_section_dialoag_layout,
            null,
            false
        )
        val alert = AlertDialog.Builder(activity)

        alert.setView(binding.root)
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false)
        val dialog = alert.create()

        if (catalogueSection != null && catalogueSection!!.catalogueSectionName!!.isNotEmpty()){
            binding.menuSectionNameEditText.setText(catalogueSection!!.catalogueSectionName!!)
            binding.taxValueEditText.setText(catalogueSection!!.taxValue!!.toString())
        }

        binding.closeDialogImageView.setOnClickListener { dialog.dismiss() }

        binding.saveButton.setOnClickListener {
            if(binding.menuSectionNameEditText.text.isEmpty()){
                binding.menuSectionErrorTextView.visibility = View.VISIBLE
                binding.taxValueErrorTextView.visibility = View.GONE
            }else if(binding.taxValueEditText.text.isEmpty()){
                binding.menuSectionErrorTextView.visibility = View.GONE
                binding.taxValueErrorTextView.visibility = View.VISIBLE
            }else{
                binding.menuSectionErrorTextView.visibility = View.GONE
                if (catalogueSection != null){
                    catalogueSection.catalogueSectionName = binding.menuSectionNameEditText.text.toString()
                    catalogueSection.taxValue = binding.taxValueEditText.text.toString().toDouble()
                    saveMenuSection(catalogueSection)
                }else{
                    val menuSection = CatalogueSectionDTO(
                        0,
                        binding.menuSectionNameEditText.text.toString(),
                        catalogueCategoryId,
                        null,
                        outLetId,
                        binding.taxValueEditText.text.toString().toDouble(),
                        null
                    )
                    saveMenuSection(menuSection)
                }
                dialog.dismiss()
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


    fun confirmationDialogForDelete(context : Context, catalogueSection : CatalogueSectionDTO){
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Confirmation")
        //set message for alert dialog
        var message = ""
        if(catalogueSection.active!!){
            message = "Are you sure, you want to delete this menu section?"
        }else{
            message = "Are you sure, you want to restore this menu section?"
        }
        builder.setMessage(message)
//        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            dialogInterface.dismiss()
            changeMenuSectionStatus(catalogueSection, !catalogueSection.active!!)
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