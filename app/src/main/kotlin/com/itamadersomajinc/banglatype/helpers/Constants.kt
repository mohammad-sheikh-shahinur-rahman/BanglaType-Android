package com.itamadersomajinc.banglatype.helpers


enum class ShiftState {
    OFF,
    ON_ONE_CHAR,
    ON_PERMANENT;
}

// limit the count of alternative characters that show up at long pressing a key
const val MAX_KEYS_PER_MINI_ROW = 9

// shared prefs
const val VIBRATE_ON_KEYPRESS = "vibrate_on_keypress"
const val VIBRATION_DURATION = "vibration_duration"

const val SOUND_ON_KEYPRESS = "sound_on_keypress"
const val SOUND_NONE = 0
const val SOUND_SYSTEM = 1
const val SOUND_ALWAYS = 2

// keypress sound volume (used only when SOUND_ALWAYS), as percentage 0..100
const val KEYPRESS_SOUND_VOLUME = "keypress_sound_volume"
const val SOUND_VOLUME_LOW = 25
const val SOUND_VOLUME_MEDIUM = 60
const val SOUND_VOLUME_HIGH = 100

// keypress vibration strength (used only when vibrateOnKeypress is on)
const val VIBRATION_STRENGTH = "vibration_strength"
const val VIBRATION_SYSTEM = 0
const val VIBRATION_LIGHT = 1
const val VIBRATION_MEDIUM = 2
const val VIBRATION_STRONG = 3

// one-handed typing mode
const val ONE_HANDED_MODE = "one_handed_mode"
const val ONE_HANDED_OFF = 0
const val ONE_HANDED_LEFT = 1
const val ONE_HANDED_RIGHT = 2
const val ONE_HANDED_WIDTH_PERCENT = 82

const val SHOW_POPUP_ON_KEYPRESS = "show_popup_on_keypress"
const val SHOW_KEY_BORDERS = "show_key_borders"
const val SENTENCES_CAPITALIZATION = "sentences_capitalization"
const val AUTO_PUNCTUATION = "auto_punctuation"
const val SHOW_EMOJI_KEY = "show_emoji_key"
const val SHOW_LANGUAGE_SWITCH_KEY = "show_language_switch_key"
const val LAST_EXPORTED_CLIPS_FOLDER = "last_exported_clips_folder"
const val KEYBOARD_LANGUAGE = "keyboard_language"
const val HEIGHT_PERCENTAGE = "height_percentage"
const val SHOW_CLIPBOARD_CONTENT = "show_clipboard_content"
const val SHOW_SUGGESTIONS = "show_suggestions"
const val CLIPBOARD_HISTORY_ENABLED = "clipboard_history_enabled"
const val CLIPBOARD_HISTORY_LIMIT = 50
const val SHOW_NUMBERS_ROW = "show_numbers_row"
const val ENABLE_SHORTCUTS = "enable_shortcuts"
const val SELECTED_LANGUAGES = "selected_languages"
const val VOICE_INPUT_METHOD = "voice_input_method"
const val RECENTLY_USED_EMOJIS = "recently_used_emojis"
const val CONTINUOUS_VOICE_TYPING = "continuous_voice_typing"
const val VOICE_TYPING_PUNCTUATION = "voice_typing_punctuation"

// custom keyboard theme (Gboard-style) — these are keyboard-only and never touch the app theme
const val KEYBOARD_THEME_ID = "keyboard_theme_id"
const val KEYBOARD_BG_IMAGE_PATH = "keyboard_bg_image_path"
const val KEYBOARD_BG_DIM = "keyboard_bg_dim"
const val KEYBOARD_TEXT_COLOR = "keyboard_text_color"
const val KEYBOARD_COLOR = "keyboard_color"
const val KEYBOARD_PRIMARY_COLOR = "keyboard_primary_color"
const val KEYBOARD_KEY_OPACITY = "keyboard_key_opacity"
const val KEYBOARD_THEME_DEFAULT = "default"
const val KEYBOARD_THEME_CUSTOM_PHOTO = "custom_photo"
const val KEYBOARD_BG_IMAGE_FILE = "keyboard_bg.jpg"
const val DEFAULT_KEYBOARD_BG_DIM = 35
const val DEFAULT_KEYBOARD_KEY_OPACITY = 30

// differentiate current and pinned clips at the keyboards' Clipboard section
const val ITEM_SECTION_LABEL = 0
const val ITEM_CLIP = 1

const val LANGUAGE_ENGLISH_QWERTY = 0
const val LANGUAGE_RUSSIAN = 1
const val LANGUAGE_FRENCH_AZERTY = 2
const val LANGUAGE_ENGLISH_QWERTZ = 3
const val LANGUAGE_SPANISH = 4
const val LANGUAGE_GERMAN = 5
const val LANGUAGE_ENGLISH_DVORAK = 6
const val LANGUAGE_ROMANIAN = 7
const val LANGUAGE_SLOVENIAN = 8
const val LANGUAGE_BULGARIAN = 9
const val LANGUAGE_TURKISH_Q = 10
const val LANGUAGE_LITHUANIAN = 11
const val LANGUAGE_BENGALI = 12
const val LANGUAGE_GREEK = 13
const val LANGUAGE_NORWEGIAN = 14
const val LANGUAGE_SWEDISH = 15
const val LANGUAGE_DANISH = 16
const val LANGUAGE_FRENCH_BEPO = 17
const val LANGUAGE_VIETNAMESE_TELEX = 18
const val LANGUAGE_POLISH = 19
const val LANGUAGE_UKRAINIAN = 20
const val LANGUAGE_CHUVASH = 22
const val LANGUAGE_ESPERANTO = 23
const val LANGUAGE_HEBREW = 24
const val LANGUAGE_ARABIC = 25
const val LANGUAGE_CENTRAL_KURDISH = 26
const val LANGUAGE_BELARUSIAN_CYRL = 27
const val LANGUAGE_BELARUSIAN_LATN = 28
const val LANGUAGE_KABYLE_AZERTY = 29
const val LANGUAGE_CZECH_QWERTY = 30
const val LANGUAGE_ITALIAN = 31
const val LANGUAGE_CZECH_QWERTZ = 32
const val LANGUAGE_GERMAN_QWERTZ = 33
const val LANGUAGE_PORTUGUESE = 34
const val LANGUAGE_PORTUGUESE_HCESAR = 35
const val LANGUAGE_DUTCH = 36
const val LANGUAGE_LATVIAN = 37
const val LANGUAGE_TURKISH = 38
const val LANGUAGE_ENGLISH_ASSET = 39
const val LANGUAGE_ENGLISH_COLEMAK = 40
const val LANGUAGE_ENGLISH_COLEMAKDH = 41
const val LANGUAGE_ENGLISH_NIRO = 42
const val LANGUAGE_ENGLISH_SOUL = 43
const val LANGUAGE_ENGLISH_WORKMAN = 44

// Bangla layouts
const val LANGUAGE_BANGLA_AVRO = 45
const val LANGUAGE_BANGLA_JATIYO = 46
const val LANGUAGE_BANGLA_PROBHAT = 47

// Keep this sorted (by display name). BanglaType exposes only Bangla layouts plus plain English.
val SUPPORTED_LANGUAGES = listOf(
    LANGUAGE_BANGLA_AVRO,
    LANGUAGE_ENGLISH_QWERTY,
    LANGUAGE_BANGLA_JATIYO,
    LANGUAGE_BANGLA_PROBHAT
)

// keyboard height percentage options
const val KEYBOARD_HEIGHT_70_PERCENT = 70
const val KEYBOARD_HEIGHT_80_PERCENT = 80
const val KEYBOARD_HEIGHT_90_PERCENT = 90
const val KEYBOARD_HEIGHT_100_PERCENT = 100
const val KEYBOARD_HEIGHT_120_PERCENT = 120
const val KEYBOARD_HEIGHT_140_PERCENT = 140
const val KEYBOARD_HEIGHT_160_PERCENT = 160

const val EMOJI_SPEC_FILE_PATH = "media/emoji_spec.txt"
const val EMOJI_KEYWORDS_FILE_PATH = "media/emoji_keywords.json"
const val LANGUAGE_VN_TELEX = "language/extension.json"
const val LANGUAGE_AVRO_RULES = "language/avro_rules.json"
const val RECENT_EMOJIS_LIMIT = 36

// Android constant
const val INPUT_METHOD_SUBTYPE_VOICE = "voice"
