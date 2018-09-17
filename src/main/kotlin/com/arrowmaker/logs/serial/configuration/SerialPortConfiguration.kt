package com.arrowmaker.logs.serial.configuration

import jssc.SerialPort.*


object SerialPortConfiguration {
    const val PORT = "COM1"
    const val BAUD_RATE = BAUDRATE_115200
    const val PARITY = PARITY_NONE
    const val DATA_BITS = DATABITS_8
    const val STOP_BITS = STOPBITS_1

    val baudrates = listOf(BAUDRATE_110, BAUDRATE_115200,
            BAUDRATE_1200, BAUDRATE_14400, BAUDRATE_19200, BAUDRATE_38400,
            BAUDRATE_4800, BAUDRATE_300, BAUDRATE_57600, BAUDRATE_600, BAUDRATE_9600)

    val parity = listOf(
            Parity(PARITY_NONE, "NONE"),
            Parity(PARITY_EVEN, "EVEN"),
            Parity(PARITY_ODD, "ODD"))

    val dataBits = listOf(DATABITS_8, DATABITS_7, DATABITS_6, DATABITS_5)

    val stopBits = listOf(
            StopBit(STOPBITS_1, "ONE"),
            StopBit(STOPBITS_1_5, "ONE_HALF"),
            StopBit(STOPBITS_2, "TWO"))
}

data class Parity(val parity: Int, val string: String) {
    override fun toString(): String = string
}

data class StopBit(val stopBits: Int, val string: String) {
    override fun toString(): String  = string
}