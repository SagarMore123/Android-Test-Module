package com.sagar.logutil.checqk.model.discount

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "one_dashboard_membership_holder")
class OneDashboardMembershipHolderDTO1 {

    @PrimaryKey
    var hwDiscountCardId: Long = 0

    @ColumnInfo
    var hwDiscountCardName: String = ""

    @ColumnInfo
    var userMembershipTypeId: ArrayList<Long>? = null

    var selected = false
}