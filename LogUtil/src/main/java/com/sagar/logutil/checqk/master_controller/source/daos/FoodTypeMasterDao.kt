package com.sagar.logutil.checqk.master_controller.source.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagar.logutil.checqk.model.FoodTypeMasterDTO

@Dao
interface FoodTypeMasterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<FoodTypeMasterDTO>)

    @Query("select * from food_type")
    fun fetchAllData(): List<FoodTypeMasterDTO>

    @Query("delete from food_type")
    fun deleteAllDetails()

    @Query("select * from food_type where foodTypeName = :name ")
    fun fetchDetailsByName(name: String): List<FoodTypeMasterDTO>

    @Query("select * from food_type where foodTypeId = :id ")
    fun fetchDetailsById(id: Long): FoodTypeMasterDTO

}