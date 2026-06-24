package com.itamadersomajinc.banglatype.commons.models

import com.itamadersomajinc.banglatype.commons.helpers.FONT_TYPE_SYSTEM_DEFAULT
import com.itamadersomajinc.banglatype.commons.helpers.MyContentProvider

data class GlobalConfig(
    val themeType: Int,
    val textColor: Int,
    val backgroundColor: Int,
    val primaryColor: Int,
    val accentColor: Int,
    val appIconColor: Int,
    val showCheckmarksOnSwitches: Boolean,
    val lastUpdatedTS: Int = 0,
    val fontType: Int = FONT_TYPE_SYSTEM_DEFAULT,
    val fontName: String = "",
)

fun GlobalConfig?.isGlobalThemingEnabled(): Boolean {
    return this != null && themeType != MyContentProvider.GLOBAL_THEME_DISABLED
}
