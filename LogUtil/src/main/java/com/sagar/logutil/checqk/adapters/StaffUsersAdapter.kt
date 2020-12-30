package com.sagar.logutil.checqk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.checqk.model.UserDTO
import com.sagar.logutil.databinding.StaffUsersListCellLayoutBinding


class StaffUsersAdapter(
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<StaffUsersAdapter.StaffUserViewHolder>() {

    private var staffUserList = ArrayList<UserDTO>()
    private var status: Boolean = true

    fun setStaffUsersList(staffUserList: ArrayList<UserDTO>, status: Boolean) {
        this.staffUserList = staffUserList
        this.status = status
        notifyDataSetChanged()
    }

    class StaffUserViewHolder(private val binding: StaffUsersListCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            status: Boolean,
            staffUserDTO: UserDTO?,
            listener: OnItemClickListener
        ) {

            binding.status = status
            binding.staffUser = staffUserDTO

            binding.mainLayout.setOnClickListener {
//                val visi : Int = binding.contentLayout.visibility == View.GONE ? View.GONE : View.VISIBLE
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

            if (staffUserDTO != null) {
                binding.editImageView.setOnClickListener {
                    listener.onEditItemClick(position, staffUserDTO!!)
                }
                binding.deleteImageView.setOnClickListener {
                    listener.onDeleteItemClick(position, staffUserDTO!!)
                }

            }

        }

        companion object {
//            var prevSelectedGalleryImageCategory: GalleryImageCategory? = null

            fun from(parent: ViewGroup): StaffUserViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    StaffUsersListCellLayoutBinding.inflate(layoutInflater, parent, false)
                return StaffUserViewHolder(binding)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffUserViewHolder {


        return StaffUserViewHolder.from(parent)

    }

    override fun getItemCount(): Int = staffUserList.size

    override fun onBindViewHolder(holderMenu: StaffUserViewHolder, position: Int) {

        val menuCategoryItem = staffUserList[position]
        holderMenu.bind(context, position, status, menuCategoryItem, listener)

    }


    interface OnItemClickListener {
        fun onEditItemClick(
            position: Int,
            staffUser: UserDTO
        )

        fun onDeleteItemClick(
            position: Int,
            staffUser: UserDTO
        )
    }

}