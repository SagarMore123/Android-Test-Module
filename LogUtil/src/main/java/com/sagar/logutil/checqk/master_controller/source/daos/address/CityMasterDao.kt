package com.sagar.logutil.checqk.master_controller.source.daos.address

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagar.logutil.checqk.model.CityMasterDTO

@Dao
interface CityMasterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<CityMasterDTO>)

    @Query("select * from city order by cityId asc")
    fun fetchAllData(): List<CityMasterDTO>

    @Query("select * from city where countryId = :id order by cityId asc")
    fun fetchListByCountryId(id: Long): List<CityMasterDTO>

    @Query("select * from city where stateId = :id order by cityId asc")
    fun fetchListByStateId(id: Long): List<CityMasterDTO>

    @Query("delete from city")
    fun deleteAllDetails()

    @Query("select * from city where cityName = :name ")
    fun fetchDetailsByName(name: String): List<CityMasterDTO>

    @Query("select * from city where cityId = :id")
    fun fetchDetailsById(id: Long): CityMasterDTO

}