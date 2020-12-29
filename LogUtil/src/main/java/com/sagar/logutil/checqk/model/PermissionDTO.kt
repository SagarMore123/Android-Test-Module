package com.astrika.checqk.model

import java.io.Serializable

data class PermissionDTO (
    var moduleId : Long?,
    var parentModuleId : Long?,
    var name : String?,
    var addition : Boolean?,
    var edit : Boolean?,
    var viewModule : Boolean?,
    var deActivate : Boolean?,
    var reActivate : Boolean?,
    var viewInactive : Boolean?,
    var permissions: ArrayList<PermissionDTO>? // For UI Purpose
) : Serializable