package com.sagar.logutil.checqk.model

data class StaffListingResponseDTO(
    val success: SuccessDTO?,
    val error: ErrorDTO?,
    val userMasterDTOs :  ArrayList<UserDTO>?
)