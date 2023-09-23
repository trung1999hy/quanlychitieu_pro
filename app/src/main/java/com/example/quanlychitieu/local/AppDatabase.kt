package com.example.quanlychitieu.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quanlychitieu.local.dao.ArchiveDao
import com.example.quanlychitieu.local.dao.CollectDao
import com.example.quanlychitieu.local.dao.MoneyDao
import com.example.quanlychitieu.local.dao.NoteDao
import com.example.quanlychitieu.local.dao.NoteTypeDao
import com.example.quanlychitieu.local.dao.SpendingDao
import com.example.quanlychitieu.model.Archive
import com.example.quanlychitieu.model.Collect
import com.example.quanlychitieu.model.Money
import com.example.quanlychitieu.model.Note
import com.example.quanlychitieu.model.Spending
import com.example.quanlychitieu.model.NoteType

@Database(
    entities = [Money::class, Spending::class, Collect::class, Note::class, NoteType::class, Archive::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDatabaseDao(): MoneyDao
    abstract fun getSpendingDao(): SpendingDao
    abstract fun getCollectDao(): CollectDao
    abstract fun getNoteDao(): NoteDao
    abstract fun getNoteTypeDao() : NoteTypeDao
    abstract fun getArchiveDao() : ArchiveDao

    companion object {
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context, AppDatabase::class.java, "database-name"
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build()
            }
            return instance!!
        }
    }
}