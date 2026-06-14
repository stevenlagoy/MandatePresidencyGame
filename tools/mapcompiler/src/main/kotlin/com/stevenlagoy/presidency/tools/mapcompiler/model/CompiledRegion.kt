package com.stevenlagoy.presidency.tools.mapcompiler.model

/**
 * A single run of consecutive pixels in one image row, all belonging to the
 * same region. Stored in global image coordinates (not tile-local).
 *
 * Using Short for row and colStart caps image dimensions at 65,535 px — well
 * above our 52,224 × 28,672 padded size. Length fits in a Short too (max 512
 * for a full tile-width run, though rows can span multiple tiles).
 *
 * Packed as 3 × 2 bytes = 6 bytes per run in the binary format.
 */
data class PixelRun(
    val row: Int,
    val colStart: Int,
    val length: Int,
)

/**
 * A fully built region, ready to serialize.
 *
 * [id]       — zero-based sequential index assigned during accumulation.
 * [color]    — the ARGB int from the source image that identifies this region.
 * [minX],
 * [minY],
 * [maxX],
 * [maxY]     — bounding box in global image coordinates.
 * [centroidX],
 * [centroidY]— pixel centroid (mean of all pixel positions), as floats.
 * [pixelCount]— total number of pixels in this region (not runs).
 * [runs]     — RLE-encoded pixel coverage, sorted by row then colStart.
 */
data class CompiledRegion(
    val id: Int,
    val color: Int,
    val minX: Int,
    val minY: Int,
    val maxX: Int,
    val maxY: Int,
    val centroidX: Float,
    val centroidY: Float,
    val pixelCount: Long,
    val runs: List<PixelRun>,
)

/**
 * The full output of compiling one map layer.
 */
data class CompiledMap(
    val layerType: LayerType,
    val imageWidth: Int,
    val imageHeight: Int,
    val regions: List<CompiledRegion>,
)
