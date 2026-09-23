package com.stevenlagoy.presidency.map.travel.route

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.map.entities.MapEntity

class Airport(
    engine: Engine,
    name: String = "",
    commonName: String = "",
    IATA: String = "",
    _location: MapEntity? = null,
    size: AirportSize = AirportSize.SMALL,
    enplanement: Int = 0,
    connections: List<MapEntity> = listOf(),
) : Route(engine, name, connections) {

    var commonName = commonName
        internal set

    var IATA = IATA
        internal set

    lateinit var location: MapEntity
        internal set

    var size = size
        internal set

    var enplanement = enplanement
        internal set

    init {
        if (_location != null) location = _location
    }

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    override var connections: List<MapEntity> = connections
//        get() = if (location?.state?.censusDivision == null) return emptyList() else when (size) {
//            AirportSize.LARGE ->
//                engine.MAP_MANAGER.ROUTE_MANAGER.getAirports(AirportSize.LARGE).map { it.location } +
//                engine.MAP_MANAGER.ROUTE_MANAGER.getAirportsInRegion(location!!.state.censusDivision!!.censusRegion, AirportSize.MEDIUM).map { it.location }
//            AirportSize.MEDIUM ->
//                engine.MAP_MANAGER.ROUTE_MANAGER.getAirportsInRegion(location!!.state.censusDivision!!.censusRegion, AirportSize.LARGE).map { it.location } +
//                engine.MAP_MANAGER.ROUTE_MANAGER.getAirportsInDivision(location!!.state.censusDivision!!, AirportSize.SMALL).map { it.location }
//            AirportSize.SMALL ->
//                engine.MAP_MANAGER.ROUTE_MANAGER.getAirportsInDivision(location!!.state.censusDivision!!, AirportSize.MEDIUM).map { it.location } +
//                engine.MAP_MANAGER.ROUTE_MANAGER.getAirportsInDivision(location!!.state.censusDivision!!, AirportSize.SMALL).map { it.location }
//        }.filterNotNull()

    override fun toJson() = JSONObject(IATA, listOf(
        JSONObject("fullName", name),
        JSONObject("commonName", commonName),
        JSONObject("IATA", IATA),
        JSONObject("location", location.name),
        JSONObject("size", size.name),
        JSONObject("enplanement", enplanement),
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        commonName = json.requireString("commonName")
        IATA = json.requireString("IATA")
        location = engine.MAP_MANAGER.matchPlace(json.requireString("location")).orElseThrow { IllegalArgumentException("Could not find location ${json.requireString("location")} for airport $name") }
        size = AirportSize.valueOf(json.requireString("size").uppercase().replace(Regex("[^A-Z]"), "_"))
        enplanement = json.requireInt("enplanement")
    }

    enum class AirportSize {
        LARGE, MEDIUM, SMALL;
    }

}
