package com.itamadersomajinc.banglatype.helpers

import android.content.Context
import com.itamadersomajinc.banglatype.R
import org.json.JSONObject
import java.util.Locale

data class EmojiData(
    val emoji: String,
    val category: String,
    val variants: List<String> = emptyList(),
    var keywords: List<String> = emptyList()
)

val cachedVNTelexData = HashMap<String, String>()

fun getCategoryTitleRes(category: String): Int {
    return when (category) {
        RECENTLY_USED_EMOJIS -> R.string.recently_used
        "smileys_emotion" -> R.string.smileys_and_emotions
        "people_body" -> R.string.people_and_body
        "animals_nature" -> R.string.animals_and_nature
        "food_drink" -> R.string.food_and_drink
        "activities" -> R.string.activities
        "travel_places" -> R.string.travel_and_places
        "objects" -> R.string.objects
        "symbols" -> R.string.symbols
        "flags" -> R.string.flags
        else -> R.string.smileys_and_emotions
    }
}

fun getCategoryIconRes(category: String): Int {
    return when (category) {
        RECENTLY_USED_EMOJIS -> R.drawable.ic_clock_vector
        "smileys_emotion" -> R.drawable.ic_emoji_category_smileys
        "people_body" -> R.drawable.ic_emoji_category_people
        "animals_nature" -> R.drawable.ic_emoji_category_animals
        "food_drink" -> R.drawable.ic_emoji_category_food
        "activities" -> R.drawable.ic_emoji_category_activities
        "travel_places" -> R.drawable.ic_emoji_category_travel
        "objects" -> R.drawable.ic_emoji_category_objects
        "symbols" -> R.drawable.ic_emoji_category_symbols
        "flags" -> R.drawable.ic_emoji_category_flags
        else -> R.drawable.ic_emoji_category_smileys
    }
}

fun parseRawEmojiSpecsFile(context: Context, path: String): List<EmojiData> {
    val emojis = mutableListOf<EmojiData>()
    var currentCategory = ""
    var currentEmoji: EmojiData? = null
    try {
        context.assets.open(path).bufferedReader().useLines { lines ->
            lines.forEach { line ->
                val trimmed = line.trim()
                if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
                    currentCategory = trimmed.substring(1, trimmed.length - 1)
                    currentEmoji = null
                } else if (trimmed.isNotBlank() && !trimmed.startsWith("#")) {
                    val emojiValue = trimmed.removeSuffix(";;")
                    if (line.startsWith("\t")) {
                        // It's a variant of the last base emoji
                        (currentEmoji?.variants as? MutableList<String>)?.add(emojiValue)
                    } else {
                        // It's a new base emoji
                        val newEmoji = EmojiData(emojiValue, currentCategory, mutableListOf())
                        emojis.add(newEmoji)
                        currentEmoji = newEmoji
                    }
                }
            }
        }
    } catch (ignored: Exception) {
    }
    return emojis
}

private val emojiKeywords = HashMap<String, List<String>>()

fun parseEmojiKeywords(context: Context, path: String) {
    try {
        val jsonString = context.assets.open(path).bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(jsonString)
        val keys = jsonObject.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val keywordsArray = jsonObject.getJSONArray(key)
            val keywords = mutableListOf<String>()
            for (i in 0 until keywordsArray.length()) {
                keywords.add(keywordsArray.getString(i))
            }
            emojiKeywords[key] = keywords
        }
    } catch (ignored: Exception) {
    }
}

fun searchEmojis(emojis: List<EmojiData>, query: String): List<EmojiData> {
    val lowercaseQuery = query.lowercase(Locale.getDefault())
    return emojis.filter { emoji ->
        emoji.emoji.contains(query) || 
        emojiKeywords[emoji.emoji]?.any { it.contains(lowercaseQuery) } == true
    }
}

fun parseRawJsonSpecsFile(context: Context, path: String) {
    try {
        val jsonString = context.assets.open(path).bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(jsonString)
        val rules = jsonObject.getJSONObject("rules")
        val keys = rules.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            cachedVNTelexData[key] = rules.getString(key)
        }
    } catch (ignored: Exception) {
    }
}
