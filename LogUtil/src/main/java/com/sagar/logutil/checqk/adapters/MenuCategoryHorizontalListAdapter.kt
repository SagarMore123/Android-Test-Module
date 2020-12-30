package com.sagar.logutil.checqk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.R
import com.sagar.logutil.checqk.model.CategoryCatalogueDTO
import com.sagar.logutil.databinding.MenuCategoryHorizontalListCellLayoutBinding


class MenuCategoryHorizontalListAdapter(
    private val context: Context,
    private val listener : OnMenuCategoryClickListener
) : RecyclerView.Adapter<MenuCategoryHorizontalListAdapter.MenuCategoryViewHolder>() {

    private var menuCategoryList = ArrayList<CategoryCatalogueDTO>()

    fun setMenuCategoryList(menuCategoryList: ArrayList<CategoryCatalogueDTO>) {
        this.menuCategoryList = menuCategoryList
        notifyDataSetChanged()
    }

    class MenuCategoryViewHolder(private val binding: MenuCategoryHorizontalListCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            menuCategory: CategoryCatalogueDTO,
            listener : OnMenuCategoryClickListener
        ) {

            binding.menuCategory = menuCategory

            if (position == 0){
                binding.menuCategoryButton.setTextColor(context.resources.getColor(R.color.colorWhite))
                binding.menuCategoryButton.setBackgroundDrawable(
                    context.resources.getDrawable(R.drawable.circular_rounded_corner_purple_background)
                )
                previousSelectedButton = binding.menuCategoryButton
            }

            binding.menuCategoryButton.setOnClickListener{
                listener.onMenuCategorySelected(position, menuCategory.catalogueCategoryId!!)
                if(previousSelectedButton != null){
                    previousSelectedButton!!.setTextColor(context.resources.getColor(R.color.colorPrimary))
                    previousSelectedButton!!.setBackgroundDrawable(
                        context.resources.getDrawable(R.drawable.circular_rounded_corner_white_background_with_blue_stroke)
                    )
                }
                binding.menuCategoryButton.setTextColor(context.resources.getColor(R.color.colorWhite))
                binding.menuCategoryButton.setBackgroundDrawable(
                    context.resources.getDrawable(R.drawable.circular_rounded_corner_purple_background)
                )
                previousSelectedButton = binding.menuCategoryButton
            }
        }

        companion object {
            var previousSelectedButton : Button? = null

            fun from(parent: ViewGroup): MenuCategoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    MenuCategoryHorizontalListCellLayoutBinding.inflate(layoutInflater, parent, false)
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

    interface OnMenuCategoryClickListener {
        fun onMenuCategorySelected(
            position: Int,
            catalogueCategoryId: Long
        )
    }

}