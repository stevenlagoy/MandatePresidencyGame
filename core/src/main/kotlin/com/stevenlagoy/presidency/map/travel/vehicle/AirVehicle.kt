package com.stevenlagoy.presidency.map.travel.vehicle

import com.stevenlagoy.presidency.map.travel.route.Airport

class AirVehicle(
    modelName: String,
    speed: Double,
    range: Double,
    capacity: Int,
    costPerMile: Double,
    val size: Airport.AirportSize
) : Vehicle(modelName, speed, range, capacity, costPerMile)
