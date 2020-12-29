package com.sagar.logutil.checqk.adapters

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class DragListItemAdapter<T>(
    adapter: T,
    context: Context,
    dragDirs: Int,
    swipeDirs: Int
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs)
{
    var nameAdapter = adapter
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        when (viewHolder) {
            is MenuCategoryListAdapter.MenuCategoryViewHolder -> {
                val listAdapter = nameAdapter as MenuCategoryListAdapter
                listAdapter.swapItems(viewHolder.adapterPosition, target.adapterPosition)
            }
            is MenuSectionListAdapter.MenuSectionViewHolder -> {
                val listAdapter = nameAdapter as MenuSectionListAdapter
                listAdapter.swapItems(viewHolder.adapterPosition, target.adapterPosition)
            }
            is DishCategoryAdapter.DishCategoryViewHolder -> {
                val listAdapter = nameAdapter as DishCategoryAdapter
                listAdapter.swapItems(viewHolder.adapterPosition, target.adapterPosition)
            }
            is DishListAdapter.DishListViewHolder -> {
                val listAdapter = nameAdapter as DishListAdapter
                listAdapter.swapItems(viewHolder.adapterPosition, target.adapterPosition)
            }
        }
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun getSwipeDirs (recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        if (viewHolder is MenuCategoryListAdapter.MenuCategoryViewHolder
            || viewHolder is MenuSectionListAdapter.MenuSectionViewHolder
            || viewHolder is DishCategoryAdapter.DishCategoryViewHolder
            || viewHolder is DishListAdapter.DishListViewHolder
        ) return 0
        return super.getSwipeDirs(recyclerView, viewHolder)
    }


}