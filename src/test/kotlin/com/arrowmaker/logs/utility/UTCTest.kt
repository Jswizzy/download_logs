package com.arrowmaker.logs.utility

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class UTCTest {
    @Test
    fun `marshal date`() {
        val utcString = dateTime.toUTCString()
        assertEquals(utcFormattedString, utcString)
    }

    @Test
    fun `unmarshal date`() {
        val date = utcFormattedString.toUTC()
        assertEquals(dateTime, date)
    }

    @Test
    fun `nmea time to LocalDateTime`() {
        val expected = LocalDateTime.of(LocalDate.now(), LocalTime.of(12,35,19))

        val utc = UTC.unMarshalTime("123519")

        assertEquals(expected, utc)
    }

    companion object {
        val dateTime: LocalDateTime = LocalDateTime.of(2014, 5, 12, 18, 42, 14, 580000000)
        const val utcFormattedString = "2014-05-12T18:42:14.58Z"
    }
}