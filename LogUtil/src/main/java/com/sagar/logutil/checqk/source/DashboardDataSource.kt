package com.astrika.checqk.source

import androidx.annotation.NonNull
import com.astrika.checqk.model.*
import com.astrika.checqk.network.network_utils.IDataSourceCallback

interface DashboardDataSource {

    fun getAllMenuCategories(
        @NonNull outletId : Long,
        @NonNull callback: IDataSourceCallback<GetCatalogueCategoriesResponseDTO>
    )

    fun saveMenuCategories(
        @NonNull menuCategory : CategoryCatalogueDTO,
        @NonNull callback: IDataSourceCallback<Long>
    )

    fun saveMenuCategoriesSequence(
        @NonNull menuCategoryList : ArrayList<CategoryCatalogueDTO>,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun changeMenuCategoryStatus(
        @NonNull catalogueCategoryId : Long,
        @NonNull status : Boolean,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun getAllMenuSections(
        @NonNull catalogueCategoryId : Long,
        @NonNull outletId : Long,
        @NonNull callback: IDataSourceCallback<GetMenuSectionsResponseDTO>
    )

    fun saveMenuSection(
        @NonNull catalogueSection : CatalogueSectionDTO,
        @NonNull callback: IDataSourceCallback<Long>
    )

    fun saveMenuSectionsSequence(
        @NonNull catalogueSectionList : ArrayList<CatalogueSectionDTO>,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun changeMenuSectionStatus(
        @NonNull catalogueSectionId : Long,
        @NonNull status : Boolean,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun getAllDishes(
        @NonNull catalogueCategoryId : Long,
        @NonNull outletId : Long,
        @NonNull productName: String,
        @NonNull callback: IDataSourceCallback<ArrayList<ProductWithSectionDetails>>
    )

    fun getAllDishFlags(
        @NonNull callback: IDataSourceCallback<ArrayList<ProductFlagDTO>>
    )

    fun saveDish(
        @NonNull productDetailsDTO: ProductDetailsDTO,
        @NonNull callback: IDataSourceCallback<Long>
    )

    fun changeDishStatus(
        @NonNull productId : Long,
        @NonNull status : Boolean,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun saveDishTiming(
        @NonNull dishTimingRequestDTO: DishTimingRequestDTO,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun getDishDetails(
        @NonNull productId : Long,
        @NonNull callback: IDataSourceCallback<ProductDetailsDTO>
    )

    fun getDishTiming(
        @NonNull productId : Long,
        @NonNull callback: IDataSourceCallback<ArrayList<DishTimingDTO>>
    )

    fun saveDishSequence(
        @NonNull productList : ArrayList<ProductDetailsDTO>,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun getInactiveDishes(
        @NonNull catalogueCategoryId : Long,
        @NonNull catalogueSectionId : Long,
        @NonNull outletId : Long,
        @NonNull callback: IDataSourceCallback<ArrayList<ProductDetailsDTO>>
    )

    fun getStaffListing(
        @NonNull commonListingDTO : CommonListingDTO,
        @NonNull callback: IDataSourceCallback<ArrayList<UserDTO>>
    )

    fun saveStaffUser(
        @NonNull staffUserDTO: UserDTO,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun getStaffUserDetails(
        @NonNull userId : Long,
        @NonNull callback: IDataSourceCallback<UserDTO>
    )

    fun getStaffSafetyReadings(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<ArrayList<StaffSafetyReadingDTO>>
    )

    fun saveStaffSafetyReadings(
        @NonNull staffSafetyReadingRequestDTO: StaffSafetyReadingRequestDTO,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun getAccessRolesListing(
        @NonNull commonListingDTO : CommonListingDTO,
        @NonNull callback: IDataSourceCallback<ArrayList<AccessRoleDTO>>
    )

    fun getApplicationModules(
        @NonNull callback: IDataSourceCallback<ArrayList<PermissionDTO>>
    )

    fun saveAccessRole(
        @NonNull accessRoleDTO: AccessRoleDTO,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun getAccessRoleDetails(
        @NonNull accessRoleId : Long,
        @NonNull callback: IDataSourceCallback<AccessRoleDTO>
    )

    fun changeAccessRoleStatus(
        @NonNull accessRoleId : Long,
        @NonNull status : Boolean,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun getReservedTableListing(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<ArrayList<BookingDTO>>
    )

    fun getReservedTableListingByOutletId(
        @NonNull outletId: Long,
        @NonNull callback: IDataSourceCallback<ArrayList<TableManagementDTO>>
    )

    fun assignTable(
        @NonNull bookingId: Long,
        @NonNull status: String,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun saveTableListing(
        @NonNull tableListManagementDTO: ArrayList<TableManagementDTO>,
        @NonNull callback: IDataSourceCallback<String>
    )

    fun getTableListing(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<ArrayList<TableManagementDTO>>
    )


}