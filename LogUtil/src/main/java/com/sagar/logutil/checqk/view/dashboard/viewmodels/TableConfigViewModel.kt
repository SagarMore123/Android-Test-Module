package com.sagar.logutil.checqk.view.dashboard.viewmodels

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.sagar.logutil.R
import com.sagar.logutil.checqk.master_controller.source.MasterRepository
import com.sagar.logutil.checqk.master_controller.source.daos.SystemValueMasterDao
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.network.network_utils.SERVER_IMG_URL
import com.sagar.logutil.checqk.source.DashboardRepository
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.GenericBaseObservable
import io.github.hyuwah.draggableviewlib.Draggable
import io.github.hyuwah.draggableviewlib.DraggableListener
import io.github.hyuwah.draggableviewlib.makeDraggable


class TableConfigViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var masterRepository: MasterRepository,
    private var dashboardRepository: DashboardRepository
) : GenericBaseObservable(application, owner, view) {

    var showProgressBar = MutableLiveData<Boolean>()

    var typeOfTableArrayList = ArrayList<SystemValueMasterDTO>()
    var typeOfTableListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()

    var tableManagementList = ArrayList<TableManagementDTO>()
    var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)
    var outLetId = 0L
    lateinit var tableSetupLayout: RelativeLayout

    init {
        outLetId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L
        populateMasters()
        populateTableListing()
    }

    private fun populateMasters() {
        //fetch unoccupied grey seat images from svm
        masterRepository.fetchSystemMasterValueByNameLocal(
            SystemValueMasterDao.TABLE_CONFIG_UNOCCUPIED,
            object : IDataSourceCallback<List<SystemValueMasterDTO>> {

                override fun onDataFound(data: List<SystemValueMasterDTO>) {
                    typeOfTableArrayList.clear()
                    typeOfTableArrayList = data as ArrayList<SystemValueMasterDTO>
                    typeOfTableListMutableLiveData.value = typeOfTableArrayList
                }

                override fun onError(error: String) {
                    getmSnackbar().value = error
                }
            })

    }

    private fun populateTableListing() {
        showProgressBar.value = true
        val commonListingDTO = CommonListingDTO()
        val defaultSort = CommonSortDTO()
        defaultSort.sortField = Constants.TABLE_ID_COLUMN
        defaultSort.sortOrder = Constants.SORT_ORDER
        commonListingDTO.defaultSort = defaultSort
        commonListingDTO.length = Constants.PAGINATION_PAGE_DATA_COUNT
        val commonSearchDTOList = ArrayList<CommonSearchDTO>()
        val commonSearchDTO = CommonSearchDTO()
        outLetId.let {
            commonSearchDTO.search = it.toString()
        }
        commonSearchDTO.searchCol = Constants.OUTLET_ID_COLUMN
        commonSearchDTOList.add(commonSearchDTO)
        commonListingDTO.search = commonSearchDTOList

        dashboardRepository.getTableListing(commonListingDTO,
            object : IDataSourceCallback<ArrayList<TableManagementDTO>> {

                override fun onDataFound(data: ArrayList<TableManagementDTO>) {
                    showProgressBar.value = false
                    tableManagementList = data
                    arrangeTablesOnLayout()
                }

                override fun onError(error: String) {
                    showProgressBar.value = false
                    getmSnackbar().value = error
                }
            })
    }


    private fun arrangeTablesOnLayout() {
        if (tableManagementList.isNotEmpty()) {
            for (table in tableManagementList) {
                val layoutparams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                val inflater = LayoutInflater.from(getmContext())
                val childLayout = inflater.inflate(R.layout.table_cell_layout, tableSetupLayout, false)
                tableSetupLayout.addView(childLayout)
                childLayout.layoutParams = layoutparams
                val imageView = childLayout.findViewById<ImageView>(R.id.imageView)
                if (table.capacity != 0L) {
                    val typeOfSeater =
                        Constants.getTypeOfSeaterInFullName(table.capacity.toString())
                    for (item in typeOfTableArrayList) {
                        if (item.name != null) {
                            if (item.name?.startsWith(typeOfSeater) == true) {
                                Glide.with(getmContext())
                                    .load(SERVER_IMG_URL + item.value).into(imageView)
                                break
                            }
                        }
                    }
                }
                val tableNameTxt = childLayout.findViewById<TextView>(R.id.tableNameTxt)
                tableNameTxt.text = table.tableCode
//                childLayout.x = table.xcoordinate ?: 0f
//                childLayout.y = table.ycoordinate ?: 0f

                // Viewing Position Modification as the Container layout
                childLayout.x = (table.xcoordinate ?: 0f) - 20f
                val (tableSetupLayoutX,tableSetupLayoutY) = tableSetupLayout.screenLocationInWindow
                childLayout.y = (table.ycoordinate ?: 0f) - tableSetupLayoutY.toFloat()

                childLayout.makeDraggable(Draggable.STICKY.NONE, true, object : DraggableListener {
                    override fun onPositionChanged(view: View) {

                        val (x,y) = view.screenLocationInWindow
                        table.xcoordinate = x.toFloat()
                        table.ycoordinate = y.toFloat()

/*
                        val offsetViewBounds:Rect = Rect()
                        view.getDrawingRect(offsetViewBounds)
                        tableSetupLayout.offsetDescendantRectToMyCoords(view,offsetViewBounds)

                        var relativeTop = offsetViewBounds.top
                        var relativeLeft = offsetViewBounds.left

                        table.xcoordinate = relativeTop.toFloat()
                        table.ycoordinate = relativeLeft.toFloat()
*/

                        Log.i("XY","${table.xcoordinate}  ${table.ycoordinate}")

/*                        val location = view.getLocationOnScreen()
                        table.xcoordinate = location.x.toFloat()
                        table.ycoordinate = (location.y - childLayout.measuredHeight).toFloat()*/
                    }

                })

                childLayout.setOnClickListener {
                    onClickChildLayout(table,tableNameTxt,childLayout)
                }

            }

        }
    }

    private fun onClickChildLayout(
        table: TableManagementDTO,
        tableNameTxt: TextView,
        childLayout: View
    ) {
        //show menu to edit and remove the table
        openEditTableDialog(table,tableNameTxt,childLayout)
    }

    private fun openEditTableDialog(
        table: TableManagementDTO,
        tableNameTxt: TextView,
        childLayout: View
    ) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.edit_table_dialog_layout)
        val tableName = dialog.findViewById(R.id.tableName) as EditText
        val dismissDialog = dialog.findViewById(R.id.dismissDialog) as ImageView
        val removeTableBtn = dialog.findViewById(R.id.removeTable) as Button
        val saveTableBtn = dialog.findViewById(R.id.saveTable) as Button
        tableName.setText(table.tableCode ?: "")

        dismissDialog.setOnClickListener {
            dialog.dismiss()
        }

        removeTableBtn.setOnClickListener {
            tableManagementList.remove(table)
            tableSetupLayout.removeView(childLayout)
            dialog.dismiss()
        }
        saveTableBtn.setOnClickListener {
            tableNameTxt.text = tableName.text.toString()
            table.tableCode = tableName.text.toString()
            dialog.dismiss()
        }
        dialog.show()
    }


    fun openDialog(
        mContext: Context,
        systemValueMasterDTO: SystemValueMasterDTO
    ) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.add_table_dialog_layout)
        val tableName = dialog.findViewById(R.id.tableName) as EditText
        val cancelBtn = dialog.findViewById(R.id.cancel) as Button
        val addBtnBtn = dialog.findViewById(R.id.add) as Button

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        addBtnBtn.setOnClickListener {
            addView(mContext, tableName.text.toString(), systemValueMasterDTO, tableSetupLayout)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun addView(
        mContext: Context,
        tableName: String,
        systemValueMasterDTO: SystemValueMasterDTO,
        tableSetupLayout: RelativeLayout
    ) {
        val tableManagementDTO = TableManagementDTO()
        val layoutparams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        layoutparams.addRule(RelativeLayout.CENTER_IN_PARENT)

        val inflater = LayoutInflater.from(activity)
        val childLayout = inflater.inflate(R.layout.table_cell_layout, tableSetupLayout, false)
        tableSetupLayout.addView(childLayout)
        childLayout.layoutParams = layoutparams

        val imageView = childLayout.findViewById<ImageView>(R.id.imageView)
        if (systemValueMasterDTO.value != null) {
            Glide.with(mContext).load(SERVER_IMG_URL + systemValueMasterDTO.value).into(imageView)
        }
        val tableNameTxt = childLayout.findViewById<TextView>(R.id.tableNameTxt)
//        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
//            imageView.makeDraggable()
        tableNameTxt.text = tableName
        tableManagementDTO.xcoordinate = childLayout.x
        tableManagementDTO.ycoordinate = childLayout.y
        childLayout.makeDraggable(Draggable.STICKY.NONE, true, object : DraggableListener {

            override fun onPositionChanged(view: View) {

                val (x,y) = view.screenLocationInWindow

                /*val offsetViewBounds:Rect = Rect()
                view.getDrawingRect(offsetViewBounds)
                tableSetupLayout.offsetDescendantRectToMyCoords(view,offsetViewBounds)

                var relativeTop = offsetViewBounds.top
                var relativeLeft = offsetViewBounds.left*/


                tableManagementDTO.xcoordinate = x.toFloat()
                tableManagementDTO.ycoordinate = y.toFloat()

                Log.i("X1Y1","${tableManagementDTO.xcoordinate}  ${tableManagementDTO.ycoordinate}")

/*

                val location = view.getLocationOnScreen()
//                linearLayout.layoutParams.width / 2
//                absX = location.x
//                absY = location.y - childLayout.measuredHeight
                tableManagementDTO.xcoordinate = location.x.toFloat()
                tableManagementDTO.ycoordinate = (location.y.toFloat() - childLayout.measuredHeight.toFloat())
*/
                tableManagementDTO.tableCode = tableNameTxt.text.toString()
                tableManagementDTO.occupied = false
                tableManagementDTO.capacity =
                    Constants.getTypeOfSeater(systemValueMasterDTO.name).toLong()
                if (outLetId != 0L) {
                    tableManagementDTO.outletId = outLetId
                }

            }
        })

        tableManagementList.add(tableManagementDTO)
    }

    val View.screenLocationInWindow get():IntArray{
        val point = IntArray(2)
        getLocationInWindow(point)
        return point
    }

    fun View.getLocationOnScreen(): Point {
        val location = IntArray(2)
        this.getLocationOnScreen(location)
        return Point(location[0], location[1])

    }

    /*fun View.getLocationOnWindow(): Point {
        val location = IntArray(2)
        this.getLocationInWindow(location)
        return Point(location[0], location[1])

    }*/

    fun saveSetup() {
        showProgressBar.value = true
        if (tableManagementList.isNotEmpty()) {
            dashboardRepository.saveTableListing(
                tableManagementList,
                object : IDataSourceCallback<String> {

                    override fun onDataFound(data: String) {
                        getmSnackbar().value = data
                        showProgressBar.value = false
                    }

                    override fun onError(error: String) {
                        getmSnackbar().value = error
                        showProgressBar.value = false
                    }
                })
        }
    }

}