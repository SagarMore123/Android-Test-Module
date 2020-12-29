package com.astrika.checqk.view.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.astrika.checqk.model.StaffSafetyReadingDTO
import com.astrika.checqk.model.UserDTO
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.CustomProgressBar
import com.astrika.checqk.utils.Utils
import com.astrika.checqk.view.dashboard.viewmodels.StaffUserViewModel
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.StaffUsersAdapter
import com.sagar.logutil.databinding.FragmentStaffUserBinding


class StaffUserFragment : Fragment(),
    StaffUsersAdapter.OnItemClickListener{

    lateinit var binding : FragmentStaffUserBinding
    private lateinit var viewModel: StaffUserViewModel

    private var progressBar = CustomProgressBar()

    lateinit var staffUsersAdapter: StaffUsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_staff_user, container, false)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_staff_user,
            container,
            false
        )
        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            StaffUserViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        staffUsersAdapter = StaffUsersAdapter(requireContext(), this)
        binding.staffUserRecycleIew.adapter = staffUsersAdapter


        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()

        })

        viewModel.subAdminActive.observe(viewLifecycleOwner, Observer {
            if(it){
                binding.activeStaffUserTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary))
                binding.activeView.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary))
//                binding.activeSubAdminTxt.setBackgroundResource(R.drawable.top_left_bottom_left_curve_drawable_light_blue)
            }else{
                binding.activeStaffUserTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorBlack))
                binding.activeView.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.random_grey))
//                binding.activeSubAdminTxt.setBackgroundResource(R.drawable.top_left_bottom_left_curve_drawable)
            }
        })

        viewModel.subAdminInActive.observe(viewLifecycleOwner, Observer {
            if(it){
                binding.inactiveStaffUsersTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary))
                binding.inactiveView.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary))
//                binding.inactiveSubAdminTxt.setBackgroundResource(R.drawable.top_right_bottom_right_curve_drawable_light_blue)
            }else{
                binding.inactiveStaffUsersTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorBlack))
                binding.inactiveView.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.random_grey))
//                binding.inactiveSubAdminTxt.setBackgroundResource(R.drawable.top_right_bottom_right_curve_drawable)
            }
        })

        viewModel.staffUsersMutableLiveData.observe(viewLifecycleOwner, Observer {
            it.let{
                staffUsersAdapter.setStaffUsersList(it, viewModel.isActive)
            }
        })

        binding.addNewStaffUser.setOnClickListener {
            AddStaffUserDialogFragment().show(childFragmentManager,AddStaffUserDialogFragment.TAG)
        }
        binding.addSafetyReading.setOnClickListener {

            val currentDate = Utils.getCurrentDate()

            var staffUsersList =  ArrayList<StaffSafetyReadingDTO>()
            for(staffUser in viewModel.staffUsersMutableLiveData.value!!){

                staffUser.profileImage?.base64 = ""
                staffUsersList.add(
                    StaffSafetyReadingDTO(
                        staffSecurityMeasuresId = 0,
                        userName = staffUser.fullName,
                        staffUserId = staffUser.userId,
                        outletId = staffUser.outletId,
                        maskFlag = false,
                        tempReading = 0,
                        checkedOn = currentDate,
                        profile = staffUser.profileImage
                    )
                )
            }

            val bundle = Bundle()
            bundle.putSerializable(Constants.STAFF_USERS_LIST,staffUsersList!!)

//            AddStaffSafetyReadingsFragment().show(childFragmentManager,AddStaffSafetyReadingsFragment.TAG)
            val addStaffSafetyReadingsFragment = AddStaffSafetyReadingsFragment()
            addStaffSafetyReadingsFragment.arguments = bundle
            addStaffSafetyReadingsFragment.show(childFragmentManager,AddStaffSafetyReadingsFragment.TAG)

        }


        viewModel.getStaffListing("", viewModel.isActive)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onEditItemClick(position: Int, staffUser: UserDTO) {
//        AddStaffSafetyReadingsFragment().show(childFragmentManager,AddStaffSafetyReadingsFragment.TAG)
        val addStaffUserDialogFragment = AddStaffUserDialogFragment()
        val args = Bundle()
        args.putSerializable(Constants.STAFF_USER_DTO,staffUser)
        addStaffUserDialogFragment.arguments = args
        addStaffUserDialogFragment.show(childFragmentManager,AddStaffUserDialogFragment.TAG)

    }

    override fun onDeleteItemClick(position: Int, staffUser: UserDTO) {
        viewModel.changeSubAdminStatus(staffUser.userId,!viewModel.isActive)
    }

}