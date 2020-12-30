package com.sagar.logutil.checqk.model

import java.io.Serializable

data class AccessRoleDTO(
    val id : Long?,
    val applicableForEntityId : Long?, // OutLet Id
    val assignedToUser : Long?,
    val description : String?,
    val name : String?,
    val createdBy : String?,
    val createdOn : String?,
    val modifiedBy : String?,
    val permissions: ArrayList<PermissionDTO>?
) : Serializable