package com.stevenlagoy.presidency.map.travel.route

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.map.Municipality

class Airport(
    ENGINE: Engine,
    name: String,
    val commonName: String,
    val IATA: String,
    val location: Municipality,
    val size: AirportSize,
    val enplanement: Int,
) : Route(ENGINE, name, emptyList()) {

    enum class AirportSize {
        LARGE, MEDIUM, SMALL;
    }

    constructor(ENGINE: Engine, json: JSONObject) : this(
        ENGINE,
        json.get("fullName", String::class.java),
        json.get("commonName", String::class.java),
        json.get("IATA", String::class.java),
        ENGINE.MAP_MANAGER.matchMunicipalityByName(json.get("location", String::class.java)).get(),
        json.get("size", AirportSize::class.java),
        json.get("enplanement", Int::class.java),
    )

    override val connections get() = when (size) {
        AirportSize.LARGE ->
            ENGINE.MAP_MANAGER.ROUTE_MANAGER.getAirports(AirportSize.LARGE).map { it.location } +
            ENGINE.MAP_MANAGER.ROUTE_MANAGER.getAirportsInRegion(location.state.region!!, AirportSize.MEDIUM).map { it.location }
        AirportSize.MEDIUM ->
            ENGINE.MAP_MANAGER.ROUTE_MANAGER.getAirportsInRegion(location.state.region!!, AirportSize.LARGE).map { it.location } +
            ENGINE.MAP_MANAGER.ROUTE_MANAGER.getAirportsInDivision(location.state.division!!, AirportSize.SMALL).map { it.location }
        AirportSize.SMALL ->
            ENGINE.MAP_MANAGER.ROUTE_MANAGER.getAirportsInDivision(location.state.division!!, AirportSize.MEDIUM).map { it.location } +
            ENGINE.MAP_MANAGER.ROUTE_MANAGER.getAirportsInDivision(location.state.division, AirportSize.SMALL).map { it.location }
    }

    override fun toJson() = JSONObject(IATA, listOf(
        JSONObject("name", name),
        JSONObject("commonName", commonName),
        JSONObject("IATA", IATA),
        JSONObject("location", location.uniqueName),
        JSONObject("size", size.name),
        JSONObject("enplanement", enplanement),
    ))

    override fun fromJson(json: JSONObject?): Airport {
        TODO("")
    }

}
