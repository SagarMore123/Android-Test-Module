package com.sagar.logutil.checqk.model

data class GetAccessRolesResponseDTO(
    val success: SuccessDTO?,
    val error: ErrorDTO?,
    val list :  ArrayList<AccessRoleDTO>
)