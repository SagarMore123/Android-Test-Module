package com.sagar.logutil.checqk.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.checqk.model.GalleryImageCategory
import com.sagar.logutil.checqk.model.ImageDTO
import com.sagar.logutil.databinding.GalleryImagesCellLayoutBinding


class GalleryImageAdapter(
    private val context: Context,
    private val listener: OnItemClickListener, private val subImageListener:GallerySubImageAdapter.OnSubImageClickListener
) : RecyclerView.Adapter<GalleryImageAdapter.ViewHolder>(){

    var galleryImageCategoryList = ArrayList<GalleryImageCategory>()
//    private var gallerySubImageAdapter = GallerySubImageAdapter(context)

    lateinit var imageDTO : ImageDTO
    var position : Int = 0

    private var myArrayUri = ArrayList<Uri>()
    private var imageCategoryPosition = 0

    fun setGalleryCategoryList(galleryImageCategoryList: ArrayList<GalleryImageCategory>) {
        this.galleryImageCategoryList = galleryImageCategoryList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: GalleryImagesCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var galleryBinding: GalleryImagesCellLayoutBinding = binding

        fun bind(
            context: Context,
            position: Int,
            listener: OnItemClickListener,
            subImageListener: GallerySubImageAdapter.OnSubImageClickListener,
            galleryCategoryImageItem: GalleryImageCategory
        ) {

            binding.galleryImageCategory = galleryCategoryImageItem

            val gallerySubImageAdapter = GallerySubImageAdapter(context,subImageListener)

            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            galleryBinding.singleCategoryRecyclerView.layoutManager = layoutManager
            galleryBinding.singleCategoryRecyclerView.adapter = gallerySubImageAdapter
            if(galleryCategoryImageItem.documentGetDtos != null && galleryCategoryImageItem.documentGetDtos!!.size > 0 ){
                gallerySubImageAdapter.setImageList(galleryCategoryImageItem.documentGetDtos!!)
            }


            /*if(galleryCategoryImageItem.showView == true){
                itemView.visibility = View.VISIBLE
            }else{
                itemView.visibility = View.GONE
            }*/

//            subImageListener.onRemove(position)

            binding.addLayout.setOnClickListener {
                listener.onAddClick(position, gallerySubImageAdapter, galleryCategoryImageItem)
            }

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    GalleryImagesCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val galleryCategoryItem = galleryImageCategoryList[position]

        holder.bind(context, position, listener,subImageListener,galleryCategoryItem)
//        gallerySubImageAdapter.setUriList(myArrayUri)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int = galleryImageCategoryList.size


    interface OnItemClickListener {
        fun onGalleryCategoryItemClick(
            position: Int,
            galleryImageCategory: GalleryImageCategory
        )

        fun onAddClick(
            position: Int,
            gallerySubImageAdapter: GallerySubImageAdapter,
            galleryImageCategory: GalleryImageCategory
        )

    }



}