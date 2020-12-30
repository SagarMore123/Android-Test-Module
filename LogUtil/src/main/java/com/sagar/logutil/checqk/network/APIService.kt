package com.sagar.logutil.checqk.network

import com.sagar.logutil.checqk.network.network_utils.NetworkUtils.Companion.retrofit
import com.sagar.logutil.checqk.network.services.DashboardService
import com.sagar.logutil.checqk.network.services.MastersService
import com.sagar.logutil.checqk.network.services.UserService

object UserApi {

    val retrofitService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    val mastersRetrofitService: MastersService by lazy {
        retrofit.create(MastersService::class.java)
    }

/*
    val systemValueMastersRetrofitService : MastersService by lazy{
        systemValueMasterRetrofit.create(MastersService::class.java)
    }
*/

    val retrofitDashboardService: DashboardService by lazy {
        retrofit.create(DashboardService::class.java)
    }
}