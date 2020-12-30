package com.sagar.logutil.checqk.model.discount

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "in_house_membership_holder")
class InHouseMembershipHolderDTO {

    @PrimaryKey
    var inHouseDiscountCardId: Long = 0

    @ColumnInfo
    var inHouseDiscountCardName: String = ""

    @ColumnInfo
    var userMembershipTypeId: ArrayList<Long>? = null

    var selected = false
}