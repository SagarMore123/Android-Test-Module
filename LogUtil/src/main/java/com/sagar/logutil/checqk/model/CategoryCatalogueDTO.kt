package com.astrika.checqk.model

data class CategoryCatalogueDTO(
    var catalogueCategoryId : Long?,
    var catalogueCategoryName : String?,
    var catalogueCategoryDesc : String?,
    var catalogueCategorySequenceNo : Long?,
    var active : Boolean?,
    var outletId : Long?,
    var isSelected : Boolean = false
)