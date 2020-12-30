package com.sagar.logutil.checqk.model

data class ProductWithSectionDetails(
    var catalogueSectionDTO : CatalogueSectionDTO,
    var activeProductList : ArrayList<ProductDetailsDTO>?=null,
    var inActiveProductList : ArrayList<ProductDetailsDTO>?=null
)