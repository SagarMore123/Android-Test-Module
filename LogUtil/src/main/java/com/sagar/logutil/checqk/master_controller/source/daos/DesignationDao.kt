package com.sagar.logutil.checqk.master_controller.source.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagar.logutil.checqk.model.DesignationDTO

@Dao
interface DesignationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<DesignationDTO>)

    @Query("select * from designation")
    fun fetchAllData(): List<DesignationDTO>

    @Query("delete from designation")
    fun deleteAllDetails()

    @Query("select * from designation where designationName = :name ")
    fun fetchDetailsByName(name: String): List<DesignationDTO>

    @Query("select * from designation where designationId = :id ")
    fun fetchDetailsById(id: Long): DesignationDTO

}