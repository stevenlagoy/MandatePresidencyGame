package com.stevenlagoy.presidency.tools.mapcompiler

import com.stevenlagoy.presidency.tools.mapcompiler.model.CompiledMap
import com.stevenlagoy.presidency.tools.mapcompiler.model.CompiledRegion
import com.stevenlagoy.presidency.tools.mapcompiler.model.LayerType
import com.stevenlagoy.presidency.tools.mapcompiler.model.PixelRun
import java.util.ArrayList

/**
 * Accumulates pixel runs from [LayerProcessor] across all tiles, then produces
 * a final [CompiledMap] with fully built [CompiledRegion] objects including
 * bounding boxes, centroids, and sorted run lists.
 *
 * Memory layout: one [RegionData] per unique color encountered. Each
 * [RegionData] holds the growing list of runs for that color. For a map with
 * ~3,200 counties plus ~20 terrain types, this is a manageable number of
 * HashMaps entries — the memory pressure is in the run lists themselves.
 *
 * A US county map of this resolution might have on the order of 5–50 million
 * pixel runs (one per contiguous horizontal span per row per county). Each
 * [PixelRun] is a lightweight data class (3 ints). At ~30 bytes each that's
 * roughly 150MB–1.5GB of run data in the worst case. If this proves too large,
 * a streaming writer (write runs directly to disk per tile) is a straightforward
 * follow-up optimization.
 */
class RegionAccumulator(private val layerType: LayerType) {

    private val regions = HashMap<Int, RegionData>(1024)
    private var nextId = 0

    /**
     * Called by [LayerProcessor] for each pixel run found in a tile.
     * All coordinates are in global image space.
     */
    fun addRun(color: Int, row: Int, colStart: Int, length: Int) {
        val data = regions.getOrPut(color) { RegionData(id = nextId++) }
        data.addRun(row, colStart, length)
    }

    /**
     * Finalizes all accumulated data into a [CompiledMap].
     * Should be called exactly once, after all tiles have been processed.
     */
    fun build(): CompiledMap {
        val compiledRegions = regions.entries
            .sortedBy { it.value.id }
            .map { (color, data) -> data.toCompiledRegion(color) }

        return CompiledMap(
            layerType   = layerType,
            imageWidth  = 0,   // caller fills these in if needed; omitted here
            imageHeight = 0,   // to keep the accumulator decoupled from config
            regions     = compiledRegions,
        )
    }

    private inner class RegionData(val id: Int) {
        val runs = ArrayList<PixelRun>(64)

        // Bounding box — updated incrementally
        var minX = Int.MAX_VALUE
        var minY = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var maxY = Int.MIN_VALUE

        // Centroid accumulators — use Long to avoid overflow on ~1.5B pixels
        var sumX = 0L
        var sumY = 0L
        var pixelCount = 0L

        fun addRun(row: Int, colStart: Int, length: Int) {
            runs.add(PixelRun(row = row, colStart = colStart, length = length))

            // Bounding box
            if (colStart < minX)            minX = colStart
            if (row < minY)                 minY = row
            val colEnd = colStart + length - 1
            if (colEnd > maxX)              maxX = colEnd
            if (row > maxY)                 maxY = row

            // Centroid: sum of all pixel x,y positions in this run.
            // For a run at (row, colStart..colStart+length-1):
            //   sumX += colStart + (colStart+1) + ... + (colStart+length-1)
            //         = length * colStart + (0 + 1 + ... + length-1)
            //         = length * colStart + length*(length-1)/2
            sumX += length.toLong() * colStart + length.toLong() * (length - 1) / 2
            sumY += length.toLong() * row
            pixelCount += length
        }

        fun toCompiledRegion(color: Int): CompiledRegion {
            val centroidX = if (pixelCount > 0) sumX.toFloat() / pixelCount else 0f
            val centroidY = if (pixelCount > 0) sumY.toFloat() / pixelCount else 0f

            return CompiledRegion(
                id         = id,
                color      = color,
                minX       = if (minX == Int.MAX_VALUE) 0 else minX,
                minY       = if (minY == Int.MAX_VALUE) 0 else minY,
                maxX       = if (maxX == Int.MIN_VALUE) 0 else maxX,
                maxY       = if (maxY == Int.MIN_VALUE) 0 else maxY,
                centroidX  = centroidX,
                centroidY  = centroidY,
                pixelCount = pixelCount,
                runs       = runs,  // already in row-major order (tiles processed top→bottom, left→right)
            )
        }
    }
}
