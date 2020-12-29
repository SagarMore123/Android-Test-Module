package com.astrika.checqk.view.dashboard.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.astrika.checqk.model.BookingDTO
import com.astrika.checqk.utils.CustomProgressBar
import com.astrika.checqk.utils.Utils
import com.astrika.checqk.view.dashboard.viewmodels.ReservedTableViewModel
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.ReservedTableAdapter
import com.sagar.logutil.databinding.FragmentReserveTableBinding

class ReserveTableFragment : Fragment(), ReservedTableAdapter.OnItemClickListener {


    private lateinit var binding: FragmentReserveTableBinding
    private lateinit var viewModel: ReservedTableViewModel
    lateinit var reservedTableAdapter: ReservedTableAdapter
    lateinit var mContext: Context
    private var progressBar = CustomProgressBar()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container != null) {
            mContext = container.context
        }
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_reserve_table,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            ReservedTableViewModel::class.java,
            this,
            binding.root
        )!!

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reservedTableAdapter = ReservedTableAdapter(requireContext(), this)
        binding.reserveTablesRecyclerView.adapter = reservedTableAdapter

        viewModel.fetchReservedTableListing()
        observers()

    }

    private fun observers() {

        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it)
                progressBar.show(requireContext(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })

        viewModel.tableLiveList.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {

            }
            //set the data to recycler view
            /*viewModel.noDataFoundVisibility.set(true)
        } else {
            viewModel.noDataFoundVisibility.set(false)
        }*/
            reservedTableAdapter.setTableList(it)
        })

        //VACANT(green)
        viewModel.vacantTableArrayListMutableLiveData.observe(requireActivity(), Observer {
            reservedTableAdapter.vacantTableArrayList(it)
        })

        //OCCUPIED(blue)
        viewModel.occupiedTableArrayListMutableLiveData.observe(requireActivity(), Observer {
            reservedTableAdapter.occupiedTableArrayList(it)
        })

        //RESERVED(red)
        viewModel.reservedTableArrayListMutableLiveData.observe(requireActivity(), Observer {
            reservedTableAdapter.reservedTableArrayList(it)
        })

        viewModel.tableManagementListLiveData.observe(requireActivity(), Observer {
            reservedTableAdapter.getTableSetup(it)
        })

    }

    override fun onItemClick(bookingDTO: BookingDTO) {
        //service call for assigning the table
        bookingDTO.bookingId.let {
            viewModel.assignTableServiceCall(bookingDTO.bookingId)
        }
    }

}