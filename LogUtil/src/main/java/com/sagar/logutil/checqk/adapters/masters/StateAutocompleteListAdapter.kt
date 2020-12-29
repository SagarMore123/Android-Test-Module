package com.sagar.logutil.checqk.adapters.masters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.checqk.model.StateMasterDTO
import com.sagar.logutil.databinding.AutocompleteTextItemCellLayoutBinding

class StateAutocompleteListAdapter(
    private val clickListener: OnItemClickListener
) :
    RecyclerView.Adapter<StateAutocompleteListAdapter.ViewHolder>() {

    var list = listOf<StateMasterDTO>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(position, item, clickListener)
    }

    class ViewHolder(private val binding: AutocompleteTextItemCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            position: Int,
            stateMasterDTO: StateMasterDTO,
            itemClick: OnItemClickListener
        ) {


            binding.text = stateMasterDTO.stateName

            binding.itemTxt.setOnClickListener {
                itemClick.onItemClick(position, stateMasterDTO)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    AutocompleteTextItemCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, stateMasterDTO: StateMasterDTO)

    }

}