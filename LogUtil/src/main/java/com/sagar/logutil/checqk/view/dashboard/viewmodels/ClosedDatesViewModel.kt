package com.astrika.checqk.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.net.Uri
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.checqk.model.*
import com.astrika.checqk.network.network_utils.IDataSourceCallback
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.GenericBaseObservable
import com.astrika.checqk.utils.Utils
import com.astrika.checqk.view.login.UserLoginActivity
import com.astrika.checqk.view.login.remote.UserRepository

class ClosedDatesViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var userRepository: UserRepository
) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>(false)
    private var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)
    private var outLetId = 0L
    var isNext = MutableLiveData<Boolean>(false)

    var selectedDetailImageName = MutableLiveData<String>("Cropped Image uploaded")

    var startDate = MutableLiveData<String>("")
    var occasion = MutableLiveData<String>("")
    var errorMsg = MutableLiveData<String>("")
    var safetyMeasuresErrorMsg = MutableLiveData<String>("")

    var openTimingsLayoutVisible = MutableLiveData<Boolean>(false)
    var closedDatesLayoutVisible = MutableLiveData<Boolean>(false)
    var safetyMeasuresLayoutVisible = MutableLiveData<Boolean>(false)
    var safetyMeasuresProfileImageVisible = MutableLiveData<Boolean>(false)
    var safetyMeasuresDetailImageVisible = MutableLiveData<Boolean>(false)


    private var closedDatesArrayList = ArrayList<ClosedDatesDTO>()
    var closedDatesListMutableLiveData = MutableLiveData<List<ClosedDatesDTO>>()

    // Safety Measures
    var safetyMeasuresDetailsArrayList = ArrayList<SafetyMeasuresDetailsDTO>()
    var safetyMeasuresDetailsListMutableLiveData = MutableLiveData<List<SafetyMeasuresDetailsDTO>>(
        emptyList()
    )

    var outletSecurityMeasuresDTO = OutletSecurityMeasuresDTO()
    var safetyMeasuresDetailTitle = MutableLiveData<String>("")
    var profilePath = ObservableField<String>("")
    var coverUri: Uri? = null
    var detailUri: Uri? = null
    var isCoverPic = false

    init {
        outLetId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L
        populateClosedDates()
    }

    private fun populateClosedDates() {

        showProgressBar.value = true
        userRepository.fetchOutletClosedDates(
            outLetId,
            object : IDataSourceCallback<ArrayList<ClosedDatesDTO>> {

                override fun onDataFound(data: ArrayList<ClosedDatesDTO>) {
                    closedDatesArrayList.clear()
                    if (!data.isNullOrEmpty()) {
                        for (item in data) {
                            item.displayDate =
                                Utils.getDatePickerFormattedDate4(item.date).toString()
                        }
                    }
                    closedDatesArrayList = data
                    closedDatesListMutableLiveData.value = closedDatesArrayList
                    populateSafetyMeasures()

                }

                override fun onDataNotFound() {
                    populateSafetyMeasures()
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                    populateSafetyMeasures()
                }
            })
    }


    private fun populateSafetyMeasures() {

        userRepository.fetchSafetyMeasures(
            outLetId,
            object : IDataSourceCallback<OutletSecurityMeasuresDTO> {

                override fun onDataFound(data: OutletSecurityMeasuresDTO) {
                    showProgressBar.value = false
                    outletSecurityMeasuresDTO = data

                    if (!data.outletImageDTO?.documentGetDto?.path.isNullOrBlank()) {
                        safetyMeasuresProfileImageVisible.value = true
                        profilePath.set(data.outletImageDTO?.documentGetDto?.path)
                    }

                    safetyMeasuresDetailsArrayList = data.secMeasuresDTOs ?: arrayListOf()
                    safetyMeasuresDetailsListMutableLiveData.value = safetyMeasuresDetailsArrayList
                }

                override fun onDataNotFound() {
                    showProgressBar.value = false
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })
    }

    fun onAddOccasion() {

        if (!startDate.value.isNullOrBlank() && !occasion.value.isNullOrBlank()) {


            val closedDatesDTO = ClosedDatesDTO()

            closedDatesDTO.outletId = outLetId
            closedDatesDTO.occasion = occasion.value ?: ""
            closedDatesDTO.date =
                Utils.getDatePickerFormattedSavingDate(startDate.value ?: "").toString()
            closedDatesDTO.displayDate =
                Utils.getDatePickerFormattedDate3(startDate.value ?: "").toString()

            var isDatePresent = false
            for (item in closedDatesArrayList ?: arrayListOf()) {
                if (item.date.equals(closedDatesDTO.date, true)) {
                    isDatePresent = true
                    break
                }
            }

            if (!isDatePresent) {
                showProgressBar.value = true

                userRepository.saveOutletClosedDates(
                    closedDatesDTO,
                    object : IDataSourceCallback<String> {
                        override fun onDataFound(data: String) {
                            showProgressBar.value = false
                            closedDatesArrayList.add(closedDatesDTO)
                            closedDatesListMutableLiveData.value = closedDatesArrayList
                            getmSnackbar().value = data
                            startDate.value = ""
                            occasion.value = ""
                            errorMsg.value = ""
                        }

                        override fun onError(error: String) {
                            showProgressBar.value = false
                            getmSnackbar().value = error
                        }
                    })

            } else {
                errorMsg.value = "Start date is already present"
            }

        } else if (startDate.value.isNullOrBlank()) {
            errorMsg.value = "Please enter start date"

        } else if (occasion.value.isNullOrBlank()) {
            errorMsg.value = "Please enter occasion"
        }
    }

    /*

        fun onSaveClosedDatesClick() {

            showProgressBar.value = true

            val closedDatesDTO = ClosedDatesDTO()
            userRepository.saveOutletClosedDates(
                closedDatesDTO,
                object : IDataSourceCallback<String> {
                    override fun onDataFound(data: String) {
                        showProgressBar.value = false
                        getmSnackbar().value = data
                    }

                    override fun onError(error: String) {
                        showProgressBar.value = false
                        getmSnackbar().value = error
                    }
                })

        }

    */
    fun onRemoveItem(position: Int, closedDatesDTO: ClosedDatesDTO) {

        if (closedDatesDTO.outletDateRestrictionId != 0L) {
            showProgressBar.value = true
            userRepository.deleteOutletClosedDate(
                closedDatesDTO.outletDateRestrictionId,
                false,
                object : IDataSourceCallback<String> {

                    override fun onDataFound(data: String) {
                        showProgressBar.value = false
                        getmSnackbar().value = data
                        closedDatesArrayList.removeAt(position)
                        closedDatesListMutableLiveData.value = closedDatesArrayList
                    }

                    override fun onError(error: String) {
                        showProgressBar.value = false
                        getmSnackbar().value = error
                    }
                })


        } else {
            closedDatesArrayList.removeAt(position)
            closedDatesListMutableLiveData.value = closedDatesArrayList
        }

    }

    fun onOpeningTimingClick() {
        openTimingsLayoutVisible.value = !openTimingsLayoutVisible.value!!
    }

    fun onClosedDatesClick() {
        closedDatesLayoutVisible.value = !closedDatesLayoutVisible.value!!
    }

    fun onSafetyMeasuresClick() {
        safetyMeasuresLayoutVisible.value = !safetyMeasuresLayoutVisible.value!!
    }

    fun onSafetyMeasuresAddDetailsClick() {

        if (!safetyMeasuresDetailTitle.value.isNullOrBlank() && detailUri != null) {

            val safetyMeasuresDetailsDTO = SafetyMeasuresDetailsDTO()
            safetyMeasuresDetailsDTO.title = safetyMeasuresDetailTitle.value ?: ""
            safetyMeasuresDetailsDTO.imageUri = detailUri
            safetyMeasuresDetailsArrayList.add(safetyMeasuresDetailsDTO)
            safetyMeasuresDetailsListMutableLiveData.value = safetyMeasuresDetailsArrayList

            safetyMeasuresDetailTitle.value = ""
            detailUri = null
            safetyMeasuresDetailImageVisible.value = false
            safetyMeasuresErrorMsg.value = ""
        } else if (safetyMeasuresDetailTitle.value.isNullOrBlank()) {
            safetyMeasuresErrorMsg.value = "Please enter title"

        } else if (occasion.value.isNullOrBlank()) {
            safetyMeasuresErrorMsg.value = "Please add image"
        }
    }

    fun onRemoveSafetyMeasuresDetailItem(
        position: Int,
        safetyMeasuresDetailsDTO: SafetyMeasuresDetailsDTO
    ) {

        if (safetyMeasuresDetailsDTO.outletSecurityMeasuresId != 0L) {
            showProgressBar.value = true
            userRepository.deleteOutletSecurityMeasureById(
                safetyMeasuresDetailsDTO.outletSecurityMeasuresId,
                false,
                object : IDataSourceCallback<String> {

                    override fun onDataFound(data: String) {
                        showProgressBar.value = false
                        getmSnackbar().value = data
                        safetyMeasuresDetailsArrayList.removeAt(position)
                        safetyMeasuresDetailsListMutableLiveData.value =
                            safetyMeasuresDetailsArrayList
                    }

                    override fun onError(error: String) {
                        showProgressBar.value = false
                        getmSnackbar().value = error
                    }
                })


        } else {
            safetyMeasuresDetailsArrayList.removeAt(position)
            safetyMeasuresDetailsListMutableLiveData.value = safetyMeasuresDetailsArrayList
        }

    }

    fun onSaveSafetyMeasuresClick() {

        if (coverUri != null) {
            val imageDTO = ImageDTO()
            imageDTO.documentName = "CoverImage$outLetId.jpg"
            imageDTO.base64 = Utils.getBitmapFromUri(application, coverUri)
                ?.let { Utils.convertBitmapToBase64(it) }
            outletSecurityMeasuresDTO.outletImageDTO = OutletImageDTO()
            outletSecurityMeasuresDTO.outletImageDTO?.documentGetDto = imageDTO
            outletSecurityMeasuresDTO.outletImageDTO?.outletId = outLetId
        }

        outletSecurityMeasuresDTO.secMeasuresDTOs = arrayListOf()
        for ((i, item) in safetyMeasuresDetailsArrayList.withIndex() ?: arrayListOf()) {

            if (item.imageUri != null || !item.outletSecMeasuresDetailImage.path.isNullOrBlank()) {

                if (item.imageUri != null) {
                    val imageDTO = ImageDTO()
                    imageDTO.documentName = "Detail_Image_" + outLetId + "_" + (i + 1) + ".jpg"
                    imageDTO.base64 = Utils.getBitmapFromUri(
                        application,
                        item.imageUri
                    )
                        ?.let { Utils.convertBitmapToBase64(it) }

                    val safetyMeasuresDetailsDTO = SafetyMeasuresDetailsDTO()
                    safetyMeasuresDetailsDTO.outletId = outLetId
                    safetyMeasuresDetailsDTO.title = item.title ?: ""
                    safetyMeasuresDetailsDTO.outletSecMeasuresDetailImage = imageDTO
                    outletSecurityMeasuresDTO.secMeasuresDTOs?.add(safetyMeasuresDetailsDTO)

                } else {
                    outletSecurityMeasuresDTO.secMeasuresDTOs?.add(item)

                }
            }

/*

            if (item.binding?.safetyMeasuresDetailsDTO?.title?.isNotBlank() == true
                || item.binding?.safetyMeasuresDetailsDTO?.imageUri != null
                || item.image?.path?.isNotBlank() == true
            ) {


                if (item.binding?.safetyMeasuresDetailsDTO?.imageUri != null) {
                    val imageDTO = ImageDTO()
                    imageDTO.documentName = "Detail_Image_" + outLetId + "_" + (i + 1) + ".jpg"
                    imageDTO.base64 = Utils.getBitmapFromUri(
                        application,
                        item.binding?.safetyMeasuresDetailsDTO?.imageUri
                    )
                        ?.let { Utils.convertBitmapToBase64(it) }

                    val safetyMeasuresDetailsDTO = SafetyMeasuresDetailsDTO()
                    safetyMeasuresDetailsDTO.title =
                        item.binding?.safetyMeasuresDetailsDTO?.title ?: ""
                    safetyMeasuresDetailsDTO.image = imageDTO
                    outletSecurityMeasuresDTO.outletSecurityMeasuresDetailImage?.add(
                        safetyMeasuresDetailsDTO
                    )

                } else {
                    outletSecurityMeasuresDTO.outletSecurityMeasuresDetailImage?.add(item)

                }
            }
*/

        }


        /* if (detailUri != null) {
             val imageDTO = ImageDTO()
             imageDTO.documentName = "DetailImage$outLetId.jpg"
             imageDTO.base64 = Utils.getBitmapFromUri(application, detailUri)
                 ?.let { Utils.convertBitmapToBase64(it) }
             outletSecurityMeasuresDTO.outletSecurityMeasuresDetailImage = imageDTO
             isNewImagePresent = true
         }
 */

//        if (isNewImagePresent) {

        showProgressBar.value = true

        userRepository.saveSafetyMeasures(
            outletSecurityMeasuresDTO,
            object : IDataSourceCallback<String> {
                override fun onDataFound(data: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = data
                    isNext.value = true

                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })

/*
        } else {
            safetyMeasuresErrorMsg.value = "Please add safety image"
        }
*/
    }

    fun onLoginClick() {
        Constants.changeActivity<UserLoginActivity>(activity)
    }

}