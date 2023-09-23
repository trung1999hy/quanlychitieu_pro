package com.example.quanlychitieu.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.quanlychitieu.model.Collect

@Dao
interface CollectDao {
    @Query("SELECT * FROM collect")
    fun getAll(): List<Collect>
    @Query("SELECT * FROM collect")
    fun getAllLiveData(): LiveData<List<Collect>>
    @Insert
    suspend fun insertAll(vararg collect: Collect)

    @Delete
    suspend  fun delete(collect: Collect)
}