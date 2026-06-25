package com.itamadersomajinc.banglatype.helpers

import android.graphics.Color
import com.itamadersomajinc.banglatype.R

enum class KeyboardThemeType {
    COLOR,      // solid color preset, reuses the existing color pipeline
    GRADIENT,   // gradient background drawn behind the keys
    PHOTO       // user-picked photo (handled separately via the stored image path)
}

/**
 * A built-in keyboard theme. COLOR themes only set the three theme colors and let the existing
 * rendering pipeline handle them. GRADIENT themes additionally draw a gradient behind the keys.
 */
data class KeyboardTheme(
    val id: String,
    val nameResId: Int,
    val type: KeyboardThemeType,
    val textColor: Int,
    val backgroundColor: Int,
    val primaryColor: Int,
    val gradientColors: IntArray = intArrayOf()
) {
    override fun equals(other: Any?) = other is KeyboardTheme && other.id == id
    override fun hashCode() = id.hashCode()
}

private fun rgb(hex: Long) = Color.rgb(
    ((hex shr 16) and 0xFF).toInt(),
    ((hex shr 8) and 0xFF).toInt(),
    (hex and 0xFF).toInt()
)

// Built-in presets shown in the theme picker, in display order.
val builtInKeyboardThemes: List<KeyboardTheme> = listOf(
    KeyboardTheme(
        id = KEYBOARD_THEME_DEFAULT,
        nameResId = R.string.theme_default,
        type = KeyboardThemeType.COLOR,
        textColor = rgb(0xEEEEEE),
        backgroundColor = rgb(0x2E2E2E),
        primaryColor = rgb(0x4CAF50)
    ),
    KeyboardTheme(
        id = "light",
        nameResId = R.string.theme_light,
        type = KeyboardThemeType.COLOR,
        textColor = rgb(0x333333),
        backgroundColor = rgb(0xECEFF1),
        primaryColor = rgb(0x2196F3)
    ),
    KeyboardTheme(
        id = "midnight",
        nameResId = R.string.theme_midnight,
        type = KeyboardThemeType.COLOR,
        textColor = rgb(0xE0E0E0),
        backgroundColor = rgb(0x121212),
        primaryColor = rgb(0x7C4DFF)
    ),
    KeyboardTheme(
        id = "rose",
        nameResId = R.string.theme_rose,
        type = KeyboardThemeType.COLOR,
        textColor = rgb(0xFFFFFF),
        backgroundColor = rgb(0x880E4F),
        primaryColor = rgb(0xFF80AB)
    ),
    KeyboardTheme(
        id = "ocean",
        nameResId = R.string.theme_ocean,
        type = KeyboardThemeType.GRADIENT,
        textColor = rgb(0xFFFFFF),
        backgroundColor = rgb(0x1565C0),
        primaryColor = rgb(0x80D8FF),
        gradientColors = intArrayOf(rgb(0x2193B0), rgb(0x6DD5ED))
    ),
    KeyboardTheme(
        id = "sunset",
        nameResId = R.string.theme_sunset,
        type = KeyboardThemeType.GRADIENT,
        textColor = rgb(0xFFFFFF),
        backgroundColor = rgb(0xE65100),
        primaryColor = rgb(0xFFD180),
        gradientColors = intArrayOf(rgb(0xFF512F), rgb(0xDD2476))
    ),
    KeyboardTheme(
        id = "forest",
        nameResId = R.string.theme_forest,
        type = KeyboardThemeType.GRADIENT,
        textColor = rgb(0xFFFFFF),
        backgroundColor = rgb(0x1B5E20),
        primaryColor = rgb(0xB9F6CA),
        gradientColors = intArrayOf(rgb(0x134E5E), rgb(0x71B280))
    ),
    KeyboardTheme(
        id = "grape",
        nameResId = R.string.theme_grape,
        type = KeyboardThemeType.GRADIENT,
        textColor = rgb(0xFFFFFF),
        backgroundColor = rgb(0x4A148C),
        primaryColor = rgb(0xEA80FC),
        gradientColors = intArrayOf(rgb(0x654EA3), rgb(0xEAAFC8))
    )
)

fun keyboardThemeById(id: String): KeyboardTheme? = builtInKeyboardThemes.firstOrNull { it.id == id }
