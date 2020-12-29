package com.astrika.checqk.model.discount

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "outlet_discount_membership_plan")
class OutletDiscountMembershipPlanDTO {

    @PrimaryKey
    var membershipId: Long = 0

    @ColumnInfo
    var membershipName: String = ""

    @ColumnInfo
    var membershipHolderId: ArrayList<Long>? = null

    @ColumnInfo
    var userMembershipTypeId: ArrayList<Long>? = null

    @ColumnInfo
    var isTimeSlotAvailable = false

    var selected = false
}