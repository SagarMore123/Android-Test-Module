package com.sagar.logutil.checqk.view.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.sagar.logutil.R
import com.sagar.logutil.checqk.model.LoginResponseDTO
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.CustomProgressBar
import com.sagar.logutil.checqk.utils.Utils
import com.sagar.logutil.checqk.view.login.viewmodels.FirstTimeLoginViewModel
import com.sagar.logutil.databinding.FragmentLoginBinding

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    lateinit var viewModel: FirstTimeLoginViewModel
    var progressBar = CustomProgressBar()
    lateinit var loginResponseDTO: LoginResponseDTO
//    var emailAddress: String = ""
    var emailAddress: String = "poojashelar7326@gmail.com"
//    var emailAddress: String = "poojasangle7326@gmail.com"
//    var emailAddress: String = "sagar@gmail.com"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (arguments != null) {
            emailAddress = arguments?.getString(Constants.EMAIL_ID).toString()
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            FirstTimeLoginViewModel::class.java,
            this,
            binding.root
        )!!
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setEmail()
        viewModelObserver()
/*

        val data = Constants.passwordEncrypt("P@ssw0rd")
        Log.e("Encryption",data?:"")
*/

        return binding.root
    }

    private fun setEmail() {
        emailAddress.let {
            viewModel.loginId.value = emailAddress
        }
    }

    private fun viewModelObserver() {
        viewModel.loginId.observe(viewLifecycleOwner, Observer {
            viewModel.loginIdError.value = ""
        })

        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })

        viewModel.loginResponseDTOLiveData.observe(viewLifecycleOwner, Observer {
            loginResponseDTO = it
        })

        viewModel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            if (it) {
                val bundle = Bundle()
                bundle.putString(Constants.EMAIL_ID, viewModel.loginId.value)
                binding.root.findNavController()
                    .navigate(R.id.action_loginFragment_to_loginFragmentWithPassword, bundle)
            } else {
                val bundle = bundleOf(Constants.LOGIN_RESPONSE to loginResponseDTO)
                bundle.putString(Constants.EMAIL_ID, viewModel.loginId.value)
                binding.root.findNavController()
                    .navigate(R.id.action_loginFragment_to_verifyOtpFragment, bundle)
            }
        })


    }

}
