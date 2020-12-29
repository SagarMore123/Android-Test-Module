package com.sagar.logutil.checqk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.checqk.model.PermissionDTO
import com.sagar.logutil.R
import com.sagar.logutil.databinding.AccessRoleListCellLayoutBinding


class AccessRolesAdapter(
    private val context: Context
) : RecyclerView.Adapter<AccessRolesAdapter.AccessRoleViewHolder>() {

    private var permissionParentList = ArrayList<PermissionDTO>()

    fun setparentPermissionList(permissionParentList: ArrayList<PermissionDTO>) {
        this.permissionParentList = permissionParentList
        notifyDataSetChanged()
    }

    class AccessRoleViewHolder(private val binding: AccessRoleListCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            permissionParent: PermissionDTO
        ) {

            binding.permissionDto = permissionParent

            val permissionAdapter = PermissionAdapter(context, binding)
            binding.permissionRecyclerView.adapter = permissionAdapter
            permissionAdapter.setPermissionList(permissionParent.permissions!!)


            binding.expandImageView.setOnClickListener{
//                listener.onEditItemClick(position, menuSection)
                if (binding.permissionListLayout.visibility == View.VISIBLE){
                    binding.permissionListLayout.visibility = View.GONE
                    binding.expandImageView.setImageDrawable(
                        context.resources.getDrawable(
                            R.drawable.ic_arrow_down
                        )
                    )
                }
                else{
                    binding.permissionListLayout.visibility = View.VISIBLE
                    binding.expandImageView.setImageDrawable(
                        context.resources.getDrawable(
                            R.drawable.ic_arrow_up
                        )
                    )
                }
            }

            binding.viewCheckBox.setOnClickListener{
                if (permissionParent.viewModule == null ){
                    permissionParent.viewModule = true
                    binding.viewCheckBox.setBackgroundDrawable(
                        context.resources.getDrawable(
                            R.drawable.ic_check,
                            null
                        )
                    )
                    checkedAllViewPermission(permissionParent, permissionAdapter,true)
                }else{
                    if(permissionParent.viewModule!!){
                        permissionParent.viewModule = false
                        binding.viewCheckBox.setBackgroundDrawable(
                            context.resources.getDrawable(
                                R.drawable.ic_baseline_grey_lens_24,
                                null
                            )
                        )
                        checkedAllViewPermission(permissionParent, permissionAdapter,false)
                    }else{
                        permissionParent.viewModule = true
                        binding.viewCheckBox.setBackgroundDrawable(
                            context.resources.getDrawable(
                                R.drawable.ic_check,
                                null
                            )
                        )
                        checkedAllViewPermission(permissionParent, permissionAdapter,true)
                    }

                }
            }
            binding.addNewCheckBox.setOnClickListener{
               /* if (binding.addNewCheckBox.isChecked){
                    checkedAllAddPermission(permissionParent, permissionAdapter,true)
                }else{
                    checkedAllAddPermission(permissionParent, permissionAdapter,false)
                }*/
                if (permissionParent.addition == null ){
                    permissionParent.addition = true
                    binding.addNewCheckBox.setBackgroundDrawable(
                        context.resources.getDrawable(
                            R.drawable.ic_check,
                            null
                        )
                    )
                    checkedAllAddPermission(permissionParent, permissionAdapter,true)
                }else{
                    if(permissionParent.addition!!){
                        permissionParent.addition = false
                        binding.addNewCheckBox.setBackgroundDrawable(
                            context.resources.getDrawable(
                                R.drawable.ic_baseline_grey_lens_24,
                                null
                            )
                        )
                        checkedAllAddPermission(permissionParent, permissionAdapter,false)
                    }else{
                        permissionParent.addition = true
                        binding.addNewCheckBox.setBackgroundDrawable(
                            context.resources.getDrawable(
                                R.drawable.ic_check,
                                null
                            )
                        )
                        checkedAllAddPermission(permissionParent, permissionAdapter,true)
                    }

                }
            }
            binding.updateCheckBox.setOnClickListener{
                /*if (binding.updateCheckBox.isChecked){
                    checkedAllEditPermission(permissionParent, permissionAdapter,true)
                }else{
                    checkedAllEditPermission(permissionParent, permissionAdapter,false)
                }*/
                if (permissionParent.edit == null ){
                    permissionParent.edit = true
                    binding.updateCheckBox.setBackgroundDrawable(
                        context.resources.getDrawable(
                            R.drawable.ic_check,
                            null
                        )
                    )
                    checkedAllEditPermission(permissionParent, permissionAdapter,true)
                }else{
                    if(permissionParent.edit!!){
                        permissionParent.edit = false
                        binding.updateCheckBox.setBackgroundDrawable(
                            context.resources.getDrawable(
                                R.drawable.ic_baseline_grey_lens_24,
                                null
                            )
                        )
                        checkedAllEditPermission(permissionParent, permissionAdapter,false)
                    }else{
                        permissionParent.edit = true
                        binding.updateCheckBox.setBackgroundDrawable(
                            context.resources.getDrawable(
                                R.drawable.ic_check,
                                null
                            )
                        )
                        checkedAllEditPermission(permissionParent, permissionAdapter,true)
                    }

                }
            }

          /*  binding.inactiveListArrowImageView.setOnClickListener{
                showAndHide(context, binding)
            }*/

        }

        companion object {
//            var prevSelectedGalleryImageCategory: GalleryImageCategory? = null

            fun from(parent: ViewGroup): AccessRoleViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    AccessRoleListCellLayoutBinding.inflate(layoutInflater, parent, false)
                return AccessRoleViewHolder(binding)
            }

            fun checkedAllViewPermission(permissionParent : PermissionDTO, permissionAdapter : PermissionAdapter, checked : Boolean){
                for(permission in permissionParent.permissions!!){
                    permission.viewModule = checked
                }
                permissionAdapter.setPermissionList(permissionParent.permissions!!)
            }
            fun checkedAllAddPermission(permissionParent : PermissionDTO, permissionAdapter : PermissionAdapter, checked : Boolean){
                for(permission in permissionParent.permissions!!){
                    permission.addition = checked
                }
                permissionAdapter.setPermissionList(permissionParent.permissions!!)
            }
            fun checkedAllEditPermission(permissionParent : PermissionDTO, permissionAdapter : PermissionAdapter, checked : Boolean){
                for(permission in permissionParent.permissions!!){
                    permission.edit = checked
                }
                permissionAdapter.setPermissionList(permissionParent.permissions!!)
            }
        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccessRoleViewHolder {
        return AccessRoleViewHolder.from(parent)
    }

    override fun getItemCount(): Int = permissionParentList.size

    override fun onBindViewHolder(holderMenu: AccessRoleViewHolder, position: Int) {

        val accessRoleDTO = permissionParentList[position]
//        holderMenu.bind(context, position, menuCategoryItem, dishAddClickListener, dishItemClickListener)
        holderMenu.bind(context, position, accessRoleDTO)

    }


    interface OnAccessRoleClickListener {
       /* fun onAddItemClick(
            position: Int,
            dishMenuSection: DishWithSectionDetails
        )
        fun onInactiveListClick(
            position: Int,
            dishMenuSection: DishWithSectionDetails
        )*/
    }

}