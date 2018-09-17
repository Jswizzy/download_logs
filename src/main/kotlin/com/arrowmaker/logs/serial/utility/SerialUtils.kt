package com.arrowmaker.logs.serial.utility

import jssc.SerialPortList


object SerialUtils {
    fun portNames(): Array<out String> = SerialPortList.getPortNames()
}