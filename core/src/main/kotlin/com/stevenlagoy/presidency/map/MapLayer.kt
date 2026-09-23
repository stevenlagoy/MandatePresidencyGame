package com.stevenlagoy.presidency.map

import com.stevenlagoy.presidency.map.MapIndex.Companion.HEADER_BYTES
import com.stevenlagoy.presidency.map.MapIndex.Companion.MAGIC
import com.stevenlagoy.presidency.map.MapIndex.Companion.SUPPORTED_VER
import com.stevenlagoy.presidency.map.MapIndex.Companion.TABLE_ENTRY_B
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream

/**
 * Loaded index for one compiled map layer (.bin file).
 *
 * Provides spatial queries over all regions in the layer.
 * Pixel run data is loaded eagerly by default since MapRegion.contains()
 * and MapRegion.paintOnto() need it — if memory becomes a concern, this
 * can be made lazy without changing the public API.
 */
class MapLayer private constructor(
    val imageWidth:  Int,
    val imageHeight: Int,
    val layerId:     Int,
    val regions:     List<MapRegion>,
) {
    private val byColor: Map<Int, MapRegion> = regions.associateBy { it.color }
    private val byId:    Map<Int, MapRegion> = regions.associateBy { it.id }

    /** Returns the region whose source color matches [argb], or null. */
    fun regionByColor(argb: Int): MapRegion? = byColor[argb]
    /** Returns the region with the given sequential id, or null. */
    fun regionById(argb: Int): MapRegion? = byId[argb]

    /**
     * Returns the region containing [mapX], [mapY], or null if the
     * pixel is in empty/background space.
     *
     * Uses bounding boxes to quickly eliminate candidates before doing
     * a full run check — efficient for typical county-map densities.
     */
    fun regionAt(mapX: Int, mapY: Int): MapRegion? =
        regions
            .filter { r ->
                mapX >= r.minX && mapX <= r.maxX &&
                    mapY >= r.minY && mapY <= r.maxY
            }
            .firstOrNull { it.contains(mapX, mapY) }

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
        fun load(file: File): MapLayer {
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
                val regions = ArrayList<MapRegion>(regionCount)
                repeat(regionCount) {
                    regions += MapRegion(
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

                return MapLayer(
                    imageWidth       = imageWidth,
                    imageHeight      = imageHeight,
                    layerId          = layerId,
                    regions          = regions.toList(),                )
            }
        }
    }
}

/*
    public void loadMapBinaries() {
        File binFile = Gdx.files.internal("maps/counties.bin").file();
        MapIndex mapIndex = MapIndex.Companion.load(binFile);
        int matched = 0, unmatched = 0;
        for (County county : counties) {
            if (county.getColor() == null) continue;
            int countyColor = county.getColor();
            RegionData region = mapIndex.regionByColor(countyColor);
            if (region != null) {
                county.setRegion$core(region);
                matched++;
            }
            else {
                Logger.error("No map region for %s (color #%s)", county.getFullName(), ColorUtils.toHex(countyColor));
            }
        }
    }

 */
