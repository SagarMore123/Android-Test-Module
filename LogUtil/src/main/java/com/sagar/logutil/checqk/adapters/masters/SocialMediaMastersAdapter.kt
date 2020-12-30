package com.sagar.logutil.checqk.adapters.masters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.checqk.model.SocialMediaMasterDTO
import com.sagar.logutil.databinding.SocialMediaMastersIconItemCellLayoutBinding

class SocialMediaMastersAdapter(
    private var mActivity: Activity,
    private var clickListenerRemove: OnItemClickListener
) :
    RecyclerView.Adapter<SocialMediaMastersAdapter.ViewHolder>() {

    private var arrayList = listOf<SocialMediaMasterDTO>()

    fun submitList(updatedList: List<SocialMediaMasterDTO>) {
        val oldList = arrayList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ItemDiffCallback(oldList, updatedList)
        )
        arrayList = updatedList
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()

    }


    class ItemDiffCallback(
        private var oldList: List<SocialMediaMasterDTO>,
        private var newList: List<SocialMediaMasterDTO>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].mediumId == newList[oldItemPosition].mediumId)
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
        holder.bind(mActivity, position, item, clickListenerRemove)
    }

    class ViewHolder(private val binding: SocialMediaMastersIconItemCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            activity: Activity,
            position: Int,
            dto: SocialMediaMasterDTO,
            itemClick: OnItemClickListener
        ) {

            binding.socialMediaMasterDTO = dto

            binding.img.setOnClickListener {
                itemClick.onItemClick(position, dto)
            }

        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SocialMediaMastersIconItemCellLayoutBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return ViewHolder(binding)
            }
        }
    }

    // For adding binding to list
    interface OnItemClickListener {
        fun onItemClick(
            position: Int,
            socialMediaMasterDTO: SocialMediaMasterDTO
        )
    }


}