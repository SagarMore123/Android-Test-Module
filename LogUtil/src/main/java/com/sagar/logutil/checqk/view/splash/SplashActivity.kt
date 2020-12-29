package com.astrika.checqk.view.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.astrika.checqk.master_controller.sync.MasterSyncIntentService
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.RootUtil
import com.astrika.checqk.view.login.UserLoginActivity
import com.sagar.logutil.R
import com.sagar.logutil.checqk.view.dashboard.DashboardActivity

class SplashActivity : AppCompatActivity() {

    private var isFirstTime: Boolean = true
    private var isLogout: Boolean ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isFirstTime = Constants.getIsFirstTime(this@SplashActivity)
        isLogout = Constants.getIsLogout(this@SplashActivity)
        if(isFirstTime && isLogout == null){
            startImmediateSync(this)
        }
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(
            {
                // Tamper Checking and restrictions for installing the application on rooted devices
                if (RootUtil.isDeviceRooted) {
                    Constants.showToastMessage(this, "Your Device Is Rooted")
                } else {
                    if (isFirstTime || isLogout == true) {
                        Constants.changeActivity<UserLoginActivity>(this@SplashActivity)
                    } else {
                        Constants.changeActivity<DashboardActivity>(this@SplashActivity)
                    }
                    finish()
                }
            }, Constants.SPLASH_TIME_OUT
        )

    }


    private fun startImmediateSync(context: Context) {
        val intentToSyncImmediately = Intent(context, MasterSyncIntentService::class.java)
        intentToSyncImmediately.putExtra(Constants.IS_SPLASH_MASTER, true)
        context.startService(intentToSyncImmediately)
    }


}
