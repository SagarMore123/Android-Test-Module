package com.astrika.checqk.master_controller.source.daos.address

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.astrika.checqk.model.StateMasterDTO

@Dao
interface StateMasterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<StateMasterDTO>)

    @Query("select * from state order by stateId asc")
    fun fetchAllData(): List<StateMasterDTO>

    @Query("select * from state where countryId = :id order by stateId asc")
    fun fetchListByCountryId(id: Long): List<StateMasterDTO>

    @Query("delete from state")
    fun deleteAllDetails()

    @Query("select * from state where stateName = :name ")
    fun fetchDetailsByName(name: String): List<StateMasterDTO>

    @Query("select * from state where stateId = :id")
    fun fetchDetailsById(id: Long): StateMasterDTO

}