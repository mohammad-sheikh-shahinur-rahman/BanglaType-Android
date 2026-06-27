package com.itamadersomajinc.banglatype.activities

import android.content.Intent
import android.content.res.ColorStateList
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
            setupMaterialScrollListener(mainNestedScrollview, mainAppbar)

            changeKeyboardHolder.setOnClickListener {
                inputMethodManager.showInputMethodPicker()
            }

            settingsHolder.setOnClickListener {
                launchSettings()
            }

            manualHolder.setOnClickListener {
                startActivity(Intent(this@MainActivity, ManualActivity::class.java))
            }

            shareHolder.setOnClickListener {
                shareApp()
            }

            aboutHolder.setOnClickListener {
                launchAbout()
            }
        }
    }

    private fun shareApp() {
        val shareText = getString(R.string.share_app_text, BuildConfig.BASE_APPLICATION_ID)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(intent, getString(R.string.share_with_friends)))
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
        applyHeroColors()
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
        AboutDialog(this, "1.0.0")
    }

    private fun updateChangeKeyboardColor() {
        val primaryColor = getProperPrimaryColor()
        val contrastColor = primaryColor.getContrastColor()
        val isEnabled = isKeyboardEnabled()

        binding.changeKeyboardCard.setCardBackgroundColor(if (isEnabled) primaryColor else 0xFFFFC107.toInt()) // Amber for not enabled
        binding.changeKeyboard.setTextColor(contrastColor)
        binding.changeKeyboardIcon.applyColorFilter(contrastColor)
        
        binding.changeKeyboard.text = if (isEnabled) getString(R.string.change_keyboard) else "কিবোর্ড চালু করুন (Enable Keyboard)"
        binding.changeKeyboardIcon.setImageResource(if (isEnabled) R.drawable.ic_check_vector else R.drawable.ic_language_outlined)
    }

    private fun applyHeroColors() {
        val primaryColor = getProperPrimaryColor()
        val contrastColor = primaryColor.getContrastColor()
        val isEnabled = isKeyboardEnabled()
        
        binding.heroCard.setCardBackgroundColor(primaryColor)
        listOf(binding.heroTitle, binding.heroTagline, binding.heroDescription)
            .forEach { it.setTextColor(contrastColor) }

        if (isEnabled) {
            binding.heroTagline.text = "BanglaType is active and ready!"
        }

        listOf(
            binding.settingsIcon,
            binding.manualIcon,
            binding.shareIcon,
            binding.aboutIcon
        ).forEach { it.applyColorFilter(primaryColor) }
    }

    private fun isKeyboardEnabled(): Boolean {
        return inputMethodManager.enabledInputMethodList.any {
            it.settingsActivity == SettingsActivity::class.java.canonicalName
        }
    }
}
