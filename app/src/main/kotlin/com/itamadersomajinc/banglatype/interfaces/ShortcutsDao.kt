package com.itamadersomajinc.banglatype.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itamadersomajinc.banglatype.models.Shortcut

@Dao
interface ShortcutsDao {
    @Query("SELECT * FROM shortcuts ORDER BY timestamp DESC, id DESC")
    fun getShortcuts(): List<Shortcut>

    @Query("SELECT id FROM shortcuts WHERE trigger = :trigger")
    fun getShortcutWithTrigger(trigger: String): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(shortcut: Shortcut): Long

    @Query("DELETE FROM shortcuts WHERE id = :id")
    fun delete(id: Long)

    @Query("DELETE FROM shortcuts")
    fun deleteAll()
}
