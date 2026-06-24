package com.itamadersomajinc.banglatype.helpers

import android.content.Context
import android.media.AudioManager
import android.view.HapticFeedbackConstants
import android.view.View
import com.itamadersomajinc.banglatype.commons.extensions.performHapticFeedback
import com.itamadersomajinc.banglatype.commons.helpers.isOreoMr1Plus
import com.itamadersomajinc.banglatype.extensions.config
import com.itamadersomajinc.banglatype.extensions.safeStorageContext

/**
 * Helper for keypress haptics and audio.
 */
class KeyboardFeedbackManager(private val context: Context) {

    private val config: Config
        get() = context.safeStorageContext.config

    private val audioManager by lazy {
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    /**
     * Perform haptic feedback for standard keypress.
     */
    fun vibrateIfNeeded(view: View) {
        if (config.vibrateOnKeypress) view.performHapticFeedback()
    }

    /**
     * Perform haptic feedback for cursor handle movement.
     */
    fun performHapticHandleMove(view: View) {
        if (!config.vibrateOnKeypress) return
        if (isOreoMr1Plus()) {
            @Suppress("DEPRECATION")
            view.performHapticFeedback(
                HapticFeedbackConstants.TEXT_HANDLE_MOVE,
                HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            )
        }
    }

    /**
     * Play keypress sound if enabled.
     */
    fun playKeypressSoundIfNeeded(code: Int) {
        val soundMode = config.soundOnKeypress
        if (soundMode == SOUND_NONE) return

        val effect = when (code) {
            MyKeyboard.KEYCODE_DELETE -> AudioManager.FX_KEYPRESS_DELETE
            MyKeyboard.KEYCODE_ENTER -> AudioManager.FX_KEYPRESS_RETURN
            MyKeyboard.KEYCODE_SPACE -> AudioManager.FX_KEYPRESS_SPACEBAR
            else -> AudioManager.FX_KEYPRESS_STANDARD
        }

        when (soundMode) {
            SOUND_SYSTEM -> audioManager.playSoundEffect(effect)
            SOUND_ALWAYS -> audioManager.playSoundEffect(effect, 1.0f)
        }
    }

    /**
     * Perform both haptic and audio feedback for a keypress.
     */
    fun performKeypressFeedback(view: View, keyCode: Int) {
        vibrateIfNeeded(view)
        playKeypressSoundIfNeeded(keyCode)
    }
}
