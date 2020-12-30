package com.sagar.logutil.checqk.model

data class GetInactiveDishListResponseDTO(
    val success: SuccessDTO?,
    val error: ErrorDTO?,
    val inactiveProductList :  ArrayList<ProductDetailsDTO>
)