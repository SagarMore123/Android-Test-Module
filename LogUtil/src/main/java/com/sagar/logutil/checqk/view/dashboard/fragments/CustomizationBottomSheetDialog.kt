package com.astrika.checqk.view.dashboard.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.astrika.checqk.utils.CustomProgressBar
import com.astrika.checqk.utils.Utils
import com.astrika.checqk.view.dashboard.viewmodels.CustomizationViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sagar.logutil.R
import com.sagar.logutil.databinding.CustomizationBottomSheetDialogBinding

class CustomizationBottomSheetDialog(fragment: AddDishDialogFragment) : BottomSheetDialogFragment() {

    private lateinit var binding: CustomizationBottomSheetDialogBinding
    private lateinit var viewModel: CustomizationViewModel
    lateinit var mContext: Context
    private var progressBar = CustomProgressBar()
    private var addDishDialogFragment:AddDishDialogFragment = fragment


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container != null) {
            mContext = container.context
        }
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.customization_bottom_sheet_dialog,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            CustomizationViewModel::class.java,
            this,
            binding.root
        )!!

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        return binding.root

    }

}