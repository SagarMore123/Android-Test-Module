package com.sagar.logutil.checqk.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.checqk.model.ClosedDatesDTO
import com.sagar.logutil.databinding.ClosedDateItemCellLayoutBinding

class ClosedDatesAdapter(
    private var mActivity: Activity,
    private var clickListener: OnItemClickListener
) :
    RecyclerView.Adapter<ClosedDatesAdapter.ViewHolder>() {

    private var arrayList = listOf<ClosedDatesDTO>()

    fun submitList(updatedList: List<ClosedDatesDTO>) {
        val oldList = arrayList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ItemDiffCallback(oldList, updatedList)
        )
        arrayList = updatedList
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()

    }


    class ItemDiffCallback(
        private var oldList: List<ClosedDatesDTO>,
        private var newList: List<ClosedDatesDTO>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].outletDateRestrictionId == newList[oldItemPosition].outletDateRestrictionId)
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
        holder.bind(position, item, clickListener)
    }

    class ViewHolder(private val binding: ClosedDateItemCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            position: Int,
            closedDatesDTO: ClosedDatesDTO,
            itemClick: OnItemClickListener
        ) {

            binding.closedDateDTO = closedDatesDTO

            binding.removeImg.setOnClickListener {
                itemClick.onRemoveItemClick(position, closedDatesDTO)
            }

        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ClosedDateItemCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    interface OnItemClickListener {
        fun onRemoveItemClick(
            position: Int,
            closedDatesDTO: ClosedDatesDTO
        )
    }


}