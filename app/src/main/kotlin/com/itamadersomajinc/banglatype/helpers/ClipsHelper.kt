package com.itamadersomajinc.banglatype.helpers

import android.content.Context
import com.itamadersomajinc.banglatype.extensions.clipsDB
import com.itamadersomajinc.banglatype.models.Clip

class ClipsHelper(val context: Context) {

    // make sure clips have unique values
    fun insertClip(clip: Clip): Long {
        return if (context.clipsDB.getClipWithValue(clip.value) == null) {
            context.clipsDB.insertOrUpdate(clip)
        } else {
            -1
        }
    }
}
