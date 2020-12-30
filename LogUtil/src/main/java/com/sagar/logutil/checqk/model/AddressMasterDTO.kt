package com.sagar.logutil.checqk.model

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import java.io.Serializable

class CommunicationInfoDTO : Serializable {

    var communicationInfoId: Long? = 0
    var emailIds: ArrayList<String>? = null
    var outletAddress = AddressMasterDTO()
    var outletAddressId: Long? = 0
    var outletId: Long? = 0
    var phoneNos: ArrayList<String>? = null
    var socialMediaMappings: ArrayList<SocialMediaDTO>? = null
    var webUrl: String? = ""

}

class SocialMediaDTO : Serializable {

    var mediumId: Long? = 0
    var outletId: Long? = 0
    var socialUrlMappingId: Long? = 0
    var mediumIconPath: String? = ""
    var url: String? = ""
}

class AddressMasterDTO : Serializable {

    var addressId: Long? = 0
    var addressLine1: String? = ""
    var addressLine2: String? = ""
    var addressType: Long? = 0
    var areaId: Long? = 0
    var areaName: String? = ""
    var cityId: Long? = 0
    var cityName: String? = ""
    var countryId: Long? = 0
    var countryName: String? = ""
    var landlineNo: String? = ""
    var landmark: String? = ""
    var latitude: String? = ""
    var longitude: String? = ""
    var mobileNo: String? = ""
    var pincode: Long? = 0
    var pincodeNo: String? = ""
    var stateId: Long? = 0
    var stateName: String? = ""

}

class DisplayCommunicationInfoDTO {

    var addressLine1 = MutableLiveData<String>("")
    var addressLine2 = MutableLiveData<String>("")

    var countryId: Long = 0
    var countryName = MutableLiveData<String>("")

    var stateId: Long = 0
    var stateName = MutableLiveData<String>("")

    var cityId: Long = 0
    var cityName = MutableLiveData<String>("")

    var areaId: Long = 0
    var areaName = MutableLiveData<String>("")
    var pincodeNo = MutableLiveData<String>("")
    var landmark = MutableLiveData<String>("")
    var latitude = MutableLiveData<String>("")
    var longitude = MutableLiveData<String>("")
    var websiteUrl = MutableLiveData<String>("")


    var countryCode = MutableLiveData<String>("")
    var mobileNo = MutableLiveData<String>("")
    var emailAddress = MutableLiveData<String>("")

    var mediumId: Long = 0
    var mediumIconPath = ObservableField<String>()
    var socialMediaLink = MutableLiveData<String>("")

    // Errors
    var mobileErrorMsg = MutableLiveData<String>("")
    var emailAddressErrorMsg = MutableLiveData<String>("")

    var addressLine1ErrorMsg = MutableLiveData<String>("")
    var countryErrorMsg = MutableLiveData<String>("")
    var stateErrorMsg = MutableLiveData<String>("")
    var cityErrorMsg = MutableLiveData<String>("")
    var areaErrorMsg = MutableLiveData<String>("")
    var latitudeErrorMsg = MutableLiveData<String>("")
    var longitudeErrorMsg = MutableLiveData<String>("")
    var socialMediaErrorMsg = MutableLiveData<String>("")
    var pincodeErrorMsg = MutableLiveData<String>("")


}