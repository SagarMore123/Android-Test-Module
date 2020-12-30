package com.sagar.logutil.checqk.model

data class CatalogueSectionDTO(
    var catalogueSectionId : Long?,
    var catalogueSectionName : String?,
    var catalogueCategoryId : Long?,
    var catalogueSectionSequenceNo : Long?,
    var outletId : Long?,
    var taxValue : Double?=0.0,
    var active : Boolean?,
    var isSelected : Boolean = false
)