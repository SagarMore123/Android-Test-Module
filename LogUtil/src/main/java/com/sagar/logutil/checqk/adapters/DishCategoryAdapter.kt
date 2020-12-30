package com.sagar.logutil.checqk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.R
import com.sagar.logutil.checqk.model.ProductWithSectionDetails
import com.sagar.logutil.databinding.DishCategoryListCellLayoutBinding


class DishCategoryAdapter(
    private val context: Context,
    private val dishAddClickListener : DishCategoryAdapter.OnDishAddClickListener,
    private val dishItemClickListener : DishListAdapter.OnDishItemClickListener
) : RecyclerView.Adapter<DishCategoryAdapter.DishCategoryViewHolder>() {

    private var dishMenuSectionList = ArrayList<ProductWithSectionDetails>()

    fun setDishCategoryList(menuSectionList: ArrayList<ProductWithSectionDetails>) {
        this.dishMenuSectionList = menuSectionList
        notifyDataSetChanged()
    }

    class DishCategoryViewHolder(private val binding: DishCategoryListCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            productMenuSection: ProductWithSectionDetails,
            addDishListener: DishCategoryAdapter.OnDishAddClickListener,
            dishItemClickListener : DishListAdapter.OnDishItemClickListener
        ) {

            binding.dishMenuSection = productMenuSection

            //For Active list
            val manager  = LinearLayoutManager(context)
            binding.dishRecyclerView.layoutManager = manager
            val dishListAdapter = DishListAdapter(context, productMenuSection.catalogueSectionDTO , dishItemClickListener)
            binding.dishRecyclerView.adapter = dishListAdapter

            // Setup ItemTouchHelper
            val callback = DragListItemAdapter(dishListAdapter, context,
                ItemTouchHelper.UP.or(ItemTouchHelper.DOWN), ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT))
            val helper = ItemTouchHelper(callback)
            helper.attachToRecyclerView(binding.dishRecyclerView)

            dishListAdapter.setDishList(productMenuSection.activeProductList?: arrayListOf())

            //For Inactive list
            val inactiveManager  = LinearLayoutManager(context)
            binding.inactiveDishRecyclerView.layoutManager = inactiveManager
            val inactiveDishListAdapter = DishListAdapter(context, productMenuSection.catalogueSectionDTO, dishItemClickListener)
            binding.inactiveDishRecyclerView.adapter = inactiveDishListAdapter

            inactiveDishListAdapter.setDishList(productMenuSection.inActiveProductList?: arrayListOf())

            binding.mainLayout.setOnClickListener{
//                listener.onEditItemClick(position, menuSection)
                if (binding.dishListLayout.visibility == View.VISIBLE)
                    binding.dishListLayout.visibility = View.GONE
                else
                    binding.dishListLayout.visibility = View.VISIBLE
            }

            binding.addButton.setOnClickListener{
                addDishListener.onAddItemClick(position, productMenuSection)
            }
            binding.showInactiveListTextView.setOnClickListener{
                showAndHide(context, binding)
            }
            binding.inactiveListArrowImageView.setOnClickListener{
                showAndHide(context, binding)
            }

        }

        companion object {
//            var prevSelectedGalleryImageCategory: GalleryImageCategory? = null

            fun from(parent: ViewGroup): DishCategoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    DishCategoryListCellLayoutBinding.inflate(layoutInflater, parent, false)
                return DishCategoryViewHolder(binding)
            }

            fun showAndHide(context : Context, binding : DishCategoryListCellLayoutBinding){
                if (binding.inactiveDishRecyclerView.visibility == View.VISIBLE){
                    binding.inactiveDishRecyclerView.visibility = View.GONE
                    binding.inactiveListArrowImageView.setImageDrawable(
                        context.resources.getDrawable(
                            R.drawable.ic_arrow_down,
                            null
                        )
                    )
                }else{
                    binding.inactiveDishRecyclerView.visibility = View.VISIBLE
                    binding.inactiveListArrowImageView.setImageDrawable(
                        context.resources.getDrawable(
                            R.drawable.ic_arrow_up,
                            null
                        )
                    )
                }
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishCategoryViewHolder {
        return DishCategoryViewHolder.from(parent)
    }

    override fun getItemCount(): Int = dishMenuSectionList.size

    override fun onBindViewHolder(holderMenu: DishCategoryViewHolder, position: Int) {

        val menuCategoryItem = dishMenuSectionList[position]
        holderMenu.bind(context, position, menuCategoryItem, dishAddClickListener, dishItemClickListener)

    }

    /**
     * Function called to swap dragged items
     */
    fun swapItems(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition..toPosition - 1) {
                dishMenuSectionList.set(i, dishMenuSectionList.set(i+1, dishMenuSectionList.get(i)));
            }
        } else {
            for (i in fromPosition..toPosition + 1) {
                dishMenuSectionList.set(i, dishMenuSectionList.set(i-1, dishMenuSectionList.get(i)));
            }
        }

        notifyItemMoved(fromPosition, toPosition)
    }

    interface OnDishAddClickListener {
        fun onAddItemClick(
            position: Int,
            productMenuSection: ProductWithSectionDetails
        )
        fun onInactiveListClick(
            position: Int,
            productMenuSection: ProductWithSectionDetails
        )
    }

}