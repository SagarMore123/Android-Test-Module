package com.sagar.logutil.checqk.view.login.remote

import android.app.Application
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.model.discount.CorporateMembershipOneDashboardDTO
import com.sagar.logutil.checqk.model.discount.OutletDiscountDetailsDTO
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback

class UserRepository(private var mUserDataSource: UserDataSource?) : UserDataSource {

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        @JvmStatic
        fun getInstance(
            mApplication: Application,
            initRemoteRepository: Boolean
        ): UserRepository? {
            if (INSTANCE == null) {
                synchronized(UserRepository::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = UserRepository(
                            if (initRemoteRepository) UserRemoteDataSource.getInstance(
                                mApplication
                            ) else null
                        )
                    }
                }
            }
            return INSTANCE
        }

    }

    override fun isFirstTimeLoginWithLoginId(
        loginDTO: LoginDTO,
        callback: IDataSourceCallback<LoginResponseDTO>
    ) {
        mUserDataSource?.isFirstTimeLoginWithLoginId(loginDTO, callback)
    }

    override fun login(loginDTO: LoginDTO, callback: IDataSourceCallback<UserDTO>) {
        mUserDataSource?.login(loginDTO, callback)
    }

    override fun verifyOtp(loginDTO: LoginDTO, callback: IDataSourceCallback<String>) {
        mUserDataSource?.verifyOtp(loginDTO, callback)
    }

    override fun resetPassword(
        resetPassword: ResetPassword,
        callback: IDataSourceCallback<String>
    ) {
        mUserDataSource?.resetPassword(resetPassword, callback)
    }

    override fun fetchTimings(outletId: Long, callback: IDataSourceCallback<OutletTimingDTO>) {
        mUserDataSource?.fetchTimings(outletId, callback)
    }

    override fun saveTimings(timings: OutletTimingDTO, callback: IDataSourceCallback<String>) {
        mUserDataSource?.saveTimings(timings, callback)
    }

    // Closed Dates

    override fun deleteOutletClosedDate(
        outletDateId: Long,
        status: Boolean,
        callback: IDataSourceCallback<String>
    ) {
        mUserDataSource?.deleteOutletClosedDate(outletDateId, status, callback)
    }

    override fun fetchOutletClosedDates(
        outletId: Long,
        callback: IDataSourceCallback<ArrayList<ClosedDatesDTO>>
    ) {
        mUserDataSource?.fetchOutletClosedDates(outletId, callback)
    }

    override fun saveOutletClosedDates(
        closedDate: ClosedDatesDTO,
        callback: IDataSourceCallback<String>
    ) {
        mUserDataSource?.saveOutletClosedDates(closedDate, callback)
    }


    // Safety Measures

    override fun deleteOutletSecurityMeasureById(
        outletSecurityMeasuresId: Long,
        status: Boolean,
        callback: IDataSourceCallback<String>
    ) {
        mUserDataSource?.deleteOutletSecurityMeasureById(outletSecurityMeasuresId, status, callback)
    }

    override fun fetchSafetyMeasures(
        outletId: Long,
        callback: IDataSourceCallback<OutletSecurityMeasuresDTO>
    ) {
        mUserDataSource?.fetchSafetyMeasures(outletId, callback)

    }

    override fun saveSafetyMeasures(
        outletSecurityMeasuresDTO: OutletSecurityMeasuresDTO,
        callback: IDataSourceCallback<String>
    ) {
        mUserDataSource?.saveSafetyMeasures(outletSecurityMeasuresDTO, callback)
    }

    // Basic Info
    override fun fetchRestaurantDetails(
        outletId: Long,
        callback: IDataSourceCallback<RestaurantMasterDTO>
    ) {
        mUserDataSource?.fetchRestaurantDetails(outletId, callback)
    }

    override fun saveRestaurantDetails(
        restaurantMasterDTO: RestaurantMasterDTO,
        callback: IDataSourceCallback<String>
    ) {
        mUserDataSource?.saveRestaurantDetails(restaurantMasterDTO, callback)
    }

    override fun saveRestaurantDetailsByVisitor(
        restaurantMasterDTO: RestaurantMasterDTO,
        callback: IDataSourceCallback<CommonResponseDTO>
    ) {
        mUserDataSource?.saveRestaurantDetailsByVisitor(restaurantMasterDTO, callback)
    }

    // Communication Info
    override fun fetchCommunicationInfo(
        outletId: Long,
        callback: IDataSourceCallback<CommunicationInfoDTO>
    ) {
        mUserDataSource?.fetchCommunicationInfo(outletId, callback)
    }

    override fun saveCommunicationInfo(
        communicationInfoDTO: CommunicationInfoDTO,
        callback: IDataSourceCallback<String>
    ) {
        mUserDataSource?.saveCommunicationInfo(communicationInfoDTO, callback)
    }


    override fun saveRestaurantProfileImage(
        restaurantProfileImageDTO: RestaurantProfileImageDTO,
        callback: IDataSourceCallback<String>
    ) {
        mUserDataSource?.saveRestaurantProfileImage(restaurantProfileImageDTO, callback)
    }

    override fun getRestaurantProfileImage(
        outletId: Long,
        callback: IDataSourceCallback<RestaurantProfileImageDTO>
    ) {
        mUserDataSource?.getRestaurantProfileImage(outletId, callback)
    }

    override fun getGalleryImageCategory(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<CommonCategoryDTO>
    ) {
        mUserDataSource?.getGalleryImageCategory(commonListingDTO, callback)

    }

    override fun saveGalleryImagesByCategory(
        galleryImageCategory: GalleryImageCategory,
        callback: IDataSourceCallback<Long>
    ) {
        mUserDataSource?.saveGalleryImagesByCategory(galleryImageCategory, callback)
    }

    override fun getAllGalleryImages(
        outletId: Long,
        callback: IDataSourceCallback<ArrayList<GalleryImageCategory>>
    ) {
        mUserDataSource?.getAllGalleryImages(outletId, callback)
    }

    override fun removeGalleryImageByImageId(
        imageId: Long,
        id: Long,
        callback: IDataSourceCallback<String>
    ) {
        mUserDataSource?.removeGalleryImageByImageId(imageId, id, callback)
    }

    override fun discardGalleryImagesByCategoryId(
        outletId: Long,
        galleryImageCategoryId: Long,
        callback: IDataSourceCallback<String>
    ) {
        mUserDataSource?.discardGalleryImagesByCategoryId(
            outletId,
            galleryImageCategoryId,
            callback
        )
    }

    override fun getMenuImageCategory(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<CommonCategoryDTO>
    ) {
        mUserDataSource?.getMenuImageCategory(commonListingDTO, callback)
    }

    override fun saveMenuImagesByCategory(
        catalogueImageCategory: CatalogueImageCategory,
        callback: IDataSourceCallback<Long>
    ) {
        mUserDataSource?.saveMenuImagesByCategory(catalogueImageCategory, callback)
    }

    override fun getAllMenuImages(
        outletId: Long,
        callback: IDataSourceCallback<ArrayList<CatalogueImageCategory>>
    ) {
        mUserDataSource?.getAllMenuImages(outletId, callback)
    }

    override fun removeMenuImageByImageId(
        imageId: Long,
        id: Long,
        callback: IDataSourceCallback<String>
    ) {
        mUserDataSource?.removeMenuImageByImageId(imageId, id, callback)
    }

    override fun discardMenuImagesByCategoryId(
        outletId: Long,
        catalogueImageCategoryId: Long,
        callback: IDataSourceCallback<String>
    ) {
        mUserDataSource?.discardMenuImagesByCategoryId(outletId, catalogueImageCategoryId, callback)
    }


    // Discount
    override fun fetchOutletDiscountDetailsList(
        outletId: Long,
        callback: IDataSourceCallback<ArrayList<OutletDiscountDetailsDTO>>
    ) {
        mUserDataSource?.fetchOutletDiscountDetailsList(outletId, callback)
    }

    override fun fetchOutletOneDashboardMasterDetails(
        productId: Long,
        outletId: Long,
        callback: IDataSourceCallback<ArrayList<CorporateMembershipOneDashboardDTO>>
    ) {
        mUserDataSource?.fetchOutletOneDashboardMasterDetails(productId, outletId, callback)
    }

    override fun fetchOutletMembershipPlanMapping(
        outletId: Long,
        callback: IDataSourceCallback<Long>
    ) {
        mUserDataSource?.fetchOutletMembershipPlanMapping(outletId, callback)
    }

    override fun saveOutletDiscountDetails(
        outletDiscountDetailsDTO: OutletDiscountDetailsDTO,
        callback: IDataSourceCallback<String>
    ) {
        mUserDataSource?.saveOutletDiscountDetails(outletDiscountDetailsDTO, callback)
    }

    override fun saveOutletSubAdmin(
        subAdminDTO: SubAdminDTO,
        callback: IDataSourceCallback<String>
    ) {
        mUserDataSource?.saveOutletSubAdmin(subAdminDTO, callback)
    }

    override fun outletSubAdminListing(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<SubAdminDTO>>
    ) {
        mUserDataSource?.outletSubAdminListing(commonListingDTO, callback)
    }

    override fun changeSubAdminStatus(
        userId: Long,
        status: Boolean,
        callback: IDataSourceCallback<String>
    ) {
        mUserDataSource?.changeSubAdminStatus(userId, status, callback)
    }

    override fun logoutUser(callback: IDataSourceCallback<String>) {
        mUserDataSource?.logoutUser(callback)
    }

    /*override fun refreshToken(callback: IDataSourceCallback<String>) {
        mUserDataSource?.refreshToken(callback)
    }*/
}
