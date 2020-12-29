package com.astrika.checqk.model

data class GetAccessRolesDetailsResponseDTO(
    val success: SuccessDTO?,
    val error: ErrorDTO?,
    val groupRoleDto : AccessRoleDTO?
)