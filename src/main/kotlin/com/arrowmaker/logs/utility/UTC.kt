package com.arrowmaker.logs.utility

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

object UTC {
    private val UTC_FORMAT: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS'Z'") }
    private val TIME_FORMAT: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern("HHmmss") }

    /**
     * converts a string in the format 'yyyy-MM-ddTHH:mm:ssZ' to a LocalDateTime object
     */
    fun parseTime(time: String): LocalDateTime = LocalDateTime.parse(time, UTC_FORMAT)

    /**
     * parses a string in the format HHMmmss to a LocalTime Object
     */
    fun parseTime(time: Double): LocalTime {
        val timeString =
                time.toInt()
                        .absoluteValue.toString()
                        .padStart(6, '0')

        return LocalTime.parse(timeString, TIME_FORMAT)
    }

    fun unMarshalTime(dateTime: String): LocalDateTime {
        return LocalDateTime.of(LocalDate.now(), LocalTime.parse(dateTime, TIME_FORMAT))
    }

    /**
     * converts a LocalDateTime object to a string in the format 'yyyy-MM-ddTHH:mm:ssZ'
     */
    fun marshalUTC(time: LocalDateTime): String = time.format(UTC_FORMAT)

    /**
     * converts a LocalTime to a string in the format HHmmss
     */
    fun marshalTime(localTime: LocalTime): String = localTime.format(TIME_FORMAT)
}

/**
 * converts a string in the format 'yyyy-MM-ddTHH:mm:ssZ' to a LocalDateTime object
 */
fun String.toUTC(): LocalDateTime = UTC.parseTime(this)

/**
 * converts a LocalDateTime object to a string in the format 'yyyy-MM-ddTHH:mm:ssZ'
 */
fun LocalDateTime.toUTCString(): String = UTC.marshalUTC(this)