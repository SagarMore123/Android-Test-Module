package com.sagar.logutil.checqk.view.discount.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.discount.DiscountDaysAdapter
import com.sagar.logutil.checqk.adapters.discount.InHouseMembershipHolderAdapter
import com.sagar.logutil.checqk.adapters.discount.MembershipTypeAdapter
import com.sagar.logutil.checqk.model.DayDTO
import com.sagar.logutil.checqk.model.DiscountDaysTimingDTO
import com.sagar.logutil.checqk.model.SystemValueMasterDTO
import com.sagar.logutil.checqk.model.discount.InHouseMembershipHolderDTO
import com.sagar.logutil.checqk.model.discount.OutletDiscountDetailsDTO
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.CustomProgressBar
import com.sagar.logutil.checqk.utils.Utils
import com.sagar.logutil.checqk.view.discount.viewmodels.InHouseDiscountViewModel
import com.sagar.logutil.databinding.FragmentInHouseCardsBinding

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InHouseCardsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InHouseCardsFragment : Fragment(),
    InHouseMembershipHolderAdapter.OnItemClickListener,
    MembershipTypeAdapter.OnItemClickListener,
    DiscountDaysAdapter.OnAddTimeSlotItemClick,
    DiscountDaysAdapter.OnTimingItemEditClickListener,
    DiscountDaysAdapter.OnTimingItemDeleteClickListener {

    private lateinit var binding: FragmentInHouseCardsBinding
    private lateinit var viewModel: InHouseDiscountViewModel

    private lateinit var discountMembershipHolderAdapter: InHouseMembershipHolderAdapter
    private lateinit var discountMembershipTypeAdapter: MembershipTypeAdapter
    private lateinit var discountDaysAdapter: DiscountDaysAdapter
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
            R.layout.fragment_in_house_cards,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            InHouseDiscountViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        discountMembershipHolderAdapter = InHouseMembershipHolderAdapter(requireActivity(), this)
        binding.discountMembershipHolderRecyclerView.adapter = discountMembershipHolderAdapter

        discountMembershipTypeAdapter = MembershipTypeAdapter(requireActivity(), this)
        binding.discountMembershipTypeRecyclerView.adapter = discountMembershipTypeAdapter

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


        viewModel.discountMembershipHolderListMutableLiveData.observe(
            requireActivity(), Observer {
                discountMembershipHolderAdapter.submitList(it)
            })

        viewModel.discountMembershipTypeListMutableLiveData.observe(
            requireActivity(), Observer {
                discountMembershipTypeAdapter.submitList(it)
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

    override fun onAddTimeSlotItemClick(position: Int, dayDTO: DayDTO) {

    }

    override fun onTimingDeleteItemClick(
        position: Int,
        mainContainerPosition: Int,
        discountDaysTimingDTO: DiscountDaysTimingDTO
    ) {

    }

    override fun onTimingItemEditClick(
        position: Int,
        mainContainerPosition: Int,
        discountDaysTimingDTO: DiscountDaysTimingDTO
    ) {
    }


    override fun onItemClick(position: Int, dto: InHouseMembershipHolderDTO) {
        viewModel.membershipHolderSelection(position)
    }


    override fun onItemClick(position: Int, systemValueMasterDTO: SystemValueMasterDTO) {
        viewModel.membershipTypeSelection(position)
    }


}