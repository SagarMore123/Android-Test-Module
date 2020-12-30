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
import com.sagar.logutil.checqk.adapters.AccessRolesAdapter
import com.sagar.logutil.checqk.model.AccessRoleDTO
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.CustomProgressBar
import com.sagar.logutil.checqk.utils.Utils
import com.sagar.logutil.checqk.view.dashboard.viewmodels.AddNewRoleDialogViewModel
import com.sagar.logutil.databinding.FragmentAddNewRoleDialogBinding

class AddNewRoleDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "AddNewRoleDialogFragment"

    }

    lateinit var binding: FragmentAddNewRoleDialogBinding
    private lateinit var viewModel: AddNewRoleDialogViewModel

    private lateinit var accessRoleAdapter: AccessRolesAdapter

    var isProfileImageChange = false
    var progressBar = CustomProgressBar()
    var accessRoleDTO: AccessRoleDTO? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_staff_user, container, false)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_new_role_dialog,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            AddNewRoleDialogViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        if (arguments != null) {
            val myArgs = arguments
            accessRoleDTO = myArgs?.getSerializable(Constants.ACCESS_ROLE_DTO) as AccessRoleDTO
//            viewModel.populateData(subAdminDTO)
        }


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        accessRoleAdapter = AccessRolesAdapter(requireContext())
        binding.accessConfigurationRecyclerView.adapter = accessRoleAdapter

        observer()

        viewModel.getApplicationModules()

        if (accessRoleDTO != null && accessRoleDTO?.id != null) {
            viewModel.accessRoleId = accessRoleDTO?.id!!
            viewModel.applicableForEntityId = accessRoleDTO?.applicableForEntityId!!
//            viewModel.getAccessRoleDetails()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun observer() {

        viewModel.showProgressBar.observe(requireActivity(), Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })

        viewModel.cancelDialog.observe(requireActivity(), Observer {
            if (it) {
                dismiss()
            }
        })


        viewModel.roleName.observe(this, Observer {
            viewModel.roleNameErrorMsg.value = ""
        })

        viewModel.roleDescription.observe(this, Observer {
            it.let {
                viewModel.roleDescriptionLength.value = it.length.toString()
            }
        })

        viewModel.permissionMutableList.observe(requireActivity(), Observer {
            it.let {
                accessRoleAdapter.setparentPermissionList(it)
            }
        })

    }

    override fun getTheme(): Int {
        return R.style.RoundedCornersDialog
    }

}