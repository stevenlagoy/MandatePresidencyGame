package com.stevenlagoy.presidency.map.travel.route

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.map.Municipality

class Airport(
    engine: Engine,
    name: String,
    val commonName: String,
    val IATA: String,
    val location: Municipality,
    val size: AirportSize,
    val enplanement: Int,
) : Route(engine, name, emptyList()) {

    enum class AirportSize {
        LARGE, MEDIUM, SMALL;
    }

    constructor(engine: Engine, json: JSONObject) : this(
        engine,
        json.get("fullName", String::class.java),
        json.get("commonName", String::class.java),
        json.get("IATA", String::class.java),
        engine.MAP_MANAGER.matchMunicipality(json.get("location", String::class.java)).get(),
        json.get("size", AirportSize::class.java),
        json.get("enplanement", Int::class.java),
    )

    override val connections get() = when (size) {
        AirportSize.LARGE ->
            engine.MAP_MANAGER.ROUTE_MANAGER.getAirports(AirportSize.LARGE).map { it.location } +
            engine.MAP_MANAGER.ROUTE_MANAGER.getAirportsInRegion(location.state.censusRegion, AirportSize.MEDIUM).map { it.location }
        AirportSize.MEDIUM ->
            engine.MAP_MANAGER.ROUTE_MANAGER.getAirportsInRegion(location.state.censusRegion, AirportSize.LARGE).map { it.location } +
            engine.MAP_MANAGER.ROUTE_MANAGER.getAirportsInDivision(location.state.censusDivision, AirportSize.SMALL).map { it.location }
        AirportSize.SMALL ->
            engine.MAP_MANAGER.ROUTE_MANAGER.getAirportsInDivision(location.state.censusDivision, AirportSize.MEDIUM).map { it.location } +
            engine.MAP_MANAGER.ROUTE_MANAGER.getAirportsInDivision(location.state.censusDivision, AirportSize.SMALL).map { it.location }
    }

    override fun toJson() = JSONObject(IATA, listOf(
        JSONObject("name", name),
        JSONObject("commonName", commonName),
        JSONObject("IATA", IATA),
        JSONObject("location", location.fullName),
        JSONObject("size", size.name),
        JSONObject("enplanement", enplanement),
    ))

    override fun fromJson(json: JSONObject?): Airport {
        TODO("")
    }

}
