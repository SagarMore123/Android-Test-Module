package com.sagar.logutil.checqk.master_controller.source.daos


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagar.logutil.checqk.model.OutletTypeMasterDTO


@Dao
interface OutletTypeMasterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<OutletTypeMasterDTO>)

    @Query("select * from outlet_type")
    fun fetchAllData(): List<OutletTypeMasterDTO>

    @Query("delete from outlet_type")
    fun deleteAllDetails()

    @Query("select * from outlet_type where outletTypeName = :name ")
    fun fetchDetailsByName(name: String): List<OutletTypeMasterDTO>

    @Query("select * from outlet_type where outletTypeId = :id ")
    fun fetchDetailsById(id: Long): OutletTypeMasterDTO


}
