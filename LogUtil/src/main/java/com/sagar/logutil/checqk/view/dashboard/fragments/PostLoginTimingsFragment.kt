package com.sagar.logutil.checqk.view.dashboard.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.ClosedDatesAdapter
import com.sagar.logutil.checqk.adapters.DaysAdapter
import com.sagar.logutil.checqk.adapters.SafetyMeasuresDetailsAdapter
import com.sagar.logutil.checqk.adapters.TimingsAdapter
import com.sagar.logutil.checqk.model.ClosedDatesDTO
import com.sagar.logutil.checqk.model.DayDTO
import com.sagar.logutil.checqk.model.SafetyMeasuresDetailsDTO
import com.sagar.logutil.checqk.model.TimingDTO
import com.sagar.logutil.checqk.utils.CustomProgressBar
import com.sagar.logutil.checqk.utils.Utils
import com.sagar.logutil.checqk.view.dashboard.viewmodels.ClosedDatesViewModel
import com.sagar.logutil.checqk.view.signup.viewmodels.TimingViewModel
import com.sagar.logutil.databinding.FragmentPostLoginTimingsBinding
import com.theartofdev.edmodo.cropper.CropImage


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class PostLoginTimingsFragment : Fragment(),
    DaysAdapter.OnItemClickListener,
    TimingsAdapter.OnItemClickListener,
    TimingsAdapter.OnTimingItemClickListener,
    ClosedDatesAdapter.OnItemClickListener,
    SafetyMeasuresDetailsAdapter.OnTextItemClickListener,
    SafetyMeasuresDetailsAdapter.OnImageItemClickListener,
    SafetyMeasuresDetailsAdapter.OnRemoveDetailItemClickListener {

    private lateinit var binding: FragmentPostLoginTimingsBinding
    private lateinit var timingViewModel: TimingViewModel
    private lateinit var closedDatesViewModel: ClosedDatesViewModel
    private lateinit var daysAdapter: DaysAdapter
    private lateinit var timingsAdapter: TimingsAdapter
    private lateinit var closedDatesAdapter: ClosedDatesAdapter
    private lateinit var safetyMeasuresDetailsAdapter: SafetyMeasuresDetailsAdapter

    private var progressBar = CustomProgressBar()

    private var itemPos = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_post_login_timings,
            container,
            false
        )

        timingViewModel = Utils.obtainBaseObservable(
            requireActivity(),
            TimingViewModel::class.java,
            this,
            binding.root
        )!!
        binding.timingViewModel = timingViewModel

        closedDatesViewModel = Utils.obtainBaseObservable(
            requireActivity(),
            ClosedDatesViewModel::class.java,
            this,
            binding.root
        )!!
        binding.closedDatesViewModel = closedDatesViewModel

        binding.lifecycleOwner = this

        daysAdapter = DaysAdapter(requireActivity(), this)
        binding.daysRecyclerView.adapter = daysAdapter

        timingsAdapter = TimingsAdapter(requireActivity(), this, this)
        binding.timingsRecyclerView.adapter = timingsAdapter

        closedDatesAdapter = ClosedDatesAdapter(requireActivity(), this)
        binding.closedDatesRecyclerView.adapter = closedDatesAdapter

        safetyMeasuresDetailsAdapter =
            SafetyMeasuresDetailsAdapter(requireActivity(), this, this, this)
        binding.safetyMeasuresDetailsRecyclerView.adapter = safetyMeasuresDetailsAdapter

        binding.coverImg.clipToOutline = true

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        timingViewModel.showProgressBar.observe(requireActivity(), Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })
/*

        closedDatesViewModel.showProgressBar.observe(requireActivity(), Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })

        closedDatesViewModel.showProgressBar.value = false
*/

        timingViewModel.daysListMutableLiveData.observe(requireActivity(), Observer {
            daysAdapter.submitList(it)
        })

        timingViewModel.timingListMutableLiveData.observe(requireActivity(), Observer {
            timingsAdapter.arrayList = it
        })

        closedDatesViewModel.closedDatesListMutableLiveData.observe(requireActivity(), Observer {
            closedDatesAdapter.submitList(it)
        })

        closedDatesViewModel.safetyMeasuresDetailsListMutableLiveData.observe(
            requireActivity(),
            Observer {
                safetyMeasuresDetailsAdapter.arrayList = it
            })


    }


    override fun onDayItemClick(position: Int, dayDTO: DayDTO) {
        timingViewModel.onDayItemClick(position, dayDTO)
    }


    // Timings section
    override fun onMarkAsClosedItemClick(position: Int, dayDTO: DayDTO) {
        timingViewModel.onMarkAsClosedItemClick(position, dayDTO)
    }

    override fun onTimingRemoveItemClick(
        position: Int,
        mainContainerPosition: Int,
        timingDTO: TimingDTO
    ) {
        timingViewModel.onTimingRemoveItemClick(position, mainContainerPosition, timingDTO)
    }

    // Closed Dates
    override fun onRemoveItemClick(position: Int, closedDatesDTO: ClosedDatesDTO) {
        closedDatesViewModel.onRemoveItem(position, closedDatesDTO)
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
            if (resultCode == RESULT_OK) {
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