package com.sagar.logutil.checqk.network.network_utils

interface IDataSourceCallback<T> {
    fun onDataFound(data: T) {}
    fun onDataFound(data: T, responseCode: Int) {}
    fun onDataNotFound(){}
    fun onError(error:String){}
}