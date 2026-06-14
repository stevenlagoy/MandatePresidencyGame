package com.stevenlagoy.presidency.tools.mapcompiler.model

/**
 * Each map layer has a source PNG, an output binary, and a definition of what
 * color value means "this pixel belongs to no region."
 *
 * [emptyColor] is the RGB int (as returned by BufferedImage.getRGB) that the
 * scanner should skip entirely. A null value means no pixels are skipped, or
 * every color is a valid region identifier.
 */
enum class LayerType(
    val filename: String,
    val outputFilename: String,
    val emptyColor: Int?,
    val description: String,
) {
    COUNTIES(
        filename       = "Counties.png",
        outputFilename = "counties.bin",
        emptyColor     = 0xFF000000.toInt(), // Black
        description    = "US counties layer"
    ),
    TERRAIN(
        filename       = "Terrain.png",
        outputFilename = "terrain.bin",
        emptyColor     = null,
        description    = "Terrain type layer"
    ),
    URBAN(
        filename       = "Urban.png",
        outputFilename = "urban.bin",
        emptyColor     = 0xFF000000.toInt(), // Black
        description    = "Urban area layer"
    ),
}
