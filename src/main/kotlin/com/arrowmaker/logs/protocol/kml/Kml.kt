package com.arrowmaker.logs.protocol.kml

import com.arrowmaker.logs.serial.model.Page
import com.arrowmaker.logs.serial.model.Pages
import com.arrowmaker.logs.serial.model.Record
import com.arrowmaker.logs.utility.UTC
import java.io.Writer
import java.time.LocalDateTime

data class Point(val utc: LocalDateTime, val lat: Double, val lon: Double, val alt: Double) {
    companion object {
        fun from(record: Record): Point = with(record) {
            return Point(utc, lat, lon, alt)
        }
    }
}

typealias PointList = List<Point>

object Points {
    fun from(pages: Pages): PointList {
        val points: MutableList<Point> = mutableListOf()

        pages.forEach {
            val eepromPoints = Points.from(it)
            points.addAll(eepromPoints)
        }

        return points.toList()
    }

    fun from(page: Page): PointList {
        val points: MutableList<Point> = mutableListOf()

        page.records.forEach {
            val point = Point.from(it)
            points.add(point)
        }
        return points.toList()
    }
}


object Kml {
    fun print(out: Writer, points: PointList) {
        val kml = Kml.parse(points)
        out.use {
            it.write(kml)
        }
    }

    private fun parse(pageMessages: PointList): String = StringBuilder(KML_HEADER).apply {
        //add timestamps to kml track
        pageMessages.forEach { point ->
            val timeStamp: String = timeStamp(point.utc)
            append(timeStamp)
        }
        //add coordinates
        pageMessages.forEach { point ->
            val coordinate = coordinate(point)
            append(coordinate)
        }

        append(KML_FOOTER)
    }.toString()
}

fun timeStamp(utc: LocalDateTime): String {
    val time = UTC.marshalUTC(utc)

    return "<when>$time</when>"
}

private fun coordinate(track: Point): String {
    val lat = track.lat
    val lon = track.lon
    val alt = track.alt

    return "<gx:coord>$lon $lat $alt</gx:coord>"
}

const val KML_HEADER = """<?xml version="1.0" encoding="UTF-8"?><kml xmlns="http://www.opengis.net/kml/2.2" xmlns:gx="http://www.google.com/kml/ext/2.2"><Folder><Placemark><gx:Point>"""
const val KML_FOOTER = """</gx:Point></Placemark></Folder></Document></kml>"""