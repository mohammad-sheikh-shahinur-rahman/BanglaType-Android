package com.itamadersomajinc.banglatype.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// A text-replacement entry: typing [trigger] followed by a separator expands it to [expansion].
@Entity(tableName = "shortcuts", indices = [(Index(value = ["trigger"], unique = true))])
data class Shortcut(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "trigger") var trigger: String,
    @ColumnInfo(name = "expansion") var expansion: String,
    @ColumnInfo(name = "timestamp") var timestamp: Long = 0
) : ListItem()
