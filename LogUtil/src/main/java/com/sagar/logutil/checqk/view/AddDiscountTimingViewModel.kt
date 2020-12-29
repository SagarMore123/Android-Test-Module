package com.astrika.checqk.view

import android.app.Activity
import android.app.Application
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.checqk.model.DiscountDaysTimingDTO
import com.astrika.checqk.utils.GenericBaseObservable
import com.example.opposfeapp.utils.SingleLiveEvent

class AddDiscountTimingViewModel(
    var activity: Activity,
    application: Application,
    owner: LifecycleOwner?,
    view: View?
) :
    GenericBaseObservable(
        application,
        owner,
        view
    ) {

    var fromTime = MutableLiveData<String>("")
    var toTime = MutableLiveData<String>("")
    var errorMsg = MutableLiveData<String>("")
    var applicableDiscountErrorMsg = MutableLiveData<String>("")
    var complimentaryErrorMsg = MutableLiveData<String>("")
    var discountEligiblePlanName = MutableLiveData<String>("")
    var weekName = MutableLiveData<String>("")

    var assuredDiscount = MutableLiveData<String>("0")
    var applicableDiscount = MutableLiveData<String>("0")
    var complimentary = MutableLiveData<String>("")
    var terms = MutableLiveData<String>("")

    var discountDaysTimingDTO = DiscountDaysTimingDTO()

    var onSaveClick = SingleLiveEvent<Void>()
    var onCancelClick = SingleLiveEvent<Void>()


    fun populateData(discountDaysTimingDTO: DiscountDaysTimingDTO) {

        this.discountDaysTimingDTO = discountDaysTimingDTO
        fromTime.value = discountDaysTimingDTO.startsAt?.trim()
        toTime.value = discountDaysTimingDTO.endsAt?.trim()
        assuredDiscount.value = discountDaysTimingDTO.assuredDiscount?.toString().trim()
        applicableDiscount.value = discountDaysTimingDTO.discountApplicable?.toString().trim()
        complimentary.value = discountDaysTimingDTO.complimentaryApplicable?.trim()
        terms.value = discountDaysTimingDTO.terms?.trim()

    }

    fun onSaveClick() {

        if (validations()) {
            onSaveClick.call()
        }

    }

    fun getmOnSaveClick(): SingleLiveEvent<Void> {
        return onSaveClick
    }


    fun onCancelClick() {
        onCancelClick.call()
    }

    fun getmOnCancelClick(): SingleLiveEvent<Void> {
        return onCancelClick
    }


    private fun validations(): Boolean {
        var isValid = true

        if (!fromTime.value.isNullOrBlank() && !toTime.value.isNullOrBlank()) {

            discountDaysTimingDTO.startsAt = fromTime.value ?: ""
            discountDaysTimingDTO.endsAt = toTime.value ?: ""

            errorMsg.value = ""
        } else if (fromTime.value.isNullOrBlank()) {
            errorMsg.value = "Please enter From time"
            isValid = false
        } else if (toTime.value.isNullOrBlank()) {
            errorMsg.value = "Please enter To time"
            isValid = false
        }

        if (assuredDiscount.value.isNullOrBlank()) {
            discountDaysTimingDTO.assuredDiscount = 0L
        } else {
            discountDaysTimingDTO.assuredDiscount = assuredDiscount.value.toString().trim().toLong()
        }

        if (applicableDiscount.value.isNullOrBlank()) {
            discountDaysTimingDTO.discountApplicable = 0L
        } else {
            discountDaysTimingDTO.discountApplicable =
                applicableDiscount.value.toString().trim().toLong()
        }


        if (discountDaysTimingDTO.assuredDiscount != 0L || !complimentary.value.isNullOrBlank()) {
            if (discountDaysTimingDTO.assuredDiscount > discountDaysTimingDTO.discountApplicable) {
                isValid = false
                complimentaryErrorMsg.value = ""
                applicableDiscountErrorMsg.value =
                    "Discount Applicable cannot be lesser than Assured Discount"
            }
        } else if (!assuredDiscount.value.isNullOrBlank()) {
            isValid = false
            complimentaryErrorMsg.value = "Please enter complimentary"

        }


        discountDaysTimingDTO.assuredDiscountString = discountDaysTimingDTO.assuredDiscount.toString().trim()
        discountDaysTimingDTO.discountApplicableString = discountDaysTimingDTO.discountApplicable.toString().trim()

        discountDaysTimingDTO.complimentaryApplicable = complimentary.value.toString().trim()
        discountDaysTimingDTO.terms = terms.value.toString().trim()



        return isValid
    }


}
