package com.arrowmaker.logs.utility

import kotlin.math.pow

private const val SCALING_FACTOR = 4096.0
private const val sealLevelPressure = 1013.25


object Pressure {
    fun from(value: Int): Double {
        val atmosphericPressure = value / SCALING_FACTOR

        return (((sealLevelPressure / atmosphericPressure).pow(1/5.257) - 1.0) * 288.15) / 0.0065
    }
}