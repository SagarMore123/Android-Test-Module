package com.sagar.logutil.checqk.view.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.opposfeapp.utils.SnackbarUtils.showSnackbar
import com.sagar.logutil.R
import com.sagar.logutil.checqk.master_controller.sync.SyncData
import com.sagar.logutil.checqk.network.NetworkController
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.CustomProgressBar
import com.sagar.logutil.checqk.view.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.activity_login.*

class UserLoginActivity : AppCompatActivity() {

    private var isFirstTime: Boolean = true
    private var isLogout: Boolean ?= null
    private var isSignUp: Boolean = false
    var progressBar = CustomProgressBar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        isFirstTime = Constants.getIsFirstTime(this)
        isLogout = Constants.getIsLogout(this)
        isSignUp = Constants.getIsSignUp(this)

        if (isFirstTime && isLogout == null) {
            if(!isSignUp){
                progressBar.show(
                    this,
                    "Please Wait...\nIt will take few minutes"
                ) // Delay to load sign up masters if it is first time login.

                progressBar.dialog?.setCancelable(false)

                SyncData.signUpMasterServices.value = 0

                SyncData.signUpMasterServices.observe(this, Observer {

                    if (it == -1) {
                        progressBar.dialog?.dismiss()
                        showSnackbar(root, NetworkController.SERVER_ERROR)

                    } else if (it == SyncData.signUpMasterServicesCounts) {
                        progressBar.dialog?.dismiss()
                    }
                })
            }
        } else {
            progressBar.dialog?.dismiss()
        }

        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey(Constants.ACCESS_TOKEN_INVALID)) {
                if (bundle.getBoolean(Constants.ACCESS_TOKEN_INVALID)) {
                    showSnackbar(root, "User has been logged out")
                }
            }
        }

        SyncData.loginMasterServicesCounts.value = 0

        SyncData.loginMasterServicesCounts.observe(this, Observer {

            if (it == SyncData.loginMasterServices) {
                val intent = Intent(this, DashboardActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        })

/*

        Handler().postDelayed(
            {
                progressBar.dialog?.dismiss() // Delay to load sign up masters if it is first time login.

            }, Constants.SIGN_UP_MASTERS_TIME_OUT
        )
*/

    }


    override fun onBackPressed() {

    }


}
