package org.medaware.catalyst

import java.awt.Color

fun String.capitalized(): String {
    if (length < 2) return uppercase()
    return substring(0, 1).uppercase() + substring(1)
}

fun luminance(r: Float, g: Float, b: Float): Float = 0.2126f * r + 0.7152f * g + 0.0722f * b

fun textColor(r: Float, g: Float, b: Float): String = if (luminance(r, g, b) > 0.5) "000000" else "ffffff"

fun textColor(hex: String): String {
    var str = hex.trim()

    if (str.startsWith('#'))
        str = str.substring(1)

    val color = Color.decode("#$str")

    val components = color.getRGBComponents(null)

    return textColor(components[0], components[1], components[2])
}
