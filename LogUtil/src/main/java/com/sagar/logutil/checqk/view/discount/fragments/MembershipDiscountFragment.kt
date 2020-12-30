package com.sagar.logutil.checqk.view.discount.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.DaysAdapter
import com.sagar.logutil.checqk.adapters.discount.DiscountDaysAdapter
import com.sagar.logutil.checqk.adapters.discount.DiscountMembershipPlanAdapter
import com.sagar.logutil.checqk.adapters.discount.MembershipHolderAdapter
import com.sagar.logutil.checqk.adapters.discount.MembershipTypeAdapter
import com.sagar.logutil.checqk.model.DayDTO
import com.sagar.logutil.checqk.model.DiscountDaysTimingDTO
import com.sagar.logutil.checqk.model.SystemValueMasterDTO
import com.sagar.logutil.checqk.model.discount.OutletDiscountDetailsDTO
import com.sagar.logutil.checqk.model.discount.OutletDiscountMembershipPlanDTO
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.CustomProgressBar
import com.sagar.logutil.checqk.utils.Utils
import com.sagar.logutil.checqk.view.AddDiscountTimingDialogActivity
import com.sagar.logutil.checqk.view.discount.viewmodels.MembershipDiscountViewModel
import com.sagar.logutil.databinding.FragmentMembershipDiscountBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MembershipDiscountFragment : Fragment(),
    DiscountMembershipPlanAdapter.OnItemClickListener,
    MembershipHolderAdapter.OnItemClickListener,
    MembershipTypeAdapter.OnItemClickListener,
    DaysAdapter.OnItemClickListener,
    DiscountDaysAdapter.OnAddTimeSlotItemClick,
    DiscountDaysAdapter.OnTimingItemEditClickListener,
    DiscountDaysAdapter.OnTimingItemDeleteClickListener {

    private lateinit var binding: FragmentMembershipDiscountBinding
    private lateinit var viewModel: MembershipDiscountViewModel

    private lateinit var discountMembershipPlanAdapter: DiscountMembershipPlanAdapter
    private lateinit var discountMembershipHolderAdapter: MembershipHolderAdapter
    private lateinit var discountMembershipTypeAdapter: MembershipTypeAdapter
    private lateinit var daysAdapter: DaysAdapter
    private lateinit var discountDaysAdapter: DiscountDaysAdapter

    private var editTimingPosition = 0
    private var editMainContainerPosition = 0
    private var isEdit = false
    var progressBar = CustomProgressBar()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_membership_discount,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            MembershipDiscountViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        discountMembershipPlanAdapter = DiscountMembershipPlanAdapter(requireActivity(), this)
        binding.discountMembershipPlanRecyclerView.adapter = discountMembershipPlanAdapter

        discountMembershipHolderAdapter = MembershipHolderAdapter(requireActivity(), this)
        binding.discountMembershipHolderRecyclerView.adapter = discountMembershipHolderAdapter

        discountMembershipTypeAdapter = MembershipTypeAdapter(requireActivity(), this)
        binding.discountMembershipTypeRecyclerView.adapter = discountMembershipTypeAdapter

        daysAdapter = DaysAdapter(requireActivity(), this)
        binding.weekDaysRecyclerView.adapter = daysAdapter

        discountDaysAdapter = DiscountDaysAdapter(requireActivity(), this, this, this)
        binding.discountDaysRecyclerView.adapter = discountDaysAdapter

        observer()

        return binding.root
    }

    fun observer() {

        viewModel.showProgressBar.observe(requireActivity(), Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })

        viewModel.discountMembershipPlanListMutableLiveData.observe(
            requireActivity(), Observer {
                discountMembershipPlanAdapter.list = it
            })

        viewModel.discountMembershipHolderListMutableLiveData.observe(
            requireActivity(), Observer {
                discountMembershipHolderAdapter.submitList(it)
            })

        viewModel.discountMembershipTypeListMutableLiveData.observe(
            requireActivity(), Observer {
                discountMembershipTypeAdapter.submitList(it)
            })

        viewModel.daysListMutableLiveData.observe(requireActivity(), Observer {
            daysAdapter.submitList(it)
        })


        viewModel.discountDayTimingsListMutableLiveData.observe(
            requireActivity(), Observer {
                discountDaysAdapter.submitList(it)
            })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments?.getSerializable(Constants.OUTLET_DISCOUNT_DETAILS_DTO_KEY) != null) {
            viewModel.outletDiscountDetailsArrayList =
                arguments?.getSerializable(Constants.OUTLET_DISCOUNT_DETAILS_DTO_KEY) as ArrayList<OutletDiscountDetailsDTO>
        }

    }

    override fun onItemClick(
        position: Int,
        outletDiscountMembershipPlanDTO: OutletDiscountMembershipPlanDTO
    ) {

    }

    override fun onMembershipHolderItemClick(
        position: Int,
        systemValueMasterDTO: SystemValueMasterDTO
    ) {
        viewModel.membershipHolderSelection(position, systemValueMasterDTO)
    }

    override fun onItemClick(position: Int, systemValueMasterDTO: SystemValueMasterDTO) {
        viewModel.membershipTypeSelection(position, systemValueMasterDTO)
    }


    override fun onDayItemClick(position: Int, dayDTO: DayDTO) {
        viewModel.onDayItemClick(position, dayDTO)
    }

    override fun onAddTimeSlotItemClick(position: Int, dayDTO: DayDTO) {

        if (!dayDTO.assuredDiscountString.isNullOrBlank()) {
            viewModel.discountDayTimingsArrayList[position].assuredDiscountErrorMsg = ""

            isEdit = false
            editMainContainerPosition = position
            val requestCode = Constants.DISCOUNT_TIMING_CODE

            val intent = Intent(activity, AddDiscountTimingDialogActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
            intent.putExtra(
                Constants.DISCOUNT_PLAN_ELIGIBLE_NAME_KEY,
                viewModel.outletDiscountMembershipPlanDTO.membershipName ?: ""
            )
            intent.putExtra(Constants.WEEK_NAME_KEY, dayDTO.dayName)

            val discountDaysTimingDTO = DiscountDaysTimingDTO()
            discountDaysTimingDTO.weekDay = dayDTO.dayId
            discountDaysTimingDTO.assuredDiscount = dayDTO.assuredDiscountString.toLong()
            intent.putExtra(Constants.DISCOUNT_TIMING_DTO_KEY, discountDaysTimingDTO)

            this.startActivityForResult(intent, requestCode)

            viewModel.discountDayTimingsArrayList[position].assuredDiscountErrorMsg = ""
            viewModel.discountDayTimingsListMutableLiveData.value =
                viewModel.discountDayTimingsArrayList

        } else {

            viewModel.discountDayTimingsArrayList[position].assuredDiscountErrorMsg =
                "Please enter assured amount"
            viewModel.discountDayTimingsListMutableLiveData.value =
                viewModel.discountDayTimingsArrayList
        }
    }


    override fun onTimingItemEditClick(
        position: Int,
        mainContainerPosition: Int,
        discountDaysTimingDTO: DiscountDaysTimingDTO
    ) {
        isEdit = true
        editTimingPosition = position
        editMainContainerPosition = mainContainerPosition

        val requestCode = Constants.DISCOUNT_TIMING_CODE
        val intent = Intent(activity, AddDiscountTimingDialogActivity::class.java)
        intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)

        if (!viewModel.discountDayTimingsArrayList[mainContainerPosition].allDaySameDiscountFlag
            && !viewModel.discountDayTimingsArrayList[mainContainerPosition].assuredDiscountString.isNullOrBlank()
        ) {
            discountDaysTimingDTO.assuredDiscount =
                viewModel.discountDayTimingsArrayList[mainContainerPosition].assuredDiscountString.toLong()
        }
        intent.putExtra(Constants.DISCOUNT_TIMING_DTO_KEY, discountDaysTimingDTO)
        intent.putExtra(
            Constants.DISCOUNT_PLAN_ELIGIBLE_NAME_KEY,
            viewModel.outletDiscountMembershipPlanDTO.membershipName ?: ""
        )

        intent.putExtra(
            Constants.WEEK_NAME_KEY,
            viewModel.discountDayTimingsArrayList[mainContainerPosition].dayName
        )
        this.startActivityForResult(intent, requestCode)

    }


    override fun onTimingDeleteItemClick(
        position: Int,
        mainContainerPosition: Int,
        discountDaysTimingDTO: DiscountDaysTimingDTO
    ) {
        viewModel.deleteTimings(position, mainContainerPosition, discountDaysTimingDTO)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Constants.DISCOUNT_TIMING_CODE) {
            val discountDaysTimingDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as DiscountDaysTimingDTO

            viewModel.addOrEditWeekDayTiming(
                isEdit,
                editTimingPosition,
                editMainContainerPosition,
                discountDaysTimingDTO
            )

        }
    }

}