package com.sagar.logutil.checqk.adapters.discount

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.astrika.checqk.model.DayDTO
import com.astrika.checqk.model.DiscountDaysTimingDTO
import com.sagar.logutil.R
import com.sagar.logutil.databinding.DiscountDayTimingItemCellLayoutBinding
import com.sagar.logutil.databinding.DiscountTimingItemCellLayoutBinding
import java.util.*
import kotlin.Comparator


class DiscountDaysAdapter(
    private var mActivity: Activity,
    private var clickAddTimeSlot: OnAddTimeSlotItemClick,
    private var onTimingItemEditClickListener: OnTimingItemEditClickListener,
    private var onTimingItemDeleteClickListener: OnTimingItemDeleteClickListener
) :
    RecyclerView.Adapter<DiscountDaysAdapter.ViewHolder>() {

    var arrayList = listOf<DayDTO>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var updatedArrayList = arrayListOf<DayDTO>()


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
        if (item.allDaySameDiscountFlag) {
            holder.bind(mActivity, position, item, clickAddTimeSlot)
        } else {
            holder.addTimingLayout(
                mActivity,
                position,
                item,
                onTimingItemEditClickListener,
                onTimingItemDeleteClickListener
            )
        }
    }

    class ViewHolder(private val binding: DiscountDayTimingItemCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            activity: Activity,
            position: Int,
            dayDTO: DayDTO,
            addTimeSlotItemClick: OnAddTimeSlotItemClick
        ) {

            binding.activity = activity
            binding.dayDTO = dayDTO

            val item = dayDTO.discountDayAndTimings[0]
            if (item.assuredDiscount != 0L) {
                item.assuredDiscountString = item.assuredDiscount.toString()
            }
            if (item.discountApplicable != 0L) {
                item.discountApplicableString = item.discountApplicable.toString()
            }

            binding.discountDaysTimingDTO = item
            binding.addTimeSlotTxt.setOnClickListener {

               val dayDTO1 = binding.dayDTO
                if (dayDTO1 != null) {
                    addTimeSlotItemClick.onAddTimeSlotItemClick(position, dayDTO1)
                }
            }


        }


        fun addTimingLayout(
            activity: Activity,
            mainContainerPosition: Int,
            dayDTO: DayDTO,
            onTimingItemEditClickListener: OnTimingItemEditClickListener,
            onTimingItemDeleteClickListener: OnTimingItemDeleteClickListener

        ) {

            binding.activity = activity
            binding.dayDTO = dayDTO
            if (!dayDTO.discountDayAndTimings.isNullOrEmpty()) {
                binding.discountTimingLayout.removeAllViews()
                Collections.sort(dayDTO.discountDayAndTimings, CustomSort())
                for ((i, item) in dayDTO.discountDayAndTimings.withIndex()) {

                    if (!item.startsAt.isNullOrBlank()) {
                        item.allDaySameDiscountFlag = dayDTO.allDaySameDiscountFlag
                        item.weekDay = dayDTO.dayId
                        item.startsAt = item.startsAt?:"00:00 AM"
                        item.endsAt = item.endsAt?:"00:00 AM"

                        val timingLayout = TimingLayout(
                            i,
                            mainContainerPosition,
                            activity,
                            item,
                            binding,
                            onTimingItemEditClickListener,
                            onTimingItemDeleteClickListener
                        )
                        binding.discountTimingLayout.addView(timingLayout)
                    }
                }
            } else {
                binding.discountTimingLayout.removeAllViews()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    DiscountDayTimingItemCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }


    }

    interface OnAddTimeSlotItemClick {
        fun onAddTimeSlotItemClick(
            position: Int,
            dayDTO: DayDTO
        )
    }


    @SuppressLint("ViewConstructor")
    class TimingLayout(
        itemPosition: Int,
        mainContainerPosition: Int,
        activity: Activity,
        discountDaysTimingDTO: DiscountDaysTimingDTO?,
        dayTimingListItemCellLayoutBinding: DiscountDayTimingItemCellLayoutBinding,
        onTimingItemEditClickListener: OnTimingItemEditClickListener,
        onTimingItemDeleteClickListener: OnTimingItemDeleteClickListener
    ) : LinearLayout(activity) {

        private var discountTimingItemCellLayoutBinding: DiscountTimingItemCellLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.discount_timing_item_cell_layout,
                dayTimingListItemCellLayoutBinding.discountTimingLayout,
                true
            )

        init {

            if (discountDaysTimingDTO != null) {

                if (discountDaysTimingDTO.assuredDiscount != 0L) {
                    discountDaysTimingDTO.assuredDiscountString = discountDaysTimingDTO.assuredDiscount.toString()
                }
                if (discountDaysTimingDTO.discountApplicable != 0L) {
                    discountDaysTimingDTO.discountApplicableString = discountDaysTimingDTO.discountApplicable.toString()
                }
                discountTimingItemCellLayoutBinding.discountTimingDTO = discountDaysTimingDTO


                discountTimingItemCellLayoutBinding.editImg.setOnClickListener {
                    onTimingItemEditClickListener.onTimingItemEditClick(
                        itemPosition,
                        mainContainerPosition,
                        discountDaysTimingDTO
                    )
                }

                discountTimingItemCellLayoutBinding.deleteImg.setOnClickListener {
                    onTimingItemDeleteClickListener.onTimingDeleteItemClick(
                        itemPosition,
                        mainContainerPosition,
                        discountDaysTimingDTO
                    )
                }

            }

        }

    }



    interface OnTimingItemDeleteClickListener {
        fun onTimingDeleteItemClick(
            position: Int,
            mainContainerPosition: Int,
            discountDaysTimingDTO: DiscountDaysTimingDTO
        )
    }

    interface OnTimingItemEditClickListener {
        fun onTimingItemEditClick(
            position: Int,
            mainContainerPosition: Int,
            discountDaysTimingDTO: DiscountDaysTimingDTO
        )
    }


    class CustomSort : Comparator<DiscountDaysTimingDTO> {
        override fun compare(o1: DiscountDaysTimingDTO, o2: DiscountDaysTimingDTO): Int {

            return if (o1.startsAt != null && o2.startsAt != null) {
                o1.startsAt.compareTo(o2.startsAt)
            } else {
                when {
                    o1.startsAt == null -> {
                        -1
                    }
                    o2.startsAt == null -> {
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