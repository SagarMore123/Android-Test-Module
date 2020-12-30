package com.sagar.logutil.checqk.model

class BookingDTO {
    var bookingId:Long = 0L
    var outletId:Long = 0L
    var tableIdsAlloted:ArrayList<Int> = arrayListOf()
    var date:String = ""
    var partySize:Long = 0L
    var timeSlotId:Long = 0L
    var time:String = ""
    var timeReq:Float = 0F
    var firstName:String = ""
    var lastName:String = ""
    var phoneNumber:String = ""
    var email:String = ""
    var altPhoneNumber:String = ""
    var notes:String = ""
    var internalNotes:String = ""
    var tags:String = ""
    var status:String = ""
    var isUser:Boolean = false
    var active:Boolean = false
    var createdOn:String = ""
    var createdBy:Long = 0L
    var modifiedOn:String = ""
    var modifiedBy:Long = 0L
}


class TableManagementDTO {

    var active: Boolean ?= true

    var capacity:Long = 0

    var occupied:Boolean ?= null

    var reserved:Boolean ?= null

    var outletId:Long? = 0

    //tableName
    var tableCode:String? = ""

    var tableId:Long ?= 0

    var xcoordinate:Float ?= 0f

    var ycoordinate:Float ?= 0f

}