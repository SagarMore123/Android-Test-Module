package com.sagar.logutil.checqk.view.login.viewmodels

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.sagar.logutil.checqk.master_controller.sync.MasterSyncIntentService
import com.sagar.logutil.checqk.model.LoginDTO
import com.sagar.logutil.checqk.model.LoginResponseDTO
import com.sagar.logutil.checqk.model.UserDTO
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.ErrorCheckUtils
import com.sagar.logutil.checqk.utils.GenericBaseObservable
import com.sagar.logutil.checqk.view.login.remote.UserRepository
import com.sagar.logutil.checqk.view.signup.SignUpActivity

class LoginViewModel : GenericBaseObservable {

    var activity: Activity
    var application: Application
    private var userRepository: UserRepository
    var loginIdEditTxt = MutableLiveData<String>("")

        var passwordEditTxt = MutableLiveData<String>("")
//    var passwordEditTxt = MutableLiveData<String>("Pass@123")

    //    var passwordEditTxt = MutableLiveData<String>("Sagar@123")
    var loginIdError = MutableLiveData<String>("")
    var passwordError = MutableLiveData<String>("")
    lateinit var error: String
    var showProgressBar = MutableLiveData<Boolean>(false)
    var navigateToForgotPassword = MutableLiveData<Boolean>(false)
    var closeClicked = MutableLiveData<Boolean>(false)
    var loginResponseDTOLiveData = MutableLiveData<LoginResponseDTO>()

    private var sharedPreferences:SharedPreferences

    constructor(
        activity: Activity,
        application: Application,
        owner: LifecycleOwner?,
        view: View?,
        userRepository: UserRepository
    )
            : super(application, owner, view) {
        this.activity = activity
        this.application = application
        this.userRepository = userRepository
        sharedPreferences = Constants.getSharedPreferences(activity)

    }

    fun onSignUpClick() {
        Constants.changeActivity<SignUpActivity>(activity)
    }

    fun onProceed() {
        if (validateLoginId() && validatePassword()) {
            val loginDTO = LoginDTO()
            loginDTO.username = loginIdEditTxt.value
            loginDTO.password = Constants.passwordEncrypt(passwordEditTxt.value?:"")
            loginDTO.role = Constants.ROLE
            loginDTO.sourceId = Constants.SOURCE
            loginServiceCall(loginDTO)
        }
    }

    fun onForgotPassword() {
        //change to forgot password fragment and pass the email
        if (validateLoginId()) {
            val loginDTO = LoginDTO()
            loginDTO.username = loginIdEditTxt.value
            loginDTO.isForgotPassword = true
            loginDTO.role = Constants.ROLE
            loginDTO.sourceId = Constants.SOURCE
            forgotPasswordServiceCall(loginDTO)
        }

    }

    private fun loginServiceCall(loginDTO: LoginDTO) {

        showProgressBar.value = true
        userRepository.login(loginDTO, object : IDataSourceCallback<UserDTO> {

            override fun onDataFound(data: UserDTO) {

                sharedPreferences.edit()?.putString(Constants.IS_FIRST_TIME, Constants.encrypt(false.toString()))?.apply()

                //Change the activity to your desired activity
                getmSnackbar().value = "Logged In Successfully"
                val intentToSyncImmediately = Intent(activity, MasterSyncIntentService::class.java)
                intentToSyncImmediately.putExtra(Constants.IS_SPLASH_MASTER, false)
                activity.startService(intentToSyncImmediately)
/*

                Handler().postDelayed({ // Delayed to get load the required masters properly.
                    val intent = Intent(activity, DashboardActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    activity.startActivity(intent)
                    showProgressBar.value = false
                }, 500)
*/

            }

            override fun onError(error: String) {
                showProgressBar.value = false
                Constants.showToastMessage(activity, error)
            }
        })
    }

    fun closeClick() {
        closeClicked.value = true
    }

    private fun forgotPasswordServiceCall(loginDTO: LoginDTO) {
        showProgressBar.value = true
        userRepository.isFirstTimeLoginWithLoginId(
            loginDTO,
            object : IDataSourceCallback<LoginResponseDTO> {

                override fun onDataFound(data: LoginResponseDTO) {
                    loginResponseDTOLiveData.value = data
                    navigateToForgotPassword.value = true
                    showProgressBar.value = false
                }

                override fun onError(error: String) {
                    /*if (error.contains("blocked") || error.contains("credentials do not match")) {
                        loginIdError.value = error
                    } else {
                        Constants.showToastMessage(activity, error)
                    }*/
                    Constants.showToastMessage(activity, error)
                    showProgressBar.value = false
                }
            })
    }

    private fun validateLoginId(): Boolean {
        error = ErrorCheckUtils.checkValidEmail(loginIdEditTxt.value)
        if (error.isNotEmpty()) {
            loginIdError.value = error
            return false
        } else {
            loginIdError.value = ""
        }
        return true
    }

    private fun validatePassword(): Boolean {
        error = ErrorCheckUtils.checkValidPassword(passwordEditTxt.value)
        if (error.isNotEmpty()) {
            passwordError.value = error
            return false
        } else {
            passwordError.value = ""
        }
        return true
    }


}
