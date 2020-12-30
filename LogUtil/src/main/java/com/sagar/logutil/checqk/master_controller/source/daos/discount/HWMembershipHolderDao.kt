package com.sagar.logutil.checqk.master_controller.source.daos.discount

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagar.logutil.checqk.model.discount.HWMembershipHolderDTO

@Dao
interface HWMembershipHolderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<HWMembershipHolderDTO>)

    @Query("select * from hw_membership_holder")
    fun fetchAllData(): List<HWMembershipHolderDTO>

    @Query("delete from hw_membership_holder")
    fun deleteAllDetails()

    @Query("select * from hw_membership_holder where hwDiscountCardName = :name ")
    fun fetchDetailsByName(name: String): List<HWMembershipHolderDTO>

    @Query("select * from hw_membership_holder where hwDiscountCardId = :id ")
    fun fetchDetailsById(id: Long): HWMembershipHolderDTO

}