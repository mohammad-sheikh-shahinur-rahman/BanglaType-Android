package com.itamadersomajinc.banglatype.extensions

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import com.itamadersomajinc.banglatype.commons.extensions.baseConfig
import com.itamadersomajinc.banglatype.commons.extensions.darkenColor
import com.itamadersomajinc.banglatype.commons.extensions.getProperBackgroundColor
import com.itamadersomajinc.banglatype.commons.extensions.isDynamicTheme
import com.itamadersomajinc.banglatype.commons.extensions.isSystemInDarkMode
import com.itamadersomajinc.banglatype.commons.extensions.lightenColor
import com.itamadersomajinc.banglatype.R
import com.itamadersomajinc.banglatype.helpers.KEYBOARD_BG_IMAGE_FILE
import com.itamadersomajinc.banglatype.helpers.KEYBOARD_THEME_CUSTOM_PHOTO
import com.itamadersomajinc.banglatype.helpers.KeyboardTheme
import com.itamadersomajinc.banglatype.helpers.KeyboardThemeType
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_ARABIC
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_BANGLA_AVRO
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_BANGLA_JATIYO
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_BANGLA_PROBHAT
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_BENGALI
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_RUSSIAN
import com.itamadersomajinc.banglatype.helpers.keyboardThemeById
import java.io.File

fun Context.getKeyboardBackgroundColor(): Int {
    val color = if (isDynamicTheme()) {
        resources.getColor(R.color.you_keyboard_background_color, theme)
    } else {
        getProperBackgroundColor().darkenColor(2)
    }

    // use darker background color when key borders are enabled
    if (config.showKeyBorders) {
        val darkerColor = color.darkenColor(2)
        return if (darkerColor == Color.WHITE) {
            resources.getColor(R.color.md_grey_200, theme)
        } else {
            darkerColor
        }
    }

    return color
}

fun Context.getStrokeColor(): Int {
    return if (isDynamicTheme()) {
        if (isSystemInDarkMode()) {
            resources.getColor(R.color.md_grey_800, theme)
        } else {
            resources.getColor(R.color.md_grey_400, theme)
        }
    } else {
        val lighterColor = safeStorageContext.getProperBackgroundColor().lightenColor()
        if (lighterColor == Color.WHITE || lighterColor == Color.BLACK) {
            resources.getColor(R.color.divider_grey, theme)
        } else {
            lighterColor
        }
    }
}

// ---- Custom keyboard theme (Gboard-style) ----

/** File holding the user-picked background photo, in device-protected storage. */
fun Context.keyboardBackgroundImageFile(): File = File(safeStorageContext.filesDir, KEYBOARD_BG_IMAGE_FILE)

/** The currently selected built-in theme, if it is a recognised preset. */
fun Context.selectedKeyboardTheme(): KeyboardTheme? = keyboardThemeById(config.keyboardThemeId)

/** True when the keyboard should render an image/gradient behind the keys instead of a solid color. */
fun Context.hasKeyboardBackgroundDrawable(): Boolean {
    if (config.keyboardThemeId == KEYBOARD_THEME_CUSTOM_PHOTO) {
        return config.keyboardBackgroundImagePath.isNotEmpty() && keyboardBackgroundImageFile().exists()
    }
    return selectedKeyboardTheme()?.type == KeyboardThemeType.GRADIENT
}

/**
 * The background drawable (photo or gradient) with a darkening scrim composited on top, sized for a
 * [width] x [height] keyboard area. Returns null for plain color themes.
 */
fun Context.getKeyboardBackgroundDrawable(width: Int, height: Int): Drawable? {
    val scrim = ColorDrawable(Color.argb((config.keyboardBackgroundDim.coerceIn(0, 100) * 255 / 100), 0, 0, 0))

    if (config.keyboardThemeId == KEYBOARD_THEME_CUSTOM_PHOTO) {
        val file = keyboardBackgroundImageFile()
        if (!file.exists()) return null
        val bitmap = decodeScaledBitmap(file, width, height) ?: return null
        val photo = BitmapDrawable(resources, bitmap).apply { gravity = android.view.Gravity.FILL }
        return LayerDrawable(arrayOf(photo, scrim))
    }

    val theme = selectedKeyboardTheme()
    if (theme?.type == KeyboardThemeType.GRADIENT && theme.gradientColors.size >= 2) {
        val gradient = GradientDrawable(GradientDrawable.Orientation.TL_BR, theme.gradientColors)
        return LayerDrawable(arrayOf(gradient, scrim))
    }

    return null
}

private fun decodeScaledBitmap(file: File, reqWidth: Int, reqHeight: Int): android.graphics.Bitmap? {
    return try {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(file.absolutePath, bounds)
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

        val targetW = if (reqWidth > 0) reqWidth else bounds.outWidth
        val targetH = if (reqHeight > 0) reqHeight else bounds.outHeight
        var sample = 1
        while (bounds.outWidth / (sample * 2) >= targetW && bounds.outHeight / (sample * 2) >= targetH) {
            sample *= 2
        }
        val opts = BitmapFactory.Options().apply { inSampleSize = sample }
        BitmapFactory.decodeFile(file.absolutePath, opts)
    } catch (e: Exception) {
        null
    }
}

/**
 * Applies a built-in preset: writes the theme colors through the existing color pipeline and clears
 * any custom photo. Disables the dynamic/system theme so the preset colors take effect.
 */
fun Context.applyKeyboardTheme(theme: KeyboardTheme) {
    val ctx = safeStorageContext
    ctx.baseConfig.isSystemThemeEnabled = false
    ctx.baseConfig.textColor = theme.textColor
    ctx.baseConfig.backgroundColor = theme.backgroundColor
    ctx.baseConfig.primaryColor = theme.primaryColor
    ctx.baseConfig.accentColor = theme.primaryColor
    ctx.config.keyboardBackgroundImagePath = ""
    ctx.config.keyboardThemeId = theme.id
}

/** Maps the active keyboard layout language to a BCP-47 tag for speech recognition. */
fun Context.getVoiceInputLocale(): String {
    return when (config.keyboardLanguage) {
        LANGUAGE_BANGLA_AVRO, LANGUAGE_BANGLA_JATIYO, LANGUAGE_BANGLA_PROBHAT, LANGUAGE_BENGALI -> "bn-BD"
        LANGUAGE_ARABIC -> "ar"
        LANGUAGE_RUSSIAN -> "ru-RU"
        else -> "en-US"
    }
}
