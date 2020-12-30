package com.sagar.logutil.checqk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.checqk.model.AccessRoleDTO
import com.sagar.logutil.databinding.SubAdminAccessRoleListCellLayoutBinding


class SubAdminAccessRoleAdapter(
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SubAdminAccessRoleAdapter.SubAdminAccessRoleViewHolder>() {

    private var groupRoleList = ArrayList<AccessRoleDTO>()
    private var status: Boolean = true

    fun setSubAdminAccessRoleList(accessRoleList: ArrayList<AccessRoleDTO>, status: Boolean) {
        this.groupRoleList = accessRoleList
        this.status = status
        notifyDataSetChanged()
    }

    class SubAdminAccessRoleViewHolder(private val binding: SubAdminAccessRoleListCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            status: Boolean,
            accessRole: AccessRoleDTO,
            listener: OnItemClickListener
        ) {

            binding.status = status
            binding.groupRole = accessRole


            binding.mainLayout.setOnClickListener {
                if (binding.contentLayout.visibility == View.VISIBLE)
                    binding.contentLayout.visibility = View.GONE
                else
                    binding.contentLayout.visibility = View.VISIBLE

            }
            binding.expandImageView.setOnClickListener {
                if (binding.contentLayout.visibility == View.VISIBLE)
                    binding.contentLayout.visibility = View.GONE
                else
                    binding.contentLayout.visibility = View.VISIBLE

            }
            binding.editImageView.setOnClickListener {
                listener.onEditItemClick(position, accessRole!!)
            }
            binding.deleteImageView.setOnClickListener {
                listener.onDeleteItemClick(position, accessRole!!)
            }


        }

        companion object {

            fun from(parent: ViewGroup): SubAdminAccessRoleViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    SubAdminAccessRoleListCellLayoutBinding.inflate(layoutInflater, parent, false)
                return SubAdminAccessRoleViewHolder(binding)
            }
        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubAdminAccessRoleViewHolder {


        return SubAdminAccessRoleViewHolder.from(parent)

    }

    override fun getItemCount(): Int = groupRoleList.size

    override fun onBindViewHolder(holderMenu: SubAdminAccessRoleViewHolder, position: Int) {

        val menuCategoryItem = groupRoleList[position]
        holderMenu.bind(context, position, status, menuCategoryItem, listener)

    }


    interface OnItemClickListener {
        fun onEditItemClick(
            position: Int,
            accessRole: AccessRoleDTO
        )

        fun onDeleteItemClick(
            position: Int,
            accessRole: AccessRoleDTO
        )
    }

}