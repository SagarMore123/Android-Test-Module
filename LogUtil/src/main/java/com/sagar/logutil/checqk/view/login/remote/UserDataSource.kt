package com.sagar.logutil.checqk.view.login.remote

import androidx.annotation.NonNull
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.model.discount.CorporateMembershipOneDashboardDTO
import com.sagar.logutil.checqk.model.discount.OutletDiscountDetailsDTO
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback

interface UserDataSource {

    fun isFirstTimeLoginWithLoginId(
        @NonNull loginDTO: LoginDTO,
        @NonNull callback: IDataSourceCallback<LoginResponseDTO>
    )

    fun login(
        @NonNull loginDTO: LoginDTO,
        @NonNull callback: IDataSourceCallback<UserDTO>
    )

    fun verifyOtp(
        @NonNull loginDTO: LoginDTO,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun resetPassword(
        @NonNull resetPassword: ResetPassword,
        @NonNull callback: IDataSourceCallback<String>
    )


    fun fetchTimings(
        @NonNull outletId: Long,
        @NonNull callback: IDataSourceCallback<OutletTimingDTO>
    )

    fun saveTimings(
        @NonNull timings: OutletTimingDTO,
        @NonNull callback: IDataSourceCallback<String>
    )

    // Closed Dates
    fun deleteOutletClosedDate(
        @NonNull outletDateId: Long,
        @NonNull status: Boolean,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun fetchOutletClosedDates(
        @NonNull outletId: Long,
        @NonNull callback: IDataSourceCallback<ArrayList<ClosedDatesDTO>>
    )

    fun saveOutletClosedDates(
        @NonNull closedDate: ClosedDatesDTO,
        @NonNull callback: IDataSourceCallback<String>
    )

    // Outlet Service
    fun deleteOutletSecurityMeasureById(
        @NonNull outletSecurityMeasuresId: Long,
        @NonNull status: Boolean,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun fetchSafetyMeasures(
        @NonNull outletId: Long,
        @NonNull callback: IDataSourceCallback<OutletSecurityMeasuresDTO>
    )

    fun saveSafetyMeasures(
        @NonNull outletSecurityMeasuresDTO: OutletSecurityMeasuresDTO,
        @NonNull callback: IDataSourceCallback<String>
    )

    // Basic Ifo
    fun fetchRestaurantDetails(
        @NonNull outletId: Long,
        @NonNull callback: IDataSourceCallback<RestaurantMasterDTO>
    )

    fun saveRestaurantDetails(
        @NonNull restaurantMasterDTO: RestaurantMasterDTO,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun saveRestaurantDetailsByVisitor(
        @NonNull restaurantMasterDTO: RestaurantMasterDTO,
        @NonNull callback: IDataSourceCallback<CommonResponseDTO>
    )

    // Communication Ifo
    fun fetchCommunicationInfo(
        @NonNull outletId: Long,
        @NonNull callback: IDataSourceCallback<CommunicationInfoDTO>
    )

    fun saveCommunicationInfo(
        @NonNull communicationInfoDTO: CommunicationInfoDTO,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun saveRestaurantProfileImage(
        @NonNull restaurantProfileImageDTO: RestaurantProfileImageDTO,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun getRestaurantProfileImage(
        @NonNull outletId: Long,
        @NonNull callback: IDataSourceCallback<RestaurantProfileImageDTO>
    )

    fun getGalleryImageCategory(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<CommonCategoryDTO>
    )

    fun saveGalleryImagesByCategory(
        @NonNull galleryImageCategory: GalleryImageCategory,
        @NonNull callback: IDataSourceCallback<Long>
    )

    fun getAllGalleryImages(
        @NonNull outletId: Long,
        @NonNull callback: IDataSourceCallback<ArrayList<GalleryImageCategory>>
    )

    fun removeGalleryImageByImageId(
        @NonNull imageId: Long,
        @NonNull id: Long,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun discardGalleryImagesByCategoryId(
        @NonNull outletId: Long,
        @NonNull galleryImageCategoryId: Long,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun getMenuImageCategory(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<CommonCategoryDTO>
    )

    fun saveMenuImagesByCategory(
        @NonNull catalogueImageCategory: CatalogueImageCategory,
        @NonNull callback: IDataSourceCallback<Long>
    )

    fun getAllMenuImages(
        @NonNull outletId: Long,
        @NonNull callback: IDataSourceCallback<ArrayList<CatalogueImageCategory>>
    )

    fun removeMenuImageByImageId(
        @NonNull imageId: Long,
        @NonNull id: Long,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun discardMenuImagesByCategoryId(
        @NonNull outletId: Long,
        @NonNull catalogueImageCategoryId: Long,
        @NonNull callback: IDataSourceCallback<String>
    )

    // Discount
    fun fetchOutletDiscountDetailsList(
        @NonNull outletId: Long,
        @NonNull callback: IDataSourceCallback<ArrayList<OutletDiscountDetailsDTO>>
    )

    fun fetchOutletOneDashboardMasterDetails(
        @NonNull productId: Long,
        @NonNull outletId: Long,
        @NonNull callback: IDataSourceCallback<ArrayList<CorporateMembershipOneDashboardDTO>>
    )

    fun fetchOutletMembershipPlanMapping(
        @NonNull outletId: Long,
        @NonNull callback: IDataSourceCallback<Long>
    )

    fun saveOutletDiscountDetails(
        @NonNull outletDiscountDetailsDTO: OutletDiscountDetailsDTO,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun saveOutletSubAdmin(
        @NonNull subAdminDTO: SubAdminDTO,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun outletSubAdminListing(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<List<SubAdminDTO>>
    )

    fun changeSubAdminStatus(
        @NonNull userId: Long,
        @NonNull status: Boolean,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun logoutUser(
        @NonNull callback: IDataSourceCallback<String>
    )

    /*fun refreshToken(
        @NonNull callback: IDataSourceCallback<String>
    )*/

}