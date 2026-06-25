package com.itamadersomajinc.banglatype.activities

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.itamadersomajinc.banglatype.R
import com.itamadersomajinc.banglatype.commons.extensions.toast

/**
 * Tiny transparent activity used by the keyboard service to request the RECORD_AUDIO runtime
 * permission, which an InputMethodService cannot request on its own. Finishes immediately.
 */
class VoicePermissionActivity : ComponentActivity() {

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                toast(R.string.voice_permission_granted)
            } else {
                toast(R.string.mic_permission_needed)
            }
            finish()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission.launch(Manifest.permission.RECORD_AUDIO)
    }
}
