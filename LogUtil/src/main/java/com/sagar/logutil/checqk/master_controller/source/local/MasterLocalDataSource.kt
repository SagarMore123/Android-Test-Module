package com.sagar.logutil.checqk.master_controller.source.local

import androidx.annotation.NonNull
import com.sagar.logutil.checqk.master_controller.source.MasterDataSource
import com.sagar.logutil.checqk.master_controller.source.daos.*
import com.sagar.logutil.checqk.master_controller.source.daos.address.*
import com.sagar.logutil.checqk.master_controller.source.daos.discount.*
import com.sagar.logutil.checqk.master_controller.sync.AppExecutors
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.model.discount.*
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback

class MasterLocalDataSource(
    mAppExecutors: AppExecutors,
    systemValueMasterDao: SystemValueMasterDao,
    mealTypeMasterDao: MealTypeMasterDao,
    outletTypeMasterDao: OutletTypeMasterDao,
    facilityMasterDao: FacilityMasterDao,
    cuisineMasterDao: CuisineMasterDao,
    foodTypeMasterDao: FoodTypeMasterDao,
    socialMediaMasterDao: SocialMediaMasterDao,
    knownLanguagesDao: KnownLanguagesDao,
    designationDao: DesignationDao,
    groupRolesStaffDao: GroupRoleStaffDao,

    // Address DAOs
    countryMasterDao: CountryMasterDao,
    stateMasterDao: StateMasterDao,
    cityMasterDao: CityMasterDao,
    areaMasterDao: AreaMasterDao,
    pincodeMasterDao: PincodeMasterDao,

    // Discount DAOs
    discountCategoryMasterDao: DiscountCategoryMasterDao,
    discountMembershipPlanDao: DiscountMembershipPlanDao,
    cravxCardMembershipHolderDao: CravxCardsMembershipHolderDao,
    hwMembershipHolderDao: HWMembershipHolderDao,
    inHouseMembershipHolderDao: InHouseMembershipHolderDao,

    //Group Role DAOs
    groupRoleDao: GroupRoleDao,
    dashboardDrawerDao: DashboardDrawerDao

) : MasterDataSource() {

    companion object {

        @Volatile
        private var INSTANCE: MasterLocalDataSource? = null

        @JvmStatic
        fun getInstance(
            appExecutors: AppExecutors,
            systemValueMasterDao: SystemValueMasterDao,
            mealTypeMasterDao: MealTypeMasterDao,
            outletTypeMasterDao: OutletTypeMasterDao,
            facilityMasterDao: FacilityMasterDao,
            cuisineMasterDao: CuisineMasterDao,
            foodTypeMasterDao: FoodTypeMasterDao,
            socialMediaMasterDao: SocialMediaMasterDao,
            knownLanguagesDao: KnownLanguagesDao,
            designationDao: DesignationDao,
            groupRolesStaffDao: GroupRoleStaffDao,

            // Address DAOs
            countryMasterDao: CountryMasterDao,
            stateMasterDao: StateMasterDao,
            cityMasterDao: CityMasterDao,
            areaMasterDao: AreaMasterDao,
            pincodeMasterDao: PincodeMasterDao,

            // Discount DAOs
            discountCategoryMasterDao: DiscountCategoryMasterDao,
            discountMembershipPlanDao: DiscountMembershipPlanDao,
            cravxCardMembershipHolderDao: CravxCardsMembershipHolderDao,
            hwMembershipHolderDao: HWMembershipHolderDao,
            inHouseMembershipHolderDao: InHouseMembershipHolderDao,

            //Group Roles DAOs
            groupRoleDao: GroupRoleDao,
            dashboardDrawerDao: DashboardDrawerDao

        ): MasterLocalDataSource? {
            if (INSTANCE == null) {
                synchronized(
                    MasterLocalDataSource::class.java
                ) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            MasterLocalDataSource(
                                appExecutors,
                                systemValueMasterDao,
                                mealTypeMasterDao,
                                outletTypeMasterDao,
                                facilityMasterDao,
                                cuisineMasterDao,
                                foodTypeMasterDao,
                                socialMediaMasterDao,
                                knownLanguagesDao,
                                designationDao,
                                groupRolesStaffDao,
                                countryMasterDao,
                                stateMasterDao,
                                cityMasterDao,
                                areaMasterDao,
                                pincodeMasterDao,
                                discountCategoryMasterDao,
                                discountMembershipPlanDao,
                                cravxCardMembershipHolderDao,
                                hwMembershipHolderDao,
                                inHouseMembershipHolderDao,
                                groupRoleDao,
                                dashboardDrawerDao
                            )
                    }
                }
            }
            return INSTANCE
        }


    }

    private var mAppExecutors: AppExecutors? = mAppExecutors

    private var systemValueMasterDao: SystemValueMasterDao? = systemValueMasterDao
    private var mealTypeMasterDao: MealTypeMasterDao? = mealTypeMasterDao
    private var outletTypeMasterDao: OutletTypeMasterDao? = outletTypeMasterDao
    private var facilityMasterDao: FacilityMasterDao? = facilityMasterDao
    private var cuisineMasterDao: CuisineMasterDao? = cuisineMasterDao
    private var foodTypeMasterDao: FoodTypeMasterDao? = foodTypeMasterDao
    private var socialMediaMasterDao: SocialMediaMasterDao? = socialMediaMasterDao
    private var knownLanguagesDao: KnownLanguagesDao? = knownLanguagesDao
    private var designationDao: DesignationDao? = designationDao
    private var groupRolesStaffDao: GroupRoleStaffDao? = groupRolesStaffDao

    // Address Dao
    private var countryMasterDao: CountryMasterDao? = countryMasterDao
    private var stateMasterDao: StateMasterDao? = stateMasterDao
    private var cityMasterDao: CityMasterDao? = cityMasterDao
    private var areaMasterDao: AreaMasterDao? = areaMasterDao
    private var pincodeMasterDao: PincodeMasterDao? = pincodeMasterDao

    // Discount Dao
    private var discountCategoryMasterDao: DiscountCategoryMasterDao? = discountCategoryMasterDao
    private var discountMembershipPlanDao: DiscountMembershipPlanDao? = discountMembershipPlanDao
    private var cravxCardMembershipHolderDao: CravxCardsMembershipHolderDao? =
        cravxCardMembershipHolderDao
    private var hwMembershipHolderDao: HWMembershipHolderDao? = hwMembershipHolderDao
    private var inHouseMembershipHolderDao: InHouseMembershipHolderDao? = inHouseMembershipHolderDao

    private var groupRoleDao: GroupRoleDao? = groupRoleDao
    private var dashboardDrawerDao: DashboardDrawerDao? = dashboardDrawerDao

    // SVM
    override fun saveSystemValueMasterDataLocal(
        dataList: List<SystemValueMasterDTO>,
        callback: IDataSourceCallback<List<SystemValueMasterDTO>>
    ) {
        val completeRunnable = Runnable {
            try {
                systemValueMasterDao?.deleteAllSystemMasterData()
                systemValueMasterDao?.insertSystemMasterData(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    override fun fetchSystemValueMasterValueByIdLocal(
        systemValueId: Long,
        callback: IDataSourceCallback<SystemValueMasterDTO>
    ) {
        val completeRunnable = Runnable {
            try {
                val systemMastersValue: SystemValueMasterDTO? =
                    systemValueMasterDao?.getValueById(systemValueId)
                mAppExecutors?.mainThread()
                    ?.execute { systemMastersValue?.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    override fun fetchSystemMasterValueByNameLocal(
        systemValueName: String,
        callback: IDataSourceCallback<List<SystemValueMasterDTO>>
    ) {
        val completeRunnable = Runnable {
            try {

                mAppExecutors?.mainThread()?.execute {
                    callback.onDataFound(
//                        systemValueMasterDao?.getAllByName(systemValueName) ?: arrayListOf()
                        systemValueMasterDao?.getAllByCode(systemValueName) ?: arrayListOf()
                    )
                }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun saveMealTypeMasterDataLocal(
        dataList: List<MealTypeMasterDTO>,
        callback: IDataSourceCallback<List<MealTypeMasterDTO>>
    ) {

        val completeRunnable = Runnable {
            try {
//                categoryDao?.deleteAllSubcategory()
                mealTypeMasterDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)

    }


    override fun fetchMealTypeMasterDataLocal(callback: IDataSourceCallback<List<MealTypeMasterDTO>>) {

        val completeRunnable = Runnable {
            try {
                val categoryList: List<MealTypeMasterDTO>? = mealTypeMasterDao?.fetchAllData()
                mAppExecutors?.mainThread()
                    ?.execute { callback.onDataFound(categoryList ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchMealTypeMasterDataById(
        id: Long,
        callback: IDataSourceCallback<MealTypeMasterDTO>
    ) {

        val completeRunnable = Runnable {
            try {
                val dto: MealTypeMasterDTO? = mealTypeMasterDao?.fetchDetailsById(id)
                mAppExecutors?.mainThread()
                    ?.execute { dto?.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun saveOutletTypeDataLocal(
        dataList: List<OutletTypeMasterDTO>,
        callback: IDataSourceCallback<List<OutletTypeMasterDTO>>
    ) {

        val completeRunnable = Runnable {
            try {
//                categoryDao?.deleteAllSubcategory()
                outletTypeMasterDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)

    }


    override fun fetchOutletTypeDataLocal(callback: IDataSourceCallback<List<OutletTypeMasterDTO>>) {

        val completeRunnable = Runnable {
            try {
                val list: List<OutletTypeMasterDTO>? = outletTypeMasterDao?.fetchAllData()
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(list ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchOutletTypeMasterDataById(
        id: Long,
        callback: IDataSourceCallback<OutletTypeMasterDTO>
    ) {

        val completeRunnable = Runnable {
            try {
                val dto: OutletTypeMasterDTO? = outletTypeMasterDao?.fetchDetailsById(id)
                mAppExecutors?.mainThread()
                    ?.execute { dto?.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun saveCuisineMasterDataLocal(
        dataList: List<CuisineMasterDTO>,
        callback: IDataSourceCallback<List<CuisineMasterDTO>>
    ) {

        val completeRunnable = Runnable {
            try {
//                skillMasterDao?.deleteAllDetails()
                cuisineMasterDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)

    }


    override fun fetchCuisineMasterDataLocal(callback: IDataSourceCallback<List<CuisineMasterDTO>>) {

        val completeRunnable = Runnable {
            try {
                val list: List<CuisineMasterDTO>? = cuisineMasterDao?.fetchAllData()
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(list ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchCuisineMasterDataById(
        id: Long,
        callback: IDataSourceCallback<CuisineMasterDTO>
    ) {
        val completeRunnable = Runnable {
            try {
                val dto: CuisineMasterDTO? = cuisineMasterDao?.fetchListById(id)
                mAppExecutors?.mainThread()
                    ?.execute { dto?.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    // Food
    override fun saveFoodTypeMasterDataLocal(
        dataList: List<FoodTypeMasterDTO>,
        callback: IDataSourceCallback<List<FoodTypeMasterDTO>>
    ) {

        val completeRunnable = Runnable {
            try {
                foodTypeMasterDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)

    }


    override fun fetchFoodTypeMasterDataLocal(callback: IDataSourceCallback<List<FoodTypeMasterDTO>>) {

        val completeRunnable = Runnable {
            try {
                val list: List<FoodTypeMasterDTO>? = foodTypeMasterDao?.fetchAllData()
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(list ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchFoodTypeMasterDataById(
        id: Long,
        callback: IDataSourceCallback<FoodTypeMasterDTO>
    ) {
        val completeRunnable = Runnable {
            try {
                val dto: FoodTypeMasterDTO? = foodTypeMasterDao?.fetchDetailsById(id)
                mAppExecutors?.mainThread()
                    ?.execute { dto?.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun saveFacilityMasterDataLocal(
        @NonNull dataList: List<FacilityMasterDTO>,
        @NonNull callback: IDataSourceCallback<List<FacilityMasterDTO>>
    ) {
        val completeRunnable = Runnable {
            try {
                facilityMasterDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)

    }

    override fun fetchFacilityMastersLocal(callback: IDataSourceCallback<List<FacilityMasterDTO>>) {

        val completeRunnable = Runnable {
            try {
                val list: List<FacilityMasterDTO>? = facilityMasterDao?.getAllData()
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(list ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    override fun fetchFacilityMasterDTOByIdLocal(
        id: Long,
        callback: IDataSourceCallback<FacilityMasterDTO?>
    ) {

        val completeRunnable = Runnable {
            try {
                val data: FacilityMasterDTO? =
                    facilityMasterDao?.getDetailsById(id)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(data) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun saveSocialMediaMasterDataLocal(
        @NonNull dataList: List<SocialMediaMasterDTO>,
        @NonNull callback: IDataSourceCallback<List<SocialMediaMasterDTO>>
    ) {
        val completeRunnable = Runnable {
            try {
                socialMediaMasterDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)

    }

    override fun fetchSocialMediaMastersLocal(callback: IDataSourceCallback<List<SocialMediaMasterDTO>>) {

        val completeRunnable = Runnable {
            try {
                val list: List<SocialMediaMasterDTO>? = socialMediaMasterDao?.fetchAllData()
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(list ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    override fun fetchSocialMediaMasterDTOByIdLocal(
        id: Long,
        callback: IDataSourceCallback<SocialMediaMasterDTO>
    ) {

        val completeRunnable = Runnable {
            try {
                val data: SocialMediaMasterDTO? =
                    socialMediaMasterDao?.fetchListById(id)
                mAppExecutors?.mainThread()?.execute { data?.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    // Address

    // Country
    override fun saveCountryMasterDataLocal(
        @NonNull dataList: List<CountryMasterDTO>,
        @NonNull callback: IDataSourceCallback<List<CountryMasterDTO>>
    ) {
        val completeRunnable = Runnable {
            try {
                countryMasterDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)

    }


    override fun fetchCountryMasterDataLocal(callback: IDataSourceCallback<List<CountryMasterDTO>>) {

        val completeRunnable = Runnable {
            try {
                val list: List<CountryMasterDTO>? = countryMasterDao?.fetchAllData()
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(list ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchCountryMasterByIdLocal(
        id: Long,
        callback: IDataSourceCallback<String>
    ) {

        val completeRunnable = Runnable {
            try {
                val data: CountryMasterDTO? =
                    countryMasterDao?.fetchDetailsById(id)
                mAppExecutors?.mainThread()
                    ?.execute { callback.onDataFound(data?.countryName ?: "") }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    // State
    override fun saveStateMasterDataLocal(
        @NonNull dataList: List<StateMasterDTO>,
        @NonNull callback: IDataSourceCallback<List<StateMasterDTO>>
    ) {
        val completeRunnable = Runnable {
            try {
                stateMasterDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchStateMasterByCountryIdLocal(
        id: Long,
        callback: IDataSourceCallback<List<StateMasterDTO>>
    ) {

        val completeRunnable = Runnable {
            try {
                val data: List<StateMasterDTO>? = stateMasterDao?.fetchListByCountryId(id)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(data ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    // City
    override fun saveCityMasterDataLocal(
        @NonNull dataList: List<CityMasterDTO>,
        @NonNull callback: IDataSourceCallback<List<CityMasterDTO>>
    ) {
        val completeRunnable = Runnable {
            try {
                cityMasterDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchCityMasterByStateIdLocal(
        id: Long,
        callback: IDataSourceCallback<List<CityMasterDTO>>
    ) {

        val completeRunnable = Runnable {
            try {
                val data: List<CityMasterDTO>? = cityMasterDao?.fetchListByStateId(id)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(data ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    // Area
    override fun saveAreaMasterDataLocal(
        @NonNull dataList: List<AreaMasterDTO>,
        @NonNull callback: IDataSourceCallback<List<AreaMasterDTO>>
    ) {
        val completeRunnable = Runnable {
            try {
                areaMasterDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchAreaMasterByStateIdLocal(
        id: Long,
        callback: IDataSourceCallback<List<AreaMasterDTO>>
    ) {

        val completeRunnable = Runnable {
            try {
                val data: List<AreaMasterDTO>? = areaMasterDao?.fetchListByCityId(id)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(data ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    // Discount Category
    override fun saveDiscountCategoryMasterDataLocal(
        @NonNull dataList: List<OutletDiscountCategoryDTO>,
        @NonNull callback: IDataSourceCallback<List<OutletDiscountCategoryDTO>>
    ) {
        val completeRunnable = Runnable {
            try {
                discountCategoryMasterDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchDiscountCategoryMasterByIdLocal(
        id: Long,
        callback: IDataSourceCallback<String>
    ) {

        val completeRunnable = Runnable {
            try {
                val data: OutletDiscountCategoryDTO? =
                    discountCategoryMasterDao?.fetchDetailsById(id)
                mAppExecutors?.mainThread()
                    ?.execute { callback.onDataFound(data?.outletDiscountCategoryName ?: "") }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchDiscountCategoryMasterDataLocal(callback: IDataSourceCallback<List<OutletDiscountCategoryDTO>>) {

        val completeRunnable = Runnable {
            try {
                val list: List<OutletDiscountCategoryDTO>? =
                    discountCategoryMasterDao?.fetchAllData()
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(list ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    // Discount Membership Plan
    override fun saveDiscountMembershipPlanMasterDataLocal(
        @NonNull dataList: List<OutletDiscountMembershipPlanDTO>,
        @NonNull callback: IDataSourceCallback<List<OutletDiscountMembershipPlanDTO>>
    ) {
        val completeRunnable = Runnable {
            try {
                discountMembershipPlanDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchDiscountMembershipPlanMasterByIdLocal(
        id: Long,
        callback: IDataSourceCallback<OutletDiscountMembershipPlanDTO>
    ) {

        val completeRunnable = Runnable {
            try {
                val data: OutletDiscountMembershipPlanDTO? =
                    discountMembershipPlanDao?.fetchDetailsById(id)
                mAppExecutors?.mainThread()
                    ?.execute { data?.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchDiscountMembershipPlanMasterDataLocal(callback: IDataSourceCallback<List<OutletDiscountMembershipPlanDTO>>) {

        val completeRunnable = Runnable {
            try {
                val list: List<OutletDiscountMembershipPlanDTO>? =
                    discountMembershipPlanDao?.fetchAllData()
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(list ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    // Cravx Card Discount Membership Holder
    override fun saveCravxCardDiscountMembershipHolderMasterDataLocal(
        @NonNull dataList: List<CravxCardsMembershipHolderDTO>,
        @NonNull callback: IDataSourceCallback<List<CravxCardsMembershipHolderDTO>>
    ) {
        val completeRunnable = Runnable {
            try {
                cravxCardMembershipHolderDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchCravxCardDiscountMembershipHolderMasterByIdLocal(
        id: Long,
        callback: IDataSourceCallback<CravxCardsMembershipHolderDTO>
    ) {

        val completeRunnable = Runnable {
            try {
                val data: CravxCardsMembershipHolderDTO? =
                    cravxCardMembershipHolderDao?.fetchDetailsById(id)
                mAppExecutors?.mainThread()?.execute { data?.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchCravxCardDiscountMembershipHolderMasterDataLocal(callback: IDataSourceCallback<List<CravxCardsMembershipHolderDTO>>) {

        val completeRunnable = Runnable {
            try {
                val list: List<CravxCardsMembershipHolderDTO>? =
                    cravxCardMembershipHolderDao?.fetchAllData()
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(list ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    // HW Discount Membership Holder
    override fun saveHWDiscountMembershipHolderMasterDataLocal(
        @NonNull dataList: List<HWMembershipHolderDTO>,
        @NonNull callback: IDataSourceCallback<List<HWMembershipHolderDTO>>
    ) {
        val completeRunnable = Runnable {
            try {
                hwMembershipHolderDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchHWDiscountMembershipHolderMasterByIdLocal(
        id: Long,
        callback: IDataSourceCallback<HWMembershipHolderDTO>
    ) {

        val completeRunnable = Runnable {
            try {
                val data: HWMembershipHolderDTO? =
                    hwMembershipHolderDao?.fetchDetailsById(id)
                mAppExecutors?.mainThread()?.execute { data?.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchHWDiscountMembershipHolderMasterDataLocal(callback: IDataSourceCallback<List<HWMembershipHolderDTO>>) {

        val completeRunnable = Runnable {
            try {
                val list: List<HWMembershipHolderDTO>? =
                    hwMembershipHolderDao?.fetchAllData()
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(list ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    // In-House Discount Membership Holder
    override fun saveInHouseDiscountMembershipHolderMasterDataLocal(
        @NonNull dataList: List<InHouseMembershipHolderDTO>,
        @NonNull callback: IDataSourceCallback<List<InHouseMembershipHolderDTO>>
    ) {
        val completeRunnable = Runnable {
            try {
                inHouseMembershipHolderDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchInHouseDiscountMembershipHolderMasterByIdLocal(
        id: Long,
        callback: IDataSourceCallback<InHouseMembershipHolderDTO>
    ) {

        val completeRunnable = Runnable {
            try {
                val data: InHouseMembershipHolderDTO? =
                    inHouseMembershipHolderDao?.fetchDetailsById(id)
                mAppExecutors?.mainThread()?.execute { data?.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchInHouseDiscountMembershipHolderMasterDataLocal(callback: IDataSourceCallback<List<InHouseMembershipHolderDTO>>) {

        val completeRunnable = Runnable {
            try {
                val list: List<InHouseMembershipHolderDTO>? =
                    inHouseMembershipHolderDao?.fetchAllData()
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(list ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun saveGroupRolesMasterDataLocal(
        dataList: List<GroupRolesDTO>,
        callback: IDataSourceCallback<List<GroupRolesDTO>>
    ) {

        val completeRunnable = Runnable {
            try {
//                categoryDao?.deleteAllSubcategory()
                groupRoleDao?.deleteAllDetails()
                groupRoleDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)

    }


    override fun fetchGroupRolesMasterDataLocal(callback: IDataSourceCallback<List<GroupRolesDTO>>) {

        val completeRunnable = Runnable {
            try {
                val categoryList: List<GroupRolesDTO>? = groupRoleDao?.fetchAllData()
                mAppExecutors?.mainThread()
                    ?.execute { callback.onDataFound(categoryList ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchGroupRolesMasterDataById(
        id: Long,
        callback: IDataSourceCallback<GroupRolesDTO>
    ) {

        val completeRunnable = Runnable {
            try {
                val dto: GroupRolesDTO? = groupRoleDao?.fetchDetailsById(id)
                mAppExecutors?.mainThread()
                    ?.execute { dto?.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    override fun saveDashboardDrawerMasterDataLocal(
        dataList: List<DashboardDrawerDTO>,
        callback: IDataSourceCallback<List<DashboardDrawerDTO>>
    ) {
        val completeRunnable = Runnable {
            try {
//                categoryDao?.deleteAllSubcategory()
                dashboardDrawerDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    override fun fetchDashboardDrawerMasterDataLocal(callback: IDataSourceCallback<List<DashboardDrawerDTO>>) {
        val completeRunnable = Runnable {
            try {
                val categoryList: List<DashboardDrawerDTO>? = dashboardDrawerDao?.fetchAllData()
                mAppExecutors?.mainThread()
                    ?.execute { callback.onDataFound(categoryList ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    override fun fetchDashboardDrawerMasterDataById(
        id: Long,
        callback: IDataSourceCallback<DashboardDrawerDTO>
    ) {
        val completeRunnable = Runnable {
            try {
                val dto: DashboardDrawerDTO? = dashboardDrawerDao?.fetchDetailsById(id)
                mAppExecutors?.mainThread()
                    ?.execute { dto?.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    //Known Language
    override fun saveKnownLanguagesMasterDataLocal(
        dataList: List<KnownLanguagesDTO>,
        callback: IDataSourceCallback<List<KnownLanguagesDTO>>
    ) {
        val completeRunnable = Runnable {
            try {
//                categoryDao?.deleteAllSubcategory()
                knownLanguagesDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    override fun fetchKnownLanguagesMasterDataLocal(callback: IDataSourceCallback<List<KnownLanguagesDTO>>) {
        val completeRunnable = Runnable {
            try {
                val categoryList: List<KnownLanguagesDTO>? = knownLanguagesDao?.fetchAllData()
                mAppExecutors?.mainThread()
                    ?.execute { callback.onDataFound(categoryList ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    override fun fetchKnownLanguagesMasterDataById(
        id: Long,
        callback: IDataSourceCallback<KnownLanguagesDTO>
    ) {
        val completeRunnable = Runnable {
            try {
                val dto: KnownLanguagesDTO? = knownLanguagesDao?.fetchDetailsById(id)
                mAppExecutors?.mainThread()
                    ?.execute { dto?.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    //Designation
    override fun saveDesignationMasterDataLocal(
        dataList: List<DesignationDTO>,
        callback: IDataSourceCallback<List<DesignationDTO>>
    ) {
        val completeRunnable = Runnable {
            try {
//                categoryDao?.deleteAllSubcategory()
                designationDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    override fun fetchDesignationMasterDataLocal(callback: IDataSourceCallback<List<DesignationDTO>>) {
        val completeRunnable = Runnable {
            try {
                val categoryList: List<DesignationDTO>? = designationDao?.fetchAllData()
                mAppExecutors?.mainThread()
                    ?.execute { callback.onDataFound(categoryList ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }

    override fun fetchDesignationMasterDataById(
        id: Long,
        callback: IDataSourceCallback<DesignationDTO>
    ) {
        val completeRunnable = Runnable {
            try {
                val dto: DesignationDTO? = designationDao?.fetchDetailsById(id)
                mAppExecutors?.mainThread()
                    ?.execute { dto?.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun saveGroupRolesStaffMasterDataLocal(
        dataList: List<GroupRolesStaffDTO>,
        callback: IDataSourceCallback<List<GroupRolesStaffDTO>>
    ) {

        val completeRunnable = Runnable {
            try {
//                categoryDao?.deleteAllSubcategory()
                groupRolesStaffDao?.deleteAllDetails()
                groupRolesStaffDao?.insert(dataList)
                mAppExecutors?.mainThread()?.execute { callback.onDataFound(dataList) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)

    }


    override fun fetchGroupRolesStaffMasterDataLocal(callback: IDataSourceCallback<List<GroupRolesStaffDTO>>) {

        val completeRunnable = Runnable {
            try {
                val categoryList: List<GroupRolesStaffDTO>? = groupRolesStaffDao?.fetchAllData()
                mAppExecutors?.mainThread()
                    ?.execute { callback.onDataFound(categoryList ?: arrayListOf()) }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }


    override fun fetchGroupRolesStaffMasterDataById(
        id: Long,
        callback: IDataSourceCallback<GroupRolesStaffDTO>
    ) {

        val completeRunnable = Runnable {
            try {
                val dto: GroupRolesStaffDTO? = groupRolesStaffDao?.fetchDetailsById(id)
                mAppExecutors?.mainThread()
                    ?.execute { dto?.let { callback.onDataFound(it) } }
            } catch (ex: Exception) {
                mAppExecutors?.mainThread()?.execute { callback.onError(ex.localizedMessage ?: "") }
            }
        }
        mAppExecutors?.diskIO()?.execute(completeRunnable)
    }
}
