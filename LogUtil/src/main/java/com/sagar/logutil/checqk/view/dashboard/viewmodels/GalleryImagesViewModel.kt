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
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.sagar.logutil.R
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.network.network_utils.NetworkUtils.Companion.coroutineScope
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.GenericBaseObservable
import com.sagar.logutil.checqk.utils.Utils
import com.sagar.logutil.checqk.view.login.UserLoginActivity
import com.sagar.logutil.checqk.view.login.remote.UserRepository
import com.sagar.logutil.databinding.AlreadyLoginPopupLayoutBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class GalleryImagesViewModel : GenericBaseObservable {

    var activity: Activity
    var application: Application
    var showProgressBar = MutableLiveData<Boolean>()
    private var userRepository: UserRepository
    private var sharedPreferences: SharedPreferences
    private var outLetId = 0L
    var profileImageUri: Uri? = null
    private var restaurantProfileImageBase64: String? = ""
    var fileName: String? = ""
    val galleryImageCategoryList = MutableLiveData<ArrayList<GalleryImageCategory>>()
    val galleryImagesList = MutableLiveData<ArrayList<GalleryImageCategory>>()
    val categoryRecyclerVisibility = ObservableBoolean(false)
    val profileImagePath = ObservableField<String>()
    var imageUrisListMutable = MutableLiveData<ArrayList<Uri>>()

    var isPreLoginContinueClickedMutableLiveData = MutableLiveData<Boolean>(false)


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

    fun saveRestaurantProfileImage(profileImageUri: Uri?) {

        if (profileImageUri != null) {
            showProgressBar.value = true
            fileName = "PROFILE_IMG" + System.currentTimeMillis() + ".jpg"
            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    restaurantProfileImageBase64 =
                        Utils.getBitmapFromUri(application, profileImageUri)
                            ?.let { Utils.convertBitmapToBase64(it) }
                }

                activity.runOnUiThread {
                    if (!restaurantProfileImageBase64.isNullOrBlank()) {
                        val restaurantProfileImageDTO = RestaurantProfileImageDTO()
                        outLetId.let { restaurantProfileImageDTO.outletId = it }
                        val profileImageDTO = ImageDTO()
                        if (restaurantProfileImageBase64 != null) {
                            profileImageDTO.base64 = restaurantProfileImageBase64
                            profileImageDTO.documentName = fileName
                            restaurantProfileImageDTO.documentGetDto = profileImageDTO
                            saveRestaurantProfileImageServiceCall(restaurantProfileImageDTO)
                        }
                    }

                }

            }
        }
    }


    private fun saveRestaurantProfileImageServiceCall(restaurantProfileImageDTO: RestaurantProfileImageDTO) {
        userRepository.saveRestaurantProfileImage(restaurantProfileImageDTO,
            object : IDataSourceCallback<String> {

                override fun onDataFound(data: String) {
                    showProgressBar.value = false
                    if (data == "success") {
                        Constants.showToastMessage(getmContext(), "Image saved successfully")
                    }
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    Constants.showToastMessage(getmContext(), error)
                }
            })

    }

    fun getGalleryImages() {
        showProgressBar.value = true
        getRestaurantProfileImage()
        getImageCategory()
//        getRestaurantGalleryImages()
    }

    private fun getRestaurantProfileImage() {
        outLetId.let {
            userRepository.getRestaurantProfileImage(
                it,
                object : IDataSourceCallback<RestaurantProfileImageDTO> {

                    override fun onDataFound(data: RestaurantProfileImageDTO) {
                        showProgressBar.value = false
                        data.documentGetDto.let { docDto ->
                            profileImagePath.set(docDto?.path)
                        }
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
    }

    private fun getImageCategory() {
        val commonListingDTO = CommonListingDTO()
        val commonSortList = ArrayList<CommonSortDTO>()
        val commonSortDTO = CommonSortDTO()
        commonSortDTO.sortField = "galleryImageCategoryId"
        commonSortDTO.sortOrder = "asc"
        commonSortList.add(commonSortDTO)
        commonListingDTO.sort = commonSortList
        commonListingDTO.length = Constants.PAGINATION_PAGE_DATA_COUNT
        userRepository.getGalleryImageCategory(
            commonListingDTO,
            object : IDataSourceCallback<CommonCategoryDTO> {

                override fun onDataFound(data: CommonCategoryDTO) {
//                    Log.e("data", data.toString())
                    galleryImageCategoryList.value = data.galleryImageCategoryList
                    getRestaurantGalleryImages()
                }

                override fun onDataNotFound() {
                    showProgressBar.value = false
                }

                override fun onError(error: String) {
                    Constants.showToastMessage(getmContext(), error)
                }
            })
    }

    private fun getRestaurantGalleryImages() {
        outLetId.let {
            userRepository.getAllGalleryImages(
                outLetId,
                object : IDataSourceCallback<ArrayList<GalleryImageCategory>> {

                    override fun onDataFound(data: ArrayList<GalleryImageCategory>) {
                        showProgressBar.value = false
                        galleryImagesList.value = data
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
    }

    fun onCategoryTxtClick() {
        if (categoryRecyclerVisibility.get()) {
            categoryRecyclerVisibility.set(false)
        } else {
            categoryRecyclerVisibility.set(true)
        }
    }

    fun saveGalleryImagesByCategory(galleryImageCategoryId: Long) {
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
                    val galleryImageCategoryDTO = GalleryImageCategory()
                    val galleryImageDTOList = ArrayList<ImageDTO>()
                    outLetId.let { galleryImageCategoryDTO.outletId = it }
                    if (arrayListBase64.isNotEmpty()) {
                        for (imageBase64 in arrayListBase64) {
                            fileName = "REST_IMG" + System.currentTimeMillis() + ".jpg"
                            val imageDTO = ImageDTO()
                            imageDTO.base64 = imageBase64
                            imageDTO.documentName = fileName
                            galleryImageDTOList.add(imageDTO)
                        }
                    }
                    galleryImageCategoryDTO.galleryImageCategoryId = galleryImageCategoryId
                    galleryImageCategoryDTO.documentGetDtos = galleryImageDTOList
                    saveGalleryImagesByCategoryServiceCall(galleryImageCategoryDTO)
                }

            }
        }
    }

    private fun saveGalleryImagesByCategoryServiceCall(galleryImageCategoryDTO: GalleryImageCategory) {
        userRepository.saveGalleryImagesByCategory(
            galleryImageCategoryDTO,
            object : IDataSourceCallback<Long> {

                override fun onDataFound(data: Long) {
                    showProgressBar.value = false
                    getRestaurantGalleryImages()
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    Constants.showToastMessage(getmContext(), error)
                }
            })
    }

    fun removeGalleryImageById(imageId: Long, id: Long) {
        showProgressBar.value = true
        userRepository.removeGalleryImageByImageId(
            imageId,
            id,
            object : IDataSourceCallback<String> {
                override fun onDataFound(data: String) {
                    showProgressBar.value = false
                    getRestaurantGalleryImages()

                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    Constants.showToastMessage(getmContext(), error)
                }

            })
    }

    fun showDiscardDialog(
        galleryImageCategory: GalleryImageCategory,
        galleryImageCategoryList: ArrayList<GalleryImageCategory>
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
            serviceCallForDiscardingCategoryImages(
                galleryImageCategory,
                galleryImageCategoryList,
                dialog
            )
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
        galleryImageCategory: GalleryImageCategory,
        galleryImageCategoryList: ArrayList<GalleryImageCategory>,
        dialog: AlertDialog
    ) {
        showProgressBar.value = true
        userRepository.discardGalleryImagesByCategoryId(
            outLetId,
            galleryImageCategory.galleryImageCategoryId,
            object : IDataSourceCallback<String> {

                override fun onDataFound(data: String) {
                    showProgressBar.value = false
                    for (item in galleryImageCategoryList) {
                        if (item.galleryImageCategoryId == galleryImageCategory.galleryImageCategoryId) {
                            item.documentGetDtos?.clear()
                            galleryImageCategory.showChecked.set(false)
                            break
                        }
                    }
                    getImageCategory()
                    getRestaurantGalleryImages()
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


    fun onSaveClick() {
        isPreLoginContinueClickedMutableLiveData.value = true
    }

    fun onLoginClick() {
        Constants.changeActivity<UserLoginActivity>(activity)
    }


}