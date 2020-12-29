package com.sagar.logutil.checqk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.astrika.checqk.model.BookingDTO
import com.astrika.checqk.model.SystemValueMasterDTO
import com.astrika.checqk.model.TableManagementDTO
import com.astrika.checqk.network.network_utils.SERVER_IMG_URL
import com.astrika.checqk.utils.Constants
import com.bumptech.glide.Glide
import com.sagar.logutil.R
import com.sagar.logutil.databinding.ReservedTablesCellLayoutBinding

class ReservedTableAdapter(
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ReservedTableAdapter.ReservedTableViewHolder>() {

    private var tableList = ArrayList<BookingDTO>()
    private var occupiedTableArrayList = listOf<SystemValueMasterDTO>()
    private var vacantTableArrayList = listOf<SystemValueMasterDTO>()
    private var reservedTableArrayList = listOf<SystemValueMasterDTO>()
    private var tableManagementList = ArrayList<TableManagementDTO>()

    fun vacantTableArrayList(vacantTableArrayList: List<SystemValueMasterDTO>) {
        this.vacantTableArrayList = vacantTableArrayList
    }

    fun occupiedTableArrayList(occupiedTableArrayList: List<SystemValueMasterDTO>) {
        this.occupiedTableArrayList = occupiedTableArrayList
    }

    fun reservedTableArrayList(reservedTableArrayList: List<SystemValueMasterDTO>) {
        this.reservedTableArrayList = reservedTableArrayList
    }

    fun getTableSetup(tableManagementList: ArrayList<TableManagementDTO>) {
        this.tableManagementList = tableManagementList
    }


    fun setTableList(tableList : ArrayList<BookingDTO>) {
        this.tableList = tableList
        notifyDataSetChanged()
    }

    inner class ReservedTableViewHolder(val binding: ReservedTablesCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            position: Int,
            bookingDTO: BookingDTO) {

            binding.bookingDTO = bookingDTO

            //configure the table management setup for awaiting checkins
            if (tableManagementList.isNotEmpty()) {
                tableSetup(binding.tableSetupLayout, bookingDTO)
            }

            binding.assignTableBtn.setOnClickListener {
                listener.onItemClick(bookingDTO)
            }

        }
    }

    private fun tableSetup(tableSetupLayout: RelativeLayout, bookingDTO: BookingDTO) {
        if (tableManagementList.isNotEmpty()) {
            for (table in tableManagementList) {
                val inflater = LayoutInflater.from(context)
                val childLayout =
                    inflater.inflate(R.layout.table_cell_layout, tableSetupLayout, false)
                tableSetupLayout.addView(childLayout)
                val imageView = childLayout.findViewById<ImageView>(R.id.imageView)

                //if the table id is already allotted to customer(table occupied)
                if (table.occupied == true) {
                    childLayout.isEnabled = false
                    loadImage(table, true, imageView)
                } else {
                    loadImage(table, false, imageView)
                }

                if(table.reserved != null){
                    if(table.reserved == true){
                        childLayout.isEnabled = false
                    }
                }

                /*if (table.capacity != 0L) {
                    val typeOfSeater =
                        Constants.getTypeOfSeaterInFullName(table.capacity.toString())
                    for (item in unOccupiedTableArrayList) {
                        if (item.name != null) {
                            if (item.name!!.startsWith(typeOfSeater)) {
                                Glide.with(context)
                                    .load(SERVER_IMG_URL + item.value).into(imageView)
                                break
                            }
                        }
                    }
                }*/
                val tableNameTxt = childLayout.findViewById<TextView>(R.id.tableNameTxt)
                tableNameTxt.text = table.tableCode
                childLayout.x = (table.xcoordinate ?: 0f) - 20f
                val (tableSetupLayoutX,tableSetupLayoutY) = tableSetupLayout.screenLocationInWindow
                childLayout.y = (table.ycoordinate ?: 0f) - tableSetupLayoutY.toFloat()

//                childLayout.x = table.xcoordinate ?: 0f
//                childLayout.y = table.ycoordinate ?: 0f

                childLayout.setOnClickListener {
                    //color change and allocate seat
                    /*if (!tableSelected) {
                        loadImage(table, true, imageView)
                        if (table.tableId != null) {
                            tableIdsAlloted.add(table.tableId!!.toInt())
                            tableSelected = true
                        }
                    } else if (tableSelected) {
                        loadImage(table, false, imageView)
                        tableIdsAlloted.remove(table.tableId!!.toInt())
                        tableSelected = false
                    }
                    checkInDTO.tableIdsAlloted = tableIdsAlloted*/
                }
            }
        }

    }

    private fun loadImage(table: TableManagementDTO, isOccupied: Boolean, imageView: ImageView) {
        if (table.capacity != 0L) {
            val typeOfSeater =
                Constants.getTypeOfSeaterInFullName(table.capacity.toString())
            val list: List<SystemValueMasterDTO> = if (isOccupied) {
                occupiedTableArrayList
            } else {
                vacantTableArrayList
            }
            for (item in list) {
                if (item.name != null) {
                    if (item.name!!.startsWith(typeOfSeater)) {
                        Glide.with(context)
                            .load(SERVER_IMG_URL + item.value).into(imageView)
                        break
                    }
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservedTableViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            ReservedTablesCellLayoutBinding.inflate(layoutInflater, parent, false)
        return ReservedTableViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tableList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun onBindViewHolder(holder: ReservedTableViewHolder, position: Int) {

        val bookingDTO = tableList[position]
        holder.bind(context, position,bookingDTO)
    }


    interface OnItemClickListener {
        //on assign table click
        fun onItemClick(bookingDTO: BookingDTO)
    }

    val View.screenLocationInWindow get():IntArray{
        val point = IntArray(2)
        getLocationInWindow(point)
        return point
    }

}
