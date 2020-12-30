package com.sagar.logutil.checqk.master_controller.source.daos.discount

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagar.logutil.checqk.model.discount.OutletDiscountCategoryDTO

@Dao
interface DiscountCategoryMasterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<OutletDiscountCategoryDTO>)

    @Query("select * from outlet_discount_category")
    fun fetchAllData(): List<OutletDiscountCategoryDTO>

    @Query("delete from outlet_discount_category")
    fun deleteAllDetails()

    @Query("select * from outlet_discount_category where outletDiscountCategoryName = :name ")
    fun fetchDetailsByName(name: String): List<OutletDiscountCategoryDTO>

    @Query("select * from outlet_discount_category where outletDiscountCategoryId = :id ")
    fun fetchDetailsById(id: Long): OutletDiscountCategoryDTO

}