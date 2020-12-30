package com.sagar.logutil.checqk.master_controller.source.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagar.logutil.checqk.model.CuisineMasterDTO

@Dao
interface CuisineMasterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<CuisineMasterDTO>)

    @Query("select * from cuisine")
    fun fetchAllData(): List<CuisineMasterDTO>

    @Query("delete from cuisine")
    fun deleteAllDetails()

    @Query("select * from cuisine where cuisineName = :name ")
    fun fetchDetailsByName(name: String): List<CuisineMasterDTO>

    @Query("select * from cuisine where cuisineId = :id ")
    fun fetchListById(id: Long): CuisineMasterDTO

}