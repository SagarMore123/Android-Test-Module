package com.sagar.logutil.checqk.model

data class GetStaffSafetyReadingResponseDTO(
    val success: SuccessDTO?,
    val error: ErrorDTO?,
    val staffSecurityMeasuresList :  ArrayList<StaffSafetyReadingDTO>?
)