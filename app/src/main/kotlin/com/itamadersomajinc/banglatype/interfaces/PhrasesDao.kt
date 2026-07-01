package com.itamadersomajinc.banglatype.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itamadersomajinc.banglatype.models.Phrase
import com.itamadersomajinc.banglatype.models.Juktakkhor

@Dao
interface PhrasesDao {
    @Query("SELECT * FROM phrases ORDER BY timestamp DESC")
    fun getPhrases(): List<Phrase>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhrase(phrase: Phrase): Long

    @Query("DELETE FROM phrases WHERE id = :id")
    fun deletePhrase(id: Long)

    @Query("SELECT * FROM juktakkhor ORDER BY timestamp DESC")
    fun getJuktakkhor(): List<Juktakkhor>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJuktakkhor(juktakkhor: Juktakkhor): Long

    @Query("DELETE FROM juktakkhor WHERE id = :id")
    fun deleteJuktakkhor(id: Long)
}
