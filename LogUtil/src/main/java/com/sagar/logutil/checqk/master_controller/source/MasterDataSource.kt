package com.sagar.logutil.checqk.master_controller.source

import androidx.annotation.NonNull
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.model.discount.*
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback

abstract class MasterDataSource {


    open fun loginMasters(
        @NonNull loginDTO: LoginDTO,
        @NonNull callback: IDataSourceCallback<String>
    ) {

    }

    open fun saveSystemValueMasterDataLocal(
        dataList: List<SystemValueMasterDTO>,
        callback: IDataSourceCallback<List<SystemValueMasterDTO>>
    ) {

    }

    open fun fetchSystemValueMasterValueByIdLocal(
        systemValueId: Long,
        callback: IDataSourceCallback<SystemValueMasterDTO>
    ) {

    }

    open fun fetchSystemMasterValueByNameLocal(
        systemValueName: String,
        callback: IDataSourceCallback<List<SystemValueMasterDTO>>
    ) {

    }

    open fun fetchSystemValueMasterDataRemote(callback: IDataSourceCallback<List<SystemValueMasterDTO>>) {}


    open fun saveMealTypeMasterDataLocal(
        @NonNull dataList: List<MealTypeMasterDTO>,
        @NonNull callback: IDataSourceCallback<List<MealTypeMasterDTO>>
    ) {
    }

    open fun fetchMealTypeMasterDataRemote(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<List<MealTypeMasterDTO>>
    ) {
    }

    open fun fetchMealTypeMasterDataLocal(callback: IDataSourceCallback<List<MealTypeMasterDTO>>) {}

    open fun fetchMealTypeMasterDataById(
        id: Long,
        callback: IDataSourceCallback<MealTypeMasterDTO>
    ) {
    }

    open fun saveOutletTypeDataLocal(
        @NonNull dataList: List<OutletTypeMasterDTO>,
        @NonNull callback: IDataSourceCallback<List<OutletTypeMasterDTO>>
    ) {
    }

    open fun fetchOutletTypeDataRemote(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<List<OutletTypeMasterDTO>>
    ) {
    }

    open fun fetchOutletTypeDataLocal(callback: IDataSourceCallback<List<OutletTypeMasterDTO>>) {}


    open fun fetchOutletTypeMasterDataById(
        id: Long,
        callback: IDataSourceCallback<OutletTypeMasterDTO>
    ) {
    }

    /**
     * Save the Cuisine data from the service
     * @param dataList
     * @param callback
     */
    open fun saveCuisineMasterDataLocal(
        @NonNull dataList: List<CuisineMasterDTO>,
        @NonNull callback: IDataSourceCallback<List<CuisineMasterDTO>>
    ) {
    }

    /**
     * Get the list of Cuisine from remote server
     * @param callback
     * @return
     */
    open fun fetchCuisineMasterDataRemote(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<List<CuisineMasterDTO>>
    ) {
    }

    /**
     * Get the list of Cuisine from local data source
     * @return
     */
    open fun fetchCuisineMasterDataLocal(callback: IDataSourceCallback<List<CuisineMasterDTO>>) {}

    open fun fetchCuisineMasterDataById(
        id: Long,
        callback: IDataSourceCallback<CuisineMasterDTO>
    ) {
    }

    // Food Type
    /**
     * Save the Food Type data from the service
     * @param dataList
     * @param callback
     */
    open fun saveFoodTypeMasterDataLocal(
        @NonNull dataList: List<FoodTypeMasterDTO>,
        @NonNull callback: IDataSourceCallback<List<FoodTypeMasterDTO>>
    ) {
    }

    /**
     * Get the list of Food Type from remote server
     * @param callback
     * @return
     */
    open fun fetchFoodTypeMasterDataRemote(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<List<FoodTypeMasterDTO>>
    ) {
    }

    /**
     * Get the list of Food Type from local data source
     * @return
     */
    open fun fetchFoodTypeMasterDataLocal(callback: IDataSourceCallback<List<FoodTypeMasterDTO>>) {}

    open fun fetchFoodTypeMasterDataById(
        id: Long,
        callback: IDataSourceCallback<FoodTypeMasterDTO>
    ) {
    }


    /**
     * Save the Famous For data from the service
     * @param dataList
     * @param callback
     */
    open fun saveFamousForMasterDataLocal(
        @NonNull dataList: List<FamousForMasterDTO>,
        @NonNull callback: IDataSourceCallback<List<FamousForMasterDTO>>
    ) {
    }

    /**
     * Get the list of Famous For from remote server
     * @param callback
     * @return
     */
    open fun fetchFamousForMasterDataRemote(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<List<FamousForMasterDTO>>
    ) {
    }

    /**
     * Get the list of Famous For from local data source
     * @return
     */
    open fun fetchFamousForMasterDataLocal(callback: IDataSourceCallback<List<FamousForMasterDTO>>) {}

    open fun fetchFamousForMasterDataById(
        id: Long,
        callback: IDataSourceCallback<FamousForMasterDTO>
    ) {
    }

    open fun saveFacilityMasterDataLocal(
        @NonNull dataList: List<FacilityMasterDTO>,
        @NonNull callback: IDataSourceCallback<List<FacilityMasterDTO>>
    ) {
    }

    open fun fetchFacilityMastersRemote(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<List<FacilityMasterDTO>>
    ) {
    }

    open fun fetchFacilityMastersLocal(callback: IDataSourceCallback<List<FacilityMasterDTO>>) {}
    open fun fetchFacilityMasterDTOByIdLocal(
        id: Long,
        callback: IDataSourceCallback<FacilityMasterDTO?>
    ) {
    }

    open fun saveSocialMediaMasterDataLocal(
        @NonNull dataList: List<SocialMediaMasterDTO>,
        @NonNull callback: IDataSourceCallback<List<SocialMediaMasterDTO>>
    ) {
    }

    open fun fetchSocialMediaMastersRemote(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<List<SocialMediaMasterDTO>>
    ) {
    }

    open fun fetchSocialMediaMastersLocal(callback: IDataSourceCallback<List<SocialMediaMasterDTO>>) {}
    open fun fetchSocialMediaMasterDTOByIdLocal(
        id: Long,
        callback: IDataSourceCallback<SocialMediaMasterDTO>
    ) {
    }

    // Address Masters

    // Country
    open fun saveCountryMasterDataLocal(
        dataList: List<CountryMasterDTO>,
        callback: IDataSourceCallback<List<CountryMasterDTO>>
    ) {

    }

    open fun fetchCountryMasterByIdLocal(
        id: Long,
        callback: IDataSourceCallback<String>
    ) {

    }

    open fun fetchCountryMasterDataLocal(callback: IDataSourceCallback<List<CountryMasterDTO>>) {}

    open fun fetchCountryMasterDataRemote(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<List<CountryMasterDTO>>
    ) {
    }

    // State
    open fun saveStateMasterDataLocal(
        dataList: List<StateMasterDTO>,
        callback: IDataSourceCallback<List<StateMasterDTO>>
    ) {

    }

    open fun fetchStateMasterByCountryIdLocal(
        id: Long,
        callback: IDataSourceCallback<List<StateMasterDTO>>
    ) {

    }

    open fun fetchStateMasterDataRemote(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<List<StateMasterDTO>>
    ) {
    }


    // City
    open fun saveCityMasterDataLocal(
        dataList: List<CityMasterDTO>,
        callback: IDataSourceCallback<List<CityMasterDTO>>
    ) {

    }

    open fun fetchCityMasterByStateIdLocal(
        id: Long,
        callback: IDataSourceCallback<List<CityMasterDTO>>
    ) {

    }

    open fun fetchCityMasterDataRemote(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<List<CityMasterDTO>>
    ) {
    }


    // Area
    open fun saveAreaMasterDataLocal(
        dataList: List<AreaMasterDTO>,
        callback: IDataSourceCallback<List<AreaMasterDTO>>
    ) {

    }

    open fun fetchAreaMasterByStateIdLocal(
        id: Long,
        callback: IDataSourceCallback<List<AreaMasterDTO>>
    ) {

    }

    open fun fetchAreaMasterDataRemote(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<List<AreaMasterDTO>>
    ) {
    }

    // Pincode
    open fun savePincodeMasterDataLocal(
        dataList: List<PincodeMasterDTO>,
        callback: IDataSourceCallback<List<PincodeMasterDTO>>
    ) {

    }

    open fun fetchPincodeMasterByAreaIdLocal(
        id: Long,
        callback: IDataSourceCallback<PincodeMasterDTO>
    ) {

    }

    open fun fetchPincodeMasterDataRemote(callback: IDataSourceCallback<List<PincodeMasterDTO>>) {}


    // Discount Category
    open fun saveDiscountCategoryMasterDataLocal(
        dataList: List<OutletDiscountCategoryDTO>,
        callback: IDataSourceCallback<List<OutletDiscountCategoryDTO>>
    ) {

    }

    open fun fetchDiscountCategoryMasterDataLocal(callback: IDataSourceCallback<List<OutletDiscountCategoryDTO>>) {}

    open fun fetchDiscountCategoryMasterByIdLocal(
        id: Long,
        callback: IDataSourceCallback<String>
    ) {
    }

    open fun fetchDiscountCategoryMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<OutletDiscountCategoryDTO>>
    ) {
    }


    // Discount Membership Plan
    open fun saveDiscountMembershipPlanMasterDataLocal(
        dataList: List<OutletDiscountMembershipPlanDTO>,
        callback: IDataSourceCallback<List<OutletDiscountMembershipPlanDTO>>
    ) {

    }

    open fun fetchDiscountMembershipPlanMasterDataLocal(callback: IDataSourceCallback<List<OutletDiscountMembershipPlanDTO>>) {}

    open fun fetchDiscountMembershipPlanMasterByIdLocal(
        id: Long,
        callback: IDataSourceCallback<OutletDiscountMembershipPlanDTO>
    ) {
    }

    open fun fetchDiscountMembershipPlanMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<OutletDiscountMembershipPlanDTO>>
    ) {
    }


    // Cravx Card Discount Membership Holder
    open fun saveCravxCardDiscountMembershipHolderMasterDataLocal(
        dataList: List<CravxCardsMembershipHolderDTO>,
        callback: IDataSourceCallback<List<CravxCardsMembershipHolderDTO>>
    ) {

    }

    open fun fetchCravxCardDiscountMembershipHolderMasterDataLocal(callback: IDataSourceCallback<List<CravxCardsMembershipHolderDTO>>) {}

    open fun fetchCravxCardDiscountMembershipHolderMasterByIdLocal(
        id: Long,
        callback: IDataSourceCallback<CravxCardsMembershipHolderDTO>
    ) {
    }

    open fun fetchCravxCardDiscountMembershipHolderMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<CravxCardsMembershipHolderDTO>>
    ) {
    }


    // HW Discount Membership Holder
    open fun saveHWDiscountMembershipHolderMasterDataLocal(
        dataList: List<HWMembershipHolderDTO>,
        callback: IDataSourceCallback<List<HWMembershipHolderDTO>>
    ) {

    }

    open fun fetchHWDiscountMembershipHolderMasterDataLocal(callback: IDataSourceCallback<List<HWMembershipHolderDTO>>) {}

    open fun fetchHWDiscountMembershipHolderMasterByIdLocal(
        id: Long,
        callback: IDataSourceCallback<HWMembershipHolderDTO>
    ) {
    }

    open fun fetchHWDiscountMembershipHolderMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<HWMembershipHolderDTO>>
    ) {
    }

    // In-House Discount Membership Holder
    open fun saveInHouseDiscountMembershipHolderMasterDataLocal(
        dataList: List<InHouseMembershipHolderDTO>,
        callback: IDataSourceCallback<List<InHouseMembershipHolderDTO>>
    ) {

    }

    open fun fetchInHouseDiscountMembershipHolderMasterDataLocal(callback: IDataSourceCallback<List<InHouseMembershipHolderDTO>>) {}

    open fun fetchInHouseDiscountMembershipHolderMasterByIdLocal(
        id: Long,
        callback: IDataSourceCallback<InHouseMembershipHolderDTO>
    ) {
    }

    open fun fetchInHouseDiscountMembershipHolderMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<InHouseMembershipHolderDTO>>
    ) {
    }

    // One Dashboard Discount Membership Holder
    open fun saveOneDashboardDiscountMembershipHolderMasterDataLocal(
        dataList: List<OneDashboardMembershipHolderDTO>,
        callback: IDataSourceCallback<List<OneDashboardMembershipHolderDTO>>
    ) {

    }

    open fun fetchOneDashboardDiscountMembershipHolderMasterDataLocal(callback: IDataSourceCallback<List<OneDashboardMembershipHolderDTO>>) {}

    open fun fetchOneDashboardDiscountMembershipHolderMasterByIdLocal(
        id: Long,
        callback: IDataSourceCallback<OneDashboardMembershipHolderDTO>
    ) {
    }

    open fun fetchOneDashboardDiscountMembershipHolderMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<OneDashboardMembershipHolderDTO>>
    ) {
    }

    //Group roles
    open fun saveGroupRolesMasterDataLocal(
        @NonNull dataList: List<GroupRolesDTO>,
        @NonNull callback: IDataSourceCallback<List<GroupRolesDTO>>
    ) {
    }

    open fun fetchGroupRolesMasterDataRemote(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<List<GroupRolesDTO>>
    ) {
    }

    open fun fetchGroupRolesMasterDataLocal(callback: IDataSourceCallback<List<GroupRolesDTO>>) {}

    open fun fetchGroupRolesMasterDataById(
        id: Long,
        callback: IDataSourceCallback<GroupRolesDTO>
    ) {
    }

    //Dashboard Drawer
    open fun saveDashboardDrawerMasterDataLocal(
        @NonNull dataList: List<DashboardDrawerDTO>,
        @NonNull callback: IDataSourceCallback<List<DashboardDrawerDTO>>
    ) {
    }

    open fun fetchDashboardDrawerMasterDataRemote(
        @NonNull callback: IDataSourceCallback<List<DashboardDrawerDTO>>
    ) {
    }

    open fun fetchDashboardDrawerMasterDataLocal(callback: IDataSourceCallback<List<DashboardDrawerDTO>>) {}

    open fun fetchDashboardDrawerMasterDataById(
        id: Long,
        callback: IDataSourceCallback<DashboardDrawerDTO>
    ) {
    }

    //Known Languages
    open fun fetchKnownLanguagesMasterDataRemote(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<List<KnownLanguagesDTO>>
    ) {
    }

    open fun saveKnownLanguagesMasterDataLocal(
        @NonNull dataList: List<KnownLanguagesDTO>,
        @NonNull callback: IDataSourceCallback<List<KnownLanguagesDTO>>
    ) {
    }


    open fun fetchKnownLanguagesMasterDataLocal(callback: IDataSourceCallback<List<KnownLanguagesDTO>>) {}

    open fun fetchKnownLanguagesMasterDataById(
        id: Long,
        callback: IDataSourceCallback<KnownLanguagesDTO>
    ) {
    }


    //Designation
    open fun fetchDesignationMasterDataRemote(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<List<DesignationDTO>>
    ) {
    }

    open fun saveDesignationMasterDataLocal(
        @NonNull dataList: List<DesignationDTO>,
        @NonNull callback: IDataSourceCallback<List<DesignationDTO>>
    ) {
    }

    open fun fetchDesignationMasterDataLocal(callback: IDataSourceCallback<List<DesignationDTO>>) {}

    open fun fetchDesignationMasterDataById(
        id: Long,
        callback: IDataSourceCallback<DesignationDTO>
    ) {
    }

    //Group roles Staff
    open fun saveGroupRolesStaffMasterDataLocal(
        @NonNull dataList: List<GroupRolesStaffDTO>,
        @NonNull callback: IDataSourceCallback<List<GroupRolesStaffDTO>>
    ) {
    }

    open fun fetchGroupRolesStaffMasterDataRemote(
        @NonNull commonListingDTO: CommonListingDTO,
        @NonNull callback: IDataSourceCallback<List<GroupRolesStaffDTO>>
    ) {
    }

    open fun fetchGroupRolesStaffMasterDataLocal(callback: IDataSourceCallback<List<GroupRolesStaffDTO>>) {}

    open fun fetchGroupRolesStaffMasterDataById(
        id: Long,
        callback: IDataSourceCallback<GroupRolesStaffDTO>
    ) {
    }
}