package com.stevenlagoy.presidency.map

import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.RandomAccessFile

/**
 * Lightweight region descriptor loaded from a compiled .bin file.
 * This is the runtime equivalent of the compiler's CompiledRegion —
 * it holds everything from the region table but NOT the run data,
 * which is read on demand via `MapBinaryLoader.loadRuns`.
 *
 * All coordinates are in source-image pixel space.
 */
data class RegionData(
    val id: Int,
    val color: Int,
    val minX: Int,
    val minY: Int,
    val maxX: Int,
    val maxY: Int,
    val centroidX: Float,
    val centroidY: Float,
    val pixelCount: Long,
    /** Byte offset into the run data section of the .bin file. */
    val runOffset: Long,
    val runCount: Int,
) {
    val boundingWidth:  Int get() = maxX - minX + 1
    val boundingHeight: Int get() = maxY - minY + 1
}

/**
 * Run-length encoded pixel span, loaded on demand.
 * Coordinates are in global source-image pixel space.
 */
data class PixelRun(val row: Int, val colStart: Int, val length: Int)

/**
 * Loaded map index for one layer (.bin file). Provides:
 *   - O(1) lookup of a region by its source-image color
 *   - Ordered list of all regions
 *   - On-demand loading of pixel run data for a specific region
 *
 * Typical usage:
 *   val counties = MapBinaryLoader.load(Gdx.files.internal("maps/counties.bin").file())
 *   val region = counties.regionByColor(pickedColor)
 *   val runs = counties.loadRuns(region)
 */
class MapIndex private constructor(
    val imageWidth: Int,
    val imageHeight: Int,
    val layerId: Int,
    val regions: List<RegionData>,
    /** Byte offset in the file where the run data section begins. */
    private val runSectionOffset: Long,
    private val sourceFile: File,
) {
    private val byColor: Map<Int, RegionData> = regions.associateBy { it.color }
    private val byId: Map<Int, RegionData>    = regions.associateBy { it.id }

    fun regionByColor(argb: Int): RegionData? = byColor[argb]
    fun regionById(id: Int): RegionData?      = byId[id]

    /**
     * Loads and returns the pixel runs for [region] from disk.
     * This performs a seek + sequential read; cache the result if you
     * need it more than once per frame.
     */
    fun loadRuns(region: RegionData): List<PixelRun> {
        if (region.runCount == 0) return emptyList()

        RandomAccessFile(sourceFile, "r").use { raf ->
            raf.seek(runSectionOffset + region.runOffset)
            return List(region.runCount) {
                val row      = raf.readInt()
                val colStart = raf.readInt()
                val length   = raf.readShort().toInt()
                raf.readShort() // padding
                PixelRun(row, colStart, length)
            }
        }
    }

    companion object {
        private const val MAGIC          = "MMAP"
        private const val SUPPORTED_VER  = 1
        private const val HEADER_BYTES   = 19   // 4+2+1+4+4+4
        private const val TABLE_ENTRY_B  = 48   // matches BinaryWriter

        /**
         * Loads the header and region table from [file].
         * The run data section is NOT loaded — it is read on demand via [loadRuns].
         *
         * @throws IllegalArgumentException if the file is not a valid MMAP binary.
         */
        fun load(file: File): MapIndex {
            DataInputStream(BufferedInputStream(FileInputStream(file))).use { din ->
                // Header
                val magic = String(ByteArray(4) { din.readByte() })
                require(magic == MAGIC) { "Not a valid MMAP file: $file" }

                val version = din.readShort().toInt()
                require(version == SUPPORTED_VER) {
                    "Unsupported MMAP version $version in $file (expected $SUPPORTED_VER)"
                }

                val layerId      = din.readByte().toInt()
                val imageWidth   = din.readInt()
                val imageHeight  = din.readInt()
                val regionCount  = din.readInt()

                // Region table
                val regions = ArrayList<RegionData>(regionCount)
                repeat(regionCount) {
                    regions += RegionData(
                        id          = din.readInt(),
                        color       = din.readInt(),
                        minX        = din.readInt(),
                        minY        = din.readInt(),
                        maxX        = din.readInt(),
                        maxY        = din.readInt(),
                        centroidX   = din.readFloat(),
                        centroidY   = din.readFloat(),
                        pixelCount  = din.readLong(),
                        runOffset   = din.readLong(),
                        runCount    = din.readInt(),
                    )
                }

                val runSectionOffset = HEADER_BYTES.toLong() + regionCount.toLong() * TABLE_ENTRY_B

                return MapIndex(
                    imageWidth       = imageWidth,
                    imageHeight      = imageHeight,
                    layerId          = layerId,
                    regions          = regions,
                    runSectionOffset = runSectionOffset,
                    sourceFile       = file,
                )
            }
        }
    }
}
