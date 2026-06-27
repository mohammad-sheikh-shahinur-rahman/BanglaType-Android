package com.itamadersomajinc.banglatype.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itamadersomajinc.banglatype.models.Bigram
import com.itamadersomajinc.banglatype.models.LearnedWord

@Dao
interface PredictionsDao {
    // --- learned words (personal vocabulary + frequency) ---
    @Query("SELECT * FROM learned_words")
    fun getAllLearnedWords(): List<LearnedWord>

    @Query("UPDATE learned_words SET count = count + 1 WHERE word = :word")
    fun bumpWord(word: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWord(word: LearnedWord)

    // --- bigrams (previous word -> next word) ---
    @Query("SELECT next FROM bigrams WHERE prev = :prev ORDER BY count DESC LIMIT :limit")
    fun getNextWords(prev: String, limit: Int): List<String>

    @Query("UPDATE bigrams SET count = count + 1 WHERE prev = :prev AND next = :next")
    fun bumpBigram(prev: String, next: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBigram(bigram: Bigram)

    @Query("DELETE FROM learned_words")
    fun clearLearnedWords()

    @Query("DELETE FROM bigrams")
    fun clearBigrams()
}
