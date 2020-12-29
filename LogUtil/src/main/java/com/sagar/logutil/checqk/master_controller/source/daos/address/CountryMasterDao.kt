package com.astrika.checqk.master_controller.source.daos.address

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.astrika.checqk.model.CountryMasterDTO

@Dao
interface CountryMasterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<CountryMasterDTO>)

    @Query("select * from country")
    fun fetchAllData(): List<CountryMasterDTO>

    @Query("delete from country")
    fun deleteAllDetails()

    @Query("select * from country where countryName = :name ")
    fun fetchDetailsByName(name: String): List<CountryMasterDTO>

    @Query("select * from country where countryId = :id ")
    fun fetchDetailsById(id: Long): CountryMasterDTO

}