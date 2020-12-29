package com.astrika.checqk.model

import java.io.Serializable

data class CommonResponseDTO(
    val success: SuccessDTO?,
    val error: ErrorDTO?,
    val otp: String?,
    val catalogueCategoryID: Long?,
    val catalogueSectionId: Long?,
    val id: Long?,
    val outletId: Long?
) : Serializable

data class SuccessDTO(
    val status: String,
    val message: String
) : Serializable


data class ErrorDTO(
    val errorCode: String,
    val message: String
) : Serializable
