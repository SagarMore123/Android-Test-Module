package com.astrika.checqk.view.signup.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.astrika.checqk.MapsActivity
import com.astrika.checqk.model.*
import com.astrika.checqk.utils.AutocompleteViewActivity
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.CustomProgressBar
import com.astrika.checqk.utils.Utils
import com.astrika.checqk.utils.location.AddressWithLatLangDTO
import com.astrika.checqk.view.dashboard.viewmodels.AddressInfoViewModel
import com.astrika.checqk.view.signup.SignUp
import com.astrika.checqk.view.signup.viewmodels.OutletCreationViewModel
import com.sagar.logutil.R
import com.sagar.logutil.databinding.FragmentOutletCreationBinding
import com.theartofdev.edmodo.cropper.CropImage


class OutletCreationFragment : Fragment() {


    private lateinit var binding: FragmentOutletCreationBinding
    private lateinit var outletCreationViewModel: OutletCreationViewModel
    private lateinit var addressInfoViewModel: AddressInfoViewModel
    private lateinit var signUp: SignUp
    var progressBar = CustomProgressBar()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        signUp = activity as SignUp
        signUp.fragmentNo(1)

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_outlet_creation,
            container,
            false
        )
        binding.lifecycleOwner = this


        binding.outletLogoImg.clipToOutline = true

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

        binding.outletLogoBtn.setOnClickListener {
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

        outletCreationViewModel.continuePressed.observe(requireActivity(), Observer {
            if (it) {
                findNavController().navigate(
                    R.id.action_outletCreationFragment_to_outletCreationAdditionalDetailsFragment
                )
            }

        })

        outletCreationViewModel.displayRestaurantMasterDTO.outletName.observe(requireActivity(),
            Observer {
                outletCreationViewModel.displayRestaurantMasterDTO.outletNameErrorMsg.value = ""
            })


        // Address section
        addressInfoViewModel.displayCommunicationInfoDTO.addressLine1.observe(requireActivity(),
            Observer {
                addressInfoViewModel.displayCommunicationInfoDTO.addressLine1ErrorMsg.value = ""
            })

        addressInfoViewModel.displayCommunicationInfoDTO.countryName.observe(requireActivity(),
            Observer {
                addressInfoViewModel.displayCommunicationInfoDTO.countryErrorMsg.value = ""
            })

        addressInfoViewModel.displayCommunicationInfoDTO.stateName.observe(requireActivity(),
            Observer {
                addressInfoViewModel.displayCommunicationInfoDTO.stateErrorMsg.value = ""
            })

        addressInfoViewModel.displayCommunicationInfoDTO.cityName.observe(requireActivity(),
            Observer {
                addressInfoViewModel.displayCommunicationInfoDTO.cityErrorMsg.value = ""
            })


        addressInfoViewModel.displayCommunicationInfoDTO.areaName.observe(requireActivity(),
            Observer {
                addressInfoViewModel.displayCommunicationInfoDTO.areaErrorMsg.value = ""
            })

        addressInfoViewModel.displayCommunicationInfoDTO.pincodeNo.observe(requireActivity(),
            Observer {
                addressInfoViewModel.displayCommunicationInfoDTO.pincodeErrorMsg.value = ""
            })

        addressInfoViewModel.displayCommunicationInfoDTO.latitude.observe(requireActivity(),
            Observer {
                addressInfoViewModel.displayCommunicationInfoDTO.latitudeErrorMsg.value = ""
            })

        addressInfoViewModel.displayCommunicationInfoDTO.longitude.observe(requireActivity(),
            Observer {
                addressInfoViewModel.displayCommunicationInfoDTO.longitudeErrorMsg.value = ""
            })

        addressInfoViewModel.getmOnCountryClick().observeChange(this, Observer {
            val requestCode = Constants.COUNTRY_CODE
            val intent = Intent(activity, AutocompleteViewActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
            this.startActivityForResult(intent, requestCode)
        })

        addressInfoViewModel.getmOnStateClick().observeChange(this, Observer {

            val requestCode = Constants.STATE_CODE
            val intent = Intent(activity, AutocompleteViewActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
            intent.putExtra(
                Constants.COUNTRY_ID_KEY,
                addressInfoViewModel.displayCommunicationInfoDTO.countryId ?: 0L
            )
            this.startActivityForResult(intent, requestCode)


        })

        addressInfoViewModel.getmOnCityClick().observeChange(this, Observer {

            val requestCode = Constants.CITY_CODE
            val intent = Intent(activity, AutocompleteViewActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
            intent.putExtra(
                Constants.STATE_ID_KEY,
                addressInfoViewModel.displayCommunicationInfoDTO.stateId ?: 0L
            )
            this.startActivityForResult(intent, requestCode)


        })

        addressInfoViewModel.getmOnAreaClick().observeChange(this, Observer {

            val requestCode = Constants.AREA_CODE
            val intent = Intent(activity, AutocompleteViewActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
            intent.putExtra(
                Constants.CITY_ID_KEY,
                addressInfoViewModel.displayCommunicationInfoDTO.cityId ?: 0L
            )
            this.startActivityForResult(intent, requestCode)

        })

        addressInfoViewModel.getmOnChangeLocationClick().observeChange(this, Observer {

            val requestCode = Constants.MAPS_CODE
            val intent = Intent(activity, MapsActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
            intent.putExtra(
                Constants.LOCATION_LATITUDE_KEY,
                addressInfoViewModel.displayCommunicationInfoDTO.latitude.value ?: ""
            )
            intent.putExtra(
                Constants.LOCATION_LONGITUDE_KEY,
                addressInfoViewModel.displayCommunicationInfoDTO.latitude.value ?: ""
            )
            this.startActivityForResult(intent, requestCode)

        })

        outletCreationViewModel.getmOnContinueClick().observeChange(requireActivity(),
            Observer {
                if (addressInfoViewModel.validations() && outletCreationViewModel.outletCreationValidations()) {
                    outletCreationViewModel.restaurantMasterDTO.communicationInfoDTO =
                        addressInfoViewModel.communicationInfoDTO
                    outletCreationViewModel.onSaveClick()
                }
            })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Utils.hideKeyboard(requireActivity())
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                outletCreationViewModel.outletLogoErrorMsg.value = ""

                outletCreationViewModel.outletLogoVisible.value = true
                binding.outletLogoImg.setImageURI(resultUri)
                outletCreationViewModel.uri = resultUri

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                outletCreationViewModel.getmSnackbar().value = error.toString()
            }
        }


        if (resultCode == Constants.COUNTRY_CODE) {
            val countryMasterDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as CountryMasterDTO
            addressInfoViewModel.displayCommunicationInfoDTO.countryId = countryMasterDTO.countryId
            addressInfoViewModel.displayCommunicationInfoDTO.countryName.value =
                countryMasterDTO.countryName
        }

        if (resultCode == Constants.STATE_CODE) {
            val stateMasterDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as StateMasterDTO
            addressInfoViewModel.displayCommunicationInfoDTO.stateId = stateMasterDTO.stateId
            addressInfoViewModel.displayCommunicationInfoDTO.stateName.value =
                stateMasterDTO.stateName
        }

        if (resultCode == Constants.CITY_CODE) {
            val cityMasterDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as CityMasterDTO
            addressInfoViewModel.displayCommunicationInfoDTO.cityId = cityMasterDTO.cityId
            addressInfoViewModel.displayCommunicationInfoDTO.cityName.value = cityMasterDTO.cityName
        }

        if (resultCode == Constants.AREA_CODE) {
            val areaMasterDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as AreaMasterDTO
            addressInfoViewModel.displayCommunicationInfoDTO.areaId = areaMasterDTO.areaId
            addressInfoViewModel.displayCommunicationInfoDTO.areaName.value = areaMasterDTO.areaName
        }

        if (resultCode == Constants.SOCIAL_MEDIA_CODE) {
            val socialMediaMasterDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as SocialMediaMasterDTO
            addressInfoViewModel.displayCommunicationInfoDTO.mediumId =
                socialMediaMasterDTO.mediumId
            addressInfoViewModel.displayCommunicationInfoDTO.mediumIconPath.set(
                socialMediaMasterDTO.mediumIcon?.path ?: ""
            )
        }

        if (resultCode == Constants.MAPS_CODE) {
            val addressWithLatLangDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as AddressWithLatLangDTO
            addressInfoViewModel.updatedAddressInfo(addressWithLatLangDTO)
        }
    }


}