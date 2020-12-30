package com.sagar.logutil.checqk.adapters.masters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.checqk.model.discount.CorporateMembershipOneDashboardDTO
import com.sagar.logutil.databinding.AutocompleteTextItemCellLayoutBinding

class CorporatesMembershipOneDashboardAutocompleteListAdapter(
    private val clickListener: OnItemClickListener
) :
    RecyclerView.Adapter<CorporatesMembershipOneDashboardAutocompleteListAdapter.ViewHolder>() {

    var list = listOf<CorporateMembershipOneDashboardDTO>()
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
            dto: CorporateMembershipOneDashboardDTO,
            itemClick: OnItemClickListener
        ) {


            binding.text = dto.corporateName ?: ""

            binding.itemTxt.setOnClickListener {
                itemClick.onItemClick(position, dto)
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
        fun onItemClick(position: Int, dto: CorporateMembershipOneDashboardDTO)

    }

}