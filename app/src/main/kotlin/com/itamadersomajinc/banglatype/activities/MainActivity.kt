package com.itamadersomajinc.banglatype.activities

import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.provider.Settings
import com.itamadersomajinc.banglatype.commons.dialogs.ConfirmationAdvancedDialog
import com.itamadersomajinc.banglatype.commons.extensions.applyColorFilter
import com.itamadersomajinc.banglatype.commons.extensions.getContrastColor
import com.itamadersomajinc.banglatype.commons.extensions.getProperPrimaryColor
import com.itamadersomajinc.banglatype.commons.extensions.hideKeyboard
import com.itamadersomajinc.banglatype.commons.extensions.updateTextColors
import com.itamadersomajinc.banglatype.commons.extensions.viewBinding
import com.itamadersomajinc.banglatype.commons.extensions.launchViewIntent
import com.itamadersomajinc.banglatype.BuildConfig
import com.itamadersomajinc.banglatype.R
import com.itamadersomajinc.banglatype.databinding.ActivityMainBinding
import com.itamadersomajinc.banglatype.dialogs.AboutDialog
import com.itamadersomajinc.banglatype.extensions.inputMethodManager

class MainActivity : SimpleActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupOptionsMenu()

        binding.apply {
            setupEdgeToEdge(padBottomSystem = listOf(mainNestedScrollview))
            setupMaterialScrollListener(binding.mainNestedScrollview, binding.mainAppbar)

            changeKeyboardHolder.setOnClickListener {
                inputMethodManager.showInputMethodPicker()
            }

            settingsHolder.setOnClickListener {
                launchSettings()
            }

            facebookLink.setOnClickListener {
                launchViewIntent(R.string.about_facebook_url)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupTopAppBar(binding.mainAppbar)
        if (!isKeyboardEnabled()) {
            ConfirmationAdvancedDialog(
                activity = this,
                messageId = R.string.redirection_note,
                positive = R.string.ok,
                negative = 0
            ) { success ->
                if (success) {
                    Intent(Settings.ACTION_INPUT_METHOD_SETTINGS).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(this)
                    }
                } else {
                    finish()
                }
            }
        }

        updateTextColors(binding.mainNestedScrollview)
        updateChangeKeyboardColor()
    }

    private fun setupOptionsMenu() {
        binding.mainToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.settings -> launchSettings()
                R.id.about -> launchAbout()
                else -> return@setOnMenuItemClickListener false
            }
            return@setOnMenuItemClickListener true
        }
    }


    private fun launchSettings() {
        hideKeyboard()
        startActivity(Intent(applicationContext, SettingsActivity::class.java))
    }

    private fun launchAbout() {
        AboutDialog(this, BuildConfig.VERSION_NAME)
    }

    private fun updateChangeKeyboardColor() {
        val applyBackground =
            resources.getDrawable(R.drawable.button_background_rounded, theme) as RippleDrawable
        (applyBackground as LayerDrawable).findDrawableByLayerId(R.id.button_background_holder)
            .applyColorFilter(getProperPrimaryColor())

        binding.changeKeyboard.apply {
            background = applyBackground
            setTextColor(getProperPrimaryColor().getContrastColor())
        }

        val facebookBackground =
            resources.getDrawable(R.drawable.button_background_rounded, theme) as RippleDrawable
        (facebookBackground as LayerDrawable).findDrawableByLayerId(R.id.button_background_holder)
            .applyColorFilter(getProperPrimaryColor())

        binding.facebookLink.apply {
            background = facebookBackground
            setTextColor(getProperPrimaryColor().getContrastColor())
        }
    }

    private fun isKeyboardEnabled(): Boolean {
        return inputMethodManager.enabledInputMethodList.any {
            it.settingsActivity == SettingsActivity::class.java.canonicalName
        }
    }
}
