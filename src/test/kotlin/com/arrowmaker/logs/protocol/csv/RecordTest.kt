package com.arrowmaker.logs.protocol.csv

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.StringWriter
import java.time.LocalDateTime

val records = listOf(
        CSVRecord(LocalDateTime.of(18,12,25,23,30).toString(), 101.0, 12.0, 4.0, 6.0),
        CSVRecord(LocalDateTime.of(18,12,25,23,30).toString(), 102.0, 14.0, 6.0, 6.0))

const val expected = """utc,lat,lon,alt,bar
0018-12-25T23:30,101.0,12.0,4.0,6.0
0018-12-25T23:30,102.0,14.0,6.0,6.0"""

class RecordTest {
    @Test
    fun `Record prints csv`() {
        val out = StringWriter()

        CSVRecords.print(out, records)

        assertEquals(expected, out.toString().trim())
    }
}