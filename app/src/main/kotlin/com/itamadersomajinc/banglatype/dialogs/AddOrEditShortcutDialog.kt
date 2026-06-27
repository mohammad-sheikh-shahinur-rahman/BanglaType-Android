package com.itamadersomajinc.banglatype.dialogs

import androidx.appcompat.app.AlertDialog
import com.itamadersomajinc.banglatype.commons.activities.BaseSimpleActivity
import com.itamadersomajinc.banglatype.commons.extensions.getAlertDialogBuilder
import com.itamadersomajinc.banglatype.commons.extensions.setupDialogStuff
import com.itamadersomajinc.banglatype.commons.extensions.showKeyboard
import com.itamadersomajinc.banglatype.commons.extensions.toast
import com.itamadersomajinc.banglatype.commons.helpers.ensureBackgroundThread
import com.itamadersomajinc.banglatype.R
import com.itamadersomajinc.banglatype.databinding.DialogAddOrEditShortcutBinding
import com.itamadersomajinc.banglatype.helpers.ShortcutsHelper
import com.itamadersomajinc.banglatype.models.Shortcut

class AddOrEditShortcutDialog(val activity: BaseSimpleActivity, val originalShortcut: Shortcut?, val callback: () -> Unit) {
    init {
        val binding = DialogAddOrEditShortcutBinding.inflate(activity.layoutInflater).apply {
            if (originalShortcut != null) {
                addShortcutTrigger.setText(originalShortcut.trigger)
                addShortcutExpansion.setText(originalShortcut.expansion)
            }
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok, null)
            .setNegativeButton(R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(binding.root, this) { alertDialog ->
                    alertDialog.showKeyboard(binding.addShortcutTrigger)
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val trigger = binding.addShortcutTrigger.text.toString().trim()
                        val expansion = binding.addShortcutExpansion.text.toString()
                        if (trigger.isEmpty()) {
                            activity.toast(R.string.trigger_cannot_be_empty)
                            return@setOnClickListener
                        }
                        if (expansion.isEmpty()) {
                            activity.toast(R.string.expansion_cannot_be_empty)
                            return@setOnClickListener
                        }

                        val shortcut = Shortcut(null, trigger, expansion)
                        if (originalShortcut != null) {
                            shortcut.id = originalShortcut.id
                        }

                        ensureBackgroundThread {
                            val result = ShortcutsHelper(activity).insertShortcut(shortcut)
                            activity.runOnUiThread {
                                if (result == -1L) {
                                    activity.toast(R.string.trigger_already_exists)
                                } else {
                                    callback()
                                    alertDialog.dismiss()
                                }
                            }
                        }
                    }
                }
            }
    }
}
