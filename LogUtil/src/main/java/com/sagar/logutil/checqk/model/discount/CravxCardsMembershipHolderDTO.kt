package com.astrika.checqk.model.discount

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cravx_cards_discount_membership_holder")
class CravxCardsMembershipHolderDTO {

    @PrimaryKey
    var cravxDiscountCardId: Long = 0

    @ColumnInfo
    var cravxDiscountCardName: String = ""

    @ColumnInfo
    var userMembershipTypeId: ArrayList<Long>? = null

    var selected = false
}