package com.stevenlagoy.presidency.map

data class HistoricalDistrict(
    val name: String,
    val provinces: List<HistoricalProvince>,
    val description: String,
)
