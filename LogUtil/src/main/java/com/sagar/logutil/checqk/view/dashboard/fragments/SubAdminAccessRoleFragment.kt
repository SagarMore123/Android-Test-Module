package com.sagar.logutil.checqk.view.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.SubAdminAccessRoleAdapter
import com.sagar.logutil.checqk.model.AccessRoleDTO
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.CustomProgressBar
import com.sagar.logutil.checqk.utils.Utils
import com.sagar.logutil.checqk.view.dashboard.viewmodels.SubAdminAccessRoleViewModel
import com.sagar.logutil.databinding.FragmentSubAdminAccessRoleBinding


class SubAdminAccessRoleFragment : Fragment(), SubAdminAccessRoleAdapter.OnItemClickListener {

    lateinit var binding: FragmentSubAdminAccessRoleBinding
    private lateinit var viewModel: SubAdminAccessRoleViewModel

    private var progressBar = CustomProgressBar()

    lateinit var subAdminAccessRoleAdapter: SubAdminAccessRoleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_staff_user, container, false)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sub_admin_access_role,
            container,
            false
        )
        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            SubAdminAccessRoleViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        subAdminAccessRoleAdapter = SubAdminAccessRoleAdapter(requireContext(), this)
        binding.accessRoleRecycleIew.adapter = subAdminAccessRoleAdapter


        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()

        })

        viewModel.activeRecords.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.activeRecordsTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
                binding.activeView.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
//                binding.activeSubAdminTxt.setBackgroundResource(R.drawable.top_left_bottom_left_curve_drawable_light_blue)
            } else {
                binding.activeRecordsTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorBlack
                    )
                )
                binding.activeView.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.random_grey
                    )
                )
//                binding.activeSubAdminTxt.setBackgroundResource(R.drawable.top_left_bottom_left_curve_drawable)
            }
        })

        viewModel.inactiveRecords.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.inactiveRecordsTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
                binding.inactiveView.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
//                binding.inactiveSubAdminTxt.setBackgroundResource(R.drawable.top_right_bottom_right_curve_drawable_light_blue)
            } else {
                binding.inactiveRecordsTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorBlack
                    )
                )
                binding.inactiveView.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.random_grey
                    )
                )
//                binding.inactiveSubAdminTxt.setBackgroundResource(R.drawable.top_right_bottom_right_curve_drawable)
            }
        })

        viewModel.accessRolesMutableLiveData.observe(viewLifecycleOwner, Observer {
            it.let {
                subAdminAccessRoleAdapter.setSubAdminAccessRoleList(it, viewModel.isActive)
            }
        })

        binding.addNewRole.setOnClickListener {
            AddNewRoleDialogFragment().show(childFragmentManager, AddNewRoleDialogFragment.TAG)
        }

        viewModel.getSubAdminAccessRoleListing("", viewModel.isActive)
    }

    override fun onEditItemClick(position: Int, accessRole: AccessRoleDTO) {
        val addNewRoleDialogFragment = AddNewRoleDialogFragment()
        val args = Bundle()
        args.putSerializable(Constants.ACCESS_ROLE_DTO, accessRole)
        addNewRoleDialogFragment.arguments = args
        addNewRoleDialogFragment.show(childFragmentManager, AddNewRoleDialogFragment.TAG)

    }

    override fun onDeleteItemClick(position: Int, accessRole: AccessRoleDTO) {
        viewModel.changeAccessRoleStatus(accessRole.id, !viewModel.isActive)

    }

}