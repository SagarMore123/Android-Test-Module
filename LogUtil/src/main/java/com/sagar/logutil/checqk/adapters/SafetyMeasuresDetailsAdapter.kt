package com.sagar.logutil.checqk.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.R
import com.sagar.logutil.checqk.model.SafetyMeasuresDetailsDTO
import com.sagar.logutil.databinding.SafetyMeasuresDetailsItemCellLayoutBinding

class SafetyMeasuresDetailsAdapter(
    private var mActivity: Activity,
    private var clickListenerText: OnTextItemClickListener,
    private var clickListenerImage: OnImageItemClickListener,
    private var clickListenerRemove: OnRemoveDetailItemClickListener
) :
    RecyclerView.Adapter<SafetyMeasuresDetailsAdapter.ViewHolder>() {

    var arrayList = listOf<SafetyMeasuresDetailsDTO>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
/*

    fun submitList(updatedList: List<SafetyMeasuresDetailsDTO>) {
        val oldList = arrayList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ItemDiffCallback(oldList, updatedList)
        )
        arrayList = updatedList
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()

    }


    class ItemDiffCallback(
        private var oldList: List<SafetyMeasuresDetailsDTO>,
        private var newList: List<SafetyMeasuresDetailsDTO>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].outletSecurityMeasuresId == newList[oldItemPosition].outletSecurityMeasuresId)
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
*/


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
        holder.bind(
            mActivity,
            position,
            item,
            clickListenerText,
            clickListenerImage,
            clickListenerRemove
        )
    }

    class ViewHolder(private val binding: SafetyMeasuresDetailsItemCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            activity: Activity,
            position: Int,
            dto: SafetyMeasuresDetailsDTO,
            clickListenerText: OnTextItemClickListener,
            imageItemClick: OnImageItemClickListener,
            removeDetailItemClick: OnRemoveDetailItemClickListener
        ) {

//            dto.binding = binding
            binding.detailImg.clipToOutline = true

            if (dto.imageUri != null) {
                binding.detailImg.setImageURI(dto.imageUri)
            } else {
                binding.detailImg.setImageDrawable(activity.getDrawable(R.drawable.person_placeholder))
            }

            binding.safetyMeasuresDetailsDTO = dto

            binding.removeImg.setOnClickListener {
                removeDetailItemClick.onRemoveDetailItemClick(position, dto)
            }

/*
            binding.detailImg.setOnClickListener {
                imageItemClick.onImageItemClick(position, dto)
            }
*/

/*
            binding.titleEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                }

                override fun afterTextChanged(s: Editable) {
                    clickListenerText.onTextItemClick(position, dto)
                }
            })
*/


        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SafetyMeasuresDetailsItemCellLayoutBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return ViewHolder(binding)
            }
        }
    }

    interface OnImageItemClickListener {
        fun onImageItemClick(
            position: Int,
            safetyMeasuresDetailsDTO: SafetyMeasuresDetailsDTO
        )
    }

    // For adding binding to list
    interface OnTextItemClickListener {
        fun onTextItemClick(
            position: Int,
            safetyMeasuresDetailsDTO: SafetyMeasuresDetailsDTO
        )
    }

    // For adding binding to list
    interface OnRemoveDetailItemClickListener {
        fun onRemoveDetailItemClick(
            position: Int,
            safetyMeasuresDetailsDTO: SafetyMeasuresDetailsDTO
        )
    }


}