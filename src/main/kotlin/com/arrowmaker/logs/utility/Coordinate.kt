package com.arrowmaker.logs.utility

import java.lang.Math.abs
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Coordinate utilities for dealing with GPS coordinates
 */
object Coordinate {
    /** converts NMEA format to Degrees decimal
     *
     * NMEA format is ddmm.mmmm, n/s (d)ddmm.mmmm, e/w
     *
     * */
    fun dmsToDd(dms: Double): Double {
        // setup decimal formatter
        val decimalFormat = DecimalFormat("#.######")
        decimalFormat.roundingMode = RoundingMode.CEILING

        val absDms = abs(dms)

        val deg = absDms / 100
        val minutes = (absDms % 100) / 60

        val total = deg.toInt() + minutes
        val result = if (dms < 0) total * -1 else total

        return decimalFormat.format(result).toDouble()
    }
}