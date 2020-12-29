package com.astrika.checqk.view.signup.viewmodels

import android.app.Activity
import android.app.Application
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.checqk.model.SystemValueMasterDTO
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.GenericBaseObservable
import com.astrika.checqk.view.login.remote.UserRepository

class SignUpViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var userRepository: UserRepository
) : GenericBaseObservable(application, owner, view) {

    lateinit var error: String
    var showProgressBar = MutableLiveData<Boolean>()

    var continuePressed = MutableLiveData<Boolean>(false)

    private var pageIndicatorArrayList = ArrayList<SystemValueMasterDTO>()
    var pageIndicatorListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()
    private var pageIndicatorList = ArrayList<ObservableBoolean>()

    private val sharedPreferences = Constants.getSharedPreferences(activity)
    var keyList = listOf(Constants.ACCESS_TOKEN)

    init {

        Constants.clearAllPrefsExceptKey(activity,keyList)

        for (i in 0..6) {

            val systemValueMasterDTO = SystemValueMasterDTO()
            systemValueMasterDTO.serialId = i.toLong()
            systemValueMasterDTO.selected = i == 0
            pageIndicatorArrayList.add(systemValueMasterDTO)

        }

        pageIndicatorListMutableLiveData.value = pageIndicatorArrayList

    }


}