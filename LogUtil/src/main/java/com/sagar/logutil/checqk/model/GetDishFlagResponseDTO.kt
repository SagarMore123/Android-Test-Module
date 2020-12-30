package com.sagar.logutil.checqk.model

data class GetDishFlagResponseDTO(
    val success: SuccessDTO?,
    val error: ErrorDTO?,
    val productFlagList :  ArrayList<ProductFlagDTO>
)