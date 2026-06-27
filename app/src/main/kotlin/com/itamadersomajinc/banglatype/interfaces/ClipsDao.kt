package com.itamadersomajinc.banglatype.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itamadersomajinc.banglatype.models.Clip

@Dao
interface ClipsDao {
    @Query("SELECT * FROM clips ORDER BY pinned DESC, timestamp DESC, id DESC")
    fun getClips(): List<Clip>

    @Query("SELECT * FROM clips WHERE value LIKE '%' || :query || '%' ORDER BY pinned DESC, timestamp DESC, id DESC")
    fun searchClips(query: String): List<Clip>

    @Query("SELECT id FROM clips WHERE value = :value")
    fun getClipWithValue(value: String): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(clip: Clip): Long

    @Query("UPDATE clips SET timestamp = :timestamp WHERE value = :value")
    fun bumpTimestamp(value: String, timestamp: Long)

    @Query("UPDATE clips SET pinned = :pinned WHERE id = :id")
    fun setPinned(id: Long, pinned: Boolean)

    // Keep only the most recent [keep] unpinned clips; older history is evicted.
    @Query("DELETE FROM clips WHERE pinned = 0 AND id NOT IN (SELECT id FROM clips WHERE pinned = 0 ORDER BY timestamp DESC, id DESC LIMIT :keep)")
    fun trimUnpinnedHistory(keep: Int)

    @Query("DELETE FROM clips WHERE id = :id")
    fun delete(id: Long)

    @Query("DELETE FROM clips")
    fun deleteAll()
}
