package com.sagar.logutil.checqk.master_controller.source.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagar.logutil.checqk.model.SocialMediaMasterDTO

@Dao
interface SocialMediaMasterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<SocialMediaMasterDTO>)

    @Query("select * from social_media")
    fun fetchAllData(): List<SocialMediaMasterDTO>

    @Query("delete from social_media")
    fun deleteAllDetails()

    @Query("select * from social_media where mediumName = :name ")
    fun fetchDetailsByName(name: String): List<SocialMediaMasterDTO>

    @Query("select * from social_media where mediumId = :id ")
    fun fetchListById(id: Long): SocialMediaMasterDTO

}