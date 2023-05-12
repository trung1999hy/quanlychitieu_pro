package com.example.quanlychitieu.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.quanlychitieu.model.Note
@Dao
interface NoteDao {
    @Update
    suspend fun update(note: Note)

    @Insert
    suspend fun addNote(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM note")
    fun getAll(): List<Note>
}