package com.astrika.checqk.source

import android.app.Application
import com.astrika.checqk.model.*
import com.astrika.checqk.network.network_utils.IDataSourceCallback

class DashboardRepository(private var mDashboardDataSource: DashboardDataSource?) : DashboardDataSource {

    companion object {
        @Volatile
        private var INSTANCE: DashboardRepository? = null

        @JvmStatic
        fun getInstance(
            mApplication: Application,
            initRemoteRepository: Boolean
        ): DashboardRepository? {
            if (INSTANCE == null) {
                synchronized(DashboardRepository::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = DashboardRepository(
                            if (initRemoteRepository) DashboardRemoteDataSource.getInstance(
                                mApplication
                            ) else null
                        )
                    }
                }
            }
            return INSTANCE
        }

    }

    override fun getAllMenuCategories(
        outletId: Long,
        callback: IDataSourceCallback<GetCatalogueCategoriesResponseDTO>
    ) {
        mDashboardDataSource?.getAllMenuCategories(outletId, callback)

    }


    override fun saveMenuCategories(
        menuCategory: CategoryCatalogueDTO,
        callback: IDataSourceCallback<Long>
    ) {
        mDashboardDataSource?.saveMenuCategories(menuCategory, callback)
    }

    override fun saveMenuCategoriesSequence(
        menuCategoryList: ArrayList<CategoryCatalogueDTO>,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.saveMenuCategoriesSequence(menuCategoryList, callback)
    }

    override fun changeMenuCategoryStatus(
        catalogueCategoryId: Long,
        status: Boolean,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.changeMenuCategoryStatus(catalogueCategoryId, status, callback)
    }

    override fun getAllMenuSections(catalogueCategoryId: Long, outletId: Long, callback: IDataSourceCallback<GetMenuSectionsResponseDTO>) {
        mDashboardDataSource?.getAllMenuSections(catalogueCategoryId, outletId, callback)

    }

    override fun saveMenuSection(
        catalogueSection: CatalogueSectionDTO,
        callback: IDataSourceCallback<Long>
    ) {
        mDashboardDataSource?.saveMenuSection(catalogueSection, callback)

    }

    override fun saveMenuSectionsSequence(
        catalogueSectionList: ArrayList<CatalogueSectionDTO>,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.saveMenuSectionsSequence(catalogueSectionList, callback)
    }

    override fun changeMenuSectionStatus(
        catalogueSectionId: Long,
        status: Boolean,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.changeMenuSectionStatus(catalogueSectionId, status, callback)
    }

    override fun getAllDishes(
        catalogueCategoryId: Long,
        outletId: Long,
        productName: String,
        callback: IDataSourceCallback<ArrayList<ProductWithSectionDetails>>
    ) {
        mDashboardDataSource?.getAllDishes(catalogueCategoryId, outletId, productName, callback)
    }

    override fun getAllDishFlags(callback: IDataSourceCallback<ArrayList<ProductFlagDTO>>) {
        mDashboardDataSource?.getAllDishFlags(callback)
    }

    override fun saveDish(productDetailsDTO: ProductDetailsDTO, callback: IDataSourceCallback<Long>) {
        mDashboardDataSource?.saveDish(productDetailsDTO, callback)
    }

    override fun saveDishTiming(
        dishTimingRequestDTO: DishTimingRequestDTO,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.saveDishTiming(dishTimingRequestDTO, callback)
    }

    override fun changeDishStatus(
        productId: Long,
        status: Boolean,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.changeDishStatus(productId, status, callback)
    }

    override fun getDishDetails(productId: Long, callback: IDataSourceCallback<ProductDetailsDTO>) {
        mDashboardDataSource?.getDishDetails(productId, callback)
    }

    override fun getDishTiming(
        productId: Long,
        callback: IDataSourceCallback<ArrayList<DishTimingDTO>>
    ) {
        mDashboardDataSource?.getDishTiming(productId, callback)
    }

    override fun getInactiveDishes(
        catalogueCategoryId: Long,
        catalogueSectionId: Long,
        outletId: Long,
        callback: IDataSourceCallback<ArrayList<ProductDetailsDTO>>
    ) {
        mDashboardDataSource?.getInactiveDishes(catalogueCategoryId, catalogueSectionId, outletId, callback)
    }

    override fun saveDishSequence(
        productList: ArrayList<ProductDetailsDTO>,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.saveDishSequence(productList, callback)
    }

    override fun getStaffListing(
        commonListingDTO : CommonListingDTO,
        callback: IDataSourceCallback<ArrayList<UserDTO>>
    ) {
        mDashboardDataSource?.getStaffListing(commonListingDTO, callback)
    }

    override fun saveStaffUser(staffUserDTO: UserDTO, callback: IDataSourceCallback<String>) {
        mDashboardDataSource?.saveStaffUser(staffUserDTO, callback)
    }

    override fun getStaffUserDetails(userId: Long, callback: IDataSourceCallback<UserDTO>) {
        mDashboardDataSource?.getStaffUserDetails(userId, callback)
    }

    override fun getStaffSafetyReadings(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<ArrayList<StaffSafetyReadingDTO>>
    ) {
        mDashboardDataSource?.getStaffSafetyReadings(commonListingDTO, callback)
    }

    override fun saveStaffSafetyReadings(
        staffSafetyReadingRequestDTO: StaffSafetyReadingRequestDTO,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.saveStaffSafetyReadings(staffSafetyReadingRequestDTO, callback)
    }

    override fun getAccessRolesListing(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<ArrayList<AccessRoleDTO>>
    ) {
        mDashboardDataSource?.getAccessRolesListing(commonListingDTO, callback)
    }

    override fun getApplicationModules(callback: IDataSourceCallback<ArrayList<PermissionDTO>>) {
        mDashboardDataSource?.getApplicationModules(callback)
    }

    override fun saveAccessRole(
        accessRoleDTO: AccessRoleDTO,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.saveAccessRole(accessRoleDTO, callback)
    }

    override fun getAccessRoleDetails(
        accessRoleId: Long,
        callback: IDataSourceCallback<AccessRoleDTO>
    ) {
        mDashboardDataSource?.getAccessRoleDetails(accessRoleId, callback)
    }

    override fun changeAccessRoleStatus(
        accessRoleId: Long,
        status: Boolean,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.changeAccessRoleStatus(accessRoleId, status, callback)
    }

    override fun getReservedTableListing(commonListingDTO: CommonListingDTO,callback: IDataSourceCallback<ArrayList<BookingDTO>>) {
        mDashboardDataSource?.getReservedTableListing(commonListingDTO,callback)
    }

    override fun getReservedTableListingByOutletId(
        outletId: Long,
        callback: IDataSourceCallback<ArrayList<TableManagementDTO>>
    ) {
        mDashboardDataSource?.getReservedTableListingByOutletId(outletId,callback)
    }

    override fun assignTable(
        bookingId: Long,
        status: String,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.assignTable(bookingId, status, callback)
    }

    override fun saveTableListing(
        tableListManagementDTO: ArrayList<TableManagementDTO>,
        callback: IDataSourceCallback<String>
    ) {
        mDashboardDataSource?.saveTableListing(tableListManagementDTO, callback)
    }

    override fun getTableListing(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<ArrayList<TableManagementDTO>>
    ) {
        mDashboardDataSource?.getTableListing(commonListingDTO, callback)
    }
}
