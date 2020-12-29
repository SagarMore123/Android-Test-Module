package com.sagar.logutil.checqk.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.astrika.checqk.model.DayDTO
import com.astrika.checqk.model.TimingDTO
import com.sagar.logutil.R
import com.sagar.logutil.databinding.DayTimingListItemCellLayoutBinding
import com.sagar.logutil.databinding.TimingItemCellLayoutBinding
import java.util.*
import kotlin.Comparator


class TimingsAdapter(
    private var mActivity: Context,
    private var clickListener: OnItemClickListener,
    private var timingClickListener: OnTimingItemClickListener
) :
    RecyclerView.Adapter<TimingsAdapter.ViewHolder>() {

    var arrayList = listOf<DayDTO>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var updatedArrayList = arrayListOf<DayDTO>()
    var singleTiming = false

    fun submitList(updatedList: List<DayDTO>) {

        updatedArrayList.clear()
        val oldList = arrayList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ItemDiffCallback(oldList, updatedList)
        )
        arrayList = updatedList
        diffResult.dispatchUpdatesTo(this)

        Collections.sort(
            arrayList,
            Comparator<DayDTO> { lhs, rhs -> // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                if (lhs.dayId < rhs.dayId) -1 else if (lhs.dayId > rhs.dayId) 1 else 0
            })

        notifyDataSetChanged()

    }


    class ItemDiffCallback(
        private var oldList: List<DayDTO>,
        private var newList: List<DayDTO>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].dayId == newList[oldItemPosition].dayId)
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
        holder.bind(mActivity, position, item, clickListener)
        holder.addTimingLayout(mActivity, singleTiming, position, item, timingClickListener)

/*
        if (!updatedArrayList.contains(item)) {
            updatedArrayList.add(item)
            holder.addTimingLayout(mActivity, position, item, timingClickListener)
        }
*/
    }

    class ViewHolder(private val binding: DayTimingListItemCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            activity: Context,
            position: Int,
            dayDTO: DayDTO,
            itemClick: OnItemClickListener
        ) {


            binding.dayDTO = dayDTO
            binding.markAsClosedImg.setOnClickListener {
                dayDTO.dayIsCheckedOrClosed = !dayDTO.dayIsCheckedOrClosed
                if(dayDTO.dayIsCheckedOrClosed){
                    binding.timingLayout.visibility = View.GONE
                }else{
                    binding.timingLayout.visibility = View.VISIBLE
                }
                itemClick.onMarkAsClosedItemClick(position, dayDTO)
            }
            if(!dayDTO.isMarkAsClosedVisibility){
                binding.markAsClosedImg.visibility = View.GONE
            }

        }


        fun addTimingLayout(
            activity: Context,
            singleTiming: Boolean,
            mainContainerPosition: Int,
            dayDTO: DayDTO,
            timingClickListener: OnTimingItemClickListener
        ) {

            if (!dayDTO.timings.isNullOrEmpty()) {
                binding.timingLayout.removeAllViews()
                Collections.sort(dayDTO.timings, CustomSort())
                for ((i, item) in dayDTO.timings.withIndex()) {
                    val timingLayout = TimingLayout(
                        i,
                        mainContainerPosition,
                        activity,
                        item,
                        binding,
                        timingClickListener
                    )
                    if (singleTiming){
//                        binding.dayLayout.addView(timingLayout)
                        binding.timeTextView.text = dayDTO.timings.get(0).opensAt + " - " + dayDTO.timings.get(0).closesAt
                        binding.timeTextView.visibility = View.VISIBLE
                        binding.removeImg.visibility = View.VISIBLE
                        binding.timingLayout.visibility = View.GONE
                        binding.removeImg.setOnClickListener {
                            timingClickListener.onTimingRemoveItemClick(
                                position,
                                mainContainerPosition,
                                dayDTO.timings.get(0)
                            )
                        }
                    } else{
                        binding.timingLayout.addView(timingLayout)
                    }

                }
            } else {
                binding.timingLayout.removeAllViews()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    DayTimingListItemCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }


    }

    interface OnItemClickListener {
        fun onMarkAsClosedItemClick(
            position: Int,
            dayDTO: DayDTO
        )
    }


    @SuppressLint("ViewConstructor")
    class TimingLayout(
        itemPosition: Int,
        mainContainerPosition: Int,
        activity: Context?,
        timingDTO: TimingDTO?,
        dayTimingListItemCellLayoutBinding: DayTimingListItemCellLayoutBinding,
        timingClickListener: OnTimingItemClickListener
    ) : LinearLayout(activity) {

        private var timingItemCellLayoutBinding: TimingItemCellLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.timing_item_cell_layout,
                dayTimingListItemCellLayoutBinding.timingLayout,
                true
            )

        init {

            if (timingDTO != null) {
                timingItemCellLayoutBinding.timingDTO = timingDTO

                timingItemCellLayoutBinding.removeImg.setOnClickListener {
                    timingClickListener.onTimingRemoveItemClick(
                        itemPosition,
                        mainContainerPosition,
                        timingDTO
                    )
                }

            }

        }

    }

    interface OnTimingItemClickListener {
        fun onTimingRemoveItemClick(
            position: Int,
            mainContainerPosition: Int,
            timingDTO: TimingDTO
        )
    }


    class CustomSort : Comparator<TimingDTO> {
        override fun compare(o1: TimingDTO, o2: TimingDTO): Int {

            return if (o1.opensAt != null && o2.opensAt != null) {
                o1.opensAt.compareTo(o2.opensAt)
            } else {
                when {
                    o1.opensAt == null -> {
                        -1
                    }
                    o2.opensAt == null -> {
                        1
                    }
                    else -> {
                        0
                    }
                }
            }
        }
    }


}