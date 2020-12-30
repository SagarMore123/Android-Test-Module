package com.sagar.logutil.checqk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.R
import com.sagar.logutil.checqk.model.CategoryCatalogueDTO
import com.sagar.logutil.databinding.MenuCategoryListCellLayoutBinding


class MenuCategoryListAdapter(
    private val context: Context,
    private val listener : OnItemClickListener
) : RecyclerView.Adapter<MenuCategoryListAdapter.MenuCategoryViewHolder>() {

    private var menuCategoryList = ArrayList<CategoryCatalogueDTO>()


    fun setMenuCategoryList(menuCategoryList: ArrayList<CategoryCatalogueDTO>) {
        this.menuCategoryList = menuCategoryList
        notifyDataSetChanged()
    }

    class MenuCategoryViewHolder(private val binding: MenuCategoryListCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            menuCategory: CategoryCatalogueDTO,
            listener : OnItemClickListener
        ) {

            binding.menuCategory = menuCategory

            if (!menuCategory.active!!){
                binding.editImageView.visibility = View.GONE
                binding.contentLayout.setBackgroundDrawable(
                    context.resources.getDrawable(
                        R.drawable.circular_rounded_corner_gray_background_with_accent_stroke,
                        null
                    )
                )
                binding.deleteImageView.setBackgroundDrawable(
                    context.resources.getDrawable(
                        R.drawable.ic_restore,
                        null
                    )
                )
            }

            binding.editImageView.setOnClickListener{
                listener.onEditItemClick(position, menuCategory)
            }

            binding.deleteImageView.setOnClickListener{
                listener.onDeleteItemClick(position, menuCategory)
            }
        }

        companion object {

            fun from(parent: ViewGroup): MenuCategoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    MenuCategoryListCellLayoutBinding.inflate(layoutInflater, parent, false)
                return MenuCategoryViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuCategoryViewHolder {
        return MenuCategoryViewHolder.from(parent)

    }

    override fun getItemCount(): Int = menuCategoryList.size

    override fun onBindViewHolder(holderMenu: MenuCategoryViewHolder, position: Int) {

        val menuCategoryItem = menuCategoryList[position]
        holderMenu.bind(context, position, menuCategoryItem, listener)

    }

    /**
     * Function called to swap dragged items
     */
    fun swapItems(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition..toPosition - 1) {
                menuCategoryList.set(i, menuCategoryList.set(i+1, menuCategoryList.get(i)));
            }
        } else {
            for (i in fromPosition..toPosition + 1) {
                menuCategoryList.set(i, menuCategoryList.set(i-1, menuCategoryList.get(i)));
            }
        }
        var i = 1L;
        for( menuCategory in menuCategoryList){
            menuCategory.catalogueCategorySequenceNo = i
            i++
        }

        notifyItemMoved(fromPosition, toPosition)
        listener.onItemSwap(menuCategoryList)
    }

    interface OnItemClickListener {
        fun onEditItemClick(
            position: Int,
            menuCategory: CategoryCatalogueDTO
        )
        fun onDeleteItemClick(
            position: Int,
            menuCategory: CategoryCatalogueDTO
        )
        fun onItemSwap(
            menuCategoryList : ArrayList<CategoryCatalogueDTO>
        )
    }

}