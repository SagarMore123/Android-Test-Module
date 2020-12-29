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
import androidx.lifecycle.Observer
import com.astrika.checqk.MapsActivity
import com.astrika.checqk.model.*
import com.astrika.checqk.utils.AutocompleteViewActivity
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.CustomProgressBar
import com.astrika.checqk.utils.Utils
import com.astrika.checqk.view.dashboard.viewmodels.AddStaffUserDialogViewModel
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.DaysAdapter
import com.sagar.logutil.checqk.adapters.DishFlagAdapter
import com.sagar.logutil.checqk.adapters.TimingsAdapter
import com.sagar.logutil.checqk.adapters.basicinfo.CuisinesAdapter
import com.sagar.logutil.databinding.FragmentAddStaffUserBinding
import com.theartofdev.edmodo.cropper.CropImage

class AddStaffUserDialogFragment : DialogFragment(),
    DaysAdapter.OnItemClickListener,
    TimingsAdapter.OnItemClickListener,
    TimingsAdapter.OnTimingItemClickListener {

    companion object {
        const val TAG = "AddStaffUserDialogFragment"

    }

    lateinit var binding: FragmentAddStaffUserBinding
    private lateinit var viewModel: AddStaffUserDialogViewModel

    private lateinit var dishFlagAdapter: DishFlagAdapter
    private lateinit var cuisinesAdapter: CuisinesAdapter
    private lateinit var daysAdapter: DaysAdapter
    private lateinit var timingsAdapter: TimingsAdapter

    var isProfileImageChange = false
    var progressBar = CustomProgressBar()
    var staffUserDTO: UserDTO? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_staff_user, container, false)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_staff_user,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            AddStaffUserDialogViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        if (arguments != null) {
            val myArgs = arguments
            staffUserDTO = myArgs?.getSerializable(Constants.STAFF_USER_DTO) as UserDTO?
//            viewModel.populateData(subAdminDTO)
        }


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        daysAdapter = DaysAdapter(requireActivity(), this)
        binding.daysRecyclerView.adapter = daysAdapter

        timingsAdapter = TimingsAdapter(requireActivity(), this, this)
        binding.timingsRecyclerView.adapter = timingsAdapter
        timingsAdapter.singleTiming = true

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

        observer()

        if (staffUserDTO?.userId != null) {
            viewModel.getStaffUserDetails(staffUserDTO?.userId!!)
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

            val requestCode = Constants.GROUP_ROLE_STAFF_CODE
            val intent = Intent(activity, AutocompleteViewActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
            this.startActivityForResult(intent, requestCode)

        })

        viewModel.onKnownLanguagesClickEvent.observeChange(this, Observer {

            val requestCode = Constants.KNOWN_LANGUAGES_CODE
            val intent = Intent(activity, AutocompleteViewActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
            this.startActivityForResult(intent, requestCode)

        })
        viewModel.onDesignationClickEvent.observeChange(this, Observer {

            val requestCode = Constants.DESIGNATION_CODE
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

        viewModel.firstName.observe(this, Observer {
            viewModel.firstNameErrorMsg.value = ""
        })

        viewModel.emailAddress.observe(this, Observer {
            viewModel.emailAddressErrorMsg.value = ""
        })

        viewModel.mobileNumber.observe(this, Observer {
            viewModel.mobileNumberErrorMsg.value = ""
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

        viewModel.daysListMutableLiveData.observe(requireActivity(), Observer {
            daysAdapter.submitList(it)
        })

        viewModel.timingListMutableLiveData.observe(requireActivity(), Observer {
            timingsAdapter.arrayList = it
        })
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

        if (resultCode == Constants.KNOWN_LANGUAGES_CODE) {
            val commonDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as CommonDialogDTO
            viewModel.knowLanguagesIdList.add(commonDTO.id!!)
            if (viewModel.knowLanguagesValue.value!!.isEmpty()) {
                viewModel.knowLanguagesValue.value = commonDTO.name
            } else if (!viewModel.knowLanguagesValue.value!!.contains(commonDTO.name!!)) {
                viewModel.knowLanguagesValue.value =
                    viewModel.knowLanguagesValue.value + ", " + commonDTO.name
            }
        }
        if (resultCode == Constants.DESIGNATION_CODE) {
            val commonDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as CommonDialogDTO
            viewModel.designationId = commonDTO.id!!
            viewModel.designationValue.value = commonDTO.name
        }

        if (resultCode == Constants.GROUP_ROLE_STAFF_CODE) {
            val commonDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as CommonDialogDTO
            viewModel.accessRoleId = commonDTO.id!!
            viewModel.accessRoleValue.value = commonDTO.name!!
        }

        /*if (resultCode == Constants.MAPS_CODE) {
            val addressWithLatLangDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as AddressWithLatLangDTO
            viewModel.updatedAddressInfo(addressWithLatLangDTO)
        }*/

    }

    override fun onDayItemClick(position: Int, dayDTO: DayDTO) {
        viewModel.onDayItemClick(position, dayDTO)
    }

    override fun onMarkAsClosedItemClick(position: Int, dayDTO: DayDTO) {
        viewModel.onMarkAsClosedItemClick(position, dayDTO)
    }

    override fun onTimingRemoveItemClick(
        position: Int,
        mainContainerPosition: Int,
        timingDTO: TimingDTO
    ) {
        viewModel.onTimingRemoveItemClick(position, mainContainerPosition, timingDTO)
    }

    override fun getTheme(): Int {
        return R.style.RoundedCornersDialog
    }
}