package com.arrowmaker.logs.service

import com.arrowmaker.logs.protocol.csv.CSVRecords
import com.arrowmaker.logs.protocol.gpx.Gpx
import com.arrowmaker.logs.protocol.kml.Kml
import com.arrowmaker.logs.protocol.kml.Points
import com.arrowmaker.logs.serial.configuration.SerialPortConfiguration
import com.arrowmaker.logs.serial.model.Pages
import com.arrowmaker.logs.serial.service.SerialPortService
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

object DownloadService {
    val baudrates = SerialPortConfiguration.baudrates.sorted()
    val stopBits = SerialPortConfiguration.stopBits
    val parities = SerialPortConfiguration.parity
    val databits = SerialPortConfiguration.dataBits

    fun download(port: String, baudRate: Int, dataBit: Int, stopBit: Int, parity: Int, file: File? = null, callback: (Int, Int) -> Unit): Pages {
        println("Starting Tasks")
        val serialPortService = SerialPortService.getSocket(port, baudRate, dataBit, parity, stopBit, callback)

        return if (file == null) {
            serialPortService.call()
        } else {
            val newFile = File(file.path + ".hex")
            val out = BufferedWriter(FileWriter(newFile))

            serialPortService.call(out)
        }
    }


    fun saveCsv(file: File, pages: Pages) {
        val newFile = File(file.path + ".csv")

        val out = BufferedWriter(FileWriter(newFile))

        val records = CSVRecords.from(pages)
        CSVRecords.print(out, records)
    }

    fun saveKml(file: File, pages: Pages) {
        val newFile = File(file.path + ".kml")

        val out = BufferedWriter(FileWriter(newFile))

        val points = Points.from(pages)
        Kml.print(out, points)
    }

    fun saveGpx(file: File, pages: Pages) {
        val newFile = file.path + ".gpx"

        val gpx = Gpx.from(pages)

        Gpx.print(newFile, gpx)
    }
}