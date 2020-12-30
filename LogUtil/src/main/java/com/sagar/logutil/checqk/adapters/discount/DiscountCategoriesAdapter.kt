package com.sagar.logutil.checqk.adapters.discount

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.checqk.model.discount.DiscountCategoryDTO
import com.sagar.logutil.databinding.DiscountCategoriesItemCellLayoutBinding

class DiscountCategoriesAdapter(
    private var mActivity: Activity,
    private var clickListener: OnItemClickListener
) :
    RecyclerView.Adapter<DiscountCategoriesAdapter.ViewHolder>() {

    var list = listOf<DiscountCategoryDTO>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(mActivity, position, item, clickListener)
    }

    class ViewHolder(private val binding: DiscountCategoriesItemCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            activity: Activity,
            position: Int,
            dto: DiscountCategoryDTO,
            itemClick: OnItemClickListener
        ) {

            binding.outletDiscountCategoryDTO = dto

            binding.itemTxt.setOnClickListener {
                dto.selected = true
                itemClick.onItemClick(position, dto)
            }
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    DiscountCategoriesItemCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(
            position: Int,
            discountCategoryDTO: DiscountCategoryDTO
        )
    }


}