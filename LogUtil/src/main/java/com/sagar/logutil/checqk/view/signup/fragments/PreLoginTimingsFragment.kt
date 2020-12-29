package com.astrika.checqk.view.signup.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.astrika.checqk.model.DayDTO
import com.astrika.checqk.model.TimingDTO
import com.astrika.checqk.utils.CustomProgressBar
import com.astrika.checqk.utils.Utils
import com.astrika.checqk.view.signup.SignUp
import com.astrika.checqk.view.signup.viewmodels.TimingViewModel
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.DaysAdapter
import com.sagar.logutil.checqk.adapters.TimingsAdapter
import com.sagar.logutil.databinding.FragmentPreLoginTimingsBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class PreLoginTimingsFragment : Fragment(),
    DaysAdapter.OnItemClickListener,
    TimingsAdapter.OnItemClickListener,
    TimingsAdapter.OnTimingItemClickListener {

    private lateinit var binding: FragmentPreLoginTimingsBinding
    private lateinit var viewModel: TimingViewModel
    private lateinit var daysAdapter: DaysAdapter
    private lateinit var timingsAdapter: TimingsAdapter

    var progressBar = CustomProgressBar()
    private lateinit var signUp: SignUp

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        signUp = activity as SignUp
        signUp.fragmentNo(4)

        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_pre_login_timings, container, false)

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            TimingViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel

        binding.lifecycleOwner = this


        daysAdapter = DaysAdapter(requireActivity(), this)
        binding.daysRecyclerView.adapter = daysAdapter

        timingsAdapter = TimingsAdapter(requireActivity(), this, this)
        binding.timingsRecyclerView.adapter = timingsAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showProgressBar.observe(requireActivity(), Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })


        viewModel.navToNext.observe(requireActivity(), Observer {
            if (it) {
                findNavController().navigate(R.id.action_PreLoginTimingsFragment_to_PreLoginGalleryAndMenuFragment)
            }
        })


        viewModel.daysListMutableLiveData.observe(requireActivity(), Observer {
            daysAdapter.submitList(it)
        })

        viewModel.timingListMutableLiveData.observe(requireActivity(), Observer {
            timingsAdapter.arrayList = it
        })

    }


    override fun onDayItemClick(position: Int, dayDTO: DayDTO) {
        viewModel.onDayItemClick(position, dayDTO)
    }


    // Timings section
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

}