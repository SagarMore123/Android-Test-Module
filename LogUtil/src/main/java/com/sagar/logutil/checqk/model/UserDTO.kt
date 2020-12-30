package com.sagar.logutil.checqk.model

import com.sagar.logutil.checqk.utils.Constants
import java.io.Serializable

data class UserDTO(
    var userId:Long?,
    var loginId:String?,
    var emailAddress:String?,
    var fullName:String?,
    var userLastName:String?,
    var userMiddleName:String?,
    var mobileNo:String?,
    var userFirstName:String?,
//    var designation:String?,
    var language:String?,
    var timeZone:String?,
    var currency:String?,
//    var userDateOfBirth:String?,
    var profileImage:ImageDTO?,
    var companyId:Long?,
    var managingOrganizationId:Long?,
    var reportingManagerId:Long?,
    var bio:String?,
    var knownLanguages:ArrayList<Long>?,
    var designationId:Long?,
    var totalExperience:String?,
    var workingSince:String?,
    var accessRoles:ArrayList<Long>?,
    var outletId:Long?,
    var productId:Long?,
    var outletAddress:AddressMasterDTO?,
    var mobileCCode:String?,
    var sourceId:String = Constants.SOURCE,
    var lastLoginDateTime:String?,
    var createdOn:String?,
    var staffTimingDTO:StaffTimingRequestDTO?
) : Serializable

class UserDetailsDTO :Serializable {

    var userId: Long? = null
    var userFirstName: String? = ""
    var userLastName: String? = ""
    var fullName: String? = null
    var mobileCode: String? = null
    var mobileNo: String? = ""
    var emailAddress: String? = ""
    var sourceId:String = Constants.SOURCE
}