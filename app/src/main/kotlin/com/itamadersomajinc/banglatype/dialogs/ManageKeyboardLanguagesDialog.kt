package com.itamadersomajinc.banglatype.dialogs

import org.fossify.commons.activities.BaseSimpleActivity
import org.fossify.commons.extensions.getAlertDialogBuilder
import org.fossify.commons.extensions.setupDialogStuff
import com.itamadersomajinc.banglatype.R
import com.itamadersomajinc.banglatype.adapters.ManageKeyboardLanguagesAdapter
import com.itamadersomajinc.banglatype.databinding.DialogManageKeyboardLanguagesBinding
import com.itamadersomajinc.banglatype.extensions.config
import com.itamadersomajinc.banglatype.extensions.getKeyboardLanguageText
import com.itamadersomajinc.banglatype.helpers.SUPPORTED_LANGUAGES

class ManageKeyboardLanguagesDialog(
    private val activity: BaseSimpleActivity,
    private val callback: () -> Unit
) {
    init {
        val binding = DialogManageKeyboardLanguagesBinding.inflate(activity.layoutInflater)
        val languageItems = SUPPORTED_LANGUAGES.map {
            it to activity.getKeyboardLanguageText(it)
        }.sortedBy { it.second }

        val adapter = ManageKeyboardLanguagesAdapter(activity.config, languageItems)
        binding.keyboardLanguageList.adapter = adapter

        activity.getAlertDialogBuilder()
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(R.string.ok) { _, _ ->
                val selectedLanguages = adapter.getSelectedLanguages()
                activity.config.selectedLanguages = selectedLanguages
                if (activity.config.keyboardLanguage !in selectedLanguages) {
                    activity.config.keyboardLanguage = selectedLanguages.first()
                }

                callback()
            }
            .apply {
                activity.setupDialogStuff(binding.root, this)
            }
    }
}
