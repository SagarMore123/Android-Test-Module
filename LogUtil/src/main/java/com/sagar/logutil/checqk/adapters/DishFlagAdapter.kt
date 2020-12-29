package com.sagar.logutil.checqk.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.checqk.model.ProductFlagDTO
import com.sagar.logutil.databinding.BasicInfoMastersItemCellLayoutBinding

class DishFlagAdapter(
    private var mActivity: Activity
) :
    RecyclerView.Adapter<DishFlagAdapter.ViewHolder>() {

    private var arrayList = listOf<ProductFlagDTO>()

    fun submitList(updatedList: List<ProductFlagDTO>) {
        arrayList = updatedList
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = arrayList[position]
        holder.bind(mActivity, position, item)
    }

    class ViewHolder(private val binding: BasicInfoMastersItemCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            activity: Activity,
            position: Int,
            dto: ProductFlagDTO
        ) {

            binding.text = dto.productFlagName ?: ""
            binding.isSelected = dto.isSelected ?: false

            binding.checkBox.setOnClickListener {
                dto.isSelected = dto.isSelected == null || !dto.isSelected!!
//                itemClick.onItemClick(position, isInMall, dto)
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
/*

    interface OnDishFlagClickListener {
        fun onDishFlagItemClick(
            position: Int,
            dishFlagDTO: DishFlagDTO
        )
    }
*/


}