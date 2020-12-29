package com.sagar.logutil.checqk.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.checqk.model.SubAdminDTO
import com.sagar.logutil.R
import com.sagar.logutil.databinding.SubAdminCellLayoutBinding

class SubAdminAdapter(val listener: OnClickListener) :
    RecyclerView.Adapter<SubAdminAdapter.ViewHolder>() {

    private var subAdminList = ArrayList<SubAdminDTO>()

    fun setSubAdminList(subAdminList: ArrayList<SubAdminDTO>) {
        this.subAdminList = subAdminList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: SubAdminCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            position: Int,
            subAdminDTO: SubAdminDTO,
            listener: OnClickListener
        ) {

            binding.subAdminDTO = subAdminDTO

            binding.editSubAdmin.setOnClickListener {
                listener.onEditClick(subAdminDTO)
            }

            binding.headerLayout.setOnClickListener {
                if (binding.detailLayout.visibility == View.VISIBLE) {
                    binding.arrowImageView.setImageResource(R.drawable.ic_arrow_down)
                    binding.detailLayout.visibility = View.GONE
                } else {
                    binding.arrowImageView.setImageResource(R.drawable.ic_arrow_up)
                    binding.detailLayout.visibility = View.VISIBLE
                }
            }


            binding.removeSubAdmin.setOnClickListener {
                if (subAdminDTO.isActive == true) {
                    listener.onRemove(subAdminDTO.userId)
                } else {
                    listener.onRestore(subAdminDTO.userId)
                }
            }

        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    SubAdminCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return subAdminList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subAdminItem = subAdminList[position]
        holder.bind(position, subAdminItem, listener)

    }

    interface OnClickListener {
        fun onEditClick(subAdminDTO: SubAdminDTO)
        fun onRemove(userId: Long?)
        fun onRestore(userId: Long?)
    }


}