package com.astrika.checqk.model

data class GetDishFlagResponseDTO(
    val success: SuccessDTO?,
    val error: ErrorDTO?,
    val productFlagList :  ArrayList<ProductFlagDTO>
)