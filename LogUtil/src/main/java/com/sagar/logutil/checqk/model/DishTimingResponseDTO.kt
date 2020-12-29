package com.astrika.checqk.model

data class DishTimingResponseDTO(
    val success: SuccessDTO?,
    val error: ErrorDTO?,
    val timings :  ArrayList<DishTimingDTO>?
)