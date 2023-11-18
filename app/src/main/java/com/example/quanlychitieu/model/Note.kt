package com.example.quanlychitieu.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "type") var type: String = "",
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "container") var container: String,
    @ColumnInfo(name = "time") var time: String = "",
    @ColumnInfo(name = "timeset") var timeSet: String = "",
    @ColumnInfo(name = "code") var code : Int = 0,
    @ColumnInfo(name = "pass") var password : String = ""
) : Serializable