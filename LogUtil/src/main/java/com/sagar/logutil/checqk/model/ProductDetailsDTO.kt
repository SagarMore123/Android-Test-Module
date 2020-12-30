package com.sagar.logutil.checqk.model

data class ProductDetailsDTO (
    var productId : Long?,
    var productName : String?,
    var productDesc : String?,
    var productOriPrice : Int?,
    var productDiscountPrice : Int?,
    var productImage : ImageDTO?,
    var availableAllTime : Boolean?,
    var productSequenceNo : Long?,
    var maxOrderQty : Long?,
    var minOrderQty : Long?,
    var outletId : Long?,
    var specialNotes : String?,
    var cuisineIds : ArrayList<Long>?,
    var productFlags : ArrayList<Long>?,
    var productFlagDTOs : ArrayList<ProductFlagDTO>?,
    var catalogueCategoryDTO : ArrayList<CategoryCatalogueDTO>?,
    var catalogueCategoryId : ArrayList<Long>?,
    var catalogueSectionDTO : ArrayList<CatalogueSectionDTO>?,
    var catalogueSectionId : ArrayList<Long>?,
    var active : Boolean?,
    var productTimingDTO : DishTimingRequestDTO?
)