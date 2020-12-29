package com.astrika.checqk.view.signup.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.checqk.model.DayDTO
import com.astrika.checqk.model.DaysEnum
import com.astrika.checqk.model.OutletTimingDTO
import com.astrika.checqk.model.TimingDTO
import com.astrika.checqk.network.network_utils.IDataSourceCallback
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.GenericBaseObservable
import com.astrika.checqk.view.login.UserLoginActivity
import com.astrika.checqk.view.login.remote.UserRepository

class TimingViewModel : GenericBaseObservable {

    var activity: Activity
    var application: Application
    var showProgressBar = MutableLiveData<Boolean>()
    private var userRepository: UserRepository

    var fromTime = MutableLiveData<String>("")
    var toTime = MutableLiveData<String>("")
    var errorMsg = MutableLiveData<String>("")

    var onAddTimeSlotClick = MutableLiveData<Boolean>(false)
    var navToNext = MutableLiveData<Boolean>(false)

    var daysArrayList = ArrayList<DayDTO>()
    var daysListMutableLiveData = MutableLiveData<List<DayDTO>>()

    var resetTimingsArrayList = ArrayList<TimingDTO>()
    var timingArrayList = ArrayList<DayDTO>()
    var timingListMutableLiveData = MutableLiveData<List<DayDTO>>()

    var updatedList = ArrayList<DayDTO>()
    private var sharedPreferences: SharedPreferences
    private var outLetId = 0L


    constructor(
        activity: Activity,
        application: Application,
        owner: LifecycleOwner?,
        view: View?,
        userRepository: UserRepository
    ) : super(application, owner, view) {
        this.activity = activity
        this.application = application
        this.userRepository = userRepository
        sharedPreferences = Constants.getSharedPreferences(application)
        outLetId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L

        initiateDays()
        initiateTimings()
        populateTimings()
    }

    private fun initiateDays() {

        daysArrayList.clear()
        daysListMutableLiveData.value = arrayListOf()
        for (item in DaysEnum.values()) {

            val dayDTO = DayDTO()
            dayDTO.dayId = item.id.toLong()
            dayDTO.dayName = item.value
            daysArrayList.add(dayDTO)
            timingArrayList.add(dayDTO)
        }

        daysListMutableLiveData.value = daysArrayList
    }

    private fun initiateTimings() {

        timingArrayList.clear()
        for (item in DaysEnum.values()) {

            val dayDTO = DayDTO()
            dayDTO.dayId = item.id.toLong()
            dayDTO.dayName = item.value
            timingArrayList.add(dayDTO)
        }

    }


    private fun resetDays() {

        fromTime.value = ""
        toTime.value = ""
        onAddTimeSlotClick.value = false

        for (item in daysArrayList) {
            item.dayIsCheckedOrClosed = false
        }
        daysListMutableLiveData.value = daysArrayList
    }

    private fun populateTimings() {

        showProgressBar.value = true
        userRepository.fetchTimings(outLetId, object : IDataSourceCallback<OutletTimingDTO> {

            override fun onDataFound(data: OutletTimingDTO) {
                showProgressBar.value = false
                resetTimingsArrayList = data.timings
                onResetClick()

            }

            override fun onDataNotFound() {
                showProgressBar.value = false
            }

            override fun onError(error: String) {
                showProgressBar.value = false
                getmSnackbar().value = error
            }
        })
    }

    fun onResetClick() {
        initiateDays()
        initiateTimings()
        for (times in resetTimingsArrayList) {

            for (item in timingArrayList) {

                if (item.dayId == times.weekDay) {
                    item.timings.add(times)
                }
            }
        }


        updatedList.clear()
        for (item in timingArrayList) {

            if (item.timings.isNotEmpty()) {
                item.dayIsCheckedOrClosed = false
                updatedList.add(item)
            }
        }

        timingListMutableLiveData.value = updatedList

    }

    fun onAddTimeSlotClick() {
        onAddTimeSlotClick.value = true
    }

    fun onAddTime() {

        if (!fromTime.value.isNullOrBlank() && !toTime.value.isNullOrBlank()) {

            var isDayPresent = false
            for (item in daysArrayList) {
                if (item.dayIsCheckedOrClosed) {
                    isDayPresent = true
                    for (item2 in timingArrayList) {
                        if (item.dayId == item2.dayId && !item2.dayIsCheckedOrClosed) {
                            val timingDTO = TimingDTO()
                            timingDTO.opensAt = fromTime.value ?: ""
                            timingDTO.closesAt = toTime.value ?: ""
                            timingDTO.outletId = outLetId
                            timingDTO.weekDay = item2.dayId
                            item2.timings.add(timingDTO)
                            break
                        }
                    }
                }
            }


            updatedList.clear()
            for (item in timingArrayList) {

                if (item.timings.isNotEmpty()) {
                    item.dayIsCheckedOrClosed = item.dayIsCheckedOrClosed
                    updatedList.add(item)
                }
            }

            timingListMutableLiveData.value = updatedList
            if (!isDayPresent) {
                errorMsg.value = "Please select day, where the time should be added"
            } else {
                resetDays()
                errorMsg.value = ""
            }


        } else if (fromTime.value.isNullOrBlank()) {
            errorMsg.value = "Please enter From time"

        } else if (toTime.value.isNullOrBlank()) {
            errorMsg.value = "Please enter To time"

        }
    }

    fun onSaveTimingsClick() {


        val saveArrayList = arrayListOf<TimingDTO>()
        for (item in updatedList) {
            if (!item.dayIsCheckedOrClosed) {
                for (item2 in item.timings) {
                    saveArrayList.add(item2)
                }
            }
        }

        val saveTimingsDTO = OutletTimingDTO()
        saveTimingsDTO.outletId = outLetId
        saveTimingsDTO.timings = saveArrayList

        showProgressBar.value = true

        userRepository.saveTimings(
            saveTimingsDTO,
            object : IDataSourceCallback<String> {
                override fun onDataFound(data: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = data
                    navToNext.value = true
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })

    }

    fun onDayItemClick(position: Int, dayDTO: DayDTO) {
        daysArrayList[position].dayIsCheckedOrClosed =
            !daysArrayList[position].dayIsCheckedOrClosed
        daysListMutableLiveData.value = daysArrayList
    }

    // Timings section
    fun onMarkAsClosedItemClick(position: Int, dayDTO: DayDTO) {
        updatedList[position] = dayDTO
        timingListMutableLiveData.value = updatedList

        for ((i, item) in timingArrayList.withIndex()) {
            if (item.dayId == dayDTO.dayId) {
                timingArrayList[i] = dayDTO
                daysArrayList[i].dayEnable = !daysArrayList[i].dayEnable
                if (!daysArrayList[i].dayEnable && daysArrayList[i].dayIsCheckedOrClosed) {
                    daysArrayList[i].dayIsCheckedOrClosed = false
                }
            }
        }

        daysListMutableLiveData.value = daysArrayList

    }

    fun onTimingRemoveItemClick(
        position: Int,
        mainContainerPosition: Int,
        timingDTO: TimingDTO
    ) {
        updatedList[mainContainerPosition].timings.remove(updatedList[mainContainerPosition].timings[position])
        if (updatedList[mainContainerPosition].timings.isEmpty()) {
            updatedList.removeAt(mainContainerPosition)
        }

        timingListMutableLiveData.value = updatedList
    }

    fun onLoginClick() {
        Constants.changeActivity<UserLoginActivity>(activity)
    }

}