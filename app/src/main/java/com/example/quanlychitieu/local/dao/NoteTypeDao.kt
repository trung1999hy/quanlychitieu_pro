package com.example.quanlychitieu.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.quanlychitieu.model.NoteType

@Dao
interface NoteTypeDao {
    @Update
    suspend fun updateNoteType(noteType: NoteType)

    @Insert
    suspend fun addNoteType(noteType: NoteType)

    @Delete
    suspend fun deleteNoteType(noteType: NoteType)

    @Query("SELECT * FROM notetype")
    fun getAll(): List<NoteType>
}