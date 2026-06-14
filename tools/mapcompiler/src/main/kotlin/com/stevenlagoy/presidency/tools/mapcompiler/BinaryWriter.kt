package com.stevenlagoy.presidency.tools.mapcompiler

import com.stevenlagoy.presidency.tools.mapcompiler.model.CompiledMap
import com.stevenlagoy.presidency.tools.mapcompiler.model.CompiledRegion
import java.io.BufferedOutputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * Writes a [CompiledMap] to a binary file.
 *
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │  FILE FORMAT  (all values big-endian, as per Java DataOutputStream)     │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │  HEADER (18 bytes)                                                      │
 * │    magic:          4 bytes  — ASCII "MMAP"                              │
 * │    version:        2 bytes  — short, currently 1                        │
 * │    layer_id:       1 byte   — LayerType.ordinal                         │
 * │    image_width:    4 bytes  — int                                       │
 * │    image_height:   4 bytes  — int                                       │
 * │    region_count:   4 bytes  — int                                       │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │  REGION TABLE  (region_count × 44 bytes each)                          │
 * │  Each entry:                                                            │
 * │    id:             4 bytes  — int                                       │
 * │    color:          4 bytes  — int (ARGB)                                │
 * │    min_x:          4 bytes  — int                                       │
 * │    min_y:          4 bytes  — int                                       │
 * │    max_x:          4 bytes  — int                                       │
 * │    max_y:          4 bytes  — int                                       │
 * │    centroid_x:     4 bytes  — float                                     │
 * │    centroid_y:     4 bytes  — float                                     │
 * │    pixel_count:    8 bytes  — long                                      │
 * │    run_offset:     8 bytes  — long (byte offset into run data section)  │
 * │    run_count:      4 bytes  — int                                       │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │  RUN DATA  (variable)                                                   │
 * │  For each region (in id order), run_count entries of 8 bytes each:     │
 * │    row:            4 bytes  — int (global image row)                    │
 * │    col_start:      4 bytes  — int (global image column)                 │
 * │    length:         2 bytes  — short (pixel count; max 512 for tile row) │
 * │    _padding:       2 bytes  — zeroed, for 8-byte alignment              │
 * └─────────────────────────────────────────────────────────────────────────┘
 *
 * The run_offset in the region table is the byte offset *from the start of
 * the run data section* (not from the start of the file). This makes it easy
 * to seek to a specific region's runs without knowing the header/table sizes
 * in the runtime reader.
 *
 * At runtime, load the header + region table into a HashMap<Int, RegionData>
 * keyed by color. Only seek into run data on demand (e.g. for selection
 * rendering or fog-of-war). For most gameplay you only need the region table.
 */
object BinaryWriter {

    private const val MAGIC = "MMAP"
    private const val VERSION: Short = 1
    private const val REGION_TABLE_ENTRIES_BYTES = 48 // 4+4+4+4+4+4+4+4+8+8+4 = 42
    private const val RUN_ENTRY_BYTES = 8             // 4+4+2+2 = 8

    fun write(map: CompiledMap, outputFile: File) {
        // Pre-compute run offsets (byte offsets into the run data section)
        val runOffsets = LongArray(map.regions.size)
        var offset = 0L
        for ((i, region) in map.regions.withIndex()) {
            runOffsets[i] = offset
            offset += region.runs.size.toLong() * RUN_ENTRY_BYTES
        }

        DataOutputStream(BufferedOutputStream(FileOutputStream(outputFile), 1 shl 20)).use { out ->
            writeHeader(out, map)
            writeRegionTable(out, map.regions, runOffsets)
            writeRunData(out, map.regions)
        }
    }

    private fun writeHeader(out: DataOutputStream, map: CompiledMap) {
        // magic: 4 bytes
        MAGIC.forEach { out.writeByte(it.code) }
        // version: 2 bytes
        out.writeShort(VERSION.toInt())
        // layer_id: 1 byte
        out.writeByte(map.layerType.ordinal)
        // image_width, image_height: 4 bytes each
        out.writeInt(map.imageWidth)
        out.writeInt(map.imageHeight)
        // region_count: 4 bytes
        out.writeInt(map.regions.size)
    }

    private fun writeRegionTable(
        out: DataOutputStream,
        regions: List<CompiledRegion>,
        runOffsets: LongArray,
    ) {
        for ((i, region) in regions.withIndex()) {
            out.writeInt(region.id)
            out.writeInt(region.color)
            out.writeInt(region.minX)
            out.writeInt(region.minY)
            out.writeInt(region.maxX)
            out.writeInt(region.maxY)
            out.writeFloat(region.centroidX)
            out.writeFloat(region.centroidY)
            out.writeLong(region.pixelCount)
            out.writeLong(runOffsets[i])
            out.writeInt(region.runs.size)
        }
    }

    private fun writeRunData(out: DataOutputStream, regions: List<CompiledRegion>) {
        for (region in regions) {
            for (run in region.runs) {
                out.writeInt(run.row)
                out.writeInt(run.colStart)
                out.writeShort(run.length)
                out.writeShort(0) // padding
            }
        }
    }
}
