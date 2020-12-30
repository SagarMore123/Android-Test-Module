package com.sagar.logutil.checqk.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sagar.logutil.R
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.databinding.ActivityThankYouDialogBinding

class ThankYouDialogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThankYouDialogBinding
    private var resultCode = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
    }

    private fun initBinding() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_thank_you_dialog)
        binding.lifecycleOwner = this

        resultCode = intent.getIntExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, 0)

        binding.closeImg.setOnClickListener {
            onBackPressed()
        }
        binding.addAdditionalInfoBtn.setOnClickListener {
            val resultIntent = Intent()
            setResult(resultCode, resultIntent)
            finish()
        }

    }


}
