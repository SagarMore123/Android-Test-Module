package com.sagar.logutil.checqk.network.services

import com.sagar.logutil.checqk.model.CommonListingDTO
import com.sagar.logutil.checqk.network.network_utils.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MastersService {

/*
    // Outlet Types
    @GET(FETCH_SVM)
    fun fetchSVM(): Call<ResponseBody>
*/

    // Outlet Types
    @GET(FETCH_SVM)
    fun fetchSVM(
//        @Header("Authorization") access_token: String
    ): Call<ResponseBody>


    // Outlet Types
    @POST(FETCH_OUTLET_TYPE)
    fun fetchOutLetTypes(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    // Meal Types
    @POST(FETCH_MEAL_TYPE)
    fun fetchMealTypes(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    // Food Types
    @POST(FETCH_FOOD_TYPE)
    fun fetchFoodTypes(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    // Facilities
    @POST(FETCH_FACILITIES)
    fun fetchFacilities(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    // Cuisines
    @POST(FETCH_CUISINES)
    fun fetchCuisines(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    // Country
    @POST(FETCH_COUNTRY)
    fun fetchCountries(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    // State
    @POST(FETCH_STATE)
    fun fetchStates(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    // City
    @POST(FETCH_CITY)
    fun fetchCites(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    // Areas
    @POST(FETCH_AREA)
    fun fetchAreas(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    // Social Media
    @POST(FETCH_SOCIAL_MEDIA)
    fun fetchSocialMedia(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>


    // Pincodes
    @POST(FETCH_PINCODE)
    fun fetchPincodes(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    // Discount Category
    @POST(FETCH_DISCOUNT_CATEGORIES)
    fun fetchDiscountCategories(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    //  Discount Membership Plan // Basic, Corporate Plan
    @POST(FETCH_DISCOUNT_MEMBERSHIP_PLAN)
    fun fetchDiscountMembershipPlan(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    // Cravx Card Discount Membership Holder
    @POST(FETCH_CRAVX_CARDS_DISCOUNT_MEMBERSHIP_HOLDER_MASTERS)
    fun fetchCravxCardsDiscountMembershipHolderMasters(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    // HW Discount Membership Holder
    @POST(FETCH_HW_DISCOUNT_MEMBERSHIP_HOLDER_MASTERS)
    fun fetchHWDiscountMembershipHolderMasters(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    // In-House Discount Membership Holder
    @POST(FETCH_IN_HOUSE_DISCOUNT_MEMBERSHIP_HOLDER_MASTERS)
    fun fetchInHouseDiscountMembershipHolderMasters(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    @POST(KNOWN_LANGUAGES_LISTING)
    fun fetchKnownLanguages(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    @POST(DESIGNATION_LISTING)
    fun fetchDesignation(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>


    // Sub Admin Group Roles
    @POST(GROUP_ROLE_SUB_ADMIN_LISTING)
    fun fetchSubAdminGroupRoles(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>

    // Staff Group Roles
    @POST(GROUP_ROLE_STAFF_LISTING)
    fun fetchStaffGroupRoles(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ): Call<ResponseBody>
}