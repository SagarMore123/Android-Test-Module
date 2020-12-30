package com.sagar.logutil.checqk.master_controller.source.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagar.logutil.checqk.model.GroupRolesDTO

@Dao
interface GroupRoleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<GroupRolesDTO>)

    @Query("select * from group_roles")
    fun fetchAllData(): List<GroupRolesDTO>

    @Query("delete from group_roles")
    fun deleteAllDetails()

    @Query("select * from group_roles where name = :name ")
    fun fetchDetailsByName(name: String): List<GroupRolesDTO>

    @Query("select * from group_roles where id = :id ")
    fun fetchDetailsById(id: Long): GroupRolesDTO


}