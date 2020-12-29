package com.sagar.logutil.checqk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.checqk.model.PermissionDTO
import com.sagar.logutil.R
import com.sagar.logutil.databinding.AccessRoleListCellLayoutBinding
import com.sagar.logutil.databinding.PermissionListCellLayoutBinding


class PermissionAdapter(
    private val context: Context,
    private val parentBinding: AccessRoleListCellLayoutBinding
) : RecyclerView.Adapter<PermissionAdapter.PermissionViewHolder>() {

    private var permissionList = ArrayList<PermissionDTO>()

    fun setPermissionList(permissionList: ArrayList<PermissionDTO>) {
        this.permissionList = permissionList
        notifyDataSetChanged()
    }

    class PermissionViewHolder(private val binding: PermissionListCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
//            permissionDTO: PermissionDTO,
            permissionList: ArrayList<PermissionDTO>,
            parentBinding: AccessRoleListCellLayoutBinding
        ) {
            val permissionDTO = permissionList[position]
            binding.permissionDTO = permissionDTO
            if (permissionDTO.viewModule == true) {
                binding.viewCheckBox.setBackgroundDrawable(
                    context.resources.getDrawable(
                        R.drawable.ic_check,
                        null
                    )
                )
            } else {
                binding.viewCheckBox.setBackgroundDrawable(
                    context.resources.getDrawable(
                        R.drawable.ic_baseline_grey_lens_24,
                        null
                    )
                )
            }
            if (permissionDTO.edit == true) {
                binding.updateCheckBox.setBackgroundDrawable(
                    context.resources.getDrawable(
                        R.drawable.ic_check,
                        null
                    )
                )
            } else {
                binding.updateCheckBox.setBackgroundDrawable(
                    context.resources.getDrawable(
                        R.drawable.ic_baseline_grey_lens_24,
                        null
                    )
                )
            }
            if (permissionDTO.addition == true) {
                binding.addNewCheckBox.setBackgroundDrawable(
                    context.resources.getDrawable(
                        R.drawable.ic_check,
                        null
                    )
                )
            } else {
                binding.addNewCheckBox.setBackgroundDrawable(
                    context.resources.getDrawable(
                        R.drawable.ic_baseline_grey_lens_24,
                        null
                    )
                )
            }



            binding.viewCheckBox.setOnClickListener {
                if (permissionDTO.viewModule == null) {
                    permissionDTO.viewModule = true
                    binding.viewCheckBox.setBackgroundDrawable(
                        context.resources.getDrawable(
                            R.drawable.ic_check,
                            null
                        )
                    )

                } else {
                    if (permissionDTO.viewModule == true) {
                        permissionDTO.viewModule = false
                        binding.viewCheckBox.setBackgroundDrawable(
                            context.resources.getDrawable(
                                R.drawable.ic_baseline_grey_lens_24,
                                null
                            )
                        )

                    } else {
                        permissionDTO.viewModule = true
                        binding.viewCheckBox.setBackgroundDrawable(
                            context.resources.getDrawable(
                                R.drawable.ic_check,
                                null
                            )
                        )
                    }

                }
                changeParentSelection(context, permissionList, parentBinding)
            }

            binding.addNewCheckBox.setOnClickListener {
                if (permissionDTO.addition == null) {
                    permissionDTO.addition = true
                    binding.addNewCheckBox.setBackgroundDrawable(
                        context.resources.getDrawable(
                            R.drawable.ic_check,
                            null
                        )
                    )

                } else {
                    if (permissionDTO.addition!!) {
                        permissionDTO.addition = false
                        binding.addNewCheckBox.setBackgroundDrawable(
                            context.resources.getDrawable(
                                R.drawable.ic_baseline_grey_lens_24,
                                null
                            )
                        )

                    } else {
                        permissionDTO.addition = true
                        binding.addNewCheckBox.setBackgroundDrawable(
                            context.resources.getDrawable(
                                R.drawable.ic_check,
                                null
                            )
                        )
                    }

                }
                changeParentSelection(context, permissionList, parentBinding)

            }

            binding.updateCheckBox.setOnClickListener {
                if (permissionDTO.edit == null) {
                    permissionDTO.edit = true
                    binding.updateCheckBox.setBackgroundDrawable(
                        context.resources.getDrawable(
                            R.drawable.ic_check,
                            null
                        )
                    )

                } else {
                    if (permissionDTO.edit!!) {
                        permissionDTO.edit = false
                        binding.updateCheckBox.setBackgroundDrawable(
                            context.resources.getDrawable(
                                R.drawable.ic_baseline_grey_lens_24,
                                null
                            )
                        )

                    } else {
                        permissionDTO.edit = true
                        binding.updateCheckBox.setBackgroundDrawable(
                            context.resources.getDrawable(
                                R.drawable.ic_check,
                                null
                            )
                        )
                    }

                }
                changeParentSelection(context, permissionList, parentBinding)

            }

        }

        companion object {

            fun from(parent: ViewGroup): PermissionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    PermissionListCellLayoutBinding.inflate(layoutInflater, parent, false)
                return PermissionViewHolder(binding)
            }

            fun changeParentSelection(
                context: Context, permissionList: ArrayList<PermissionDTO>,
                parentBinding: AccessRoleListCellLayoutBinding
            ) {
                var viewAllSelected = 0
                var editAllSelected = 0
                var addAllSelected = 0
                for (permission in permissionList) {
                    if (permission.viewModule == true) {
                        viewAllSelected++
                    }
                    if (permission.edit == true) {
                        editAllSelected++
                    }
                    if (permission.addition == true) {
                        addAllSelected++
                    }
                }
                if (viewAllSelected > 0) {
                    if (viewAllSelected == permissionList.size) {
                        parentBinding.viewCheckBox.setBackgroundDrawable(
                            context.resources.getDrawable(
                                R.drawable.ic_check
                            )
                        )
                    } else {
                        parentBinding.viewCheckBox.setBackgroundDrawable(
                            context.resources.getDrawable(
                                R.drawable.ic_signup_progress_select
                            )
                        )
                    }
                } else {
                    parentBinding.viewCheckBox.setBackgroundDrawable(context.resources.getDrawable(R.drawable.ic_baseline_grey_lens_24))
                }

                if (addAllSelected > 0) {
                    if (addAllSelected.equals(permissionList.size)) {
                        parentBinding.addNewCheckBox.setBackgroundDrawable(
                            context.resources.getDrawable(
                                R.drawable.ic_check,
                                null
                            )
                        )
                    } else {
                        parentBinding.addNewCheckBox.setBackgroundDrawable(
                            context.resources.getDrawable(
                                R.drawable.ic_signup_progress_select,
                                null
                            )
                        )
                    }
                } else {
                    parentBinding.addNewCheckBox.setBackgroundDrawable(
                        context.resources.getDrawable(
                            R.drawable.ic_baseline_grey_lens_24,
                            null
                        )
                    )
                }

                if (editAllSelected > 0) {
                    if (editAllSelected.equals(permissionList.size)) {
                        parentBinding.updateCheckBox.setBackgroundDrawable(
                            context.resources.getDrawable(
                                R.drawable.ic_check,
                                null
                            )
                        )
                    } else {
                        parentBinding.updateCheckBox.setBackgroundDrawable(
                            context.resources.getDrawable(
                                R.drawable.ic_signup_progress_select,
                                null
                            )
                        )
                    }
                } else {
                    parentBinding.updateCheckBox.setBackgroundDrawable(
                        context.resources.getDrawable(
                            R.drawable.ic_baseline_grey_lens_24,
                            null
                        )
                    )
                }
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermissionViewHolder {
        return PermissionViewHolder.from(parent)
    }

    override fun getItemCount(): Int = permissionList.size

    override fun onBindViewHolder(holderMenu: PermissionViewHolder, position: Int) {

//        val permissionDTO = permissionList[position]
//        holderMenu.bind(context, position, menuCategoryItem, dishAddClickListener, dishItemClickListener)
        holderMenu.bind(context, position, permissionList, parentBinding)

    }


    /*interface OnPermissionClickListener {
        fun onAddItemClick(
            position: Int,
            dishMenuSection: DishWithSectionDetails
        )
        fun onInactiveListClick(
            position: Int,
            dishMenuSection: DishWithSectionDetails
        )
    }*/

}