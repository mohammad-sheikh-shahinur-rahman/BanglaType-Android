package com.itamadersomajinc.banglatype.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.itamadersomajinc.banglatype.commons.extensions.*
import com.itamadersomajinc.banglatype.commons.helpers.ensureBackgroundThread
import com.itamadersomajinc.banglatype.R
import com.itamadersomajinc.banglatype.databinding.ItemClipOnKeyboardBinding
import com.itamadersomajinc.banglatype.databinding.ItemSectionLabelBinding
import com.itamadersomajinc.banglatype.extensions.config
import com.itamadersomajinc.banglatype.extensions.getCurrentClip
import com.itamadersomajinc.banglatype.extensions.getStrokeColor
import com.itamadersomajinc.banglatype.helpers.ClipsHelper
import com.itamadersomajinc.banglatype.helpers.ITEM_CLIP
import com.itamadersomajinc.banglatype.helpers.ITEM_SECTION_LABEL
import com.itamadersomajinc.banglatype.interfaces.RefreshClipsListener
import com.itamadersomajinc.banglatype.models.Clip
import com.itamadersomajinc.banglatype.models.ClipsSectionLabel
import com.itamadersomajinc.banglatype.models.ListItem

class ClipsKeyboardAdapter(
    val context: Context, var items: ArrayList<ListItem>, val refreshClipsListener: RefreshClipsListener,
    val itemClick: (clip: Clip) -> Unit
) : RecyclerView.Adapter<ClipsKeyboardAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    private var textColor = context.getProperTextColor()
    private var backgroundColor = context.getProperBackgroundColor()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = when (viewType) {
            ITEM_SECTION_LABEL -> ItemSectionLabelBinding.inflate(layoutInflater, parent, false)
            else -> ItemClipOnKeyboardBinding.inflate(layoutInflater, parent, false)
        }

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bindView(item) { itemView ->
            when (item) {
                is Clip -> setupClip(itemView, item)
                is ClipsSectionLabel -> setupSection(itemView, item)
            }

            (itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = item is ClipsSectionLabel
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = when {
        items[position] is ClipsSectionLabel -> ITEM_SECTION_LABEL
        else -> ITEM_CLIP
    }

    private fun setupClip(view: View, clip: Clip) {
        ItemClipOnKeyboardBinding.bind(view).apply {
            val rippleBg = clipHolder.background as RippleDrawable
            val layerDrawable = rippleBg.findDrawableByLayerId(R.id.clipboard_background_holder) as LayerDrawable
            layerDrawable.findDrawableByLayerId(R.id.clipboard_background_stroke).applyColorFilter(context.getStrokeColor())
            layerDrawable.findDrawableByLayerId(R.id.clipboard_background_shape).applyColorFilter(backgroundColor)

            clipValue.apply {
                text = clip.value
                removeUnderlines()
                setTextColor(textColor)
            }

            clipPin.apply {
                applyColorFilter(textColor)
                setImageResource(if (clip.pinned) R.drawable.ic_pin_filled_vector else R.drawable.ic_pin_vector)
                setOnLongClickListener { context.toast(R.string.pin_text); true }
                setOnClickListener {
                    val id = clip.id ?: return@setOnClickListener
                    if (id < 0) return@setOnClickListener
                    ensureBackgroundThread {
                        ClipsHelper(context).setPinned(id, !clip.pinned)
                        refreshClipsListener.refreshClips()
                    }
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupSection(view: View, sectionLabel: ClipsSectionLabel) {
        ItemSectionLabelBinding.bind(view).apply {
            clipsSectionLabel.apply {
                text = sectionLabel.value
                setTextColor(textColor)
            }

            clipsSectionIcon.apply {
                applyColorFilter(textColor)

                if (sectionLabel.isCurrent) {
                    setOnLongClickListener { context.toast(R.string.pin_text); true; }
                    setImageDrawable(resources.getDrawable(R.drawable.ic_pin_vector))
                    setOnClickListener {
                        ensureBackgroundThread {
                            val currentClip = context.getCurrentClip() ?: return@ensureBackgroundThread
                            val clip = Clip(null, currentClip)
                            ClipsHelper(context).insertClip(clip)
                            refreshClipsListener.refreshClips()
                            context.toast(R.string.text_pinned)
                            if (context.config.vibrateOnKeypress) {
                                performHapticFeedback()
                            }
                        }
                    }
                } else {
                    setImageDrawable(resources.getDrawable(R.drawable.ic_pin_filled_vector))
                    background = null   // avoid doing any animations on clicking clipboard_manager_holder
                }
            }
        }
    }

    open inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(any: Any, callback: (itemView: View) -> Unit): View {
            return itemView.apply {
                callback(this)

                if (any is Clip) {
                    setOnClickListener {
                        itemClick.invoke(any)
                    }
                    setOnLongClickListener {
                        val id = any.id
                        if (id != null && id >= 0) {
                            ensureBackgroundThread {
                                ClipsHelper(context).delete(id)
                                refreshClipsListener.refreshClips()
                                context.toast(R.string.clip_deleted)
                            }
                        }
                        true
                    }
                }
            }
        }
    }
}
