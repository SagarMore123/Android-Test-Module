package com.sagar.logutil.checqk.view.dashboard.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.TypeOfTableAdapter
import com.sagar.logutil.checqk.model.SystemValueMasterDTO
import com.sagar.logutil.checqk.utils.CustomProgressBar
import com.sagar.logutil.checqk.utils.Utils
import com.sagar.logutil.checqk.view.dashboard.viewmodels.TableConfigViewModel
import com.sagar.logutil.databinding.FragmentTableConfigurationBinding

class TableConfigurationFragment : Fragment(),TypeOfTableAdapter.OnItemClickListener {

    lateinit var binding : FragmentTableConfigurationBinding
    lateinit var viewModel : TableConfigViewModel
    private var progressBar = CustomProgressBar()
    private lateinit var typeOfTableAdapter: TypeOfTableAdapter
    private lateinit var mContext:Context
    var absX = 0
    var absY = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentTableConfigurationBinding>(
            inflater, R.layout.fragment_table_configuration,
            container,
            false)

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            TableConfigViewModel::class.java,
            this,
            binding.root
        )!!
        mContext = container?.context!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.tableSetupLayout = binding.tableSetupLayout
        initAdapter()
        observers()

        return binding.root

    }

    private fun initAdapter(){
        typeOfTableAdapter = TypeOfTableAdapter(requireActivity(), this)
        binding.seaterRecyclerView.setHasFixedSize(true)
        binding.seaterRecyclerView.adapter = typeOfTableAdapter

    }

    private fun observers() {
        viewModel.typeOfTableListMutableLiveData.observe(requireActivity(), Observer {
            typeOfTableAdapter.submitList(it)
        })

        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it)
                progressBar.show(mContext, "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })

    }

    override fun onItemClick(position: Int,systemValueMasterDTO: SystemValueMasterDTO) {
        //open a dialog
        viewModel.openDialog(mContext,systemValueMasterDTO)
    }



}