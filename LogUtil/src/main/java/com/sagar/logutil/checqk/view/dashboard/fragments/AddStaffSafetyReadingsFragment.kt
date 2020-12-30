package com.sagar.logutil.checqk.view.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.StaffSafetyReadingsAdapter
import com.sagar.logutil.checqk.model.StaffSafetyReadingDTO
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.CustomProgressBar
import com.sagar.logutil.checqk.utils.Utils
import com.sagar.logutil.checqk.view.dashboard.viewmodels.AddStaffSafetyReadingsViewModel
import com.sagar.logutil.databinding.FragmentAddStaffSafetyReadingsBinding

class AddStaffSafetyReadingsFragment : DialogFragment() {

    lateinit var binding: FragmentAddStaffSafetyReadingsBinding
    private lateinit var viewModel: AddStaffSafetyReadingsViewModel
    lateinit var staffSafetyReadingsAdapter: StaffSafetyReadingsAdapter
    private var progressBar = CustomProgressBar()

    companion object {
        const val TAG = "AddStaffSafetyReadingsFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_staff_safety_readings, container, false)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_staff_safety_readings,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            AddStaffSafetyReadingsViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        if (requireArguments() != null && requireArguments().containsKey(Constants.STAFF_USERS_LIST)) {
            viewModel.staffSafetyReadingsList.value =
                requireArguments().getSerializable(Constants.STAFF_USERS_LIST)!! as ArrayList<StaffSafetyReadingDTO>
        }


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        staffSafetyReadingsAdapter = StaffSafetyReadingsAdapter(requireContext())
        binding.staffSafetyReadingsRecyclerView.adapter = staffSafetyReadingsAdapter



        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()

        })


        viewModel.staffSafetyReadingsList.observe(this, Observer {
            it.let {
                staffSafetyReadingsAdapter.setStaffUsersList(it)
            }
        })

        viewModel.cancelDialog.observe(requireActivity(), Observer {
            if (it) {
                dismiss()
            }
        })

        viewModel.getStaffUserSafetyReadings()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun getTheme(): Int {
        return R.style.RoundedCornersDialog
    }
}