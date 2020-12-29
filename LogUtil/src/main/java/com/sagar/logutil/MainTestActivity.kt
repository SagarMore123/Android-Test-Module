package com.sagar.logutil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_test)
    }


    var number = 10

    companion object{
        fun init(){

        }

        fun observer(){

        }
    }

}