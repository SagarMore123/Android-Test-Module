package com.sagar.logutil.checqk.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.sagar.logutil.checqk.master_controller.source.MasterRepository
import com.sagar.logutil.checqk.source.DashboardRepository
import com.sagar.logutil.checqk.utils.GenericBaseObservable

class CustomizationViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var masterRepository: MasterRepository,
    private var dashboardRepository: DashboardRepository

) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>()

}