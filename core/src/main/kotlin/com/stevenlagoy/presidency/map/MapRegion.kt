package com.stevenlagoy.presidency.map

import com.badlogic.gdx.graphics.Color
import com.stevenlagoy.presidency.util.argbToRgba

/**
 * Represents the spatial extent of one region on a map layer.
 *
 * All coordinates are in source-image pixel space. Callers that need
 * world-space coordinates should go through MapManager.
 *
 * Instances are created by MapLayer and should not be constructed directly.
 */
class MapRegion internal constructor(
    val id:           Int,
    color:        Int, // ARGB color acting as join key
    val minX:         Int,
    val minY:         Int,
    val maxX:         Int,
    val maxY:         Int,
    val centroidX:    Float,
    val centroidY:    Float,
    val pixelCount:   Long,
    private val runs: List<PixelRun>
) {

    val color: Color = Color(argbToRgba(color))

    val boundingWidth:  Int get() = maxX - minX + 1
    val boundingHeight: Int get() = maxY - minY + 1

    /**
     * Returns `true` if the given image-space pixel falls inside this region.
     * Uses bounding box as a fast reject before checking runs.
     */
    fun contains(imageX: Int, imageY: Int): Boolean {
        if (imageX !in minX..maxX) return false
        if (imageY !in minY..maxY) return false
        return runs.any { run ->
            run.row == imageY &&
            imageX >= run.colStart &&
            imageX < run.colStart + run.length
        }
    }

    /**
     * Paints this region onto the given Pixmap with the given RGBA8888 color.
     * Replaces existing pixel values — no blending.
     */
    fun paintOnto(pixmap: com.badlogic.gdx.graphics.Pixmap, rgbaColor: Int) {
        pixmap.blending = com.badlogic.gdx.graphics.Pixmap.Blending.None
        pixmap.setColor(rgbaColor)
        for (run in runs) {
            pixmap.drawLine(run.colStart, run.row, run.colStart + run.length - 1, run.row)
        }
    }

    /**
     * Paints this region onto the given Pixmap, scaling coordinates to fit
     * a pixmap of different dimensions than the source image.
     */
    fun paintOnto(
        pixmap: com.badlogic.gdx.graphics.Pixmap,
        rgbaColor: Int,
        scaleX: Float,
        scaleY: Float,
    ) {
        pixmap.blending = com.badlogic.gdx.graphics.Pixmap.Blending.None
        pixmap.setColor(rgbaColor)
        for (run in runs) {
            val y  = (run.row * scaleY).toInt()
            val x0 = (run.colStart * scaleX).toInt()
            val x1 = ((run.colStart + run.length) * scaleX).toInt()
            if (x1 > x0) pixmap.drawLine(x0, y, x1, y)
        }
    }
}
