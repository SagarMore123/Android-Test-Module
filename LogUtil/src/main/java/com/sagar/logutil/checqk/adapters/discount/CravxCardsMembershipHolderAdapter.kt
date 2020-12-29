package com.sagar.logutil.checqk.adapters.discount

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.astrika.checqk.model.discount.CravxCardsMembershipHolderDTO
import com.sagar.logutil.databinding.MembershipHolderItemCellLayoutBinding

class CravxCardsMembershipHolderAdapter(
    private var mActivity: Activity,
    private var clickListener: OnItemClickListener
) :
    RecyclerView.Adapter<CravxCardsMembershipHolderAdapter.ViewHolder>() {

    private var arrayList = listOf<CravxCardsMembershipHolderDTO>()

    fun submitList(updatedList: List<CravxCardsMembershipHolderDTO>) {
        val oldList = arrayList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ItemDiffCallback(oldList, updatedList)
        )
        arrayList = updatedList
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()

    }


    class ItemDiffCallback(
        private var oldList: List<CravxCardsMembershipHolderDTO>,
        private var newList: List<CravxCardsMembershipHolderDTO>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].cravxDiscountCardId == newList[oldItemPosition].cravxDiscountCardId)
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].equals(newList[oldItemPosition]))
        }

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
        holder.bind(mActivity, position, item, clickListener)
    }

    class ViewHolder(private val binding: MembershipHolderItemCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            activity: Activity,
            position: Int,
            dto: CravxCardsMembershipHolderDTO,
            itemClick: OnItemClickListener
        ) {

            if (position != 0) {
                binding.itemDivider.visibility = View.VISIBLE
            }

            binding.text = dto.cravxDiscountCardName ?: ""
            binding.isSelected = dto.selected ?: false

            binding.mainLayout.setOnClickListener {
                dto.selected = true
                itemClick.onItemClick(position, dto)
            }

        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    MembershipHolderItemCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(
            position: Int,
            dto: CravxCardsMembershipHolderDTO
        )
    }


}