package com.itamadersomajinc.banglatype.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "phrases")
data class Phrase(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "value") var value: String,
    @ColumnInfo(name = "timestamp") var timestamp: Long = System.currentTimeMillis()
) : ListItem()

@Entity(tableName = "juktakkhor")
data class Juktakkhor(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "conjunct") var conjunct: String,
    @ColumnInfo(name = "breakdown") var breakdown: String,
    @ColumnInfo(name = "timestamp") var timestamp: Long = System.currentTimeMillis()
) : ListItem()
