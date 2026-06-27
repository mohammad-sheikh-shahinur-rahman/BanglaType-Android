package com.itamadersomajinc.banglatype.models

import androidx.room.Entity

@Entity(tableName = "bigrams", primaryKeys = ["prev", "next"])
data class Bigram(
    val prev: String,
    val next: String,
    val count: Int
)
