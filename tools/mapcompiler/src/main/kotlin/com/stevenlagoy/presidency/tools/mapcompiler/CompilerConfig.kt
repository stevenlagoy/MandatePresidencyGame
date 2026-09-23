package com.stevenlagoy.presidency.tools.mapcompiler

/**
 * Immutable configuration for a compilation run.
 *
 * The image dimensions here are the *padded* dimensions that the source PNGs
 * must already be. The compiler will fail if the actual image dimensions don't
 * match these values.
 *
 * Default values match the target padded size: 52224x28672 at 512px tiles.
 */
class CompilerConfig(
    val tileSize:    Int = 512,
    val imageWidth:  Int = 52224,
    val imageHeight: Int = 28672,
    val tilesWide:   Int = imageWidth / tileSize,
    val tilesHigh:   Int = imageHeight / tileSize,
) {
    init {
        require(tileSize > 0) { "tileSize must be positive" }
        require(imageWidth % tileSize == 0) {
            "imageWidth ($imageWidth) must be a multiple of tileSize ($tileSize). " +
            "Pad the source image to the next multiple."
        }
        require(imageHeight % tileSize == 0) {
            "imageHeight ($imageHeight) must be a multiple of tileSize ($tileSize). " +
            "Pad the source image to the next multiple."
        }
        require(tilesWide == imageWidth / tileSize) { "tilesWide must equal imageWidth / tileSize" }
        require(tilesHigh == imageHeight / tileSize) { "tilesHigh must equal imageHeight / tileSize" }
    }
}
