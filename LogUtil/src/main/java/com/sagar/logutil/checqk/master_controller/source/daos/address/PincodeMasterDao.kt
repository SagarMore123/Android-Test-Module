package com.astrika.checqk.master_controller.source.daos.address

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.astrika.checqk.model.PincodeMasterDTO

@Dao
interface PincodeMasterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<PincodeMasterDTO>)

    @Query("select * from pincode order by pincodeId asc")
    fun fetchAllData(): List<PincodeMasterDTO>

    @Query("select * from pincode where regionId = :id order by pincodeId asc")
    fun fetchListByRegionId(id: Long): List<PincodeMasterDTO>

    @Query("select * from pincode where cityId = :id order by pincodeId asc")
    fun fetchListByCityId(id: Long): List<PincodeMasterDTO>

    @Query("delete from pincode")
    fun deleteAllDetails()

    @Query("select * from pincode where pincode = :name ")
    fun fetchDetailsByName(name: String): List<PincodeMasterDTO>

    @Query("select * from pincode where pincodeId = :id")
    fun fetchDetailsById(id: Long): PincodeMasterDTO

}