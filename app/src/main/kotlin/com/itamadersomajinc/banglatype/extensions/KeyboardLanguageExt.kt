package com.itamadersomajinc.banglatype.extensions

import android.annotation.SuppressLint
import android.content.Context
import com.itamadersomajinc.banglatype.commons.models.RadioItem
import com.itamadersomajinc.banglatype.R
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_ARABIC
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_BANGLA_AVRO
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_BANGLA_JATIYO
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_BANGLA_PROBHAT
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_BELARUSIAN_CYRL
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_BELARUSIAN_LATN
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_BENGALI
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_BULGARIAN
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_CENTRAL_KURDISH
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_CHUVASH
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_CZECH_QWERTY
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_CZECH_QWERTZ
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_DANISH
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_DUTCH
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_ENGLISH_ASSET
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_ENGLISH_COLEMAK
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_ENGLISH_COLEMAKDH
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_ENGLISH_DVORAK
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_ENGLISH_NIRO
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_ENGLISH_QWERTZ
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_ENGLISH_SOUL
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_ENGLISH_WORKMAN
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_ESPERANTO
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_FRENCH_AZERTY
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_FRENCH_BEPO
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_GERMAN
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_GERMAN_QWERTZ
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_GREEK
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_HEBREW
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_ITALIAN
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_KABYLE_AZERTY
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_LATVIAN
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_LITHUANIAN
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_NORWEGIAN
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_POLISH
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_PORTUGUESE
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_PORTUGUESE_HCESAR
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_ROMANIAN
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_RUSSIAN
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_SLOVENIAN
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_SPANISH
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_SWEDISH
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_TURKISH
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_TURKISH_Q
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_UKRAINIAN
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_VIETNAMESE_TELEX

/**
 * Bangla script layouts use Shift to switch to a second character layer (the "shifted" keycaps),
 * not to capitalize. Sentence-start auto-capitalization must therefore never auto-engage Shift for
 * them, otherwise the keyboard opens stuck on the shifted layer.
 */
fun isBanglaScriptLanguage(language: Int): Boolean =
    language == LANGUAGE_BANGLA_JATIYO ||
        language == LANGUAGE_BANGLA_PROBHAT ||
        language == LANGUAGE_BENGALI

/**
 * Any Bangla input language (the script layouts plus Avro phonetic). Used to decide when digits
 * typed into a text field should render and commit as Bengali numerals (১২৩৪…).
 */
fun isBanglaLanguage(language: Int): Boolean =
    isBanglaScriptLanguage(language) || language == LANGUAGE_BANGLA_AVRO

/** Maps an ASCII digit ('0'-'9') to its Bengali numeral ('০'-'৯'); returns other chars unchanged. */
fun Char.toBengaliDigitOrSelf(): Char = if (this in '0'..'9') '০' + (this - '0') else this

/** Maps a Bengali numeral ('০'-'৯') to its ASCII digit ('0'-'9'); returns other chars unchanged. */
fun Char.toAsciiDigitOrSelf(): Char = if (this in '০'..'৯') '0' + (this - '০') else this

fun String.toBengaliString(): String = this.map { it.toBengaliDigitOrSelf() }.joinToString("")
fun String.toAsciiString(): String = this.map { it.toAsciiDigitOrSelf() }.joinToString("")

fun Context.getSelectedLanguagesSorted(): List<Int> {
    return config.selectedLanguages
        .map { it to getKeyboardLanguageText(it) }
        .sortedBy { it.second }
        .map { it.first }
}

fun Context.getKeyboardLanguagesRadioItems(): ArrayList<RadioItem> {
    return getSelectedLanguagesSorted()
        .map { RadioItem(it, getKeyboardLanguageText(it)) }
        .toMutableList() as ArrayList<RadioItem>
}

@Suppress("CyclomaticComplexMethod")
fun Context.getKeyboardLanguageText(language: Int): String {
    return when (language) {
        LANGUAGE_ARABIC -> getString(R.string.translation_arabic)
        LANGUAGE_BANGLA_AVRO -> getString(R.string.translation_avro)
        LANGUAGE_BANGLA_JATIYO -> getString(R.string.translation_jatiyo)
        LANGUAGE_BANGLA_PROBHAT -> getString(R.string.translation_probhat)
        LANGUAGE_BELARUSIAN_CYRL -> "${getString(R.string.translation_belarusian)} (Cyrillic)"
        LANGUAGE_BELARUSIAN_LATN -> "${getString(R.string.translation_belarusian)} (Latin)"
        LANGUAGE_BENGALI -> getString(R.string.translation_bengali)
        LANGUAGE_BULGARIAN -> getString(R.string.translation_bulgarian)
        LANGUAGE_CENTRAL_KURDISH -> getString(R.string.translation_central_kurdish)
        LANGUAGE_CHUVASH -> getString(R.string.translation_chuvash)
        LANGUAGE_CZECH_QWERTY -> "${getString(R.string.translation_czech)} (QWERTY)"
        LANGUAGE_CZECH_QWERTZ -> "${getString(R.string.translation_czech)} (QWERTZ)"
        LANGUAGE_DANISH -> getString(R.string.translation_danish)
        LANGUAGE_DUTCH -> getString(R.string.translation_dutch)
        LANGUAGE_ENGLISH_ASSET -> "${getString(R.string.translation_english)} (Asset)"
        LANGUAGE_ENGLISH_COLEMAK -> "${getString(R.string.translation_english)} (Colemak)"
        LANGUAGE_ENGLISH_COLEMAKDH -> "${getString(R.string.translation_english)} (Colemak-DH)"
        LANGUAGE_ENGLISH_DVORAK -> "${getString(R.string.translation_english)} (DVORAK)"
        LANGUAGE_ENGLISH_NIRO -> "${getString(R.string.translation_english)} (Niro)"
        LANGUAGE_ENGLISH_QWERTZ -> "${getString(R.string.translation_english)} (QWERTZ)"
        LANGUAGE_ENGLISH_SOUL -> "${getString(R.string.translation_english)} (Soul)"
        LANGUAGE_ENGLISH_WORKMAN -> "${getString(R.string.translation_english)} (Workman)"
        LANGUAGE_ESPERANTO -> getString(R.string.translation_esperanto)
        LANGUAGE_FRENCH_AZERTY -> "${getString(R.string.translation_french)} (AZERTY)"
        LANGUAGE_FRENCH_BEPO -> "${getString(R.string.translation_french)} (BEPO)"
        LANGUAGE_GERMAN -> getString(R.string.translation_german)
        LANGUAGE_GERMAN_QWERTZ -> "${getString(R.string.translation_german)} (QWERTZ)"
        LANGUAGE_GREEK -> getString(R.string.translation_greek)
        LANGUAGE_HEBREW -> getString(R.string.translation_hebrew)
        LANGUAGE_ITALIAN -> getString(R.string.translation_italian)
        LANGUAGE_KABYLE_AZERTY -> "${getString(R.string.translation_kabyle)} (AZERTY)"
        LANGUAGE_LATVIAN -> getString(R.string.translation_latvian)
        LANGUAGE_LITHUANIAN -> getString(R.string.translation_lithuanian)
        LANGUAGE_NORWEGIAN -> getString(R.string.translation_norwegian)
        LANGUAGE_POLISH -> getString(R.string.translation_polish)
        LANGUAGE_PORTUGUESE -> getString(R.string.translation_portuguese)
        LANGUAGE_PORTUGUESE_HCESAR -> "${getString(R.string.translation_portuguese)} (HCESAR)"
        LANGUAGE_ROMANIAN -> getString(R.string.translation_romanian)
        LANGUAGE_RUSSIAN -> getString(R.string.translation_russian)
        LANGUAGE_SLOVENIAN -> getString(R.string.translation_slovenian)
        LANGUAGE_SPANISH -> getString(R.string.translation_spanish)
        LANGUAGE_SWEDISH -> getString(R.string.translation_swedish)
        LANGUAGE_TURKISH -> getString(R.string.translation_turkish)
        LANGUAGE_TURKISH_Q -> "${getString(R.string.translation_turkish)} (Q)"
        LANGUAGE_UKRAINIAN -> getString(R.string.translation_ukrainian)
        LANGUAGE_VIETNAMESE_TELEX -> "${getString(R.string.translation_vietnamese)} (Telex)"
        else -> "${getString(R.string.translation_english)} (QWERTY)"
    }
}
