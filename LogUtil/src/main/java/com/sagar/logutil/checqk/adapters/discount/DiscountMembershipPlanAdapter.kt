package com.sagar.logutil.checqk.adapters.discount

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.checqk.model.discount.OutletDiscountMembershipPlanDTO
import com.sagar.logutil.databinding.BasicInfoMastersItemCellLayoutBinding

class DiscountMembershipPlanAdapter(
    private var mActivity: Activity,
    private var clickListener: OnItemClickListener
) :
    RecyclerView.Adapter<DiscountMembershipPlanAdapter.ViewHolder>() {

    var list = listOf<OutletDiscountMembershipPlanDTO>()
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

    class ViewHolder(private val binding: BasicInfoMastersItemCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            activity: Activity,
            position: Int,
            dto: OutletDiscountMembershipPlanDTO,
            itemClick: OnItemClickListener
        ) {

            binding.text = dto.membershipName ?: ""
            binding.isSelected = dto.selected ?: false

            binding.checkBox.isEnabled = false
            
            binding.checkBox.setOnClickListener {
                dto.selected = !dto.selected
                itemClick.onItemClick(position, dto)
            }

        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    BasicInfoMastersItemCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(
            position: Int,
            outletDiscountMembershipPlanDTO: OutletDiscountMembershipPlanDTO
        )
    }


}