package com.sagar.logutil.checqk.master_controller.source.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagar.logutil.checqk.model.KnownLanguagesDTO

@Dao
interface KnownLanguagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<KnownLanguagesDTO>)

    @Query("select * from known_languages")
    fun fetchAllData(): List<KnownLanguagesDTO>

    @Query("delete from known_languages")
    fun deleteAllDetails()

    @Query("select * from known_languages where languageName = :name ")
    fun fetchDetailsByName(name: String): List<KnownLanguagesDTO>

    @Query("select * from known_languages where languageId = :id ")
    fun fetchDetailsById(id: Long): KnownLanguagesDTO


}