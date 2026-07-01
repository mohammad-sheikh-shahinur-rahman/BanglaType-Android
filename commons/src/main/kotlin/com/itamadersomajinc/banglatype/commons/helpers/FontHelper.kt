package com.itamadersomajinc.banglatype.commons.helpers

import android.content.Context
import android.graphics.Typeface
import com.itamadersomajinc.banglatype.commons.extensions.baseConfig
import com.itamadersomajinc.banglatype.commons.extensions.ensureFontPresentLocally
import com.itamadersomajinc.banglatype.commons.extensions.isCredentialStorageAvailable
import java.io.File

/**
 * Helper for loading and caching custom fonts.
 */
object FontHelper {
    private var cachedTypeface: Typeface? = null
    private var cachedFontType: Int = -1
    private var cachedFontFileName: String = ""

    fun getTypeface(
        context: Context,
        fontType: Int? = null,
        fontFileName: String? = null
    ): Typeface {
        if (!context.isCredentialStorageAvailable) return Typeface.DEFAULT
        val actualFontType = fontType ?: context.baseConfig.fontType
        val actualFontFileName = fontFileName ?: context.baseConfig.fontName
        if (actualFontType == cachedFontType && actualFontFileName == cachedFontFileName && cachedTypeface != null) {
            return cachedTypeface!!
        }

        cachedFontType = actualFontType
        cachedFontFileName = actualFontFileName
        cachedTypeface = when (actualFontType) {
            FONT_TYPE_MONOSPACE -> Typeface.MONOSPACE
            FONT_TYPE_CUSTOM -> loadCustomFont(context, actualFontFileName)
            FONT_TYPE_ASSET -> loadAssetFont(context, actualFontFileName)
            else -> Typeface.DEFAULT
        }
        return cachedTypeface!!
    }

    private fun loadAssetFont(context: Context, fileName: String): Typeface {
        if (fileName.isEmpty()) return Typeface.DEFAULT
        return try {
            Typeface.createFromAsset(context.assets, fileName)
        } catch (_: Exception) {
            Typeface.DEFAULT
        }
    }

    private fun loadCustomFont(context: Context, fileName: String): Typeface {
        if (fileName.isEmpty()) return Typeface.DEFAULT
        val fontFile = File(getFontsDir(context), fileName)
        if (!fontFile.exists()) {
            context.ensureFontPresentLocally(fileName)
        }

        return try {
            if (fontFile.exists()) Typeface.createFromFile(fontFile) else Typeface.DEFAULT
        } catch (_: Exception) {
            Typeface.DEFAULT
        }
    }

    fun clearCache() {
        cachedTypeface = null
        cachedFontType = -1
        cachedFontFileName = ""
    }

    fun getFontsDir(context: Context): File {
        return File(context.filesDir, "fonts").apply {
            if (!exists()) mkdirs()
        }
    }

    fun saveFontData(context: Context, fontData: ByteArray, fileName: String): Boolean {
        return try {
            val fontFile = File(getFontsDir(context), fileName)
            fontFile.writeBytes(fontData)
            true
        } catch (_: Exception) {
            false
        }
    }

    fun getFontData(context: Context, fileName: String): ByteArray? {
        if (fileName.isEmpty()) {
            return null
        }

        return try {
            val fontFile = File(getFontsDir(context), fileName)
            if (fontFile.exists()) fontFile.readBytes() else null
        } catch (_: Exception) {
            null
        }
    }

    fun deleteFont(context: Context, fileName: String): Boolean {
        return try {
            val fontFile = File(getFontsDir(context), fileName)
            if (fontFile.exists()) fontFile.delete() else true
        } catch (_: Exception) {
            false
        }
    }

    fun getAssetFonts(context: Context, path: String = "font"): List<String> {
        val fonts = mutableListOf<String>()
        val files = context.assets.list(path) ?: return fonts
        for (file in files) {
            val fullPath = if (path.isEmpty()) file else "$path/$file"
            if (file.endsWith(".ttf", true) || file.endsWith(".otf", true)) {
                fonts.add(fullPath)
            } else {
                // try as directory - assets.list() returns filenames, not full paths.
                // If it's a directory, list() should return its contents.
                // We can check if list() on fullPath returns anything.
                val subFiles = context.assets.list(fullPath)
                if (subFiles?.isNotEmpty() == true) {
                    fonts.addAll(getAssetFonts(context, fullPath))
                }
            }
        }
        return fonts
    }
}
