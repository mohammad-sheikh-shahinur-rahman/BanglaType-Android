package com.itamadersomajinc.banglatype.activities

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.itamadersomajinc.banglatype.commons.dialogs.FilePickerDialog
import com.itamadersomajinc.banglatype.commons.extensions.*
import com.itamadersomajinc.banglatype.commons.helpers.*
import com.itamadersomajinc.banglatype.commons.interfaces.RefreshRecyclerViewListener
import com.itamadersomajinc.banglatype.R
import com.itamadersomajinc.banglatype.adapters.ShortcutsActivityAdapter
import com.itamadersomajinc.banglatype.databinding.ActivityManageShortcutsBinding
import com.itamadersomajinc.banglatype.dialogs.AddOrEditShortcutDialog
import com.itamadersomajinc.banglatype.dialogs.ExportClipsDialog
import com.itamadersomajinc.banglatype.extensions.config
import com.itamadersomajinc.banglatype.extensions.shortcutsDB
import com.itamadersomajinc.banglatype.helpers.ShortcutsHelper
import com.itamadersomajinc.banglatype.models.Shortcut
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class ManageShortcutsActivity : SimpleActivity(), RefreshRecyclerViewListener {
    companion object {
        private const val PICK_EXPORT_SHORTCUTS_INTENT = 23
        private const val PICK_IMPORT_SHORTCUTS_SOURCE_INTENT = 24
    }

    private val binding by viewBinding(ActivityManageShortcutsBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupOptionsMenu()
        updateTextColors(binding.shortcutsItemsHolder)
        updateShortcuts()

        binding.apply {
            setupEdgeToEdge(padBottomSystem = listOf(shortcutsItemsList))
            setupMaterialScrollListener(binding.shortcutsItemsList, binding.shortcutsAppbar)

            shortcutsItemsPlaceholder.text = "${getText(R.string.shortcuts_empty)}\n\n${getText(R.string.manage_shortcuts_summary)}"
            shortcutsItemsPlaceholder2.apply {
                underlineText()
                setTextColor(getProperPrimaryColor())
                setOnClickListener {
                    addOrEditShortcut()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupTopAppBar(binding.shortcutsAppbar, NavigationIcon.Arrow)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == PICK_EXPORT_SHORTCUTS_INTENT && resultCode == Activity.RESULT_OK && resultData != null && resultData.data != null) {
            val outputStream = contentResolver.openOutputStream(resultData.data!!)
            exportShortcutsTo(outputStream)
        } else if (requestCode == PICK_IMPORT_SHORTCUTS_SOURCE_INTENT && resultCode == Activity.RESULT_OK && resultData != null && resultData.data != null) {
            val inputStream = contentResolver.openInputStream(resultData.data!!)
            parseFile(inputStream)
        }
    }

    private fun setupOptionsMenu() {
        binding.shortcutsToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add_shortcut_item -> {
                    addOrEditShortcut()
                    true
                }

                R.id.export_shortcuts -> {
                    exportShortcuts()
                    true
                }

                R.id.import_shortcuts -> {
                    importShortcuts()
                    true
                }

                else -> false
            }
        }
    }

    override fun refreshItems() {
        updateShortcuts()
    }

    private fun updateShortcuts() {
        ensureBackgroundThread {
            val shortcuts = shortcutsDB.getShortcuts().toMutableList() as ArrayList<Shortcut>
            runOnUiThread {
                ShortcutsActivityAdapter(this, shortcuts, binding.shortcutsItemsList, this) {
                    addOrEditShortcut(it as Shortcut)
                }.apply {
                    binding.shortcutsItemsList.adapter = this
                }

                binding.apply {
                    shortcutsItemsList.beVisibleIf(shortcuts.isNotEmpty())
                    shortcutsItemsPlaceholder.beVisibleIf(shortcuts.isEmpty())
                    shortcutsItemsPlaceholder2.beVisibleIf(shortcuts.isEmpty())
                }
            }
        }
    }

    private fun addOrEditShortcut(shortcut: Shortcut? = null) {
        AddOrEditShortcutDialog(this, shortcut) {
            updateShortcuts()
        }
    }

    private fun exportShortcuts() {
        if (isQPlus()) {
            ExportClipsDialog(this, config.lastExportedClipsFolder, true) { path, filename ->
                Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TITLE, filename)
                    addCategory(Intent.CATEGORY_OPENABLE)

                    try {
                        startActivityForResult(this, PICK_EXPORT_SHORTCUTS_INTENT)
                    } catch (e: ActivityNotFoundException) {
                        toast(R.string.system_service_disabled, Toast.LENGTH_LONG)
                    } catch (e: Exception) {
                        showErrorToast(e)
                    }
                }
            }
        } else {
            handlePermission(PERMISSION_WRITE_STORAGE) {
                if (it) {
                    ExportClipsDialog(this, config.lastExportedClipsFolder, false) { path, filename ->
                        val file = File(path)
                        getFileOutputStream(file.toFileDirItem(this), true) {
                            exportShortcutsTo(it)
                        }
                    }
                }
            }
        }
    }

    private fun exportShortcutsTo(outputStream: OutputStream?) {
        if (outputStream == null) {
            toast(R.string.unknown_error_occurred)
            return
        }

        ensureBackgroundThread {
            val shortcuts = shortcutsDB.getShortcuts()
            if (shortcuts.isEmpty()) {
                toast(R.string.no_entries_for_exporting)
                return@ensureBackgroundThread
            }

            val map = LinkedHashMap<String, String>()
            shortcuts.forEach { map[it.trigger] = it.expansion }
            val json = Gson().toJson(map)
            outputStream.bufferedWriter().use { out ->
                out.write(json)
            }

            toast(R.string.exporting_successful)
        }
    }

    private fun importShortcuts() {
        if (isQPlus()) {
            Intent(Intent.ACTION_GET_CONTENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/plain"

                try {
                    startActivityForResult(this, PICK_IMPORT_SHORTCUTS_SOURCE_INTENT)
                } catch (e: ActivityNotFoundException) {
                    toast(R.string.system_service_disabled, Toast.LENGTH_LONG)
                } catch (e: Exception) {
                    showErrorToast(e)
                }
            }
        } else {
            handlePermission(PERMISSION_READ_STORAGE) {
                if (it) {
                    FilePickerDialog(this) {
                        ensureBackgroundThread {
                            parseFile(File(it).inputStream())
                        }
                    }
                }
            }
        }
    }

    private fun parseFile(inputStream: InputStream?) {
        if (inputStream == null) {
            toast(R.string.unknown_error_occurred)
            return
        }

        var shortcutsImported = 0
        ensureBackgroundThread {
            try {
                val token = object : TypeToken<LinkedHashMap<String, String>>() {}.type
                val entries = Gson().fromJson<LinkedHashMap<String, String>>(inputStream.bufferedReader(), token) ?: LinkedHashMap()
                entries.forEach { (trigger, expansion) ->
                    val shortcut = Shortcut(null, trigger, expansion)
                    if (ShortcutsHelper(this).insertShortcut(shortcut) > 0) {
                        shortcutsImported++
                    }
                }

                runOnUiThread {
                    val msg = if (shortcutsImported > 0) R.string.importing_successful else R.string.no_new_entries_for_importing
                    toast(msg)
                    updateShortcuts()
                }
            } catch (e: Exception) {
                showErrorToast(e)
            }
        }
    }
}
