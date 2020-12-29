package com.astrika.checqk.view

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.astrika.checqk.model.DiscountDaysTimingDTO
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.Utils
import com.sagar.logutil.R
import com.sagar.logutil.databinding.ActivityAddDiscountTimingDialogBinding

class AddDiscountTimingDialogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDiscountTimingDialogBinding
    private lateinit var viewModel: AddDiscountTimingViewModel
    private var resultCode = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        observers()

        spanText()
    }

    private fun initBinding() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_discount_timing_dialog)
        viewModel = Utils.obtainBaseObservable(
            this,
            AddDiscountTimingViewModel::class.java,
            this,
            binding.root
        )!!
        binding.lifecycleOwner = this
        binding.addDiscountTimingViewModel = viewModel

        resultCode = intent.getIntExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, 0)
        viewModel.discountEligiblePlanName.value =
            intent.getStringExtra(Constants.DISCOUNT_PLAN_ELIGIBLE_NAME_KEY) ?: ""
        viewModel.weekName.value = intent.getStringExtra(Constants.WEEK_NAME_KEY) ?: ""

        if (intent?.getSerializableExtra(Constants.DISCOUNT_TIMING_DTO_KEY) != null) {
            val discountDaysTimingDTO: DiscountDaysTimingDTO =
                intent?.getSerializableExtra(Constants.DISCOUNT_TIMING_DTO_KEY) as DiscountDaysTimingDTO
            viewModel.populateData(discountDaysTimingDTO)
        }


    }

    private fun observers() {

        viewModel.getmOnSaveClick().observeChange(this, Observer {
            val resultIntent = Intent()
            resultIntent.putExtra(Constants.SELECTED_DROPDOWN_ITEM, viewModel.discountDaysTimingDTO)
            setResult(resultCode, resultIntent)
            finish()
        })

        viewModel.getmOnCancelClick().observeChange(this, Observer {
            onBackPressed()
        })

        viewModel.applicableDiscount.observe(this, Observer {
            viewModel.applicableDiscountErrorMsg.value = ""
        })
        viewModel.complimentary.observe(this, Observer {
            viewModel.complimentaryErrorMsg.value = ""
        })

    }

    private fun spanText() {
        val wordToSpan: Spannable = SpannableString(
            "Add discount for \n" + viewModel.discountEligiblePlanName.value + " Membership for " + viewModel.weekName.value
        )
        val redColor = ColorStateList(
            arrayOf(intArrayOf()),
            intArrayOf(resources.getColor(R.color.md_red_500))
        )
        val highlightSpan = TextAppearanceSpan(null, Typeface.BOLD_ITALIC, -1, redColor, null)
        wordToSpan.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.colorPrimary)),
            18,
            29 + (viewModel.discountEligiblePlanName.value?.length ?: wordToSpan.length),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        wordToSpan.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.colorPrimary)),
            34 + (viewModel.discountEligiblePlanName.value?.length ?: 0),
            wordToSpan.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
//        wordToSpan.setSpan(highlightSpan, 17,17+( viewModel.discountEligiblePlanName.value?.length?:0), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.discountDescTxt.text = wordToSpan
    }


}
