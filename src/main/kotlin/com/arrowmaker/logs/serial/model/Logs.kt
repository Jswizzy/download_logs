package com.arrowmaker.logs.serial.model

import com.arrowmaker.logs.utility.Coordinate.dmsToDd
import com.arrowmaker.logs.utility.Pressure
import com.arrowmaker.logs.utility.UTC
import okio.Buffer
import java.lang.Float.intBitsToFloat
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.time.LocalDate
import java.time.LocalDateTime

const val INFO_BLOCK_SIZE = 23


data class InfoBlock(val eepromSize: Int, val recordSize: Int) {
    companion object {
        fun from(buffer: Buffer): InfoBlock {
            buffer.skip(19)

            val eepromSizeBytes = buffer.readByteArray(3)

            val eepromSize = threeByteIntLe(eepromSizeBytes)

            val recordSize = buffer.readByte().toInt()
            return InfoBlock(eepromSize, recordSize)
        }
    }
}

const val START = 0xba.toByte()
const val END = 0x55.toByte()
const val PAGE_SIZE = 261 //256
const val EXTRA_INFO = 5
const val RECORD_SIZE = 19

typealias Pages = List<Page>


data class Page(val page: Short, val records: Records, val lrc: Int) {
    companion object {
        fun from(buffer: Buffer): Page {
            val hex = buffer.snapshot().substring(1, 259)
            val checksum = LRC.calculate(hex.toByteArray())

            println("Hex = ${hex.hex()}\nLRC = $checksum")

            val start = buffer.readByte()
            require(start == START)

            val pageNumber = buffer.readShortLe()

            val size = (PAGE_SIZE - EXTRA_INFO) / RECORD_SIZE

            val records = mutableListOf<Record>()
            for (i in 0 until size) {

                val record = Record.from(buffer)

                println(record)

                if (isValid(record)) {
                    records.add(record)
                }
            }

            buffer.skip(9)

            val lrc = buffer.readByte().toInt()

            val end = buffer.readByte()
            println("start: $start end: $end")
            require(end == END)

          return if (checksum == lrc) {
                Page(pageNumber, records.toList(), lrc)
            } else {
                Page(pageNumber, emptyList(), lrc)
            }
        }

        private fun isValid(record: Record): Boolean = with(record) {
            lat >= -90.0 && lat <= 90.0 && lon >= -180.0 && lon <= 180.0 && utc.isAfter(LocalDateTime.MIN) &&
                    alt >= -100.0 && alt <= 9000.0 && !(lat == 0.0 && lon == 0.0)
        }
    }
}

typealias Records = List<Record>


data class Record(val utc: LocalDateTime, val lat: Double, val lon: Double, val alt: Double, val bar: Double) {
    companion object {
        fun from(buffer: Buffer): Record {
            val utc = intBitsToFloat(buffer.readInt()).toDouble()
            val lat = intBitsToFloat(buffer.readInt()).toDouble()
            val lon = intBitsToFloat(buffer.readInt()).toDouble()
            val alt = intBitsToFloat(buffer.readInt()).toDouble()
            val bar = threeByteIntLe(buffer.readByteArray(3))

            return Record(
                    try {LocalDateTime.of(LocalDate.now(), UTC.parseTime(utc)) } catch (e: Exception) {LocalDateTime.MIN},
                    dmsToDd(lat),
                    dmsToDd(lon),
                    alt,
                    Pressure.from(bar))
        }
    }
}

fun threeByteIntLe(byteArray: ByteArray): Int {
    val buffer = ByteBuffer.allocate(4)
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    buffer.put(byteArray)
    buffer.put(0x00)
    buffer.rewind()
    return buffer.int
}

fun threeByteInt(byteArray: ByteArray): Int {
    val buffer = ByteBuffer.allocate(4)
    buffer.order(ByteOrder.BIG_ENDIAN)
    buffer.put(byteArray)
    buffer.rewind()
    return buffer.int
}