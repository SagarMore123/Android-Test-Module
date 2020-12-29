package com.astrika.checqk.model.discount

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "outlet_discount_category")
class OutletDiscountCategoryDTO {

    @PrimaryKey
    var outletDiscountCategoryId: Long = 0

    @ColumnInfo
    var outletDiscountCategoryName: String = ""

    var selected = false
}