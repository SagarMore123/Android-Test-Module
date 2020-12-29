package com.sagar.logutil.checqk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.astrika.checqk.model.SystemValueMasterDTO
import com.astrika.checqk.network.network_utils.SERVER_IMG_URL
import com.astrika.checqk.utils.Constants
import com.bumptech.glide.Glide
import com.sagar.logutil.databinding.TypeOfTableCellLayoutBinding

class TypeOfTableAdapter(val context:Context, val listener: OnItemClickListener) :
    RecyclerView.Adapter<TypeOfTableAdapter.TypeOfTableViewHolder>()  {

    private var arrayList = listOf<SystemValueMasterDTO>()

    fun submitList(updatedList: List<SystemValueMasterDTO>) {
        val oldList = arrayList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ItemDiffCallback(oldList, updatedList)
        )
        arrayList = updatedList
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()

    }

    class ItemDiffCallback(
        private var oldList: List<SystemValueMasterDTO>,
        private var newList: List<SystemValueMasterDTO>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].serialId == newList[oldItemPosition].serialId)
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].equals(newList[oldItemPosition]))
        }

    }



    class TypeOfTableViewHolder(val binding: TypeOfTableCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            systemValueMasterDTO: SystemValueMasterDTO,
            listener: OnItemClickListener
        ) {

            binding.typeOfTable = "${Constants.getTypeOfSeater(systemValueMasterDTO.name)} Seater"

            if(systemValueMasterDTO.value != null){
                Glide.with(context).load(SERVER_IMG_URL + systemValueMasterDTO.value).into(binding.imageView)
            }

            binding.root.setOnClickListener {
                listener.onItemClick(position,systemValueMasterDTO)
            }

        }

        companion object {

            fun from(parent: ViewGroup): TypeOfTableViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    TypeOfTableCellLayoutBinding.inflate(layoutInflater, parent, false)
                return TypeOfTableViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeOfTableViewHolder {
        return TypeOfTableViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TypeOfTableViewHolder, position: Int) {
        val typeOfTablesDTO = arrayList[position]
        holder.bind(context,position,typeOfTablesDTO, listener)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    interface OnItemClickListener {
        fun onItemClick(
            position: Int,systemValueMasterDTO: SystemValueMasterDTO)
    }

}