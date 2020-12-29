package com.astrika.checqk.view.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.astrika.checqk.model.CatalogueSectionDTO
import com.astrika.checqk.model.ProductDetailsDTO
import com.astrika.checqk.model.ProductWithSectionDetails
import com.astrika.checqk.utils.CustomProgressBar
import com.astrika.checqk.utils.Utils
import com.astrika.checqk.view.dashboard.viewmodels.DishCategoryViewModel
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.DishCategoryAdapter
import com.sagar.logutil.checqk.adapters.DishListAdapter
import com.sagar.logutil.checqk.adapters.MenuCategoryHorizontalListAdapter
import com.sagar.logutil.databinding.FragmentDishCategoryBinding

class DishCategoryFragment : Fragment(),
    MenuCategoryHorizontalListAdapter.OnMenuCategoryClickListener,
    DishCategoryAdapter.OnDishAddClickListener,
    DishListAdapter.OnDishItemClickListener,
    AddDishDialogFragment.AddDishFragmentListener{

    private lateinit var binding: FragmentDishCategoryBinding
    private lateinit var viewModel: DishCategoryViewModel

    private var progressBar = CustomProgressBar()
    lateinit var menuCategoryListAdapter : MenuCategoryHorizontalListAdapter
    lateinit var dishCategoryAdapter : DishCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_menu_section, container, false)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dish_category,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            DishCategoryViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryListManager  = LinearLayoutManager(activity)
        categoryListManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.menuCategoryRecycler.layoutManager = categoryListManager
        menuCategoryListAdapter = MenuCategoryHorizontalListAdapter(requireContext(), this)
        binding.menuCategoryRecycler.adapter = menuCategoryListAdapter

        val manager  = LinearLayoutManager(activity)
        binding.menuSectionRecycler.layoutManager = manager
        dishCategoryAdapter = DishCategoryAdapter(requireContext(), this,this)
        binding.menuSectionRecycler.adapter = dishCategoryAdapter

        /*val dividerItemDecoration = DividerItemDecoration(requireContext() , manager.orientation)
        binding.menuSectionRecycler.addItemDecoration(dividerItemDecoration)*/


        // Setup ItemTouchHelper
       /* val callback = DragListItemAdapter(dishCategoryAdapter, requireContext(),
            ItemTouchHelper.UP.or(ItemTouchHelper.DOWN), ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT))
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(binding.menuSectionRecycler)*/

        viewModel.getAllMenuCategories()

        setObservers()

//        itemAdapter.setMenuSectionList(getMenuSectionList())
    }

    private fun setObservers(){
        viewModel.showProgressBar.observe(requireActivity(), Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })

        viewModel.menuCategoryMutableArrayList.observe(requireActivity(), Observer {
            it.let{
                menuCategoryListAdapter.setMenuCategoryList(it)
            }
        })

        viewModel.menuSectionMutableArrayList.observe(requireActivity(), Observer {
            it.let{
                dishCategoryAdapter.setDishCategoryList(it)
            }
        })
    }

    override fun onMenuCategorySelected(position: Int, catalogueCategoryId: Long) {
        viewModel.catalogueCategoryId = catalogueCategoryId
        viewModel.getAllDishesWithSections()
    }

    override fun onAddItemClick(position: Int, productSection : ProductWithSectionDetails) {
        AddDishDialogFragment.newInstance(
            productSection.catalogueSectionDTO.catalogueCategoryId!!,
            productSection.catalogueSectionDTO.catalogueSectionId!!,
            0,
            this
        ).show(childFragmentManager, AddDishDialogFragment.TAG)
    }

    override fun onInactiveListClick(position: Int, productMenuSection: ProductWithSectionDetails) {
        viewModel.getInactiveDishes(productMenuSection.catalogueSectionDTO!!)
    }

    override fun onDishEditClick(position: Int, catalogueSection : CatalogueSectionDTO, productDetails: ProductDetailsDTO) {
        AddDishDialogFragment.newInstance(
            catalogueSection.catalogueCategoryId!!,
            catalogueSection.catalogueSectionId!!,
            productDetails.productId!!,
            this
        ).show(childFragmentManager, AddDishDialogFragment.TAG)
    }

    override fun onDishDeleteClick(position: Int, productDetails: ProductDetailsDTO) {
//        viewModel.changeDishStatus(dishDetails, false)
        viewModel.confirmationDialogForDelete(requireContext(), productDetails)
    }

    override fun onCloseClick() {
        childFragmentManager.findFragmentByTag( AddDishDialogFragment.TAG)?.let {
            (it as DialogFragment).dismiss()
        }
    }

    override fun onItemSwap(productDetailList: ArrayList<ProductDetailsDTO>) {
        viewModel.saveDishSequence(productDetailList)
    }

    override fun onDishSave() {
        childFragmentManager.findFragmentByTag( AddDishDialogFragment.TAG)?.let {
            (it as DialogFragment).dismiss()
        }
        viewModel.getAllDishesWithSections()
    }


}