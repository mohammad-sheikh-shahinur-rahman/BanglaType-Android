package com.itamadersomajinc.banglatype.activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.itamadersomajinc.banglatype.commons.extensions.toast
import com.itamadersomajinc.banglatype.commons.extensions.viewBinding
import com.itamadersomajinc.banglatype.commons.helpers.NavigationIcon
import com.itamadersomajinc.banglatype.commons.helpers.ensureBackgroundThread
import com.itamadersomajinc.banglatype.R
import com.itamadersomajinc.banglatype.adapters.KeyboardThemesAdapter
import com.itamadersomajinc.banglatype.databinding.ActivityKeyboardThemePickerBinding
import com.itamadersomajinc.banglatype.extensions.applyKeyboardTheme
import com.itamadersomajinc.banglatype.extensions.config
import com.itamadersomajinc.banglatype.extensions.keyboardBackgroundImageFile
import com.itamadersomajinc.banglatype.extensions.safeStorageContext
import com.itamadersomajinc.banglatype.helpers.KEYBOARD_THEME_CUSTOM_PHOTO
import com.itamadersomajinc.banglatype.commons.extensions.baseConfig
import java.io.FileOutputStream

class KeyboardThemePickerActivity : SimpleActivity() {
    private val binding by viewBinding(ActivityKeyboardThemePickerBinding::inflate)

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                importBackgroundImage(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupEdgeToEdge(padBottomSystem = listOf(binding.themePickerHolder))

        binding.themesGrid.apply {
            layoutManager = GridLayoutManager(this@KeyboardThemePickerActivity, 3)
            adapter = KeyboardThemesAdapter(
                context = this@KeyboardThemePickerActivity,
                onPhotoTileClick = { pickImage.launch("image/*") },
                onPresetClick = { theme ->
                    applyKeyboardTheme(theme)
                    refresh()
                    toast(R.string.theme_applied)
                }
            )
        }

        binding.brightnessSlider.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                config.keyboardBackgroundDim = value.toInt()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupTopAppBar(binding.themePickerAppbar, NavigationIcon.Arrow)
        refresh()
    }

    private fun refresh() {
        binding.themesGrid.adapter?.notifyDataSetChanged()
        val isPhoto = config.keyboardThemeId == KEYBOARD_THEME_CUSTOM_PHOTO &&
            config.keyboardBackgroundImagePath.isNotEmpty()
        binding.brightnessHolder.visibility = if (isPhoto) android.view.View.VISIBLE else android.view.View.GONE
        if (isPhoto) {
            binding.brightnessSlider.value = config.keyboardBackgroundDim.coerceIn(0, 100).toFloat()
        }
    }

    // Copies the picked image into device-protected storage (so the IME can read it while locked),
    // downscaled to a reasonable size, then activates the custom-photo theme.
    private fun importBackgroundImage(uri: Uri) {
        ensureBackgroundThread {
            val bitmap = decodeDownscaled(uri)
            val success = if (bitmap != null) {
                try {
                    val file = safeStorageContext.keyboardBackgroundImageFile()
                    FileOutputStream(file).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                    }
                    true
                } catch (e: Exception) {
                    false
                } finally {
                    bitmap.recycle()
                }
            } else {
                false
            }

            runOnUiThread {
                if (success) {
                    safeStorageContext.baseConfig.isSystemThemeEnabled = false
                    config.keyboardBackgroundImagePath = safeStorageContext.keyboardBackgroundImageFile().absolutePath
                    config.keyboardThemeId = KEYBOARD_THEME_CUSTOM_PHOTO
                    refresh()
                    toast(R.string.theme_applied)
                } else {
                    toast(R.string.failed_to_load_image)
                }
            }
        }
    }

    private fun decodeDownscaled(uri: Uri): Bitmap? {
        return try {
            val maxSize = 1280
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
            if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

            var sample = 1
            while (bounds.outWidth / (sample * 2) >= maxSize || bounds.outHeight / (sample * 2) >= maxSize) {
                sample *= 2
            }
            val opts = BitmapFactory.Options().apply { inSampleSize = sample }
            contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, opts) }
        } catch (e: Exception) {
            null
        }
    }
}
