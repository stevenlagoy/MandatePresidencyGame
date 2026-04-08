package com.stevenlagoy.presidency.map

data class University(
    val location: Municipality,
    val fullName: String,
    val commonName: String,
    var graduationSize: Int,
)
