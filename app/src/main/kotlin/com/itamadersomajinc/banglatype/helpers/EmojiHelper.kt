package com.itamadersomajinc.banglatype.helpers

object EmojiHelper {
    private val emojiMap = mapOf(
        // English
        "love" to "❤️",
        "happy" to "😊",
        "cry" to "😢",
        "hot" to "🔥",
        "cool" to "😎",
        "angry" to "😡",
        "ok" to "👌",
        "yes" to "✅",
        "no" to "❌",
        "thanks" to "🙏",
        "smile" to "🙂",
        "gift" to "🎁",
        "cake" to "🎂",
        "party" to "🥳",
        "sun" to "☀️",
        "moon" to "🌙",
        "star" to "⭐",
        "flower" to "🌸",
        "congrats" to "🎉",
        "wow" to "😮",
        "lol" to "😂",
        "hi" to "👋",
        "hey" to "👋",
        
        // Bangla
        "ভালোবাসা" to "❤️",
        "হাসি" to "😊",
        "কান্না" to "😢",
        "আগুন" to "🔥",
        "রাগ" to "😡",
        "ঠিক" to "👌",
        "ধন্যবাদ" to "🙏",
        "জন্মদিন" to "🎂",
        "সূর্য" to "☀️",
        "চাঁদ" to "🌙",
        "তারা" to "⭐",
        "ফুল" to "🌸",
        "শুভেচ্ছা" to "🎉",
        "অবাক" to "😮",
        "স্বাগতম" to "👋"
    )

    fun getEmojiForWord(word: String): String? {
        val low = word.lowercase()
        // Exact match
        emojiMap[low]?.let { return it }
        
        // Prefix match for longer words
        if (low.length >= 3) {
            for ((key, emoji) in emojiMap) {
                if (key.startsWith(low) || low.startsWith(key)) {
                    return emoji
                }
            }
        }
        return null
    }
}
