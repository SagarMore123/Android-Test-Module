package com.sagar.logutil.checqk.adapters.basicinfo

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.astrika.checqk.model.FacilityMasterDTO
import com.sagar.logutil.databinding.BasicInfoFacilityMastersItemCellLayoutBinding

class FacilitiesAdapter(
    private var mActivity: Activity,
    private var clickListener: OnItemClickListener
) :
    RecyclerView.Adapter<FacilitiesAdapter.ViewHolder>() {

    private var arrayList = listOf<FacilityMasterDTO>()

    fun submitList(updatedList: List<FacilityMasterDTO>) {
        val oldList = arrayList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ItemDiffCallback(oldList, updatedList)
        )
        arrayList = updatedList
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()

    }


    class ItemDiffCallback(
        private var oldList: List<FacilityMasterDTO>,
        private var newList: List<FacilityMasterDTO>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].facilityId == newList[oldItemPosition].facilityId)
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

    class ViewHolder(private val binding: BasicInfoFacilityMastersItemCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(
            activity: Activity,
            position: Int,
            dto: FacilityMasterDTO,
            itemClick: OnItemClickListener
        ) {

            binding.facilityMasterDTO = dto
/*

            binding.facilityDescEt.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    itemClick.onItemClick(position, dto,binding)
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {

                }


            })

*/


            binding.checkBox.setOnClickListener {
                dto.selected = !dto.selected
                itemClick.onItemClick(position, dto, binding)
            }

        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    BasicInfoFacilityMastersItemCellLayoutBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                return ViewHolder(binding)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(
            position: Int,
            facilityMasterDTO: FacilityMasterDTO,
            binding: BasicInfoFacilityMastersItemCellLayoutBinding
        )
    }


}