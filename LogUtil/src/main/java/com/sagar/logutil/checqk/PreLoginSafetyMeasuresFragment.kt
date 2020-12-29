package com.astrika.checqk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.astrika.checqk.model.SafetyMeasuresDetailsDTO
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.CustomProgressBar
import com.astrika.checqk.utils.Utils
import com.astrika.checqk.view.dashboard.viewmodels.ClosedDatesViewModel
import com.astrika.checqk.view.login.UserLoginActivity
import com.astrika.checqk.view.signup.SignUp
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.SafetyMeasuresDetailsAdapter
import com.sagar.logutil.databinding.FragmentPreLoginSafetyMeasuresBinding
import com.theartofdev.edmodo.cropper.CropImage

class PreLoginSafetyMeasuresFragment : Fragment(),
    SafetyMeasuresDetailsAdapter.OnTextItemClickListener,
    SafetyMeasuresDetailsAdapter.OnImageItemClickListener,
    SafetyMeasuresDetailsAdapter.OnRemoveDetailItemClickListener {


    private lateinit var binding: FragmentPreLoginSafetyMeasuresBinding
    private lateinit var closedDatesViewModel: ClosedDatesViewModel
    private lateinit var safetyMeasuresDetailsAdapter: SafetyMeasuresDetailsAdapter
    private var itemPos = 0

    var progressBar = CustomProgressBar()
    private lateinit var signUp: SignUp


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        signUp = activity as SignUp
        signUp.fragmentNo(6)

        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_pre_login_safety_measures,
                container,
                false
            )
        binding.lifecycleOwner = this

        closedDatesViewModel = Utils.obtainBaseObservable(
            requireActivity(),
            ClosedDatesViewModel::class.java,
            this,
            binding.root
        )!!
        binding.closedDatesViewModel = closedDatesViewModel



        safetyMeasuresDetailsAdapter =
            SafetyMeasuresDetailsAdapter(requireActivity(), this, this, this)
        binding.safetyMeasuresDetailsRecyclerView.adapter = safetyMeasuresDetailsAdapter

        binding.coverBtn.clipToOutline = true
        binding.coverBtn.setOnClickListener {
            closedDatesViewModel.isCoverPic = true
            context?.let { it1 ->
                activity?.let { it2 ->
                    Utils.showChooseProfileDialog(
                        true,
                        it2,
                        it1,
                        this
                    )
                }
            }
        }

        binding.detailBtn.setOnClickListener {
            closedDatesViewModel.isCoverPic = false
            context?.let { it1 ->
                activity?.let { it2 ->
                    Utils.showChooseProfileDialog(
                        true,
                        it2,
                        it1,
                        this
                    )
                }
            }
        }


        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        closedDatesViewModel.showProgressBar.observe(requireActivity(), Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })

        closedDatesViewModel.safetyMeasuresDetailsListMutableLiveData.observe(
            requireActivity(),
            Observer {
                safetyMeasuresDetailsAdapter.arrayList = it
            })

        closedDatesViewModel.isNext.observe(requireActivity(), Observer {
            if (it) {
                Constants.changeActivity<UserLoginActivity>(requireActivity())
                Constants.getSharedPreferences(requireActivity()).edit()?.
                putString(Constants.IS_SIGN_UP,Constants.encrypt(true.toString()))?.apply()
                requireActivity().finish()
            }

        })

    }


    // Safety Measures
    override fun onTextItemClick(
        position: Int,
        safetyMeasuresDetailsDTO: SafetyMeasuresDetailsDTO
    ) {
        closedDatesViewModel.safetyMeasuresDetailsArrayList[position] = safetyMeasuresDetailsDTO
    }

    override fun onImageItemClick(
        position: Int,
        safetyMeasuresDetailsDTO: SafetyMeasuresDetailsDTO
    ) {
        itemPos = position
        closedDatesViewModel.isCoverPic = false
        context?.let { it1 ->
            activity?.let { it2 ->
                Utils.showChooseProfileDialog(
                    true,
                    it2,
                    it1,
                    this
                )
            }
        }
    }

    override fun onRemoveDetailItemClick(
        position: Int,
        safetyMeasuresDetailsDTO: SafetyMeasuresDetailsDTO
    ) {
        closedDatesViewModel.onRemoveSafetyMeasuresDetailItem(position, safetyMeasuresDetailsDTO)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        Utils.hideKeyboard(requireActivity())
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(resultData)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                closedDatesViewModel.safetyMeasuresErrorMsg.value = ""
                if (closedDatesViewModel.isCoverPic) {
                    closedDatesViewModel.safetyMeasuresProfileImageVisible.value = true
                    binding.coverImg.setImageURI(resultUri)
                    closedDatesViewModel.coverUri = resultUri
                } else {
                    closedDatesViewModel.safetyMeasuresDetailImageVisible.value = true
                    closedDatesViewModel.detailUri = resultUri
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                closedDatesViewModel.getmSnackbar().value = error.toString()
            }
        }

    }


}