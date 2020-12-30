package com.sagar.logutil.checqk.model

data class DishDetailsResponseDTO(
    val success: SuccessDTO?,
    val error: ErrorDTO?,
    val productDTO :  ProductDetailsDTO?
)