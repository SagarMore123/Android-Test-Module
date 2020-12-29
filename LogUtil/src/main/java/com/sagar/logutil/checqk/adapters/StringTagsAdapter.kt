package com.sagar.logutil.checqk.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagar.logutil.databinding.FamousForItemCellLayoutBinding

class StringTagsAdapter(
    private var mActivity: Activity,
    private var adapterType: String,
    private var clickListener: OnRemoveItemClickListener
) :
    RecyclerView.Adapter<StringTagsAdapter.ViewHolder>() {

    var list = listOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(mActivity, position, item, adapterType, clickListener)
    }

    class ViewHolder(private val binding: FamousForItemCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            activity: Activity,
            position: Int,
            string: String,
            adapterType: String,
            itemClick: OnRemoveItemClickListener
        ) {

            binding.string = string

            binding.removeImg.setOnClickListener {
                itemClick.onStringRemoveItemClick(position, adapterType, string)
            }
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    FamousForItemCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    interface OnRemoveItemClickListener {
        fun onStringRemoveItemClick(
            position: Int,
            adapterType: String,
            string: String
        )
    }


}