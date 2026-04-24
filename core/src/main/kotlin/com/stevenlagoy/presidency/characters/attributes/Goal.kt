package com.stevenlagoy.presidency.characters.attributes

class Goal(
    val prerequisites: MutableSet<Goal>,
    var priority: Double,
) {
}
