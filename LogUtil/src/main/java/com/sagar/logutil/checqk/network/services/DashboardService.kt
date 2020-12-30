package com.sagar.logutil.checqk.network.services

import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.network.network_utils.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DashboardService {

    //Get all Menu categories
    @POST(GET_ALL_MENU_CATEGORIES)
    fun getAllMenuCategories(
//        @Header("Authorization") access_token: String,
        @Query("outletId") outletId: Long
    ):Call<ResponseBody>

    //Save Menu category
    @POST(SAVE_MENU_CATEGORY)
    fun saveMenuCategory(
//        @Header("Authorization") access_token: String,
        @Body menuCategory : CategoryCatalogueDTO
    ):Call<ResponseBody>

    //Save Menu categories Sequence
    @POST(CHANGE_MENU_CATEGORY_SEQUENCE)
    fun saveMenuCategoriesSequence(
//        @Header("Authorization") access_token: String,
        @Body menuCategoryList: ArrayList<CategoryCatalogueDTO>
    ):Call<ResponseBody>

    //Change Menu category status
    @POST(CHANGE_MENU_CATEGORY_STATUS)
    fun changeMenuCategoryStatus(
//        @Header("Authorization") access_token: String,
        @Query("catalogueCategoryID") catalogueCategoryID : Long,
        @Query("status") status : Boolean
    ):Call<ResponseBody>

    //Get all Menu section
    @POST(GET_ALL_MENU_SECTIONS)
    fun getAllMenuSections(
//        @Header("Authorization") access_token: String,
        @Query("catalogueCategoryId") catalogueCategoryId: Long,
        @Query("outletId") outletId: Long,
        @Body defaultSearchSort : SearchSortDTO
    ):Call<ResponseBody>

    //Save Menu category
    @POST(SAVE_MENU_SECTION)
    fun saveMenuSection(
//        @Header("Authorization") access_token: String,
        @Body catalogueSection : CatalogueSectionDTO
    ):Call<ResponseBody>

    //Save Menu categories Sequence
    @POST(CHANGE_MENU_SECTION_SEQUENCE)
    fun saveMenuSectionsSequence(
//        @Header("Authorization") access_token: String,
        @Body catalogueSectionList: ArrayList<CatalogueSectionDTO>
    ):Call<ResponseBody>

    //Change Menu Section status
    @POST(CHANGE_MENU_SECTION_STATUS)
    fun changeMenuSectionStatus(
//        @Header("Authorization") access_token: String,
        @Query("catalogueSectionId") catalogueSectionId : Long,
        @Query("status") status : Boolean
    ):Call<ResponseBody>

    //Get all Dishes
    @POST(GET_ALL_DISHES)
    fun getAllDishes(
//        @Header("Authorization") access_token: String,
        @Query("catalogueCategoryId") catalogueCategoryId: Long,
        @Query("outletId") outletId: Long,
        @Query("searchProduct") productName: String,
        @Body defaultSearchSort : SearchSortDTO
    ):Call<ResponseBody>

    //Save Dish
    @POST(SAVE_DISH)
    fun saveDish(
//        @Header("Authorization") access_token: String,
        @Body productDetailsDTO: ProductDetailsDTO
    ):Call<ResponseBody>

    //Change Menu Section status
    @POST(CHANGE_DISH_STATUS)
    fun changeDishStatus(
//        @Header("Authorization") access_token: String,
        @Query("productId") productId : Long,
        @Query("status") status : Boolean
    ):Call<ResponseBody>

    //Get All Dish Flags
    @POST(GET_ALL_DISH_FLAG)
    fun getAllDishFlag(
//        @Header("Authorization") access_token: String,
        @Body defaultSearchSort : SearchSortDTO
    ):Call<ResponseBody>

    //Save dish time
    @POST(SAVE_DISH_TIME)
    fun saveDishTiming(
//        @Header("Authorization") access_token: String,
        @Body dishTimingRequestDTO: DishTimingRequestDTO
    ):Call<ResponseBody>

    //Save Dish Sequence
    @POST(CHANGE_DISH_SEQUENCE)
    fun saveDishSequence(
//        @Header("Authorization") access_token: String,
        @Body productList: ArrayList<ProductDetailsDTO>
    ):Call<ResponseBody>

    //Get dish
    @POST(GET_DISH)
    fun getDishDetails(
//        @Header("Authorization") access_token: String,
        @Query("productId") productId: Long
    ):Call<ResponseBody>

    //Get dish timing
    @POST(GET_DISH_TIMING)
    fun getDishTiming(
//        @Header("Authorization") access_token: String,
        @Query("productId") productId: Long
    ):Call<ResponseBody>

    //Get all Inactive Dishes
    @POST(GET_ALL_INACTIVE_DISHES)
    fun getInactiveDishes(
//        @Header("Authorization") access_token: String,
        @Query("catalogueCategoryId") catalogueCategoryId: Long,
        @Query("catalogueSectionId") catalogueSectionId: Long,
        @Query("outletId") outletId: Long,
        @Body defaultSearchSort : SearchSortDTO
    ):Call<ResponseBody>

    //Get staff listing
    @POST(GET_STAFF_LISTING)
    fun getStaffListing(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO : CommonListingDTO
    ):Call<ResponseBody>

    //Save staff User
    @POST(SAVE_STAFF_USER)
    fun saveStaffUser(
//        @Header("Authorization") access_token: String,
        @Body staffUserDTO: UserDTO
    ):Call<ResponseBody>

    //get staff User
    @POST(GET_STAFF_USER_DETAILS)
    fun getStaffUserDetails(
//        @Header("Authorization") access_token: String,
        @Path("userId") userId : Long
    ):Call<ResponseBody>

    //get staff safety
    @POST(GET_STAFF_SAFETY_READINGS)
    fun getStaffSafetyReadings(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO
    ):Call<ResponseBody>

    //Save staff Safety Reading
    @POST(SAVE_STAFF_SAFETY_READINGS)
    fun saveStaffSafetyReadings(
//        @Header("Authorization") access_token: String,
        @Body staffSafetyReadingRequestDTO: StaffSafetyReadingRequestDTO
    ):Call<ResponseBody>

    /*  //Menu image category
      @POST(REMOVE_MENU_IMAGE_BY_IMAGE_ID)
      fun removeMenuImageByImageId(
          @Header("Authorization") access_token: String,
          @Query("documentId") imageId: Long,
          @Query("id") id: Long
      ):Call<ResponseBody>*/


    @POST(GET_DRAWER_MENU)
    fun getDrawerMenu(
//        @Header("Authorization") access_token: String
    ) : Call<ResponseBody>

    //Get Access role listing
    @POST(GET_ACCESS_ROLE_LISTING)
    fun getAccessRoleListing(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO : CommonListingDTO
    ):Call<ResponseBody>

    //Get Application Modules
    @POST(GET_APPLICATION_MODULES_LISTING)
    fun getApplicationModules(
//        @Header("Authorization") access_token: String
    ):Call<ResponseBody>

    //Save Access Role
    @POST(SAVE_ACCESS_ROLE)
    fun saveAccessRole(
//        @Header("Authorization") access_token: String,
        @Body accessRoleDTO: AccessRoleDTO
    ):Call<ResponseBody>

    //Get Access Role
    @POST(GET_ACCESS_ROLE)
    fun getAccessRoleDetails(
//        @Header("Authorization") access_token: String,
        @Query("id") accessRoleId: Long
    ):Call<ResponseBody>

    //Change Access Role Status
    @POST(CHANGE_ACCESS_ROLE_STATUS)
    fun changeAccessRoleStatus(
//        @Header("Authorization") access_token: String,
        @Query("id") accessRoleId : Long,
        @Query("status") status : Boolean
    ):Call<ResponseBody>

    //Get Reserved Table Listing
    @POST(GET_RESERVED_TABLE_LISTING)
    fun getReservedTableListing(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO : CommonListingDTO
    ):Call<ResponseBody>

    //Get Table Listing By Outlet Id
    @POST(GET_TABLE_LISTING_BY_OUTLET_ID)
    fun getTableListingByOutletId(
//        @Header("Authorization") access_token: String,
        @Query("outletId") outletId: Long
    ):Call<ResponseBody>

    //Change Booking status
    @POST(CHANGE_BOOKING_STATUS)
    fun assignTable(
//        @Header("Authorization") access_token: String,
        @Query("bookingId") bookingId: Long,
        @Query("status") status: String
    ):Call<ResponseBody>

    //Save Table Listing
    @POST(SAVE_TABLE_LISTING)
    fun saveTableListing(
//        @Header("Authorization") access_token: String,
        @Body tableManagementDTO: ArrayList<TableManagementDTO>
    ): Call<ResponseBody>

    //Get Table Details
    @POST(GET_TABLE_LISTING)
    fun getTableListing(
//        @Header("Authorization") access_token: String,
        @Body commonListingDTO: CommonListingDTO

    ): Call<ResponseBody>


}
