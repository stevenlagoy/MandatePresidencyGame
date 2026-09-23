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
    CONGRESSIONAL_DISTRICTS(
        filename       = "CongressionalDistricts.png",
        outputFilename = "congressional_districts.bin",
        emptyColor     = 0xFF000000.toInt(), // Black
        description    = "Congressional Districts layer"
    ),
    COUNTY_SUBDIVISIONS(
        filename       = "CountySubdivisions.png",
        outputFilename = "county_subdivisions.bin",
        emptyColor     = 0xFF000000.toInt(), // Black
        description    = "US subcounty areas layer"
    ),
    HISTORICAL_PROVINCES(
        filename       = "HistoricalProvinces.png",
        outputFilename = "historical_provinces.bin",
        emptyColor     = 0xFF000000.toInt(), // black
        description    = "Historical provinces, districts, territories, and claims layer"
    ),
    HYDROLOGY(
        filename       = "Hydrology.png",
        outputFilename = "hydrology.bin",
        emptyColor     = 0xFF000000.toInt(), // Black
        description    = "Water body types and watersheds layer"
    ),
    PHYSIOGRAPHY(
        filename       = "Physiography.png",
        outputFilename = "physiography.bin",
        emptyColor     = 0xFF000000.toInt(), // Black
        description    = "Physiographic regions and provinces layer"
    ),
    PRECIPITATION(
        filename       = "Precipitation.png",
        outputFilename = "precipitation.bin",
        emptyColor     = 0xFF000000.toInt(), // Black
        description    = "Precipitation layer"
    ),
    TIME_ZONES(
        filename       = "TimeZones.png",
        outputFilename = "time_zones.bin",
        emptyColor     = 0xFF000000.toInt(), // Black
        description    = "tz database entry province layer"
    ),
    TOPOGRAPHY_BATHYMETRY(
        filename       = "TopographyBathymetry.png",
        outputFilename = "topography_bathymetry.bin",
        emptyColor     = null,
        description    = "Topography and bathymetry layer"
    ),
    PLACES(
        filename       = "Places.png",
        outputFilename = "places.bin",
        emptyColor     = 0xFF000000.toInt(), // Black
        description    = "Census places layer"
    ),
}
