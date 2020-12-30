package com.sagar.logutil.checqk.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.sagar.logutil.R
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.network.network_utils.NetworkUtils.Companion.coroutineScope
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.GenericBaseObservable
import com.sagar.logutil.checqk.utils.Utils
import com.sagar.logutil.checqk.view.login.remote.UserRepository
import com.sagar.logutil.databinding.AlreadyLoginPopupLayoutBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class MenuImagesViewModel : GenericBaseObservable {

    var activity: Activity
    var application: Application
    var showProgressBar = MutableLiveData<Boolean>(false)
    private var userRepository: UserRepository
    private var sharedPreferences: SharedPreferences
    private var outLetId = 0L
    val menuImageCategoryList = MutableLiveData<ArrayList<CatalogueImageCategory>>()
    val menuImagesList = MutableLiveData<ArrayList<CatalogueImageCategory>>()
    var imageUrisListMutable = MutableLiveData<ArrayList<Uri>>()
    var fileName: String? = ""
    val categoryRecyclerVisibility = ObservableBoolean(false)

    constructor(
        activity: Activity,
        application: Application,
        owner: LifecycleOwner?,
        view: View?,
        userRepository: UserRepository
    ) : super(application, owner, view) {
        this.activity = activity
        this.application = application
        this.userRepository = userRepository
        sharedPreferences = Constants.getSharedPreferences(application)
        outLetId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong()
                ?: 0L


    }

    fun getMenuImages() {
        showProgressBar.value = true
        getMenuCategory()
    }

    private fun getMenuCategory() {
        val commonListingDTO = CommonListingDTO()
        val commonSortList = ArrayList<CommonSortDTO>()
        val commonSortDTO = CommonSortDTO()
        commonSortDTO.sortField = "catalogueImageCategoryId"
        commonSortDTO.sortOrder = "asc"
        commonSortList.add(commonSortDTO)
        commonListingDTO.sort = commonSortList
        commonListingDTO.length = Constants.PAGINATION_PAGE_DATA_COUNT
        userRepository.getMenuImageCategory(
            commonListingDTO,
            object : IDataSourceCallback<CommonCategoryDTO> {

                override fun onDataFound(data: CommonCategoryDTO) {
//                    Log.e("data", data.toString())
                    showProgressBar.value = false
                    menuImageCategoryList.value = data.catalogueImageCategoryList
                    getAllMenuImages()

                }

                override fun onDataNotFound() {
                    showProgressBar.value = false
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    Constants.showToastMessage(getmContext(), error)
                }
            })

    }

    fun onCategoryTxtClick() {
        if (categoryRecyclerVisibility.get()) {
            categoryRecyclerVisibility.set(false)
        } else {
            categoryRecyclerVisibility.set(true)
        }
    }

    fun saveMenuImagesByCategory(catalogueImageCategoryId: Long) {
        val imagesUrisList = imageUrisListMutable.value
        val arrayListBase64 = ArrayList<String>()

        if (imagesUrisList != null && imagesUrisList.size > 0) {
            showProgressBar.value = true

            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    for (imageUri in imagesUrisList) {
                        val galleryImageBase64 = Utils.getBitmapFromUri(application, imageUri)
                            ?.let { Utils.convertBitmapToBase64(it) }
                        galleryImageBase64?.let { arrayListBase64.add(it) }
                    }
                }
                activity.runOnUiThread {
                    val menuImageCategoryDTO = CatalogueImageCategory()
                    val menuImageDTOList = ArrayList<ImageDTO>()
                    outLetId.let { menuImageCategoryDTO.outletId = it }
                    if (arrayListBase64.isNotEmpty()) {
                        for (imageBase64 in arrayListBase64) {
                            fileName = "MENU_IMG" + System.currentTimeMillis() + ".jpg"
                            val imageDTO = ImageDTO()
                            imageDTO.base64 = imageBase64
                            imageDTO.documentName = fileName
                            menuImageDTOList.add(imageDTO)
                        }
                    }
                    menuImageCategoryDTO.catalogueImageCategoryId = catalogueImageCategoryId
                    menuImageCategoryDTO.documentGetDtos = menuImageDTOList
                    saveMenuImagesByCategoryServiceCall(menuImageCategoryDTO)
                }

            }
        }
    }

    private fun getAllMenuImages() {
        outLetId.let {
            userRepository.getAllMenuImages(
                outLetId,
                object : IDataSourceCallback<ArrayList<CatalogueImageCategory>> {

                    override fun onDataFound(data: ArrayList<CatalogueImageCategory>) {
                        menuImagesList.value = data
                    }

                    override fun onError(error: String) {
                        Constants.showToastMessage(getmContext(), error)
                    }
                })
        }
    }

    private fun saveMenuImagesByCategoryServiceCall(catalogueImageCategory: CatalogueImageCategory) {
        userRepository.saveMenuImagesByCategory(
            catalogueImageCategory,
            object : IDataSourceCallback<Long> {

                override fun onDataFound(data: Long) {
                    showProgressBar.value = false
                    getAllMenuImages()

                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    Constants.showToastMessage(getmContext(), error)
                }
            })
    }

    fun removeMenuImageById(imageId: Long, id: Long) {
        showProgressBar.value = true
        userRepository.removeMenuImageByImageId(imageId,id, object : IDataSourceCallback<String> {
            override fun onDataFound(data: String) {
                showProgressBar.value = false
                getAllMenuImages()
            }

            override fun onError(error: String) {
                showProgressBar.value = false
                Constants.showToastMessage(getmContext(),error)
            }

        })
    }

    fun showDiscardDialog(
        catalogueImageCategory: CatalogueImageCategory,
        catalogueImageCategoryList: ArrayList<CatalogueImageCategory>
    ) {

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
            getmContext().resources.getString(R.string.this_will_remove_the_category)
        alreadyLoginPopUpLayoutBinding.yesBtn.setOnClickListener(View.OnClickListener {
            //service call for discarding the category and images
            serviceCallForDiscardingCategoryImages(catalogueImageCategory,catalogueImageCategoryList,dialog)
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

    private fun serviceCallForDiscardingCategoryImages(
        catalogueImageCategory: CatalogueImageCategory,
        catalogueImageCategoryList: java.util.ArrayList<CatalogueImageCategory>,
        dialog: AlertDialog
    ) {
        showProgressBar.value = true
        userRepository.discardMenuImagesByCategoryId(
            outLetId,
            catalogueImageCategory.catalogueImageCategoryId,
            object : IDataSourceCallback<String> {

                override fun onDataFound(data: String) {
                    showProgressBar.value = false
                    for(item in catalogueImageCategoryList){
                        if(item.catalogueImageCategoryId == catalogueImageCategory.catalogueImageCategoryId){
                            item.documentGetDtos?.clear()
                            catalogueImageCategory.showChecked.set(false)
                            break
                        }
                    }
//                    this@MenuImagesViewModel.menuImageCategoryList.value = menuImageCategoryList
                    getMenuCategory()
                    getAllMenuImages()
                    getmSnackbar().value = data
                    dialog.dismiss()
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            }
        )
    }


}