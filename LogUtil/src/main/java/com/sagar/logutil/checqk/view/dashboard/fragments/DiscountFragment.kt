package com.astrika.checqk.view.dashboard.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.astrika.checqk.model.DayDTO
import com.astrika.checqk.model.DiscountDaysTimingDTO
import com.astrika.checqk.model.SystemValueMasterDTO
import com.astrika.checqk.model.discount.DiscountCategoryDTO
import com.astrika.checqk.model.discount.OutletDiscountMembershipPlanDTO
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.Utils
import com.astrika.checqk.view.AddDiscountTimingDialogActivity
import com.astrika.checqk.view.discount.viewmodels.DiscountViewModel
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.DaysAdapter
import com.sagar.logutil.checqk.adapters.discount.*
import com.sagar.logutil.databinding.FragmentDiscountBinding

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DiscountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiscountFragment : Fragment(),
    DiscountCategoriesAdapter.OnItemClickListener,
    DiscountMembershipPlanAdapter.OnItemClickListener,
    MembershipHolderAdapter.OnItemClickListener,
    MembershipTypeAdapter.OnItemClickListener,
    DaysAdapter.OnItemClickListener,
    DiscountDaysAdapter.OnAddTimeSlotItemClick,
    DiscountDaysAdapter.OnTimingItemEditClickListener,
    DiscountDaysAdapter.OnTimingItemDeleteClickListener {

    private lateinit var binding: FragmentDiscountBinding
    private lateinit var discountViewModel: DiscountViewModel
    private lateinit var discountCategoriesAdapter: DiscountCategoriesAdapter
    private lateinit var discountMembershipPlanAdapter: DiscountMembershipPlanAdapter
    private lateinit var discountMembershipHolderAdapter: MembershipHolderAdapter
    private lateinit var discountMembershipTypeAdapter: MembershipTypeAdapter
    private lateinit var daysAdapter: DaysAdapter
    private lateinit var discountDaysAdapter: DiscountDaysAdapter

    private var editTimingPosition = 0
    private var editMainContainerPosition = 0
    private var isEdit = false

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
            R.layout.fragment_discount,
            container,
            false
        )

        discountViewModel = Utils.obtainBaseObservable(
            requireActivity(),
            DiscountViewModel::class.java,
            this,
            binding.root
        )!!
        binding.discountViewModel = discountViewModel
        binding.lifecycleOwner = this

        discountCategoriesAdapter = DiscountCategoriesAdapter(requireActivity(), this)
        binding.discountCategoryRecyclerView.adapter = discountCategoriesAdapter

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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DiscountFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DiscountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun observer() {

        discountViewModel.discountCategoryListMutableLiveData.observe(requireActivity(), Observer {
            discountCategoriesAdapter.list = it
        })

        discountViewModel.discountMembershipPlanListMutableLiveData.observe(
            requireActivity(),
            Observer {
                discountMembershipPlanAdapter.list = it
            })

        discountViewModel.discountMembershipHolderListMutableLiveData.observe(
            requireActivity(),
            Observer {
                discountMembershipHolderAdapter.submitList(it)
            })

        discountViewModel.discountMembershipTypeListMutableLiveData.observe(
            requireActivity(),
            Observer {
                discountMembershipTypeAdapter.submitList(it)
            })

        discountViewModel.daysListMutableLiveData.observe(requireActivity(), Observer {
            daysAdapter.submitList(it)
        })


        discountViewModel.discountDayTimingsListMutableLiveData.observe(
            requireActivity(),
            Observer {
                discountDaysAdapter.submitList(it)
            })

    }

    override fun onItemClick(position: Int, discountCategoryDTO: DiscountCategoryDTO) {
        discountViewModel.categorySelection(position, discountCategoryDTO)
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
        discountViewModel.membershipHolderSelection(position, systemValueMasterDTO)
    }

    override fun onItemClick(position: Int, systemValueMasterDTO: SystemValueMasterDTO) {
        discountViewModel.membershipTypeSelection(position, systemValueMasterDTO)
    }


    override fun onDayItemClick(position: Int, dayDTO: DayDTO) {
        discountViewModel.onDayItemClick(position, dayDTO)
    }

    override fun onAddTimeSlotItemClick(position: Int, dayDTO: DayDTO) {
        isEdit = false
        editMainContainerPosition = position
        val requestCode = Constants.DISCOUNT_TIMING_CODE
        val intent = Intent(activity, AddDiscountTimingDialogActivity::class.java)
        intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
        intent.putExtra(Constants.WEEK_NAME_KEY, dayDTO.dayName)
        this.startActivityForResult(intent, requestCode)
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
        intent.putExtra(Constants.DISCOUNT_TIMING_DTO_KEY, discountDaysTimingDTO)
        intent.putExtra(
            Constants.WEEK_NAME_KEY,
            discountViewModel.discountDayTimingsArrayList[mainContainerPosition].dayName
        )
        this.startActivityForResult(intent, requestCode)

    }


    override fun onTimingDeleteItemClick(
        position: Int,
        mainContainerPosition: Int,
        discountDaysTimingDTO: DiscountDaysTimingDTO
    ) {
        discountViewModel.deleteTimings(position, mainContainerPosition, discountDaysTimingDTO)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Constants.DISCOUNT_TIMING_CODE) {
            val discountDaysTimingDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as DiscountDaysTimingDTO

            discountViewModel.updateTiming(
                isEdit,
                editTimingPosition,
                editMainContainerPosition,
                discountDaysTimingDTO
            )

        }
    }


}