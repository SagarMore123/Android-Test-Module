package com.sagar.logutil.checqk.adapters.basicinfo

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.checqk.master_controller.source.MasterRepository
import com.sagar.logutil.checqk.model.CuisineMasterDTO
import com.sagar.logutil.checqk.model.FamousDishesDTO
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.databinding.FamousDishesItemCellLayoutBinding

class FamousDishesAdapter(
    private var mActivity: Activity,
    private var clickListenerRemove: OnRemoveItemClickListener
) :
    RecyclerView.Adapter<FamousDishesAdapter.ViewHolder>() {

    private var arrayList = listOf<FamousDishesDTO>()

    fun submitList(updatedList: List<FamousDishesDTO>) {
        val oldList = arrayList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ItemDiffCallback(oldList, updatedList)
        )
        arrayList = updatedList
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()

    }


    class ItemDiffCallback(
        private var oldList: List<FamousDishesDTO>,
        private var newList: List<FamousDishesDTO>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].cuisineId == newList[oldItemPosition].cuisineId)
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = arrayList[position]
        holder.bind(mActivity, position, item, clickListenerRemove)
    }

    class ViewHolder(private val binding: FamousDishesItemCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            activity: Activity,
            position: Int,
            dto: FamousDishesDTO,
            removeItemClick: OnRemoveItemClickListener
        ) {

            val masterRepository = MasterRepository.getInstance(activity)

            if (dto.cuisineId != null && dto.cuisineId != 0L) {
                masterRepository?.fetchCuisineMasterDataById(
                    dto.cuisineId,
                    object : IDataSourceCallback<CuisineMasterDTO> {
                        override fun onDataFound(data: CuisineMasterDTO) {
                            dto.cuisineName = data.cuisineName ?: ""
                            binding.famousDishesDTO = dto
                        }

                        override fun onError(error: String) {
                            binding.famousDishesDTO = dto
                        }

                    })
            } else {
                dto.cuisineName = ""
                binding.famousDishesDTO = dto
            }


            binding.removeImg.setOnClickListener {
                removeItemClick.onRemoveItemClick(position, dto)
            }

        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    FamousDishesItemCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    interface OnRemoveItemClickListener {
        fun onRemoveItemClick(
            position: Int,
            cuisineMasterDTO: FamousDishesDTO
        )
    }


}