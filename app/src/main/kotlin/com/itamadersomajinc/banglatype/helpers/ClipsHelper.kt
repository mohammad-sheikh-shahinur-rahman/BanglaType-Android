package com.itamadersomajinc.banglatype.helpers

import android.content.Context
import com.itamadersomajinc.banglatype.extensions.clipsDB
import com.itamadersomajinc.banglatype.models.Clip

class ClipsHelper(val context: Context) {

    // make sure clips have unique values
    fun insertClip(clip: Clip): Long {
        if (clip.timestamp == 0L) {
            clip.timestamp = System.currentTimeMillis()
        }
        return if (context.clipsDB.getClipWithValue(clip.value) == null) {
            context.clipsDB.insertOrUpdate(clip)
        } else {
            -1
        }
    }

    /** Adds a freshly copied value to the clipboard history (unpinned) and evicts old history. */
    fun addToHistory(value: String) {
        val trimmed = value.trim()
        if (trimmed.isEmpty()) {
            return
        }
        val db = context.clipsDB
        val now = System.currentTimeMillis()
        if (db.getClipWithValue(trimmed) != null) {
            db.bumpTimestamp(trimmed, now)
        } else {
            db.insertOrUpdate(Clip(null, trimmed, pinned = false, timestamp = now))
        }
        db.trimUnpinnedHistory(CLIPBOARD_HISTORY_LIMIT)
    }

    fun setPinned(id: Long, pinned: Boolean) {
        context.clipsDB.setPinned(id, pinned)
    }

    fun delete(id: Long) {
        context.clipsDB.delete(id)
    }
}
