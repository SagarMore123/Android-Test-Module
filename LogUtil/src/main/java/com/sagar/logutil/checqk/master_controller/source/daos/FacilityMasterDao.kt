package com.sagar.logutil.checqk.master_controller.source.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagar.logutil.checqk.model.FacilityMasterDTO

@Dao
interface FacilityMasterDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<FacilityMasterDTO>)

    @Query("select * from facility")
    fun getAllData(): List<FacilityMasterDTO>

    @Query("delete from facility")
    fun deleteAllEmotions()

    @Query("select * from facility where facilityName = :name ")
    fun getDetailsByName(name: String): List<FacilityMasterDTO>

    @Query("select * from facility where facilityId = :id ")
    fun getDetailsById(id: Long): FacilityMasterDTO


}