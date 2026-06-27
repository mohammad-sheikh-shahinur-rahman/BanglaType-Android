package com.itamadersomajinc.banglatype.services

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable
import android.icu.text.BreakIterator
import android.icu.util.ULocale
import android.inputmethodservice.InputMethodService
import android.os.Build
import android.os.Bundle
import android.text.InputType.TYPE_CLASS_DATETIME
import android.text.InputType.TYPE_CLASS_NUMBER
import android.text.InputType.TYPE_CLASS_PHONE
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_MASK_CLASS
import android.text.InputType.TYPE_MASK_VARIATION
import android.text.InputType.TYPE_NULL
import android.text.TextUtils
import android.util.Size
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.CursorAnchorInfo
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.EditorInfo.IME_ACTION_NONE
import android.view.inputmethod.EditorInfo.IME_FLAG_NO_ENTER_ACTION
import android.view.inputmethod.EditorInfo.IME_MASK_ACTION
import android.view.inputmethod.ExtractedTextRequest
import android.view.inputmethod.InlineSuggestionsRequest
import android.view.inputmethod.InlineSuggestionsResponse
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodSubtype
import android.widget.Toast
import android.widget.inline.InlinePresentationSpec
import androidx.annotation.RequiresApi
import androidx.autofill.inline.UiVersions
import androidx.autofill.inline.common.ImageViewStyle
import androidx.autofill.inline.common.TextViewStyle
import androidx.autofill.inline.common.ViewStyle
import androidx.autofill.inline.v1.InlineSuggestionUi
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat.Type
import androidx.core.view.updatePadding
import com.itamadersomajinc.banglatype.commons.extensions.applyColorFilter
import com.itamadersomajinc.banglatype.commons.extensions.getProperBackgroundColor
import com.itamadersomajinc.banglatype.commons.extensions.getProperTextColor
import com.itamadersomajinc.banglatype.commons.extensions.getSharedPrefs
import com.itamadersomajinc.banglatype.commons.extensions.setSystemBarsAppearance
import com.itamadersomajinc.banglatype.commons.helpers.ACCENT_COLOR
import com.itamadersomajinc.banglatype.commons.helpers.BACKGROUND_COLOR
import com.itamadersomajinc.banglatype.commons.helpers.CUSTOM_ACCENT_COLOR
import com.itamadersomajinc.banglatype.commons.helpers.CUSTOM_BACKGROUND_COLOR
import com.itamadersomajinc.banglatype.commons.helpers.CUSTOM_PRIMARY_COLOR
import com.itamadersomajinc.banglatype.commons.helpers.CUSTOM_TEXT_COLOR
import com.itamadersomajinc.banglatype.commons.helpers.IS_GLOBAL_THEME_ENABLED
import com.itamadersomajinc.banglatype.commons.helpers.IS_SYSTEM_THEME_ENABLED
import com.itamadersomajinc.banglatype.commons.helpers.PRIMARY_COLOR
import com.itamadersomajinc.banglatype.commons.helpers.TEXT_COLOR
import com.itamadersomajinc.banglatype.commons.helpers.isNougatPlus
import com.itamadersomajinc.banglatype.commons.helpers.isPiePlus
import com.itamadersomajinc.banglatype.R
import com.itamadersomajinc.banglatype.activities.SettingsActivity
import com.itamadersomajinc.banglatype.databinding.KeyboardViewKeyboardBinding
import com.itamadersomajinc.banglatype.extensions.config
import com.itamadersomajinc.banglatype.extensions.shortcutsDB
import com.itamadersomajinc.banglatype.extensions.getKeyboardBackgroundColor
import com.itamadersomajinc.banglatype.extensions.getKeyboardBackgroundDrawable
import com.itamadersomajinc.banglatype.extensions.hasKeyboardBackgroundDrawable
import com.itamadersomajinc.banglatype.extensions.getVoiceInputLocale
import com.itamadersomajinc.banglatype.extensions.getKeyboardLanguageText
import com.itamadersomajinc.banglatype.extensions.isBanglaLanguage
import com.itamadersomajinc.banglatype.extensions.isBanglaScriptLanguage
import com.itamadersomajinc.banglatype.extensions.toBengaliDigitOrSelf
import com.itamadersomajinc.banglatype.extensions.toBengaliString
import com.itamadersomajinc.banglatype.extensions.toAsciiString
import com.itamadersomajinc.banglatype.commons.extensions.toast
import com.itamadersomajinc.banglatype.extensions.getPreferredVoiceInputMethod
import com.itamadersomajinc.banglatype.extensions.getSelectedLanguagesSorted
import com.itamadersomajinc.banglatype.extensions.getStrokeColor
import com.itamadersomajinc.banglatype.extensions.safeStorageContext
import com.itamadersomajinc.banglatype.helpers.HEIGHT_PERCENTAGE
import com.itamadersomajinc.banglatype.helpers.ONE_HANDED_OFF
import com.itamadersomajinc.banglatype.helpers.ONE_HANDED_LEFT
import com.itamadersomajinc.banglatype.helpers.ONE_HANDED_RIGHT
import com.itamadersomajinc.banglatype.helpers.ONE_HANDED_WIDTH_PERCENT
import com.itamadersomajinc.banglatype.helpers.KEYBOARD_LANGUAGE
import com.itamadersomajinc.banglatype.helpers.LANGUAGE_ARABIC
import com.itamadersomajinc.banglatype.helpers.AvroParser
import com.itamadersomajinc.banglatype.helpers.PredictionEngine
import com.itamadersomajinc.banglatype.commons.helpers.ensureBackgroundThread
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
import com.itamadersomajinc.banglatype.helpers.MyKeyboard
import com.itamadersomajinc.banglatype.helpers.VoiceInputManager
import com.itamadersomajinc.banglatype.helpers.SHOW_KEY_BORDERS
import com.itamadersomajinc.banglatype.helpers.SHOW_NUMBERS_ROW
import com.itamadersomajinc.banglatype.helpers.ShiftState
import com.itamadersomajinc.banglatype.helpers.VOICE_INPUT_METHOD
import com.itamadersomajinc.banglatype.helpers.KEYBOARD_THEME_ID
import com.itamadersomajinc.banglatype.helpers.KEYBOARD_BG_IMAGE_PATH
import com.itamadersomajinc.banglatype.helpers.KEYBOARD_BG_DIM
import com.itamadersomajinc.banglatype.helpers.cachedVNTelexData
import com.itamadersomajinc.banglatype.interfaces.OnKeyboardActionListener
import com.itamadersomajinc.banglatype.views.MyKeyboardView
import java.io.ByteArrayOutputStream
import java.util.Locale


// based on https://www.androidauthority.com/lets-build-custom-keyboard-android-832362/
class SimpleKeyboardIME : InputMethodService(), OnKeyboardActionListener, SharedPreferences.OnSharedPreferenceChangeListener {
    companion object {
        // How quickly do we have to double tap shift to enable permanent caps lock
        private var SHIFT_PERM_TOGGLE_SPEED = 500

        // Keyboard modes
        const val KEYBOARD_LETTERS = 0
        const val KEYBOARD_SYMBOLS = 1
        const val KEYBOARD_SYMBOLS_SHIFT = 2
        const val KEYBOARD_NUMBERS = 3
        const val KEYBOARD_PHONE = 4
        const val KEYBOARD_SYMBOLS_ALT = 5
    }

    private var keyboard: MyKeyboard? = null
    private var keyboardView: MyKeyboardView? = null
    private var lastShiftPressTS = 0L
    private var keyboardMode = KEYBOARD_LETTERS
    private var inputTypeClass = TYPE_CLASS_TEXT
    private var inputTypeClassVariation = TYPE_CLASS_TEXT
    private var enterKeyType = IME_ACTION_NONE
    private var switchToLetters = false
    private var breakIterator: BreakIterator? = null

    // Raw Latin text typed for the in-progress word while the Avro Phonetic layout is active.
    // It is re-parsed to Bengali on every keystroke and shown as composing text.
    private val avroComposing = StringBuilder()
    private val isAvro get() = baseContext.config.keyboardLanguage == LANGUAGE_BANGLA_AVRO

    // In-memory trigger -> expansion map for text shortcuts; reloaded on every onStartInput so edits
    // made in the management screen are picked up without a per-keystroke database hit.
    private var shortcutsMap: Map<String, String> = emptyMap()

    private lateinit var binding: KeyboardViewKeyboardBinding

    private var voiceInputManager: VoiceInputManager? = null

    override fun onInitializeInterface() {
        super.onInitializeInterface()
        safeStorageContext.getSharedPrefs().registerOnSharedPreferenceChangeListener(this)
    }

    override fun onCreateInputView(): View {
        binding = KeyboardViewKeyboardBinding.inflate(layoutInflater)
        keyboardView = binding.keyboardView.apply {
            setKeyboardHolder(binding)
            setKeyboard(keyboard!!)
            setEditorInfo(currentInputEditorInfo)
            setupEdgeToEdge()
            mOnKeyboardActionListener = this@SimpleKeyboardIME
            updateOneHandedMode(config.oneHandedMode)
        }

        return binding.root
    }

    override fun onStartInputView(editorInfo: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(editorInfo, restarting)
        updateBackgroundColors()
        binding.keyboardHolder.post {
            ViewCompat.requestApplyInsets(binding.keyboardHolder)
        }
    }

    override fun onPress(primaryCode: Int) {
        if (primaryCode != 0) {
            keyboardView?.performKeypressFeedback(primaryCode)
        }
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        voiceInputManager?.stop()
        keyboardView?.setVoiceListening(false)
    }

    override fun onDestroy() {
        voiceInputManager?.destroy()
        voiceInputManager = null
        super.onDestroy()
    }

    override fun onStartInput(attribute: EditorInfo?, restarting: Boolean) {
        super.onStartInput(attribute, restarting)
        avroComposing.clear()
        keyboardView?.setPredictions(emptyList())
        if (config.showSuggestions) {
            ensureBackgroundThread { PredictionEngine.preload(this) }
        }
        ensureBackgroundThread {
            shortcutsMap = if (config.enableShortcuts) {
                shortcutsDB.getShortcuts().associate { it.trigger to it.expansion }
            } else {
                emptyMap()
            }
        }
        // Auto-select Google voice typing (or any available) so the mic works without setup.
        if (config.voiceInputMethod.isEmpty()) {
            ensureBackgroundThread { getPreferredVoiceInputMethod()?.let { config.voiceInputMethod = it.first.id } }
        }
        inputTypeClass = attribute!!.inputType and TYPE_MASK_CLASS
        inputTypeClassVariation = attribute.inputType and TYPE_MASK_VARIATION
        enterKeyType = attribute.imeOptions and (IME_MASK_ACTION or IME_FLAG_NO_ENTER_ACTION)
        keyboard = createNewKeyboard()
        keyboardView?.setKeyboard(keyboard!!)
        keyboardView?.setEditorInfo(attribute)
        if (isNougatPlus()) {
            breakIterator = BreakIterator.getCharacterInstance(ULocale.getDefault())
        }
        updateShiftKeyState()
    }

    private fun updateShiftKeyState() {
        if (keyboard?.mShiftState == ShiftState.ON_PERMANENT) {
            return
        }

        val editorInfo = currentInputEditorInfo
        if (config.enableSentencesCapitalization && !isBanglaScriptLanguage(config.keyboardLanguage) &&
            editorInfo != null && editorInfo.inputType != TYPE_NULL
        ) {
            if (currentInputConnection.getCursorCapsMode(editorInfo.inputType) != 0) {
                keyboard?.setShifted(ShiftState.ON_ONE_CHAR)
                keyboardView?.invalidateAllKeys()
                return
            }
        }

        keyboard?.setShifted(ShiftState.OFF)
        keyboardView?.invalidateAllKeys()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateInlineSuggestionsRequest(uiExtras: Bundle): InlineSuggestionsRequest {
        val maxWidth = resources.getDimensionPixelSize(R.dimen.suggestion_max_width)

        return InlineSuggestionsRequest.Builder(
            listOf(
                InlinePresentationSpec.Builder(
                    Size(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT),
                    Size(maxWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
                ).setStyle(buildSuggestionTextStyle()).build()
            )
        ).setMaxSuggestionCount(InlineSuggestionsRequest.SUGGESTION_COUNT_UNLIMITED)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onInlineSuggestionsResponse(response: InlineSuggestionsResponse): Boolean {
        keyboardView?.clearClipboardViews()

        response.inlineSuggestions.forEach {
            it.inflate(this, Size(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT), this.mainExecutor) { view ->
                // If inflation fails for whatever reason, passed view will be null
                if (view != null) {
                    keyboardView?.addToClipboardViews(view, addToFront = it.info.isPinned)
                }
            }
        }

        return true
    }

    override fun onKey(code: Int) {
        val inputConnection = currentInputConnection
        if (keyboard == null || inputConnection == null) {
            return
        }

        if (code != MyKeyboard.KEYCODE_SHIFT) {
            lastShiftPressTS = 0
        }

        // Avro Phonetic: any key other than a letter keystroke, Shift, or Delete ends the
        // current word, so flush the composing buffer before handling it normally.
        val isAvroLetterKey = isAvro && keyboardMode == KEYBOARD_LETTERS && code > 0 && Character.isLetter(code.toChar())
        if (isAvro && code != MyKeyboard.KEYCODE_SHIFT && code != MyKeyboard.KEYCODE_DELETE && !isAvroLetterKey) {
            finishAvroComposing()
        }

        when (code) {
            MyKeyboard.KEYCODE_DELETE -> {
                if (isAvro && avroComposing.isNotEmpty()) {
                    avroComposing.deleteCharAt(avroComposing.length - 1)
                    if (avroComposing.isEmpty()) {
                        inputConnection.finishComposingText()
                    } else {
                        inputConnection.setComposingText(AvroParser.parse(this, avroComposing.toString()), 1)
                    }
                    updateShiftKeyState()
                    updateSuggestions()
                    return
                }
                val selectedText = inputConnection.getSelectedText(0)
                if (TextUtils.isEmpty(selectedText)) {
                    val count = getCountToDelete(inputConnection)
                    inputConnection.deleteSurroundingText(count, 0)
                } else {
                    inputConnection.commitText("", 1)
                }
            }

            MyKeyboard.KEYCODE_SHIFT -> {
                if (keyboardMode == KEYBOARD_LETTERS) {
                    when {
                        keyboard!!.mShiftState == ShiftState.ON_PERMANENT -> keyboard!!.mShiftState = ShiftState.OFF
                        System.currentTimeMillis() - lastShiftPressTS < SHIFT_PERM_TOGGLE_SPEED -> keyboard!!.mShiftState = ShiftState.ON_PERMANENT
                        keyboard!!.mShiftState == ShiftState.ON_ONE_CHAR -> keyboard!!.mShiftState = ShiftState.OFF
                        keyboard!!.mShiftState == ShiftState.OFF -> keyboard!!.mShiftState = ShiftState.ON_ONE_CHAR
                    }

                    lastShiftPressTS = System.currentTimeMillis()
                } else {
                    val keyboardXml = if (keyboardMode == KEYBOARD_SYMBOLS) {
                        keyboardMode = KEYBOARD_SYMBOLS_SHIFT
                        R.xml.keys_symbols_shift
                    } else {
                        keyboardMode = KEYBOARD_SYMBOLS
                        R.xml.keys_symbols
                    }
                    keyboard = constructKeyboard(keyboardXml, enterKeyType)
                    keyboardView!!.setKeyboard(keyboard!!)
                }
                keyboardView!!.invalidateAllKeys()
            }

            MyKeyboard.KEYCODE_ENTER -> {
                learnLastWord()
                maybeEvaluateMath(inputConnection)
                val imeOptionsActionId = getImeOptionsActionId()
                if (imeOptionsActionId != IME_ACTION_NONE) {
                    inputConnection.performEditorAction(imeOptionsActionId)
                } else {
                    inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
                    inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
                }
            }

            MyKeyboard.KEYCODE_SYMBOLS_MODE_CHANGE -> {
                val keyboardXML = if (keyboardMode == KEYBOARD_SYMBOLS || keyboardMode == KEYBOARD_SYMBOLS_SHIFT) {
                    keyboardMode = KEYBOARD_SYMBOLS_ALT
                    R.xml.keys_symbols_alt
                } else {
                    keyboardMode = KEYBOARD_SYMBOLS
                    R.xml.keys_symbols
                }

                keyboard = constructKeyboard(keyboardXML, enterKeyType)
                keyboardView!!.setKeyboard(keyboard!!)
            }

            MyKeyboard.KEYCODE_MODE_CHANGE -> {
                val keyboardXml = if (keyboardMode == KEYBOARD_LETTERS) {
                    keyboardMode = KEYBOARD_SYMBOLS
                    R.xml.keys_symbols
                } else {
                    keyboardMode = KEYBOARD_LETTERS
                    getKeyboardLayoutXML()
                }

                keyboard = constructKeyboard(keyboardXml, enterKeyType)
                keyboardView!!.setKeyboard(keyboard!!)
            }

            MyKeyboard.KEYCODE_EMOJI_OR_LANGUAGE -> {
                if (config.showEmojiKey) {
                    keyboardView?.openEmojiPalette()
                } else if (config.showLanguageSwitchKey) {
                    val sortedLanguages = getSelectedLanguagesSorted()
                    if (sortedLanguages.size > 1) {
                        val currentIndex = sortedLanguages.indexOf(config.keyboardLanguage)
                        val nextIndex = (currentIndex + 1) % sortedLanguages.size
                        config.keyboardLanguage = sortedLanguages[nextIndex]
                        reloadKeyboard()
                    }
                }
            }

            MyKeyboard.KEYCODE_POPUP_EMOJI -> keyboardView?.openEmojiPalette()
            MyKeyboard.KEYCODE_POPUP_TEXT_EDIT -> keyboardView?.openTextEditingPanel()
            MyKeyboard.KEYCODE_POPUP_SETTINGS -> Intent(this, SettingsActivity::class.java)
                .apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(this)
                }

            else -> {
                var codeChar = code.toChar()
                val originalText = inputConnection.getExtractedText(ExtractedTextRequest(), 0)?.text

                if (Character.isLetter(codeChar) && keyboard!!.mShiftState > ShiftState.OFF) {
                    if (baseContext.config.keyboardLanguage == LANGUAGE_TURKISH_Q) {
                        codeChar = codeChar.toString().uppercase(Locale.forLanguageTag("tr")).single()
                    } else {
                        codeChar = Character.toUpperCase(codeChar)
                    }
                }

                // Auto-punctuation: a quick second space after a word becomes a sentence-ending
                // full stop plus a space ("। " for Bangla, ". " otherwise), like most keyboards.
                if (code == MyKeyboard.KEYCODE_SPACE && keyboardMode == KEYBOARD_LETTERS &&
                    inputTypeClass == TYPE_CLASS_TEXT && maybeAutoPunctuate(inputConnection)
                ) {
                    updateShiftKeyState()
                    updateSuggestions()
                    return
                }

                // Avro Phonetic: accumulate the Latin letter into the current word and show the
                // live Bengali transliteration as composing text. Case is preserved (e.g. S vs s)
                // so the parser can distinguish case-sensitive phonemes.
                if (isAvro && keyboardMode == KEYBOARD_LETTERS && Character.isLetter(codeChar)) {
                    avroComposing.append(codeChar)
                    inputConnection.setComposingText(AvroParser.parse(this, avroComposing.toString()), 1)
                    updateShiftKeyState()
                    updateSuggestions()
                    return
                }

                // If the keyboard is set to symbols and the user presses space, we usually should switch back to the letters keyboard.
                // However, avoid doing that in cases when the EditText for example requires numbers as the input.
                // We can detect that by the text not changing on pressing Space.
                if (keyboardMode != KEYBOARD_LETTERS && inputTypeClass == TYPE_CLASS_TEXT && code == MyKeyboard.KEYCODE_SPACE) {
                    inputConnection.commitText(codeChar.toString(), 1)
                    val newText = inputConnection.getExtractedText(ExtractedTextRequest(), 0)?.text
                    if (originalText != newText) {
                        switchToLetters = keyboardMode != KEYBOARD_SYMBOLS_ALT
                    }
                } else {
                    when {
                        !originalText.isNullOrEmpty() && cachedVNTelexData.isNotEmpty() -> {
                            val fullText = originalText.toString() + codeChar.toString()
                            val lastIndexEmpty = if (fullText.contains(" ")) {
                                fullText.lastIndexOf(" ")
                            } else 0
                            if (lastIndexEmpty >= 0) {
                                val word = fullText.subSequence(lastIndexEmpty, fullText.length).trim().toString()
                                val wordChars = word.toCharArray()
                                val predictWord = StringBuilder()
                                for (char in wordChars.size - 1 downTo 0) {
                                    predictWord.append(wordChars[char])
                                    val shouldChangeText = predictWord.reverse().toString()
                                    if (cachedVNTelexData.containsKey(shouldChangeText)) {
                                        inputConnection.setComposingRegion(fullText.length - shouldChangeText.length, fullText.length)
                                        inputConnection.setComposingText(cachedVNTelexData[shouldChangeText], fullText.length)
                                        inputConnection.setComposingRegion(fullText.length, fullText.length)
                                        return
                                    }
                                }
                                inputConnection.commitText(codeChar.toString(), 1)
                                updateShiftKeyState()
                            }
                        }

                        else -> {
                            inputConnection.commitText(codeChar.toString(), 1)
                            updateShiftKeyState()
                        }
                    }
                }
            }
        }

        // Expand a text shortcut once the user types a word separator (space or punctuation).
        // Special keys (shift, delete, enter, mode change) use negative codes and are skipped.
        if (code > 0 && !Character.isLetterOrDigit(code.toChar())) {
            maybeExpandShortcut(inputConnection)
        }

        // A space ends the current word — learn it for future suggestions.
        if (code == MyKeyboard.KEYCODE_SPACE) {
            learnLastWord()
            maybeEvaluateMath(inputConnection)
        }
        updateSuggestions()
    }

    /**
     * If the word just before the cursor looks like a math expression (e.g. "5+5"), evaluates it
     * and replaces that word with the result.
     */
    private fun maybeEvaluateMath(inputConnection: InputConnection) {
        val before = inputConnection.getTextBeforeCursor(64, 0)?.toString() ?: return
        val prefix = currentMathExpressionPrefix(before)
        if (prefix.length > 1 && prefix.any { it.isDigit() || it in '০'..'৯' } &&
            (prefix.contains("+") || prefix.contains("-") || prefix.contains("*") || prefix.contains("/"))) {
            val result = tryEvaluateMath(prefix)
            if (result != prefix) {
                inputConnection.beginBatchEdit()
                inputConnection.deleteSurroundingText(prefix.length, 0)
                inputConnection.commitText(result, 1)
                inputConnection.endBatchEdit()
            }
        }
    }

    private fun currentMathExpressionPrefix(s: String): String {
        var start = s.length
        while (start > 0 && (Character.isLetterOrDigit(s[start - 1]) || "+-*/().".contains(s[start - 1]))) start--
        return s.substring(start)
    }

    /**
     * Double-space → sentence end: if the cursor is preceded by "[letter/digit][space]" and the user
     * presses space again, swap that previous space for a full stop and a space ("। " for Bangla,
     * ". " otherwise). Returns true when it acted, so the caller skips committing the normal space.
     */
    private fun maybeAutoPunctuate(inputConnection: InputConnection): Boolean {
        if (!config.autoPunctuation) {
            return false
        }
        if (isAvro && avroComposing.isNotEmpty()) {
            return false
        }
        val before = inputConnection.getTextBeforeCursor(2, 0) ?: return false
        if (before.length < 2 || before[1] != ' ' || !Character.isLetterOrDigit(before[0])) {
            return false
        }
        val sentenceEnd = if (isBanglaLanguage(config.keyboardLanguage)) "। " else ". "
        inputConnection.beginBatchEdit()
        inputConnection.deleteSurroundingText(1, 0)
        inputConnection.commitText(sentenceEnd, 1)
        inputConnection.endBatchEdit()
        return true
    }

    /**
     * Replaces a just-typed shortcut trigger with its expansion. Call right after a word separator
     * (space/punctuation) has been committed: the buffer then ends with [trigger][separator], so we
     * look back at the word preceding the separator and swap it for the configured expansion.
     */
    private fun maybeExpandShortcut(inputConnection: InputConnection) {
        if (!config.enableShortcuts || shortcutsMap.isEmpty()) {
            return
        }
        val before = inputConnection.getTextBeforeCursor(64, 0)?.toString() ?: return
        if (before.isEmpty()) {
            return
        }
        val separator = before.last()
        if (isWordChar(separator)) {
            return
        }
        val trigger = currentWordPrefix(before.dropLast(1))
        if (trigger.isEmpty()) {
            return
        }
        val expansion = shortcutsMap[trigger] ?: return
        inputConnection.beginBatchEdit()
        inputConnection.deleteSurroundingText(trigger.length + 1, 0)
        inputConnection.commitText(expansion + separator, 1)
        inputConnection.endBatchEdit()
    }

    private fun getCountToDelete(inputConnection: InputConnection): Int {
        if (breakIterator == null || !isNougatPlus()) {
            return 1
        }

        val prevText = inputConnection.getTextBeforeCursor(8, 0)


        if (!TextUtils.isEmpty(prevText)) {
            return breakIterator?.let {
                it.setText(prevText.toString())
                val end = it.last()
                val start = it.previous()
                (end - (if (start == BreakIterator.DONE) 0 else start)).coerceIn(0, prevText?.length)
            } ?: 1
        }

        return 1
    }

    override fun onActionUp() {
        if (switchToLetters) {
            // TODO: Change keyboardMode to enum class
            keyboardMode = KEYBOARD_LETTERS

            keyboard = constructKeyboard(getKeyboardLayoutXML(), enterKeyType)

            val editorInfo = currentInputEditorInfo
            if (!isBanglaScriptLanguage(config.keyboardLanguage) && editorInfo != null &&
                editorInfo.inputType != TYPE_NULL && keyboard?.mShiftState != ShiftState.ON_PERMANENT
            ) {
                if (currentInputConnection.getCursorCapsMode(editorInfo.inputType) != 0) {
                    keyboard?.setShifted(ShiftState.ON_ONE_CHAR)
                }
            }

            keyboardView!!.setKeyboard(keyboard!!)
            switchToLetters = false
        }
    }

    override fun moveCursorLeft() {
        moveCursor(false)
    }

    override fun moveCursorRight() {
        moveCursor(true)
    }

    override fun onDeleteWord() {
        val inputConnection = currentInputConnection ?: return

        // Avro: trim the live composing buffer instead of touching committed text.
        if (isAvro && avroComposing.isNotEmpty()) {
            avroComposing.clear()
            inputConnection.finishComposingText()
            updateShiftKeyState()
            updateSuggestions()
            return
        }

        // If something is selected, a word-delete just removes the selection.
        val selectedText = inputConnection.getSelectedText(0)
        if (!TextUtils.isEmpty(selectedText)) {
            inputConnection.commitText("", 1)
            updateSuggestions()
            return
        }

        val before = inputConnection.getTextBeforeCursor(100, 0) ?: return
        if (before.isEmpty()) return

        // Delete the run of trailing whitespace, then the word before it.
        var end = before.length
        while (end > 0 && before[end - 1].isWhitespace()) end--
        while (end > 0 && !before[end - 1].isWhitespace()) end--
        val count = before.length - end
        inputConnection.deleteSurroundingText(if (count > 0) count else 1, 0)
        updateSuggestions()
    }

    override fun onEditCursorMove(keyCode: Int, withShift: Boolean) {
        val inputConnection = currentInputConnection ?: return
        val meta = if (withShift) KeyEvent.META_SHIFT_ON or KeyEvent.META_SHIFT_LEFT_ON else 0
        inputConnection.sendKeyEvent(KeyEvent(0, 0, KeyEvent.ACTION_DOWN, keyCode, 0, meta))
        inputConnection.sendKeyEvent(KeyEvent(0, 0, KeyEvent.ACTION_UP, keyCode, 0, meta))
        keyboardView?.performHapticHandleMove()
    }

    override fun onEditContextAction(menuActionId: Int) {
        currentInputConnection?.performContextMenuAction(menuActionId)
    }

    override fun onText(text: String) {
        var textToCommit = text
        if (textToCommit.length > 1 && (textToCommit.contains("+") || textToCommit.contains("-") || textToCommit.contains("*") || textToCommit.contains("/"))) {
            textToCommit = tryEvaluateMath(textToCommit)
        }
        currentInputConnection?.commitText(textToCommit, 1)
    }

    private fun tryEvaluateMath(text: String): String {
        return try {
            val isBengali = text.any { it in '০'..'৯' }
            val expression = text.toAsciiString().trim()
            if (!expression.any { it.isDigit() }) return text
            
            // Simple regex based evaluator for basic expressions
            val result = object : Any() {
                fun eval(str: String): Double {
                    return object : Any() {
                        var pos = -1
                        var ch = 0
                        fun nextChar() {
                            ch = if (++pos < str.length) str[pos].toInt() else -1
                        }

                        fun eat(charToEat: Int): Boolean {
                            while (ch == ' '.toInt()) nextChar()
                            if (ch == charToEat) {
                                nextChar()
                                return true
                            }
                            return false
                        }

                        fun parse(): Double {
                            nextChar()
                            val x = parseExpression()
                            if (pos < str.length) return Double.NaN
                            return x
                        }

                        fun parseExpression(): Double {
                            var x = parseTerm()
                            while (true) {
                                if (eat('+'.toInt())) x += parseTerm()
                                else if (eat('-'.toInt())) x -= parseTerm()
                                else return x
                            }
                        }

                        fun parseTerm(): Double {
                            var x = parseFactor()
                            while (true) {
                                if (eat('*'.toInt())) x *= parseFactor()
                                else if (eat('/'.toInt())) x /= parseFactor()
                                else return x
                            }
                        }

                        fun parseFactor(): Double {
                            if (eat('+'.toInt())) return parseFactor()
                            if (eat('-'.toInt())) return -parseFactor()
                            var x: Double
                            val startPos = pos
                            if (eat('('.toInt())) {
                                x = parseExpression()
                                eat(')'.toInt())
                            } else if (ch >= '0'.toInt() && ch <= '9'.toInt() || ch == '.'.toInt()) {
                                while (ch >= '0'.toInt() && ch <= '9'.toInt() || ch == '.'.toInt()) nextChar()
                                x = str.substring(startPos, pos).toDouble()
                            } else {
                                return Double.NaN
                            }
                            return x
                        }
                    }.parse()
                }
            }.eval(expression)

            if (result.isNaN()) text else {
                val longRes = result.toLong()
                val resString = if (result == longRes.toDouble()) longRes.toString() else result.toString()
                if (isBengali) resString.toBengaliString() else resString
            }
        } catch (e: Exception) {
            text
        }
    }

    override fun reloadKeyboard() {
        val keyboard = createNewKeyboard()
        this.keyboard = keyboard
        keyboardView?.setKeyboard(keyboard)
    }

    override fun onOneHandedModeChanged(mode: Int) {
        config.oneHandedMode = mode
        // Rebuild at the new width, then re-apply the side alignment / controls on the holder.
        reloadKeyboard()
        keyboardView?.updateOneHandedMode(mode)
    }

    override fun changeInputMethod(id: String, subtype: InputMethodSubtype) {
        if (isPiePlus()) {
            switchInputMethod(id, subtype)
        } else {
            switchInputMethod(id)
        }
    }

    override fun onStartVoiceInput() {
        val manager = voiceInputManager ?: VoiceInputManager(this).also { voiceInputManager = it }
        if (!manager.isAvailable()) {
            toast(R.string.no_app_found)
            keyboardView?.setVoiceListening(false)
            return
        }

        keyboardView?.setVoiceListening(true)
        keyboardView?.setVoicePartialText("")
        manager.start(getVoiceInputLocale(), continuous = config.continuousVoiceTyping, object : VoiceInputManager.Callbacks {
            override fun onPartialResult(text: String) {
                keyboardView?.setVoicePartialText(text)
            }

            override fun onFinalResult(text: String, isEndOfSession: Boolean) {
                if (text.isNotBlank()) {
                    val processedText = if (config.voiceTypingPunctuation) applyVoiceCommands(text) else text
                    currentInputConnection?.commitText("$processedText ", 1)
                    learnLastWord()
                    updateShiftKeyState()
                    updateSuggestions()
                }
                if (isEndOfSession) {
                    keyboardView?.setVoiceListening(false)
                    keyboardView?.setVoicePartialText("")
                } else {
                    keyboardView?.setVoicePartialText("")
                }
            }

            override fun onRmsChanged(rmsdB: Float) {
                keyboardView?.setVoiceLevel(rmsdB)
            }

            override fun onEndOfSpeech() {
                keyboardView?.setVoicePartialText(getString(R.string.processing))
            }

            override fun onError(errorCode: Int) {
                keyboardView?.setVoiceListening(false)
                keyboardView?.setVoicePartialText("")
            }
        })
    }

    private fun applyVoiceCommands(text: String): String {
        var result = text
        val locale = getVoiceInputLocale()
        if (locale == "bn-BD") {
            result = result
                .replace("দাঁড়ি", "।")
                .replace("কমা", ",")
                .replace("প্রশ্নবোধক", "?")
                .replace("বিস্ময়সূচক", "!")
                .replace("নতুন লাইন", "\n")
        } else {
            result = result
                .replace("period", ".")
                .replace("full stop", ".")
                .replace("comma", ",")
                .replace("question mark", "?")
                .replace("exclamation mark", "!")
                .replace("new line", "\n")
        }
        return result
    }

    override fun onStopVoiceInput() {
        voiceInputManager?.stop()
        keyboardView?.setVoiceListening(false)
    }

    private fun createNewKeyboard(): MyKeyboard {
        val keyboardXml = when (inputTypeClass) {
            TYPE_CLASS_NUMBER -> {
                keyboardMode = KEYBOARD_NUMBERS
                R.xml.keys_numbers
            }

            TYPE_CLASS_PHONE -> {
                keyboardMode = KEYBOARD_PHONE
                R.xml.keys_phone
            }

            TYPE_CLASS_DATETIME -> {
                keyboardMode = KEYBOARD_SYMBOLS
                R.xml.keys_symbols
            }

            else -> {
                keyboardMode = KEYBOARD_LETTERS
                getKeyboardLayoutXML()
            }
        }
        return constructKeyboard(keyboardXml, enterKeyType)
    }

    override fun onUpdateSelection(oldSelStart: Int, oldSelEnd: Int, newSelStart: Int, newSelEnd: Int, candidatesStart: Int, candidatesEnd: Int) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd)
        // If the cursor moved away from the Avro composing region (tap elsewhere, selection, etc.),
        // commit what we have and reset so the Latin buffer never desyncs from the field.
        if (avroComposing.isNotEmpty() && (candidatesStart < 0 || newSelStart != newSelEnd || newSelEnd != candidatesEnd)) {
            finishAvroComposing()
        }
        if (newSelStart == newSelEnd) {
            keyboardView?.closeClipboardManager()
            updateSuggestions()
        }
        updateShiftKeyState()
    }

    private fun finishAvroComposing() {
        if (avroComposing.isNotEmpty()) {
            currentInputConnection?.finishComposingText()
            avroComposing.clear()
        }
    }

    // ---- Word prediction / suggestions ----

    private fun isWordChar(c: Char) = c.isLetter()

    /** Trailing run of word characters before the cursor (empty if the last char is a separator). */
    private fun currentWordPrefix(s: String): String {
        var start = s.length
        while (start > 0 && isWordChar(s[start - 1])) start--
        return s.substring(start)
    }

    /** Returns (word-before-last, last-word) found at the tail of [s], ignoring trailing separators. */
    private fun lastTwoWords(s: String): Pair<String?, String> {
        var end = s.length
        while (end > 0 && !isWordChar(s[end - 1])) end--
        var start = end
        while (start > 0 && isWordChar(s[start - 1])) start--
        val last = s.substring(start, end)
        var pEnd = start
        while (pEnd > 0 && !isWordChar(s[pEnd - 1])) pEnd--
        var pStart = pEnd
        while (pStart > 0 && isWordChar(s[pStart - 1])) pStart--
        val prev = if (pEnd > pStart) s.substring(pStart, pEnd) else null
        return prev to last
    }

    private fun textBeforeCursor(): String =
        currentInputConnection?.getTextBeforeCursor(64, 0)?.toString() ?: ""

    private fun updateSuggestions() {
        val kbView = keyboardView ?: return
        if (!config.showSuggestions || keyboardMode != KEYBOARD_LETTERS) {
            kbView.setPredictions(emptyList())
            return
        }
        val avroActive = isAvro && avroComposing.isNotEmpty()
        val avroWord = if (avroActive) AvroParser.parse(this, avroComposing.toString()) else null
        val before = if (avroActive) "" else textBeforeCursor()
        ensureBackgroundThread {
            val suggestions: List<String> = when {
                avroWord != null -> PredictionEngine.currentWordSuggestions(this, avroWord)
                else -> {
                    val prefix = currentWordPrefix(before)
                    if (prefix.isNotEmpty()) {
                        PredictionEngine.currentWordSuggestions(this, prefix)
                    } else {
                        val prev = lastTwoWords(before).second
                        if (prev.isNotEmpty()) PredictionEngine.nextWordSuggestions(this, prev) else emptyList()
                    }
                }
            }
            kbView.post { kbView.setPredictions(suggestions) }
        }
    }

    /** Records the word that was just completed (call after committing a space/enter/punctuation). */
    private fun learnLastWord() {
        if (!config.showSuggestions) {
            return
        }
        val (prev, last) = lastTwoWords(textBeforeCursor())
        if (last.isNotEmpty()) {
            ensureBackgroundThread { PredictionEngine.learn(this, prev, last) }
        }
    }

    override fun onPredictionPicked(word: String) {
        val inputConnection = currentInputConnection ?: return
        val prevWord: String?
        if (isAvro && avroComposing.isNotEmpty()) {
            val composing = AvroParser.parse(this, avroComposing.toString())
            val beforeNoComposing = textBeforeCursor().removeSuffix(composing)
            prevWord = lastTwoWords(beforeNoComposing).second.ifEmpty { null }
            inputConnection.setComposingText(word, 1)
            inputConnection.finishComposingText()
            avroComposing.clear()
            inputConnection.commitText(" ", 1)
        } else {
            val before = textBeforeCursor()
            val prefix = currentWordPrefix(before)
            prevWord = lastTwoWords(before.dropLast(prefix.length)).second.ifEmpty { null }
            if (prefix.isNotEmpty()) {
                inputConnection.deleteSurroundingText(prefix.length, 0)
            }
            inputConnection.commitText("$word ", 1)
        }
        ensureBackgroundThread { PredictionEngine.learn(this, prevWord, word) }
        updateShiftKeyState()
        updateSuggestions()
    }

    override fun onSpaceSwipeLanguage(forward: Boolean) {
        val languages = getSelectedLanguagesSorted()
        if (languages.size < 2) {
            return
        }
        val currentIndex = languages.indexOf(config.keyboardLanguage)
        val nextIndex = if (forward) {
            (currentIndex + 1) % languages.size
        } else {
            (currentIndex - 1 + languages.size) % languages.size
        }
        config.keyboardLanguage = languages[nextIndex]
        finishAvroComposing()
        keyboardView?.setPredictions(emptyList())
        reloadKeyboard()
        toast(getKeyboardLanguageText(config.keyboardLanguage), Toast.LENGTH_SHORT)
    }

    override fun onUpdateCursorAnchorInfo(cursorAnchorInfo: CursorAnchorInfo?) {
        super.onUpdateCursorAnchorInfo(cursorAnchorInfo)
        updateShiftKeyState()
    }

    private fun moveCursor(moveRight: Boolean) {
        val inputConnection = currentInputConnection
        val extractedText = inputConnection.getExtractedText(ExtractedTextRequest(), 0) ?: return
        val text = extractedText.text ?: return
        val oldPos = extractedText.selectionStart
        val newPos = if (moveRight) {
            oldPos + 1
        } else {
            oldPos - 1
        }.coerceIn(0, text.length)

        if (newPos != oldPos) {
            inputConnection?.setSelection(newPos, newPos)
            keyboardView?.performHapticHandleMove()
        }
    }

    private fun getImeOptionsActionId(): Int {
        return if (currentInputEditorInfo.imeOptions and IME_FLAG_NO_ENTER_ACTION != 0) {
            IME_ACTION_NONE
        } else {
            currentInputEditorInfo.imeOptions and IME_MASK_ACTION
        }
    }

    private fun getKeyboardLayoutXML(): Int {
        return when (baseContext.config.keyboardLanguage) {
            LANGUAGE_ARABIC -> R.xml.keys_letters_arabic
            LANGUAGE_BANGLA_AVRO -> R.xml.keys_letters_english_qwerty
            LANGUAGE_BANGLA_JATIYO -> R.xml.keys_letters_bangla_jatiyo
            LANGUAGE_BANGLA_PROBHAT -> R.xml.keys_letters_bangla_probhat
            LANGUAGE_BELARUSIAN_CYRL -> R.xml.keys_letters_belarusian_cyrl
            LANGUAGE_BELARUSIAN_LATN -> R.xml.keys_letters_belarusian_latn
            LANGUAGE_BENGALI -> R.xml.keys_letters_bengali
            LANGUAGE_BULGARIAN -> R.xml.keys_letters_bulgarian
            LANGUAGE_CENTRAL_KURDISH -> R.xml.keys_letters_central_kurdish
            LANGUAGE_CHUVASH -> R.xml.keys_letters_chuvash
            LANGUAGE_CZECH_QWERTY -> R.xml.keys_letters_czech_qwerty
            LANGUAGE_CZECH_QWERTZ -> R.xml.keys_letters_czech_qwertz
            LANGUAGE_DANISH -> R.xml.keys_letters_danish
            LANGUAGE_DUTCH -> R.xml.keys_letters_dutch
            LANGUAGE_ENGLISH_ASSET -> R.xml.keys_letters_english_asset
            LANGUAGE_ENGLISH_COLEMAK -> R.xml.keys_letters_english_colemak
            LANGUAGE_ENGLISH_COLEMAKDH -> R.xml.keys_letters_english_colemakdh
            LANGUAGE_ENGLISH_DVORAK -> R.xml.keys_letters_english_dvorak
            LANGUAGE_ENGLISH_NIRO -> R.xml.keys_letters_english_niro
            LANGUAGE_ENGLISH_QWERTZ -> R.xml.keys_letters_english_qwertz
            LANGUAGE_ENGLISH_SOUL -> R.xml.keys_letters_english_soul
            LANGUAGE_ENGLISH_WORKMAN -> R.xml.keys_letters_english_workman
            LANGUAGE_ESPERANTO -> R.xml.keys_letters_esperanto
            LANGUAGE_FRENCH_AZERTY -> R.xml.keys_letters_french_azerty
            LANGUAGE_FRENCH_BEPO -> R.xml.keys_letters_french_bepo
            LANGUAGE_GERMAN -> R.xml.keys_letters_german
            LANGUAGE_GERMAN_QWERTZ -> R.xml.keys_letters_german_qwertz
            LANGUAGE_GREEK -> R.xml.keys_letters_greek
            LANGUAGE_HEBREW -> R.xml.keys_letters_hebrew
            LANGUAGE_ITALIAN -> R.xml.keys_letters_italian
            LANGUAGE_KABYLE_AZERTY -> R.xml.keys_letters_kabyle_azerty
            LANGUAGE_LATVIAN -> R.xml.keys_letters_latvian
            LANGUAGE_LITHUANIAN -> R.xml.keys_letters_lithuanian
            LANGUAGE_NORWEGIAN -> R.xml.keys_letters_norwegian
            LANGUAGE_POLISH -> R.xml.keys_letters_polish
            LANGUAGE_PORTUGUESE -> R.xml.keys_letters_portuguese
            LANGUAGE_PORTUGUESE_HCESAR -> R.xml.keys_letters_portuguese_hcesar
            LANGUAGE_ROMANIAN -> R.xml.keys_letters_romanian
            LANGUAGE_RUSSIAN -> R.xml.keys_letters_russian
            LANGUAGE_SLOVENIAN -> R.xml.keys_letters_slovenian
            LANGUAGE_SWEDISH -> R.xml.keys_letters_swedish
            LANGUAGE_SPANISH -> R.xml.keys_letters_spanish_qwerty
            LANGUAGE_TURKISH -> R.xml.keys_letters_turkish
            LANGUAGE_TURKISH_Q -> R.xml.keys_letters_turkish_q
            LANGUAGE_UKRAINIAN -> R.xml.keys_letters_ukrainian
            else -> R.xml.keys_letters_english_qwerty
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("RestrictedApi", "UseCompatLoadingForDrawables")
    private fun buildSuggestionTextStyle(): Bundle {
        val stylesBuilder = UiVersions.newStylesBuilder()

        val verticalPadding = resources.getDimensionPixelSize(R.dimen.small_margin)
        val horizontalPadding = resources.getDimensionPixelSize(R.dimen.activity_margin)

        val textSize = resources.getDimension(R.dimen.label_text_size) / resources.displayMetrics.scaledDensity

        val rippleBg = resources.getDrawable(R.drawable.clipboard_background, theme) as RippleDrawable
        val layerDrawable = rippleBg.findDrawableByLayerId(R.id.clipboard_background_holder) as LayerDrawable
        layerDrawable.findDrawableByLayerId(R.id.clipboard_background_stroke).applyColorFilter(getStrokeColor())
        layerDrawable.findDrawableByLayerId(R.id.clipboard_background_shape).applyColorFilter(getProperBackgroundColor())

        val maxWidth = resources.getDimensionPixelSize(R.dimen.suggestion_max_width)
        val height = resources.getDimensionPixelSize(R.dimen.label_text_size) + verticalPadding * 2
        val chipBackgroundIcon: Icon = rippleBg.toBitmap(width = maxWidth, height = height).toIcon()

        val chipStyle =
            ViewStyle.Builder()
                // don't use Icon.createWithBitmap(), it crashes the app. Issue https://github.com/SimpleMobileTools/Simple-Keyboard/issues/248
                .setBackground(chipBackgroundIcon)
                .setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
                .build()

        val iconStyle = ImageViewStyle.Builder().build()

        val style = InlineSuggestionUi.newStyleBuilder()
            .setSingleIconChipStyle(chipStyle)
            .setChipStyle(chipStyle)
            .setStartIconStyle(iconStyle)
            .setEndIconStyle(iconStyle)
            .setSingleIconChipIconStyle(iconStyle)
            .setTitleStyle(
                TextViewStyle.Builder()
                    .setLayoutMargin(0, 0, horizontalPadding, 0)
                    .setTextColor(getProperTextColor())
                    .setTextSize(textSize)
                    .build()
            )
            .setSubtitleStyle(
                TextViewStyle.Builder()
                    .setTextColor(getProperTextColor())
                    .setTextSize(textSize)
                    .build()
            )
            .build()
        stylesBuilder.addStyle(style)
        return stylesBuilder.build()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key != null && key in arrayOf(
                SHOW_KEY_BORDERS, KEYBOARD_LANGUAGE, HEIGHT_PERCENTAGE, SHOW_NUMBERS_ROW, VOICE_INPUT_METHOD,
                TEXT_COLOR, BACKGROUND_COLOR, PRIMARY_COLOR, ACCENT_COLOR, CUSTOM_TEXT_COLOR, CUSTOM_BACKGROUND_COLOR,
                CUSTOM_PRIMARY_COLOR, CUSTOM_ACCENT_COLOR, IS_GLOBAL_THEME_ENABLED, IS_SYSTEM_THEME_ENABLED,
                KEYBOARD_THEME_ID, KEYBOARD_BG_IMAGE_PATH, KEYBOARD_BG_DIM
            )
        ) {
            if (::binding.isInitialized) {
                keyboardView?.setupKeyboard()
                updateBackgroundColors()
            }
        }
    }

    private fun setupEdgeToEdge() {
        window.window?.apply {
            WindowCompat.enableEdgeToEdge(this)
            ViewCompat.setOnApplyWindowInsetsListener(binding.keyboardHolder) { view, insets ->
                val system = insets.getInsetsIgnoringVisibility(Type.systemBars())
                binding.keyboardHolder.updatePadding(bottom = system.bottom)
                insets
            }
        }
    }

    private fun updateBackgroundColors() {
        val backgroundColor = safeStorageContext.getKeyboardBackgroundColor()
        val holder = binding.keyboardHolder
        // Photo/gradient theme: paint the image across the whole keyboard (toolbar + keys) area.
        val bgDrawable = if (safeStorageContext.hasKeyboardBackgroundDrawable()) {
            safeStorageContext.getKeyboardBackgroundDrawable(holder.width, holder.height)
        } else {
            null
        }
        if (bgDrawable != null) {
            holder.background = bgDrawable
        } else {
            holder.setBackgroundColor(backgroundColor)
        }
        window.window?.setSystemBarsAppearance(backgroundColor)
    }

    private fun Bitmap.toIcon(): Icon {
        val byteArray: ByteArray = ByteArrayOutputStream().let { outputStream ->
            this.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.toByteArray()
        }
        this.recycle()

        return Icon.createWithData(byteArray, 0, byteArray.size)
    }

    private fun constructKeyboard(keyboardXml: Int, enterKeyType: Int): MyKeyboard {
        // In one-handed mode the keyboard is built at a reduced width so its keys reflow into a
        // narrower block; MyKeyboardView then shifts that block to the chosen side.
        val widthOverride = if (config.oneHandedMode != ONE_HANDED_OFF) {
            resources.displayMetrics.widthPixels * ONE_HANDED_WIDTH_PERCENT / 100
        } else {
            null
        }
        val keyboard = MyKeyboard(this, keyboardXml, enterKeyType, widthOverride)
        if (shouldUseBanglaDigits()) {
            applyBanglaDigits(keyboard)
        }
        return adjustBottomRow(keyboard)
    }

    /**
     * Bengali numerals are used for digits only while typing a Bangla language into a plain text field.
     * Number/phone/date fields keep ASCII digits so the host app can still parse the value.
     */
    private fun shouldUseBanglaDigits(): Boolean =
        isBanglaLanguage(config.keyboardLanguage) && inputTypeClass == TYPE_CLASS_TEXT

    /** Rewrites digit keys (label + committed code + corner hint) on [keyboard] to Bengali numerals. */
    private fun applyBanglaDigits(keyboard: MyKeyboard) {
        keyboard.mKeys?.forEach { key ->
            val label = key.label
            if (label.length == 1 && label[0] in '0'..'9' && key.code == label[0].code) {
                val bengali = label[0].toBengaliDigitOrSelf()
                key.label = bengali.toString()
                key.code = bengali.code
            }
            if (key.topSmallNumber.isNotEmpty()) {
                key.topSmallNumber = key.topSmallNumber.map { it.toBengaliDigitOrSelf() }.joinToString("")
            }
        }
    }

    // hacky, but good enough for now
    private fun adjustBottomRow(keyboard: MyKeyboard): MyKeyboard {
        keyboard.mKeys?.let { keys ->
            val spaceKeyIndex = keys.indexOfFirst { it.code == MyKeyboard.KEYCODE_SPACE }
            if (spaceKeyIndex != -1) {
                val spaceKey = keys[spaceKeyIndex]
                spaceKey.label = spaceKey.label.ifEmpty {
                    if (config.selectedLanguages.size > 1) getKeyboardLanguageText(config.keyboardLanguage) else ""
                }
            }

            if (keyboardMode != KEYBOARD_LETTERS) return keyboard
            val emojiKeyIndex = keys.indexOfFirst { it.code == MyKeyboard.KEYCODE_EMOJI_OR_LANGUAGE }
            if (emojiKeyIndex != -1 && spaceKeyIndex != -1) {
                val emojiKey = keys[emojiKeyIndex]
                val spaceKey = keys[spaceKeyIndex]
                emojiKey.secondaryIcon = null
                when {
                    config.showEmojiKey -> {
                        // no-op
                    }
                    config.showLanguageSwitchKey -> {
                        emojiKey.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_language_outlined, theme)
                    }
                    else -> {
                        // both emoji and language keys are disabled
                        spaceKey.width += emojiKey.width + emojiKey.gap
                        spaceKey.x = emojiKey.x

                        val mutableKeys = keys.toMutableList()
                        mutableKeys.removeAt(emojiKeyIndex)
                        keyboard.mKeys = mutableKeys
                    }
                }
            }

            // When emoji key is enabled, show settings-only popup with no hint on tools key
            if (config.showEmojiKey) {
                val currentKeys = keyboard.mKeys ?: return keyboard
                val toolsKey = currentKeys.firstOrNull { it.role == MyKeyboard.KEY_ROLE_TOOLS }
                if (toolsKey != null) {
                    toolsKey.popupResId = R.xml.popup_tools
                    toolsKey.secondaryIcon = null
                }
            }
        }
        return keyboard
    }
}
