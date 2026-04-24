package com.stevenlagoy.presidency.util

import java.time.Period

fun Period.toTotalDays(): Long = (this.toTotalMonths() * (365.25/12) + this.days).toLong()
