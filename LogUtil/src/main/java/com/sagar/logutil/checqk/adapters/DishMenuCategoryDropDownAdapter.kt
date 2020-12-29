package com.sagar.logutil.checqk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.checqk.model.CategoryCatalogueDTO
import com.sagar.logutil.databinding.DishMenuCategoryDropdownCellLayoutBinding


class DishMenuCategoryDropDownAdapter(
    private val context: Context, private val listener: OnMenuCategoryClickListener
) : RecyclerView.Adapter<DishMenuCategoryDropDownAdapter.CategoryViewHolder>() {

    private var menuCategoryList = ArrayList<CategoryCatalogueDTO>()


    fun setDishMenuCategoryList(menuCategoryList: ArrayList<CategoryCatalogueDTO>) {
        this.menuCategoryList = menuCategoryList
    }

    class CategoryViewHolder(private val binding: DishMenuCategoryDropdownCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            position: Int,
            menuCategory: CategoryCatalogueDTO,
            itemClickListener: OnMenuCategoryClickListener
        ) {

            binding.catalogueCategoryDTO = menuCategory

            binding.root.setOnClickListener {

                if(menuCategory.isSelected != null){
                    menuCategory.isSelected = !menuCategory.isSelected!!
                }else{
                    menuCategory.isSelected = true
                }
                itemClickListener.onMenuCategoryItemClick(position, menuCategory)
            }
            binding.markAsClosedImg.setOnCheckedChangeListener{ buttonView, isChecked ->
                menuCategory.isSelected = isChecked
                itemClickListener.onMenuCategoryItemClick(position, menuCategory)
            }
        }

        companion object {
//            var prevSelectedGalleryImageCategory: GalleryImageCategory? = null

            fun from(parent: ViewGroup): CategoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    DishMenuCategoryDropdownCellLayoutBinding.inflate(layoutInflater, parent, false)
                return CategoryViewHolder(binding)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {


        return CategoryViewHolder.from(parent)

    }

    override fun getItemCount(): Int = menuCategoryList.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        val menuCategoryItem = menuCategoryList[position]
        holder.bind(position, menuCategoryItem, listener)

    }


    interface OnMenuCategoryClickListener {
        fun onMenuCategoryItemClick(
            position: Int,
            menuCategoryDetails: CategoryCatalogueDTO
        )
    }

}