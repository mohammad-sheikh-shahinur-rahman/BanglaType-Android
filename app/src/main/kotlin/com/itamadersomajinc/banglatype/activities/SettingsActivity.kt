package com.itamadersomajinc.banglatype.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.itamadersomajinc.banglatype.commons.dialogs.RadioGroupDialog
import com.itamadersomajinc.banglatype.commons.extensions.beVisibleIf
import com.itamadersomajinc.banglatype.commons.extensions.getProperPrimaryColor
import com.itamadersomajinc.banglatype.commons.extensions.updateTextColors
import com.itamadersomajinc.banglatype.commons.extensions.viewBinding
import com.itamadersomajinc.banglatype.commons.helpers.NavigationIcon
import com.itamadersomajinc.banglatype.commons.helpers.isTiramisuPlus
import com.itamadersomajinc.banglatype.commons.models.RadioItem
import com.itamadersomajinc.banglatype.R
import com.itamadersomajinc.banglatype.databinding.ActivitySettingsBinding
import com.itamadersomajinc.banglatype.dialogs.AboutDialog
import com.itamadersomajinc.banglatype.dialogs.ManageKeyboardLanguagesDialog
import com.itamadersomajinc.banglatype.extensions.config
import com.itamadersomajinc.banglatype.extensions.getCurrentVoiceInputMethod
import com.itamadersomajinc.banglatype.extensions.getKeyboardLanguageText
import com.itamadersomajinc.banglatype.extensions.getKeyboardLanguagesRadioItems
import com.itamadersomajinc.banglatype.extensions.getVoiceInputMethods
import com.itamadersomajinc.banglatype.extensions.getVoiceInputRadioItems
import com.itamadersomajinc.banglatype.extensions.selectedKeyboardTheme
import com.itamadersomajinc.banglatype.helpers.KEYBOARD_THEME_CUSTOM_PHOTO
import com.itamadersomajinc.banglatype.helpers.KEYBOARD_HEIGHT_100_PERCENT
import com.itamadersomajinc.banglatype.helpers.KEYBOARD_HEIGHT_120_PERCENT
import com.itamadersomajinc.banglatype.helpers.KEYBOARD_HEIGHT_140_PERCENT
import com.itamadersomajinc.banglatype.helpers.KEYBOARD_HEIGHT_160_PERCENT
import com.itamadersomajinc.banglatype.helpers.KEYBOARD_HEIGHT_70_PERCENT
import com.itamadersomajinc.banglatype.helpers.KEYBOARD_HEIGHT_80_PERCENT
import com.itamadersomajinc.banglatype.helpers.KEYBOARD_HEIGHT_90_PERCENT
import com.itamadersomajinc.banglatype.helpers.SOUND_ALWAYS
import com.itamadersomajinc.banglatype.helpers.SOUND_NONE
import com.itamadersomajinc.banglatype.helpers.SOUND_SYSTEM
import com.itamadersomajinc.banglatype.helpers.SOUND_VOLUME_HIGH
import com.itamadersomajinc.banglatype.helpers.SOUND_VOLUME_LOW
import com.itamadersomajinc.banglatype.helpers.SOUND_VOLUME_MEDIUM
import com.itamadersomajinc.banglatype.helpers.VIBRATION_LIGHT
import com.itamadersomajinc.banglatype.helpers.VIBRATION_MEDIUM
import com.itamadersomajinc.banglatype.helpers.VIBRATION_STRONG
import com.itamadersomajinc.banglatype.helpers.VIBRATION_SYSTEM
import com.itamadersomajinc.banglatype.helpers.ONE_HANDED_LEFT
import com.itamadersomajinc.banglatype.helpers.ONE_HANDED_OFF
import com.itamadersomajinc.banglatype.helpers.ONE_HANDED_RIGHT
import java.util.Locale
import kotlin.system.exitProcess

class SettingsActivity : SimpleActivity() {
    private val binding by viewBinding(ActivitySettingsBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            setupEdgeToEdge(padBottomSystem = listOf(settingsNestedScrollview))
            setupMaterialScrollListener(binding.settingsNestedScrollview, binding.settingsAppbar)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onResume() {
        super.onResume()
        setupTopAppBar(binding.settingsAppbar, NavigationIcon.Arrow)

        setupCustomizeColors()
        setupKeyboardTheme()
        setupUseEnglish()
        setupLanguage()
        // setupManageClipboardItems() - Removed from this version of UI for now or handled differently
        setupVibrateOnKeypress()
        // setupVibrationStrength()
        setupSoundOnKeypress()
        // setupSoundVolume()
        setupShowPopupOnKeypress()
        setupShowKeyBorders()
        setupManageKeyboardLanguages()
        setupKeyboardLanguage()
        setupKeyboardHeightMultiplier()
        // setupOneHandedMode()
        // setupShowEmojiKey()
        // setupShowLanguageSwitchKey()
        // setupShowClipboardContent()
        // setupSentencesCapitalization()
        setupAutoPunctuation()
        setupShowNumbersRow()
        // setupEnableShortcuts()
        // setupSpaceSwipeCursorControl()
        // setupSwipeDeleteWord()
        // setupManageShortcuts()
        // setupVoiceInputMethod()
        setupContinuousVoiceTyping()
        setupVoiceTypingPunctuation()
        setupSupport()

        binding.apply {
            updateTextColors(settingsNestedScrollview)

            arrayOf(
                settingsColorCustomizationSectionLabel,
                settingsGeneralSettingsLabel,
                settingsLayoutAppearanceLabel,
                settingsKeypressLabel,
                settingsTypingInputLabel,
                settingsSupportLabel
            ).forEach {
                it.setTextColor(getProperPrimaryColor())
            }

            arrayOf(
                settingsColorCustomizationIcon,
                settingsKeyboardThemeIcon
            ).forEach {
                it.setColorFilter(getProperPrimaryColor())
            }
        }
    }

    private fun setupSupport() {
        binding.apply {
            settingsManualHolder.setOnClickListener {
                startActivity(Intent(this@SettingsActivity, ManualActivity::class.java))
            }

            settingsPrivacyPolicyHolder.setOnClickListener {
                startActivity(Intent(this@SettingsActivity, PrivacyPolicyActivity::class.java))
            }

            settingsAboutHolder.setOnClickListener {
                AboutDialog(this@SettingsActivity, "1.0.0")
            }
        }
    }

    private fun setupCustomizeColors() {
        binding.apply {
            settingsColorCustomizationHolder.setOnClickListener {
                startCustomizationActivity()
            }
        }
    }

    private fun setupKeyboardTheme() {
        binding.apply {
            settingsKeyboardThemeValue.text = when {
                config.keyboardThemeId == KEYBOARD_THEME_CUSTOM_PHOTO -> getString(R.string.my_photo)
                else -> selectedKeyboardTheme()?.let { getString(it.nameResId) } ?: getString(R.string.theme_default)
            }
            settingsKeyboardThemeHolder.setOnClickListener {
                Intent(this@SettingsActivity, KeyboardThemePickerActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
    }

    private fun setupUseEnglish() {
        binding.apply {
            settingsUseEnglishHolder.beVisibleIf((config.wasUseEnglishToggled || Locale.getDefault().language != "en") && !isTiramisuPlus())
            settingsUseEnglish.isChecked = config.useEnglish
            settingsUseEnglishHolder.setOnClickListener {
                settingsUseEnglish.toggle()
                config.useEnglish = settingsUseEnglish.isChecked
                exitProcess(0)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setupLanguage() {
        binding.apply {
            settingsLanguage.text = Locale.getDefault().displayLanguage
            settingsLanguageHolder.beVisibleIf(isTiramisuPlus())
            settingsLanguageHolder.setOnClickListener {
                launchChangeAppLanguageIntent()
            }
        }
    }

    /*
    private fun setupManageClipboardItems() {
        binding.settingsManageClipboardItemsHolder.setOnClickListener {
            Intent(this, ManageClipboardItemsActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    private fun setupEnableShortcuts() {
        binding.apply {
            settingsEnableShortcuts.isChecked = config.enableShortcuts
            settingsEnableShortcutsHolder.setOnClickListener {
                settingsEnableShortcuts.toggle()
                config.enableShortcuts = settingsEnableShortcuts.isChecked
            }
        }
    }

    private fun setupSpaceSwipeCursorControl() {
        binding.apply {
            settingsSpaceSwipeCursorControl.isChecked = config.spaceSwipeCursorControl
            settingsSpaceSwipeCursorControlHolder.setOnClickListener {
                settingsSpaceSwipeCursorControl.toggle()
                config.spaceSwipeCursorControl = settingsSpaceSwipeCursorControl.isChecked
            }
        }
    }

    private fun setupSwipeDeleteWord() {
        binding.apply {
            settingsSwipeDeleteWord.isChecked = config.swipeDeleteWord
            settingsSwipeDeleteWordHolder.setOnClickListener {
                settingsSwipeDeleteWord.toggle()
                config.swipeDeleteWord = settingsSwipeDeleteWord.isChecked
            }
        }
    }

    private fun setupManageShortcuts() {
        binding.settingsManageShortcutsHolder.setOnClickListener {
            Intent(this, ManageShortcutsActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
    */

    private fun setupVibrateOnKeypress() {
        binding.apply {
            settingsVibrateOnKeypress.isChecked = config.vibrateOnKeypress
            settingsVibrateOnKeypressHolder.setOnClickListener {
                settingsVibrateOnKeypress.toggle()
                config.vibrateOnKeypress = settingsVibrateOnKeypress.isChecked
            }
        }
    }

    private fun setupSoundOnKeypress() {
        binding.apply {
            settingsSoundOnKeypress.text = getSoundOnKeypressText(config.soundOnKeypress)
            settingsSoundOnKeypressHolder.setOnClickListener {
                val items = arrayListOf(
                    RadioItem(SOUND_NONE, getString(R.string.sound_none)),
                    RadioItem(SOUND_SYSTEM, getString(R.string.sound_system)),
                    RadioItem(SOUND_ALWAYS, getString(R.string.sound_always))
                )
                RadioGroupDialog(
                    activity = this@SettingsActivity,
                    items = items,
                    checkedItemId = config.soundOnKeypress
                ) {
                    config.soundOnKeypress = it as Int
                    settingsSoundOnKeypress.text = getSoundOnKeypressText(config.soundOnKeypress)
                }
            }
        }
    }

    private fun getSoundOnKeypressText(mode: Int): String = getString(
        when (mode) {
            SOUND_SYSTEM -> R.string.sound_system
            SOUND_ALWAYS -> R.string.sound_always
            else -> R.string.sound_none
        }
    )

    /*
    private fun setupVibrationStrength() {
        binding.apply {
            settingsVibrationStrength.text = getVibrationStrengthText(config.vibrationStrength)
            settingsVibrationStrengthHolder.setOnClickListener {
                val items = arrayListOf(
                    RadioItem(VIBRATION_SYSTEM, getString(R.string.vibration_default)),
                    RadioItem(VIBRATION_LIGHT, getString(R.string.vibration_light)),
                    RadioItem(VIBRATION_MEDIUM, getString(R.string.vibration_medium)),
                    RadioItem(VIBRATION_STRONG, getString(R.string.vibration_strong))
                )
                RadioGroupDialog(this@SettingsActivity, items, config.vibrationStrength) {
                    config.vibrationStrength = it as Int
                    settingsVibrationStrength.text = getVibrationStrengthText(config.vibrationStrength)
                }
            }
        }
    }
    */

    private fun getVibrationStrengthText(strength: Int): String = getString(
        when (strength) {
            VIBRATION_LIGHT -> R.string.vibration_light
            VIBRATION_MEDIUM -> R.string.vibration_medium
            VIBRATION_STRONG -> R.string.vibration_strong
            else -> R.string.vibration_default
        }
    )

    /*
    private fun setupSoundVolume() {
        binding.apply {
            settingsSoundVolume.text = getSoundVolumeText(config.keypressSoundVolume)
            settingsSoundVolumeHolder.setOnClickListener {
                val items = arrayListOf(
                    RadioItem(SOUND_VOLUME_LOW, getString(R.string.volume_low)),
                    RadioItem(SOUND_VOLUME_MEDIUM, getString(R.string.volume_medium)),
                    RadioItem(SOUND_VOLUME_HIGH, getString(R.string.volume_high))
                )
                RadioGroupDialog(this@SettingsActivity, items, config.keypressSoundVolume) {
                    config.keypressSoundVolume = it as Int
                    settingsSoundVolume.text = getSoundVolumeText(config.keypressSoundVolume)
                }
            }
        }
    }
    */

    private fun getSoundVolumeText(volume: Int): String = getString(
        when {
            volume <= SOUND_VOLUME_LOW -> R.string.volume_low
            volume <= SOUND_VOLUME_MEDIUM -> R.string.volume_medium
            else -> R.string.volume_high
        }
    )

    /*
    private fun setupOneHandedMode() {
        binding.apply {
            settingsOneHandedMode.text = getOneHandedModeText(config.oneHandedMode)
            settingsOneHandedModeHolder.setOnClickListener {
                val items = arrayListOf(
                    RadioItem(ONE_HANDED_OFF, getString(R.string.one_handed_off)),
                    RadioItem(ONE_HANDED_LEFT, getString(R.string.one_handed_left)),
                    RadioItem(ONE_HANDED_RIGHT, getString(R.string.one_handed_right))
                )
                RadioGroupDialog(this@SettingsActivity, items, config.oneHandedMode) {
                    config.oneHandedMode = it as Int
                    settingsOneHandedMode.text = getOneHandedModeText(config.oneHandedMode)
                }
            }
        }
    }
    */

    private fun getOneHandedModeText(mode: Int): String = getString(
        when (mode) {
            ONE_HANDED_LEFT -> R.string.one_handed_left
            ONE_HANDED_RIGHT -> R.string.one_handed_right
            else -> R.string.one_handed_off
        }
    )

    private fun setupShowPopupOnKeypress() {
        binding.apply {
            settingsShowPopupOnKeypress.isChecked = config.showPopupOnKeypress
            settingsShowPopupOnKeypressHolder.setOnClickListener {
                settingsShowPopupOnKeypress.toggle()
                config.showPopupOnKeypress = settingsShowPopupOnKeypress.isChecked
            }
        }
    }

    private fun setupShowKeyBorders() {
        binding.apply {
            settingsShowKeyBorders.isChecked = config.showKeyBorders
            settingsShowKeyBordersHolder.setOnClickListener {
                settingsShowKeyBorders.toggle()
                config.showKeyBorders = settingsShowKeyBorders.isChecked
            }
        }
    }

    private fun setupManageKeyboardLanguages() {
        binding.apply {
            settingsManageKeyboardLanguagesHolder.setOnClickListener {
                ManageKeyboardLanguagesDialog(this@SettingsActivity) {
                    settingsKeyboardLanguage.text = getKeyboardLanguageText(config.keyboardLanguage)
                }
            }
        }
    }

    private fun setupKeyboardLanguage() {
        binding.apply {
            settingsKeyboardLanguage.text = getKeyboardLanguageText(config.keyboardLanguage)
            settingsKeyboardLanguageHolder.setOnClickListener {
                val items = getKeyboardLanguagesRadioItems()
                RadioGroupDialog(this@SettingsActivity, items, config.keyboardLanguage) {
                    config.keyboardLanguage = it as Int
                    settingsKeyboardLanguage.text = getKeyboardLanguageText(config.keyboardLanguage)
                }
            }
        }
    }

    private fun setupKeyboardHeightMultiplier() {
        binding.apply {
            settingsKeyboardHeightMultiplier.text =
                getKeyboardHeightPercentageText(config.keyboardHeightPercentage)
            settingsKeyboardHeightMultiplierHolder.setOnClickListener {
                val items = arrayListOf(
                    RadioItem(
                        id = KEYBOARD_HEIGHT_70_PERCENT,
                        title = getKeyboardHeightPercentageText(KEYBOARD_HEIGHT_70_PERCENT)
                    ),
                    RadioItem(
                        id = KEYBOARD_HEIGHT_80_PERCENT,
                        title = getKeyboardHeightPercentageText(KEYBOARD_HEIGHT_80_PERCENT)
                    ),
                    RadioItem(
                        id = KEYBOARD_HEIGHT_90_PERCENT,
                        title = getKeyboardHeightPercentageText(KEYBOARD_HEIGHT_90_PERCENT)
                    ),
                    RadioItem(
                        id = KEYBOARD_HEIGHT_100_PERCENT,
                        title = getKeyboardHeightPercentageText(KEYBOARD_HEIGHT_100_PERCENT)
                    ),
                    RadioItem(
                        id = KEYBOARD_HEIGHT_120_PERCENT,
                        title = getKeyboardHeightPercentageText(KEYBOARD_HEIGHT_120_PERCENT)
                    ),
                    RadioItem(
                        id = KEYBOARD_HEIGHT_140_PERCENT,
                        title = getKeyboardHeightPercentageText(KEYBOARD_HEIGHT_140_PERCENT)
                    ),
                    RadioItem(
                        id = KEYBOARD_HEIGHT_160_PERCENT,
                        title = getKeyboardHeightPercentageText(KEYBOARD_HEIGHT_160_PERCENT)
                    ),
                )

                RadioGroupDialog(this@SettingsActivity, items, config.keyboardHeightPercentage) {
                    config.keyboardHeightPercentage = it as Int
                    settingsKeyboardHeightMultiplier.text =
                        getKeyboardHeightPercentageText(config.keyboardHeightPercentage)
                }
            }
        }
    }

    private fun getKeyboardHeightPercentageText(keyboardHeightPercentage: Int): String =
        "$keyboardHeightPercentage%"

    /*
    private fun setupShowClipboardContent() {
        binding.apply {
            settingsShowClipboardContent.isChecked = config.showClipboardContent
            settingsShowClipboardContentHolder.setOnClickListener {
                settingsShowClipboardContent.toggle()
                config.showClipboardContent = settingsShowClipboardContent.isChecked
            }

            settingsClipboardHistory.isChecked = config.clipboardHistoryEnabled
            settingsClipboardHistoryHolder.setOnClickListener {
                settingsClipboardHistory.toggle()
                config.clipboardHistoryEnabled = settingsClipboardHistory.isChecked
            }

            settingsShowSuggestions.isChecked = config.showSuggestions
            settingsShowSuggestionsHolder.setOnClickListener {
                settingsShowSuggestions.toggle()
                config.showSuggestions = settingsShowSuggestions.isChecked
            }
        }
    }

    private fun setupSentencesCapitalization() {
        binding.apply {
            settingsStartSentencesCapitalized.isChecked = config.enableSentencesCapitalization
            settingsStartSentencesCapitalizedHolder.setOnClickListener {
                settingsStartSentencesCapitalized.toggle()
                config.enableSentencesCapitalization = settingsStartSentencesCapitalized.isChecked
            }
        }
    }
    */

    private fun setupAutoPunctuation() {
        binding.apply {
            settingsAutoPunctuation.isChecked = config.autoPunctuation
            settingsAutoPunctuationHolder.setOnClickListener {
                settingsAutoPunctuation.toggle()
                config.autoPunctuation = settingsAutoPunctuation.isChecked
            }
        }
    }

    /*
    private fun setupShowEmojiKey() {
        binding.apply {
            settingsShowEmojiKeyHolder.setOnClickListener {
                settingsShowEmojiKey.toggle()
                config.showEmojiKey = settingsShowEmojiKey.isChecked
                if (settingsShowEmojiKey.isChecked) {
                    config.showLanguageSwitchKey = false
                    settingsShowLanguageSwitchKey.isChecked = false
                }
            }
            settingsShowEmojiKey.isChecked = config.showEmojiKey
        }
    }

    private fun setupShowLanguageSwitchKey() {
        binding.apply {
            settingsShowLanguageSwitchKeyHolder.setOnClickListener {
                settingsShowLanguageSwitchKey.toggle()
                config.showLanguageSwitchKey = settingsShowLanguageSwitchKey.isChecked
                if (settingsShowLanguageSwitchKey.isChecked) {
                    config.showEmojiKey = false
                    settingsShowEmojiKey.isChecked = false
                }
            }
            settingsShowLanguageSwitchKey.isChecked = config.showLanguageSwitchKey
        }
    }
    */

    private fun setupShowNumbersRow() {
        binding.apply {
            settingsShowNumbersRow.isChecked = config.showNumbersRow
            settingsShowNumbersRowHolder.setOnClickListener {
                settingsShowNumbersRow.toggle()
                config.showNumbersRow = settingsShowNumbersRow.isChecked
            }
        }
    }

    /*
    private fun setupVoiceInputMethod() {
        binding.apply {
            settingsVoiceInputMethodValue.text =
                getCurrentVoiceInputMethod()?.first?.loadLabel(packageManager)
                    ?: getString(R.string.none)
            settingsVoiceInputMethodHolder.setOnClickListener {
                val inputMethods = getVoiceInputMethods()
                if (inputMethods.isEmpty()) {
                    toast(R.string.no_app_found)
                    return@setOnClickListener
                }

                RadioGroupDialog(
                    activity = this@SettingsActivity,
                    items = getVoiceInputRadioItems(),
                    checkedItemId = inputMethods.indexOf(getCurrentVoiceInputMethod(inputMethods))
                ) {
                    config.voiceInputMethod = inputMethods.getOrNull(it as Int)?.first?.id.orEmpty()
                    settingsVoiceInputMethodValue.text =
                        getCurrentVoiceInputMethod(inputMethods)?.first?.loadLabel(packageManager)
                            ?: getString(R.string.none)
                }
            }
        }
    }
    */

    private fun setupContinuousVoiceTyping() {
        binding.apply {
            settingsContinuousVoiceTyping.isChecked = config.continuousVoiceTyping
            settingsContinuousVoiceTypingHolder.setOnClickListener {
                settingsContinuousVoiceTyping.toggle()
                config.continuousVoiceTyping = settingsContinuousVoiceTyping.isChecked
            }
        }
    }

    private fun setupVoiceTypingPunctuation() {
        binding.apply {
            settingsVoiceTypingPunctuation.isChecked = config.voiceTypingPunctuation
            settingsVoiceTypingPunctuationHolder.setOnClickListener {
                settingsVoiceTypingPunctuation.toggle()
                config.voiceTypingPunctuation = settingsVoiceTypingPunctuation.isChecked
            }
        }
    }
}
