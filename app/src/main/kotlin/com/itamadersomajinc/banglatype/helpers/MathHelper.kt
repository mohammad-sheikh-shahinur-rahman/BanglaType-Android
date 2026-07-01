package com.itamadersomajinc.banglatype.helpers

import com.itamadersomajinc.banglatype.extensions.toAsciiString
import com.itamadersomajinc.banglatype.extensions.toBengaliString

object MathHelper {
    fun tryEvaluateMath(text: String): String {
        return try {
            val isBengali = text.any { it in '০'..'৯' }
            val expression = text.toAsciiString().trim()
            if (!expression.any { it.isDigit() }) return text

            val result = object : Any() {
                fun eval(str: String): Double {
                    return object : Any() {
                        var pos = -1
                        var ch = 0
                        fun nextChar() {
                            ch = if (++pos < str.length) str[pos].toInt() else -1
                        }

                        fun eat(charToEat: Int): Boolean {
                            while (ch == ' '.toInt()) nextChar()
                            if (ch == charToEat) {
                                nextChar()
                                return true
                            }
                            return false
                        }

                        fun parse(): Double {
                            nextChar()
                            val x = parseExpression()
                            if (pos < str.length) return Double.NaN
                            return x
                        }

                        fun parseExpression(): Double {
                            var x = parseTerm()
                            while (true) {
                                if (eat('+'.toInt())) x += parseTerm()
                                else if (eat('-'.toInt())) x -= parseTerm()
                                else return x
                            }
                        }

                        fun parseTerm(): Double {
                            var x = parseFactor()
                            while (true) {
                                if (eat('*'.toInt())) x *= parseFactor()
                                else if (eat('/'.toInt())) x /= parseFactor()
                                else return x
                            }
                        }

                        fun parseFactor(): Double {
                            if (eat('+'.toInt())) return parseFactor()
                            if (eat('-'.toInt())) return -parseFactor()
                            var x: Double
                            val startPos = pos
                            if (eat('('.toInt())) {
                                x = parseExpression()
                                eat(')'.toInt())
                            } else if (ch >= '0'.toInt() && ch <= '9'.toInt() || ch == '.'.toInt()) {
                                while (ch >= '0'.toInt() && ch <= '9'.toInt() || ch == '.'.toInt()) nextChar()
                                x = str.substring(startPos, pos).toDouble()
                            } else {
                                return Double.NaN
                            }
                            return x
                        }
                    }.parse()
                }
            }.eval(expression)

            if (result.isNaN()) text else {
                val longRes = result.toLong()
                val resString = if (result == longRes.toDouble()) longRes.toString() else result.toString()
                if (isBengali) resString.toBengaliString() else resString
            }
        } catch (e: Exception) {
            text
        }
    }
}
