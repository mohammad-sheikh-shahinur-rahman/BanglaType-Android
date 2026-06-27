package com.itamadersomajinc.banglatype.helpers

import android.content.Context
import org.json.JSONObject

/**
 * Avro Phonetic transliteration engine.
 *
 * A Kotlin port of the canonical Avro Phonetic parser (Rifat Nabi's jsAvroPhonetic /
 * Kaustav Das Modak's pyAvroPhonetic). The grammar itself is vendored, unmodified, from
 * OmicronLab's avro-lib.js (MIT licensed) at [LANGUAGE_AVRO_RULES].
 *
 * Converts Roman/Latin text into its Bengali phonetic equivalent, e.g.
 * "ami banglay gan gai" -> "আমি বাংলায় গান গাই".
 */
object AvroParser {

    private data class AvMatch(val type: String, val scope: String, val value: String?)
    private data class AvRule(val matches: List<AvMatch>, val replace: String)
    private data class AvPattern(val find: String, val replace: String, val rules: List<AvRule>?)

    @Volatile
    private var loaded = false

    private var vowels = "aeiou"
    private var consonants = "bcdfghjklmnpqrstvwxyz"
    private var caseSensitives = "oiudgjnrstyz"

    // Patterns split by whether they carry context rules, each indexed by their "find" string.
    // First occurrence wins, matching the original dictionary's longest-first ordering.
    private val nonRuleByFind = HashMap<String, AvPattern>()
    private val ruleByFind = HashMap<String, AvPattern>()
    private var maxNonRuleLen = 0
    private var maxRuleLen = 0

    @Synchronized
    private fun ensureLoaded(context: Context) {
        if (loaded) {
            return
        }
        try {
            val jsonString = context.assets.open(LANGUAGE_AVRO_RULES).bufferedReader().use { it.readText() }
            val data = JSONObject(jsonString).getJSONObject("data")
            vowels = data.optString("vowel", vowels)
            consonants = data.optString("consonant", consonants)
            caseSensitives = data.optString("casesensitive", caseSensitives)

            val patterns = data.getJSONArray("patterns")
            for (i in 0 until patterns.length()) {
                val obj = patterns.getJSONObject(i)
                val find = obj.getString("find")
                val replace = obj.optString("replace", "")
                val rulesArray = obj.optJSONArray("rules")
                if (rulesArray == null) {
                    if (!nonRuleByFind.containsKey(find)) {
                        nonRuleByFind[find] = AvPattern(find, replace, null)
                        maxNonRuleLen = maxOf(maxNonRuleLen, find.length)
                    }
                } else {
                    val rules = ArrayList<AvRule>(rulesArray.length())
                    for (r in 0 until rulesArray.length()) {
                        val ruleObj = rulesArray.getJSONObject(r)
                        val matchesArray = ruleObj.getJSONArray("matches")
                        val matches = ArrayList<AvMatch>(matchesArray.length())
                        for (m in 0 until matchesArray.length()) {
                            val matchObj = matchesArray.getJSONObject(m)
                            matches.add(
                                AvMatch(
                                    type = matchObj.optString("type", "prefix"),
                                    scope = matchObj.optString("scope", ""),
                                    value = if (matchObj.has("value")) matchObj.getString("value") else null
                                )
                            )
                        }
                        rules.add(AvRule(matches, ruleObj.optString("replace", "")))
                    }
                    if (!ruleByFind.containsKey(find)) {
                        ruleByFind[find] = AvPattern(find, replace, rules)
                        maxRuleLen = maxOf(maxRuleLen, find.length)
                    }
                }
            }
            loaded = true
        } catch (ignored: Exception) {
            loaded = false
        }
    }

    /** Pre-loads and caches the grammar (safe to call off the main thread). */
    fun preload(context: Context) = ensureLoaded(context)

    /**
     * Transliterates [text] from Roman script to Bengali. Returns [text] unchanged if the
     * grammar could not be loaded.
     */
    fun parse(context: Context, text: String): String {
        if (text.isEmpty()) {
            return ""
        }
        ensureLoaded(context)
        if (!loaded) {
            return text
        }

        val fixed = fixStringCase(text)
        val output = StringBuilder()
        val n = fixed.length
        var curEnd = 0
        var cur = 0
        while (cur < n) {
            if (cur >= curEnd) {
                var matched = false

                // Longest matching non-rule pattern takes precedence over any rule pattern.
                var len = minOf(maxNonRuleLen, n - cur)
                while (len >= 1) {
                    val pattern = nonRuleByFind[fixed.substring(cur, cur + len)]
                    if (pattern != null) {
                        output.append(pattern.replace)
                        curEnd = cur + len
                        matched = true
                        break
                    }
                    len--
                }

                if (!matched) {
                    len = minOf(maxRuleLen, n - cur)
                    while (len >= 1) {
                        val pattern = ruleByFind[fixed.substring(cur, cur + len)]
                        if (pattern != null) {
                            curEnd = cur + len
                            val replaced = processRules(pattern.rules!!, fixed, cur, curEnd)
                            output.append(replaced ?: pattern.replace)
                            matched = true
                            break
                        }
                        len--
                    }
                }

                if (!matched) {
                    curEnd = cur + 1
                    output.append(fixed[cur])
                }
            }
            cur++
        }
        return output.toString()
    }

    private fun processRules(rules: List<AvRule>, fixed: String, cur: Int, curEnd: Int): String? {
        var matched = false
        var replaced = ""
        for (rule in rules) {
            matched = false
            for (match in rule.matches) {
                matched = processMatch(match, fixed, cur, curEnd)
                if (!matched) {
                    break
                }
            }
            if (matched) {
                replaced = rule.replace
                break
            }
        }
        return if (matched) replaced else null
    }

    private fun processMatch(match: AvMatch, fixed: String, cur: Int, curEnd: Int): Boolean {
        var replace = true
        val type = match.type
        val chk = if (type == "prefix") cur - 1 else curEnd
        val negative = match.scope.startsWith("!")
        val scope = if (negative) match.scope.substring(1) else match.scope
        val len = fixed.length

        when (scope) {
            "punctuation" -> {
                val big = (chk < 0 && type == "prefix") ||
                    (chk >= len && type == "suffix") ||
                    isPunctuation(fixed[chk])
                if (!(big xor negative)) {
                    replace = false
                }
            }

            "vowel" -> {
                val guard = (chk >= 0 && type == "prefix") || (chk < len && type == "suffix")
                val big = guard && isVowel(fixed[chk])
                if (!(big xor negative)) {
                    replace = false
                }
            }

            "consonant" -> {
                val guard = (chk >= 0 && type == "prefix") || (chk < len && type == "suffix")
                val big = guard && isConsonant(fixed[chk])
                if (!(big xor negative)) {
                    replace = false
                }
            }

            "exact" -> {
                val value = match.value ?: ""
                val start: Int
                val end: Int
                if (type == "prefix") {
                    start = cur - value.length
                    end = cur
                } else {
                    start = curEnd
                    end = curEnd + value.length
                }
                if (!isExact(value, fixed, start, end, negative)) {
                    replace = false
                }
            }
        }
        return replace
    }

    private fun isExact(needle: String, haystack: String, start: Int, end: Int, matchNot: Boolean): Boolean {
        val matches = start >= 0 && end < haystack.length && haystack.substring(start, end) == needle
        return matches xor matchNot
    }

    private fun fixStringCase(text: String): String {
        val sb = StringBuilder(text.length)
        for (c in text) {
            sb.append(if (isCaseSensitive(c)) c else c.lowercaseChar())
        }
        return sb.toString()
    }

    private fun isCaseSensitive(c: Char) = caseSensitives.contains(c.lowercaseChar())
    private fun isVowel(c: Char) = vowels.contains(c.lowercaseChar())
    private fun isConsonant(c: Char) = consonants.contains(c.lowercaseChar())
    private fun isPunctuation(c: Char) = !(isVowel(c) || isConsonant(c))
}
