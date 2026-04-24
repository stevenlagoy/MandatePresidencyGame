package com.stevenlagoy.presidency.characters.attributes.finances

abstract class Liability(
    var liabilityType: LiabilityType,
    var value: Double = 0.0,
) {
}
