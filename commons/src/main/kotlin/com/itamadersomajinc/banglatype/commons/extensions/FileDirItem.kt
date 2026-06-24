package com.itamadersomajinc.banglatype.commons.extensions

import android.content.Context
import com.itamadersomajinc.banglatype.commons.models.FileDirItem

fun FileDirItem.isRecycleBinPath(context: Context): Boolean {
    return path.startsWith(context.recycleBinPath)
}
