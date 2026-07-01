package com.itamadersomajinc.banglatype.extensions

import android.app.KeyguardManager
import android.content.ClipboardManager
import android.content.Context
import android.inputmethodservice.InputMethodService
import android.os.IBinder
import android.os.UserManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodInfo
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodSubtype
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.itamadersomajinc.banglatype.commons.databinding.DialogTitleBinding
import com.itamadersomajinc.banglatype.commons.extensions.baseConfig
import com.itamadersomajinc.banglatype.commons.extensions.getColoredDrawableWithColor
import com.itamadersomajinc.banglatype.commons.extensions.getProperBackgroundColor
import com.itamadersomajinc.banglatype.commons.extensions.getProperPrimaryColor
import com.itamadersomajinc.banglatype.commons.extensions.getProperTextColor
import com.itamadersomajinc.banglatype.commons.extensions.isBlackAndWhiteTheme
import com.itamadersomajinc.banglatype.commons.extensions.isDynamicTheme
import com.itamadersomajinc.banglatype.commons.extensions.updateTextColors
import com.itamadersomajinc.banglatype.commons.helpers.isNougatPlus
import com.itamadersomajinc.banglatype.commons.models.RadioItem
import com.itamadersomajinc.banglatype.commons.views.MyTextView
import com.itamadersomajinc.banglatype.commons.views.MyEditText
import com.itamadersomajinc.banglatype.R
import com.itamadersomajinc.banglatype.databases.ClipsDatabase
import com.itamadersomajinc.banglatype.databases.PhrasesDatabase
import com.itamadersomajinc.banglatype.databases.PredictionDatabase
import com.itamadersomajinc.banglatype.databases.ShortcutsDatabase
import com.itamadersomajinc.banglatype.helpers.Config
import com.itamadersomajinc.banglatype.helpers.INPUT_METHOD_SUBTYPE_VOICE
import com.itamadersomajinc.banglatype.interfaces.ClipsDao
import com.itamadersomajinc.banglatype.interfaces.PhrasesDao
import com.itamadersomajinc.banglatype.interfaces.PredictionsDao
import com.itamadersomajinc.banglatype.interfaces.ShortcutsDao

val Context.config: Config get() = Config.newInstance(applicationContext.safeStorageContext)

val Context.safeStorageContext: Context
    get() = if (isDeviceInDirectBootMode) {
        createDeviceProtectedStorageContext()
    } else {
        this
    }

val Context.isDeviceInDirectBootMode: Boolean
    get() {
        val userManager = getSystemService(Context.USER_SERVICE) as UserManager
        return isNougatPlus() && !userManager.isUserUnlocked
    }

val Context.isDeviceLocked: Boolean
    get() {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return keyguardManager.isDeviceLocked || keyguardManager.isKeyguardLocked || isDeviceInDirectBootMode
    }

val Context.inputMethodManager: InputMethodManager
    get() = getSystemService(InputMethodService.INPUT_METHOD_SERVICE) as InputMethodManager

val Context.clipsDB: ClipsDao
    get() = ClipsDatabase.getInstance(applicationContext.safeStorageContext).ClipsDao()

val Context.phrasesDB: PhrasesDao
    get() = PhrasesDatabase.getInstance(applicationContext.safeStorageContext).PhrasesDao()

val Context.predictionsDB: PredictionsDao
    get() = PredictionDatabase.getInstance(applicationContext.safeStorageContext).PredictionsDao()

val Context.shortcutsDB: ShortcutsDao
    get() = ShortcutsDatabase.getInstance(applicationContext.safeStorageContext).ShortcutsDao()

fun Context.getCurrentClip(): String? {
    val clipboardManager = (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    return clipboardManager.primaryClip?.getItemAt(0)?.text?.toString()
}

fun Context.getKeyboardDialogBuilder() = if (safeStorageContext.isDynamicTheme()) {
    MaterialAlertDialogBuilder(this, R.style.MyKeyboard_Alert)
} else {
    AlertDialog.Builder(this, R.style.MyKeyboard_Alert)
}

fun Context.setupKeyboardDialogStuff(
    windowToken: IBinder,
    view: View,
    dialog: AlertDialog.Builder,
    titleId: Int = 0,
    titleText: String = "",
    cancelOnTouchOutside: Boolean = true,
    callback: ((alertDialog: AlertDialog) -> Unit)? = null
) {
    val textColor = getProperTextColor()
    val backgroundColor = getProperBackgroundColor()
    val primaryColor = getProperPrimaryColor()
    
    if (view is ViewGroup) {
        updateTextColors(view)
    } else if (view is MyTextView) {
        view.setColors(textColor, primaryColor, backgroundColor)
    } else if (view is MyEditText) {
        view.setColors(textColor, primaryColor, backgroundColor)
    }

    dialog.create().apply {
        if (titleId != 0) {
            setTitle(titleId)
        } else if (titleText.isNotEmpty()) {
            setTitle(titleText)
        }

        val lp = window?.attributes
        lp?.token = windowToken
        lp?.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
        window?.attributes = lp
        window?.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)

        setView(view)
        setCancelable(cancelOnTouchOutside)
        show()

        val bgDrawable = when {
            isBlackAndWhiteTheme() -> ResourcesCompat.getDrawable(
                resources, R.drawable.black_dialog_background, theme
            )

            isDynamicTheme() -> ResourcesCompat.getDrawable(
                resources, R.drawable.dialog_you_background, theme
            )

            else -> resources.getColoredDrawableWithColor(
                drawableId = R.drawable.dialog_bg,
                color = baseConfig.backgroundColor
            )
        }

        window?.setBackgroundDrawable(bgDrawable)
        callback?.invoke(this)
    }
}

fun Context.getVoiceInputMethods(
    imm: InputMethodManager = inputMethodManager
): List<Pair<InputMethodInfo, InputMethodSubtype>> {
    return imm.enabledInputMethodList.flatMap { im ->
        imm.getEnabledInputMethodSubtypeList(im, true)
            .filter { it.mode == INPUT_METHOD_SUBTYPE_VOICE }
            .map { im to it }
    }
}

fun Context.getCurrentVoiceInputMethod(
    inputMethods: List<Pair<InputMethodInfo, InputMethodSubtype>> = getVoiceInputMethods()
) = inputMethods.find { it.first.id == config.voiceInputMethod }

/** Best available voice input method, preferring Google voice typing, else the first available. */
fun Context.getPreferredVoiceInputMethod(
    inputMethods: List<Pair<InputMethodInfo, InputMethodSubtype>> = getVoiceInputMethods()
): Pair<InputMethodInfo, InputMethodSubtype>? {
    return inputMethods.firstOrNull { it.first.packageName.startsWith("com.google.android") }
        ?: inputMethods.firstOrNull()
}

/** Resolves the configured voice method, falling back to (and remembering) the preferred one. */
fun Context.getOrAutoSelectVoiceInputMethod(): Pair<InputMethodInfo, InputMethodSubtype>? {
    val inputMethods = getVoiceInputMethods()
    val current = getCurrentVoiceInputMethod(inputMethods)
    if (current != null) {
        return current
    }
    val preferred = getPreferredVoiceInputMethod(inputMethods) ?: return null
    config.voiceInputMethod = preferred.first.id
    return preferred
}

fun Context.getVoiceInputRadioItems(
    inputMethods: List<Pair<InputMethodInfo, InputMethodSubtype>> = getVoiceInputMethods()
): ArrayList<RadioItem> {
    val radioItems = arrayListOf(RadioItem(id = -1, title = getString(R.string.none)))
    for ((index, pair) in inputMethods.withIndex()) {
        radioItems += RadioItem(id = index, title = pair.first.loadLabel(packageManager).toString())
    }

    return radioItems
}
