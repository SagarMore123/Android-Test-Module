package com.astrika.checqk.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.checqk.master_controller.source.MasterRepository
import com.astrika.checqk.master_controller.source.daos.SystemValueMasterDao
import com.astrika.checqk.model.*
import com.astrika.checqk.network.network_utils.IDataSourceCallback
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.GenericBaseObservable
import com.astrika.checqk.view.login.UserLoginActivity
import com.astrika.checqk.view.login.remote.UserRepository
import com.example.opposfeapp.utils.SingleLiveEvent
import com.sagar.logutil.databinding.BasicInfoFacilityMastersItemCellLayoutBinding
import java.math.BigDecimal

class BasicInfoViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var masterRepository: MasterRepository,
    private var userRepository: UserRepository
) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>()
    private var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)
    private var outLetId = 0L

    var cuisineNameSearch = MutableLiveData<String>("")
    var clearCuisineNameSearchVisible = ObservableBoolean(false)

    var productName = MutableLiveData<String>("")
    var dishCategoryId: Long = 0L
    var dishCategoryValue = MutableLiveData<String>("")

    var cuisineErrorMsg = MutableLiveData<String>("")
    var facilityErrorMsg = MutableLiveData<String>("")
    var famousDishesErrorMsg = MutableLiveData<String>("")

    var costForTwo = MutableLiveData<String>("")

    var famousFor = MutableLiveData<String>("")
    var famousForErrorMsg = MutableLiveData<String>("")

    var outletDescription = MutableLiveData<String>("")

    var openTimingsLayoutVisible = MutableLiveData<Boolean>(false)
    var cuisineInfoLayoutVisible = MutableLiveData<Boolean>(false)
    var facilityInfoLayoutVisible = MutableLiveData<Boolean>(false)
    var famousDishesLayoutVisible = MutableLiveData<Boolean>(false)

    var isPreLoginContinueClickedMutableLiveData = MutableLiveData<Boolean>(false)


    var outletTypeArrayList = ArrayList<OutletTypeMasterDTO>()
    var outletTypeListMutableLiveData = MutableLiveData<List<OutletTypeMasterDTO>>()

    var seatingTypeArrayList = ArrayList<SystemValueMasterDTO>()
    var seatingTypeListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()

    var foodTypeArrayList = ArrayList<FoodTypeMasterDTO>()
    var foodTypeListMutableLiveData = MutableLiveData<List<FoodTypeMasterDTO>>()

    var mealTypeArrayList = ArrayList<MealTypeMasterDTO>()
    var mealTypeListMutableLiveData = MutableLiveData<List<MealTypeMasterDTO>>()

    var famousForArrayList = ArrayList<String>()
    var famousForListMutableLiveData = MutableLiveData<List<String>>()

    var cuisineArrayList = ArrayList<CuisineMasterDTO>()
    var cuisineListMutableLiveData = MutableLiveData<List<CuisineMasterDTO>>()

    var facilityArrayList = ArrayList<FacilityMasterDTO>()
    var facilityListMutableLiveData = MutableLiveData<List<FacilityMasterDTO>>()
    var facilityBindingList = ArrayList<BasicInfoFacilityMastersItemCellLayoutBinding>()

    var inMallArrayList = ArrayList<BooleanMasterDTO>()
    var inMallListMutableLiveData = MutableLiveData<List<BooleanMasterDTO>>()

    var alcoholServedArrayList = ArrayList<BooleanMasterDTO>()
    var alcoholServedListMutableLiveData = MutableLiveData<List<BooleanMasterDTO>>()

    var famousDishArrayList = ArrayList<FamousDishesDTO>()
    var famousDishListMutableLiveData = MutableLiveData<List<FamousDishesDTO>>()
    var categoryClick = SingleLiveEvent<Void>()


    init {

        outLetId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L
        populateMasters()

    }


    var restaurantMasterDTO = RestaurantMasterDTO()
    private fun populateData() {

        userRepository.fetchRestaurantDetails(outLetId,
            object : IDataSourceCallback<RestaurantMasterDTO> {

                override fun onDataFound(data: RestaurantMasterDTO) {
                    showProgressBar.value = false
                    restaurantMasterDTO = data

                    // Outlet Type
                    for (id in restaurantMasterDTO.outletType ?: arrayListOf()) {
                        for (item in outletTypeArrayList) {
                            if (id == item.outletTypeId) {
                                item.selected = true
                            }
                        }
                    }
                    outletTypeListMutableLiveData.value = outletTypeArrayList


                    // Seating Type
                    for (id in restaurantMasterDTO.seatingIds ?: arrayListOf()) {
                        for (item in seatingTypeArrayList) {
                            if (id == item.serialId) {
                                item.selected = true
                            }
                        }
                    }
                    seatingTypeListMutableLiveData.value = seatingTypeArrayList


                    // Food Type
                    for (id in restaurantMasterDTO.foodTypes ?: arrayListOf()) {
                        for (item in foodTypeArrayList) {
                            if (id == item.foodTypeId) {
                                item.selected = true
                            }
                        }
                    }
                    foodTypeListMutableLiveData.value = foodTypeArrayList


                    // Meal Type
                    for (id in restaurantMasterDTO.mealTypes ?: arrayListOf()) {
                        for (item in mealTypeArrayList) {
                            if (id == item.mealTypeId) {
                                item.selected = true
                            }
                        }
                    }
                    mealTypeListMutableLiveData.value = mealTypeArrayList


                    // Famous For
                    splitFamousForString(restaurantMasterDTO.famousFor ?: "")


                    // Cuisines
                    for (id in restaurantMasterDTO.cuisineIds ?: arrayListOf()) {
                        for (item in cuisineArrayList) {
                            if (id == item.cuisineId) {
                                item.selected = true
                            }
                        }
                    }
                    cuisineListMutableLiveData.value = cuisineArrayList

                    // Facility Info
                    for (facilityDTO in restaurantMasterDTO.facilityTextMappings ?: arrayListOf()) {
                        for (item in facilityArrayList) {
                            if (facilityDTO.facilityId == item.facilityId) {
                                item.facilityTextMappingId = facilityDTO.facilityTextMappingId
                                item.facilityValue = facilityDTO.supportText ?: ""
                                item.selected = true
                            }
                        }
                    }
                    facilityListMutableLiveData.value = facilityArrayList

                    // Famous Dishes
                    famousDishArrayList = restaurantMasterDTO.productCuisineMappings ?: arrayListOf()
                    famousDishListMutableLiveData.value = famousDishArrayList

                    costForTwo.value = restaurantMasterDTO.costForTwo.toString()
                    outletDescription.value = restaurantMasterDTO.outletDesc ?: ""

                    if (restaurantMasterDTO.inMall != false) {
                        inMallArrayList[0].selected = true
                        inMallArrayList[1].selected = false
                    } else {
                        inMallArrayList[0].selected = false
                        inMallArrayList[1].selected = true
                    }
                    inMallListMutableLiveData.value = inMallArrayList

                    if (restaurantMasterDTO.alcohol) {
                        alcoholServedArrayList[0].selected = true
                        alcoholServedArrayList[1].selected = false
                    } else {
                        alcoholServedArrayList[0].selected = false
                        alcoholServedArrayList[1].selected = true
                    }
                    alcoholServedListMutableLiveData.value = alcoholServedArrayList

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

    fun splitFamousForString(string: String) {

        val stringArrayList = string.split(",")

        for (s in stringArrayList) {
            if (!famousForArrayList.contains(s) && s.isNotBlank()) {
                famousForArrayList.add(s)
            }
        }
        famousForListMutableLiveData.value = famousForArrayList
    }

    private fun populateMasters() {

        showProgressBar.value = true

        // Outlet Type
        masterRepository.fetchOutletTypeDataLocal(object :
            IDataSourceCallback<List<OutletTypeMasterDTO>> {

            override fun onDataFound(data: List<OutletTypeMasterDTO>) {
                outletTypeArrayList.clear()
                outletTypeArrayList = data as ArrayList<OutletTypeMasterDTO>
                outletTypeListMutableLiveData.value = outletTypeArrayList
            }

            override fun onError(error: String) {
                getmSnackbar().value = error
            }
        })

        // Seating Type
        masterRepository.fetchSystemMasterValueByNameLocal(SystemValueMasterDao.SEATING,
            object : IDataSourceCallback<List<SystemValueMasterDTO>> {

                override fun onDataFound(data: List<SystemValueMasterDTO>) {
                    seatingTypeArrayList.clear()
                    seatingTypeArrayList = data as ArrayList<SystemValueMasterDTO>
                    seatingTypeListMutableLiveData.value = seatingTypeArrayList
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                }
            })

        // Food Type
        masterRepository.fetchFoodTypeMasterDataLocal(
            object : IDataSourceCallback<List<FoodTypeMasterDTO>> {

                override fun onDataFound(data: List<FoodTypeMasterDTO>) {
                    foodTypeArrayList.clear()
                    foodTypeArrayList = data as ArrayList<FoodTypeMasterDTO>
                    foodTypeListMutableLiveData.value = foodTypeArrayList
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                }
            })

        // Meal type
        masterRepository.fetchMealTypeMasterDataLocal(object :
            IDataSourceCallback<List<MealTypeMasterDTO>> {

            override fun onDataFound(data: List<MealTypeMasterDTO>) {
                mealTypeArrayList.clear()
                mealTypeArrayList = data as ArrayList<MealTypeMasterDTO>
                mealTypeListMutableLiveData.value = mealTypeArrayList
            }

            override fun onError(error: String) {
                getmSnackbar().value = error
            }
        })

        // Cuisine
        masterRepository.fetchCuisineMasterDataLocal(object :
            IDataSourceCallback<List<CuisineMasterDTO>> {

            override fun onDataFound(data: List<CuisineMasterDTO>) {
                cuisineArrayList.clear()
                cuisineArrayList = data as ArrayList<CuisineMasterDTO>
                cuisineListMutableLiveData.value = cuisineArrayList
            }

            override fun onError(error: String) {
                getmSnackbar().value = error
            }
        })

        // Facility Info
        masterRepository.fetchFacilityMastersLocal(object :
            IDataSourceCallback<List<FacilityMasterDTO>> {

            override fun onDataFound(data: List<FacilityMasterDTO>) {
                facilityArrayList.clear()
                facilityArrayList = data as ArrayList<FacilityMasterDTO>
                facilityListMutableLiveData.value = facilityArrayList
                populateData()
            }

            override fun onError(error: String) {
                getmSnackbar().value = error
                populateData()
            }
        })


        inMallArrayList = arrayListOf()
        alcoholServedArrayList = arrayListOf()

        val booleanMaster = BooleanMasterDTO()
        booleanMaster.id = 1
        booleanMaster.name = "Yes"
        booleanMaster.selected = true
        inMallArrayList.add(booleanMaster)
        alcoholServedArrayList.add(booleanMaster)

        val booleanMaster1 = BooleanMasterDTO()
        booleanMaster1.id = 2
        booleanMaster1.name = "No"
        booleanMaster1.selected = false
        inMallArrayList.add(booleanMaster1)
        alcoholServedArrayList.add(booleanMaster1)

        inMallListMutableLiveData.value = inMallArrayList
        alcoholServedListMutableLiveData.value = alcoholServedArrayList

    }

    fun onAddFamousDishes() {

        if (!productName.value.isNullOrBlank() && !dishCategoryValue.value.isNullOrBlank()) {

            val famousDishesDTO = FamousDishesDTO()
            famousDishesDTO.outletId = outLetId
            famousDishesDTO.productName = productName.value ?: ""
            famousDishesDTO.cuisineId = dishCategoryId
            famousDishesDTO.cuisineName = dishCategoryValue.value ?: ""
            famousDishArrayList.add(famousDishesDTO)
            famousDishListMutableLiveData.value = famousDishArrayList

            productName.value = ""
            dishCategoryValue.value = ""
            famousDishesErrorMsg.value = ""


        } else if (productName.value.isNullOrBlank()) {
            famousDishesErrorMsg.value = "Please enter dish name"

        } else if (dishCategoryValue.value.isNullOrBlank()) {
            famousDishesErrorMsg.value = "Please select category"
        }
    }


    fun onCategoryClick() {
        categoryClick.call()
    }

    fun getmOnCategoryClick(): SingleLiveEvent<Void> {
        return categoryClick
    }


    fun onRemoveFamousDishesItem(position: Int) {
        famousDishArrayList.removeAt(position)
        famousDishListMutableLiveData.value = famousDishArrayList
    }

    fun onRemoveFamousForItem(position: Int, adapterType: String) {
        if (adapterType == Constants.FAMOUS_FOR_STRING_ADAPTER) {
            famousForArrayList.removeAt(position)
            famousForListMutableLiveData.value = famousForArrayList
        }
    }

    fun onBasicInfoClick() {
        openTimingsLayoutVisible.value = !openTimingsLayoutVisible.value!!
    }

    fun onCuisineInfoClick() {
        cuisineInfoLayoutVisible.value = !cuisineInfoLayoutVisible.value!!
    }

    fun onClearCuisineSearch() {
        cuisineNameSearch.value = ""
    }

    fun onFacilityInfoClick() {
        facilityInfoLayoutVisible.value = !facilityInfoLayoutVisible.value!!
    }

    fun onFamousDishesClick() {
        famousDishesLayoutVisible.value = !famousDishesLayoutVisible.value!!
    }


    fun searchByText(searchQuery: String?) {

        if (!searchQuery.isNullOrEmpty()) {
            val searchedDataList = ArrayList<CuisineMasterDTO>()
            for (item in cuisineArrayList) {
                if ((item.cuisineName.contains(searchQuery, true))) {
                    searchedDataList.add(item)
                }
            }
            cuisineListMutableLiveData.value = arrayListOf()
            cuisineListMutableLiveData.value = searchedDataList
        }

    }

    fun onSaveClick(isPreLogin: Boolean) {

        validations()


        if (isPreLogin) {

            restaurantMasterDTO.status =
                RestaurantInfoStatusEnum.AWAITING_APPROVAL.name // Setting status for the creating the restaurant/outlet

        } else {

            restaurantMasterDTO.status =
                RestaurantInfoStatusEnum.AWAITING_INFO_APPROVAL.name // Setting status for the updating the restaurant/outlet
        }

        showProgressBar.value = true

        userRepository.saveRestaurantDetails(restaurantMasterDTO,
            object : IDataSourceCallback<String> {

                override fun onDataFound(data: String) {
                    showProgressBar.value = false
                    isPreLoginContinueClickedMutableLiveData.value = true
                    getmSnackbar().value = data
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })
    }

    private fun validations() {

        restaurantMasterDTO.outletId = outLetId

        restaurantMasterDTO.outletDesc = outletDescription.value.toString().trim()

        if (costForTwo.value.toString().trim().replace(".", "").isBlank()
        ) {
            restaurantMasterDTO.costForTwo = BigDecimal(0)
        } else {
            restaurantMasterDTO.costForTwo = costForTwo.value.toString().trim().toBigDecimal()
        }




        restaurantMasterDTO.outletType = arrayListOf()
        for (item in outletTypeArrayList) {
            if (item.selected) {
                restaurantMasterDTO.outletType?.add(item.outletTypeId)
            }
        }

        restaurantMasterDTO.seatingIds = arrayListOf()
        for (item in seatingTypeArrayList) {
            if (item.selected) {
                item.serialId?.let { restaurantMasterDTO.seatingIds?.add(it) }
            }
        }


        restaurantMasterDTO.inMall = inMallArrayList[0].selected

        restaurantMasterDTO.alcohol = alcoholServedArrayList[0].selected

        restaurantMasterDTO.foodTypes = arrayListOf()
        for (item in foodTypeArrayList) {
            if (item.selected) {
                restaurantMasterDTO.foodTypes?.add(item.foodTypeId)
            }
        }

        restaurantMasterDTO.mealTypes = arrayListOf()
        for (item in mealTypeArrayList) {
            if (item.selected) {
                restaurantMasterDTO.mealTypes?.add(item.mealTypeId)
            }
        }



        restaurantMasterDTO.famousFor = ""
        val stringBuffer = StringBuffer("")
        for ((i, item) in famousForArrayList.withIndex()) {
            if (i == (famousForArrayList.size - 1)) {
                stringBuffer.append(item)
            } else {
                stringBuffer.append("$item,")
            }

        }
        restaurantMasterDTO.famousFor = stringBuffer.toString()

        restaurantMasterDTO.cuisineIds = arrayListOf()
        for (item in cuisineArrayList) {
            if (item.selected) {
                restaurantMasterDTO.cuisineIds?.add(item.cuisineId)
            }
        }

        restaurantMasterDTO.facilityTextMappings = arrayListOf()
/*
        for (item in facilityArrayList) {
            if (item.selected) {
                val facilityDTO = FacilityDTO()
                facilityDTO.facilityId = item.facilityId
                facilityDTO.facilityTextMappingId = item.facilityTextMappingId
                facilityDTO.outletId = outLetId
                facilityDTO.supportText = item.facilityValue
                restaurantMasterDTO.facilityTextMappings?.add(facilityDTO)
            }
        }
*/

        for (item in facilityBindingList) {

            if (item.facilityMasterDTO?.selected == true) {
                val facilityDTO = FacilityDTO()
                facilityDTO.facilityId = item.facilityMasterDTO?.facilityId ?: 0L
                facilityDTO.facilityTextMappingId =
                    item.facilityMasterDTO?.facilityTextMappingId ?: 0L
                facilityDTO.outletId = outLetId
                facilityDTO.supportText = item.facilityMasterDTO?.facilityValue ?: ""
                restaurantMasterDTO.facilityTextMappings?.add(facilityDTO)
            }
        }

        restaurantMasterDTO.productCuisineMappings = famousDishArrayList ?: arrayListOf()

    }

    fun onLoginClick() {
        Constants.changeActivity<UserLoginActivity>(activity)
    }

}