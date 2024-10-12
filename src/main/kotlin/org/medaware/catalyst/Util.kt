package org.medaware.catalyst

fun String.capitalized(): String {
    if (length < 2) return uppercase()
    return substring(0, 1).uppercase() + substring(1)
}