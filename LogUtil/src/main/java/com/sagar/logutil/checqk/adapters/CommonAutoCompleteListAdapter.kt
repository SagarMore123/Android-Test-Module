package com.sagar.logutil.checqk.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.checqk.model.CommonDialogDTO
import com.sagar.logutil.databinding.CommonAutoCompleteTextItemCellLayoutBinding

class CommonAutoCompleteListAdapter(
    private val clickListener: OnItemClickListener
) :
    RecyclerView.Adapter<CommonAutoCompleteListAdapter.ViewHolder>() {

    var list = listOf<CommonDialogDTO>()
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(position, item, clickListener)
    }

    class ViewHolder(private val binding: CommonAutoCompleteTextItemCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            position: Int,
            dto: CommonDialogDTO,
            itemClick: OnItemClickListener
        ) {

            binding.commonDto = dto
            binding.itemTxt.setOnClickListener {
                itemClick.onItemClick(position, dto)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    CommonAutoCompleteTextItemCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, commonDialogDTO: CommonDialogDTO)

    }

}