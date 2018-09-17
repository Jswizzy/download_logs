package com.arrowmaker.logs.protocol.gpx

import com.arrowmaker.logs.serial.model.Page
import com.arrowmaker.logs.serial.model.Pages
import io.jenetics.jpx.GPX
import java.time.ZoneOffset


object Gpx {
    fun print(out: String, gpx: GPX) {
        GPX.write(gpx, out)
    }

    fun from(page: Page, gpx: GPX.Builder): GPX.Builder {
        page.records.forEach { record ->
            gpx.addTrack { trackBuilder ->
                trackBuilder.addSegment { segment ->
                    segment.addPoint { wayPoint ->
                        with (record) {
                            wayPoint.lat(lat).lon(lon).ele(alt).time(utc.toInstant(ZoneOffset.UTC))
                        }
                    }
                }
            }
        }
        return gpx
    }

    fun from (pages: Pages): GPX {
        val gpx = GPX.builder()

        pages.forEach { page ->
            val g = Gpx.from(page, gpx)
        }
        return gpx.build()
    }
}