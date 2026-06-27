package com.itamadersomajinc.banglatype.helpers

import android.content.Context
import com.itamadersomajinc.banglatype.commons.helpers.BaseConfig
import com.itamadersomajinc.banglatype.extensions.isDeviceLocked
import com.itamadersomajinc.banglatype.extensions.safeStorageContext
import java.util.Locale

class Config(context: Context) : BaseConfig(context) {
    companion object {
        fun newInstance(context: Context) = Config(context.safeStorageContext)
    }

    var vibrateOnKeypress: Boolean
        get() = prefs.getBoolean(VIBRATE_ON_KEYPRESS, true)
        set(vibrateOnKeypress) = prefs.edit().putBoolean(VIBRATE_ON_KEYPRESS, vibrateOnKeypress).apply()

    var keypressVibrationDuration: Int
        get() = prefs.getInt(VIBRATION_DURATION, 30)
        set(duration) = prefs.edit().putInt(VIBRATION_DURATION, duration).apply()

    var soundOnKeypress: Int
        get() = prefs.getInt(SOUND_ON_KEYPRESS, SOUND_SYSTEM)
        set(soundOnKeypress) = prefs.edit().putInt(SOUND_ON_KEYPRESS, soundOnKeypress).apply()

    var keypressSoundVolume: Int
        get() = prefs.getInt(KEYPRESS_SOUND_VOLUME, SOUND_VOLUME_HIGH)
        set(keypressSoundVolume) = prefs.edit().putInt(KEYPRESS_SOUND_VOLUME, keypressSoundVolume).apply()

    var vibrationStrength: Int
        get() = prefs.getInt(VIBRATION_STRENGTH, VIBRATION_SYSTEM)
        set(vibrationStrength) = prefs.edit().putInt(VIBRATION_STRENGTH, vibrationStrength).apply()

    var oneHandedMode: Int
        get() = prefs.getInt(ONE_HANDED_MODE, ONE_HANDED_OFF)
        set(oneHandedMode) = prefs.edit().putInt(ONE_HANDED_MODE, oneHandedMode).apply()

    var showPopupOnKeypress: Boolean
        get() = prefs.getBoolean(SHOW_POPUP_ON_KEYPRESS, true)
        set(showPopupOnKeypress) = prefs.edit().putBoolean(SHOW_POPUP_ON_KEYPRESS, showPopupOnKeypress).apply()

    var enableSentencesCapitalization: Boolean
        get() = prefs.getBoolean(SENTENCES_CAPITALIZATION, true)
        set(enableCapitalization) = prefs.edit().putBoolean(SENTENCES_CAPITALIZATION, enableCapitalization).apply()

    var autoPunctuation: Boolean
        get() = prefs.getBoolean(AUTO_PUNCTUATION, true)
        set(autoPunctuation) = prefs.edit().putBoolean(AUTO_PUNCTUATION, autoPunctuation).apply()

    // Opt-in: off by default so a plain spacebar swipe changes language. When enabled, swiping the
    // spacebar moves the text cursor instead.
    var spaceSwipeCursorControl: Boolean
        get() = prefs.getBoolean("space_swipe_cursor_control", false)
        set(value) = prefs.edit().putBoolean("space_swipe_cursor_control", value).apply()

    var swipeDeleteWord: Boolean
        get() = prefs.getBoolean("swipe_delete_word", true)
        set(value) = prefs.edit().putBoolean("swipe_delete_word", value).apply()

    var enableShortcuts: Boolean
        get() = prefs.getBoolean(ENABLE_SHORTCUTS, true)
        set(enableShortcuts) = prefs.edit().putBoolean(ENABLE_SHORTCUTS, enableShortcuts).apply()

    var showEmojiKey: Boolean
        get() = prefs.getBoolean(SHOW_EMOJI_KEY, true)
        set(showEmojiKey) = prefs.edit().putBoolean(SHOW_EMOJI_KEY, showEmojiKey).apply()

    var showLanguageSwitchKey: Boolean
        get() = prefs.getBoolean(SHOW_LANGUAGE_SWITCH_KEY, false)
        set(showLanguageSwitchKey) = prefs.edit().putBoolean(SHOW_LANGUAGE_SWITCH_KEY, showLanguageSwitchKey).apply()

    var showKeyBorders: Boolean
        get() = prefs.getBoolean(SHOW_KEY_BORDERS, true)
        set(showKeyBorders) = prefs.edit().putBoolean(SHOW_KEY_BORDERS, showKeyBorders).apply()

    var lastExportedClipsFolder: String
        get() = prefs.getString(LAST_EXPORTED_CLIPS_FOLDER, "")!!
        set(lastExportedClipsFolder) = prefs.edit().putString(LAST_EXPORTED_CLIPS_FOLDER, lastExportedClipsFolder).apply()

    var keyboardLanguage: Int
        get() = prefs.getInt(KEYBOARD_LANGUAGE, LANGUAGE_BANGLA_AVRO)
        set(keyboardLanguage) = prefs.edit().putInt(KEYBOARD_LANGUAGE, keyboardLanguage).apply()

    var keyboardHeightPercentage: Int
        get() = prefs.getInt(HEIGHT_PERCENTAGE, 100)
        set(keyboardHeightMultiplier) = prefs.edit().putInt(HEIGHT_PERCENTAGE, keyboardHeightMultiplier).apply()

    var showClipboardContent: Boolean
        get() = prefs.getBoolean(SHOW_CLIPBOARD_CONTENT, true)
        set(showClipboardContent) = prefs.edit().putBoolean(SHOW_CLIPBOARD_CONTENT, showClipboardContent).apply()

    var showSuggestions: Boolean
        get() = prefs.getBoolean(SHOW_SUGGESTIONS, true)
        set(showSuggestions) = prefs.edit().putBoolean(SHOW_SUGGESTIONS, showSuggestions).apply()

    var clipboardHistoryEnabled: Boolean
        get() = prefs.getBoolean(CLIPBOARD_HISTORY_ENABLED, true)
        set(clipboardHistoryEnabled) = prefs.edit().putBoolean(CLIPBOARD_HISTORY_ENABLED, clipboardHistoryEnabled).apply()

    var showNumbersRow: Boolean
        get() = if (context.isDeviceLocked) {
            true
        } else {
            prefs.getBoolean(SHOW_NUMBERS_ROW, false)
        }
        set(showNumbersRow) = prefs.edit().putBoolean(SHOW_NUMBERS_ROW, showNumbersRow).apply()

    var voiceInputMethod: String
        get() = prefs.getString(VOICE_INPUT_METHOD, "")!!
        set(voiceInputMethod) = prefs.edit().putString(VOICE_INPUT_METHOD, voiceInputMethod).apply()

    var continuousVoiceTyping: Boolean
        get() = prefs.getBoolean(CONTINUOUS_VOICE_TYPING, true)
        set(value) = prefs.edit().putBoolean(CONTINUOUS_VOICE_TYPING, value).apply()

    var voiceTypingPunctuation: Boolean
        get() = prefs.getBoolean(VOICE_TYPING_PUNCTUATION, true)
        set(value) = prefs.edit().putBoolean(VOICE_TYPING_PUNCTUATION, value).apply()

    // ---- Custom keyboard theme (Gboard-style) ----

    var keyboardThemeId: String
        get() = prefs.getString(KEYBOARD_THEME_ID, KEYBOARD_THEME_DEFAULT)!!
        set(keyboardThemeId) = prefs.edit().putString(KEYBOARD_THEME_ID, keyboardThemeId).apply()

    // Absolute path of the user-picked background photo (stored in device-protected storage so the
    // keyboard can read it even while the device is locked). Empty when no photo theme is set.
    var keyboardBackgroundImagePath: String
        get() = prefs.getString(KEYBOARD_BG_IMAGE_PATH, "")!!
        set(keyboardBackgroundImagePath) = prefs.edit().putString(KEYBOARD_BG_IMAGE_PATH, keyboardBackgroundImagePath).apply()

    // Darkening scrim drawn over the background image/gradient, 0 (none) .. 100 (black).
    var keyboardBackgroundDim: Int
        get() = prefs.getInt(KEYBOARD_BG_DIM, DEFAULT_KEYBOARD_BG_DIM)
        set(keyboardBackgroundDim) = prefs.edit().putInt(KEYBOARD_BG_DIM, keyboardBackgroundDim).apply()

    // Keyboard-only colors (kept separate from the app theme so changing a keyboard theme never
    // recolors the rest of the app). Only used when a custom keyboard theme is selected.
    var keyboardTextColor: Int
        get() = prefs.getInt(KEYBOARD_TEXT_COLOR, 0xFFEEEEEE.toInt())
        set(keyboardTextColor) = prefs.edit().putInt(KEYBOARD_TEXT_COLOR, keyboardTextColor).apply()

    var keyboardColor: Int
        get() = prefs.getInt(KEYBOARD_COLOR, 0xFF2E2E2E.toInt())
        set(keyboardColor) = prefs.edit().putInt(KEYBOARD_COLOR, keyboardColor).apply()

    var keyboardPrimaryColor: Int
        get() = prefs.getInt(KEYBOARD_PRIMARY_COLOR, 0xFF0078F8.toInt())
        set(keyboardPrimaryColor) = prefs.edit().putInt(KEYBOARD_PRIMARY_COLOR, keyboardPrimaryColor).apply()

    // Key contrast over the background, 0 (barely visible) .. 100 (most opaque).
    var keyboardKeyOpacity: Int
        get() = prefs.getInt(KEYBOARD_KEY_OPACITY, DEFAULT_KEYBOARD_KEY_OPACITY)
        set(keyboardKeyOpacity) = prefs.edit().putInt(KEYBOARD_KEY_OPACITY, keyboardKeyOpacity).apply()

    var selectedLanguages: MutableSet<Int>
        get() {
            val defaultLanguages = hashSetOf(
                LANGUAGE_BANGLA_AVRO.toString(),
                LANGUAGE_ENGLISH_QWERTY.toString(),
                LANGUAGE_BANGLA_PROBHAT.toString()
            )
            val stringSet = prefs.getStringSet(SELECTED_LANGUAGES, defaultLanguages)!!
            return stringSet.map { it.toInt() }.toMutableSet()
        }
        set(selectedLanguages) {
            val stringSet = selectedLanguages.map { it.toString() }.toSet()
            prefs.edit().putStringSet(SELECTED_LANGUAGES, stringSet).apply()
        }

    fun getDefaultLanguage(): Int {
        val conf = context.resources.configuration
        return if (conf.locale.toString().lowercase(Locale.getDefault()).startsWith("ru_")) {
            LANGUAGE_RUSSIAN
        } else {
            LANGUAGE_ENGLISH_QWERTY
        }
    }

    var recentlyUsedEmojis: List<String>
        get() = prefs.getString(RECENTLY_USED_EMOJIS, "\uD83D\uDC30")!!.split("|").filter { it.isNotEmpty() }
        set(recentlyUsedEmojis) = prefs.edit().putString(
            RECENTLY_USED_EMOJIS, recentlyUsedEmojis.joinToString("|")
        ).apply()

    fun addRecentEmoji(emoji: String) {
        val recentEmojis = recentlyUsedEmojis.toMutableList()
        recentEmojis.remove(emoji)
        recentEmojis.add(0, emoji)
        recentlyUsedEmojis = recentEmojis.take(RECENT_EMOJIS_LIMIT)
    }
}
