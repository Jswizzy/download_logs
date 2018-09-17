package com.arrowmaker.logs.serial.configuration

object ResponseCodes {
    // Response codes (see F50x_Master_Interface.h)
    const val SRC_RSP_UNKNOWN_CMD = 0x73.toByte()
    const val SRC_CMD_GET_EEPROM = 0x90.toByte()
}