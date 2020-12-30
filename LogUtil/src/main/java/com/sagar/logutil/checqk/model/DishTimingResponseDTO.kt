package com.sagar.logutil.checqk.model

data class DishTimingResponseDTO(
    val success: SuccessDTO?,
    val error: ErrorDTO?,
    val timings :  ArrayList<DishTimingDTO>?
)