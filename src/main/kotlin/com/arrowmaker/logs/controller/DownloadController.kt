package com.arrowmaker.logs.controller

import com.arrowmaker.logs.serial.configuration.Parity
import com.arrowmaker.logs.serial.configuration.StopBit
import com.arrowmaker.logs.serial.model.Pages
import com.arrowmaker.logs.serial.utility.SerialUtils
import com.arrowmaker.logs.service.DownloadService
import tornadofx.*
import java.io.File

class DownloadController : Controller() {
    val ports = mutableListOf<String>().observable()
    val baudRates = DownloadService.baudrates.observable()
    val dataBits = DownloadService.databits.observable()
    val stopBits = DownloadService.stopBits.observable()
    val parities = DownloadService.parities.observable()

    init {
        updatePorts()
    }

    fun updatePorts() {
        val names = SerialUtils.portNames()
        ports.clear()
        ports.addAll(names)
    }

    fun download(port: String, baudRate: Int, dataBit: Int, stopBit: StopBit, parity: Parity, file: File? = null, callback: (Int, Int) -> Unit): Pages =
            DownloadService.download(port, baudRate, dataBit, stopBit.stopBits, parity.parity, file, callback)


    fun csv(file: File, pages: Pages) = DownloadService.saveCsv(file, pages)

    fun kml(file: File, pages: Pages) = DownloadService.saveKml(file, pages)

    fun gpx(file: File, pages: Pages) = DownloadService.saveGpx(file, pages)
}