package com.astrika.checqk.model

data class GetApplicationModulesResponseDTO(
    val success: SuccessDTO?,
    val error: ErrorDTO?,
    val modules :  ArrayList<PermissionDTO>
)