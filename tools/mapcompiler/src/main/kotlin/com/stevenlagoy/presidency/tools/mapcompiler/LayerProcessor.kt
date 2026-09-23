package com.stevenlagoy.presidency.tools.mapcompiler

/**
 * Stateless processor that converts a single tile's pixel buffer into pixel
 * runs and feeds them to a [RegionAccumulator].
 *
 * Each call to [processTile] walks the tile row by row. Within each row it
 * performs a single left-to-right pass, coalescing consecutive pixels of the
 * same color into one [com.stevenlagoy.presidency.tools.mapcompiler.model.PixelRun].
 *
 * Pixel coordinates output here are in *global image space* (i.e. the tile's
 * offset is added back before the run is emitted).
 *
 * Runs that span a tile boundary horizontally are not possible (each tile is a
 * fixed-width slice), but runs within a tile row are coalesced greedily. Runs
 * that span tile boundaries vertically are also not merged here — the
 * [RegionAccumulator] can merge them if needed, but for our binary format it
 * doesn't matter: adjacent runs on the same global row but in different tiles
 * will simply appear consecutively in the run list and can be coalesced at
 * load time if desired.
 */
object LayerProcessor {

    /**
     * @param tileX       Tile column index (0-based)
     * @param tileY       Tile row index (0-based)
     * @param pixels      ARGB int array in row-major order, length = tileSize²
     * @param tileSize    Width (and height) of each tile in pixels
     * @param imageWidth  Full image width, used to compute global column offset
     * @param emptyColor  ARGB int value to skip entirely (null = skip nothing)
     * @param accumulator Receives each completed pixel run
     */
    fun processTile(
        tileX: Int,
        tileY: Int,
        pixels: IntArray,
        tileSize: Int,
        imageWidth: Int,
        emptyColor: Int?,
        accumulator: RegionAccumulator
    ) {
        val globalColOffset = tileX * tileSize
        val globalRowOffset = tileY * tileSize

        for (localRow in 0 until tileSize) {
            val globalRow = globalRowOffset + localRow
            val rowBase = localRow * tileSize

            var runStart = -1
            var runColor = 0

            for (localCol in 0 until tileSize) {
                val pixel = pixels[rowBase + localCol]

                if (pixel == emptyColor) {
                    // Flush open runs before skipping
                    if (runStart >= 0) {
                        accumulator.addRun(
                            color    = runColor,
                            row      = globalRow,
                            colStart = globalColOffset + runStart,
                            length   = localCol - runStart,
                        )
                        runStart = -1
                    }
                    continue
                }

                if (runStart < 0) {
                    // Start a new run
                    runStart = localCol
                    runColor = pixel
                }
                else if (pixel != runColor) {
                    // Color changed: flush current run and start new
                    accumulator.addRun(
                        color    = runColor,
                        row      = globalRow,
                        colStart = globalColOffset + runStart,
                        length   = localCol - runStart,
                    )
                    runStart = localCol
                    runColor = pixel
                }
            }
            if (runStart >= 0) {
                accumulator.addRun(
                    color    = runColor,
                    row      = globalRow,
                    colStart = globalColOffset + runStart,
                    length   = tileSize - runStart,
                )
            }
        }
    }
}
