package com.sagar.logutil.checqk.view.discount.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.sagar.logutil.checqk.master_controller.source.MasterRepository
import com.sagar.logutil.checqk.master_controller.source.daos.SystemValueMasterDao
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.model.discount.CorporateMembershipOneDashboardDTO
import com.sagar.logutil.checqk.model.discount.DiscountCategoryDTO
import com.sagar.logutil.checqk.model.discount.OutletDiscountDetailsDTO
import com.sagar.logutil.checqk.model.discount.OutletDiscountMembershipPlanDTO
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.GenericBaseObservable
import com.sagar.logutil.checqk.view.login.remote.UserRepository

class DiscountViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var masterRepository: MasterRepository,
    private var userRepository: UserRepository
) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>()
    private var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)
    private var outLetId = 0L
    private var productId = 0L
    var discountPlansLayoutVisible = MutableLiveData<Boolean>(true)

    // Discount Category
    private var discountCategoryArrayList = ArrayList<DiscountCategoryDTO>()
    var discountCategoryListMutableLiveData = MutableLiveData<List<DiscountCategoryDTO>>()

    // Discount Membership plan -> Basic Plan, Basic Corporate Plan,....etc
    private var discountMembershipPlanArrayList = ArrayList<OutletDiscountMembershipPlanDTO>()
    var discountMembershipPlanListMutableLiveData =
        MutableLiveData<List<OutletDiscountMembershipPlanDTO>>()

    // Discount Membership Holder -> INDIVIDUALS, CORPORATES
    private var discountMembershipHolderArrayList = ArrayList<SystemValueMasterDTO>()
    var discountMembershipHolderListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()

    // Discount Membership Type -> Regular, VIP
    private var discountMembershipTypeArrayList = ArrayList<SystemValueMasterDTO>()
    var discountMembershipTypeListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()

    // Week Days
    var daysArrayList = ArrayList<DayDTO>()
    var daysListMutableLiveData = MutableLiveData<List<DayDTO>>()

    // Discount Days
    var discountDayTimingsArrayList = ArrayList<DayDTO>()
    var discountDayTimingsListMutableLiveData = MutableLiveData<List<DayDTO>>()


    var membershipDiscountArrayList = ArrayList<OutletDiscountDetailsDTO>()
    var cravxCardDiscountArrayList = ArrayList<OutletDiscountDetailsDTO>()
    var hwCardDiscountArrayList = ArrayList<OutletDiscountDetailsDTO>()
    var inHouseDiscountArrayList = ArrayList<OutletDiscountDetailsDTO>()

    var oneDashboardDiscountArrayList = ArrayList<OutletDiscountDetailsDTO>()
    var oneDashboardDiscountMastersArrayList = ArrayList<CorporateMembershipOneDashboardDTO>()


    var observableBoolean = MutableLiveData<Boolean>(false)

    init {
        outLetId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L
        productId =
            Constants.decrypt(sharedPreferences.getString(Constants.PRODUCT_ID, ""))?.toLong() ?: 0L
        initiateDays()
        populateMasters()


        populateOutletDiscountDetailsList()
    }

    private fun populateOutletDiscountDetailsList() {

        userRepository.fetchOutletDiscountDetailsList(outLetId,
            object : IDataSourceCallback<ArrayList<OutletDiscountDetailsDTO>> {

                override fun onDataFound(data: ArrayList<OutletDiscountDetailsDTO>) {

                    for (item in data) {

                        when (item.outletDiscountCategory) {

                            DiscountEnum.MEMBERSHIP_DISCOUNTS.name -> {
                                membershipDiscountArrayList.add(item)
                            }

                            DiscountEnum.CRAVX_CARD.name -> {
                                cravxCardDiscountArrayList.add(item)
                            }

                            DiscountEnum.HW_CARD.name -> {
                                hwCardDiscountArrayList.add(item)
                            }

                            DiscountEnum.ONE_DASHBOARD.name -> {
                                oneDashboardDiscountArrayList.add(item)
                            }

                            DiscountEnum.INHOUSE_CARD.name -> {
                                inHouseDiscountArrayList.add(item)
                            }
                        }
                    }

                    observableBoolean.value = true

                }

                override fun onDataNotFound() {

                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                }
            })

        userRepository.fetchOutletOneDashboardMasterDetails(productId, outLetId,
            object : IDataSourceCallback<ArrayList<CorporateMembershipOneDashboardDTO>> {

                override fun onDataFound(data: ArrayList<CorporateMembershipOneDashboardDTO>) {

                    oneDashboardDiscountMastersArrayList = data

                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                }
            })
    }

    private fun populateMasters() {

        // Discount Category
        for (item in DiscountEnum.values()) {

            val discountCategoryDTO = DiscountCategoryDTO()
            discountCategoryDTO.outletDiscountCategoryId = item.id
            discountCategoryDTO.outletDiscountCategoryName = item.value
            discountCategoryArrayList.add(discountCategoryDTO)
        }
        if (!discountCategoryArrayList.isNullOrEmpty()) {
            discountCategoryArrayList[0].selected = true
        }
        discountCategoryListMutableLiveData.value = discountCategoryArrayList

/*
        // Discount Category
        masterRepository.fetchDiscountCategoryMasterDataLocal(object :
            IDataSourceCallback<List<OutletDiscountCategoryDTO>> {

            override fun onDataFound(data: List<OutletDiscountCategoryDTO>) {
                discountCategoryArrayList = data as ArrayList<OutletDiscountCategoryDTO>
                discountCategoryArrayList[0].selected=true
                discountCategoryListMutableLiveData.value = discountCategoryArrayList
            }

            override fun onError(error: String) {
                getmSnackbar().value = error
            }
        })
*/

        // Discount Membership plan -> Basic Plan, Basic Corporate Plan,....etc
        masterRepository.fetchDiscountMembershipPlanMasterDataLocal(object :
            IDataSourceCallback<List<OutletDiscountMembershipPlanDTO>> {

            override fun onDataFound(data: List<OutletDiscountMembershipPlanDTO>) {
                discountMembershipPlanArrayList = data as ArrayList<OutletDiscountMembershipPlanDTO>
                if (!discountMembershipPlanArrayList.isNullOrEmpty()) {
                    discountMembershipPlanArrayList[0].selected = true
                }
                discountMembershipPlanListMutableLiveData.value = discountMembershipPlanArrayList
            }

            override fun onError(error: String) {
                getmSnackbar().value = error
            }
        })

        // Discount Membership Holder -> INDIVIDUALS, CORPORATES
        masterRepository.fetchSystemMasterValueByNameLocal(
            SystemValueMasterDao.MEMBERSHIP_HOLDER,
            object : IDataSourceCallback<List<SystemValueMasterDTO>> {

                override fun onDataFound(data: List<SystemValueMasterDTO>) {
                    discountMembershipHolderArrayList = data as ArrayList<SystemValueMasterDTO>
                    if (!discountMembershipHolderArrayList.isNullOrEmpty()) {
                        discountMembershipHolderArrayList[0].selected = true
                    }
                    discountMembershipHolderListMutableLiveData.value =
                        discountMembershipHolderArrayList
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                }
            })

        // Discount Membership Type -> Regular, VIP
        masterRepository.fetchSystemMasterValueByNameLocal(
            SystemValueMasterDao.MEMBERSHIP_TYPE,
            object : IDataSourceCallback<List<SystemValueMasterDTO>> {

                override fun onDataFound(data: List<SystemValueMasterDTO>) {
                    discountMembershipTypeArrayList = data as ArrayList<SystemValueMasterDTO>
                    if (!discountMembershipTypeArrayList.isNullOrEmpty()) {
                        discountMembershipTypeArrayList[0].selected = true
                    }
                    discountMembershipTypeListMutableLiveData.value =
                        discountMembershipTypeArrayList
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                }
            })
    }

    private fun initiateDays() {

        daysArrayList.clear()
        daysListMutableLiveData.value = arrayListOf()
        for (item in DaysEnum.values()) {

            val dayDTO = DayDTO()
            dayDTO.dayId = item.id.toLong()
            dayDTO.dayName = item.value
            daysArrayList.add(dayDTO)

            // discount
            dayDTO.allDaySameDiscountFlag = true

            val discountDaysTimingDTO = DiscountDaysTimingDTO()
            discountDaysTimingDTO.allDaySameDiscountFlag = true
            dayDTO.discountDayAndTimings.add(discountDaysTimingDTO)
            discountDayTimingsArrayList.add(dayDTO)
        }

        daysListMutableLiveData.value = daysArrayList
        discountDayTimingsListMutableLiveData.value = discountDayTimingsArrayList
    }

    fun onDayItemClick(position: Int, dayDTO: DayDTO) {
        daysArrayList[position].dayIsCheckedOrClosed = !daysArrayList[position].dayIsCheckedOrClosed
        daysListMutableLiveData.value = daysArrayList

        discountDayTimingsArrayList[position].allDaySameDiscountFlag = dayDTO.dayIsCheckedOrClosed
        discountDayTimingsListMutableLiveData.value = discountDayTimingsArrayList

    }

    fun categorySelection(position: Int, discountCategoryDTO: DiscountCategoryDTO) {
        for ((i, item) in discountCategoryArrayList.withIndex()) {
            item.selected = i == position
        }
        discountCategoryListMutableLiveData.value = discountCategoryArrayList
    }

    fun membershipHolderSelection(position: Int, systemValueMasterDTO: SystemValueMasterDTO) {
        for ((i, item) in discountMembershipHolderArrayList.withIndex()) {
            item.selected = i == position
        }
        discountMembershipHolderListMutableLiveData.value = discountMembershipHolderArrayList
    }

    fun membershipTypeSelection(position: Int, systemValueMasterDTO: SystemValueMasterDTO) {
        for ((i, item) in discountMembershipTypeArrayList.withIndex()) {
            item.selected = i == position
        }
        discountMembershipTypeListMutableLiveData.value = discountMembershipTypeArrayList
    }

    fun updateTiming(
        isEdit: Boolean,
        position: Int,
        mainContainerPosition: Int,
        discountDaysTimingDTO: DiscountDaysTimingDTO
    ) {

        if (isEdit) {
            discountDayTimingsArrayList[mainContainerPosition].discountDayAndTimings[position] =
                discountDaysTimingDTO
        } else {
            discountDayTimingsArrayList[mainContainerPosition].discountDayAndTimings.add(
                discountDaysTimingDTO
            )
        }

        discountDayTimingsListMutableLiveData.value = discountDayTimingsArrayList
    }

    fun deleteTimings(
        position: Int,
        mainContainerPosition: Int,
        discountDaysTimingDTO: DiscountDaysTimingDTO
    ) {
        discountDayTimingsArrayList[mainContainerPosition].discountDayAndTimings.removeAt(position)
        discountDayTimingsListMutableLiveData.value = discountDayTimingsArrayList
    }

    fun onSaveDiscountClick() {

    }


}