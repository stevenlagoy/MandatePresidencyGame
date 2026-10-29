package com.stevenlagoy.presidency.citizens.attributes

class Goal(
    val prerequisites: MutableSet<Goal>,
    var priority: Double,
)
