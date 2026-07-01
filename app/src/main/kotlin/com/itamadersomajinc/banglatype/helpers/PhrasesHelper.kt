package com.itamadersomajinc.banglatype.helpers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itamadersomajinc.banglatype.R
import com.itamadersomajinc.banglatype.commons.extensions.getProperTextColor
import com.itamadersomajinc.banglatype.commons.extensions.applyColorFilter
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable

object PhrasesHelper {
    val commonPhrases = listOf(
        "আসসালামু আলাইকুম",
        "ওয়ালাইকুম আসসালাম",
        "জাজাকাল্লাহু খাইরান",
        "আলহামদুলিল্লাহ",
        "ইনশাআল্লাহ",
        "মাশাআল্লাহ",
        "সুবহানাল্লাহ",
        "ধন্যবাদ",
        "অভিনন্দন",
        "শুভকামনা"
    )

    val juktakkhorHelp = listOf(
        "ক্ষ" to "ক + ষ",
        "জ্ঞ" to "জ + ঞ",
        "শ্চ" to "শ + চ",
        "স্থ" to "স + থ",
        "হ্ম" to "হ + ম",
        "হ্ন" to "হ + ন",
        "ত্ত" to "ত + ত",
        "ঞ্জ" to "ঞ + জ",
        "ঞ্চ" to "ঞ + চ",
        "ণ্ট" to "ণ + ট",
        "ণ্ঠ" to "ণ + ঠ",
        "ণ্ড" to "ণ + ড"
    )
}

class SimpleTextAdapter(
    val context: Context,
    val items: List<String>,
    private val textColor: Int,
    private val backgroundColor: Int,
    private val strokeColor: Int,
    val itemClick: (text: String) -> Unit
) : RecyclerView.Adapter<SimpleTextAdapter.ViewHolder>() {

    private val customTypeface = com.itamadersomajinc.banglatype.commons.helpers.FontHelper.getTypeface(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_phrase_on_keyboard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textView: TextView = view.findViewById(R.id.phrase_value)

        fun bind(text: String) {
            textView.text = text
            textView.setTextColor(textColor)
            textView.typeface = customTypeface
            
            val rippleBg = itemView.background as? RippleDrawable
            val layerDrawable = rippleBg?.findDrawableByLayerId(R.id.clipboard_background_holder) as? LayerDrawable
            layerDrawable?.findDrawableByLayerId(R.id.clipboard_background_stroke)?.applyColorFilter(strokeColor)
            layerDrawable?.findDrawableByLayerId(R.id.clipboard_background_shape)?.applyColorFilter(backgroundColor)

            itemView.setOnClickListener { itemClick(text) }
        }
    }
}
