package com.arrowmaker.logs.serial.service

import com.arrowmaker.logs.serial.configuration.ResponseCodes
import com.arrowmaker.logs.serial.configuration.SerialPortConfiguration
import com.arrowmaker.logs.serial.model.*
import jssc.SerialPort
import okio.Buffer
import java.io.Writer

class SerialPortService(port: String = SerialPortConfiguration.PORT, private val progressCallBack: (current: Int, total: Int) -> Unit) : AutoCloseable, SerialPort(port) {
    fun call(out: Writer? = null): Pages {
        val readBytes = readBytes(INFO_BLOCK_SIZE)

        val infoBlock = InfoBlock.from(readBytes.toBuffer())

        requestDownload()

        val pages = mutableListOf<Page>()

        val rawData =
                if (out != null) {
                    StringBuilder()
                } else {
                    null
                }

        val size = infoBlock.eepromSize // 524,288
        val numPages = size / (PAGE_SIZE - EXTRA_INFO) // 2048

        for (i in 0 until 30) { //numPages) {

            val pageBuffer = readBytes(PAGE_SIZE).toBuffer()

            val hex = pageBuffer.snapshot().hex()
            rawData?.append(hex)

            progressCallBack(i, numPages)

            try {
                val page = Page.from(pageBuffer)
                //println("Page: $page")
                pages.add(page)
            } catch (e: Exception) {
                println(e)
                continue
            }
        }

        // save raw data
        out?.use {
            it.write(rawData.toString())
        }

        return pages.toList()
    }

    private fun requestDownload(): Boolean {
        writeByte(ResponseCodes.SRC_RSP_UNKNOWN_CMD)
        return writeByte(ResponseCodes.SRC_CMD_GET_EEPROM)
    }

    override fun close() {
        if (isOpened) {
            closePort()
        }
    }

    companion object {
        /**
         * Factory that creates a SerialPort Object for the Tracker USB serial port
         */
        fun getSocket(
                port: String = SerialPortConfiguration.PORT,
                baudRate: Int = SerialPortConfiguration.BAUD_RATE,
                dataBits: Int = SerialPortConfiguration.DATA_BITS,
                parity: Int = SerialPortConfiguration.PARITY,
                stopBits: Int = SerialPortConfiguration.STOP_BITS,
                observer: (Int, Int) -> Unit): SerialPortService {

            return SerialPortService(port, observer).apply {
                openPort()
                setParams(baudRate, dataBits, stopBits, parity)
            }
        }
    }
}

fun ByteArray.toBuffer(): Buffer = Buffer().write(this)