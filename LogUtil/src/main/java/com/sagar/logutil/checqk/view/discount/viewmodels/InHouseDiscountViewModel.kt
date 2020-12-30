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
import com.sagar.logutil.checqk.model.discount.InHouseMembershipHolderDTO
import com.sagar.logutil.checqk.model.discount.OutletDiscountDetailsDTO
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.GenericBaseObservable
import com.sagar.logutil.checqk.utils.Utils
import com.sagar.logutil.checqk.view.login.remote.UserRepository

class InHouseDiscountViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var masterRepository: MasterRepository,
    private var userRepository: UserRepository
) : GenericBaseObservable(application, owner, view) {

    private var outLetId = 0L
    var selectedMembershipHolderId: Long = 0
    private var selectedMemberShipTypeDTO = SystemValueMasterDTO()
    var showProgressBar = MutableLiveData<Boolean>()
    private var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)


    // Discount Membership Holder -> DIWALI SPECIAL, VIP TRAVELLERS
    private var discountMembershipHolderArrayList = ArrayList<InHouseMembershipHolderDTO>()
    var discountMembershipHolderListMutableLiveData =
        MutableLiveData<List<InHouseMembershipHolderDTO>>()

    // Discount Membership Type -> Regular, VIP
    private var tempDiscountMembershipTypeArrayList = ArrayList<SystemValueMasterDTO>()
    private var discountMembershipTypeArrayList = ArrayList<SystemValueMasterDTO>()
    var discountMembershipTypeListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()

    // Discount Days
    var discountDayTimingsArrayList = ArrayList<DayDTO>()
    var discountDayTimingsListMutableLiveData = MutableLiveData<List<DayDTO>>()

    var outletDiscountDetailsArrayList = ArrayList<OutletDiscountDetailsDTO>()
    var outletDiscountDetailsDTO = OutletDiscountDetailsDTO()

    init {
        outLetId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L
        initiateDays()
        populateMasters()
    }

    private fun populateMasters() {


        // Cravx Cards Discount Membership Holder -> DIWALI SPECIAL, VIP TRAVELLERS
        masterRepository.fetchInHouseDiscountMembershipHolderMasterDataLocal(object :
            IDataSourceCallback<List<InHouseMembershipHolderDTO>> {

            override fun onDataFound(data: List<InHouseMembershipHolderDTO>) {
                discountMembershipHolderArrayList = data as ArrayList<InHouseMembershipHolderDTO>
                discountMembershipHolderListMutableLiveData.value =
                    discountMembershipHolderArrayList


                // Discount Membership Type -> Regular, VIP
                discountMembershipTypeArrayList.clear()

                masterRepository.fetchSystemMasterValueByNameLocal(
                    SystemValueMasterDao.MEMBERSHIP_TYPE,
                    object : IDataSourceCallback<List<SystemValueMasterDTO>> {

                        override fun onDataFound(data: List<SystemValueMasterDTO>) {
                            discountMembershipTypeArrayList =
                                data as ArrayList<SystemValueMasterDTO>

                            if (!discountMembershipHolderArrayList.isNullOrEmpty()) {
                                membershipHolderSelection(0)
                            }

                        }

                        override fun onError(error: String) {
//                                            getmSnackbar().value = error
                        }
                    })


            }

            override fun onError(error: String) {
//                getmSnackbar().value = error
            }
        })


    }

    private fun initiateDays() {

        discountDayTimingsArrayList.clear()
        discountDayTimingsListMutableLiveData.value = arrayListOf()
        for (item in DaysEnum.values()) {

            val dayDTO = DayDTO()
            dayDTO.dayId = item.id.toLong()
            dayDTO.dayName = item.value
            dayDTO.dayIsCheckedOrClosed = true

            // discount
            dayDTO.allDaySameDiscountFlag = true

            val discountDaysTimingDTO = DiscountDaysTimingDTO()
            discountDaysTimingDTO.weekDay = item.id.toLong()
            discountDaysTimingDTO.allDaySameDiscountFlag = true
            dayDTO.discountDayAndTimings.add(discountDaysTimingDTO)
            discountDayTimingsArrayList.add(dayDTO)
        }

        discountDayTimingsListMutableLiveData.value = discountDayTimingsArrayList
    }

    private fun populateDiscountData(outletDiscountDetailsDTO: OutletDiscountDetailsDTO) {

        //Response timings loop
        for (times in outletDiscountDetailsDTO.discountTimingList ?: arrayListOf()) {

            for ((i, item) in discountDayTimingsArrayList.withIndex()) { // Setting data to UI loop

                if (item.dayId == times.weekDay && !item.discountDayAndTimings.contains(times)) { // Check for same day
                    item.allDaySameDiscountFlag = times.allDaySameDiscountFlag
                    item.assuredDiscountString = times.assuredDiscount.toString()

                    times.assuredDiscountString = times.assuredDiscount.toString()
                    times.discountApplicableString = times.discountApplicable.toString()
                    item.discountDayAndTimings.add(times)
                }
            }
        }

        // Removing empty timing content only if list contains multiple timings data
        for (item in discountDayTimingsArrayList) {
            if (item.discountDayAndTimings.size > 1) {
                for ((i, item2) in item.discountDayAndTimings.withIndex()) {
                    if (item2.assuredDiscountString.isNullOrBlank()
                        && item2.discountApplicableString.isNullOrBlank()
                        && item2.complimentaryApplicable.isNullOrBlank()
                        && item2.terms.isNullOrBlank()
                    ) {
                        item.discountDayAndTimings.removeAt(i)
                        break
                    }
                }
            }
        }

        // Rendering UI Week Timing list
        discountDayTimingsListMutableLiveData.value = discountDayTimingsArrayList

    }

    private fun updateDiscount() {
        initiateDays()

        for (outletDetails in outletDiscountDetailsArrayList ?: arrayListOf()) {

            if (outletDetails.cardId == selectedMembershipHolderId
                && outletDetails.userMembershipTypeId == selectedMemberShipTypeDTO.serialId
            ) {
                outletDiscountDetailsDTO = outletDetails
                populateDiscountData(outletDiscountDetailsDTO)
                break
            }

        }

    }


    fun membershipHolderSelection(position: Int) {

        if (selectedMembershipHolderId != discountMembershipHolderArrayList[position].inHouseDiscountCardId) {
            for ((i, item) in discountMembershipHolderArrayList.withIndex()) {

                if (i == position) {
                    item.selected = true
                    selectedMembershipHolderId =
                        item.inHouseDiscountCardId // Membership Holder Selection

                    tempDiscountMembershipTypeArrayList.clear()
                    discountMembershipTypeListMutableLiveData.value = arrayListOf()
                    for (userMembershipTypeId in item.userMembershipTypeId ?: arrayListOf()) {

                        for (localUserMembershipType in discountMembershipTypeArrayList) {

                            if (localUserMembershipType.serialId == userMembershipTypeId) {
                                tempDiscountMembershipTypeArrayList.add(localUserMembershipType)
                                break
                            }
                        }


                    }

                    // checking for Membership Type is already selected or not
                    if (!tempDiscountMembershipTypeArrayList.isNullOrEmpty()) {
                        // Membership Type Selection
                        selectedMemberShipTypeDTO = SystemValueMasterDTO()
                        membershipTypeSelection(0)
                    }


                } else {
                    item.selected = false
                }

            }
            discountMembershipHolderListMutableLiveData.value = discountMembershipHolderArrayList
            updateDiscount()
        }
    }


    fun membershipTypeSelection(position: Int) {

        if (selectedMemberShipTypeDTO.serialId != tempDiscountMembershipTypeArrayList[position].serialId) {
            for ((i, item) in tempDiscountMembershipTypeArrayList.withIndex()) {

                if (i == position) {
                    item.selected = true
//                    selectedMembershipTypeId = item.serialId
                    selectedMemberShipTypeDTO = item
                } else {
                    item.selected = false
                }
            }
            discountMembershipTypeListMutableLiveData.value = tempDiscountMembershipTypeArrayList
            updateDiscount()
        }
    }


    fun onSaveDiscountClick() {

        if (Utils.commonDiscountValidations(
                outLetId,
                outletDiscountDetailsDTO,
                selectedMembershipHolderId,
                selectedMemberShipTypeDTO,
                discountDayTimingsArrayList,
                discountDayTimingsListMutableLiveData
            )
        ) {

            outletDiscountDetailsDTO.outletDiscountCategory = DiscountEnum.INHOUSE_CARD.name
            showProgressBar.value = true
            userRepository.saveOutletDiscountDetails(outletDiscountDetailsDTO,
                object : IDataSourceCallback<String> {

                    override fun onDataFound(data: String) {
                        showProgressBar.value = false
                        getmSnackbar().value = data
                    }

                    override fun onError(error: String) {
                        showProgressBar.value = false
                        getmSnackbar().value = error
                    }
                })
        }

    }

}