package com.sagar.logutil.checqk.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.checqk.model.CatalogueImageCategory
import com.sagar.logutil.checqk.model.ImageDTO
import com.sagar.logutil.databinding.MenuImagesCellLayoutBinding


class MenuImagesAdapter(
    private val context: Context,
    private val listener: OnItemClickListener,
    private val subImageListener: MenuSubImageAdapter.OnSubImageClickListener
) : RecyclerView.Adapter<MenuImagesAdapter.ViewHolder>() {

    var menuImageCategoryList = ArrayList<CatalogueImageCategory>()

    //    private var gallerySubImageAdapter = GallerySubImageAdapter(context)

    lateinit var imageDTO: ImageDTO
    var position: Int = 0

    private var myArrayUri = ArrayList<Uri>()
    private var imageCategoryPosition = 0

    fun setMenuImagesCategoryList(catalogueImageCategoryList: ArrayList<CatalogueImageCategory>) {
        this.menuImageCategoryList = catalogueImageCategoryList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: MenuImagesCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var menuBinding: MenuImagesCellLayoutBinding = binding

        fun bind(
            context: Context,
            position: Int,
            listener: OnItemClickListener,
            subImageListener: MenuSubImageAdapter.OnSubImageClickListener,
            catalogueCategoryImageItem: CatalogueImageCategory
        ) {

            binding.menuImageCategory = catalogueCategoryImageItem

            val menuSubImageAdapter = MenuSubImageAdapter(context, subImageListener)

            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            menuBinding.singleCategoryRecyclerView.layoutManager = layoutManager
            menuBinding.singleCategoryRecyclerView.adapter = menuSubImageAdapter
            if (catalogueCategoryImageItem.documentGetDtos != null && catalogueCategoryImageItem.documentGetDtos!!.size > 0) {
                menuSubImageAdapter.setImageList(catalogueCategoryImageItem.documentGetDtos!!)
            }


            binding.addLayout.setOnClickListener {
                listener.onAddClick(position, menuSubImageAdapter, catalogueCategoryImageItem)
            }

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    MenuImagesCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val galleryCategoryItem = menuImageCategoryList[position]

        holder.bind(context, position, listener, subImageListener, galleryCategoryItem)
//        gallerySubImageAdapter.setUriList(myArrayUri)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int = menuImageCategoryList.size


    interface OnItemClickListener {
        fun onMenuCategoryItemClick(
            position: Int,
            catalogueImageCategory: CatalogueImageCategory
        )

        fun onAddClick(
            position: Int,
            menuSubImageAdapter: MenuSubImageAdapter,
            catalogueImageCategory: CatalogueImageCategory
        )

    }


}