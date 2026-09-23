package com.stevenlagoy.presidency.map

import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.map.entities.MapEntity
import java.util.TimeZone

/**
 * The tz database partitions the world into regions where local clocks all
 * show the same time. Time Zone regions are areas listed in the tz database
 * with a unique identifier.
 */
class TimeZoneRegion(
    engine: Engine,
    val identifier: String,
    val timeZone: TimeZone,
) : MapEntity(engine, identifier) {
}
