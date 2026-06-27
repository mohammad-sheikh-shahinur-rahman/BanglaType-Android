package com.itamadersomajinc.banglatype.adapters

import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.itamadersomajinc.banglatype.commons.activities.BaseSimpleActivity
import com.itamadersomajinc.banglatype.commons.adapters.MyRecyclerViewAdapter
import com.itamadersomajinc.banglatype.commons.dialogs.ConfirmationDialog
import com.itamadersomajinc.banglatype.commons.extensions.beVisibleIf
import com.itamadersomajinc.banglatype.commons.extensions.setupViewBackground
import com.itamadersomajinc.banglatype.commons.helpers.ensureBackgroundThread
import com.itamadersomajinc.banglatype.commons.interfaces.RefreshRecyclerViewListener
import com.itamadersomajinc.banglatype.commons.views.MyRecyclerView
import com.itamadersomajinc.banglatype.R
import com.itamadersomajinc.banglatype.databinding.ItemShortcutInActivityBinding
import com.itamadersomajinc.banglatype.dialogs.AddOrEditShortcutDialog
import com.itamadersomajinc.banglatype.extensions.shortcutsDB
import com.itamadersomajinc.banglatype.models.Shortcut

class ShortcutsActivityAdapter(
    activity: BaseSimpleActivity, var items: ArrayList<Shortcut>, recyclerView: MyRecyclerView, val listener: RefreshRecyclerViewListener, itemClick: (Any) -> Unit
) : MyRecyclerViewAdapter(activity, recyclerView, itemClick) {

    init {
        setupDragListener(true)
    }

    override fun getActionMenuId() = R.menu.cab_clips

    override fun prepareActionMode(menu: Menu) {
        menu.apply {
            findItem(R.id.cab_edit).isVisible = isOneItemSelected()
        }
    }

    override fun actionItemPressed(id: Int) {
        if (selectedKeys.isEmpty()) {
            return
        }

        when (id) {
            R.id.cab_edit -> editShortcut()
            R.id.cab_delete -> askConfirmDelete()
        }
    }

    override fun getSelectableItemCount() = items.size

    override fun getIsItemSelectable(position: Int) = true

    override fun getItemSelectionKey(position: Int) = items.getOrNull(position)?.id?.toInt()

    override fun getItemKeyPosition(key: Int) = items.indexOfFirst { it.id?.toInt() == key }

    override fun onActionModeCreated() {
        notifyDataSetChanged()
    }

    override fun onActionModeDestroyed() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = createViewHolder(ItemShortcutInActivityBinding.inflate(layoutInflater, parent, false).root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bindView(item, true, true) { itemView, layoutPosition ->
            setupView(itemView, item)
        }
        bindViewHolder(holder)
    }

    override fun getItemCount() = items.size

    private fun editShortcut() {
        val selectedShortcut = getSelectedItems().firstOrNull() ?: return
        AddOrEditShortcutDialog(activity, selectedShortcut) {
            listener.refreshItems()
            finishActMode()
        }
    }

    private fun askConfirmDelete() {
        ConfirmationDialog(activity, "", R.string.proceed_with_deletion, R.string.yes, R.string.cancel) {
            deleteSelection()
        }
    }

    private fun deleteSelection() {
        val deleteShortcuts = ArrayList<Shortcut>(selectedKeys.size)
        val positions = getSelectedItemPositions()

        getSelectedItems().forEach {
            deleteShortcuts.add(it)
        }

        items.removeAll(deleteShortcuts)
        removeSelectedItems(positions)

        ensureBackgroundThread {
            deleteShortcuts.forEach { shortcut ->
                activity.shortcutsDB.delete(shortcut.id!!.toLong())
            }

            if (items.isEmpty()) {
                listener.refreshItems()
            }
        }
    }

    private fun getSelectedItems() = items.filter { selectedKeys.contains(it.id!!.toInt()) } as ArrayList<Shortcut>

    private fun setupView(view: View, shortcut: Shortcut) {
        if (shortcut.id == null) {
            return
        }

        val isSelected = selectedKeys.contains(shortcut.id!!.toInt())
        ItemShortcutInActivityBinding.bind(view).apply {
            root.setupViewBackground(activity)
            shortcutTrigger.text = shortcut.trigger
            shortcutTrigger.setTextColor(textColor)
            shortcutExpansion.text = shortcut.expansion
            shortcutExpansion.setTextColor(textColor)
            shortcutHolder.isSelected = isSelected
        }
    }
}
