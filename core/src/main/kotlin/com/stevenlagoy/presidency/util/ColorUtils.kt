@file:JvmName("ColorUtils")
package com.stevenlagoy.presidency.util

import com.badlogic.gdx.graphics.Color

/**
 * Parses "#RRGGBB" or "#AARRGGBB" into a gdx Color.
 * Note: gdx's own Color.valueOf expects alpha LAST (RRGGBBAA); this expects
 * alpha FIRST to match this project's JSON convention.
 */
fun parseGdxColor(hex: String): Color {
    val clean = hex.removePrefix("#")
    return when (clean.length) {
        6 -> Color.valueOf(clean) // gdx already handles bare RRGGBB natively
        8 -> Color.valueOf(clean.substring(2) + clean.substring(0, 2)) // AARRGGBB -> RRGGBBAA
        else -> throw IllegalArgumentException("Invalid hex color: $hex")
    }
}

/**
 * Turns an ARGB color into an RGBA color. Unpacks Alpha, Red, Green, and Blue
 * channels then repacks into RGBA order.
 */
fun argbToRgba(argb: Int): Int {
    val a = (argb ushr 24) and 0xFF
    val r = (argb ushr 16) and 0xFF
    val g = (argb ushr 8) and 0xFF
    val b = argb and 0xFF

    // Pack into RGBA order: Red, Green, Blue, Alpha
    return (r shl 24) or (g shl 16) or (b shl 8) or (a shl 0)
}

/**
 * Turns an RGBA color into an ARGB color. Unpacks Red, Green, Blue, and Alpha
 * channels then repacks into ARGB order.
 */
fun rgbaToArgb(rgba: Int): Int {
    val r = (rgba ushr 24) and 0xFF
    val g = (rgba ushr 16) and 0xFF
    val b = (rgba ushr 8) and 0xFF
    val a = rgba and 0xFF

    // Pack into ARGB order: Alpha, Red, Green, Blue
    return (a shl 24) or (r shl 16) or (g shl 8) or (b shl 0)
}

/** Serializes to "#AARRGGBB". */
fun Color.toArgbHex(): String {
    val rgbaHex = toString() // gdx's native format: "RRGGBBAA", no '#'
    return "#" + rgbaHex.substring(6, 8) + rgbaHex.substring(0, 6)
}

/** Serializes to "#RRGGBB", dropping alpha. */
fun Color.toRgbHex(): String = "#" + toString().substring(0, 6)

/** Alpha-blends [other] over this color, weighting this color by [alpha]. */
@JvmOverloads
fun Color.merge(other: Color, alpha: Float = 0.5f): Color =
    cpy().lerp(other, 1f - alpha)
