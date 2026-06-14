@file:JvmName("ColorUtils")
package com.stevenlagoy.presidency.util

/**
 * Parses a hex color string from JSON into an ARGB int compatible with
 * the map binary format (same representation as BufferedImage.getRGB).
 *
 * Accepts:
 *   "#RRGGBB"   → alpha is assumed 0xFF (fully opaque)
 *   "#AARRGGBB" → alpha taken from string
 *
 * Returns null if the string is null, blank, or malformed, so callers
 * can distinguish "no color assigned" from a parse error.
 */
fun parseHex(hex: String?): Int? {
    if (hex.isNullOrBlank()) return null
    val clean = hex.trimStart('#')
    return when (clean.length) {
        6 -> (0xFF shl 24) or clean.toInt(16)
        8 -> clean.toLong(16).toInt()
        else -> null
    }
}

fun toHex(argb: Int) = "#%08X".format(argb)
