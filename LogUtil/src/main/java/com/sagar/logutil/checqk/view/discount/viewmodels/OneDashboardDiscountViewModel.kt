package com.sagar.logutil.checqk.view.discount.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.opposfeapp.utils.SingleLiveEvent
import com.sagar.logutil.checqk.master_controller.source.MasterRepository
import com.sagar.logutil.checqk.master_controller.source.daos.SystemValueMasterDao
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.model.discount.CorporateMembershipOneDashboardDTO
import com.sagar.logutil.checqk.model.discount.OneDashboardMembershipHolderDTO
import com.sagar.logutil.checqk.model.discount.OutletDiscountDetailsDTO
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.GenericBaseObservable
import com.sagar.logutil.checqk.view.login.remote.UserRepository

class OneDashboardDiscountViewModel(
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
    var oneDashboardCardName = MutableLiveData<String>("Select")
    var showProgressBar = MutableLiveData<Boolean>()
    private var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)

    private val onCorporateClick = SingleLiveEvent<Void>()

    // Discount Membership Holder -> DIWALI SPECIAL, VIP TRAVELLERS
    private var discountMembershipHolderArrayList = ArrayList<OneDashboardMembershipHolderDTO>()
    var discountMembershipHolderListMutableLiveData =
        MutableLiveData<List<OneDashboardMembershipHolderDTO>>()

    // Discount Membership Type -> Regular, VIP
    private var tempDiscountMembershipTypeArrayList = ArrayList<SystemValueMasterDTO>()
    private var discountMembershipTypeArrayList = ArrayList<SystemValueMasterDTO>()
    var discountMembershipTypeListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()

    // Discount Days
    private var discountDayTimingsArrayList = ArrayList<DayDTO>()
    var discountDayTimingsListMutableLiveData = MutableLiveData<List<DayDTO>>()

    var corporateMembershipOneDashboardDiscountMasterArrayList =
        ArrayList<CorporateMembershipOneDashboardDTO>()
    var corporateMembershipOneDashboardDTO = CorporateMembershipOneDashboardDTO()

    var outletDiscountDetailsArrayList = ArrayList<OutletDiscountDetailsDTO>()
    var outletDiscountDetailsDTO = OutletDiscountDetailsDTO()

    init {
        outLetId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L
        fetchUserMembershipMaster()
        initiateDays()
    }

    private fun fetchUserMembershipMaster() {

        // Discount Membership Type -> Regular, VIP
        discountMembershipTypeArrayList.clear()

        masterRepository.fetchSystemMasterValueByNameLocal(
            SystemValueMasterDao.MEMBERSHIP_TYPE,
            object : IDataSourceCallback<List<SystemValueMasterDTO>> {

                override fun onDataFound(data: List<SystemValueMasterDTO>) {
                    discountMembershipTypeArrayList =
                        data as ArrayList<SystemValueMasterDTO>
                    populateCorporateMasters()
                }

            })

    }

    fun populateCorporateMasters() {

        if (!corporateMembershipOneDashboardDiscountMasterArrayList.isNullOrEmpty()) {

            oneDashboardCardName.value = corporateMembershipOneDashboardDTO.corporateName ?: ""
            // One Dashboard Discount Membership Holder -> DIWALI SPECIAL, VIP TRAVELLERS
            discountMembershipHolderArrayList =
                corporateMembershipOneDashboardDTO.membershipDetailsList ?: arrayListOf()
            if (!discountMembershipHolderArrayList.isNullOrEmpty()) {
                membershipHolderSelection(0)
            }

        }

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

            if (outletDetails.oneDashboardPlanId == selectedMembershipHolderId
                && outletDetails.userMembershipTypeId == selectedMemberShipTypeDTO.serialId
            ) {
                outletDiscountDetailsDTO = outletDetails
                populateDiscountData(outletDiscountDetailsDTO)
                break
            }

        }

    }

    fun membershipHolderSelection(position: Int) {

        if (selectedMembershipHolderId != discountMembershipHolderArrayList[position].membershipId) {
            for ((i, item) in discountMembershipHolderArrayList.withIndex()) {

                if (i == position) {
                    item.selected = true
                    selectedMembershipHolderId =
                        item.membershipId // Membership Holder Selection

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


    fun onCorporateClick() {
        onCorporateClick.call()
    }

    fun getmOnCorporateClick(): SingleLiveEvent<Void> {
        return onCorporateClick
    }

    fun onSaveDiscountClick() {

        if (validations()) {

            outletDiscountDetailsDTO.outletDiscountCategory = DiscountEnum.ONE_DASHBOARD.name
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

    private fun validations(): Boolean {

        var isValid = true

        if (outletDiscountDetailsDTO.oneDashboardPlanId != selectedMembershipHolderId
            || outletDiscountDetailsDTO.userMembershipTypeId != selectedMemberShipTypeDTO.serialId
        ) {
            outletDiscountDetailsDTO.outletDiscountDetailsId = 0
        }
        outletDiscountDetailsDTO.outletId = outLetId
        outletDiscountDetailsDTO.outletDiscountCategory =
            DiscountEnum.ONE_DASHBOARD.name

        if (selectedMembershipHolderId == 0L) {
            isValid = false
        } else {
            outletDiscountDetailsDTO.oneDashboardPlanId = selectedMembershipHolderId
        }
        if (selectedMemberShipTypeDTO.serialId == 0L) {
            isValid = false
        } else {
            outletDiscountDetailsDTO.userMembershipTypeId = selectedMemberShipTypeDTO.serialId
        }

        outletDiscountDetailsDTO.discountTimingList = arrayListOf()

        mainLoop@ for (item in discountDayTimingsArrayList ?: arrayListOf()) {

            for (item2 in item.discountDayAndTimings ?: arrayListOf()) {

                if (item2.assuredDiscountString.isNullOrBlank()) {
                    item2.assuredDiscount = 0L
                } else {
                    item2.assuredDiscount = item2.assuredDiscountString.trim().toLong()
                }

                if (item2.discountApplicableString.isNullOrBlank()) {
                    item2.discountApplicable = 0L
                } else {
                    item2.discountApplicable = item2.discountApplicableString.trim().toLong()
                }


                if (item.allDaySameDiscountFlag) {

                    if (item2.assuredDiscount != 0L || !item2.complimentaryApplicable.isNullOrBlank()) {
                        if (item2.assuredDiscount <= item2.discountApplicable) {
                            outletDiscountDetailsDTO.discountTimingList.add(item2)
                            item2.complimentaryApplicableErrorMsg = ""
                            item2.discountApplicableErrorMsg = ""
                        } else {
                            isValid = false
                            item2.complimentaryApplicableErrorMsg = ""
                            item2.discountApplicableErrorMsg =
                                "Discount Applicable cannot be lesser than Assured Discount"
                        }
                    } else if ((item2.assuredDiscountString.isNullOrBlank() && !item2.discountApplicableString.isNullOrBlank())
                        || (item2.assuredDiscountString.isNullOrBlank() && !item2.terms.isNullOrBlank())
                    ) {
                        outletDiscountDetailsDTO.discountTimingList.add(item2)
                        item2.complimentaryApplicableErrorMsg = ""
                        item2.discountApplicableErrorMsg = ""
                    } else if (!item2.assuredDiscountString.isNullOrBlank()) {
                        isValid = false
                        item2.complimentaryApplicableErrorMsg = "Please enter complimentary"
                        item2.discountApplicableErrorMsg = ""

                    }

                } else {

                    var dayAssuredDiscount = 0L
                    if (!item.assuredDiscountString.isNullOrBlank()) {
                        dayAssuredDiscount = item.assuredDiscountString.trim().toLong()
                    }
                    item2.assuredDiscount = dayAssuredDiscount
                    if (item2.assuredDiscount != 0L || !item2.complimentaryApplicable.isNullOrBlank()) {
                        if (item2.assuredDiscount <= item2.discountApplicable) {
                            outletDiscountDetailsDTO.discountTimingList.add(item2)
                            item2.complimentaryApplicableErrorMsg = ""
                            item2.discountApplicableErrorMsg = ""
                            item.assuredDiscountErrorMsg = ""
                            item.discountTimingErrorMsg = ""
                        } else if (!item2.startsAt.isNullOrBlank()) {
                            isValid = false
                            item2.complimentaryApplicableErrorMsg = ""
                            item.discountTimingErrorMsg =
                                "For " + item2.startsAt + " - " + item2.endsAt + ",\nDiscount Applicable cannot be lesser than Assured Discount"

                            break@mainLoop
                        }
                    } else if ((item2.assuredDiscountString.isNullOrBlank() && !item2.discountApplicableString.isNullOrBlank())
                        || (item2.assuredDiscountString.isNullOrBlank() && !item2.terms.isNullOrBlank())
                    ) {
                        outletDiscountDetailsDTO.discountTimingList.add(item2)
                        item2.complimentaryApplicableErrorMsg = ""
                        item2.discountApplicableErrorMsg = ""
                    } else if (!item2.assuredDiscountString.isNullOrBlank()) {
                        isValid = false
                        item.discountTimingErrorMsg =
                            "For " + item2.startsAt + " - " + item2.endsAt + ",\nPlease enter complimentary"

                        break@mainLoop
                    } else {
                        item.assuredDiscountErrorMsg = ""
                        item.discountTimingErrorMsg = ""
                    }


                }
            }
        }

        discountDayTimingsListMutableLiveData.value = discountDayTimingsArrayList

        if (outletDiscountDetailsDTO.discountTimingList.size == 0) {
            isValid = false
        }

        return isValid
    }

}