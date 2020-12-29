package com.astrika.checqk.network.services

import com.astrika.checqk.model.*
import com.astrika.checqk.model.discount.OutletDiscountDetailsDTO
import com.astrika.checqk.network.network_utils.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @POST(IS_FIRST_TIME_LOGIN_WITH_LOGIN_ID)
    fun isFirstTimeLoginWithLoginId(@Body loginDTO: LoginDTO): Call<ResponseBody>

    @POST(LOGIN_MASTERS)
    fun loginMasters(@Body loginDTO: LoginDTO): Call<ResponseBody>

    @POST(LOGIN_WITH_LOGIN_ID)
    fun login(@Body loginDTO: LoginDTO): Call<ResponseBody>

    @POST(LOGIN_WITH_LOGIN_ID)
    fun verifyOtp(@Body loginDTO: LoginDTO): Call<ResponseBody>

    @POST(RESET_PASSWORD)
    fun resetPassword(
//        @Header("Authorization") accessToken: String,
        @Body resetPassword: ResetPassword
    ): Call<ResponseBody>


    // Timings
    @POST(FETCH_TIMINGS)
    fun fetchTimings(
//        @Header("Authorization") access_token: String,
        @Query("outletId") outletId: Long
    ): Call<ResponseBody>

    // Timings
    @POST(SAVE_TIMING)
    fun saveTimings(
//        @Header("Authorization") access_token: String,
        @Header("Content-Type") contentType: String,
        @Body timings: OutletTimingDTO
    ): Call<ResponseBody>


    // Closed Dates

    @POST(DELETE_OUTLET_CLOSED_DATE)
    fun deleteOutletClosedDate(
//        @Header("Authorization") access_token: String,
        @Query("outletDateId") outletDateId: Long,
        @Query("status") status: Boolean
    ): Call<ResponseBody>

    @POST(FETCH_OUTLET_CLOSED_DATES)
    fun fetchOutletClosedDates(
//        @Header("Authorization") access_token: String,
        @Query("outletId") outletId: Long
    ): Call<ResponseBody>

    @POST(SAVE_OUTLET_CLOSED_DATES)
    fun saveOutletClosedDates(
//        @Header("Authorization") access_token: String,
        @Header("Content-Type") contentType: String,
        @Body closedDate: ClosedDatesDTO
    ): Call<ResponseBody>


    // Safety features

    @POST(FETCH_SAFETY_MEASURES)
    fun fetchSafetyMeasures(
//        @Header("Authorization") access_token: String,
        @Query("outletId") outletId: Long
    ): Call<ResponseBody>

    @POST(SAVE_SAFETY_MEASURES)
    fun saveSafetyMeasures(
//        @Header("Authorization") access_token: String,
        @Header("Content-Type") contentType: String,
        @Body outletSecurityMeasuresDTO: OutletSecurityMeasuresDTO
    ): Call<ResponseBody>

    @POST(DELETE_SAFETY_MEASURES)
    fun deleteOutletSecurityMeasureById(
//        @Header("Authorization") access_token: String,
        @Query("outletSecurityMeasuresId") outletSecurityMeasuresId: Long,
        @Query("status") status: Boolean
    ): Call<ResponseBody>

    // Basic Info
    @POST(FETCH_RESTAURANT_DETAILS)
    fun fetchRestaurantDetails(
//        @Header("Authorization") access_token: String,
        @Query("outletId") outletId: Long
    ): Call<ResponseBody>

    @POST(SAVE_RESTAURANT_DETAILS)
    fun saveRestaurantDetails(
//        @Header("Authorization") access_token: String,
        @Header("Content-Type") contentType: String,
        @Body restaurantMasterDTO: RestaurantMasterDTO
    ): Call<ResponseBody>

    @POST(SAVE_RESTAURANT_DETAILS_BY_VISITOR)
    fun saveRestaurantDetailsByVisitor(
//        @Header("Authorization") access_token: String,
        @Header("Content-Type") contentType: String,
        @Query("assignAdmin") assignAdmin: Boolean,
        @Body restaurantMasterDTO: RestaurantMasterDTO
    ): Call<ResponseBody>


    // Communication Info
    @POST(FETCH_COMMUNICATION_INFO)
    fun fetchCommunicationInfo(
//        @Header("Authorization") access_token: String,
        @Query("outletId") outletId: Long
    ): Call<ResponseBody>

    @POST(SAVE_COMMUNICATION_INFO)
    fun saveCommunicationInfo(
//        @Header("Authorization") access_token: String,
        @Header("Content-Type") contentType: String,
        @Body communicationInfoDTO: CommunicationInfoDTO
    ): Call<ResponseBody>


    //Save restaurant image
    @POST(SAVE_RESTAURANT_PROFILE_IMAGE)
    fun saveRestaurantProfileImage(
//        @Header("Authorization") access_token: String,
        @Body restaurantProfileImageDTO: RestaurantProfileImageDTO
    ): Call<ResponseBody>

    //Get restaurant image
    @POST(GET_RESTAURANT_PROFILE_IMAGE)
    fun getRestaurantProfileImage(
//        @Header("Authorization") access_token: String,
        @Query("outletId") outletId: Long
    ): Call<ResponseBody>

    //Gallery image category
    @POST(GALLERY_IMAGE_CATEGORY)
    fun getGalleryImageCategory(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    //Gallery
    @POST(SAVE_GALLERY_IMAGES_BY_CATEGORY)
    fun saveGalleryImagesByCategory(
//        @Header("Authorization") access_token: String,
        @Body galleryImageCategory: GalleryImageCategory
    ): Call<ResponseBody>

    //Gallery
    @POST(GET_ALL_GALLERY_IMAGES)
    fun getAllGalleryImages(
//        @Header("Authorization") access_token: String,
        @Query("outletId") outletId: Long
    ): Call<ResponseBody>


    //Gallery
    @POST(REMOVE_GALLERY_IMAGE_BY_IMAGE_ID)
    fun removeGalleryImageByImageId(
//        @Header("Authorization") access_token: String,
        @Query("documentId") imageId: Long,
        @Query("id") id: Long
    ): Call<ResponseBody>

    //Gallery
    @POST(DISCARD_GALLERY_IMAGES_BY_CATEGORY)
    fun discardGalleryImageByCategoryId(
//        @Header("Authorization") access_token: String,
        @Query("outletId") outletId: Long,
        @Query("galleryImageCategoryId") galleryImageCategoryId: Long
    ): Call<ResponseBody>

    //Menu image category
    @POST(MENU_IMAGE_CATEGORY)
    fun getMenuImageCategory(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    //Menu image category
    @POST(SAVE_MENU_IMAGES_BY_CATEGORY)
    fun saveMenuImagesByCategory(
//        @Header("Authorization") access_token: String,
        @Body catalogueImageCategory: CatalogueImageCategory
    ): Call<ResponseBody>

    //Menu image category
    @POST(GET_ALL_MENU_IMAGES)
    fun getAllMenuImages(
//        @Header("Authorization") access_token: String,
        @Query("outletId") outletId: Long
    ): Call<ResponseBody>

    //Menu image category
    @POST(REMOVE_MENU_IMAGE_BY_IMAGE_ID)
    fun removeMenuImageByImageId(
//        @Header("Authorization") access_token: String,
        @Query("documentId") imageId: Long,
        @Query("id") id: Long
    ): Call<ResponseBody>

    @POST(DISCARD_MENU_IMAGES_BY_CATEGORY)
    fun discardMenuImageByCategoryId(
//        @Header("Authorization") access_token: String,
        @Query("outletId") outletId: Long,
        @Query("catalogueImageCategoryId") catalogueImageCategoryId: Long
    ): Call<ResponseBody>


    //Fetch Discount Details List
    @POST(FETCH_OUTLET_DISCOUNT_DETAILS_LIST)
    fun fetchOutletDiscountDetailsList(
//        @Header("Authorization") access_token: String,
        @Query("outletId") outletId: Long
    ): Call<ResponseBody>

    //Fetch Membership Plan Mapping
    @POST(FETCH_OUTLET_MEMBERSHIP_PLAN_MAPPING)
    fun fetchOutletMembershipPlanMapping(
//        @Header("Authorization") access_token: String,
        @Query("outletId") outletId: Long
    ): Call<ResponseBody>

    //Fetch One Dashboard Discount or Corporate Membership Masters
    @POST(FETCH_OUTLET_CORPORATE_MEMBERSHIP_OR_ONE_DASHBOARD)
    fun fetchOutletOneDashboardMasterDetails(
//        @Header("Authorization") access_token: String,
        @Query("productId") productId: Long,
        @Query("outletId") outletId: Long
    ): Call<ResponseBody>

    //Save Discount Details
    @POST(SAVE_OUTLET_DISCOUNT_DETAILS)
    fun saveOutletDiscountDetails(
//        @Header("Authorization") access_token: String,
        @Header("Content-Type") contentType: String,
        @Body outletDiscountDetailsDTO: OutletDiscountDetailsDTO
    ): Call<ResponseBody>


    @POST(SAVE_OUTLET_SUB_ADMIN)
    fun saveOutletSubAdmin(
//        @Header("Authorization") access_token: String,
        @Body subAdminDTO: SubAdminDTO
    ): Call<ResponseBody>

    @POST(OUTLET_SUB_ADMIN_LISTING)
    fun outletSubAdminListing(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    @POST(CHANGE_SUB_ADMIN_STATUS)
    fun changeSubAdminStatus(
//        @Header("Authorization") access_token: String,
        @Path("userId") userId: Long,
        @Path("status") status: Boolean
    ): Call<ResponseBody>


    @POST(LOGOUT_USER_URL)
    fun logoutUser(
//        @Header("Authorization") access_token: String
    ): Call<ResponseBody>

    @POST(REFRESH_TOKEN)
    fun refreshToken(@Body refreshTokenDTO: RefreshTokenDTO): Call<AccessTokenDTO>

}