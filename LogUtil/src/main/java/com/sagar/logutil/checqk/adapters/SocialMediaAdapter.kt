package com.sagar.logutil.checqk.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.checqk.model.SocialMediaDTO
import com.sagar.logutil.databinding.SocialMediaItemCellLayoutBinding

class SocialMediaAdapter(
    private var mActivity: Activity,
    private var clickListenerRemove: OnRemoveDetailItemClickListener
) :
    RecyclerView.Adapter<SocialMediaAdapter.ViewHolder>() {

    private var arrayList = listOf<SocialMediaDTO>()

    fun submitList(updatedList: List<SocialMediaDTO>) {
        val oldList = arrayList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ItemDiffCallback(oldList, updatedList)
        )
        arrayList = updatedList
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()

    }


    class ItemDiffCallback(
        private var oldList: List<SocialMediaDTO>,
        private var newList: List<SocialMediaDTO>
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

    class ViewHolder(private val binding: SocialMediaItemCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            activity: Activity,
            position: Int,
            dto: SocialMediaDTO,
            removeDetailItemClick: OnRemoveDetailItemClickListener
        ) {

            binding.socialMediaDTO = dto

            binding.removeImg.setOnClickListener {
                removeDetailItemClick.onRemoveDetailItemClick(position, dto)
            }

        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SocialMediaItemCellLayoutBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return ViewHolder(binding)
            }
        }
    }

    // For adding binding to list
    interface OnRemoveDetailItemClickListener {
        fun onRemoveDetailItemClick(
            position: Int,
            socialMediaDTO: SocialMediaDTO
        )
    }


}