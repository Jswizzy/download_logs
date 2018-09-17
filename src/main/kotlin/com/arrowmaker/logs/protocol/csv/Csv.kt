package com.arrowmaker.logs.protocol.csv

import com.arrowmaker.logs.serial.model.Page
import com.arrowmaker.logs.serial.model.Pages
import com.arrowmaker.logs.serial.model.Record
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import java.io.Writer

typealias RecordList = List<CSVRecord>


object CSVRecords {
    fun print(out: Writer, records: RecordList) {
        val mapper = CsvMapper()
        val schema = mapper.schemaFor(CSVRecord::class.java).withHeader()
        val writer = mapper.writer(schema)

        writer.writeValue(out, records)
    }

    fun from(page: Page): RecordList =
            page.records.asSequence().map {
                CSVRecord.from(it)
            }.toList()

    fun from(pages: Pages): RecordList =
        pages.flatMap {
            from(it)
        }.toList()
}

@JsonPropertyOrder("utc", "lat", "lon", "alt", "bar")
data class CSVRecord(val utc: String, val lat: Double, val lon: Double, val alt: Double, val bar: Double) {
    companion object {
        fun from(record: Record): CSVRecord = with(record) {
            val utc = utc.toString()
            val lat = lat
            val lon = lon
            val alt = alt
            val bar = bar

            CSVRecord(utc, lat, lon, alt, bar)
        }
    }
}

