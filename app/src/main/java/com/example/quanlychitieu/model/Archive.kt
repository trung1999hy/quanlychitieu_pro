package com.example.quanlychitieu.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity
data class Archive(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "container") var container: String,
    @ColumnInfo(name = "time") var time: String = "",
) : Serializable
