package com.sagar.logutil.checqk.model

data class GetDishListResponseDTO(
    val success: SuccessDTO?,
    val error: ErrorDTO?,
    val productSectionListingDTOs :  ArrayList<ProductWithSectionDetails>
)