package com.sagar.logutil.checqk.master_controller.source.daos.discount

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagar.logutil.checqk.model.discount.InHouseMembershipHolderDTO

@Dao
interface InHouseMembershipHolderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<InHouseMembershipHolderDTO>)

    @Query("select * from in_house_membership_holder")
    fun fetchAllData(): List<InHouseMembershipHolderDTO>

    @Query("delete from in_house_membership_holder")
    fun deleteAllDetails()

    @Query("select * from in_house_membership_holder where inHouseDiscountCardName = :name ")
    fun fetchDetailsByName(name: String): List<InHouseMembershipHolderDTO>

    @Query("select * from in_house_membership_holder where inHouseDiscountCardId = :id ")
    fun fetchDetailsById(id: Long): InHouseMembershipHolderDTO

}