package com.astrika.checqk.model

data class GetStaffSafetyReadingResponseDTO(
    val success: SuccessDTO?,
    val error: ErrorDTO?,
    val staffSecurityMeasuresList :  ArrayList<StaffSafetyReadingDTO>?
)