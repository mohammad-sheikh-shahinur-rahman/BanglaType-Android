package com.itamadersomajinc.banglatype.extensions

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import com.itamadersomajinc.banglatype.commons.extensions.adjustAlpha
import com.itamadersomajinc.banglatype.commons.extensions.darkenColor
import com.itamadersomajinc.banglatype.commons.extensions.getContrastColor
import com.itamadersomajinc.banglatype.commons.extensions.getProperBackgroundColor
import com.itamadersomajinc.banglatype.commons.extensions.getProperPrimaryColor
import com.itamadersomajinc.banglatype.commons.extensions.getProperTextColor
import com.itamadersomajinc.banglatype.commons.extensions.isDynamicTheme
import com.itamadersomajinc.banglatype.commons.extensions.isSystemInDarkMode
import com.itamadersomajinc.banglatype.commons.extensions.lightenColor
import com.itamadersomajinc.banglatype.R
import com.itamadersomajinc.banglatype.helpers.KEYBOARD_BG_IMAGE_FILE
import com.itamadersomajinc.banglatype.helpers.KEYBOARD_THEME_CUSTOM_PHOTO
import com.itamadersomajinc.banglatype.helpers.KEYBOARD_THEME_DEFAULT
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

// ---- Keyboard-only colors ----
// These intentionally do NOT read the app theme when a custom keyboard theme is selected, so that
// changing the keyboard theme never recolors the rest of the app. When the "default" theme is
// active the keyboard falls back to the app theme (its original behaviour).

/** True when the user picked a keyboard theme other than "default" (which follows the app theme). */
fun Context.isKeyboardThemeCustom(): Boolean = config.keyboardThemeId != KEYBOARD_THEME_DEFAULT

fun Context.getKeyboardTextColor(): Int =
    if (isKeyboardThemeCustom()) config.keyboardTextColor else getProperTextColor()

fun Context.getKeyboardBaseColor(): Int =
    if (isKeyboardThemeCustom()) config.keyboardColor else getProperBackgroundColor()

fun Context.getKeyboardAccentColor(): Int =
    if (isKeyboardThemeCustom()) config.keyboardPrimaryColor else getProperPrimaryColor()

fun Context.getKeyboardBackgroundColor(): Int {
    val color = when {
        isKeyboardThemeCustom() -> config.keyboardColor.darkenColor(2)
        isDynamicTheme() -> resources.getColor(R.color.you_keyboard_background_color, theme)
        else -> getProperBackgroundColor().darkenColor(2)
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
    if (isKeyboardThemeCustom()) {
        val lighter = config.keyboardColor.lightenColor()
        return if (lighter == Color.WHITE || lighter == Color.BLACK) {
            resources.getColor(R.color.divider_grey, theme)
        } else {
            lighter
        }
    }
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

/** Colour of the individual keys; [hasImage] true when a photo/gradient sits behind them. */
fun Context.getKeyboardKeyColor(hasImage: Boolean): Int {
    if (isKeyboardThemeCustom()) {
        val base = config.keyboardColor
        val contrast = if (hasImage) Color.WHITE else base.getContrastColor()
        val maxAlpha = if (hasImage) 0.30f else 0.55f
        val opacity = (config.keyboardKeyOpacity.coerceIn(0, 100) / 100f) * maxAlpha
        return contrast.adjustAlpha(opacity)
    }

    val backgroundColor = getKeyboardBackgroundColor()
    val lighterColor = backgroundColor.lightenColor()
    return if (isDynamicTheme()) {
        lighterColor
    } else if (backgroundColor == Color.BLACK) {
        backgroundColor.getContrastColor().adjustAlpha(0.1f)
    } else {
        lighterColor
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
 * Applies a built-in preset to the KEYBOARD ONLY: stores the theme colors in the keyboard-specific
 * prefs (never the app theme) and clears any custom photo.
 */
fun Context.applyKeyboardTheme(theme: KeyboardTheme) {
    val cfg = safeStorageContext.config
    cfg.keyboardBackgroundImagePath = ""
    cfg.keyboardThemeId = theme.id
    if (theme.id != KEYBOARD_THEME_DEFAULT) {
        cfg.keyboardTextColor = theme.textColor
        cfg.keyboardColor = theme.backgroundColor
        cfg.keyboardPrimaryColor = theme.primaryColor
    }
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
