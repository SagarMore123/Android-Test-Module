package com.sagar.logutil.checqk.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.net.Uri
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.sagar.logutil.checqk.master_controller.source.MasterRepository
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.network.network_utils.NetworkUtils.Companion.coroutineScope
import com.sagar.logutil.checqk.source.DashboardRepository
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.GenericBaseObservable
import com.sagar.logutil.checqk.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class AddDishDialogViewModel : GenericBaseObservable {

    var activity: Activity
    var application: Application
    var showProgressBar = MutableLiveData<Boolean>()
    private var masterRepository: MasterRepository
    private var dashboardRepository: DashboardRepository
    private var sharedPreferences: SharedPreferences
    var outLetId = 0L
    var catalogueCategoryId = 0L
    var catalogueSectionId = 0L
    var index = 0
    var productId = 0L

    var addPhotoClick = MutableLiveData<Boolean>(false)
    var closeClick = MutableLiveData<Boolean>(false)
    var onSaveClose = MutableLiveData<Boolean>(false)

    var productName = MutableLiveData<String>("")
    var dishDescription = MutableLiveData<String>("")
    var dishImageUrl = MutableLiveData<String>("")
    var dishOriginalPrice = MutableLiveData<String>("")
    var dishDiscountedPrice = MutableLiveData<String>("")
    var minOrder = MutableLiveData<String>("")
    var maxOrder = MutableLiveData<String>("")

    var menuCategoryMutableArrayList = MutableLiveData<ArrayList<CategoryCatalogueDTO>>()
    val menuCategoryRecyclerVisibility = ObservableBoolean(false)
    var selectedMenuCategoryArrayList = ArrayList<CategoryCatalogueDTO>()
    val selectedMenuCategoryList = MutableLiveData<String>("")
    val selectedMenuCategoryIdList = MutableLiveData<ArrayList<Long>>(arrayListOf())

    var menuSectionList = ArrayList<CatalogueSectionDTO>()
    var menuSectionMutableArrayList = MutableLiveData<ArrayList<CatalogueSectionDTO>>()
    val menuSectionRecyclerVisibility = ObservableBoolean(false)
    var selectedMenuSectionArrayList = ArrayList<CatalogueSectionDTO>()
    val selectedMenuSectionList = MutableLiveData<String>("")
    val selectedMenuSectionIdList = MutableLiveData<ArrayList<Long>>(arrayListOf())

    var dishFlagsMutableArrayList = MutableLiveData<ArrayList<ProductFlagDTO>>()
    val selectedDishFlags = ArrayList<Long>(0)

    var cuisineNameSearch = MutableLiveData<String>("")
    var cuisineArrayList = ArrayList<CuisineMasterDTO>()
    var cuisineListMutableLiveData = MutableLiveData<List<CuisineMasterDTO>>()
    val selectedCuisineList = ArrayList<Long>(0)
    var clearCuisineNameSearchVisible = ObservableBoolean(false)

    var fromTime = MutableLiveData<String>("")
    var toTime = MutableLiveData<String>("")
    var errorMsg = MutableLiveData<String>("")

    var onAddTimeSlotClick = MutableLiveData<Boolean>(false)
    var navToNext = MutableLiveData<Boolean>(false)

    var daysArrayList = ArrayList<DayDTO>()
    var daysListMutableLiveData = MutableLiveData<List<DayDTO>>()

    var resetTimingsArrayList = ArrayList<TimingDTO>()
    var timingArrayList = ArrayList<DayDTO>()
    var timingListMutableLiveData = MutableLiveData<List<DayDTO>>()

    var foodImageDTO = ImageDTO()
    var foodImageUrl = MutableLiveData<Uri>()
    var foodImageBase64 : String = ""
    var dishSequenceNumber : Long = 0L

    var updatedList = ArrayList<DayDTO>()

    var isFirstTimeSections = true

    constructor(
        activity: Activity,
        application: Application,
        owner: LifecycleOwner?,
        view: View?,
        masterRepository: MasterRepository,
        dashboardRepository: DashboardRepository
    ) : super(application, owner, view) {
        this.activity = activity
        this.application = application
        this.masterRepository = masterRepository
        this.dashboardRepository = dashboardRepository
        sharedPreferences = Constants.getSharedPreferences(application)

        outLetId = Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L

        getAllMenuCategories()
        getAllDishFlags()
        populateMasters()
        onResetClick()
    }

    fun getAllMenuCategories(){
        dashboardRepository.getAllMenuCategories(outLetId, object : IDataSourceCallback<GetCatalogueCategoriesResponseDTO>{
            override fun onDataFound(data: GetCatalogueCategoriesResponseDTO) {
                menuCategoryMutableArrayList.value = data.activeCatalogueCategoryDTOs
//                catalogueCategoryId = data.get(0).catalogueCategoryId!!

                getAllMenuSections()
                if(productId != 0L)
                    getDishDetails()
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

//        showProgressBar.value = true
        dashboardRepository.getAllMenuSections(catalogueCategoryId, outLetId, object : IDataSourceCallback<GetMenuSectionsResponseDTO>{
            override fun onDataFound(data: GetMenuSectionsResponseDTO) {
               /* if (isFirstTimeSections){
                    isFirstTimeSections = false
                }*/
                for(menuSection in data.activeCatalogueSectionDTOs){
                    var exist = false
                    for(existingMenuSection in menuSectionList){
                        if(menuSection.catalogueSectionId == existingMenuSection.catalogueSectionId){
                            exist = true
                            break
                        }
                    }
                    if (!exist)
                        menuSectionList.add(menuSection)
                }
//                menuSectionList.addAll(data.activeMenuSectionDTOs)
                menuSectionMutableArrayList.value = menuSectionList
                showProgressBar.value = false
                if (isFirstTimeSections){
                    isFirstTimeSections = false
                }

            }

            override fun onDataNotFound() {
                menuSectionMutableArrayList.value = arrayListOf()
                showProgressBar.value = false
            }

            override fun onError(error: String) {
                menuSectionMutableArrayList.value = arrayListOf()
                getmSnackbar().value = error
                showProgressBar.value = false
            }
        })
    }

    private fun getAllDishFlags(){
        dashboardRepository.getAllDishFlags(object : IDataSourceCallback<ArrayList<ProductFlagDTO>>{
            override fun onDataFound(data: ArrayList<ProductFlagDTO>) {
                dishFlagsMutableArrayList.value = data
            }

            override fun onDataNotFound() {
                dishFlagsMutableArrayList.value = arrayListOf()
            }

            override fun onError(error: String) {

            }
        })
    }

    private fun populateMasters() {
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

    private fun initiateDays() {

        daysArrayList.clear()
        daysListMutableLiveData.value = arrayListOf()
        for (item in DaysEnum.values()) {

            val dayDTO = DayDTO()
            dayDTO.dayId = item.id.toLong()
            dayDTO.dayName = item.value
            dayDTO.isMarkAsClosedVisibility = false
            daysArrayList.add(dayDTO)
            timingArrayList.add(dayDTO)
        }

        daysListMutableLiveData.value = daysArrayList
    }

    private fun initiateTimings() {

        timingArrayList.clear()
        for (item in DaysEnum.values()) {

            val dayDTO = DayDTO()
            dayDTO.dayId = item.id.toLong()
            dayDTO.dayName = item.value
            dayDTO.isMarkAsClosedVisibility = false
            timingArrayList.add(dayDTO)
        }

    }


    private fun resetDays() {

        fromTime.value = ""
        toTime.value = ""
        onAddTimeSlotClick.value = false

        for (item in daysArrayList) {
            item.dayIsCheckedOrClosed = false
        }
        daysListMutableLiveData.value = daysArrayList
    }

    private fun populateTimings() {

       /* showProgressBar.value = true
        userRepository.fetchTimings(outLetId, object : IDataSourceCallback<OutletTimingDTO> {

            override fun onDataFound(data: OutletTimingDTO) {
                showProgressBar.value = false
                resetTimingsArrayList = data.timings
                onResetClick()

            }

            override fun onDataNotFound() {
                showProgressBar.value = false
            }

            override fun onError(error: String) {
                showProgressBar.value = false
                getmSnackbar().value = error
            }
        })*/
    }

    fun onResetClick() {
        initiateDays()
        initiateTimings()
        for (times in resetTimingsArrayList) {

            for (item in timingArrayList) {

                if (item.dayId == times.weekDay) {
                    item.timings.add(times)
                }
            }
        }


        updatedList.clear()
        for (item in timingArrayList) {

            if (item.timings.isNotEmpty()) {
                item.dayIsCheckedOrClosed = false
                updatedList.add(item)
            }
        }

        timingListMutableLiveData.value = updatedList

    }

    fun onAddTimeSlotClick() {
        onAddTimeSlotClick.value = true
    }

    fun onAddTime() {

        if (!fromTime.value.isNullOrBlank() && !toTime.value.isNullOrBlank()) {

            var isDayPresent = false
            for (item in daysArrayList) {
                if (item.dayIsCheckedOrClosed) {
                    isDayPresent = true
                    for (item2 in timingArrayList) {
                        if (item.dayId == item2.dayId && !item2.dayIsCheckedOrClosed) {
                            val timingDTO = TimingDTO()
                            timingDTO.opensAt = fromTime.value ?: ""
                            timingDTO.closesAt = toTime.value ?: ""
                            timingDTO.outletId = outLetId
                            timingDTO.weekDay = item2.dayId
                            item2.timings.add(timingDTO)
                            break
                        }
                    }
                }
            }


            updatedList.clear()
            for (item in timingArrayList) {

                if (item.timings.isNotEmpty()) {
                    item.dayIsCheckedOrClosed = item.dayIsCheckedOrClosed
                    updatedList.add(item)
                }
            }

            timingListMutableLiveData.value = updatedList
            if (!isDayPresent) {
                errorMsg.value = "Please select day, where the time should be added"
            } else {
                resetDays()
                errorMsg.value = ""
            }


        } else if (fromTime.value.isNullOrBlank()) {
            errorMsg.value = "Please enter From time"

        } else if (toTime.value.isNullOrBlank()) {
            errorMsg.value = "Please enter To time"

        }
    }

    fun onDayItemClick(position: Int, dayDTO: DayDTO) {
        daysArrayList[position].dayIsCheckedOrClosed =
            !daysArrayList[position].dayIsCheckedOrClosed
        daysListMutableLiveData.value = daysArrayList
    }

    // Timings section
    fun onMarkAsClosedItemClick(position: Int, dayDTO: DayDTO) {
        updatedList[position] = dayDTO
        timingListMutableLiveData.value = updatedList

        for ((i, item) in timingArrayList.withIndex()) {
            if (item.dayId == dayDTO.dayId) {
                timingArrayList[i] = dayDTO
                daysArrayList[i].dayEnable = !daysArrayList[i].dayEnable
                if (!daysArrayList[i].dayEnable && daysArrayList[i].dayIsCheckedOrClosed) {
                    daysArrayList[i].dayIsCheckedOrClosed = false
                }
            }
        }

        daysListMutableLiveData.value = daysArrayList

    }

    fun onTimingRemoveItemClick(
        position: Int,
        mainContainerPosition: Int,
        timingDTO: TimingDTO
    ) {
        updatedList[mainContainerPosition].timings.remove(updatedList[mainContainerPosition].timings[position])
        if (updatedList[mainContainerPosition].timings.isEmpty()) {
            updatedList.removeAt(mainContainerPosition)
        }

        timingListMutableLiveData.value = updatedList
    }

    fun onMenuCategoryClick(){
        menuCategoryRecyclerVisibility.set(!menuCategoryRecyclerVisibility.get())
        menuSectionRecyclerVisibility.set(false)
    }

    fun onMenuSectionClick(){
        menuCategoryRecyclerVisibility.set(false)
        menuSectionRecyclerVisibility.set(!menuSectionRecyclerVisibility.get())
    }

    fun onCloseClick(){
        closeClick.value = true
    }
    fun onFoodPhotoClick(){
        addPhotoClick.value = true
    }

    fun onClearCuisineSearch() {
        cuisineNameSearch.value = ""
    }

    fun getDishDetails(){
        showProgressBar.value = true
        dashboardRepository.getDishDetails(productId, object :IDataSourceCallback<ProductDetailsDTO>{
            override fun onDataFound(data: ProductDetailsDTO) {
                showDataOnScreen(data)
//                getDishTimingDetails()
            }

            override fun onError(error: String) {
                getmSnackbar().value = error
                showProgressBar.value = false
            }
        })
    }

    fun showCategorySelectedData() {
        selectedMenuCategoryList.value = ""
        for (menuCategory in selectedMenuCategoryArrayList) {
            if (menuCategory.isSelected != null && menuCategory.isSelected) {
                if (selectedMenuCategoryList.value == "")
                    selectedMenuCategoryList.value = menuCategory.catalogueCategoryName
                else if (!selectedMenuCategoryList.value!!.contains(menuCategory.catalogueCategoryName!!))
                    selectedMenuCategoryList.value = selectedMenuCategoryList.value + ", " +
                                menuCategory.catalogueCategoryName
                /*else
                    viewModel.selectedMenuCategoryList.value = viewModel.selectedMenuCategoryList.value + ", " +
                            menuCategory.menuCategoryName*/
            }
        }
    }

    fun showSectionSelectedData(){
        selectedMenuSectionList.value = ""
        for (menuSection in selectedMenuSectionArrayList){
            if (menuSection.isSelected != null && menuSection.isSelected){
                if(selectedMenuSectionList.value == "")
                    selectedMenuSectionList.value = menuSection.catalogueSectionName
                else if(!selectedMenuSectionList.value!!.contains(menuSection.catalogueSectionName!!))
                    selectedMenuSectionList.value = selectedMenuSectionList.value + ", " +
                            menuSection.catalogueSectionName
            }
        }
    }

    fun showDataOnScreen(productDetailsDTO: ProductDetailsDTO){
        productId = productDetailsDTO.productId!!
        productName.value = productDetailsDTO.productName
        dishDescription.value = productDetailsDTO.productDesc
        dishImageUrl.value = productDetailsDTO.productImage!!.path
        dishOriginalPrice.value = productDetailsDTO.productOriPrice.toString()
        dishDiscountedPrice.value = productDetailsDTO.productDiscountPrice.toString()
        minOrder.value = productDetailsDTO.minOrderQty.toString()
        maxOrder.value = productDetailsDTO.maxOrderQty.toString()
        maxOrder.value = productDetailsDTO.maxOrderQty.toString()

        dishSequenceNumber = productDetailsDTO.productSequenceNo?:0

        var menuCategoryList = menuCategoryMutableArrayList.value
        for(menuCategory in menuCategoryList?: arrayListOf()){
            for(prevSelectedMenuCategory in productDetailsDTO.catalogueCategoryDTO?: arrayListOf()){
                if(prevSelectedMenuCategory.catalogueCategoryId == menuCategory.catalogueCategoryId){
                    menuCategory.isSelected = true
                    selectedMenuCategoryArrayList.add(menuCategory)
                }
            }
        }
        menuCategoryMutableArrayList.value = menuCategoryList
        showCategorySelectedData()

        var menuSectionList = menuSectionMutableArrayList.value
        for(menuSection in menuSectionList!!){
            for(prevSelectedMenuSection in productDetailsDTO.catalogueSectionDTO!!){
                if(prevSelectedMenuSection.catalogueSectionId == menuSection.catalogueSectionId){
                    menuSection.isSelected = true
                    selectedMenuSectionArrayList.add(menuSection)
//                    catalogueCategoryId = menuSection.catalogueCategoryId!!
//                    getAllMenuSections()
                }
            }
        }
        menuSectionMutableArrayList.value = menuSectionList
        showSectionSelectedData()


        /*for(menuCategory in dishDetailsDTO.catalogueCategoryDTO!!){
            if (selectedMenuCategoryList.value.equals("")){
                selectedMenuCategoryList.value = menuCategory.menuCategoryName
                catalogueCategoryId = menuCategory.catalogueCategoryId!!
                getAllMenuSections()
            }else{
                selectedMenuCategoryList.value = ", "+menuCategory.menuCategoryName
            }
            selectedMenuCategoryIdList.value!!.add(menuCategory.catalogueCategoryId!!)
            selectedMenuCategoryArrayList.add(menuCategory)
        }
        showCategorySelectedData()*/

       /* selectedMenuCategoryList.value = ""
        for(menuSection in dishDetailsDTO.menuSectionDTO!!){
            if (selectedMenuSectionList.value.equals("")){
                selectedMenuSectionList.value = menuSection.menuSectionName
            }else{
                selectedMenuSectionList.value = ", "+menuSection.menuSectionName
            }
            selectedMenuSectionIdList.value!!.add(menuSection.catalogueCategoryId!!)
            selectedMenuSectionArrayList.add(menuSection)
        }*/
        val dishFlagList = dishFlagsMutableArrayList.value
        for (dishFlag in dishFlagList?: arrayListOf()){
            for(selectedFlag in productDetailsDTO.productFlags?: arrayListOf()){
                if (selectedFlag == dishFlag.productFlagId){
                    dishFlag.isSelected = true
                    break
                }
            }
        }
        dishFlagsMutableArrayList.value = dishFlagList

        foodImageDTO = productDetailsDTO.productImage!!

        for(cuisine in cuisineArrayList){
            for(selectedCuisine in productDetailsDTO.cuisineIds!!){
                if (selectedCuisine.equals(cuisine.cuisineId)){
                    cuisine.selected = true
                    break
                }
            }
        }
        cuisineListMutableLiveData.value = cuisineArrayList

        resetTimingsArrayList.clear()
        for(time in productDetailsDTO.productTimingDTO?.timings?: arrayListOf()){
            val timingDTO = TimingDTO()
            timingDTO.timingId = time.productTimingId
            timingDTO.outletId = time.outletId
            timingDTO.weekDay = time.weekDay
            timingDTO.opensAt = time.opensAt
            timingDTO.closesAt = time.closesAt

            resetTimingsArrayList.add(
                timingDTO
            )
        }
//                resetTimingsArrayList = data.timings
        onResetClick()

//        resetTimingsArrayList = data.timings
//        onResetClick()

    }

    fun getDishTimingDetails(){
        dashboardRepository.getDishTiming(productId, object : IDataSourceCallback<ArrayList<DishTimingDTO>>{
            override fun onDataFound(data: ArrayList<DishTimingDTO>) {
                resetTimingsArrayList.clear()
                for(time in data){
                    var timingDTO = TimingDTO()
                    timingDTO.timingId = time.productTimingId
                    timingDTO.outletId = time.outletId
                    timingDTO.weekDay = time.weekDay
                    timingDTO.opensAt = time.opensAt
                    timingDTO.closesAt = time.closesAt

                    resetTimingsArrayList.add(
                        timingDTO
                    )
                }
//                resetTimingsArrayList = data.timings
                onResetClick()
                showProgressBar.value = false
            }

            override fun onDataNotFound() {
                getmSnackbar().value = "No times added"
                showProgressBar.value = false
            }

            override fun onError(error: String) {
                getmSnackbar().value = error
                showProgressBar.value = false
            }
        })
    }

    fun setImage(uri  :Uri) {
        foodImageUrl.value = uri

        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val imageBase64 = Utils.getBitmapFromUri(application, foodImageUrl.value)
                    ?.let { Utils.convertBitmapToBase64(it) }
                imageBase64?.let { foodImageBase64 = it }
                foodImageDTO.base64 = imageBase64

                val file = File(uri.path)
                foodImageDTO.documentName = file.name + ".jpeg"
//                foodImageDTO.documentName = "image_${System.currentTimeMillis()}"

            }
            /*activity.runOnUiThread {

                if (foodImageDTO.isUri == true) {
                    binding.imageView.setImageURI(imageDTO.imageUri)

                } else {
                    Glide.with(context).load("$SERVER_IMG_URL${imageDTO.path}")
                        .into(binding.imageView)
                }

            }*/

        }
    }

    fun validation(){
        selectedMenuCategoryIdList.value = arrayListOf()
        var categoryList = ArrayList<Long>()
        for (menuCategory in selectedMenuCategoryArrayList){
            if (menuCategory?.isSelected != null && menuCategory.isSelected!!){
                categoryList.add(menuCategory.catalogueCategoryId!!)
            }
        }
        selectedMenuCategoryIdList.value = categoryList

        selectedMenuSectionIdList.value = arrayListOf()
        var sectionList = ArrayList<Long>()
        for (menuSection in selectedMenuSectionArrayList){
            if (menuSection?.isSelected != null && menuSection.isSelected!!){
                sectionList.add(menuSection.catalogueSectionId!!)
            }
        }
        selectedMenuSectionIdList.value = sectionList


        selectedDishFlags.clear()
        for (dish in dishFlagsMutableArrayList.value?: arrayListOf()){
            if (dish.isSelected != null && dish.isSelected == true){
                selectedDishFlags.add(dish.productFlagId?:0)
            }
        }

        selectedCuisineList.clear()
        for (cuisine in cuisineArrayList?: arrayListOf()){
            if (cuisine.selected){
                selectedCuisineList.add(cuisine.cuisineId)
            }
        }

        when {
            selectedMenuCategoryIdList.value.isNullOrEmpty() -> {
                getmSnackbar().value = "Select menu categories"
            }
            selectedMenuSectionIdList.value.isNullOrEmpty() -> {
                getmSnackbar().value = "Select menu sections"
            }
            foodImageDTO.base64 == "" && foodImageDTO.path == ""  -> {
                getmSnackbar().value = "Upload food image"
            }
            productName.value.isNullOrEmpty() -> {
                getmSnackbar().value = "Enter dish name"
            }
            dishDescription.value.isNullOrEmpty() -> {
                getmSnackbar().value = "Enter dish summery"
            }
            dishOriginalPrice.value.isNullOrEmpty() -> {
                getmSnackbar().value = "Enter dish price"
            }
            (!dishDiscountedPrice.value.isNullOrEmpty() &&
                    (dishOriginalPrice.value!!.toInt() <= dishDiscountedPrice.value!!.toInt()) ) -> {
                getmSnackbar().value = "Discounted price should be less than price"
            }
            minOrder.value.isNullOrEmpty() -> {
                getmSnackbar().value = "Enter minimum order"
            }
            maxOrder.value.isNullOrEmpty() -> {
                getmSnackbar().value = "Enter max order"
            }
            /*selectedDishFlags.isNullOrEmpty() -> {
                getmSnackbar().value = "Select dish flag"
            }
            selectedCuisineList.isNullOrEmpty() -> {
                getmSnackbar().value = "Select cuisines"
            }
            timingListMutableLiveData.value.isNullOrEmpty() -> {
                getmSnackbar().value = "Enter dish availability time"
            }*/
            else -> {
                var discountedPrice = 0
                if (!dishDiscountedPrice.value!!.equals("")){
                    discountedPrice = dishDiscountedPrice.value!!.toInt()
                }

                var timingList = ArrayList<DishTimingDTO>()
                for( day in timingListMutableLiveData.value!!){
                    for(time in day.timings){
                        timingList.add(
                            DishTimingDTO(
                                productId,
                                time.timingId,
                                outLetId,
                                time.weekDay,
                                time.opensAt,
                                time.closesAt
                            )
                        )
                    }
                }
                val dishTimingRequest = DishTimingRequestDTO(
                    timingList,
                    productId
                )

                var dishDetailsDTO = ProductDetailsDTO(
                    productId,
                    productName.value,

                     dishDescription.value,
                    dishOriginalPrice.value!!.toInt(),
                    discountedPrice,
                    foodImageDTO,
                    true,
                    dishSequenceNumber,
                    maxOrder.value!!.toLong(),
                    minOrder.value!!.toLong(),
                    outLetId,
                    "",
                    selectedCuisineList,
                    selectedDishFlags,
                    null,
                    null,
                    selectedMenuCategoryIdList.value!!,
                    null,
                    selectedMenuSectionIdList.value!!,
                    true,
                    dishTimingRequest
                )
                saveDishDetailsService(dishDetailsDTO)
            }
        }
    }

    fun saveDishDetailsService(productDetailsDTO : ProductDetailsDTO){

        showProgressBar.value = true
        dashboardRepository.saveDish(productDetailsDTO, object : IDataSourceCallback<Long>{
            override fun onDataFound(data: Long) {
                productId = data
//                saveTimingDetails()
                getmSnackbar().value = "Dish Save Successfully"
                showProgressBar.value = false
                onSaveClose.value = true
            }

            override fun onError(error: String) {
                getmSnackbar().value = error
                showProgressBar.value = false
            }
        })
    }

    fun saveTimingDetails(){
        var timingList = ArrayList<DishTimingDTO>()
        for( day in timingListMutableLiveData.value!!){
            for(time in day.timings){
                timingList.add(
                    DishTimingDTO(
                        productId,
                        time.timingId,
                        outLetId,
                        time.weekDay,
                        time.opensAt,
                        time.closesAt
                    )
                )
            }
        }
        val dishTimingRequest = DishTimingRequestDTO(
            timingList,
            productId
        )
        if (!timingList.isNullOrEmpty()){
            dashboardRepository.saveDishTiming(dishTimingRequest, object : IDataSourceCallback<String>{
                override fun onDataFound(data: String) {
                    getmSnackbar().value = "Dish Save Successfully"
                    showProgressBar.value = false
                    onSaveClose.value = true
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                    showProgressBar.value = false
                }
            })
        }else{
            onSaveClose.value = true
            showProgressBar.value = false
        }

    }

}