package com.itamadersomajinc.banglatype.helpers

import android.content.Context
import com.itamadersomajinc.banglatype.extensions.predictionsDB
import com.itamadersomajinc.banglatype.models.Bigram
import com.itamadersomajinc.banglatype.models.LearnedWord

/**
 * Word prediction / suggestion engine.
 *
 * Combines bundled frequency dictionaries (English + Bangla, from hermitdave/FrequencyWords, MIT)
 * with on-device learning (personal word frequencies + next-word bigrams persisted in Room).
 *
 * - [currentWordSuggestions] completes/ranks the word being typed (prefix match).
 * - [nextWordSuggestions] proposes the next word from learned bigrams.
 * - [learn] records a typed word and the (previous -> word) transition.
 */
object PredictionEngine {

    private const val EN_DICT = "dictionary/en.txt"
    private const val BN_DICT = "dictionary/bn.txt"
    private const val K_PER_NODE = 5
    const val MAX_SUGGESTIONS = 3

    private class TrieNode {
        val children = HashMap<Char, TrieNode>()
        var word: String? = null
        var freq: Long = 0
        // cached top words within this subtree, sorted by freq desc (size <= K_PER_NODE)
        var top: List<Pair<String, Long>> = emptyList()
    }

    private val tries = HashMap<String, TrieNode>()           // lang -> trie root
    private val learnedCounts = HashMap<String, Int>()        // personal word -> count (both languages)
    @Volatile private var learnedLoaded = false

    fun isBangla(text: String): Boolean = text.any { it.code in 0x0980..0x09FF }

    private fun langOf(text: String): String = if (isBangla(text)) "bn" else "en"

    @Synchronized
    private fun ensureDictLoaded(context: Context, lang: String) {
        if (tries.containsKey(lang)) {
            return
        }
        val root = TrieNode()
        try {
            val path = if (lang == "bn") BN_DICT else EN_DICT
            context.assets.open(path).bufferedReader().useLines { lines ->
                lines.forEach { line ->
                    val sep = line.indexOf(' ')
                    if (sep > 0) {
                        val word = line.substring(0, sep)
                        val freq = line.substring(sep + 1).trim().toLongOrNull() ?: 0L
                        if (word.isNotEmpty() && word.all { it.isLetter() }) {
                            insert(root, word.lowercase(), freq)
                        }
                    }
                }
            }
            computeTops(root)
        } catch (ignored: Exception) {
        }
        tries[lang] = root
    }

    @Synchronized
    private fun ensureLearnedLoaded(context: Context) {
        if (learnedLoaded) {
            return
        }
        try {
            context.predictionsDB.getAllLearnedWords().forEach { learnedCounts[it.word] = it.count }
        } catch (ignored: Exception) {
        }
        learnedLoaded = true
    }

    /** Pre-loads dictionaries and learned data; safe to call off the main thread. */
    fun preload(context: Context) {
        ensureLearnedLoaded(context)
        ensureDictLoaded(context, "en")
        ensureDictLoaded(context, "bn")
    }

    private fun insert(root: TrieNode, word: String, freq: Long) {
        var node = root
        for (c in word) {
            node = node.children.getOrPut(c) { TrieNode() }
        }
        if (node.word == null || freq > node.freq) {
            node.word = word
            node.freq = freq
        }
    }

    private fun computeTops(node: TrieNode): List<Pair<String, Long>> {
        val acc = ArrayList<Pair<String, Long>>()
        if (node.word != null) {
            acc.add(node.word!! to node.freq)
        }
        for (child in node.children.values) {
            acc.addAll(computeTops(child))
        }
        node.top = acc.sortedByDescending { it.second }.take(K_PER_NODE)
        return node.top
    }

    private fun completions(root: TrieNode, prefix: String): List<Pair<String, Long>> {
        var node = root
        for (c in prefix) {
            node = node.children[c] ?: return emptyList()
        }
        return node.top
    }

    /**
     * Suggestions for the word currently being typed (its [prefix]).
     * Learned words rank above dictionary completions.
     */
    fun currentWordSuggestions(context: Context, prefixRaw: String): List<String> {
        val prefix = prefixRaw.lowercase()
        if (prefix.isEmpty()) {
            return emptyList()
        }
        ensureLearnedLoaded(context)
        val lang = langOf(prefix)
        ensureDictLoaded(context, lang)
        val root = tries[lang] ?: return emptyList()

        val ranked = LinkedHashMap<String, Long>()
        // learned matches first, boosted well above dictionary frequencies
        learnedCounts.entries
            .filter { it.key.startsWith(prefix) && it.key != prefix }
            .sortedByDescending { it.value }
            .forEach { ranked[it.key] = Long.MAX_VALUE / 2 + it.value }

        var dict = completions(root, prefix)
        // light correction: if nothing matches, retry against the prefix minus its last char
        if (dict.isEmpty() && prefix.length >= 2) {
            dict = completions(root, prefix.dropLast(1)).filter { it.first != prefix }
        }
        dict.forEach { (w, f) -> if (w != prefix) ranked.putIfAbsent(w, f) }

        val result = ranked.entries.sortedByDescending { it.value }.take(MAX_SUGGESTIONS).map { it.key }.toMutableList()
        
        // Add emoji suggestion if available
        EmojiHelper.getEmojiForWord(prefix)?.let { emoji ->
            if (!result.contains(emoji)) {
                if (result.size >= MAX_SUGGESTIONS) {
                    result[result.size - 1] = emoji
                } else {
                    result.add(emoji)
                }
            }
        }
        
        return result
    }

    /** Next-word suggestions from learned bigrams of [prevWordRaw]. */
    fun nextWordSuggestions(context: Context, prevWordRaw: String): List<String> {
        val prev = prevWordRaw.lowercase()
        if (prev.isEmpty()) {
            return emptyList()
        }
        return try {
            context.predictionsDB.getNextWords(prev, MAX_SUGGESTIONS)
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Records that [word] was typed (optionally after [prevWord]). Updates the in-memory learned
     * counts immediately and persists to Room. Call off the main thread.
     */
    fun learn(context: Context, prevWordRaw: String?, wordRaw: String) {
        val word = wordRaw.lowercase()
        if (word.isEmpty() || !word.all { it.isLetter() }) {
            return
        }
        ensureLearnedLoaded(context)
        learnedCounts[word] = (learnedCounts[word] ?: 0) + 1
        try {
            val dao = context.predictionsDB
            if (dao.bumpWord(word) == 0) {
                dao.insertWord(LearnedWord(word, 1))
            }
            val prev = prevWordRaw?.lowercase()
            if (!prev.isNullOrEmpty() && prev.all { it.isLetter() }) {
                if (dao.bumpBigram(prev, word) == 0) {
                    dao.insertBigram(Bigram(prev, word, 1))
                }
            }
        } catch (ignored: Exception) {
        }
    }
}
