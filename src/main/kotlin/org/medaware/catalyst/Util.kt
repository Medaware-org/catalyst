package org.medaware.catalyst

import java.awt.Color

fun String.capitalized(): String {
    if (length < 2) return uppercase()
    return substring(0, 1).uppercase() + substring(1)
}

fun luminance(r: Int, g: Int, b: Int): Double = 0.2126 * r + 0.7152 * g + 0.0722 * b

fun textColor(r: Int, g: Int, b: Int): String = if (luminance(r, g, b) > 0.5) "000000" else "ffffff"

fun textColor(hex: String): String {
    var str = hex.trim()

    if (str.startsWith('#'))
        str = str.substring(1)

    val color = Color.decode(str)

    return textColor(color.red, color.green, color.blue)
}
