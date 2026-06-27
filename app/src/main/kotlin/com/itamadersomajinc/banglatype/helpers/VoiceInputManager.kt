package com.itamadersomajinc.banglatype.helpers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

/**
 * Thin wrapper around [SpeechRecognizer] that performs voice typing entirely inside the app.
 * The owner (the IME) receives partial/final transcripts via [Callbacks] and commits them itself.
 * All methods must be called on the main thread.
 */
class VoiceInputManager(private val context: Context) {

    interface Callbacks {
        fun onReadyForSpeech() {}
        fun onPartialResult(text: String) {}
        fun onFinalResult(text: String, isEndOfSession: Boolean) {}
        fun onRmsChanged(rmsdB: Float) {}
        fun onError(errorCode: Int) {}
        fun onEndOfSpeech() {}
    }

    private var recognizer: SpeechRecognizer? = null
    var isListening = false
        private set
    private var callbacks: Callbacks? = null
    private var currentLanguageTag: String = "en-US"
    private var isContinuous: Boolean = false

    fun isAvailable() = SpeechRecognizer.isRecognitionAvailable(context)

    fun start(languageTag: String, continuous: Boolean = false, callbacks: Callbacks) {
        if (!isAvailable()) {
            callbacks.onError(SpeechRecognizer.ERROR_RECOGNIZER_BUSY)
            return
        }
        this.callbacks = callbacks
        this.currentLanguageTag = languageTag
        this.isContinuous = continuous
        stopInternal()
        startListeningInternal()
    }

    private fun startListeningInternal() {
        recognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(listener)
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, currentLanguageTag)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, currentLanguageTag)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }

        isListening = true
        recognizer?.startListening(intent)
    }

    fun stop() {
        isContinuous = false
        if (isListening) {
            recognizer?.stopListening()
        }
    }

    fun destroy() {
        stopInternal()
        callbacks = null
    }

    private fun stopInternal() {
        isListening = false
        recognizer?.apply {
            try {
                cancel()
                destroy()
            } catch (ignored: Exception) {
            }
        }
        recognizer = null
    }

    private val listener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            callbacks?.onReadyForSpeech()
        }

        override fun onBeginningOfSpeech() {}

        override fun onRmsChanged(rmsdB: Float) {
            callbacks?.onRmsChanged(rmsdB)
        }

        override fun onBufferReceived(buffer: ByteArray?) {}

        override fun onEndOfSpeech() {
            callbacks?.onEndOfSpeech()
        }

        override fun onError(error: Int) {
            if (isContinuous && (error == SpeechRecognizer.ERROR_NO_MATCH || error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT)) {
                startListeningInternal()
            } else {
                isListening = false
                callbacks?.onError(error)
            }
        }

        override fun onResults(results: Bundle?) {
            val text = results
                ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ?.firstOrNull()
                .orEmpty()

            val isEndOfSession = !isContinuous
            callbacks?.onFinalResult(text, isEndOfSession)

            if (isContinuous) {
                startListeningInternal()
            } else {
                isListening = false
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {
            val text = partialResults
                ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ?.firstOrNull()
                .orEmpty()
            if (text.isNotEmpty()) {
                callbacks?.onPartialResult(text)
            }
        }

        override fun onEvent(eventType: Int, params: Bundle?) {}
    }
}
