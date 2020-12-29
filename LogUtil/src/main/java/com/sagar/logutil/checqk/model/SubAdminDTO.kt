package com.astrika.checqk.model

import com.astrika.checqk.utils.Constants
import java.io.Serializable

class SubAdminDTO : Serializable{

    var accessRoles:ArrayList<Long> ?= null
    var outletAddress:AddressMasterDTO ?= null
    var userId:Long ?= 0
    var outletId:Long ?= 0
    var profileImage:ImageDTO ?= null
    var emailAddress:String ?= ""
    var fullName:String ?= ""
    var mobileNo:String ?= ""
    var lastLoginDateTime:String ?= null
    var createdOn:String ?= null
    var roles:ArrayList<RolesDTO> ?= null
    var isActive:Boolean ?= null
    var mobileCCode:String ?= ""
    var sourceId:String = Constants.SOURCE

}


class RolesDTO : Serializable{
    var roleId:Long ?= 0
    var name:String ?= ""
    var active:Boolean ?= null
    var createdOn:String ?= ""
    var createdBy:String ?= ""
    var lastModifiedBy:String ?= ""
    var lastModifiedOn:String ?= ""
    var roleName:String ? = ""

}