package com.sagar.logutil

import android.util.Log

class LogError {

    companion object {
        private const val TAG = "App Error"

        fun error(message: String) {
            Log.e(TAG, message)
        }
    }
}