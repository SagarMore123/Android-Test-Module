package com.sagar.logutil.checqk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.R
import com.sagar.logutil.checqk.model.DashboardDrawerDTO
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.databinding.DashboardDrawerCellLayoutBinding

class DashboardDrawerAdapter(
    private val context: Context,
    private val listener: OnItemClickListener,
    private val subListener: DashboardSubMenuAdapter.OnSubMenuItemClickListener
) : RecyclerView.Adapter<DashboardDrawerAdapter.DashboardDrawerViewHolder>() {

    private var dashboardDrawerList = ArrayList<DashboardDrawerDTO>()

    fun setDrawerDashboardList(dashboardDrawerList: ArrayList<DashboardDrawerDTO>) {
        this.dashboardDrawerList = dashboardDrawerList
        notifyDataSetChanged()
    }

    class DashboardDrawerViewHolder(val binding: DashboardDrawerCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            dashboardDrawerDTO: DashboardDrawerDTO,
            listener: OnItemClickListener,
            subListener: DashboardSubMenuAdapter.OnSubMenuItemClickListener
        ) {
            binding.dashboardDrawerDTO = dashboardDrawerDTO

            val adapter = DashboardSubMenuAdapter(context, subListener)
            binding.subItemsRecyclerView.adapter = adapter
            if (!dashboardDrawerDTO.applicationModules.isNullOrEmpty()) {
                adapter.setDrawerDashboardSubMenuList(
                    position,
                    dashboardDrawerDTO.applicationModules as ArrayList<DashboardDrawerDTO>
                )
            }

            if (dashboardDrawerDTO.name == Constants.LOG_OUT) {
                binding.dropdownIcon.visibility = View.GONE
            }

            binding.titleLayout.setOnClickListener {
                if (dashboardDrawerDTO.name != Constants.LOG_OUT) {
                    if (binding.subItemsRecyclerView.visibility == View.VISIBLE) {
                        binding.subItemsRecyclerView.visibility = View.GONE
                        binding.dropdownIcon.setImageDrawable(context.resources.getDrawable(R.drawable.ic_arrow_down))
                    } else {
                        binding.subItemsRecyclerView.visibility = View.VISIBLE
                        binding.dropdownIcon.setImageDrawable(context.resources.getDrawable(R.drawable.ic_arrow_up))
                    }
                } else {
                    dashboardDrawerDTO.selected = !dashboardDrawerDTO.selected
                    listener.onItemClick(dashboardDrawerDTO)
                }

            }

        }

        companion object {

            fun from(parent: ViewGroup): DashboardDrawerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    DashboardDrawerCellLayoutBinding.inflate(layoutInflater, parent, false)
                return DashboardDrawerViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardDrawerViewHolder {
        return DashboardDrawerViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return dashboardDrawerList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        val dashboardDrawerDTO = dashboardDrawerList[position]
        return dashboardDrawerDTO.moduleId.toInt()
    }


    override fun onBindViewHolder(holder: DashboardDrawerViewHolder, position: Int) {

        val dashboardDrawerDTO = dashboardDrawerList[position]
        holder.bind(context, position, dashboardDrawerDTO, listener, subListener)
    }


    interface OnItemClickListener {
        fun onItemClick(dashboardDrawerDTO: DashboardDrawerDTO)
    }

}