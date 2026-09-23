package com.stevenlagoy.presidency.util

/* Temperature */

enum class TemperatureUnit(val freezingPoint: Double, val boilingPoint: Double, val symbol: String) {
    CELSIUS(0.0, 100.0, "°C"),
    FAHRENHEIT(32.0, 212.0, "°F"), // Fahrenheit is defined by the freezing point of brine
    KELVIN(273.15, 373.15, "K"),
    RANKINE(491.67, 671.67, "°R"),
    RØMER(7.5, 60.0, "°Rø"),
    RÉAUMUR(0.0, 80.0, "°Ré");
}

fun convertTemperature(temperature: Double, fromUnit: TemperatureUnit, toUnit: TemperatureUnit): Double {
    val scaleDifference = (fromUnit.boilingPoint - fromUnit.freezingPoint) / (toUnit.boilingPoint - toUnit.freezingPoint)
    return ((temperature - fromUnit.freezingPoint) * scaleDifference) + toUnit.freezingPoint
}

fun degreesFahrenheitToCelsius(degreesFahrenheit: Double): Double = convertTemperature(degreesFahrenheit, TemperatureUnit.FAHRENHEIT, TemperatureUnit.CELSIUS)

fun degreesCelsiusToFahrenheit(degreesCelsius: Double): Double = convertTemperature(degreesCelsius, TemperatureUnit.CELSIUS, TemperatureUnit.FAHRENHEIT)

fun degreesCelsiusToKelvin(degreesCelsius: Double): Double = convertTemperature(degreesCelsius, TemperatureUnit.CELSIUS, TemperatureUnit.KELVIN)

fun degreesFahrenheitToRankine(degreesFahrenheit: Double): Double = convertTemperature(degreesFahrenheit, TemperatureUnit.FAHRENHEIT, TemperatureUnit.RANKINE)

/* Length */

enum class LengthUnit(val cm: Double, val symbol: String) {
    CENTIMETER(1.0, "cm"),
    METER     (CENTIMETER.cm * 100, "m"),
    KILOMETER (METER.cm * 1000, "km"),
    INCH      (CENTIMETER.cm * 2.54, "in"),
    FOOT      (INCH.cm * 12, "ft"),
    YARD      (FOOT.cm * 3, "yd"),
    MILE      (FOOT.cm * 5280, "mi");
}

fun convertLength(length: Double, fromUnit: LengthUnit, toUnit: LengthUnit): Double = length * fromUnit.cm / toUnit.cm

fun milesToKilometers(miles: Double): Double = convertLength(miles, LengthUnit.MILE, LengthUnit.KILOMETER)

fun kilometersToMiles(kilometers: Double): Double = convertLength(kilometers, LengthUnit.KILOMETER, LengthUnit.MILE)

fun feetToMeters(feet: Double): Double = convertLength(feet, LengthUnit.FOOT, LengthUnit.METER)

fun metersToFeet(meters: Double): Double = convertLength(meters, LengthUnit.METER, LengthUnit.FOOT)

/* Weight & Mass */

enum class MassUnit(val kg: Double, val symbol: String) {
    KILOGRAM(1.0, "kg"),
    GRAM    (KILOGRAM.kg / 1000, "g"),
    POUND   (KILOGRAM.kg * 0.45359237, "lb"), // Avoirdupois Pound
    OUNCE   (POUND.kg / 16, "oz"),
    TON     (POUND.kg * 2000, "tn"); // US short ton
}

fun convertMass(mass: Double, fromUnit: MassUnit, toUnit: MassUnit): Double = mass * fromUnit.kg / toUnit.kg

fun poundsToKilograms(pounds: Double): Double = convertMass(pounds, MassUnit.POUND, MassUnit.KILOGRAM)

fun kilogramsToPounds(kilograms: Double): Double = convertMass(kilograms, MassUnit.KILOGRAM, MassUnit.POUND)

/* Volume & Capacity */

enum class VolumeUnit(val liters: Double, val symbol: String) {
    LITER (1.0, "L"),
    GALLON(LITER.liters * 3.785411784, "gal"), // US gallon
    QUART (GALLON.liters / 4, "qt"),
    PINT  (QUART.liters / 2, "pt"),
    CUP   (PINT.liters / 2, "c"),
    OUNCE (CUP.liters / 8, " fl oz");
}

fun convertVolume(volume: Double, fromUnit: VolumeUnit, toUnit: VolumeUnit): Double = volume * fromUnit.liters / toUnit.liters
