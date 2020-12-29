package com.astrika.checqk.view.signup.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.astrika.checqk.model.SocialMediaDTO
import com.astrika.checqk.model.SocialMediaMasterDTO
import com.astrika.checqk.utils.AutocompleteViewActivity
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.CustomProgressBar
import com.astrika.checqk.utils.Utils
import com.astrika.checqk.view.ThankYouDialogActivity
import com.astrika.checqk.view.dashboard.viewmodels.AddressInfoViewModel
import com.astrika.checqk.view.signup.SignUp
import com.astrika.checqk.view.signup.viewmodels.OutletCreationViewModel
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.SocialMediaAdapter
import com.sagar.logutil.databinding.FragmentOutletCreationAdditionalDetailsBinding


class OutletCreationAdditionalDetailsFragment : Fragment(),
    SocialMediaAdapter.OnRemoveDetailItemClickListener {


    private lateinit var binding: FragmentOutletCreationAdditionalDetailsBinding
    private lateinit var outletCreationViewModel: OutletCreationViewModel
    private lateinit var addressInfoViewModel: AddressInfoViewModel
    private lateinit var socialMediaAdapter: SocialMediaAdapter

    private lateinit var signUp: SignUp
    var progressBar = CustomProgressBar()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        signUp = activity as SignUp
        signUp.fragmentNo(2)

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_outlet_creation_additional_details,
            container,
            false
        )
        binding.lifecycleOwner = this


        outletCreationViewModel = Utils.obtainBaseObservable(
            requireActivity(),
            OutletCreationViewModel::class.java,
            this,
            binding.root
        )!!
        binding.outletCreationViewModel = outletCreationViewModel


        // Address Info
        addressInfoViewModel = Utils.obtainBaseObservable(
            requireActivity(),
            AddressInfoViewModel::class.java,
            this,
            binding.root
        )!!
        binding.addressInfoViewModel = addressInfoViewModel

        socialMediaAdapter = SocialMediaAdapter(requireActivity(), this)
        binding.socialRecyclerView.adapter = socialMediaAdapter


        observer()

        return binding.root
    }


    fun observer() {

        outletCreationViewModel.showProgressBar.observe(requireActivity(), Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })

        outletCreationViewModel.continuePressed.observe(requireActivity(), Observer {
            if (it) {
                val requestCode = Constants.OUTLET_CREATED_CODE
                val intent = Intent(activity, ThankYouDialogActivity::class.java)
                intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
                this.startActivityForResult(intent, requestCode)
            }

        })

        // Is Owner
        outletCreationViewModel.displayRestaurantMasterDTO.isOutletOwner.observe(requireActivity(),
            Observer {
                outletCreationViewModel.outletOwnerLayoutVisible.value = it
            })

        outletCreationViewModel.displayRestaurantMasterDTO.outletMobileCode.observe(requireActivity(),
            Observer {
                outletCreationViewModel.displayRestaurantMasterDTO.outletMobileNoErrorMsg.value = ""
            })

        outletCreationViewModel.displayRestaurantMasterDTO.outletMobileNo.observe(requireActivity(),
            Observer {
                outletCreationViewModel.displayRestaurantMasterDTO.outletMobileNoErrorMsg.value = ""
            })

        outletCreationViewModel.displayRestaurantMasterDTO.outletEmail.observe(requireActivity(),
            Observer {
                outletCreationViewModel.displayRestaurantMasterDTO.outletEmailErrorMsg.value = ""
            })

        outletCreationViewModel.displayRestaurantMasterDTO.userFirstName.observe(requireActivity(),
            Observer {
                outletCreationViewModel.displayRestaurantMasterDTO.userFirstNameErrorMsg.value = ""
            })

        outletCreationViewModel.displayRestaurantMasterDTO.userLastName.observe(requireActivity(),
            Observer {
                outletCreationViewModel.displayRestaurantMasterDTO.userLastNameErrorMsg.value = ""
            })

        outletCreationViewModel.displayRestaurantMasterDTO.userMobileCode.observe(requireActivity(),
            Observer {
                outletCreationViewModel.displayRestaurantMasterDTO.userMobileNoErrorMsg.value = ""
            })

        outletCreationViewModel.displayRestaurantMasterDTO.userMobileNo.observe(requireActivity(),
            Observer {
                outletCreationViewModel.displayRestaurantMasterDTO.userMobileNoErrorMsg.value = ""
            })

        outletCreationViewModel.displayRestaurantMasterDTO.userEmail.observe(requireActivity(),
            Observer {
                outletCreationViewModel.displayRestaurantMasterDTO.userEmailErrorMsg.value = ""
            })


        // Social Media
        addressInfoViewModel.socialMediaListMutableLiveData.observe(requireActivity(), Observer {
            socialMediaAdapter.submitList(it)
        })

        addressInfoViewModel.getmOnSocialMediaIconClick().observeChange(this, Observer {

            val requestCode = Constants.SOCIAL_MEDIA_CODE
            val intent = Intent(activity, AutocompleteViewActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
            this.startActivityForResult(intent, requestCode)

        })


        outletCreationViewModel.getmOnContinueClick().observeChange(requireActivity(),
            Observer {
                if (outletCreationViewModel.outletCreationAdditionalDataValidations()) {
                    addressInfoViewModel.communicationInfoDTO.socialMediaMappings =
                        addressInfoViewModel.socialMediaArrayList ?: arrayListOf()
                    addressInfoViewModel.communicationInfoDTO.webUrl =
                        addressInfoViewModel.displayCommunicationInfoDTO.websiteUrl.value ?: ""
                    outletCreationViewModel.restaurantMasterDTO.communicationInfoDTO =
                        addressInfoViewModel.communicationInfoDTO
                    outletCreationViewModel.onSaveClick()
                }
            })


    }

    override fun onRemoveDetailItemClick(position: Int, socialMediaDTO: SocialMediaDTO) {
        addressInfoViewModel.onRemoveSocialMediaItem(position)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Constants.SOCIAL_MEDIA_CODE) {
            val socialMediaMasterDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as SocialMediaMasterDTO
            addressInfoViewModel.displayCommunicationInfoDTO.mediumId =
                socialMediaMasterDTO.mediumId
            addressInfoViewModel.displayCommunicationInfoDTO.mediumIconPath.set(
                socialMediaMasterDTO.mediumIcon?.path ?: ""
            )
        }

        if (resultCode == Constants.OUTLET_CREATED_CODE) {
            findNavController().navigate(R.id.action_outletCreationAddtionalDetailsFragment_to_PreLoginBasicInfoFragment)
        }

    }


}