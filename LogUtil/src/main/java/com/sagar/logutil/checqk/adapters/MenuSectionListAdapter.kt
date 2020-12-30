package com.sagar.logutil.checqk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.R
import com.sagar.logutil.checqk.model.CatalogueSectionDTO
import com.sagar.logutil.databinding.MenuSectionListCellLayoutBinding


class MenuSectionListAdapter(
    private val context: Context,
    private val listener : MenuSectionListAdapter.OnItemClickListener
) : RecyclerView.Adapter<MenuSectionListAdapter.MenuSectionViewHolder>() {

    private var menuSectionList = ArrayList<CatalogueSectionDTO>()


    fun setMenuSectionList(catalogueSectionList: ArrayList<CatalogueSectionDTO>) {
        this.menuSectionList = catalogueSectionList
        notifyDataSetChanged()
    }

    class MenuSectionViewHolder(private val binding: MenuSectionListCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            catalogueSection: CatalogueSectionDTO,
            listener : MenuSectionListAdapter.OnItemClickListener
        ) {

            binding.menuSection = catalogueSection
            if (!catalogueSection.active!!){
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
                listener.onEditItemClick(position, catalogueSection)
            }

            binding.deleteImageView.setOnClickListener{
                listener.onDeleteItemClick(position, catalogueSection)
            }
        }

        companion object {
//            var prevSelectedGalleryImageCategory: GalleryImageCategory? = null

            fun from(parent: ViewGroup): MenuSectionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    MenuSectionListCellLayoutBinding.inflate(layoutInflater, parent, false)
                return MenuSectionViewHolder(binding)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuSectionViewHolder {


        return MenuSectionViewHolder.from(parent)

    }

    override fun getItemCount(): Int = menuSectionList.size

    override fun onBindViewHolder(holderMenu: MenuSectionViewHolder, position: Int) {

        val menuCategoryItem = menuSectionList[position]
        holderMenu.bind(context, position, menuCategoryItem, listener)

    }

    /**
     * Function called to swap dragged items
     */
    fun swapItems(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition..toPosition - 1) {
                menuSectionList.set(i, menuSectionList.set(i+1, menuSectionList.get(i)));
            }
        } else {
            for (i in fromPosition..toPosition + 1) {
                menuSectionList.set(i, menuSectionList.set(i-1, menuSectionList.get(i)));
            }
        }

        var i = 1L;
        for( menuSection in menuSectionList){
            menuSection.catalogueSectionSequenceNo = i
            i++
        }

        notifyItemMoved(fromPosition, toPosition)
        listener.onItemSwap(menuSectionList)
    }

    interface OnItemClickListener {
        fun onEditItemClick(
            position: Int,
            catalogueSection: CatalogueSectionDTO
        )
        fun onDeleteItemClick(
            position: Int,
            catalogueSection: CatalogueSectionDTO
        )
        fun onItemSwap(
            catalogueSectionList : ArrayList<CatalogueSectionDTO>
        )
    }

}