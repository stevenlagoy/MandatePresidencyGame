package com.stevenlagoy.presidency.map.travel.vehicle

abstract class Vehicle(
    val modelName: String,
    val speed: Double, // Rate of travel
    val range: Double, // Distance which can be traveled without stopping
    val capacity: Int, // Number of passengers possible
    val costPerMile: Double,
)
