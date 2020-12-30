package com.sagar.logutil.checqk.network

import android.content.Context
import com.sagar.logutil.R
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.model.discount.OutletDiscountDetailsDTO
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.network.network_utils.NetworkResponseCallback
import com.sagar.logutil.checqk.network.network_utils.NetworkUtils.Companion.HTTP_RETROFIT_FAILURE
import com.sagar.logutil.checqk.network.network_utils.NetworkUtils.Companion.HTTP_SUCCESS
import com.sagar.logutil.checqk.network.network_utils.NetworkUtils.Companion.getStringResponseFromRaw
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.CustomGsonBuilder
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkController {

    companion object {

        var instance: NetworkController? = null
        lateinit var mContext: Context

        const val SERVER_ERROR = "Something went wrong on the server"

        fun getInstance(context: Context): NetworkController {
            mContext = context

            if (instance == null) {
                instance = NetworkController()
            }
            accessToken = Constants.getAccessToken(mContext) ?: ""
            emailId = Constants.getEmailId(mContext) ?: ""
            return instance as NetworkController
        }

        const val contentType = "application/json"
        var emailId = ""
        var accessToken = ""
        var refreshToken = ""
    }

    private class RetrofitServiceTask(var networkResponseCallback: NetworkResponseCallback) :
        Callback<ResponseBody> {

        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.code() == HTTP_SUCCESS) {
                getStringResponseFromRaw(response)?.let { data ->
                    networkResponseCallback.onSuccess(data)
                }
            } else {
                var errorMsg = SERVER_ERROR
                val jsonError = getStringResponseFromRaw(response.errorBody()!!)
                try {

                    val jsonObject = JSONObject(jsonError.toString())
                    if (jsonObject.has("error")) {
                        if (jsonObject.getString("error").equals("invalid_token", ignoreCase = true)) {
//                            val intent = Intent(mContext, UserLoginActivity::class.java)
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                            intent.putExtra(Constants.ACCESS_TOKEN_INVALID, true)
                            Constants.clearSharedPrefs(mContext)
//                            mContext.startActivity(intent)


                        }

                        if (!jsonObject.getJSONObject("error").getString("message")
                                .isNullOrBlank()
                        ) {
                            networkResponseCallback.onError(
                                response.code(),
                                jsonObject.getJSONObject("error").getString("message")
                            )
                        } else {
                            networkResponseCallback.onError(response.code(), SERVER_ERROR)
                        }
                    }
                } catch (e: Exception) {
                    networkResponseCallback.onError(response.code(), SERVER_ERROR)
                }

            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            try {
                if (t.message != null) {
                    if (t.message?.contains("Failed") == true || t.message?.contains("failed to connect") == true) {
                        networkResponseCallback.onError(
                            HTTP_RETROFIT_FAILURE,
                            mContext.resources.getString(R.string.network_failure_string)
                        )
                    } else {
                        networkResponseCallback.onError(500, SERVER_ERROR)
                    }
                } else {
                    networkResponseCallback.onError(500, SERVER_ERROR)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

    }


    // returns CommonResponseDTO
    fun commonResponseDTO(data: String, callback: IDataSourceCallback<CommonResponseDTO>) {

        try {

            if (data.isNotBlank()) {

                val jsonObject = JSONObject(data)
                val gson = CustomGsonBuilder().getInstance().create()
                val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                    jsonObject.toString(),
                    CommonResponseDTO::class.java
                )

                when {
                    commonResponseDTO.success != null -> {
                        callback.onDataFound(commonResponseDTO)
                    }
                    commonResponseDTO.error != null -> {
                        callback.onError(commonResponseDTO.error.message)
                    }
                    else -> {
                        callback.onError(SERVER_ERROR)
                    }
                }
            }

        } catch (e: Exception) {

            callback.onError(SERVER_ERROR)
        }

    }

    // returns Message String
    fun commonResponseString(data: String, callback: IDataSourceCallback<String>) {

        try {

            if (data.isNotBlank()) {

                val jsonObject = JSONObject(data)
                val gson = CustomGsonBuilder().getInstance().create()
                val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                    jsonObject.toString(),
                    CommonResponseDTO::class.java
                )

                when {
                    commonResponseDTO.success != null -> {
                        callback.onDataFound(commonResponseDTO.success.message ?: "")
                    }
                    commonResponseDTO.error != null -> {
                        callback.onError(commonResponseDTO.error.message)
                    }
                    else -> {
                        callback.onError(SERVER_ERROR)
                    }
                }
            }

        } catch (e: Exception) {

            callback.onError(SERVER_ERROR)
        }

    }


    fun isFirstTimeLoginWithLoginId(loginDTO: LoginDTO, callback: NetworkResponseCallback) {
        val response = UserApi.retrofitService.isFirstTimeLoginWithLoginId(loginDTO)
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun loginMasters(loginDTO: LoginDTO, callback: NetworkResponseCallback) {
        loginDTO.username = "visitor@syspiretechnologies.com"
        loginDTO.password = Constants.passwordEncrypt("P@ssw0rd")
        val response = UserApi.retrofitService.loginMasters(loginDTO)
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun login(loginDTO: LoginDTO, callback: NetworkResponseCallback) {
        val response = UserApi.retrofitService.verifyOtp(loginDTO)
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun verifyOtp(loginDTO: LoginDTO, callback: NetworkResponseCallback) {
        val response = UserApi.retrofitService.verifyOtp(loginDTO)
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun resetPassword(resetPassword: ResetPassword, callback: NetworkResponseCallback) {
        val response = UserApi.retrofitService.resetPassword(
//            accessToken,
            resetPassword
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


    fun fetchTimings(outletId: Long, callback: NetworkResponseCallback) {
        val response = UserApi.retrofitService.fetchTimings(
//            accessToken,
            outletId
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun saveTimings(
        timings: OutletTimingDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitService.saveTimings(
//            accessToken,
            contentType,
            timings
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun deleteOutletClosedDate(
        outletDateId: Long,
        status: Boolean,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.retrofitService.deleteOutletClosedDate(
//                accessToken,
                outletDateId,
                status
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun fetchOutletClosedDates(outletId: Long, callback: NetworkResponseCallback) {
        val response = UserApi.retrofitService.fetchOutletClosedDates(
//            accessToken,
            outletId
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun saveOutletClosedDates(
        closedDate: ClosedDatesDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitService.saveOutletClosedDates(
//            accessToken,
            contentType,
            closedDate
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun fetchSafetyMeasures(outletId: Long, callback: NetworkResponseCallback) {
        val response = UserApi.retrofitService.fetchSafetyMeasures(
//            accessToken,
            outletId
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun saveSafetyMeasures(
        outletSecurityMeasuresDTO: OutletSecurityMeasuresDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitService.saveSafetyMeasures(
//            accessToken,
            contentType,
            outletSecurityMeasuresDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun deleteOutletSecurityMeasureById(
        outletSecurityMeasuresId: Long,
        status: Boolean,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.retrofitService.deleteOutletSecurityMeasureById(
//                accessToken,
                outletSecurityMeasuresId,
                status
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun fetchRestaurantDetails(outletId: Long, callback: NetworkResponseCallback) {
        val response = UserApi.retrofitService.fetchRestaurantDetails(
//            accessToken,
            outletId
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun saveRestaurantDetails(
        restaurantMasterDTO: RestaurantMasterDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitService.saveRestaurantDetails(
//            accessToken,
            contentType,
            restaurantMasterDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun saveRestaurantDetailsByVisitor(
        restaurantMasterDTO: RestaurantMasterDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitService.saveRestaurantDetailsByVisitor(
//            accessToken,
            contentType,
            restaurantMasterDTO.userDetailsDto != null,
            restaurantMasterDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun fetchCommunicationInfo(outletId: Long, callback: NetworkResponseCallback) {
        val response = UserApi.retrofitService.fetchCommunicationInfo(
//            accessToken,
            outletId
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun saveCommunicationInfo(
        communicationInfoDTO: CommunicationInfoDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitService.saveCommunicationInfo(
//            accessToken,
            contentType,
            communicationInfoDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


    //  Masters

    /*
        // SVM
        fun fetchSVM(callback: NetworkResponseCallback) {
            val response = UserApi.systemValueMastersRetrofitService.fetchSVM(accessToken)
            val responseCall: Call<ResponseBody> = response
            responseCall.enqueue(RetrofitServiceTask(callback))
        }
    */
    // SVM
    fun fetchSVM(callback: NetworkResponseCallback) {
        val response = UserApi.mastersRetrofitService.fetchSVM(
//            accessToken
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // Outlet Types
    fun fetchOutLetTypes(commonListingDTO: CommonListingDTO, callback: NetworkResponseCallback) {
        val response =
            UserApi.mastersRetrofitService.fetchOutLetTypes(
//                accessToken,
                commonListingDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // Meal Types
    fun fetchMealTypes(commonListingDTO: CommonListingDTO, callback: NetworkResponseCallback) {
        val response = UserApi.mastersRetrofitService.fetchMealTypes(
//            accessToken,
            commonListingDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // Food Types
    fun fetchFoodTypes(commonListingDTO: CommonListingDTO, callback: NetworkResponseCallback) {
        val response = UserApi.mastersRetrofitService.fetchFoodTypes(
//            accessToken,
            commonListingDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // Facilities
    fun fetchFacilities(commonListingDTO: CommonListingDTO, callback: NetworkResponseCallback) {
        val response = UserApi.mastersRetrofitService.fetchFacilities(
//            accessToken,
            commonListingDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // Cuisines
    fun fetchCuisines(commonListingDTO: CommonListingDTO, callback: NetworkResponseCallback) {
        val response = UserApi.mastersRetrofitService.fetchCuisines(
//            accessToken,
            commonListingDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // Country
    fun fetchCountries(commonListingDTO: CommonListingDTO, callback: NetworkResponseCallback) {
        val response = UserApi.mastersRetrofitService.fetchCountries(
//            accessToken,
            commonListingDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // States
    fun fetchStates(commonListingDTO: CommonListingDTO, callback: NetworkResponseCallback) {
        val response = UserApi.mastersRetrofitService.fetchStates(
//            accessToken,
            commonListingDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // Cities
    fun fetchCities(commonListingDTO: CommonListingDTO, callback: NetworkResponseCallback) {
        val response = UserApi.mastersRetrofitService.fetchCites(
//            accessToken,
            commonListingDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // Areas
    fun fetchAreas(commonListingDTO: CommonListingDTO, callback: NetworkResponseCallback) {
        val response = UserApi.mastersRetrofitService.fetchAreas(
//            accessToken,
            commonListingDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // Social Media
    fun fetchSocialMedia(commonListingDTO: CommonListingDTO, callback: NetworkResponseCallback) {
        val response =
            UserApi.mastersRetrofitService.fetchSocialMedia(
//                accessToken,
                commonListingDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // Discount Category
    fun fetchDiscountCategories(
        commonListingDTO: CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.mastersRetrofitService.fetchDiscountCategories(
//                accessToken,
                commonListingDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    //  Discount Membership Plan // Basic, Corporate Plan
    fun fetchDiscountMembershipPlan(
        commonListingDTO: CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.mastersRetrofitService.fetchDiscountMembershipPlan(
//                accessToken,
                commonListingDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // Cravx Cards Discount Membership Holder
    fun fetchCravxCardsDiscountMembershipHolderMasters(
        commonListingDTO: CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.mastersRetrofitService.fetchCravxCardsDiscountMembershipHolderMasters(
//                accessToken,
                commonListingDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // HW Discount Membership Holder
    fun fetchHWDiscountMembershipHolderMasters(
        commonListingDTO: CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.mastersRetrofitService.fetchHWDiscountMembershipHolderMasters(
//                accessToken,
                commonListingDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // In-House Discount Membership Holder
    fun fetchInHouseDiscountMembershipHolderMasters(
        commonListingDTO: CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.mastersRetrofitService.fetchInHouseDiscountMembershipHolderMasters(
//                accessToken,
                commonListingDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


    fun saveRestaurantProfileImage(
        restaurantProfileImageDTO: RestaurantProfileImageDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitService.saveRestaurantProfileImage(
//            accessToken,
            restaurantProfileImageDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getRestaurantProfileImage(
        outletId: Long,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitService.getRestaurantProfileImage(
//            accessToken,
            outletId
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getGalleryImageCategory(
        commonListingDTO: CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitService.getGalleryImageCategory(
//            accessToken,
            commonListingDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun saveGalleryImagesByCategory(
        galleryImageCategory: GalleryImageCategory,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitService.saveGalleryImagesByCategory(
//            accessToken,
            galleryImageCategory
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getAllGalleryImages(
        outletId: Long,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitService.getAllGalleryImages(
//            accessToken,
            outletId
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun removeGalleryImageByImageId(
        imageId: Long,
        id: Long,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitService.removeGalleryImageByImageId(
//            accessToken,
            imageId,
            id
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun discardGalleryImageByCategoryId(
        outletId: Long,
        galleryImageCategoryId: Long,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitService.discardGalleryImageByCategoryId(
//            accessToken,
            outletId,
            galleryImageCategoryId
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


    fun getMenuImageCategory(
        commonListingDTO: CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitService.getMenuImageCategory(
//            accessToken,
            commonListingDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun saveMenuImagesByCategory(
        catalogueImageCategory: CatalogueImageCategory,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitService.saveMenuImagesByCategory(
//            accessToken,
            catalogueImageCategory
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getAllMenuImages(
        outletId: Long,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitService.getAllMenuImages(
//            accessToken,
            outletId
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun removeMenuImageByImageId(
        imageId: Long,
        id: Long,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitService.removeMenuImageByImageId(
//            accessToken,
            imageId,
            id
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun discardMenuImageByCategoryId(
        outletId: Long,
        catalogueImageCategoryId: Long,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitService.discardMenuImageByCategoryId(
//            accessToken,
            outletId,
            catalogueImageCategoryId
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getAllMenuCategories(
        outletId: Long,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.getAllMenuCategories(
//            accessToken,
            outletId
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun saveMenuCategory(
        menuCategory: CategoryCatalogueDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.saveMenuCategory(
//            accessToken,
            menuCategory
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun saveMenuCategoriesSequence(
        menuCategoryList: ArrayList<CategoryCatalogueDTO>,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.saveMenuCategoriesSequence(
//            accessToken,
            menuCategoryList
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun changeMenuCategoryStatus(
        catalogueCategoryId: Long,
        status: Boolean,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.changeMenuCategoryStatus(
//            accessToken,
            catalogueCategoryId,
            status
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getAllMenuSections(
        catalogueCategoryId: Long,
        outletId: Long,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.getAllMenuSections(
//            accessToken,
            catalogueCategoryId,
            outletId,
            SearchSortDTO(
                addtionalSearch = null,
                search = null,
                defaultSort = SortField("catalogueSectionId", "asc"),
                sort = null,
                length = 0,
                start = 0,
                status = true,
                statusCode = null
            )
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun saveMenuSection(
        catalogueSection: CatalogueSectionDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.saveMenuSection(
//            accessToken,
            catalogueSection
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun saveMenuSectionsSequence(
        catalogueSectionList: ArrayList<CatalogueSectionDTO>,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.saveMenuSectionsSequence(
//            accessToken,
            catalogueSectionList
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun changeMenuSectionStatus(
        catalogueSectionId: Long,
        status: Boolean,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.changeMenuSectionStatus(
//            accessToken,
            catalogueSectionId,
            status
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // Discount Section
    fun fetchOutletDiscountDetailsList(outletId: Long, callback: NetworkResponseCallback) {
        val response = UserApi.retrofitService.fetchOutletDiscountDetailsList(
//            accessToken,
            outletId
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun fetchOutletMembershipPlanMapping(outletId: Long, callback: NetworkResponseCallback) {
        val response =
            UserApi.retrofitService.fetchOutletMembershipPlanMapping(
//                accessToken,
                outletId
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // Fetch One Dashboard Discount or Corporate Membership Masters
    fun fetchOutletOneDashboardMasterDetails(
        productId: Long,
        outletId: Long,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.retrofitService.fetchOutletOneDashboardMasterDetails(
//                accessToken,
                95,
                outletId)
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun saveOutletDiscountDetails(
        outletDiscountDetailsDTO: OutletDiscountDetailsDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitService.saveOutletDiscountDetails(
//            accessToken,
            contentType,
            outletDiscountDetailsDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // group roles listing
    fun fetchSubAdminGroupRoles(
        commonListingDTO: CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.mastersRetrofitService.fetchSubAdminGroupRoles(
//                accessToken,
                commonListingDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // save outlet subadmin
    fun saveOutletSubAdmin(
        subAdminDTO: SubAdminDTO,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.retrofitService.saveOutletSubAdmin(
//                accessToken,
                subAdminDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // outlet subadmin listing
    fun outletSubAdminListing(
        commonListingDTO: CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.retrofitService.outletSubAdminListing(
//                accessToken,
                commonListingDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // change subadmin status
    fun changeSubAdminStatus(
        userId: Long,
        status: Boolean,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.retrofitService.changeSubAdminStatus(
//                accessToken,
                userId,
                status
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getAllDishes(
        catalogueCategoryId: Long,
        outletId: Long,
        productName: String,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.getAllDishes(
//            accessToken,
            catalogueCategoryId,
            outletId,
            productName,
            SearchSortDTO(
                addtionalSearch = null,
                search = null,
                defaultSort = SortField("productId", "asc"),
                sort = null,
                length = 0,
                start = 0,
                status = true,
                statusCode = null
            )
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getAllDishFlag(
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.getAllDishFlag(
//            accessToken,
            SearchSortDTO(
                addtionalSearch = null,
                search = null,
                defaultSort = SortField("productFlagId", "asc"),
                sort = null,
                length = 0,
                start = 0,
                status = true,
                statusCode = null
            )
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun saveDish(
        productDetailsDTO: ProductDetailsDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.saveDish(
//            accessToken,
            productDetailsDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun changeDishStatus(
        dish: Long,
        status: Boolean,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.changeDishStatus(
//            accessToken,
            dish,
            status
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun saveDishTiming(
        dishTimingRequestDTO: DishTimingRequestDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.saveDishTiming(
//            accessToken,
            dishTimingRequestDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getDishDetails(
        productId: Long,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.getDishDetails(
//            accessToken,
            productId
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getDishTiming(
        productId: Long,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.getDishTiming(
//            accessToken,
            productId
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getInactiveDishes(
        catalogueCategoryId: Long,
        catalogueSectionId: Long,
        outletId: Long,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.getInactiveDishes(
//            accessToken,
            catalogueCategoryId,
            catalogueSectionId,
            outletId,
            SearchSortDTO(
                addtionalSearch = null,
                search = null,
                defaultSort = SortField("productId", "asc"),
                sort = null,
                length = 0,
                start = 0,
                status = false,
                statusCode = null
            )
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun saveDishSequence(
        productList: ArrayList<ProductDetailsDTO>,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.saveDishSequence(
//            accessToken,
            productList
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getStaffListing(
        commonListingDTO : CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.getStaffListing(
//            accessToken,
            commonListingDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // Known Languages listing
    fun fetchKnownLanguages(
        commonListingDTO: CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.mastersRetrofitService.fetchKnownLanguages(
//                accessToken,
                commonListingDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // Designation listing
    fun fetchDesignation(
        commonListingDTO: CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.mastersRetrofitService.fetchDesignation(
//                accessToken,
                commonListingDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }
    fun saveStaffUser(
        staffUserDTO: UserDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.saveStaffUser(
//            accessToken,
            staffUserDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }
    fun getStaffUserDetails(
        userId: Long,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.getStaffUserDetails(
//            accessToken,
            userId
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getStaffSafetyReadings(
        commonListingDTO: CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.getStaffSafetyReadings(
//            accessToken,
            commonListingDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }
    fun saveStaffSafetyReadings(
        staffSafetyReadingRequestDTO: StaffSafetyReadingRequestDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.saveStaffSafetyReadings(
//            accessToken,
            staffSafetyReadingRequestDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getDrawerMenu(callback: NetworkResponseCallback) {
        val response = UserApi.retrofitDashboardService.getDrawerMenu(
//            accessToken
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // logout service
    fun logoutUser(
        callback: NetworkResponseCallback
    ) {
        val response =
//            UserApi.retrofitService.logoutUser(accessToken)
            UserApi.retrofitService.logoutUser()
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // group roles staff listing
    fun fetchStaffGroupRoles(
        commonListingDTO: CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.mastersRetrofitService.fetchStaffGroupRoles(
//                accessToken,
                commonListingDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getAccessRoleListing(
        commonListingDTO : CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.getAccessRoleListing(
//            accessToken,
            commonListingDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }
    fun getApplicationModules(
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.getApplicationModules(
//            accessToken
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun saveAccessRole(
        accessRoleDTO: AccessRoleDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.saveAccessRole(
//            accessToken,
            accessRoleDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }
    fun getAccessRoleDetails(
        accessRoleId: Long,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.getAccessRoleDetails(
//            accessToken,
            accessRoleId
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun changeAccessRoleStatus(
        accessRoleId: Long,
        status: Boolean,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.changeAccessRoleStatus(
//            accessToken,
            accessRoleId,
            status
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getReservedTableListing(
        commonListingDTO: CommonListingDTO,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.getReservedTableListing(
//            accessToken,
            commonListingDTO
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun getTableListingByOutletId(
        outletId: Long,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.getTableListingByOutletId(
//            accessToken,
            outletId
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    fun assignTable(
        bookingId: Long,
        status: String,
        callback: NetworkResponseCallback
    ) {
        val response = UserApi.retrofitDashboardService.assignTable(
//            accessToken,
            bookingId,
            status
        )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    // Refresh Token service
    /*fun refreshToken(
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.retrofitService.refreshToken(emailId, refreshToken)
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }*/

    //save table listing
    fun saveTableListing(
        tableListManagementDTO: ArrayList<TableManagementDTO>,
        callback: NetworkResponseCallback
    ) {
        val response =
            UserApi.retrofitDashboardService.saveTableListing(
//                accessToken,
                tableListManagementDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }

    //get table listing
    fun getTableListing(commonListingDTO: CommonListingDTO, callback: NetworkResponseCallback) {
        val response =
            UserApi.retrofitDashboardService.getTableListing(
//                accessToken,
                commonListingDTO
            )
        val responseCall: Call<ResponseBody> = response
        responseCall.enqueue(RetrofitServiceTask(callback))
    }


}