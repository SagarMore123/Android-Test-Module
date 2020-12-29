package com.astrika.checqk.master_controller.source.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.astrika.checqk.model.MealTypeMasterDTO

@Dao
interface MealTypeMasterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<MealTypeMasterDTO>)

    @Query("select * from meal_type")
    fun fetchAllData(): List<MealTypeMasterDTO>

    @Query("delete from meal_type")
    fun deleteAllDetails()

    @Query("select * from meal_type where mealTypeName = :name ")
    fun fetchDetailsByName(name: String): List<MealTypeMasterDTO>

    @Query("select * from meal_type where mealTypeId = :id ")
    fun fetchDetailsById(id: Long): MealTypeMasterDTO


}
