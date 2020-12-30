package com.sagar.logutil.checqk.master_controller.source.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagar.logutil.checqk.model.GroupRolesStaffDTO

@Dao
interface GroupRoleStaffDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemMasters: List<GroupRolesStaffDTO>)

    @Query("select * from group_roles_staff")
    fun fetchAllData(): List<GroupRolesStaffDTO>

    @Query("delete from group_roles_staff")
    fun deleteAllDetails()

    @Query("select * from group_roles_staff where name = :name ")
    fun fetchDetailsByName(name: String): List<GroupRolesStaffDTO>

    @Query("select * from group_roles_staff where id = :id ")
    fun fetchDetailsById(id: Long): GroupRolesStaffDTO


}