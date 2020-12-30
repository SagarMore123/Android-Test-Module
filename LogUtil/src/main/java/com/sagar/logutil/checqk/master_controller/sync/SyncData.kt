package com.sagar.logutil.checqk.master_controller.sync

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.sagar.logutil.checqk.master_controller.source.MasterRepository
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.model.discount.*
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.utils.Constants

class SyncData {

    companion object {
        @Volatile
        var masterRepository: MasterRepository? = null

        private val commonListingDTO = CommonListingDTO()

        /**
         * Performs the network request for updated weather, parses the JSON from that request, and
         * inserts the new weather information into our ContentProvider. Will notify the user that new
         * weather has been loaded if the user hasn't been notified of the weather within the last day
         * AND they haven't disabled notifications in the preferences screen.
         *
         * @param context Used to access utility methods and the ContentResolver
         */

        var signUpMasterServicesCounts = 11
        var signUpMasterServices = MutableLiveData<Int>(0)


        var loginMasterServices = 9
        var loginMasterServicesCounts = MutableLiveData<Int>(0)

        @Synchronized
        fun splashSyncData(context: Context?) {
//            context?.let { Constants.clearSharedPrefs(it) }
            masterRepository = MasterRepository.getInstance(context)

            /*--------  Login By Visitor section -----------*/
            try {

                masterRepository?.loginMasters(LoginDTO(), object : IDataSourceCallback<String> {

                    override fun onDataFound(data: String) {

                        /*--------  City section -----------*/
                        try {

                            commonListingDTO.defaultSort.sortField = Constants.CITY_SORT_FIELD
                            commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER

                            masterRepository?.fetchCityMasterDataRemote(commonListingDTO, object :
                                IDataSourceCallback<List<CityMasterDTO>> {

                                override fun onDataFound(data: List<CityMasterDTO>) {
                                    signUpMasterServices.value =
                                        signUpMasterServices.value?.plus(1)
                                }

                                override fun onError(error: String) {
                                    signUpMasterServices.value =
                                        signUpMasterServices.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                }

                                override fun onDataNotFound() {
                                    signUpMasterServices.value =
                                        signUpMasterServices.value?.plus(1)
                                }
                            })

                        } catch (e: Exception) {
                            signUpMasterServices.value =
                                signUpMasterServices.value?.plus(1)
                        }

                        /*--------  SVM section -----------*/
                        try {

                            masterRepository?.fetchSystemValueMasterDataRemote(object :
                                IDataSourceCallback<List<SystemValueMasterDTO>> {

                                override fun onDataFound(data: List<SystemValueMasterDTO>) {
                                    signUpMasterServices.value =
                                        signUpMasterServices.value?.plus(1)
                                }

                                override fun onDataNotFound() {
                                    signUpMasterServices.value =
                                        signUpMasterServices.value?.plus(1)
                                }

                                override fun onError(error: String) {
                                    signUpMasterServices.value =
                                        signUpMasterServices.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                }

                            })

                        } catch (e: Exception) {
                            signUpMasterServices.value =
                                signUpMasterServices.value?.plus(1)
                        }

                        /*--------  Meal Type section -----------*/
                        try {

                            commonListingDTO.defaultSort.sortField = Constants.MEAL_TYPE_SORT_FIELD
                            commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER

                            masterRepository?.fetchMealTypeMasterDataRemote(
                                commonListingDTO,
                                object :
                                    IDataSourceCallback<List<MealTypeMasterDTO>> {

                                    override fun onDataFound(data: List<MealTypeMasterDTO>) {
                                        signUpMasterServices.value =
                                            signUpMasterServices.value?.plus(1)
                                    }

                                    override fun onDataNotFound() {
                                        signUpMasterServices.value =
                                            signUpMasterServices.value?.plus(1)
                                    }

                                    override fun onError(error: String) {
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                        signUpMasterServices.value =
                                            signUpMasterServices.value?.plus(1)
                                    }

                                })

                        } catch (e: Exception) {
                            signUpMasterServices.value =
                                signUpMasterServices.value?.plus(1)
                        }


                        /*--------  Outlet Type section -----------*/
                        try {


                            commonListingDTO.defaultSort.sortField =
                                Constants.OUTLET_TYPE_SORT_FIELD
                            commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER

                            masterRepository?.fetchOutletTypeDataRemote(commonListingDTO, object :
                                IDataSourceCallback<List<OutletTypeMasterDTO>> {

                                override fun onDataFound(data: List<OutletTypeMasterDTO>) {
                                    signUpMasterServices.value =
                                        signUpMasterServices.value?.plus(1)
                                }

                                override fun onDataNotFound() {
                                    signUpMasterServices.value =
                                        signUpMasterServices.value?.plus(1)
                                }

                                override fun onError(error: String) {
                                    signUpMasterServices.value =
                                        signUpMasterServices.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                }

                            })

                        } catch (e: Exception) {
                            signUpMasterServices.value =
                                signUpMasterServices.value?.plus(1)
                        }


                        /*--------  Cuisine section -----------*/
                        try {


                            commonListingDTO.defaultSort.sortField = Constants.CUISINE_SORT_FIELD
                            commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER

                            masterRepository?.fetchCuisineMasterDataRemote(
                                commonListingDTO,
                                object :
                                    IDataSourceCallback<List<CuisineMasterDTO>> {

                                    override fun onDataFound(data: List<CuisineMasterDTO>) {
                                        signUpMasterServices.value =
                                            signUpMasterServices.value?.plus(1)
                                    }

                                    override fun onDataNotFound() {
                                        signUpMasterServices.value =
                                            signUpMasterServices.value?.plus(1)
                                    }

                                    override fun onError(error: String) {
                                        signUpMasterServices.value =
                                            signUpMasterServices.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                    }

                                })

                        } catch (e: Exception) {
                            signUpMasterServices.value =
                                signUpMasterServices.value?.plus(1)
                        }

                        /*--------  Food Type section -----------*/
                        try {


                            commonListingDTO.defaultSort.sortField = Constants.FOOD_TYPE_SORT_FIELD
                            commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER

                            masterRepository?.fetchFoodTypeMasterDataRemote(
                                commonListingDTO,
                                object :
                                    IDataSourceCallback<List<FoodTypeMasterDTO>> {

                                    override fun onDataFound(data: List<FoodTypeMasterDTO>) {
                                        signUpMasterServices.value =
                                            signUpMasterServices.value?.plus(1)
                                    }

                                    override fun onError(error: String) {
                                        signUpMasterServices.value =
                                            signUpMasterServices.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onDataNotFound() {
                                        signUpMasterServices.value =
                                            signUpMasterServices.value?.plus(1)
                                    }
                                })

                        } catch (e: Exception) {
                            signUpMasterServices.value =
                                signUpMasterServices.value?.plus(1)
                        }


                        /*--------  Facility section -----------*/
                        try {

                            commonListingDTO.defaultSort.sortField = Constants.FACILITY_SORT_FIELD
                            commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER

                            masterRepository?.fetchFacilityMastersRemote(commonListingDTO, object :
                                IDataSourceCallback<List<FacilityMasterDTO>> {

                                override fun onDataFound(data: List<FacilityMasterDTO>) {
                                    signUpMasterServices.value =
                                        signUpMasterServices.value?.plus(1)
                                }

                                override fun onError(error: String) {
                                    signUpMasterServices.value =
                                        signUpMasterServices.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                }

                                override fun onDataNotFound() {
                                    signUpMasterServices.value =
                                        signUpMasterServices.value?.plus(1)
                                }
                            })

                        } catch (e: Exception) {
                            signUpMasterServices.value =
                                signUpMasterServices.value?.plus(1)
                        }

                        /*--------  Country section -----------*/
                        try {

                            commonListingDTO.defaultSort.sortField = Constants.COUNTRY_SORT_FIELD
                            commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER

                            masterRepository?.fetchCountryMasterDataRemote(
                                commonListingDTO,
                                object :
                                    IDataSourceCallback<List<CountryMasterDTO>> {

                                    override fun onDataFound(data: List<CountryMasterDTO>) {
                                        signUpMasterServices.value =
                                            signUpMasterServices.value?.plus(1)
                                    }

                                    override fun onError(error: String) {
                                        signUpMasterServices.value =
                                            signUpMasterServices.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onDataNotFound() {
                                        signUpMasterServices.value =
                                            signUpMasterServices.value?.plus(1)
                                    }
                                })

                        } catch (e: Exception) {
                            signUpMasterServices.value =
                                signUpMasterServices.value?.plus(1)
                        }


                        /*--------  State section -----------*/
                        try {

                            commonListingDTO.defaultSort.sortField = Constants.STATE_SORT_FIELD
                            commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER

                            masterRepository?.fetchStateMasterDataRemote(commonListingDTO, object :
                                IDataSourceCallback<List<StateMasterDTO>> {

                                override fun onDataFound(data: List<StateMasterDTO>) {
                                    signUpMasterServices.value =
                                        signUpMasterServices.value?.plus(1)
                                }

                                override fun onError(error: String) {
                                    signUpMasterServices.value =
                                        signUpMasterServices.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                }

                                override fun onDataNotFound() {
                                    signUpMasterServices.value =
                                        signUpMasterServices.value?.plus(1)
                                }
                            })

                        } catch (e: Exception) {
                            signUpMasterServices.value =
                                signUpMasterServices.value?.plus(1)
                        }


                        /*--------  Area section -----------*/
                        try {

                            commonListingDTO.defaultSort.sortField = Constants.AREA_SORT_FIELD
                            commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER

                            masterRepository?.fetchAreaMasterDataRemote(commonListingDTO, object :
                                IDataSourceCallback<List<AreaMasterDTO>> {

                                override fun onDataFound(data: List<AreaMasterDTO>) {
                                    signUpMasterServices.value =
                                        signUpMasterServices.value?.plus(1)
                                }

                                override fun onError(error: String) {
                                    signUpMasterServices.value =
                                        signUpMasterServices.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                }

                                override fun onDataNotFound() {
                                    signUpMasterServices.value =
                                        signUpMasterServices.value?.plus(1)
                                }
                            })

                        } catch (e: Exception) {
                            signUpMasterServices.value =
                                signUpMasterServices.value?.plus(1)
                        }

                        /*--------  Social Media section -----------*/
                        try {

                            commonListingDTO.defaultSort.sortField =
                                Constants.SOCIAL_MEDIA_SORT_FIELD
                            commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER

                            masterRepository?.fetchSocialMediaMastersRemote(
                                commonListingDTO,
                                object :
                                    IDataSourceCallback<List<SocialMediaMasterDTO>> {

                                    override fun onDataFound(data: List<SocialMediaMasterDTO>) {
                                        signUpMasterServices.value =
                                            signUpMasterServices.value?.plus(1)
                                    }

                                    override fun onError(error: String) {
                                        signUpMasterServices.value =
                                            signUpMasterServices.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onDataNotFound() {
                                        signUpMasterServices.value =
                                            signUpMasterServices.value?.plus(1)
                                    }
                                })

                        } catch (e: Exception) {
                            signUpMasterServices.value =
                                signUpMasterServices.value?.plus(1)
                        }

                    }

                    override fun onDataNotFound() {
                        signUpMasterServices.value = -1
                    }

                    override fun onError(error: String) {
                        signUpMasterServices.value = -1
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }

                })

            } catch (e: Exception) {
                signUpMasterServices.value = -1
            }


        }


        @Synchronized
        fun syncData(context: Context?) {
            masterRepository = MasterRepository.getInstance(context)

            /*--------  Drawer section -----------*/
            try {
                masterRepository?.fetchDashboardDrawerMasterDataRemote(object :
                    IDataSourceCallback<List<DashboardDrawerDTO>> {

                    override fun onDataFound(data: List<DashboardDrawerDTO>) {
                        loginMasterServicesCounts.value = loginMasterServicesCounts.value?.plus(1)
                    }

                    override fun onDataNotFound() {
                        loginMasterServicesCounts.value = loginMasterServicesCounts.value?.plus(1)
                    }

                    override fun onError(error: String) {
                        loginMasterServicesCounts.value = loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }

                })

            } catch (e: Exception) {
                loginMasterServicesCounts.value =
                    loginMasterServicesCounts.value?.plus(1)
            }

            /*--------  Discount Category section -----------*/
            try {

                commonListingDTO.defaultSort.sortField =
                    Constants.DISCOUNT_CATEGORY_SORT_FIELD
                commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER

                masterRepository?.fetchDiscountCategoryMasterDataRemote(
                    commonListingDTO,
                    object :
                        IDataSourceCallback<List<OutletDiscountCategoryDTO>> {

                        override fun onDataFound(data: List<OutletDiscountCategoryDTO>) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }

                        override fun onError(error: String) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }

                        override fun onDataNotFound() {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }
                    })

            } catch (e: Exception) {
                loginMasterServicesCounts.value =
                    loginMasterServicesCounts.value?.plus(1)
            }

            /*--------  Discount Membership Plan section -----------*/
            try {

                commonListingDTO.defaultSort.sortField =
                    Constants.DISCOUNT_MEMBERSHIP_PLAN_SORT_FIELD
                commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER

                masterRepository?.fetchDiscountMembershipPlanMasterDataRemote(
                    commonListingDTO,
                    object :
                        IDataSourceCallback<List<OutletDiscountMembershipPlanDTO>> {

                        override fun onDataFound(data: List<OutletDiscountMembershipPlanDTO>) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }

                        override fun onError(error: String) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }

                        override fun onDataNotFound() {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }
                    })

            } catch (e: Exception) {
                loginMasterServicesCounts.value =
                    loginMasterServicesCounts.value?.plus(1)
            }

            /*-------- Cravx Cards Discount Membership Holder section -----------*/
            try {

                commonListingDTO.defaultSort.sortField =
                    Constants.CRAVX_CARDS_DISCOUNT_MEMBERSHIP_HOLDER_SORT_FIELD
                commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER

                masterRepository?.fetchCravxCardDiscountMembershipHolderMasterDataRemote(
                    commonListingDTO,
                    object :
                        IDataSourceCallback<List<CravxCardsMembershipHolderDTO>> {

                        override fun onDataFound(data: List<CravxCardsMembershipHolderDTO>) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }

                        override fun onError(error: String) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }

                        override fun onDataNotFound() {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }
                    })

            } catch (e: Exception) {
                loginMasterServicesCounts.value =
                    loginMasterServicesCounts.value?.plus(1)

            }

            /*-------- HW Discount Membership Holder section -----------*/
            try {

                commonListingDTO.defaultSort.sortField =
                    Constants.HW_DISCOUNT_MEMBERSHIP_HOLDER_SORT_FIELD
                commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER

                masterRepository?.fetchHWDiscountMembershipHolderMasterDataRemote(
                    commonListingDTO,
                    object :
                        IDataSourceCallback<List<HWMembershipHolderDTO>> {

                        override fun onDataFound(data: List<HWMembershipHolderDTO>) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }

                        override fun onError(error: String) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }

                        override fun onDataNotFound() {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }
                    })

            } catch (e: Exception) {
                loginMasterServicesCounts.value =
                    loginMasterServicesCounts.value?.plus(1)
            }

            /*-------- In-House Discount Membership Holder section -----------*/
            try {

                commonListingDTO.defaultSort.sortField =
                    Constants.IN_HOUSE_DISCOUNT_MEMBERSHIP_HOLDER_SORT_FIELD
                commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER

                masterRepository?.fetchInHouseDiscountMembershipHolderMasterDataRemote(
                    commonListingDTO,
                    object :
                        IDataSourceCallback<List<InHouseMembershipHolderDTO>> {

                        override fun onDataFound(data: List<InHouseMembershipHolderDTO>) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }

                        override fun onError(error: String) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }

                        override fun onDataNotFound() {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }
                    })

            } catch (e: Exception) {
                loginMasterServicesCounts.value =
                    loginMasterServicesCounts.value?.plus(1)
            }


            /*--------  Group Roles section -----------*/
            try {

                commonListingDTO.defaultSort.sortField = Constants.GROUP_ROLE_SORT_FIELD
                commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER
                val searchList = ArrayList<CommonSearchDTO>()
                val commonSearchDTO = CommonSearchDTO()
                commonSearchDTO.search = context?.let { Constants.getOutletId(it) }!!
                commonSearchDTO.searchCol = Constants.SUB_ADMIN_GROUP_ROLE_COLUMN
                searchList.add(commonSearchDTO)
                commonListingDTO.search = searchList

                masterRepository?.fetchGroupRolesMasterDataRemote(
                    commonListingDTO,
                    object :
                        IDataSourceCallback<List<GroupRolesDTO>> {

                        override fun onDataFound(data: List<GroupRolesDTO>) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }

                        override fun onDataNotFound() {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }

                        override fun onError(error: String) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }

                    })

            } catch (e: Exception) {
                loginMasterServicesCounts.value =
                    loginMasterServicesCounts.value?.plus(1)
            }

            /*--------  Known Languages section -----------*/
            try {

                commonListingDTO.defaultSort.sortField = Constants.KNOWN_LANGUAGE_SORT_FIELD
                commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER
                /*val searchList = ArrayList<CommonSearchDTO>()
                val commonSearchDTO = CommonSearchDTO()
                commonSearchDTO.search = Constants.SUB_ADMIN_GROUP_ROLE_SEARCH
                commonSearchDTO.searchCol = Constants.SUB_ADMIN_GROUP_ROLE_COLUMN
                searchList.add(commonSearchDTO)*/

                masterRepository?.fetchKnownLanguagesMasterDataRemote(commonListingDTO,
                    object : IDataSourceCallback<List<KnownLanguagesDTO>> {

                        override fun onDataFound(data: List<KnownLanguagesDTO>) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }

                        override fun onDataNotFound() {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }

                        override fun onError(error: String) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }

                    })

            } catch (e: Exception) {
                loginMasterServicesCounts.value =
                    loginMasterServicesCounts.value?.plus(1)
            }

            /*--------  Designation section -----------*/
            try {

                commonListingDTO.defaultSort.sortField = Constants.DESIGNATION_SORT_FIELD
                commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER
                /*val searchList = ArrayList<CommonSearchDTO>()
                val commonSearchDTO = CommonSearchDTO()
                commonSearchDTO.search = Constants.SUB_ADMIN_GROUP_ROLE_SEARCH
                commonSearchDTO.searchCol = Constants.SUB_ADMIN_GROUP_ROLE_COLUMN
                searchList.add(commonSearchDTO)*/

                masterRepository?.fetchDesignationMasterDataRemote(commonListingDTO,
                    object : IDataSourceCallback<List<DesignationDTO>> {

                        override fun onDataFound(data: List<DesignationDTO>) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }

                        override fun onDataNotFound() {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }

                        override fun onError(error: String) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }

                    })

            } catch (e: Exception) {
                loginMasterServicesCounts.value =
                    loginMasterServicesCounts.value?.plus(1)
            }

            /*--------  Group Roles Staff section -----------*/
            try {

                commonListingDTO.defaultSort.sortField = Constants.GROUP_ROLE_SORT_FIELD
                commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER
                val searchList = ArrayList<CommonSearchDTO>()
                val commonSearchDTO = CommonSearchDTO()
                commonSearchDTO.search = context?.let { Constants.getOutletId(it) }.toString()
                commonSearchDTO.searchCol = Constants.SUB_ADMIN_GROUP_ROLE_COLUMN
                searchList.add(commonSearchDTO)
                commonListingDTO.search = searchList

                masterRepository?.fetchGroupRolesStaffMasterDataRemote(
                    commonListingDTO,
                    object :
                        IDataSourceCallback<List<GroupRolesStaffDTO>> {

                        override fun onDataFound(data: List<GroupRolesStaffDTO>) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }

                        override fun onDataNotFound() {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }

                        override fun onError(error: String) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }

                    })

            } catch (e: Exception) {
                loginMasterServicesCounts.value =
                    loginMasterServicesCounts.value?.plus(1)
            }
        }
    }
}