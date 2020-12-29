package com.astrika.checqk.master_controller.source.daos.address

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.astrika.checqk.model.AreaMasterDTO

@Dao
interface AreaMasterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<AreaMasterDTO>)

    @Query("select * from area order by areaId asc")
    fun fetchAllData(): List<AreaMasterDTO>

    @Query("select * from area where countryId = :id order by areaId asc")
    fun fetchListByCountryId(id: Long): List<AreaMasterDTO>

    @Query("select * from area where cityId = :id order by areaId asc")
    fun fetchListByCityId(id: Long): List<AreaMasterDTO>

    @Query("delete from area")
    fun deleteAllDetails()

    @Query("select * from area where areaName = :name ")
    fun fetchDetailsByName(name: String): List<AreaMasterDTO>

    @Query("select * from area where areaId = :id")
    fun fetchDetailsById(id: Long): AreaMasterDTO

}