package com.sagar.logutil.checqk.view.dashboard.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.*
import com.sagar.logutil.checqk.adapters.basicinfo.CuisinesAdapter
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.CustomProgressBar
import com.sagar.logutil.checqk.utils.Utils
import com.sagar.logutil.checqk.view.dashboard.viewmodels.AddDishDialogViewModel
import com.sagar.logutil.databinding.FragmentAddDishDialogBinding

class AddDishDialogFragment : DialogFragment(),
    DishMenuCategoryDropDownAdapter.OnMenuCategoryClickListener,
    DishMenuSectionsDropDownAdapter.OnMenuSectionClickListener,
    CuisinesAdapter.OnItemClickListener,
    DaysAdapter.OnItemClickListener,
    TimingsAdapter.OnItemClickListener,
    TimingsAdapter.OnTimingItemClickListener {

    private lateinit var binding: FragmentAddDishDialogBinding
    private lateinit var viewModel: AddDishDialogViewModel

    private lateinit var menuCategoryAdapter: DishMenuCategoryDropDownAdapter
    private lateinit var menuSectionAdapter: DishMenuSectionsDropDownAdapter

    private lateinit var dishFlagAdapter: DishFlagAdapter
    private lateinit var cuisinesAdapter: CuisinesAdapter
    private lateinit var daysAdapter: DaysAdapter
    private lateinit var timingsAdapter: TimingsAdapter

    private var progressBar = CustomProgressBar()

    private var itemPos = 0

    companion object {

        const val TAG = "AddDishDialogFragment"
        var saveFlag = false
        private lateinit var listener: AddDishFragmentListener

        private const val KEY_MENU_CATEGORY_ID = "KEY_MENU_CATEGORY_ID"
        private const val KEY_MENU_SECTION_ID = "KEY_MENU_SECTION_ID"
        private const val KEY_DISH_ID = "KEY_DISH_ID"

        fun newInstance(
            catalogueCategoryId: Long, catalogueSectionId: Long, productId: Long,
            listener: AddDishFragmentListener
        ): AddDishDialogFragment {
            val args = Bundle()
            args.putLong(KEY_MENU_CATEGORY_ID, catalogueCategoryId)
            args.putLong(KEY_MENU_SECTION_ID, catalogueSectionId)
            args.putLong(KEY_DISH_ID, productId)
            val fragment = AddDishDialogFragment()
            fragment.arguments = args
            this.listener = listener
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_dish_dialog, container, false)

//        dialog?.window?.setBackgroundDrawableResource(R.drawable.circular_rounded_corner_white_background)
//        getDialog()!!.getWindow().setBackgroundDrawableResource(R.drawable.circular_rounded_corner_white_background)

        saveFlag = false
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_dish_dialog,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            AddDishDialogViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initAdapters()
        observers()

        if (requireArguments().containsKey(KEY_MENU_CATEGORY_ID) && !requireArguments().getLong(
                KEY_MENU_CATEGORY_ID
            ).equals(0L)
        ) {
            viewModel.catalogueCategoryId = requireArguments().getLong(KEY_MENU_CATEGORY_ID)
        }
        if (requireArguments().containsKey(KEY_MENU_SECTION_ID) && !requireArguments().getLong(
                KEY_MENU_SECTION_ID
            ).equals(0L)
        ) {
            viewModel.catalogueSectionId = requireArguments().getLong(KEY_MENU_SECTION_ID)
        }

        if (requireArguments().containsKey(KEY_DISH_ID) && !requireArguments().getLong(KEY_DISH_ID)
                .equals(0L)
        ) {
            viewModel.productId = requireArguments().getLong(KEY_DISH_ID)
//            viewModel.getDishDetails()
        }

        return binding.root
    }

    fun initAdapters() {

        //Dish Flag
        dishFlagAdapter = DishFlagAdapter(requireActivity())
        binding.dishFlagRecyclerView.adapter = dishFlagAdapter

        //Menu Category
        menuCategoryAdapter = DishMenuCategoryDropDownAdapter(requireActivity(), this)
        binding.menuCategoryRecyclerView.adapter = menuCategoryAdapter

        //Menu Section
        menuSectionAdapter = DishMenuSectionsDropDownAdapter(requireActivity(), this)
        binding.menuSectionRecyclerView.adapter = menuSectionAdapter

        // Cuisine
        cuisinesAdapter = CuisinesAdapter(requireActivity(), this)
        binding.cuisineRecyclerView.adapter = cuisinesAdapter

        daysAdapter = DaysAdapter(requireActivity(), this)
        binding.daysRecyclerView.adapter = daysAdapter

        timingsAdapter = TimingsAdapter(requireActivity(), this, this)
        binding.timingsRecyclerView.adapter = timingsAdapter

    }

    fun observers() {
        viewModel.showProgressBar.observe(requireActivity(), Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })
        viewModel.dishFlagsMutableArrayList.observe(requireActivity(), Observer {
            it.let {
                dishFlagAdapter.submitList(it)
            }
        })

        viewModel.cuisineNameSearch.observe(requireActivity(), Observer {

            if (it?.length!! >= 1) {
                viewModel.searchByText(it)
                viewModel.clearCuisineNameSearchVisible.set(true)
            } else if (it.isEmpty()) {
                viewModel.cuisineListMutableLiveData.value =
                    viewModel.cuisineArrayList
                viewModel.clearCuisineNameSearchVisible.set(false)
            }
        })

        // Cuisine
        viewModel.cuisineListMutableLiveData.observe(requireActivity(), Observer {
            cuisinesAdapter.submitList(it)
        })

        viewModel.daysListMutableLiveData.observe(requireActivity(), Observer {
            daysAdapter.submitList(it)
        })

        viewModel.timingListMutableLiveData.observe(requireActivity(), Observer {
            timingsAdapter.arrayList = it
        })

        viewModel.menuCategoryMutableArrayList.observe(requireActivity(), Observer {
            it.let {
//                menuCategoryAdapter.setDishMenuCategoryList(it)
                if (viewModel.catalogueCategoryId != 0L) {
                    for (menuCategory in it) {
                        if (menuCategory.catalogueCategoryId!! == viewModel.catalogueCategoryId) {
                            menuCategory.isSelected = true
                            viewModel.selectedMenuCategoryArrayList.add(menuCategory)
                        }
                    }
                }
                viewModel.showCategorySelectedData()
                menuCategoryAdapter.setDishMenuCategoryList(it)
            }
        })

        viewModel.menuSectionMutableArrayList.observe(requireActivity(), Observer {
            it.let {
                if (viewModel.catalogueSectionId != 0L && viewModel.isFirstTimeSections) {
                    viewModel.isFirstTimeSections = false
                    for (menuSection in it?: arrayListOf()) {
                        if (menuSection.catalogueSectionId == viewModel.catalogueSectionId) {
                            menuSection.isSelected = true
                            viewModel.selectedMenuSectionArrayList.add(menuSection)
                        }
                    }
                }
                viewModel.showSectionSelectedData()
                menuSectionAdapter.setDishMenuSectionList(it)
//                isFirstTimeSections = false
            }
        })

        viewModel.closeClick.observe(requireActivity(), Observer {
            if (it) {
                listener.onCloseClick()
//                requireActivity().onBackPressed()
                /* saveFlag = false
                 childFragmentManager.findFragmentByTag( AddDishDialogFragment.TAG)?.let {
                     (it as DialogFragment).dismiss()
                 }*/
            }
        })
        viewModel.onSaveClose.observe(requireActivity(), Observer {
            if (it) {
                listener.onDishSave()
//                requireActivity().onBackPressed()
                /*saveFlag = true
                childFragmentManager.findFragmentByTag( AddDishDialogFragment.TAG)?.let {
                    (it as DialogFragment).dismiss()
                }*/
            }
        })
        viewModel.addPhotoClick.observe(requireActivity(), Observer {
            if (it) {
                context?.let { it1 -> activity?.let { it2 -> Utils.selectImages(it2, it1, this) } }
            }
        })

        /*viewModel.foodImageUrl.observe(requireActivity(), Observer {
            if (it !== null){
                binding.
            }
        })*/
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onItemClick(position: Int, cuisineMasterDTO: CuisineMasterDTO) {
        viewModel.cuisineArrayList[position].selected = cuisineMasterDTO.selected
        viewModel.cuisineListMutableLiveData.value = viewModel.cuisineArrayList
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

    override fun onMenuCategoryItemClick(position: Int, menuCategoryDetails: CategoryCatalogueDTO) {
        viewModel.menuCategoryRecyclerVisibility.set(false)

        if (menuCategoryDetails.isSelected) {
            viewModel.selectedMenuCategoryArrayList.add(menuCategoryDetails)
            viewModel.catalogueCategoryId = menuCategoryDetails.catalogueCategoryId!!
            viewModel.getAllMenuSections()
        } else {
            viewModel.selectedMenuCategoryArrayList.remove(menuCategoryDetails)
            var sectionList = ArrayList<CatalogueSectionDTO>()
            for (menuSection in viewModel.menuSectionList) {
                if (menuCategoryDetails.catalogueCategoryId != menuSection.catalogueCategoryId) {
                    sectionList.add(menuSection)
                }
            }
            viewModel.menuSectionList = sectionList
            viewModel.menuSectionMutableArrayList.value = viewModel.menuSectionList
        }
        viewModel.showCategorySelectedData()

        /*  if (viewModel.selectedMenuCategoryList.value.equals("")){
              viewModel.selectedMenuCategoryList.value =  menuCategoryDetails.menuCategoryName
              viewModel.catalogueCategoryId = menuCategoryDetails.catalogueCategoryId!!
              viewModel.selectedMenuCategoryIdList.value!!.add(menuCategoryDetails.catalogueCategoryId!!)
              viewModel.getAllMenuSections()
          }else if(!viewModel.selectedMenuCategoryList.value!!.contains(menuCategoryDetails.menuCategoryName!!)){
              viewModel.selectedMenuCategoryList.value = viewModel.selectedMenuCategoryList.value + ", " + menuCategoryDetails.menuCategoryName
              viewModel.catalogueCategoryId = menuCategoryDetails.catalogueCategoryId!!
              viewModel.selectedMenuCategoryIdList.value!!.add(menuCategoryDetails.catalogueCategoryId!!)
              viewModel.getAllMenuSections()
          }*/

        /*  if(viewModel.selectedMenuCategoryArrayList.isEmpty()){
              viewModel.selectedMenuCategoryArrayList.add(menuCategoryDetails)
              viewModel.catalogueCategoryId = menuCategoryDetails.catalogueCategoryId!!
              viewModel.getAllMenuSections()
          }else{
              if(menuCategoryDetails.isSelected){
                  viewModel.selectedMenuCategoryArrayList.add(menuCategoryDetails)
                  viewModel.catalogueCategoryId = menuCategoryDetails.catalogueCategoryId!!
              }else{
                  viewModel.selectedMenuCategoryArrayList.remove(menuCategoryDetails)
              }
          }
          showCategorySelectedData()*/

    }


    override fun onMenuSectionItemClick(position: Int, catalogueSection: CatalogueSectionDTO) {
        viewModel.menuSectionRecyclerVisibility.set(false)

        if (catalogueSection.isSelected) {
            viewModel.selectedMenuSectionArrayList.add(catalogueSection)
        } else {
            viewModel.selectedMenuSectionArrayList.remove(catalogueSection)
        }

        viewModel.showSectionSelectedData()

        /*if (viewModel.selectedMenuSectionList.value.equals("")){
            viewModel.selectedMenuSectionList.value =  menuSection.menuSectionName
            viewModel.selectedMenuSectionIdList.value!!.add(menuSection.catalogueSectionId!!)
        }else  if(!viewModel.selectedMenuSectionList.value!!.contains(menuSection.menuSectionName!!)){
            viewModel.selectedMenuSectionList.value = viewModel.selectedMenuSectionList.value + ", " + menuSection.menuSectionName
            viewModel.selectedMenuSectionIdList.value!!.add(menuSection.catalogueSectionId!!)
        }*/

        /*if(viewModel.selectedMenuSectionArrayList.isEmpty()){
            viewModel.selectedMenuSectionArrayList.add(menuSection)
        }else{
            if(menuSection.isSelected){
                viewModel.selectedMenuSectionArrayList.add(menuSection)
            }else{
                viewModel.selectedMenuSectionArrayList.remove(menuSection)
            }
        }
        viewModel.selectedMenuSectionList.value = ""
        for(menuSectionDTO in viewModel.selectedMenuSectionArrayList){
            if (viewModel.selectedMenuSectionList.value.equals("")){
                viewModel.selectedMenuSectionList.value =  menuSectionDTO.menuSectionName
                viewModel.selectedMenuSectionIdList.value!!.add(menuSectionDTO.catalogueSectionId!!)
            }else  if(!viewModel.selectedMenuSectionList.value!!.contains(menuSectionDTO.menuSectionName!!)){
                viewModel.selectedMenuSectionList.value = viewModel.selectedMenuSectionList.value + ", " + menuSectionDTO.menuSectionName
                viewModel.selectedMenuSectionIdList.value!!.add(menuSectionDTO.catalogueSectionId!!)
            }
        }*/
    }


    /*override fun onRemoveItemClick(position: Int, closedDatesDTO: ClosedDatesDTO) {
//        viewModel.onRemoveItem(position, closedDatesDTO)
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Utils.hideKeyboard(requireActivity())
        when (requestCode) {
            Constants.SELECT_FILE -> {
                onSelectResult(data)
            }
            Constants.CAMERA -> {
                if (data != null) {
                    if (data.extras?.get("data") != null) {

                        val thumbnail = data.extras?.get("data") as Bitmap
                        val uri = Utils.getImageUri(requireContext(), thumbnail)
                        uri?.let {
                            binding.dishImageView.setImageURI(it)
                            viewModel.setImage(it)
                        }

                    }
                }

            }
        }
    }

    private fun onSelectResult(data: Intent?) {
        if (data != null) {
            val mClipData = data.clipData
            if (mClipData != null) {
                val uriList = ArrayList<Uri>()
                val item = mClipData.getItemAt(0)
                val uri = item.uri
                uri?.let {
                    binding.dishImageView.setImageURI(it)
                    viewModel.setImage(it)
                }
            } else {
                val singleImageUri = data.data
                binding.dishImageView.setImageURI(singleImageUri)
                viewModel.setImage(singleImageUri!!)

            }

        }
    }

    interface AddDishFragmentListener {
        fun onCloseClick()
        fun onDishSave()
    }

    override fun getTheme(): Int {
        return R.style.RoundedCornersDialog
    }

}