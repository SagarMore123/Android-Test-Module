package com.sagar.logutil.checqk.adapters.masters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.checqk.model.CityMasterDTO
import com.sagar.logutil.databinding.AutocompleteTextItemCellLayoutBinding

class CityAutocompleteListAdapter(
    private val clickListener: OnItemClickListener
) :
    RecyclerView.Adapter<CityAutocompleteListAdapter.ViewHolder>() {

    var list = listOf<CityMasterDTO>()
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
            cityMasterDTO: CityMasterDTO,
            itemClick: OnItemClickListener
        ) {


            binding.text = cityMasterDTO.cityName

            binding.itemTxt.setOnClickListener {
                itemClick.onItemClick(position, cityMasterDTO)
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
        fun onItemClick(position: Int, cityMasterDTO: CityMasterDTO)

    }

}