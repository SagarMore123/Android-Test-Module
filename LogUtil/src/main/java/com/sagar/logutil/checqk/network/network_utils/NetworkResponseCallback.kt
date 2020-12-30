package com.sagar.logutil.checqk.network.network_utils

interface NetworkResponseCallback {

    fun onSuccess(data:String)

    fun onError(errorCode:Int,errorData:String)

}