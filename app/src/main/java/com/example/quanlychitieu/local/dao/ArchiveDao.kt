package com.example.quanlychitieu.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.quanlychitieu.model.Archive
import com.example.quanlychitieu.model.Note
@Dao
interface ArchiveDao {

    @Update
    suspend fun update(item: Archive)

    @Insert
    suspend fun add(item: Archive)

    @Delete
    suspend fun delete(item: Archive)

    @Query("SELECT * FROM archive")
    fun getAll(): List<Archive>

    @Query("SELECT * FROM archive")
    fun getAllLiveData(): LiveData<List<Archive>>
}