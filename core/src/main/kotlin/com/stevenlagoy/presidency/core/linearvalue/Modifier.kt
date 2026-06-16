package com.stevenlagoy.presidency.core.linearvalue

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic

class Modifier(
    baseEffect: Double = 0.0,
    decayFunction: DecayFunction = DecayFunction { 1.0 },
) : Jsonic<Modifier> {

    var baseEffect: Double = baseEffect
        internal set

    var decayFunction: DecayFunction = decayFunction
        internal set

    constructor(json: JSONObject) : this() {
        fromJson(json)
    }

    fun evaluate(): Double = baseEffect * decayFunction.evaluate()

    override fun toJson() = JSONObject(hashCode().toString(), listOf(
        JSONObject("baseEffect", baseEffect),
        JSONObject("decayFunction", decayFunction.toString()), // TODO Idk if this works
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        baseEffect = json.get("baseEffect", Number::class.java).toDouble()
        decayFunction = json.get("decayFunction", DecayFunction::class.java)
    }
}
