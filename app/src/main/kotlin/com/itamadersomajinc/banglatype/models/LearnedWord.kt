package com.itamadersomajinc.banglatype.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "learned_words")
data class LearnedWord(
    @PrimaryKey val word: String,
    val count: Int
)
