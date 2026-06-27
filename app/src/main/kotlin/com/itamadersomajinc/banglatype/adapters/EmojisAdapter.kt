package com.itamadersomajinc.banglatype.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.emoji2.text.EmojiCompat
import androidx.recyclerview.widget.RecyclerView
import com.itamadersomajinc.banglatype.commons.extensions.adjustAlpha
import com.itamadersomajinc.banglatype.commons.extensions.getProperTextColor
import com.itamadersomajinc.banglatype.databinding.ItemEmojiBinding
import com.itamadersomajinc.banglatype.databinding.ItemEmojiCategoryTitleBinding
import com.itamadersomajinc.banglatype.helpers.EmojiData
import com.itamadersomajinc.banglatype.helpers.getCategoryTitleRes

class EmojisAdapter(
    val context: Context,
    var items: List<Item>,
    val itemClick: (emoji: EmojiData) -> Unit,
    val itemLongClick: ((emoji: EmojiData) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)
    private val textColor = context.getProperTextColor()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_EMOJI -> EmojiViewHolder(
                ItemEmojiBinding.inflate(layoutInflater, parent, false)
            )

            ITEM_TYPE_CATEGORY -> EmojiCategoryViewHolder(
                ItemEmojiCategoryTitleBinding.inflate(layoutInflater, parent, false)
            )

            else -> throw IllegalArgumentException("Unsupported view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is EmojiViewHolder -> holder.bindView(item as Item.Emoji)
            is EmojiCategoryViewHolder -> holder.bindView(item as Item.Category)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Item.Emoji -> ITEM_TYPE_EMOJI
            is Item.Category -> ITEM_TYPE_CATEGORY
        }
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(emojiItems: List<Item>) {
        items = emojiItems
        notifyDataSetChanged()
    }

    inner class EmojiViewHolder(val binding: ItemEmojiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(emoji: Item.Emoji) {
            val processed = EmojiCompat.get().process(emoji.emojiData.emoji)
            itemView.apply {
                binding.emojiValue.text = processed
                setOnClickListener {
                    itemClick.invoke(emoji.emojiData)
                }
                if (emoji.emojiData.variants.isNotEmpty() && itemLongClick != null) {
                    setOnLongClickListener {
                        itemLongClick.invoke(emoji.emojiData)
                        true
                    }
                } else {
                    setOnLongClickListener(null)
                }
            }
        }
    }

    inner class EmojiCategoryViewHolder(val binding: ItemEmojiCategoryTitleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(category: Item.Category) {
            binding.emojiCategoryTitle.apply {
                text = context.getString(getCategoryTitleRes(category.value))
                setTextColor(textColor.adjustAlpha(0.6f))
            }
        }
    }

    sealed interface Item {
        data class Emoji(val emojiData: EmojiData) : Item
        data class Category(val value: String) : Item
    }

    companion object {
        private const val ITEM_TYPE_EMOJI = 0
        private const val ITEM_TYPE_CATEGORY = 1
    }
}
