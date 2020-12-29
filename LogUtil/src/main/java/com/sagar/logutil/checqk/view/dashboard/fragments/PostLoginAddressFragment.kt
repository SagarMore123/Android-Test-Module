package com.astrika.checqk.view.dashboard.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.astrika.checqk.MapsActivity
import com.astrika.checqk.model.*
import com.astrika.checqk.utils.AutocompleteViewActivity
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.CustomProgressBar
import com.astrika.checqk.utils.Utils
import com.astrika.checqk.utils.location.AddressWithLatLangDTO
import com.astrika.checqk.view.dashboard.viewmodels.AddressInfoViewModel
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.SocialMediaAdapter
import com.sagar.logutil.checqk.adapters.StringTagsAdapter
import com.sagar.logutil.databinding.FragmentPostLoginAddressBinding


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PostLoginAddressFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostLoginAddressFragment : Fragment(), StringTagsAdapter.OnRemoveItemClickListener,
    SocialMediaAdapter.OnRemoveDetailItemClickListener {

    private lateinit var binding: FragmentPostLoginAddressBinding
    private lateinit var addressInfoViewModel: AddressInfoViewModel
    private lateinit var mobileAdapter: StringTagsAdapter
    private lateinit var emailAdapter: StringTagsAdapter
    private lateinit var socialMediaAdapter: SocialMediaAdapter

    var progressBar = CustomProgressBar()

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_post_login_address,
            container,
            false
        )
        binding.lifecycleOwner = this

        addressInfoViewModel = Utils.obtainBaseObservable(
            requireActivity(),
            AddressInfoViewModel::class.java,
            this,
            binding.root
        )!!
        binding.addressInfoViewModel = addressInfoViewModel
        addressInfoViewModel.populateCommunicationInfo()


        mobileAdapter =
            StringTagsAdapter(requireActivity(), Constants.MOBILE_NO_STRING_ADAPTER, this)
        binding.mobileRecyclerView.adapter = mobileAdapter

        emailAdapter =
            StringTagsAdapter(requireActivity(), Constants.EMAILS_ADDRESS_STRING_ADAPTER, this)
        binding.emailAddressRecyclerView.adapter = emailAdapter

        socialMediaAdapter = SocialMediaAdapter(requireActivity(), this)
        binding.socialRecyclerView.adapter = socialMediaAdapter

        observer()

/*

        binding.webView.setWebViewClient(WebViewClient())
//        binding.webView.loadUrl("https://www.google.com/maps")
        binding.webView.loadUrl("http://maps.google.com/maps?" + "saddr=43.0054446,-87.9678884" + "&daddr=42.9257104,-88.0508355");

        val webSettings: WebSettings = binding.webView.getSettings()
        webSettings.javaScriptEnabled = true
*/

        return binding.root
    }

    fun observer() {

        addressInfoViewModel.showProgressBar.observe(requireActivity(), Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
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

        // Mobile
        addressInfoViewModel.mobileNoArrayListMutableLiveData.observe(requireActivity(), Observer {
            mobileAdapter.list = it
        })

        // Email
        addressInfoViewModel.emailAddressArrayListMutableLiveData.observe(
            requireActivity(),
            Observer {
                emailAdapter.list = it
            })

        // Social Media
        addressInfoViewModel.socialMediaListMutableLiveData.observe(requireActivity(), Observer {
            socialMediaAdapter.submitList(it)
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

        addressInfoViewModel.getmOnSocialMediaIconClick().observeChange(this, Observer {

            val requestCode = Constants.SOCIAL_MEDIA_CODE
            val intent = Intent(activity, AutocompleteViewActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
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
                addressInfoViewModel.displayCommunicationInfoDTO.longitude.value ?: ""
            )
            this.startActivityForResult(intent, requestCode)

        })

        addressInfoViewModel.isNext.observe(requireActivity(), Observer {
/*
            if (it) {
                findNavController().navigate(R.id.action_PostLoginAddressFragment_to_PostLoginTimingsFragment)
            }
*/
        })


    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddressFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PostLoginAddressFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onStringRemoveItemClick(position: Int, adapterType: String, string: String) {
        addressInfoViewModel.onRemoveStringTagItem(position, adapterType)
    }

    override fun onRemoveDetailItemClick(position: Int, socialMediaDTO: SocialMediaDTO) {
        addressInfoViewModel.onRemoveSocialMediaItem(position)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

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