package com.sagar.logutil.checqk.model

data class DishTimingDTO(
    var productId: Long? = 0,
    var productTimingId: Long = 0,
    var outletId: Long = 0,
    var weekDay: Long = 0,
    var opensAt: String = "",
    var closesAt: String = ""
)