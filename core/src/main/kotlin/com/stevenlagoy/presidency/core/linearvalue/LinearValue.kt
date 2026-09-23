package com.stevenlagoy.presidency.core.linearvalue

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable

class LinearValue(
    min: Double,
    max: Double,
    var base: Double = min,
) : JSONSerializable<LinearValue> {

    var min: Double = min
        internal set

    var max: Double = max
        internal set

    val additiveModifiers: MutableList<Modifier> = mutableListOf()

    val multiplicativeModifiers: MutableList<Modifier> = mutableListOf()

    val currentValue: Double get() {
        pruneExpired()
        val addSum = additiveModifiers.sumOf { it.evaluate() }
        val multProduct = multiplicativeModifiers.fold(1.0) { acc, mod -> acc * mod.evaluate() }
        return base * multProduct + addSum
    }

    constructor(min: Int, max: Int, base: Int) : this(min.toDouble(), max.toDouble(), base.toDouble())

    init {
        require(max >= min) { "Max must be greater than min." }
        require(base in max..min) { "Base must be between min and max." }
    }

    fun pruneExpired() {
        additiveModifiers.removeAll { it.decayFunction.evaluate() <= 0 }
        multiplicativeModifiers.removeAll { it.decayFunction.evaluate() <= 0 }
    }

    fun shift(delta: Double) {
        base = (base + delta).coerceIn(min, max)
    }

    companion object {
        fun linearValueAtMax(min: Double, max: Double) = LinearValue(min, max, base = max)

        fun linearValueAtMin(min: Double, max: Double) = LinearValue(min, max, base = min)

        fun linearValueAt(min: Double, max: Double, fraction: Double): LinearValue {
            require(fraction in 0.0..1.0) { "Fraction must be between 0 and 1.0 but was $fraction" }
            return LinearValue(min, max, base = (max - min) * fraction)
        }
    }

    override fun toJson(): JSONObject = JSONObject(hashCode().toString(), listOf(
        JSONObject("min", min),
        JSONObject("max", max),
        JSONObject("base", base),
        JSONObject("additiveModifiers", additiveModifiers.map { it.toJson() }),
        JSONObject("multiplicativeModifiers", multiplicativeModifiers.map { it.toJson() }),
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        min = json.requireDouble("min")
        max = json.requireDouble("max")
        base = json.requireDouble("base")
        additiveModifiers.clear()
        additiveModifiers.addAll(json.requireArray("additiveModifiers").filterIsInstance<JSONObject>().map { Modifier(it) })
        multiplicativeModifiers.clear()
        multiplicativeModifiers.addAll(json.requireArray("multiplicativeModifiers").filterIsInstance<JSONObject>().map { Modifier(it) })
    }
}
