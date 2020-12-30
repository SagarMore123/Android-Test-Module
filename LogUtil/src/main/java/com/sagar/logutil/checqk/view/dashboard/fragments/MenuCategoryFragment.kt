package com.sagar.logutil.checqk.view.dashboard.fragments

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
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.DragListItemAdapter
import com.sagar.logutil.checqk.adapters.MenuCategoryListAdapter
import com.sagar.logutil.checqk.model.CategoryCatalogueDTO
import com.sagar.logutil.checqk.utils.CustomProgressBar
import com.sagar.logutil.checqk.utils.Utils
import com.sagar.logutil.checqk.view.dashboard.viewmodels.MenuCategoryViewModel
import com.sagar.logutil.databinding.FragmentMenuCategoryBinding


class MenuCategoryFragment : Fragment(), MenuCategoryListAdapter.OnItemClickListener {

    private lateinit var binding: FragmentMenuCategoryBinding
    private lateinit var viewModel: MenuCategoryViewModel

    private var progressBar = CustomProgressBar()
    lateinit var menuCategoryListAdapter : MenuCategoryListAdapter
    lateinit var inactiveMenuCategoryListAdapter : MenuCategoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_menu_category, container, false)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_menu_category,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            MenuCategoryViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val manager  = LinearLayoutManager(activity)
        binding.menuCategoryRecycler.layoutManager = manager
        menuCategoryListAdapter = MenuCategoryListAdapter(requireContext(), this)
        binding.menuCategoryRecycler.adapter = menuCategoryListAdapter

        val dividerItemDecoration = DividerItemDecoration(requireContext() , manager.orientation)
        binding.menuCategoryRecycler.addItemDecoration(dividerItemDecoration)


        // Setup ItemTouchHelper
        val callback = DragListItemAdapter(
            menuCategoryListAdapter,
            requireContext(),
            ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),
            ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)
        )
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(binding.menuCategoryRecycler)

        binding.inactiveMenuCategoryRecyclerView.layoutManager =  LinearLayoutManager(activity)
        inactiveMenuCategoryListAdapter = MenuCategoryListAdapter(requireContext(), this)
        binding.inactiveMenuCategoryRecyclerView.adapter = inactiveMenuCategoryListAdapter

//        viewModel.getMenuList()
        viewModel.getAllMenuCategories()

        setObservers()
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
        viewModel.inactiveMenuCategoryMutableArrayList.observe(requireActivity(), Observer {
            it.let{
                inactiveMenuCategoryListAdapter.setMenuCategoryList(it)
            }
        })
    }

    override fun onEditItemClick(position: Int, menuCategory: CategoryCatalogueDTO) {
        viewModel.index = position
        viewModel.showAddMenuCategoryDialog(menuCategory)
    }

    override fun onDeleteItemClick(position: Int, menuCategory: CategoryCatalogueDTO) {
        viewModel.index = position
//        viewModel.changeMenuCategoryStatus(menuCategory, !menuCategory.active!!)
        viewModel.confirmationDialogForDelete(requireContext(), menuCategory)
    }

    override fun onItemSwap(menuCategoryList: ArrayList<CategoryCatalogueDTO>) {
//        viewModel.menuCategoryList = menuCategoryList
        viewModel.saveMenuCategoriesSequence(menuCategoryList)
    }


}