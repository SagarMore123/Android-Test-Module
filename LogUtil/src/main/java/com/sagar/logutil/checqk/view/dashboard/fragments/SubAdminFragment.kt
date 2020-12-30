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
import com.sagar.logutil.checqk.adapters.SubAdminAdapter
import com.sagar.logutil.checqk.model.SubAdminDTO
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.CustomProgressBar
import com.sagar.logutil.checqk.utils.Utils
import com.sagar.logutil.checqk.view.dashboard.viewmodels.SubAdminViewModel
import com.sagar.logutil.databinding.FragmentSubAdminBinding

/**
 * A simple [Fragment] subclass.
 */
class SubAdminFragment : Fragment(),SubAdminAdapter.OnClickListener {

    private lateinit var binding: FragmentSubAdminBinding
    private lateinit var viewModel:SubAdminViewModel
    private lateinit var adapter:SubAdminAdapter
    private var progressBar = CustomProgressBar()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sub_admin,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            SubAdminViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        viewModel.fetchSubAdminListing()
        adapter = SubAdminAdapter(this)
        binding.subAdminRecyclerView.adapter = adapter


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListeners()
        onObservers()
    }

    private fun onObservers() {
        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()

        })

        viewModel.subAdminDTOMutableList.observe(viewLifecycleOwner, Observer {
            adapter.setSubAdminList(it)
        })

        viewModel.subAdminActive.observe(viewLifecycleOwner, Observer {
            if(it){
                binding.activeSubAdminTxt.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary))
                binding.activeView.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary))
//                binding.activeSubAdminTxt.setBackgroundResource(R.drawable.top_left_bottom_left_curve_drawable_light_blue)
            }else{
                binding.activeSubAdminTxt.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorBlack))
                binding.activeView.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.random_grey))
//                binding.activeSubAdminTxt.setBackgroundResource(R.drawable.top_left_bottom_left_curve_drawable)
            }
        })

        viewModel.subAdminInActive.observe(viewLifecycleOwner, Observer {
            if(it){
                binding.inactiveSubAdminTxt.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary))
                binding.inactiveView.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary))
//                binding.inactiveSubAdminTxt.setBackgroundResource(R.drawable.top_right_bottom_right_curve_drawable_light_blue)
            }else{
                binding.inactiveSubAdminTxt.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorBlack))
                binding.inactiveView.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.random_grey))
//                binding.inactiveSubAdminTxt.setBackgroundResource(R.drawable.top_right_bottom_right_curve_drawable)
            }
        })


    }

    private fun onClickListeners() {
        binding.addNewSubAdmin.setOnClickListener {
            AddSubAdminDialog().show(childFragmentManager,AddSubAdminDialog.TAG)
        }

    }

    override fun onEditClick(subAdminDTO: SubAdminDTO) {
        val addSubAdminDialogFragment = AddSubAdminDialog()
        val args = Bundle()
        args.putSerializable(Constants.SUB_ADMIN_DTO,subAdminDTO)
        addSubAdminDialogFragment.arguments = args
        addSubAdminDialogFragment.show(childFragmentManager,AddSubAdminDialog.TAG)
    }

    override fun onRemove(userId: Long?) {
        //show the dialog intimating the user
        viewModel.showConfirmationDialog(userId)
    }

    override fun onRestore(userId: Long?) {
        viewModel.changeSubAdminStatus(userId,true)
    }
}
