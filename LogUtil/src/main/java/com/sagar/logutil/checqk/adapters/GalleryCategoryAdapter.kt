package com.sagar.logutil.checqk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.checqk.model.GalleryImageCategory
import com.sagar.logutil.databinding.CategoryCellLayoutBinding


class GalleryCategoryAdapter(
    private val context: Context, private val listener: OnItemClickListener
) : RecyclerView.Adapter<GalleryCategoryAdapter.CategoryViewHolder>() {

    private var galleryImageCategoryList = ArrayList<GalleryImageCategory>()


    fun setGalleryCategoryList(galleryImageCategoryList: ArrayList<GalleryImageCategory>) {
        this.galleryImageCategoryList = galleryImageCategoryList
    }

    class CategoryViewHolder(private val binding: CategoryCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            position: Int,
            galleryImageCategory: GalleryImageCategory,
            itemClickListener: OnItemClickListener
        ) {


            binding.galleryImageCategory = galleryImageCategory

            binding.root.setOnClickListener {
                /*if (prevSelectedGalleryImageCategory != null) {
                    prevSelectedGalleryImageCategory!!.showChecked?.set(false)
                }
                prevSelectedGalleryImageCategory = galleryImageCategory*/

//                galleryImageCategory.showChecked?.set(true)
                itemClickListener.onGalleryCategoryItemClick(position, galleryImageCategory)
            }



        }

        companion object {
//            var prevSelectedGalleryImageCategory: GalleryImageCategory? = null

            fun from(parent: ViewGroup): CategoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    CategoryCellLayoutBinding.inflate(layoutInflater, parent, false)
                return CategoryViewHolder(binding)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {


        return CategoryViewHolder.from(parent)

    }

    override fun getItemCount(): Int = galleryImageCategoryList.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        val galleryCategoryItem = galleryImageCategoryList[position]
        holder.bind(position, galleryCategoryItem, listener)

    }


    interface OnItemClickListener {
        fun onGalleryCategoryItemClick(
            position: Int,
            galleryImageCategory: GalleryImageCategory
        )
    }

}