package com.astrika.checqk.network

import com.astrika.checqk.network.network_utils.NetworkUtils.Companion.retrofit
import com.astrika.checqk.network.services.DashboardService
import com.astrika.checqk.network.services.MastersService
import com.astrika.checqk.network.services.UserService

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