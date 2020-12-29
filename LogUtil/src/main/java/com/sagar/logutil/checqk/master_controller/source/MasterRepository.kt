package com.astrika.checqk.master_controller.source

import android.content.Context
import com.astrika.checqk.master_controller.CravXRoomDatabase
import com.astrika.checqk.master_controller.source.local.MasterLocalDataSource
import com.astrika.checqk.master_controller.source.remote.MasterRemoteDataSource
import com.astrika.checqk.master_controller.sync.AppExecutors
import com.astrika.checqk.model.*
import com.astrika.checqk.model.discount.*
import com.astrika.checqk.network.network_utils.IDataSourceCallback

class MasterRepository(
    private var mMasterRemoteDataSource: MasterDataSource?,
    private var mMasterLocalDataSource: MasterDataSource?
) : MasterDataSource() {

    companion object {
        @Volatile
        private var INSTANCE: MasterRepository? = null

        fun getInstance(context: Context?): MasterRepository? {
            if (INSTANCE == null) {
//                synchronized(UserRepository::class.java) {
                if (INSTANCE == null) {
                    val databaseCravX: CravXRoomDatabase = CravXRoomDatabase.getDatabase(context!!)
                    INSTANCE = MasterRepository(
                        MasterRemoteDataSource.getInstance(context),
                        MasterLocalDataSource.getInstance(
                            AppExecutors(),
                            databaseCravX.systemValueMasterDao(),
                            databaseCravX.mealTypeMasterDao(),
                            databaseCravX.outLetTypeDao(),
                            databaseCravX.memberDao(),
                            databaseCravX.skillDao(),
                            databaseCravX.statusDao(),
                            databaseCravX.socialMediaMasterDao(),
                            databaseCravX.knownLanguagesDao(),
                            databaseCravX.designationDao(),
                            databaseCravX.groupRoleStaffDao(),

                            // Address DAOs
                            databaseCravX.countryDao(),
                            databaseCravX.stateDao(),
                            databaseCravX.cityDao(),
                            databaseCravX.areaDao(),
                            databaseCravX.pincodeDao(),

                            // Discount
                            databaseCravX.discountCategoryDao(),
                            databaseCravX.discountMembershipPlanDao(),
                            databaseCravX.cravxCardMembershipHolderDao(),
                            databaseCravX.hwMembershipHolderDao(),
                            databaseCravX.inHouseMembershipHolderDao(),

                            //Group roles
                            databaseCravX.groupRoleDao(),
                            databaseCravX.dashboardDrawerDao()
                        )
                    )
//                    }
                }
            }
            return INSTANCE
        }
    }


    override fun loginMasters(loginDTO: LoginDTO, callback: IDataSourceCallback<String>) {
        mMasterRemoteDataSource?.loginMasters(loginDTO, callback)
    }


    override fun saveSystemValueMasterDataLocal(
        dataList: List<SystemValueMasterDTO>,
        callback: IDataSourceCallback<List<SystemValueMasterDTO>>
    ) {
        mMasterLocalDataSource?.saveSystemValueMasterDataLocal(dataList, callback)
    }

    override fun fetchSystemValueMasterValueByIdLocal(
        systemValueId: Long,
        callback: IDataSourceCallback<SystemValueMasterDTO>
    ) {
        mMasterLocalDataSource?.fetchSystemValueMasterValueByIdLocal(systemValueId, callback)
    }

    override fun fetchSystemMasterValueByNameLocal(
        systemValueName: String,
        callback: IDataSourceCallback<List<SystemValueMasterDTO>>
    ) {
        mMasterLocalDataSource?.fetchSystemMasterValueByNameLocal(systemValueName, callback)
    }


    override fun fetchSystemValueMasterDataRemote(callback: IDataSourceCallback<List<SystemValueMasterDTO>>) {
        mMasterRemoteDataSource?.fetchSystemValueMasterDataRemote(object :
            IDataSourceCallback<List<SystemValueMasterDTO>> {

            override fun onDataFound(data: List<SystemValueMasterDTO>) {
                saveSystemValueMasterDataLocal(data, callback)
            }

            override fun onError(error: String) {
                callback.onError(error)
            }

        })
    }


    /*--------  Meal type section -----------*/

    override fun saveMealTypeMasterDataLocal(
        dataList: List<MealTypeMasterDTO>,
        callback: IDataSourceCallback<List<MealTypeMasterDTO>>
    ) {
        mMasterLocalDataSource?.saveMealTypeMasterDataLocal(dataList, callback)
    }

    override fun fetchMealTypeMasterDataLocal(callback: IDataSourceCallback<List<MealTypeMasterDTO>>) {
        mMasterLocalDataSource?.fetchMealTypeMasterDataLocal(callback)
    }

    override fun fetchMealTypeMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<MealTypeMasterDTO>>
    ) {

        mMasterRemoteDataSource?.fetchMealTypeMasterDataRemote(
            commonListingDTO,
            object : IDataSourceCallback<List<MealTypeMasterDTO>> {

                override fun onDataFound(data: List<MealTypeMasterDTO>) {
                    saveMealTypeMasterDataLocal(data, callback)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })

    }

    override fun fetchMealTypeMasterDataById(
        id: Long,
        callback: IDataSourceCallback<MealTypeMasterDTO>
    ) {
        mMasterLocalDataSource?.fetchMealTypeMasterDataById(id, callback)
    }

    /*--------  Outlet Type section -----------*/

    override fun saveOutletTypeDataLocal(
        dataList: List<OutletTypeMasterDTO>,
        callback: IDataSourceCallback<List<OutletTypeMasterDTO>>
    ) {
        mMasterLocalDataSource?.saveOutletTypeDataLocal(dataList, callback)
    }

    override fun fetchOutletTypeDataLocal(callback: IDataSourceCallback<List<OutletTypeMasterDTO>>) {
        mMasterLocalDataSource?.fetchOutletTypeDataLocal(callback)
    }

    override fun fetchOutletTypeDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<OutletTypeMasterDTO>>
    ) {

        mMasterRemoteDataSource?.fetchOutletTypeDataRemote(
            commonListingDTO,
            object : IDataSourceCallback<List<OutletTypeMasterDTO>> {

                override fun onDataFound(data: List<OutletTypeMasterDTO>) {
                    saveOutletTypeDataLocal(data, callback)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })

    }

    override fun fetchOutletTypeMasterDataById(
        id: Long,
        callback: IDataSourceCallback<OutletTypeMasterDTO>
    ) {
        mMasterLocalDataSource?.fetchOutletTypeMasterDataById(id, callback)
    }


    /*--------  Cuisine section -----------*/

    override fun saveCuisineMasterDataLocal(
        dataList: List<CuisineMasterDTO>,
        callback: IDataSourceCallback<List<CuisineMasterDTO>>
    ) {
        mMasterLocalDataSource?.saveCuisineMasterDataLocal(dataList, callback)
    }

    override fun fetchCuisineMasterDataLocal(callback: IDataSourceCallback<List<CuisineMasterDTO>>) {
        mMasterLocalDataSource?.fetchCuisineMasterDataLocal(callback)
    }

    override fun fetchCuisineMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<CuisineMasterDTO>>
    ) {

        mMasterRemoteDataSource?.fetchCuisineMasterDataRemote(
            commonListingDTO,
            object : IDataSourceCallback<List<CuisineMasterDTO>> {

                override fun onDataFound(data: List<CuisineMasterDTO>) {
                    saveCuisineMasterDataLocal(data, callback)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })

    }

    override fun fetchCuisineMasterDataById(
        id: Long,
        callback: IDataSourceCallback<CuisineMasterDTO>
    ) {
        mMasterLocalDataSource?.fetchCuisineMasterDataById(id, callback)
    }


    /*--------  Food Type section -----------*/

    override fun saveFoodTypeMasterDataLocal(
        dataList: List<FoodTypeMasterDTO>,
        callback: IDataSourceCallback<List<FoodTypeMasterDTO>>
    ) {
        mMasterLocalDataSource?.saveFoodTypeMasterDataLocal(dataList, callback)
    }

    override fun fetchFoodTypeMasterDataLocal(callback: IDataSourceCallback<List<FoodTypeMasterDTO>>) {
        mMasterLocalDataSource?.fetchFoodTypeMasterDataLocal(callback)
    }

    override fun fetchFoodTypeMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<FoodTypeMasterDTO>>
    ) {

        mMasterRemoteDataSource?.fetchFoodTypeMasterDataRemote(commonListingDTO,
            object : IDataSourceCallback<List<FoodTypeMasterDTO>> {

                override fun onDataFound(data: List<FoodTypeMasterDTO>) {
                    saveFoodTypeMasterDataLocal(data, callback)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })

    }

    override fun fetchFoodTypeMasterDataById(
        id: Long,
        callback: IDataSourceCallback<FoodTypeMasterDTO>
    ) {
        mMasterLocalDataSource?.fetchFoodTypeMasterDataById(id, callback)
    }


    //--------  Facility section -----------

    override fun saveFacilityMasterDataLocal(
        dataList: List<FacilityMasterDTO>,
        callback: IDataSourceCallback<List<FacilityMasterDTO>>
    ) {
        mMasterLocalDataSource?.saveFacilityMasterDataLocal(dataList, callback)
    }

    override fun fetchFacilityMastersLocal(callback: IDataSourceCallback<List<FacilityMasterDTO>>) {
        mMasterLocalDataSource?.fetchFacilityMastersLocal(callback)
    }

    override fun fetchFacilityMasterDTOByIdLocal(
        id: Long,
        callback: IDataSourceCallback<FacilityMasterDTO?>
    ) {
        mMasterLocalDataSource?.fetchFacilityMasterDTOByIdLocal(id, callback)
    }

    override fun fetchFacilityMastersRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<FacilityMasterDTO>>
    ) {
        mMasterRemoteDataSource?.fetchFacilityMastersRemote(
            commonListingDTO,
            object : IDataSourceCallback<List<FacilityMasterDTO>> {

                override fun onDataFound(data: List<FacilityMasterDTO>) {
                    saveFacilityMasterDataLocal(data, callback)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })
    }


    //--------  Country section -----------

    override fun saveCountryMasterDataLocal(
        dataList: List<CountryMasterDTO>,
        callback: IDataSourceCallback<List<CountryMasterDTO>>
    ) {
        mMasterLocalDataSource?.saveCountryMasterDataLocal(dataList, callback)
    }

    override fun fetchCountryMasterDataLocal(callback: IDataSourceCallback<List<CountryMasterDTO>>) {
        mMasterLocalDataSource?.fetchCountryMasterDataLocal(callback)
    }

    override fun fetchCountryMasterByIdLocal(
        id: Long,
        callback: IDataSourceCallback<String>
    ) {
        mMasterLocalDataSource?.fetchCountryMasterByIdLocal(id, callback)
    }

    override fun fetchCountryMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<CountryMasterDTO>>
    ) {
        mMasterRemoteDataSource?.fetchCountryMasterDataRemote(
            commonListingDTO,
            object : IDataSourceCallback<List<CountryMasterDTO>> {

                override fun onDataFound(data: List<CountryMasterDTO>) {
                    saveCountryMasterDataLocal(data, callback)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })
    }


    //--------  City section -----------


    override fun saveCityMasterDataLocal(
        dataList: List<CityMasterDTO>,
        callback: IDataSourceCallback<List<CityMasterDTO>>
    ) {
        mMasterLocalDataSource?.saveCityMasterDataLocal(dataList, callback)
    }

    override fun fetchCityMasterByStateIdLocal(
        id: Long,
        callback: IDataSourceCallback<List<CityMasterDTO>>
    ) {
        mMasterLocalDataSource?.fetchCityMasterByStateIdLocal(id, callback)

    }

    override fun fetchCityMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<CityMasterDTO>>
    ) {
        mMasterRemoteDataSource?.fetchCityMasterDataRemote(
            commonListingDTO,
            object : IDataSourceCallback<List<CityMasterDTO>> {

                override fun onDataFound(data: List<CityMasterDTO>) {
                    saveCityMasterDataLocal(data, callback)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })
    }


    // State
    override fun saveStateMasterDataLocal(
        dataList: List<StateMasterDTO>,
        callback: IDataSourceCallback<List<StateMasterDTO>>
    ) {
        mMasterLocalDataSource?.saveStateMasterDataLocal(dataList, callback)
    }

    override fun fetchStateMasterByCountryIdLocal(
        id: Long,
        callback: IDataSourceCallback<List<StateMasterDTO>>
    ) {
        mMasterLocalDataSource?.fetchStateMasterByCountryIdLocal(id, callback)
    }

    override fun fetchStateMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<StateMasterDTO>>
    ) {

        mMasterRemoteDataSource?.fetchStateMasterDataRemote(
            commonListingDTO,
            object : IDataSourceCallback<List<StateMasterDTO>> {

                override fun onDataFound(data: List<StateMasterDTO>) {
                    saveStateMasterDataLocal(data, callback)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })
    }

    override fun saveAreaMasterDataLocal(
        dataList: List<AreaMasterDTO>,
        callback: IDataSourceCallback<List<AreaMasterDTO>>
    ) {
        mMasterLocalDataSource?.saveAreaMasterDataLocal(dataList, callback)
    }

    override fun fetchAreaMasterByStateIdLocal(
        id: Long,
        callback: IDataSourceCallback<List<AreaMasterDTO>>
    ) {
        mMasterLocalDataSource?.fetchAreaMasterByStateIdLocal(id, callback)
    }

    override fun fetchAreaMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<AreaMasterDTO>>
    ) {

        mMasterRemoteDataSource?.fetchAreaMasterDataRemote(
            commonListingDTO,
            object : IDataSourceCallback<List<AreaMasterDTO>> {

                override fun onDataFound(data: List<AreaMasterDTO>) {
                    saveAreaMasterDataLocal(data, callback)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })
    }


    override fun saveSocialMediaMasterDataLocal(
        dataList: List<SocialMediaMasterDTO>,
        callback: IDataSourceCallback<List<SocialMediaMasterDTO>>
    ) {
        mMasterLocalDataSource?.saveSocialMediaMasterDataLocal(dataList, callback)
    }


    override fun fetchSocialMediaMastersLocal(callback: IDataSourceCallback<List<SocialMediaMasterDTO>>) {
        mMasterLocalDataSource?.fetchSocialMediaMastersLocal(callback)
    }

    override fun fetchSocialMediaMasterDTOByIdLocal(
        id: Long,
        callback: IDataSourceCallback<SocialMediaMasterDTO>
    ) {
        mMasterLocalDataSource?.fetchSocialMediaMasterDTOByIdLocal(id, callback)
    }


    override fun fetchSocialMediaMastersRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<SocialMediaMasterDTO>>
    ) {

        mMasterRemoteDataSource?.fetchSocialMediaMastersRemote(
            commonListingDTO,
            object : IDataSourceCallback<List<SocialMediaMasterDTO>> {

                override fun onDataFound(data: List<SocialMediaMasterDTO>) {
                    saveSocialMediaMasterDataLocal(data, callback)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })
    }

    override fun savePincodeMasterDataLocal(
        dataList: List<PincodeMasterDTO>,
        callback: IDataSourceCallback<List<PincodeMasterDTO>>
    ) {
        mMasterLocalDataSource?.savePincodeMasterDataLocal(dataList, callback)
    }

    override fun fetchPincodeMasterByAreaIdLocal(
        id: Long,
        callback: IDataSourceCallback<PincodeMasterDTO>
    ) {
        mMasterLocalDataSource?.fetchPincodeMasterByAreaIdLocal(id, callback)
    }

    override fun fetchPincodeMasterDataRemote(callback: IDataSourceCallback<List<PincodeMasterDTO>>) {
        mMasterLocalDataSource?.fetchPincodeMasterDataRemote(callback)
    }

    //Discount Categories
    override fun saveDiscountCategoryMasterDataLocal(
        dataList: List<OutletDiscountCategoryDTO>,
        callback: IDataSourceCallback<List<OutletDiscountCategoryDTO>>
    ) {
        mMasterLocalDataSource?.saveDiscountCategoryMasterDataLocal(dataList, callback)
    }

    override fun fetchDiscountCategoryMasterDataLocal(callback: IDataSourceCallback<List<OutletDiscountCategoryDTO>>) {
        mMasterLocalDataSource?.fetchDiscountCategoryMasterDataLocal(callback)
    }

    override fun fetchDiscountCategoryMasterByIdLocal(
        id: Long,
        callback: IDataSourceCallback<String>
    ) {
        mMasterLocalDataSource?.fetchDiscountCategoryMasterByIdLocal(id, callback)
    }

    override fun fetchDiscountCategoryMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<OutletDiscountCategoryDTO>>
    ) {

        mMasterRemoteDataSource?.fetchDiscountCategoryMasterDataRemote(
            commonListingDTO,
            object : IDataSourceCallback<List<OutletDiscountCategoryDTO>> {

                override fun onDataFound(data: List<OutletDiscountCategoryDTO>) {
                    saveDiscountCategoryMasterDataLocal(data, callback)
                }

                override fun onDataNotFound() {
                    callback.onDataNotFound()
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })

    }

    // Discount Membership Plan
    override fun saveDiscountMembershipPlanMasterDataLocal(
        dataList: List<OutletDiscountMembershipPlanDTO>,
        callback: IDataSourceCallback<List<OutletDiscountMembershipPlanDTO>>
    ) {
        mMasterLocalDataSource?.saveDiscountMembershipPlanMasterDataLocal(dataList, callback)
    }

    override fun fetchDiscountMembershipPlanMasterDataLocal(callback: IDataSourceCallback<List<OutletDiscountMembershipPlanDTO>>) {
        mMasterLocalDataSource?.fetchDiscountMembershipPlanMasterDataLocal(callback)
    }

    override fun fetchDiscountMembershipPlanMasterByIdLocal(
        id: Long,
        callback: IDataSourceCallback<OutletDiscountMembershipPlanDTO>
    ) {
        mMasterLocalDataSource?.fetchDiscountMembershipPlanMasterByIdLocal(id, callback)
    }

    override fun fetchDiscountMembershipPlanMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<OutletDiscountMembershipPlanDTO>>
    ) {

        mMasterRemoteDataSource?.fetchDiscountMembershipPlanMasterDataRemote(
            commonListingDTO,
            object : IDataSourceCallback<List<OutletDiscountMembershipPlanDTO>> {

                override fun onDataFound(data: List<OutletDiscountMembershipPlanDTO>) {
                    saveDiscountMembershipPlanMasterDataLocal(data, callback)
                }

                override fun onDataNotFound() {
                    callback.onDataNotFound()
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })

    }

    override fun saveCravxCardDiscountMembershipHolderMasterDataLocal(
        dataList: List<CravxCardsMembershipHolderDTO>,
        callback: IDataSourceCallback<List<CravxCardsMembershipHolderDTO>>
    ) {
        mMasterLocalDataSource?.saveCravxCardDiscountMembershipHolderMasterDataLocal(
            dataList,
            callback
        )
    }

    override fun fetchCravxCardDiscountMembershipHolderMasterDataLocal(callback: IDataSourceCallback<List<CravxCardsMembershipHolderDTO>>) {
        mMasterLocalDataSource?.fetchCravxCardDiscountMembershipHolderMasterDataLocal(callback)
    }

    override fun fetchCravxCardDiscountMembershipHolderMasterByIdLocal(
        id: Long,
        callback: IDataSourceCallback<CravxCardsMembershipHolderDTO>
    ) {
        mMasterLocalDataSource?.fetchCravxCardDiscountMembershipHolderMasterByIdLocal(id, callback)
    }

    override fun fetchCravxCardDiscountMembershipHolderMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<CravxCardsMembershipHolderDTO>>
    ) {
        mMasterRemoteDataSource?.fetchCravxCardDiscountMembershipHolderMasterDataRemote(
            commonListingDTO,
            object : IDataSourceCallback<List<CravxCardsMembershipHolderDTO>> {

                override fun onDataFound(data: List<CravxCardsMembershipHolderDTO>) {
                    saveCravxCardDiscountMembershipHolderMasterDataLocal(data, callback)
                }

                override fun onDataNotFound() {
                    callback.onDataNotFound()
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })
    }

    // HW Discount Membership Holder section
    override fun saveHWDiscountMembershipHolderMasterDataLocal(
        dataList: List<HWMembershipHolderDTO>,
        callback: IDataSourceCallback<List<HWMembershipHolderDTO>>
    ) {
        mMasterLocalDataSource?.saveHWDiscountMembershipHolderMasterDataLocal(dataList, callback)
    }

    override fun fetchHWDiscountMembershipHolderMasterDataLocal(callback: IDataSourceCallback<List<HWMembershipHolderDTO>>) {
        mMasterLocalDataSource?.fetchHWDiscountMembershipHolderMasterDataLocal(callback)
    }

    override fun fetchHWDiscountMembershipHolderMasterByIdLocal(
        id: Long,
        callback: IDataSourceCallback<HWMembershipHolderDTO>
    ) {
        mMasterLocalDataSource?.fetchHWDiscountMembershipHolderMasterByIdLocal(id, callback)
    }

    override fun fetchHWDiscountMembershipHolderMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<HWMembershipHolderDTO>>
    ) {
        mMasterRemoteDataSource?.fetchHWDiscountMembershipHolderMasterDataRemote(
            commonListingDTO,
            object : IDataSourceCallback<List<HWMembershipHolderDTO>> {

                override fun onDataFound(data: List<HWMembershipHolderDTO>) {
                    saveHWDiscountMembershipHolderMasterDataLocal(data, callback)
                }

                override fun onDataNotFound() {
                    callback.onDataNotFound()
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })
    }

    // In-House Discount Membership Holder section
    override fun saveInHouseDiscountMembershipHolderMasterDataLocal(
        dataList: List<InHouseMembershipHolderDTO>,
        callback: IDataSourceCallback<List<InHouseMembershipHolderDTO>>
    ) {
        mMasterLocalDataSource?.saveInHouseDiscountMembershipHolderMasterDataLocal(
            dataList,
            callback
        )
    }

    override fun fetchInHouseDiscountMembershipHolderMasterDataLocal(callback: IDataSourceCallback<List<InHouseMembershipHolderDTO>>) {
        mMasterLocalDataSource?.fetchInHouseDiscountMembershipHolderMasterDataLocal(callback)
    }

    override fun fetchInHouseDiscountMembershipHolderMasterByIdLocal(
        id: Long,
        callback: IDataSourceCallback<InHouseMembershipHolderDTO>
    ) {
        mMasterLocalDataSource?.fetchInHouseDiscountMembershipHolderMasterByIdLocal(id, callback)
    }

    override fun fetchInHouseDiscountMembershipHolderMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<InHouseMembershipHolderDTO>>
    ) {
        mMasterRemoteDataSource?.fetchInHouseDiscountMembershipHolderMasterDataRemote(
            commonListingDTO,
            object : IDataSourceCallback<List<InHouseMembershipHolderDTO>> {

                override fun onDataFound(data: List<InHouseMembershipHolderDTO>) {
                    saveInHouseDiscountMembershipHolderMasterDataLocal(data, callback)
                }

                override fun onDataNotFound() {
                    callback.onDataNotFound()
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })
    }


    /*  override fun fetchGroupRolesMasterDataRemote(
          commonListingDTO: CommonListingDTO,
          callback: IDataSourceCallback<List<GroupRolesDTO>>
      ) {
          mMasterRemoteDataSource?.fetchGroupRolesMasterDataRemote(
              commonListingDTO,
              object : IDataSourceCallback<List<GroupRolesDTO>> {

                  override fun onDataFound(data: List<GroupRolesDTO>) {
                      saveGroupRolesMasterDataLocal(data, callback)
                  }

                  override fun onError(error: String) {
                      callback.onError(error)
                  }
              }
          )
      }
  */

    /*--------  Group Role section -----------*/

    override fun saveGroupRolesMasterDataLocal(
        dataList: List<GroupRolesDTO>,
        callback: IDataSourceCallback<List<GroupRolesDTO>>
    ) {
        mMasterLocalDataSource?.saveGroupRolesMasterDataLocal(dataList, callback)
    }

    override fun fetchGroupRolesMasterDataLocal(callback: IDataSourceCallback<List<GroupRolesDTO>>) {
        mMasterLocalDataSource?.fetchGroupRolesMasterDataLocal(callback)
    }

    override fun fetchGroupRolesMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<GroupRolesDTO>>
    ) {

        mMasterRemoteDataSource?.fetchGroupRolesMasterDataRemote(
            commonListingDTO,
            object : IDataSourceCallback<List<GroupRolesDTO>> {

                override fun onDataFound(data: List<GroupRolesDTO>) {
                    saveGroupRolesMasterDataLocal(data, callback)
                }

                override fun onDataNotFound() {
                    callback.onDataNotFound()
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })

    }

    override fun fetchGroupRolesMasterDataById(
        id: Long,
        callback: IDataSourceCallback<GroupRolesDTO>
    ) {
        mMasterLocalDataSource?.fetchGroupRolesMasterDataById(id, callback)
    }

    override fun saveDashboardDrawerMasterDataLocal(
        dataList: List<DashboardDrawerDTO>,
        callback: IDataSourceCallback<List<DashboardDrawerDTO>>
    ) {
        mMasterLocalDataSource?.saveDashboardDrawerMasterDataLocal(dataList, callback)
    }

    override fun fetchDashboardDrawerMasterDataLocal(callback: IDataSourceCallback<List<DashboardDrawerDTO>>) {
        mMasterLocalDataSource?.fetchDashboardDrawerMasterDataLocal(callback)
    }

    override fun fetchDashboardDrawerMasterDataRemote(
        callback: IDataSourceCallback<List<DashboardDrawerDTO>>
    ) {
        mMasterRemoteDataSource?.fetchDashboardDrawerMasterDataRemote(
            object : IDataSourceCallback<List<DashboardDrawerDTO>> {

                override fun onDataFound(data: List<DashboardDrawerDTO>) {
                    saveDashboardDrawerMasterDataLocal(data, callback)
                }

                override fun onDataNotFound() {
                    callback.onDataNotFound()
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })

    }

    override fun fetchDashboardDrawerMasterDataById(
        id: Long,
        callback: IDataSourceCallback<DashboardDrawerDTO>
    ) {
        mMasterLocalDataSource?.fetchDashboardDrawerMasterDataById(id, callback)
    }

    //Known Languages
    override fun fetchKnownLanguagesMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<KnownLanguagesDTO>>
    ) {
        mMasterRemoteDataSource?.fetchKnownLanguagesMasterDataRemote(
            commonListingDTO,
            object : IDataSourceCallback<List<KnownLanguagesDTO>> {
                override fun onDataFound(data: List<KnownLanguagesDTO>) {
                    saveKnownLanguagesMasterDataLocal(data, callback)
                }

                override fun onDataNotFound() {
                    callback.onDataNotFound()
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
        )
    }

    override fun saveKnownLanguagesMasterDataLocal(
        dataList: List<KnownLanguagesDTO>,
        callback: IDataSourceCallback<List<KnownLanguagesDTO>>
    ) {
        mMasterLocalDataSource?.saveKnownLanguagesMasterDataLocal(dataList, callback)
    }

    override fun fetchKnownLanguagesMasterDataLocal(callback: IDataSourceCallback<List<KnownLanguagesDTO>>) {
        mMasterLocalDataSource?.fetchKnownLanguagesMasterDataLocal(callback)
    }

    override fun fetchKnownLanguagesMasterDataById(
        id: Long,
        callback: IDataSourceCallback<KnownLanguagesDTO>
    ) {
        mMasterLocalDataSource?.fetchKnownLanguagesMasterDataById(id, callback)
    }

    override fun fetchDesignationMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<DesignationDTO>>
    ) {
        mMasterRemoteDataSource?.fetchDesignationMasterDataRemote(
            commonListingDTO,
            object : IDataSourceCallback<List<DesignationDTO>> {
                override fun onDataFound(data: List<DesignationDTO>) {
                    saveDesignationMasterDataLocal(data, callback)
                }

                override fun onDataNotFound() {
                    callback.onDataNotFound()
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
        )
    }

    override fun saveDesignationMasterDataLocal(
        dataList: List<DesignationDTO>,
        callback: IDataSourceCallback<List<DesignationDTO>>
    ) {
        mMasterLocalDataSource?.saveDesignationMasterDataLocal(dataList, callback)
    }

    override fun fetchDesignationMasterDataLocal(callback: IDataSourceCallback<List<DesignationDTO>>) {
        mMasterLocalDataSource?.fetchDesignationMasterDataLocal(callback)
    }

    override fun fetchDesignationMasterDataById(
        id: Long,
        callback: IDataSourceCallback<DesignationDTO>
    ) {
        mMasterLocalDataSource?.fetchDesignationMasterDataById(id, callback)
    }

    /*--------  Group Role Staff section -----------*/

    override fun saveGroupRolesStaffMasterDataLocal(
        dataList: List<GroupRolesStaffDTO>,
        callback: IDataSourceCallback<List<GroupRolesStaffDTO>>
    ) {
        mMasterLocalDataSource?.saveGroupRolesStaffMasterDataLocal(dataList, callback)
    }

    override fun fetchGroupRolesStaffMasterDataLocal(callback: IDataSourceCallback<List<GroupRolesStaffDTO>>) {
        mMasterLocalDataSource?.fetchGroupRolesStaffMasterDataLocal(callback)
    }

    override fun fetchGroupRolesStaffMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<GroupRolesStaffDTO>>
    ) {

        mMasterRemoteDataSource?.fetchGroupRolesStaffMasterDataRemote(
            commonListingDTO,
            object : IDataSourceCallback<List<GroupRolesStaffDTO>> {

                override fun onDataFound(data: List<GroupRolesStaffDTO>) {
                    saveGroupRolesStaffMasterDataLocal(data, callback)
                }

                override fun onDataNotFound() {
                    callback.onDataNotFound()
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }

            })

    }

    override fun fetchGroupRolesStaffMasterDataById(
        id: Long,
        callback: IDataSourceCallback<GroupRolesStaffDTO>
    ) {
        mMasterLocalDataSource?.fetchGroupRolesStaffMasterDataById(id, callback)
    }
}