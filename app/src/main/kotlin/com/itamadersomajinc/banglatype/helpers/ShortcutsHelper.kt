package com.itamadersomajinc.banglatype.helpers

import android.content.Context
import com.itamadersomajinc.banglatype.extensions.shortcutsDB
import com.itamadersomajinc.banglatype.models.Shortcut

class ShortcutsHelper(val context: Context) {

    // make sure shortcuts have unique triggers; returns -1 if the trigger already exists
    fun insertShortcut(shortcut: Shortcut): Long {
        shortcut.trigger = shortcut.trigger.trim()
        if (shortcut.timestamp == 0L) {
            shortcut.timestamp = System.currentTimeMillis()
        }

        val existingId = context.shortcutsDB.getShortcutWithTrigger(shortcut.trigger)
        // Allow updating the same row (edit), but block creating a duplicate trigger.
        if (existingId != null && existingId != shortcut.id) {
            return -1
        }
        return context.shortcutsDB.insertOrUpdate(shortcut)
    }

    fun delete(id: Long) {
        context.shortcutsDB.delete(id)
    }
}
