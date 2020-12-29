package com.astrika.checqk.view.discount.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.checqk.master_controller.source.MasterRepository
import com.astrika.checqk.model.*
import com.astrika.checqk.model.discount.OutletDiscountDetailsDTO
import com.astrika.checqk.model.discount.OutletDiscountMembershipPlanDTO
import com.astrika.checqk.network.network_utils.IDataSourceCallback
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.GenericBaseObservable
import com.astrika.checqk.view.login.remote.UserRepository

class MembershipDiscountViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var masterRepository: MasterRepository,
    private var userRepository: UserRepository
) : GenericBaseObservable(application, owner, view) {

    private var outLetId = 0L
    var selectedMembershipHolderId: Long = 0
    var selectedMembershipTypeId: Long = 0
    var timeSlotLayoutVisible = MutableLiveData<Boolean>(false)
    var showProgressBar = MutableLiveData<Boolean>()
    private var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)


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

    var outletDiscountDetailsArrayList = ArrayList<OutletDiscountDetailsDTO>()
    var outletDiscountDetailsDTO = OutletDiscountDetailsDTO()
    var outletDiscountMembershipPlanDTO = OutletDiscountMembershipPlanDTO()

    init {
        outLetId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L
        initiateDays(true) // Default true for All day same discount flag
        populateMasters()
    }


    private fun populateMasters() {


        // Discount Membership plan -> Basic Plan, Basic Corporate Plan,....etc
        masterRepository.fetchDiscountMembershipPlanMasterDataLocal(object :
            IDataSourceCallback<List<OutletDiscountMembershipPlanDTO>> {

            override fun onDataFound(data: List<OutletDiscountMembershipPlanDTO>) {
                discountMembershipPlanArrayList = data as ArrayList<OutletDiscountMembershipPlanDTO>
                discountMembershipPlanListMutableLiveData.value = discountMembershipPlanArrayList
                populateOutletMembershipPlanMapping()
            }

            override fun onError(error: String) {
                getmSnackbar().value = error
            }
        })

    }

    private fun initiateDays(isAllDaySameDiscountFlag: Boolean) {

        daysArrayList.clear()
        daysListMutableLiveData.value = arrayListOf()
        discountDayTimingsArrayList.clear()
        discountDayTimingsListMutableLiveData.value = arrayListOf()
        for (item in DaysEnum.values()) {

            val dayDTO = DayDTO()
            dayDTO.dayId = item.id.toLong()
            dayDTO.dayName = item.value
            dayDTO.dayIsCheckedOrClosed = isAllDaySameDiscountFlag
            daysArrayList.add(dayDTO)

            // discount
            dayDTO.allDaySameDiscountFlag = isAllDaySameDiscountFlag

            val discountDaysTimingDTO = DiscountDaysTimingDTO()
            discountDaysTimingDTO.weekDay = item.id.toLong()
            discountDaysTimingDTO.allDaySameDiscountFlag = isAllDaySameDiscountFlag
            dayDTO.discountDayAndTimings.add(discountDaysTimingDTO)
            discountDayTimingsArrayList.add(dayDTO)
        }

        daysListMutableLiveData.value = daysArrayList
        discountDayTimingsListMutableLiveData.value = discountDayTimingsArrayList
    }


    private fun populateOutletMembershipPlanMapping() {

        showProgressBar.value = true
        userRepository.fetchOutletMembershipPlanMapping(outLetId,
            object : IDataSourceCallback<Long> {

                override fun onDataFound(data: Long) {
                    showProgressBar.value = false
                    for (item in discountMembershipPlanArrayList) {
                        if (item.membershipId == data) {
                            item.selected = true
                            outletDiscountMembershipPlanDTO = item
                            if (item.isTimeSlotAvailable) {
                                timeSlotLayoutVisible.value = true
                            }

                            // Discount Membership Holder -> INDIVIDUALS, CORPORATES
                            discountMembershipHolderArrayList.clear()
                            for (membershipHolder in item.membershipHolderId ?: arrayListOf()) {

                                masterRepository.fetchSystemValueMasterValueByIdLocal(
                                    membershipHolder,
                                    object : IDataSourceCallback<SystemValueMasterDTO> {

                                        override fun onDataFound(data: SystemValueMasterDTO) {
                                            discountMembershipHolderArrayList.add(data)
                                            if (!discountMembershipHolderArrayList.isNullOrEmpty()) {
                                                discountMembershipHolderArrayList[0].selected = true
                                                selectedMembershipHolderId =
                                                    discountMembershipHolderArrayList[0].serialId
                                            }
                                            discountMembershipHolderListMutableLiveData.value =
                                                discountMembershipHolderArrayList

                                        }

                                        override fun onError(error: String) {
                                            getmSnackbar().value = error
                                        }
                                    })
                            }


                            // Discount Membership Type -> Regular, VIP
                            discountMembershipTypeArrayList.clear()
                            for (membershipType in item.userMembershipTypeId ?: arrayListOf()) {


                                masterRepository.fetchSystemValueMasterValueByIdLocal(
                                    membershipType,
                                    object : IDataSourceCallback<SystemValueMasterDTO> {

                                        override fun onDataFound(data: SystemValueMasterDTO) {
                                            discountMembershipTypeArrayList.add(data)
                                            if (!discountMembershipTypeArrayList.isNullOrEmpty()) {
                                                discountMembershipTypeArrayList[0].selected = true
                                                selectedMembershipTypeId =
                                                    discountMembershipTypeArrayList[0].serialId
                                            }
                                            discountMembershipTypeListMutableLiveData.value =
                                                discountMembershipTypeArrayList

                                            updateDiscount()
                                        }

                                        override fun onError(error: String) {
//                                            getmSnackbar().value = error
                                        }
                                    })
                            }


                            break
                        }
                    }

                    discountMembershipPlanListMutableLiveData.value =
                        discountMembershipPlanArrayList

                }

                override fun onDataNotFound() {
                    showProgressBar.value = false
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })
    }


    private fun populateDiscountData(outletDiscountDetailsDTO: OutletDiscountDetailsDTO) {

        //Response timings loop
        for (times in outletDiscountDetailsDTO.discountTimingList ?: arrayListOf()) {

            for ((i, item) in discountDayTimingsArrayList.withIndex()) { // Setting data to UI loop

                if (item.dayId == times.weekDay && !item.discountDayAndTimings.contains(times)) { // Check for same day
                    item.allDaySameDiscountFlag = times.allDaySameDiscountFlag
                    daysArrayList[i].dayIsCheckedOrClosed = times.allDaySameDiscountFlag
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
        daysListMutableLiveData.value = daysArrayList
        discountDayTimingsListMutableLiveData.value = discountDayTimingsArrayList

    }

    private fun updateDiscount() {
        initiateDays(!outletDiscountMembershipPlanDTO.isTimeSlotAvailable)

        for (outletDetails in outletDiscountDetailsArrayList ?: arrayListOf()) {

            if (outletDiscountMembershipPlanDTO.membershipId == outletDetails.outletMembershipPlanId
                && outletDetails.memberShipHolderId == selectedMembershipHolderId
                && outletDetails.userMembershipTypeId == selectedMembershipTypeId
            ) {
                outletDiscountDetailsDTO = outletDetails
                populateDiscountData(outletDiscountDetailsDTO)
                break
            }

        }

    }

    fun onDayItemClick(position: Int, dayDTO: DayDTO) {
        daysArrayList[position].dayIsCheckedOrClosed = !daysArrayList[position].dayIsCheckedOrClosed
        daysListMutableLiveData.value = daysArrayList

        discountDayTimingsArrayList[position].allDaySameDiscountFlag = dayDTO.dayIsCheckedOrClosed
        discountDayTimingsListMutableLiveData.value = discountDayTimingsArrayList

    }


    fun membershipHolderSelection(position: Int, systemValueMasterDTO: SystemValueMasterDTO) {

        if (selectedMembershipHolderId != discountMembershipHolderArrayList[position].serialId) {
            for ((i, item) in discountMembershipHolderArrayList.withIndex()) {

                if (i == position) {
                    item.selected = true
                    selectedMembershipHolderId = item.serialId
                } else {
                    item.selected = false
                }

            }
            discountMembershipHolderListMutableLiveData.value = discountMembershipHolderArrayList
            updateDiscount()
        }
    }


    fun membershipTypeSelection(position: Int, systemValueMasterDTO: SystemValueMasterDTO) {

        if (selectedMembershipTypeId != discountMembershipTypeArrayList[position].serialId) {
            for ((i, item) in discountMembershipTypeArrayList.withIndex()) {

                if (i == position) {
                    item.selected = true
                    selectedMembershipTypeId = item.serialId
                } else {
                    item.selected = false
                }
            }
            discountMembershipTypeListMutableLiveData.value = discountMembershipTypeArrayList
            updateDiscount()
        }
    }

    fun addOrEditWeekDayTiming(
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

        discountDayTimingsArrayList[mainContainerPosition].assuredDiscountErrorMsg = ""
        discountDayTimingsArrayList[mainContainerPosition].discountTimingErrorMsg = ""
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

        if (validations()) {

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


        if (outletDiscountMembershipPlanDTO.membershipId != outletDiscountDetailsDTO.outletMembershipPlanId
            || outletDiscountDetailsDTO.memberShipHolderId != selectedMembershipHolderId
            || outletDiscountDetailsDTO.userMembershipTypeId != selectedMembershipTypeId
        ) {
            outletDiscountDetailsDTO.outletDiscountDetailsId = 0
        }
        outletDiscountDetailsDTO.outletId = outLetId
//        outletDiscountDetailsDTO.outletDiscountCategoryId = DiscountEnum.MEMBERSHIP_DISCOUNTS.id
        outletDiscountDetailsDTO.outletDiscountCategory =
            DiscountEnum.MEMBERSHIP_DISCOUNTS.name
        outletDiscountDetailsDTO.outletMembershipPlanId =
            outletDiscountMembershipPlanDTO.membershipId
//        outletDiscountDetailsDTO.outletMembershipPlanId = 1
        outletDiscountDetailsDTO.outletMembershipPlanName =
            outletDiscountMembershipPlanDTO.membershipName

        if (selectedMembershipHolderId == 0L) {
            isValid = false
        } else {
            outletDiscountDetailsDTO.memberShipHolderId = selectedMembershipHolderId
        }
        if (selectedMembershipTypeId == 0L) {
            isValid = false
        } else {
            outletDiscountDetailsDTO.userMembershipTypeId = selectedMembershipTypeId
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
/*
        if (!isValid) {
            discountDayTimingsListMutableLiveData.value = discountDayTimingsArrayList
        }
*/
        if (outletDiscountDetailsDTO.discountTimingList.size == 0) {
            isValid = false
        }

        return isValid
    }

}