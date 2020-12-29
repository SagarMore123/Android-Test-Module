package com.astrika.checqk.model

data class GetDishListResponseDTO(
    val success: SuccessDTO?,
    val error: ErrorDTO?,
    val productSectionListingDTOs :  ArrayList<ProductWithSectionDetails>
)