package com.sagar.logutil.checqk.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.checqk.model.StaffSafetyReadingDTO
import com.sagar.logutil.databinding.StaffSafetyReadingCellLayoutBinding


class StaffSafetyReadingsAdapter(
    private val context: Context
) : RecyclerView.Adapter<StaffSafetyReadingsAdapter.StaffSafetyReadingViewHolder>() {

    private var staffUserList = ArrayList<StaffSafetyReadingDTO>()


    fun setStaffUsersList(staffUserList: ArrayList<StaffSafetyReadingDTO>) {
        this.staffUserList = staffUserList
        notifyDataSetChanged()
    }

    class StaffSafetyReadingViewHolder(private val binding: StaffSafetyReadingCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            staffSafetyReadingDTO: StaffSafetyReadingDTO
        ) {

            try {


                binding.staffSafetyReading = staffSafetyReadingDTO

                if (!staffSafetyReadingDTO.userName.isNullOrBlank()
                    && !staffSafetyReadingDTO.userName.equals("null", true)
                ) {
                    var initialLetters = ""
                    val array = staffSafetyReadingDTO.userName?.split(" ")
                    initialLetters = array?.get(0)?.substring(0, 1) ?: ""
                    if (array?.size ?: 0 > 1)
                        initialLetters += array?.get(1)?.substring(0, 1)
                    binding.initialLettersTextView.text = initialLetters
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            binding.temperatureEditText.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.isNotEmpty())
                        staffSafetyReadingDTO.tempReading = s.toString().toInt()
                    else
                        staffSafetyReadingDTO.tempReading = 0
                }
            })
/*
            binding.mainLayout.setOnClickListener{
//                val visi : Int = binding.contentLayout.visibility == View.GONE ? View.GONE : View.VISIBLE
                if(binding.contentLayout.visibility == View.VISIBLE)
                    binding.contentLayout.visibility = View.GONE
                else
                    binding.contentLayout.visibility = View.VISIBLE

            }*/


        }

        companion object {
//            var prevSelectedGalleryImageCategory: GalleryImageCategory? = null

            fun from(parent: ViewGroup): StaffSafetyReadingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    StaffSafetyReadingCellLayoutBinding.inflate(layoutInflater, parent, false)
                return StaffSafetyReadingViewHolder(binding)
            }
        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StaffSafetyReadingViewHolder {


        return StaffSafetyReadingViewHolder.from(parent)

    }

    override fun getItemCount(): Int = staffUserList.size

    override fun onBindViewHolder(holderMenu: StaffSafetyReadingViewHolder, position: Int) {

        val staffUserReading = staffUserList[position]
        holderMenu.bind(context, position, staffUserReading)

    }


    /*interface OnItemClickListener {
        fun onEditItemClick(
            position: Int,
            staffUser: UserDTO
        )
        fun onDeleteItemClick(
            position: Int,
            staffUser: UserDTO
        )
    }*/

}