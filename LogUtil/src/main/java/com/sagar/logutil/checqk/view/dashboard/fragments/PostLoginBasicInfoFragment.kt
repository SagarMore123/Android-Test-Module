package com.sagar.logutil.checqk.view.dashboard.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.StringTagsAdapter
import com.sagar.logutil.checqk.adapters.basicinfo.*
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.utils.*
import com.sagar.logutil.checqk.view.dashboard.viewmodels.BasicInfoViewModel
import com.sagar.logutil.databinding.BasicInfoFacilityMastersItemCellLayoutBinding
import com.sagar.logutil.databinding.FragmentPostLoginBasicInfoBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class PostLoginBasicInfoFragment : Fragment(),
    OutletTypeAdapter.OnItemClickListener,
    SeatingTypeAdapter.OnItemClickListener,
    FoodTypeAdapter.OnItemClickListener,
    MealTypeAdapter.OnItemClickListener,
    CuisinesAdapter.OnItemClickListener,
    BooleanAdapter.OnItemClickListener,
    FacilitiesAdapter.OnItemClickListener,
    StringTagsAdapter.OnRemoveItemClickListener,
    FamousDishesAdapter.OnRemoveItemClickListener {


    private lateinit var binding: FragmentPostLoginBasicInfoBinding
    private lateinit var basicInfoViewModel: BasicInfoViewModel
    private lateinit var outletTypeAdapter: OutletTypeAdapter
    private lateinit var seatingTypeAdapter: SeatingTypeAdapter
    private lateinit var foodTypeAdapter: FoodTypeAdapter
    private lateinit var mealTypeAdapter: MealTypeAdapter
    private lateinit var cuisinesAdapter: CuisinesAdapter
    private lateinit var facilitiesAdapter: FacilitiesAdapter
    private lateinit var famousDishesAdapter: FamousDishesAdapter
    private lateinit var stringTagsAdapter: StringTagsAdapter

    private lateinit var inMallBooleanAdapter: BooleanAdapter
    private lateinit var alcoholServedBooleanAdapter: BooleanAdapter

    private var progressBar = CustomProgressBar()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_post_login_basic_info,
            container,
            false
        )

        basicInfoViewModel = Utils.obtainBaseObservable(
            requireActivity(),
            BasicInfoViewModel::class.java,
            this,
            binding.root
        )!!
        binding.basicInfoViewModel = basicInfoViewModel
        binding.lifecycleOwner = this

        initAdapters()
        observer()
        textWatcher()

        return binding.root
    }

    fun observer() {

        basicInfoViewModel.showProgressBar.observe(requireActivity(), Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })


        basicInfoViewModel.getmOnCategoryClick().observeChange(this, Observer {
            val intent = Intent(activity, AutocompleteViewActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, Constants.CUISINE_CODE)
            this.startActivityForResult(intent, Constants.CUISINE_CODE)
        })
    }

    private fun initAdapters() {

        // Outlet Type
        outletTypeAdapter = OutletTypeAdapter(requireActivity(), this)
        binding.outletTypeRecyclerView.adapter = outletTypeAdapter

        // Seating Type
        seatingTypeAdapter = SeatingTypeAdapter(requireActivity(), this)
        binding.seatingTypeRecyclerView.adapter = seatingTypeAdapter

        // In Mall
        inMallBooleanAdapter = BooleanAdapter(requireActivity(), this, true)
        binding.inMallTypeRecyclerView.adapter = inMallBooleanAdapter

        // Alcohol Served ?
        alcoholServedBooleanAdapter = BooleanAdapter(requireActivity(), this, false)
        binding.servedAlcoholRecyclerView.adapter = alcoholServedBooleanAdapter

        // Food Type
        foodTypeAdapter = FoodTypeAdapter(requireActivity(), this)
        binding.foodTypeRecyclerView.adapter = foodTypeAdapter

        // Meal Type
        mealTypeAdapter = MealTypeAdapter(requireActivity(), this)
        binding.mealTypeRecyclerView.adapter = mealTypeAdapter

        // Cuisine
        cuisinesAdapter = CuisinesAdapter(requireActivity(), this)
        binding.cuisineRecyclerView.adapter = cuisinesAdapter

        // Facility
        facilitiesAdapter = FacilitiesAdapter(requireActivity(), this)
        binding.facilityRecyclerView.adapter = facilitiesAdapter

        // Famous Dishes
        famousDishesAdapter = FamousDishesAdapter(requireActivity(), this)
        binding.famousDishesRecyclerView.adapter = famousDishesAdapter

        // Famous For
        stringTagsAdapter =
            StringTagsAdapter(requireActivity(), Constants.FAMOUS_FOR_STRING_ADAPTER, this)
        binding.famousForRecyclerView.adapter = stringTagsAdapter


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Outlet type
        basicInfoViewModel.outletTypeListMutableLiveData.observe(requireActivity(), Observer {
            outletTypeAdapter.submitList(it)
        })

        // Seating type
        basicInfoViewModel.seatingTypeListMutableLiveData.observe(requireActivity(), Observer {
            seatingTypeAdapter.submitList(it)
        })

        // Food type
        basicInfoViewModel.foodTypeListMutableLiveData.observe(requireActivity(), Observer {
            foodTypeAdapter.submitList(it)
        })

        // Meal type
        basicInfoViewModel.mealTypeListMutableLiveData.observe(requireActivity(), Observer {
            mealTypeAdapter.submitList(it)
        })

        // In Mall
        basicInfoViewModel.inMallListMutableLiveData.observe(requireActivity(), Observer {
            inMallBooleanAdapter.submitList(it)
        })

        // Alcohol Served
        basicInfoViewModel.alcoholServedListMutableLiveData.observe(requireActivity(), Observer {
            alcoholServedBooleanAdapter.submitList(it)
        })

        // Cuisine
        basicInfoViewModel.cuisineListMutableLiveData.observe(requireActivity(), Observer {
            cuisinesAdapter.submitList(it)
        })

        // Facility
        basicInfoViewModel.facilityListMutableLiveData.observe(requireActivity(), Observer {
            facilitiesAdapter.submitList(it)
        })

        // Famous Dishes
        basicInfoViewModel.famousDishListMutableLiveData.observe(requireActivity(), Observer {
            famousDishesAdapter.submitList(it)
        })

        // Famous For
        basicInfoViewModel.famousForListMutableLiveData.observe(requireActivity(), Observer {
            stringTagsAdapter.list = it
        })


        basicInfoViewModel.cuisineNameSearch.observe(requireActivity(), Observer {

            if (it?.length!! >= 1) {
                basicInfoViewModel.searchByText(it)
                basicInfoViewModel.clearCuisineNameSearchVisible.set(true)
            } else if (it.isEmpty()) {
                basicInfoViewModel.cuisineListMutableLiveData.value =
                    basicInfoViewModel.cuisineArrayList
                basicInfoViewModel.clearCuisineNameSearchVisible.set(false)
            }
        })

    }

    private fun textWatcher() {
        val inputFilter = arrayOf<InputFilter>(MoneyValueFilter())
        binding.costForTwoEt.filters = inputFilter

        binding.famousForEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (s.trim().isNotEmpty() && s.endsWith(",") && count > before) {
                    basicInfoViewModel.splitFamousForString(s.trim().toString())
                    binding.famousForEt.setText("")
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        binding.famousForEt.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                basicInfoViewModel.splitFamousForString(binding.famousForEt.text.toString().trim())
                binding.famousForEt.setText("")
            }
            false
        }


    }


    override fun onItemClick(position: Int, outletTypeMasterDTO: OutletTypeMasterDTO) {
        basicInfoViewModel.outletTypeArrayList[position] = outletTypeMasterDTO
        basicInfoViewModel.outletTypeListMutableLiveData.value =
            basicInfoViewModel.outletTypeArrayList
    }

    override fun onItemClick(position: Int, systemValueMasterDTO: SystemValueMasterDTO) {
        basicInfoViewModel.seatingTypeArrayList[position] = systemValueMasterDTO
        basicInfoViewModel.seatingTypeListMutableLiveData.value =
            basicInfoViewModel.seatingTypeArrayList
    }

    override fun onItemClick(position: Int, foodTypeMasterDTO: FoodTypeMasterDTO) {
        basicInfoViewModel.foodTypeArrayList[position] = foodTypeMasterDTO
        basicInfoViewModel.foodTypeListMutableLiveData.value = basicInfoViewModel.foodTypeArrayList

    }

    override fun onItemClick(position: Int, mealTypeMasterDTO: MealTypeMasterDTO) {
        basicInfoViewModel.mealTypeArrayList[position] = mealTypeMasterDTO
        basicInfoViewModel.mealTypeListMutableLiveData.value = basicInfoViewModel.mealTypeArrayList
    }

    override fun onItemClick(position: Int, cuisineMasterDTO: CuisineMasterDTO) {
        basicInfoViewModel.cuisineArrayList[position].selected = cuisineMasterDTO.selected
        basicInfoViewModel.cuisineListMutableLiveData.value = basicInfoViewModel.cuisineArrayList
    }

    override fun onItemClick(
        position: Int,
        facilityMasterDTO: FacilityMasterDTO,
        binding: BasicInfoFacilityMastersItemCellLayoutBinding
    ) {
        basicInfoViewModel.facilityArrayList[position] = facilityMasterDTO
        basicInfoViewModel.facilityListMutableLiveData.value = basicInfoViewModel.facilityArrayList


        if (basicInfoViewModel.facilityBindingList.size == 0) {
            basicInfoViewModel.facilityBindingList.add(binding)
        } else {

            var isBindingPresent = false
            for (item in basicInfoViewModel.facilityBindingList) {

                if (item.facilityMasterDTO?.facilityId == binding.facilityMasterDTO?.facilityId) {
                    item.facilityMasterDTO = binding.facilityMasterDTO
                    isBindingPresent = true
                }
            }

            if (!isBindingPresent) {
                basicInfoViewModel.facilityBindingList.add(binding)
            }
        }


    }

    override fun onRemoveItemClick(position: Int, cuisineMasterDTO: FamousDishesDTO) {
        basicInfoViewModel.onRemoveFamousDishesItem(position)
    }


    override fun onStringRemoveItemClick(position: Int, adapterType: String, string: String) {
        basicInfoViewModel.onRemoveFamousForItem(position, adapterType)
    }

    override fun onItemClick(position: Int, isInMall: Boolean, booleanMasterDTO: BooleanMasterDTO) {

        if (isInMall) {
            for ((i, item) in basicInfoViewModel.inMallArrayList.withIndex()) {
                if (i == position) {
                    item.selected = booleanMasterDTO.selected
                } else {
                    item.selected = !booleanMasterDTO.selected
                }
            }
            basicInfoViewModel.inMallListMutableLiveData.value = basicInfoViewModel.inMallArrayList
        } else {
            for ((i, item) in basicInfoViewModel.alcoholServedArrayList.withIndex()) {
                if (i == position) {
                    item.selected = booleanMasterDTO.selected
                } else {
                    item.selected = !booleanMasterDTO.selected
                }
            }
            basicInfoViewModel.alcoholServedListMutableLiveData.value =
                basicInfoViewModel.alcoholServedArrayList
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Constants.CUISINE_CODE) {
            val cuisineMasterDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as CuisineMasterDTO
            basicInfoViewModel.dishCategoryId = cuisineMasterDTO.cuisineId
            basicInfoViewModel.dishCategoryValue.value = cuisineMasterDTO.cuisineName

        }
    }


}