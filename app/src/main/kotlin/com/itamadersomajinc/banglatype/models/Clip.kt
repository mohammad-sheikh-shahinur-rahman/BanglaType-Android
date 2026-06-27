package com.itamadersomajinc.banglatype.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "clips", indices = [(Index(value = ["id"], unique = true))])
data class Clip(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "value") var value: String,
    @ColumnInfo(name = "pinned") var pinned: Boolean = false,
    @ColumnInfo(name = "timestamp") var timestamp: Long = 0
) : ListItem()
