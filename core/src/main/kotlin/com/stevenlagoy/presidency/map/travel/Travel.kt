package com.stevenlagoy.presidency.map.travel

import com.stevenlagoy.presidency.characters.Citizen
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.map.entities.MapEntity
import com.stevenlagoy.presidency.map.MapManager
import com.stevenlagoy.presidency.map.travel.vehicle.*

class Travel(
    engine: Engine,
    val travellersDestinations: MutableMap<Citizen, MapEntity>,
    val availableVehicles: MutableList<Vehicle>,
    val legs: MutableList<TravelLeg>,
) : EngineBound(engine) {

    abstract class TravelLeg(
        engine: Engine,
        val source: MapEntity,
        val destination: MapEntity,
        val travellers: MutableSet<Citizen>,
        open val vehicle: Vehicle
    ) : EngineBound(engine) {
        abstract val distance: Double
        val cost:     Double = vehicle.costPerMile * distance
        val duration: Double = vehicle.speed * distance
    }

    class RoadLeg(
        engine: Engine,
        source: MapEntity,
        destination: MapEntity,
        travellers: MutableSet<Citizen>,
        override val vehicle: RoadVehicle
    ) : TravelLeg(engine, source, destination, travellers, vehicle) {
        override val distance: Double = MapManager.getRoadDistance(source, destination)
    }

    class RailLeg(
        engine: Engine,
        source: MapEntity,
        destination: MapEntity,
        travellers: MutableSet<Citizen>,
        override val vehicle: RailVehicle,
    ) : TravelLeg(engine, source, destination, travellers, vehicle) {
        override val distance: Double = MapManager.getRailDistance(source, destination)
    }

    class AirLeg(
        engine: Engine,
        source: MapEntity,
        destination: MapEntity,
        travellers: MutableSet<Citizen>,
        override val vehicle: AirVehicle
    ) : TravelLeg(engine, source, destination, travellers, vehicle) {
        override val distance: Double = MapManager.getAirDistance(source, destination)
    }

    class WaterLeg(
        engine: Engine,
        source: MapEntity,
        destination: MapEntity,
        travellers: MutableSet<Citizen>,
        override val vehicle: WaterVehicle
    ) : TravelLeg(engine, source, destination, travellers, vehicle) {
        override val distance: Double = MapManager.getWaterDistance(source, destination)
    }

    val distance: Double get() = legs.sumOf<TravelLeg> { it.distance }
    val cost:     Double get() = legs.sumOf<TravelLeg> { it.cost }
    val duration: Double get() = legs.sumOf<TravelLeg> { it.duration }

}
