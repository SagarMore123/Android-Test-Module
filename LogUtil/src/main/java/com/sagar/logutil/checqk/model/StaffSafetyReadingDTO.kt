package com.astrika.checqk.model

data class StaffSafetyReadingDTO(
    var staffSecurityMeasuresId : Long?,
    var userName : String?,
    var staffUserId : Long?,
    var outletId : Long?,
    var maskFlag : Boolean? = false,
    var tempReading : Int? = 0,
    var checkedOn : String?,
    var profile : ImageDTO?

)