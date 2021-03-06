package com.sagar.logutil.checqk.master_controller.sync

import android.app.IntentService
import android.content.Intent
import com.sagar.logutil.checqk.utils.Constants

class MasterSyncIntentService : IntentService("MasterSyncIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        if (intent?.getBooleanExtra(Constants.IS_SPLASH_MASTER, false) == true) {
            SyncData.splashSyncData(this)
        } else {
            SyncData.syncData(this)
        }
    }
}