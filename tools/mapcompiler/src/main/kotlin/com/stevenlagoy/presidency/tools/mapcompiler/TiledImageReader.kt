package com.stevenlagoy.presidency.tools.mapcompiler

import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.imageio.stream.FileImageInputStream

class TiledImageReader(
    private val file: File,
    private val config: CompilerConfig,
) {
    private val reader: javax.imageio.ImageReader
    private val stream: FileImageInputStream

    init {
        val imageStream = FileImageInputStream(file)
        val readers = ImageIO.getImageReadersBySuffix("png")
        check(readers.hasNext()) { "No PNG ImageReader available in this JVM" }
        val imageReader = readers.next()
        imageReader.input = imageStream

        val width  = imageReader.getWidth(0)
        val height = imageReader.getHeight(0)
        check(width == config.imageWidth && height == config.imageHeight) {
            "Image dimensions mismatch for ${file.name}: " +
                "expected ${config.imageWidth}×${config.imageHeight}, got ${width}×${height}. " +
                "Pad the source image first (see scripts/pad_maps.sh)."
        }

        reader = imageReader
        stream = imageStream
    }

    /**
     * Iterates over every tile in row-major order, invoking [block] with the
     * tile's column index, row index, and pixel buffer.
     *
     * Decodes one horizontal strip (tileSize rows × full image width) per
     * tile-row, then slices it into individual tile columns. This means each
     * pixel row is decoded exactly once regardless of how many tile columns
     * there are — O(imageHeight) decodes instead of O(tilesWide * tilesHigh).
     *
     * Peak memory: one strip = imageWidth * tileSize * 4 bytes (~107MB for
     * 52224×512).
     *
     * The tileBuffer IntArray is reused across [block] calls; do not retain it.
     */
    fun forEachTile(block: (tileX: Int, tileY: Int, pixels: IntArray) -> Unit) {
        val tileSize   = config.tileSize
        val imageWidth = config.imageWidth
        val param      = reader.defaultReadParam

        // Reusable buffers
        val stripBuffer = IntArray(imageWidth * tileSize)
        val tileBuffer  = IntArray(tileSize * tileSize)

        for (tileY in 0 until config.tilesHigh) {
            val stripY = tileY * tileSize

            // Decode one full-width strip — one decode call per tile-row
            param.sourceRegion = Rectangle(0, stripY, imageWidth, tileSize)
            val strip: BufferedImage = reader.read(0, param)
            strip.getRGB(0, 0, imageWidth, tileSize, stripBuffer, 0, imageWidth)

            // Slice the strip into tile columns entirely in memory
            for (tileX in 0 until config.tilesWide) {
                val colOffset = tileX * tileSize

                // Copy this tile's columns out of the strip row by row
                for (row in 0 until tileSize) {
                    val stripRowBase = row * imageWidth
                    val tileRowBase  = row * tileSize
                    System.arraycopy(
                        stripBuffer, stripRowBase + colOffset,
                        tileBuffer,  tileRowBase,
                        tileSize
                    )
                }

                block(tileX, tileY, tileBuffer)
            }
        }
    }

    fun close() {
        try { reader.dispose() } catch (_: Exception) {}
        try { stream.close()  } catch (_: Exception) {}
    }
}
