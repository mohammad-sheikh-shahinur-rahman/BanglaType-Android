package com.itamadersomajinc.banglatype.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itamadersomajinc.banglatype.commons.extensions.applyColorFilter
import com.itamadersomajinc.banglatype.commons.extensions.getProperPrimaryColor
import com.itamadersomajinc.banglatype.commons.extensions.getProperTextColor
import com.itamadersomajinc.banglatype.databinding.ItemKeyboardThemeBinding
import com.itamadersomajinc.banglatype.extensions.config
import com.itamadersomajinc.banglatype.extensions.keyboardBackgroundImageFile
import com.itamadersomajinc.banglatype.helpers.KEYBOARD_THEME_CUSTOM_PHOTO
import com.itamadersomajinc.banglatype.helpers.KeyboardTheme
import com.itamadersomajinc.banglatype.helpers.KeyboardThemeType
import com.itamadersomajinc.banglatype.helpers.builtInKeyboardThemes

class KeyboardThemesAdapter(
    private val context: Context,
    private val onPhotoTileClick: () -> Unit,
    private val onPresetClick: (KeyboardTheme) -> Unit
) : RecyclerView.Adapter<KeyboardThemesAdapter.ViewHolder>() {

    private val themes = builtInKeyboardThemes
    private val cornerRadius = context.resources.displayMetrics.density * 12

    class ViewHolder(val binding: ItemKeyboardThemeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKeyboardThemeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = themes.size + 1 // + the "my photo" tile at position 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val textColor = context.getProperTextColor()
        val primaryColor = context.getProperPrimaryColor()
        binding.themeName.setTextColor(textColor)
        binding.themeCheck.applyColorFilter(primaryColor)
        binding.themeAddIcon.applyColorFilter(textColor)

        if (position == 0) {
            bindPhotoTile(holder, primaryColor, textColor)
            return
        }

        val theme = themes[position - 1]
        val selected = context.config.keyboardThemeId == theme.id &&
            context.config.keyboardBackgroundImagePath.isEmpty()

        binding.themePreviewPhoto.visibility = android.view.View.GONE
        binding.themeAddIcon.visibility = android.view.View.GONE
        binding.themeCheck.visibility = if (selected) android.view.View.VISIBLE else android.view.View.GONE
        binding.themeName.text = context.getString(theme.nameResId)

        val preview = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            this.cornerRadius = this@KeyboardThemesAdapter.cornerRadius
            if (theme.type == KeyboardThemeType.GRADIENT && theme.gradientColors.size >= 2) {
                orientation = GradientDrawable.Orientation.TL_BR
                colors = theme.gradientColors
            } else {
                setColor(theme.backgroundColor)
            }
            if (selected) setStroke((context.resources.displayMetrics.density * 3).toInt(), primaryColor)
        }
        binding.themePreview.background = preview
        binding.root.setOnClickListener { onPresetClick(theme) }
    }

    private fun bindPhotoTile(holder: ViewHolder, primaryColor: Int, textColor: Int) {
        val binding = holder.binding
        binding.themeName.text = context.getString(com.itamadersomajinc.banglatype.R.string.my_photo)
        val selected = context.config.keyboardThemeId == KEYBOARD_THEME_CUSTOM_PHOTO &&
            context.config.keyboardBackgroundImagePath.isNotEmpty()

        val preview = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = this@KeyboardThemesAdapter.cornerRadius
            setColor(Color.argb(40, 128, 128, 128))
            setStroke(
                (context.resources.displayMetrics.density * if (selected) 3 else 1).toInt(),
                if (selected) primaryColor else textColor
            )
        }
        binding.themePreview.background = preview

        val file = context.keyboardBackgroundImageFile()
        if (selected && file.exists()) {
            val bmp = try {
                BitmapFactory.decodeFile(file.absolutePath)
            } catch (e: Exception) {
                null
            }
            if (bmp != null) {
                binding.themePreviewPhoto.setImageBitmap(bmp)
                binding.themePreviewPhoto.visibility = android.view.View.VISIBLE
                binding.themeAddIcon.visibility = android.view.View.GONE
            } else {
                binding.themePreviewPhoto.visibility = android.view.View.GONE
                binding.themeAddIcon.visibility = android.view.View.VISIBLE
            }
        } else {
            binding.themePreviewPhoto.visibility = android.view.View.GONE
            binding.themeAddIcon.visibility = android.view.View.VISIBLE
        }

        binding.themeCheck.visibility = if (selected) android.view.View.VISIBLE else android.view.View.GONE
        binding.root.setOnClickListener { onPhotoTileClick() }
    }
}
