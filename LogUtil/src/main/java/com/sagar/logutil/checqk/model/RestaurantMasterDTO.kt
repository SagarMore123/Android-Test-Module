package com.sagar.logutil.checqk.model

import androidx.lifecycle.MutableLiveData
import java.io.Serializable
import java.math.BigDecimal

class RestaurantMasterDTO : Serializable {

    var outletId: Long = 0
    var outletDesc: String? = ""
    var outletName: String? = ""
    var type: String? = "Outlet"
    var status: String? = RestaurantInfoStatusEnum.AWAITING_INFO_APPROVAL.name
    var active = true
    var alcohol = true
    var companyId: Long = 0
    var costForTwo: BigDecimal? = BigDecimal(0)
    var dateOfApproval: String? = ""
    var cuisineIds: ArrayList<Long>? = null
    var productCuisineMappings: ArrayList<FamousDishesDTO>? = null
    var facilityTextMappings: ArrayList<FacilityDTO>? = null
    var famousFor: String? = ""
    var outletType: ArrayList<Long>? = null
    var seatingIds: ArrayList<Long>? = null
    var foodTypes: ArrayList<Long>? = null
    var mealTypes: ArrayList<Long>? = null
    var inMall: Boolean? = true
    var isOutletOpen: Boolean? = true
    var restaurantProfileImage: ImageDTO? = null


    // Outlet/Restaurant Creation
    var userDetailsDto: UserDetailsDTO? = null
    var agreementFromDate: String? = null
    var agreementToDate: String? = null
    var agreementDoc: ImageDTO? = null
    var companyName: String? = null
    var displayId: String? = null
    var communicationInfoDTO: CommunicationInfoDTO? = null
}

class FamousDishesDTO : Serializable {

    var dishCuisineMappingId: Long = 0
    var cuisineId: Long = 0
    var cuisineName = "" // Only for Android UI
    var outletId: Long = 0
    var productName = ""


    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }

        other as FamousDishesDTO

        if (dishCuisineMappingId != other.dishCuisineMappingId) {
            return false
        }

        if (cuisineId != other.cuisineId) {
            return false
        }

        if (cuisineName != other.cuisineName) {
            return false
        }

        if (outletId != other.outletId) {
            return false
        }

        if (productName != other.productName) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = dishCuisineMappingId.hashCode()
        result = 31 * result + cuisineId.hashCode()
        result = 31 * result + cuisineName.hashCode()
        result = 31 * result + outletId.hashCode()
        result = 31 * result + productName.hashCode()
        return result
    }

}

class FacilityDTO : Serializable {

    var facilityId: Long = 0
    var outletId: Long = 0
    var facilityTextMappingId: Long = 0
    var supportText = ""


}

class RestaurantProfileImageDTO {
    var documentGetDto: ImageDTO? = null
    var outletId: Long = 0
}


class DisplayRestaurantMasterDTO {

    // Fields
    var outletName = MutableLiveData<String>("")
    var outletMobileCode = MutableLiveData<String>("")
    var outletMobileNo = MutableLiveData<String>("")
    var outletEmail = MutableLiveData<String>("")
    var isOutletOwner = MutableLiveData<Boolean>(true)
    var userFirstName = MutableLiveData<String>("")
    var userLastName = MutableLiveData<String>("")
    var userMobileCode = MutableLiveData<String>("")
    var userMobileNo = MutableLiveData<String>("")
    var userEmail = MutableLiveData<String>("")
    var isOutletOpen = MutableLiveData<Boolean>(true)

    // Errors
    var outletNameErrorMsg = MutableLiveData<String>("")
    var userFirstNameErrorMsg = MutableLiveData<String>("")
    var userLastNameErrorMsg = MutableLiveData<String>("")
    var userMobileNoErrorMsg = MutableLiveData<String>("")
    var userEmailErrorMsg = MutableLiveData<String>("")

    var outletMobileNoErrorMsg = MutableLiveData<String>("")
    var outletEmailErrorMsg = MutableLiveData<String>("")

}