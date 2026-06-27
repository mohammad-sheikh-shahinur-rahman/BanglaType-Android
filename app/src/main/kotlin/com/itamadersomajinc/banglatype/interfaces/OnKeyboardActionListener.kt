package com.itamadersomajinc.banglatype.interfaces

import android.view.inputmethod.InputMethodSubtype

/**
 * The SimpleKeyboardIME class uses this interface to communicate with the input connection
 */
interface OnKeyboardActionListener {
    /**
     * Called when the user presses a key. This is sent before the [.onKey] is called. For keys that repeat, this is only called once.
     * @param primaryCode the unicode of the key being pressed. If the touch is not on a valid key, the value will be zero.
     */
    fun onPress(primaryCode: Int)

    /**
     * Send a key press to the listener.
     * @param code this is the key that was pressed
     */
    fun onKey(code: Int)

    /**
     * Called when the finger has been lifted after pressing a key
     */
    fun onActionUp()

    /**
     * Called when the user long presses Space and moves to the left
     */
    fun moveCursorLeft()

    /**
     * Called when the user long presses Space and moves to the right
     */
    fun moveCursorRight()

    /**
     * Called when the user swipes left from the Backspace key to delete the previous word.
     */
    fun onDeleteWord()

    /**
     * Called from the text-editing panel to move the cursor with a D-pad key.
     * @param keyCode one of KeyEvent.KEYCODE_DPAD_LEFT/RIGHT/UP/DOWN
     * @param withShift true to extend the selection (Select mode) instead of just moving
     */
    fun onEditCursorMove(keyCode: Int, withShift: Boolean)

    /**
     * Called from the text-editing panel to run a context-menu action on the field.
     * @param menuActionId one of android.R.id.selectAll / cut / copy / paste
     */
    fun onEditContextAction(menuActionId: Int)

    /**
     * Sends a sequence of characters to the listener.
     * @param text the string to be displayed.
     */
    fun onText(text: String)

    /**
     * Called to force the KeyboardView to reload the keyboard
     */
    fun reloadKeyboard()

    /**
     * Called when input method is changed in-app.
     */
    fun changeInputMethod(id: String, subtype: InputMethodSubtype)

    /**
     * Called when the user taps a word-prediction suggestion in the toolbar.
     */
    fun onPredictionPicked(word: String)

    /**
     * Called when the user swipes horizontally across the spacebar to change language.
     * @param forward true to move to the next language, false for the previous one.
     */
    fun onSpaceSwipeLanguage(forward: Boolean)

    /**
     * Called when the user taps the speech-bar mic to start in-app voice typing.
     */
    fun onStartVoiceInput()

    /**
     * Called to stop an in-progress in-app voice typing session.
     */
    fun onStopVoiceInput()

    /**
     * Called from the one-handed side controls to switch the active side (left ↔ right) or to
     * leave one-handed mode entirely.
     * @param mode one of ONE_HANDED_OFF / ONE_HANDED_LEFT / ONE_HANDED_RIGHT
     */
    fun onOneHandedModeChanged(mode: Int)
}
