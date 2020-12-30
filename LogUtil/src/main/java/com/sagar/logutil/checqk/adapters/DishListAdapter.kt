package com.sagar.logutil.checqk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.R
import com.sagar.logutil.checqk.model.CatalogueSectionDTO
import com.sagar.logutil.checqk.model.ProductDetailsDTO
import com.sagar.logutil.databinding.DishListCellLayoutBinding


class DishListAdapter(
    private val context: Context,
    private val catalogueSection : CatalogueSectionDTO,
    private val listener : OnDishItemClickListener
) : RecyclerView.Adapter<DishListAdapter.DishListViewHolder>() {

    private var dishDetailList = ArrayList<ProductDetailsDTO>()


    fun setDishList(productDetailList: ArrayList<ProductDetailsDTO>) {
        this.dishDetailList = productDetailList
        notifyDataSetChanged()
    }

    class DishListViewHolder(private val binding: DishListCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            catalogueSection : CatalogueSectionDTO,
            productDetails: ProductDetailsDTO,
            listener : OnDishItemClickListener
        ) {

            binding.dishDetails = productDetails
//            binding.originalPriceTextView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

            if(productDetails.productFlagDTOs != null && productDetails.productFlagDTOs?.size?:0 > 0){
                binding.section1TextView.text = productDetails.productFlagDTOs!![0].productFlagName?:""
                binding.section1TextView.visibility = View.VISIBLE
            }else{
                binding.section1TextView.visibility = View.GONE
            }
            if(productDetails.productFlagDTOs != null && productDetails.productFlagDTOs!!.size > 1){
                binding.section2TextView.text = productDetails.productFlagDTOs!![1].productFlagName
                binding.section2TextView.visibility = View.VISIBLE
            }else{
                binding.section2TextView.visibility = View.GONE
            }

            if(productDetails.active == false){ // Inactive
                binding.editImageView.visibility = View.GONE
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    binding.contentLayout.setBackgroundColor(
                        context.resources.getColor(
                            R.color.random_grey_light,
                            null
                        )
                    )
                }
                binding.deleteImageView.setBackgroundDrawable(
                    context.resources.getDrawable(
                        R.drawable.ic_restore,
                        null
                    )
                )
            }

            binding.editImageView.setOnClickListener{
                listener.onDishEditClick(position, catalogueSection, productDetails)
            }

            binding.deleteImageView.setOnClickListener{
                listener.onDishDeleteClick(position, productDetails)
            }
        }

        companion object {
//            var prevSelectedGalleryImageCategory: GalleryImageCategory? = null

            fun from(parent: ViewGroup): DishListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    DishListCellLayoutBinding.inflate(layoutInflater, parent, false)
                return DishListViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishListViewHolder {
        return DishListViewHolder.from(parent)
    }

    override fun getItemCount(): Int = dishDetailList.size

    override fun onBindViewHolder(holderMenu: DishListViewHolder, position: Int) {

        val menuCategoryItem = dishDetailList[position]
            holderMenu.bind(context, position, catalogueSection, menuCategoryItem, listener)

    }

    /**
     * Function called to swap dragged items
     */
    fun swapItems(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition..toPosition - 1) {
                dishDetailList.set(i, dishDetailList.set(i+1, dishDetailList.get(i)));
            }
        } else {
            for (i in fromPosition..toPosition + 1) {
                dishDetailList.set(i, dishDetailList.set(i-1, dishDetailList.get(i)));
            }
        }
        var i = 1L;
        for( dishDetails in dishDetailList){
            dishDetails.productSequenceNo = i
            i++
        }
        notifyItemMoved(fromPosition, toPosition)

        listener.onItemSwap(dishDetailList)
    }

    interface OnDishItemClickListener {
        fun onDishEditClick(
            position: Int,
            catalogueSection : CatalogueSectionDTO,
            productDetails: ProductDetailsDTO
        )
        fun onDishDeleteClick(
            position: Int,
            productDetails: ProductDetailsDTO
        )
        fun onItemSwap(
            productDetailList : ArrayList<ProductDetailsDTO>
        )
       /* fun onDishEnableClick(
            position: Int,
            dishDetails: DishDetailsDTO
        )*/
    }

}