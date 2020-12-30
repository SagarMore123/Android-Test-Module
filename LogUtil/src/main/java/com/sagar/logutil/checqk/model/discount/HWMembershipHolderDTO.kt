package com.sagar.logutil.checqk.model.discount

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hw_membership_holder")
class HWMembershipHolderDTO {

    @PrimaryKey
    var hwDiscountCardId: Long = 0

    @ColumnInfo
    var hwDiscountCardName: String = ""

    @ColumnInfo
    var userMembershipTypeId: ArrayList<Long>? = null

    var selected = false
}