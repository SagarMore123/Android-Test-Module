package com.astrika.checqk.view.dashboard.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.astrika.checqk.MapsActivity
import com.astrika.checqk.model.*
import com.astrika.checqk.utils.AutocompleteViewActivity
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.CustomProgressBar
import com.astrika.checqk.utils.Utils
import com.astrika.checqk.utils.location.AddressWithLatLangDTO
import com.astrika.checqk.view.dashboard.viewmodels.AddNewSubAdminViewModel
import com.sagar.logutil.R
import com.sagar.logutil.databinding.FragmentAddSubAdminDialogBinding
import com.theartofdev.edmodo.cropper.CropImage

/**
 * A simple [Fragment] subclass.
 */
class AddSubAdminDialog : DialogFragment() {

    companion object {
        const val TAG = "AddSubAdminDialog"
    }

    lateinit var binding: FragmentAddSubAdminDialogBinding
    private lateinit var viewModel: AddNewSubAdminViewModel
    var isProfileImageChange = false
    var progressBar = CustomProgressBar()
    var subAdminDTO: SubAdminDTO? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_sub_admin_dialog,
            container,
            false
        )
//        return inflater.inflate(R.layout.fragment_add_sub_admin_dialog, container, false)

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            AddNewSubAdminViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        if (arguments != null) {
            val myArgs = arguments
            subAdminDTO = myArgs?.getSerializable(Constants.SUB_ADMIN_DTO) as SubAdminDTO?
            viewModel.populateData(subAdminDTO)
        }

        observer()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileImageEdit.setOnClickListener {
            isProfileImageChange = true
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

        viewModel.getmOnAccessInfoClick().observeChange(this, Observer {

            val requestCode = Constants.GROUP_ROLE_CODE
            val intent = Intent(activity, AutocompleteViewActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
            this.startActivityForResult(intent, requestCode)

        })

        viewModel.getmOnCountryClick().observeChange(this, Observer {
            val requestCode = Constants.COUNTRY_CODE
            val intent = Intent(activity, AutocompleteViewActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
            this.startActivityForResult(intent, requestCode)
        })

        viewModel.getmOnStateClick().observeChange(this, Observer {

            val requestCode = Constants.STATE_CODE
            val intent = Intent(activity, AutocompleteViewActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
            intent.putExtra(
                Constants.COUNTRY_ID_KEY,
                viewModel.displayCommunicationInfoDTO.countryId ?: 0L
            )
            this.startActivityForResult(intent, requestCode)

        })

        viewModel.getmOnCityClick().observeChange(this, Observer {

            val requestCode = Constants.CITY_CODE
            val intent = Intent(activity, AutocompleteViewActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
            intent.putExtra(
                Constants.STATE_ID_KEY,
                viewModel.displayCommunicationInfoDTO.stateId ?: 0L
            )
            this.startActivityForResult(intent, requestCode)


        })

        viewModel.getmOnAreaClick().observeChange(this, Observer {

            val requestCode = Constants.AREA_CODE
            val intent = Intent(activity, AutocompleteViewActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
            intent.putExtra(
                Constants.CITY_ID_KEY,
                viewModel.displayCommunicationInfoDTO.cityId ?: 0L
            )
            this.startActivityForResult(intent, requestCode)

        })

        viewModel.getmOnChangeLocationClick().observeChange(this, Observer {

            val requestCode = Constants.MAPS_CODE
            val intent = Intent(activity, MapsActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
            intent.putExtra(
                Constants.LOCATION_LATITUDE_KEY,
                viewModel.displayCommunicationInfoDTO.latitude.value ?: ""
            )
            intent.putExtra(
                Constants.LOCATION_LONGITUDE_KEY,
                viewModel.displayCommunicationInfoDTO.latitude.value ?: ""
            )
            this.startActivityForResult(intent, requestCode)

        })

        viewModel.fullName.observe(this, Observer {
            viewModel.fullNameErrorMsg.value = ""
        })

        viewModel.emailAddress.observe(this, Observer {
            viewModel.emailAddressErrorMsg.value = ""
        })

        viewModel.mobileNumber.observe(this, Observer {
            viewModel.mobileNumberErrorMsg.value = ""
        })

        viewModel.mobileCode.observe(this, Observer {
            viewModel.countryCodeErrorMsg.value = ""
        })

        viewModel.displayCommunicationInfoDTO.addressLine1.observe(this, Observer {
            viewModel.displayCommunicationInfoDTO.addressLine1ErrorMsg.value = ""
        })

        viewModel.displayCommunicationInfoDTO.countryName.observe(requireActivity(),
            Observer {
                viewModel.displayCommunicationInfoDTO.countryErrorMsg.value = ""
            })

        viewModel.displayCommunicationInfoDTO.stateName.observe(requireActivity(),
            Observer {
                viewModel.displayCommunicationInfoDTO.stateErrorMsg.value = ""
            })

        viewModel.displayCommunicationInfoDTO.cityName.observe(requireActivity(),
            Observer {
                viewModel.displayCommunicationInfoDTO.cityErrorMsg.value = ""
            })


        viewModel.displayCommunicationInfoDTO.areaName.observe(requireActivity(),
            Observer {
                viewModel.displayCommunicationInfoDTO.areaErrorMsg.value = ""
            })

        viewModel.displayCommunicationInfoDTO.pincodeNo.observe(requireActivity(),
            Observer {
                viewModel.displayCommunicationInfoDTO.pincodeErrorMsg.value = ""
            })

        viewModel.displayCommunicationInfoDTO.latitude.observe(requireActivity(),
            Observer {
                viewModel.displayCommunicationInfoDTO.latitudeErrorMsg.value = ""
            })

        viewModel.displayCommunicationInfoDTO.longitude.observe(requireActivity(),
            Observer {
                viewModel.displayCommunicationInfoDTO.longitudeErrorMsg.value = ""
            })

    }

    override fun getTheme(): Int {
        return R.style.RoundedCornersDialog
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                if (isProfileImageChange) {
                    binding.profileImage.setImageURI(resultUri)
                    viewModel.convertUriToBase64(resultUri)
//                    viewModel.saveRestaurantProfileImage(resultUri)
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                viewModel.getmSnackbar().value = error.toString()
            }
        }

        if (resultCode == Constants.GROUP_ROLE_CODE) {
            val groupRoleDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as GroupRolesDTO
            viewModel.accessInfoId = groupRoleDTO.id
            viewModel.accessInfoValue.value = groupRoleDTO.name
        }

        if (resultCode == Constants.COUNTRY_CODE) {
            val countryMasterDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as CountryMasterDTO
            viewModel.displayCommunicationInfoDTO.countryId = countryMasterDTO.countryId
            viewModel.displayCommunicationInfoDTO.countryName.value =
                countryMasterDTO.countryName
        }

        if (resultCode == Constants.STATE_CODE) {
            val stateMasterDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as StateMasterDTO
            viewModel.displayCommunicationInfoDTO.stateId = stateMasterDTO.stateId
            viewModel.displayCommunicationInfoDTO.stateName.value =
                stateMasterDTO.stateName
        }

        if (resultCode == Constants.CITY_CODE) {
            val cityMasterDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as CityMasterDTO
            viewModel.displayCommunicationInfoDTO.cityId = cityMasterDTO.cityId
            viewModel.displayCommunicationInfoDTO.cityName.value = cityMasterDTO.cityName
        }

        if (resultCode == Constants.AREA_CODE) {
            val areaMasterDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as AreaMasterDTO
            viewModel.displayCommunicationInfoDTO.areaId = areaMasterDTO.areaId
            viewModel.displayCommunicationInfoDTO.areaName.value = areaMasterDTO.areaName
        }

        if (resultCode == Constants.MAPS_CODE) {
            val addressWithLatLangDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as AddressWithLatLangDTO
            viewModel.updatedAddressInfo(addressWithLatLangDTO)
        }
    }

    /*override fun onStop() {
        super.onStop()
        dialog?.dismiss()
    }*/
}
