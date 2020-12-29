package com.astrika.checqk.model

data class GetInactiveDishListResponseDTO(
    val success: SuccessDTO?,
    val error: ErrorDTO?,
    val inactiveProductList :  ArrayList<ProductDetailsDTO>
)