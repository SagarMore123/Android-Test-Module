package com.sagar.logutil.checqk.model

import java.io.Serializable

data class LoginResponseDTO(
    val firstTimeLogin:Boolean,
    val success:Success?,
    val mobile:String?,
    val otp:String?,
    val otpExpireTime:Int,
    val message:String?,
    val isAlreadyLoggin:Boolean,
    val error:Error?
):Serializable

data class Success(
    val status:String?,
    val message: String?
):Serializable

data class Error(
    val errorCode:String?,
    val message:String?
):Serializable

data class VisitorResponseDTO(
    val otp:String?,
    val otpExpireTime:Int,
    val status:Int?,
    val success:String?,
    val userId:Long?,
    val loginId:String?
)