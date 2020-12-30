package com.sagar.logutil.checqk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.checqk.model.CatalogueImageCategory
import com.sagar.logutil.databinding.MenuCategoryCellLayoutBinding


class MenuCategoryAdapter(
    private val context: Context, private val listener: OnItemClickListener
) : RecyclerView.Adapter<MenuCategoryAdapter.CategoryViewHolder>() {

    private var menuImageCategoryList = ArrayList<CatalogueImageCategory>()

    fun setMenuCategoryList(galleryImageCategoryList: ArrayList<CatalogueImageCategory>) {
        this.menuImageCategoryList = galleryImageCategoryList
    }

    class CategoryViewHolder(private val binding: MenuCategoryCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            position: Int,
            catalogueImageCategory: CatalogueImageCategory,
            itemClickListener: OnItemClickListener
        ) {


            binding.menuImageCategory = catalogueImageCategory

            binding.root.setOnClickListener {
                /*if (prevSelectedMenuImageCategory != null) {
                    prevSelectedMenuImageCategory!!.showChecked?.set(false)
                }
                prevSelectedMenuImageCategory = menuImageCategory

                menuImageCategory.showChecked?.set(true)*/
                itemClickListener.onMenuCategoryItemClick(position, catalogueImageCategory)
            }

        }

        companion object {
//            var prevSelectedMenuImageCategory: MenuImageCategory? = null

            fun from(parent: ViewGroup): CategoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    MenuCategoryCellLayoutBinding.inflate(layoutInflater, parent, false)
                return CategoryViewHolder(binding)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        return CategoryViewHolder.from(parent)

    }

    override fun getItemCount(): Int = menuImageCategoryList.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        val galleryCategoryItem = menuImageCategoryList[position]
        holder.bind(position, galleryCategoryItem, listener)

    }


    interface OnItemClickListener {
        fun onMenuCategoryItemClick(
            position: Int,
            catalogueImageCategory: CatalogueImageCategory
        )
    }

}