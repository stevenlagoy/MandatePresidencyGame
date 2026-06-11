package com.stevenlagoy.presidency.map.travel

import com.stevenlagoy.presidency.characters.Citizen
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.map.MapManager
import com.stevenlagoy.presidency.map.Municipality
import com.stevenlagoy.presidency.map.travel.vehicle.AirVehicle
import com.stevenlagoy.presidency.map.travel.vehicle.RailVehicle
import com.stevenlagoy.presidency.map.travel.vehicle.RoadVehicle
import com.stevenlagoy.presidency.map.travel.vehicle.Vehicle
import com.stevenlagoy.presidency.map.travel.vehicle.WaterVehicle

class Travel(
    protected val ENGINE: Engine,
    val travellersDestinations: MutableMap<Citizen, Municipality>,
    val availableVehicles: MutableList<Vehicle>,
    val legs: MutableList<TravelLeg>,
) {

    abstract class TravelLeg(
        protected val ENGINE: Engine,
        val source: Municipality,
        val destination: Municipality,
        val travellers: MutableSet<Citizen>,
        open val vehicle: Vehicle
    ) {
        abstract val distance: Double
        val cost:     Double = vehicle.costPerMile * distance
        val duration: Double = vehicle.speed * distance
    }

    class RoadLeg(
        ENGINE: Engine,
        source: Municipality,
        destination: Municipality,
        travellers: MutableSet<Citizen>,
        override val vehicle: RoadVehicle
    ) : TravelLeg(ENGINE, source, destination, travellers, vehicle) {
        override val distance: Double = MapManager.getRoadDistance(source, destination)
    }

    class RailLeg(
        ENGINE: Engine,
        source: Municipality,
        destination: Municipality,
        travellers: MutableSet<Citizen>,
        override val vehicle: RailVehicle,
    ) : TravelLeg(ENGINE, source, destination, travellers, vehicle) {
        override val distance: Double = MapManager.getRailDistance(source, destination)
    }

    class AirLeg(
        ENGINE: Engine,
        source: Municipality,
        destination: Municipality,
        travellers: MutableSet<Citizen>,
        override val vehicle: AirVehicle
    ) : TravelLeg(ENGINE, source, destination, travellers, vehicle) {
        override val distance: Double = MapManager.getAirDistance(source, destination)
    }

    class WaterLeg(
        ENGINE: Engine,
        source: Municipality,
        destination: Municipality,
        travellers: MutableSet<Citizen>,
        override val vehicle: WaterVehicle
    ) : TravelLeg(ENGINE, source, destination, travellers, vehicle) {
        override val distance: Double = MapManager.getWaterDistance(source, destination)
    }

    val distance: Double get() = legs.sumOf<TravelLeg> { it.distance }
    val cost: Double     get() = legs.sumOf<TravelLeg> { it.cost }
    val duration: Double get() = legs.sumOf<TravelLeg> { it.duration }

}
