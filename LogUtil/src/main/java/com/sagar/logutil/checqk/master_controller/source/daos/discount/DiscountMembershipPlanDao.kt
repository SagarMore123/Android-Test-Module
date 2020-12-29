package com.astrika.checqk.master_controller.source.daos.discount

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.astrika.checqk.model.discount.OutletDiscountMembershipPlanDTO

@Dao
interface DiscountMembershipPlanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<OutletDiscountMembershipPlanDTO>)

    @Query("select * from outlet_discount_membership_plan")
    fun fetchAllData(): List<OutletDiscountMembershipPlanDTO>

    @Query("delete from outlet_discount_membership_plan")
    fun deleteAllDetails()

    @Query("select * from outlet_discount_membership_plan where membershipName = :name ")
    fun fetchDetailsByName(name: String): List<OutletDiscountMembershipPlanDTO>

    @Query("select * from outlet_discount_membership_plan where membershipId = :id ")
    fun fetchDetailsById(id: Long): OutletDiscountMembershipPlanDTO

}