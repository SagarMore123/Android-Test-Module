package com.sagar.logutil.checqk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.checqk.model.CatalogueSectionDTO
import com.sagar.logutil.databinding.DishMenuSectionDropdownCellLayoutBinding


class DishMenuSectionsDropDownAdapter(
    private val context: Context, private val listener: OnMenuSectionClickListener
) : RecyclerView.Adapter<DishMenuSectionsDropDownAdapter.SectionViewHolder>() {

    private var menuSectionsList = ArrayList<CatalogueSectionDTO>()


    fun setDishMenuSectionList(catalogueSectionsList: ArrayList<CatalogueSectionDTO>) {
        this.menuSectionsList = catalogueSectionsList
    }

    class SectionViewHolder(private val binding: DishMenuSectionDropdownCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            position: Int,
            catalogueSection: CatalogueSectionDTO,
            itemClickListener: OnMenuSectionClickListener
        ) {

            binding.menuSectionDTO = catalogueSection

            binding.root.setOnClickListener {
              /*  if (prevSelectedGalleryImageCategory != null) {
                    prevSelectedGalleryImageCategory!!.showChecked?.set(false)
                }
                prevSelectedGalleryImageCategory = galleryImageCategory*/

//                menuSection.isSelected = !menuSection.isSelected!!
                itemClickListener.onMenuSectionItemClick(position, catalogueSection)
            }

            binding.markAsClosedImg.setOnCheckedChangeListener{ buttonView, isChecked ->
                catalogueSection.isSelected = isChecked
                itemClickListener.onMenuSectionItemClick(position, catalogueSection)
            }


        }

        companion object {
//            var prevSelectedGalleryImageCategory: GalleryImageCategory? = null

            fun from(parent: ViewGroup): SectionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    DishMenuSectionDropdownCellLayoutBinding.inflate(layoutInflater, parent, false)
                return SectionViewHolder(binding)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {


        return SectionViewHolder.from(parent)

    }

    override fun getItemCount(): Int = menuSectionsList.size

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {

        val menuCategoryItem = menuSectionsList[position]
        holder.bind(position, menuCategoryItem, listener)

    }


    interface OnMenuSectionClickListener {
        fun onMenuSectionItemClick(
            position: Int,
            catalogueSection: CatalogueSectionDTO
        )
    }

}