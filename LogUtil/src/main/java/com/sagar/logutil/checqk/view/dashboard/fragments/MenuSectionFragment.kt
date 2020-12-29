package com.astrika.checqk.view.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.astrika.checqk.model.CatalogueSectionDTO
import com.astrika.checqk.utils.CustomProgressBar
import com.astrika.checqk.utils.Utils
import com.astrika.checqk.view.dashboard.viewmodels.MenuSectionViewModel
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.DragListItemAdapter
import com.sagar.logutil.checqk.adapters.MenuCategoryHorizontalListAdapter
import com.sagar.logutil.checqk.adapters.MenuSectionListAdapter
import com.sagar.logutil.databinding.FragmentMenuSectionBinding

class MenuSectionFragment : Fragment(),
    MenuCategoryHorizontalListAdapter.OnMenuCategoryClickListener,
    MenuSectionListAdapter.OnItemClickListener {

    private lateinit var binding: FragmentMenuSectionBinding
    private lateinit var viewModel: MenuSectionViewModel

    private var progressBar = CustomProgressBar()
    lateinit var menuCategoryListAdapter : MenuCategoryHorizontalListAdapter
    lateinit var menuSectionListAdapter : MenuSectionListAdapter
    lateinit var inactiveMenuSectionListAdapter : MenuSectionListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_menu_section, container, false)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_menu_section,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            MenuSectionViewModel::class.java,
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
        menuSectionListAdapter = MenuSectionListAdapter(requireContext(), this)
        binding.menuSectionRecycler.adapter = menuSectionListAdapter

        val dividerItemDecoration = DividerItemDecoration(requireContext() , manager.orientation)
        binding.menuSectionRecycler.addItemDecoration(dividerItemDecoration)


        // Setup ItemTouchHelper
        val callback = DragListItemAdapter(menuSectionListAdapter, requireContext(),
            ItemTouchHelper.UP.or(ItemTouchHelper.DOWN), ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT))
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(binding.menuSectionRecycler)

        binding.inactiveMenuCategoryRecyclerView.layoutManager = LinearLayoutManager(activity)
        inactiveMenuSectionListAdapter = MenuSectionListAdapter(requireContext(), this)
        binding.inactiveMenuCategoryRecyclerView.adapter = inactiveMenuSectionListAdapter

        viewModel.getAllMenuCategories()
//        viewModel.getAllMenuSections()

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
                menuSectionListAdapter.setMenuSectionList(it)
            }
        })
        viewModel.inactiveMenuSectionMutableArrayList.observe(requireActivity(), Observer {
            it.let{
                inactiveMenuSectionListAdapter.setMenuSectionList(it)
            }
        })
    }

    override fun onEditItemClick(position: Int, catalogueSection: CatalogueSectionDTO) {
        viewModel.index = position
        viewModel.showAddMenuSectionDialog(catalogueSection)
    }

    override fun onDeleteItemClick(position: Int, catalogueSection: CatalogueSectionDTO) {
        viewModel.index = position
//        viewModel.changeMenuSectionStatus(menuSection, !menuSection.active!!)
        viewModel.confirmationDialogForDelete(requireContext(), catalogueSection)
    }

    override fun onItemSwap(catalogueSectionList: ArrayList<CatalogueSectionDTO>) {
//        viewModel.menuSectionList = menuSectionList
        viewModel.saveMenuSectionsSequence(catalogueSectionList)
    }

    override fun onMenuCategorySelected(position: Int, catalogueCategoryId: Long) {
        viewModel.catalogueCategoryId = catalogueCategoryId
        viewModel.getAllMenuSections()
    }
}