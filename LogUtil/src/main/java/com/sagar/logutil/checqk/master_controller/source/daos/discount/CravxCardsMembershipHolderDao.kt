package com.astrika.checqk.master_controller.source.daos.discount

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.astrika.checqk.model.discount.CravxCardsMembershipHolderDTO

@Dao
interface CravxCardsMembershipHolderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<CravxCardsMembershipHolderDTO>)

    @Query("select * from cravx_cards_discount_membership_holder")
    fun fetchAllData(): List<CravxCardsMembershipHolderDTO>

    @Query("delete from cravx_cards_discount_membership_holder")
    fun deleteAllDetails()

    @Query("select * from cravx_cards_discount_membership_holder where cravxDiscountCardName = :name ")
    fun fetchDetailsByName(name: String): List<CravxCardsMembershipHolderDTO>

    @Query("select * from cravx_cards_discount_membership_holder where cravxDiscountCardId = :id ")
    fun fetchDetailsById(id: Long): CravxCardsMembershipHolderDTO

}